package com.douban.common

import java.net.{URLDecoder, HttpURLConnection, URL}
import java.net.HttpURLConnection._
import java.io._
import net.liftweb.json._
import com.douban.models.Bean
import java.text.SimpleDateFormat
import net.liftweb.json.JValue


/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/21/12 8:38 PM
 * @version 1.0
 */
class Req

object Req {
  val timeout = 10 * 1000
  //10 seconds
  val persistenceTimeout = 10 * 60
  //10 minutes
  val PUT = "PUT"
  val POST = "POST"
  val GET = "GET"
  val DELETE = "DELETE"
  val ENCODING = "UTF-8"
  implicit val formats = new DefaultFormats {
    override def dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  }

  /**
   *
   * @param request 参数Bean
   * @param withFile 是否传文件
   * @tparam RESULT 返回的类型
   * @return
   */
  def post[RESULT: Manifest](url: String, request: Bean, withFile: Boolean = false): RESULT = {
    val c: HttpURLConnection = postData(url, request, withFile)
    parseJSON[RESULT](c)
  }

  /**
   * 只判断状态码，不读取数据
   * @param request 参数对象
   * @return
   */
  def postNoResult(url: String, request: Bean, withFile: Boolean = false): Boolean = {
    val c = postData(url, request, withFile)
    val code = c.getResponseCode
    if (!succeed(code)) parseJSON(c)
    c.disconnect()
    succeed(code)
  }

  /**
   *
   * @param secured 是否使用https,默认不用
   * @tparam RESULT 返回结果类型
   * @return
   */
  def get[RESULT: Manifest](url: String, secured: Boolean = false): RESULT = {
    var newUrl = if (secured) url else url.replace("https://", "http://")
    newUrl = addApiKey(newUrl) //添加API key，增加次数
    val c = getData(newUrl, secured)
    parseJSON[RESULT](c)
  }

  /**
   *
   * @param request 参数Bean
   * @return RESULT put成功后的数据
   */
  def put[RESULT: Manifest](url: String, request: Bean, withFile: Boolean = false): RESULT = {
    val c: HttpURLConnection = putData(url, request, withFile)
    parseJSON[RESULT](c)
  }

  /**
   * 只判断状态码，不读取数据
   * @param request 参数Bean
   * @return
   */
  def putNoResult(url: String, request: Bean, withFile: Boolean = false): Boolean = {
    val c = putData(url, request, withFile)
    val code = c.getResponseCode
    if (!succeed(code)) parseJSON(c)
    c.disconnect()
    succeed(code)
  }

  def delete(url: String): Boolean = {
    val c = deleteData(url)
    val code = c.getResponseCode
    if (!succeed(code)) parseJSON(c)
    c.disconnect()
    succeed(code)
  }

