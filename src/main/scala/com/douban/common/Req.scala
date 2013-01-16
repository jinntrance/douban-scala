package com.douban.common

import com.douban.models.Bean
import java.io._
import java.net.HttpURLConnection._
import java.net.{URLDecoder, HttpURLConnection, URL}
import java.text.SimpleDateFormat
import java.util.Random
import net.liftweb.json._
import scala.concurrent._
import duration._


/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/21/12 8:38 PM
 * @version 1.0
 */
class Req

object Req {
  val timeout = 20 * 1000
  val duration=Duration(2,SECONDS)
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
   * @tparam RESULT 返回的类型
   * @return
   */
  def post[RESULT: Manifest](url: String, request: Bean): RESULT = {
    val c: HttpURLConnection = postData(url, request)
    parseJSON[RESULT](c)
  }

  /**
   * 只判断状态码，不读取数据
   * @param request 参数对象
   * @return
   */
  def postNoResult(url: String, request: Bean): Boolean = {
    val c = postData(url, request)
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
  def put[RESULT: Manifest](url: String, request: Bean): RESULT = {
    val c: HttpURLConnection = putData(url, request)
    parseJSON[RESULT](c)
  }

  /**
   * 只判断状态码，不读取数据
   * @param request 参数Bean
   * @return
   */
  def putNoResult(url: String, request: Bean): Boolean = {
    val c = putData(url, request)
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

  private def connect(c: HttpURLConnection, request: Bean, authorized: Boolean = true) {
    c.setUseCaches(true)
    c.setConnectTimeout(timeout)
    c.setReadTimeout(timeout)
    //Android 2.3及以后的HttpConnectionUrl自动使用gzip，故此处就不再添加
    c.setRequestProperty("Connection", "Keep-Alive")
    c.setRequestProperty("Keep-Alive", s"timeout=$persistenceTimeout")
    //添加认证的access token
    if (authorized)
      c.setRequestProperty("Authorization", s"Bearer ${Auth.access_token}")
    c.setRequestProperty("Charset", ENCODING)
    println(c.getRequestMethod + "ing " + URLDecoder.decode(c.getURL.toString, ENCODING))
    if ((c.getRequestMethod == POST || c.getRequestMethod == PUT) && null != request) {
      if (request.files.size == 0) {
        c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        val out = new BufferedOutputStream(c.getOutputStream)
        val paras = request.toParas
        println("request body-->" + URLDecoder.decode(paras, ENCODING))
        out.write(paras.getBytes(ENCODING))
        out.flush()
        out.close()
      } else {
        val b = genBoundary
        c.setRequestProperty("Content-Type", s"multipart/form-data;boundary=$b")
        println(s"request body with boundary-->$b")
        val out = new BufferedOutputStream(c.getOutputStream)
        upload(b, out, Extraction.decompose(request))
        request.files.foreach {
          case (k, v) => uploadFile(b, out, k, v, withoutFile = false)
        }
        val boundaryString = s"\r\n--$b--\r\n"
        out.write(boundaryString.getBytes(ENCODING))
        out.flush()
        out.close()
        print(boundaryString)
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
  def parseJSON[R: Manifest](c: HttpURLConnection): R ={
      val v: JsonAST.JValue = JsonParser.parse(getResponse(
        if (succeed(c.getResponseCode)) c.getInputStream else c.getErrorStream
      ))
      println(s"response code-->${c.getResponseCode}")
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
    println(content.result())
    content.result()
  }

  private def postData(url: String, request: Bean): HttpURLConnection = {
    val c: HttpURLConnection = getConnection(url)
    c.setDoOutput(true)
    c.setRequestMethod(POST)
    this.connect(c, request)
    c
  }

  private def putData(url: String, request: Bean): HttpURLConnection = {
    val c: HttpURLConnection = getConnection(url)
    c.setDoOutput(true)
    c.setRequestMethod(PUT)
    this.connect(c, request)
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
    for {JField(k, vs) <- json
    } vs match {
      case JString(_) | JInt(_) | JDouble(_) | JBool(_) => uploadFile(boundary, out, k, vs.extract[String])
      case _ =>
    }
  }

  private def uploadFile(boundary: String, out: BufferedOutputStream, key: String, value: String, withoutFile: Boolean = true) {
    val k = "\"" + key + "\""
    val lineEnd = "\r\n" //每一数据用lineEnd分开
    val boundaryString = lineEnd + "--" + boundary + lineEnd
    if (withoutFile) {
      //there must be no extra space chars in s string
      val s = s"${boundaryString}Content-Disposition:form-data;name=$k$lineEnd$lineEnd$value"
      print(s)
      out.write(s.getBytes(ENCODING))
    }
    else {
      val v = "\"" + value + "\""
      val s = s"${boundaryString}Content-Disposition:form-data;name=$k;filename=$v${lineEnd}Content-Type: image/jpg$lineEnd$lineEnd"
      print(s)
      out.write(s.getBytes(ENCODING))
      val file = new BufferedInputStream(new FileInputStream(value))
      val buf = new Array[Byte](1024)
      Stream.continually(file.read(buf)).takeWhile(_ != -1)
        .foreach(out.write(buf, 0, _))
    }

  }

  private def genBoundary: String = {
    val multipartChars = "_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray
    val buffer = new StringBuilder
    val rand = new Random()
    for (i <- 1 to 8) buffer.append(multipartChars.charAt(rand.nextInt(multipartChars.length)))
    buffer.result()
  }
}