  private def connect(c: HttpURLConnection, request: Bean, authorized: Boolean = true, withFile: Boolean = false) {
    c.setUseCaches(true)
    c.setConnectTimeout(timeout)
    c.setReadTimeout(timeout)
    //Android 2.3及以后的HttpConnectionUrl自动使用gzip，故此处就不再添加
    c.setRequestProperty("Connection", "Keep-Alive")
    c.setRequestProperty("Keep-Alive", "timeout=" + persistenceTimeout)
    //添加认证的access token
    if (authorized)
      c.setRequestProperty("Authorization", "Bearer " + Auth.access_token)
    c.setRequestProperty("Charset", ENCODING)
    println(c.getRequestMethod + "ing " + URLDecoder.decode(c.getURL.toString, ENCODING))
    if ((c.getRequestMethod == POST || c.getRequestMethod == PUT) && null != request) {
      val out = new BufferedOutputStream(c.getOutputStream)
      if (withFile) {
        val b = genBoundary
        c.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + b)
        upload(b, out, Extraction.decompose(request))
        out.write(b.getBytes(ENCODING))
      }
      else {
        c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        val paras = request.toParas
        println("request body-->" + URLDecoder.decode(paras, ENCODING))
        out.write(paras.getBytes(ENCODING))
        out.flush()
        out.close()
      }
    }
    c.connect()
  }

  /**
   * 解析返回数据，出错则抛出异常并封装进 Error
   * @tparam R 返回值类型
   * @see com.douban.common.Error
   * @return
   *
   */
  def parseJSON[R: Manifest](c: HttpURLConnection): R = {
    val v: JsonAST.JValue = JsonParser.parse(getResponse(
      if (succeed(c.getResponseCode)) c.getInputStream else c.getErrorStream
    ))
    println("response code-->" + c.getResponseCode)
    c.disconnect()
    println(pretty(render(v)))
    if (succeed(c.getResponseCode)) v.extract[R] else throw new DoubanException(v.extract[Error])
  }

  /**
   * 把返回结果读出为String
   * @return
   */
  private def getResponse(inputStream: InputStream): String = {
    val reader = new BufferedReader(new InputStreamReader(inputStream))
    val content = new StringBuilder
    var line = reader.readLine()
    while (null != line) {
      content.append(line)
      line = reader.readLine()
    }
    content.result()
  }

  private def postData(url: String, request: Bean, withFile: Boolean): HttpURLConnection = {
    val c: HttpURLConnection = getConnection(url)
    c.setDoOutput(true)
    c.setRequestMethod(POST)
    this.connect(c, request, withFile = withFile)
    c
  }

  private def putData(url: String, request: Bean, withFile: Boolean = false): HttpURLConnection = {
    val c: HttpURLConnection = getConnection(url)
    c.setDoOutput(true)
    c.setRequestMethod(PUT)
    this.connect(c, request, withFile = withFile)
    c
  }

  private def deleteData(url: String): HttpURLConnection = {
    val c: HttpURLConnection = getConnection(url)
    c.setRequestMethod(DELETE)
    this.connect(c, null)
    c
  }

  private def getData(url: String, authorized: Boolean): HttpURLConnection = {
    val c: HttpURLConnection = getConnection(url)
    this.connect(c, null, authorized = authorized)
    c
  }

  /**
   * 判斷狀態碼，201-203都算成功
   */
  private def succeed(code: Int): Boolean = {
    code >= HTTP_OK && code <= HTTP_PARTIAL
  }

  private def addApiKey(url: String) = {
    if (-1 == url.indexOf('?')) url + "?" + Auth.addApiKey
    else url + "&" + Auth.addApiKey
  }

  private def getConnection(url: String) = new URL(url).openConnection().asInstanceOf[HttpURLConnection]

  private def upload(boundary: String, out: BufferedOutputStream, json: JValue) {
    for {JField(k, v) <- json
    } v match {
      case JObject(List()) | JArray(List()) =>
      case JObject(List(fields)) => upload(boundary, out, fields.value)
      case JObject(List(values)) => {
        var i = 0
        for {
          v: JValue <- values} {
          i += 1 //TODO 上传图片是否都是1，2，3，4....?
          uploadFile(boundary, out, i.toString, v.extract[String], withoutFile = false)
        }
      }
      case v: JValue => uploadFile(boundary, out, k, json.\(k).extract[String])
    }
  }

  private def uploadFile(boundary: String, out: BufferedOutputStream, key: String, value: String, withoutFile: Boolean = true) {
    val lineEnd = "\r\n" //没个数据用lineEnd分开
    if (withoutFile) {
      val s = "--" + boundary + lineEnd + "Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd + value + lineEnd
      out.write(s.getBytes(ENCODING))
    }

    else {
      val s = "--" + boundary + lineEnd + "Content-Disposition: form-data;Content-Type:application/octet-stream; name=\"" + key + "\";filename=\"" + value + "\"" + lineEnd
      out.write(s.getBytes(ENCODING))
      val file = new BufferedInputStream(new FileInputStream(value))
      val buf = new Array[Byte](1024)
      Stream.continually(file.read(buf))
        .takeWhile(_ != -1)
        .foreach(out.write(buf, 0, _))
      out.write(lineEnd.getBytes(ENCODING))
    }

  }

  def genBoundary: String = {
    "" //TODO gennarate
  }
}
