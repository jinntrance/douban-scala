package com.douban.common

import com.douban.models.Bean
import java.io._
import java.net.HttpURLConnection._
import java.net.{URLDecoder, HttpURLConnection, URL}
import java.util.Random
import com.google.gson.{JsonElement, GsonBuilder}
import com.google.gson.reflect.TypeToken
import scala.reflect.ClassTag
import scala.reflect.classTag
import collection.JavaConverters._
import scala.annotation._, elidable._


/**
 * Copyright by <a href="http://www.josephjctang.com"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/21/12 8:38 PM
 * @version 1.0
 */
object Req {
  var accessToken = ""
  var apiKey = Auth.api_key
  //20 seconds
  @inline val timeout = 20 * 1000
  //10 minutes
  @inline val persistenceTimeout = 10 * 60
  @inline val PUT = "PUT"
  @inline val POST = "POST"
  @inline val GET = "GET"
  @inline val DELETE = "DELETE"
  @inline val ENCODING = "UTF-8"
  val emptyJSON = """{}"""
  lazy val g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()


  def init(accessToken: String, apiKey: String = Auth.api_key, scope: String = "") = {
    this.accessToken = accessToken
    this.apiKey = apiKey
    Auth.scope = scope
    this
  }

  /**
   *
   * @param request 参数Bean
   * @param withResult 是否读取返回数据，默认读取 ，不读取数据时返回None为成功
   * @tparam RESULT 返回的类型。不读取数据时返回None为成功，抛出异常为读取数据不成功
   * @return
   */
  def post[RESULT: Manifest](url: String, request: Bean, withResult: Boolean = true): Option[RESULT] = {
    val c: HttpURLConnection = postData(url, request)
    if (withResult) Some(parseJSON[RESULT](c))
    else {
      if (!succeed(c.getResponseCode)) parseJSON(c)
      c.disconnect()
      None
    }
  }

  /**
   *
   * @param secured 是否使用https,默认不用
   * @tparam RESULT 返回结果类型
   * @return
   */
  def get[RESULT: Manifest](url: String, secured: Boolean = false): RESULT = {
    val newUrl = if (secured) url else addApiKey(url.replace("https://", "http://")) //添加API key，增加次数
    val c = getData(newUrl, secured)
    parseJSON[RESULT](c)
  }

  /**
   *
   * @param request 参数Bean
   * @param withResult 是否读取返回数据，默认读取,不读取数据时返回null为成功
   * @return RESULT put成功后的数据，不读取数据时返回null为成功，，抛出异常为读取数据不成功
   */
  def put[RESULT: Manifest](url: String, request: Bean, withResult: Boolean = true): Option[RESULT] = {
    val c: HttpURLConnection = putData(url, request)
    if (withResult) Some(parseJSON[RESULT](c))
    else {
      if (!succeed(c.getResponseCode)) parseJSON(c)
      c.disconnect()
      None
    }
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
      c.setRequestProperty("Authorization", s"Bearer $accessToken")
    c.setRequestProperty("Charset", ENCODING)

    if ((c.getRequestMethod == POST || c.getRequestMethod == PUT) && null != request) {
      if (request.files.size == 0) {
        c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        val out = new BufferedOutputStream(c.getOutputStream)
        val paras = request.toParas
        debug("request body-->" + URLDecoder.decode(paras, ENCODING))
        out.write(paras.getBytes(ENCODING))
        out.flush()
        out.close()
      } else {
        val b = genBoundary
        c.setRequestProperty("Content-Type", s"multipart/form-data;boundary=$b")
        debug(s"request body with boundary-->$b")
        val out = new BufferedOutputStream(c.getOutputStream)
        upload(b, out, g.toJsonTree(request))
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
    debug(c.getRequestMethod + "ing " + URLDecoder.decode(c.getURL.toString, ENCODING))
    debug("all header-->")
    debug(c.getHeaderFields)
  }

  /**
   * 解析返回数据，出错则抛出异常并封装进 Error
   * @tparam R 返回值类型
   * @see com.douban.common.Error
   * @return
   *
   */
  def parseJSON[R: ClassTag](c: HttpURLConnection): R = {
    val v = getResponse(
      if (succeed(c.getResponseCode)) c.getInputStream else c.getErrorStream
    )
    debug(s"response code-->${c.getResponseCode}")
    c.disconnect()
    val t = new TypeToken[ClassTag[R]]() {}.getType
    if (succeed(c.getResponseCode)) g.fromJson(v, classTag[R].runtimeClass).asInstanceOf[R] else throw new DoubanException(g.fromJson(v, classOf[Error]))
  }

  /**
   * 把返回结果读出为String
   * @return
   */
  @inline private def getResponse(inputStream: InputStream): String = {
    val reader = new BufferedReader(new InputStreamReader(inputStream))
    val content = new StringBuilder
    var line = reader.readLine()
    while (null != line) {
      content.append(line)
      line = reader.readLine()
    }
    debug(content.result())
    content.result()
  }

  @inline private def postData(url: String, request: Bean): HttpURLConnection = {
    val c: HttpURLConnection = getConnection(url)
    c.setDoOutput(true)
    c.setRequestMethod(POST)
    this.connect(c, request)
    c
  }

  @inline private def putData(url: String, request: Bean): HttpURLConnection = {
    val c: HttpURLConnection = getConnection(url)
    c.setDoOutput(true)
    c.setRequestMethod(PUT)
    this.connect(c, request)
    c
  }

  @inline private def deleteData(url: String): HttpURLConnection = {
    val c: HttpURLConnection = getConnection(url)
    c.setRequestMethod(DELETE)
    this.connect(c, null)
    c
  }

  private def getData(url: String, authorized: Boolean): HttpURLConnection = {
    val c: HttpURLConnection = getConnection(url)
    this.connect(c, null, authorized)
    c
  }

  /**
   * 判斷狀態碼，201-203都算成功
   */
  @inline private def succeed(code: Int): Boolean = {
    code >= HTTP_OK && code <= HTTP_PARTIAL
  }

  @inline private def addApiKey(url: String) = {
    val key = "apikey=" + apiKey
    if (-1 == url.indexOf('?')) url + "?" + key
    else url + "&" + key
  }

  @inline private def getConnection(url: String) = new URL(url).openConnection().asInstanceOf[HttpURLConnection]

  private def upload(boundary: String, out: BufferedOutputStream, json: JsonElement) {
    json.getAsJsonObject.entrySet().asScala.foreach(e => {
      if (e.getValue.isJsonPrimitive) uploadFile(boundary, out, e.getKey, e.getValue.getAsString)
      else if (e.getValue.isJsonObject) upload(boundary, out, e.getValue)
    })

  }

  private def uploadFile(boundary: String, out: BufferedOutputStream, key: String, value: String, withoutFile: Boolean = true) {
    val k = "\"" + key.trim + "\""
    val lineEnd = "\r\n" //每一数据用lineEnd分开
    val boundaryString = lineEnd + "--" + boundary + lineEnd
    if (withoutFile) {
      //there must be no extra space chars in s string
      val s = s"${boundaryString}Content-Disposition:form-data;name=$k$lineEnd$lineEnd${value.trim}"
      print(s)
      out.write(s.getBytes(ENCODING))
    }
    else {
      val v = "\"" + value.trim + "\""
      val contentType = "Content-Type: image/" + value.substring(value.lastIndexOf('.') + 1).trim.toLowerCase
      val s = s"${boundaryString}Content-Disposition:form-data;name=$k;filename=$v$lineEnd$contentType$lineEnd$lineEnd"
      print(s)
      out.write(s.getBytes(ENCODING))
      val file = new BufferedInputStream(new FileInputStream(value))
      val buf = new Array[Byte](1024)
      Stream.continually(file.read(buf)).takeWhile(_ != -1)
        .foreach(out.write(buf, 0, _))
    }

  }

  @inline private def genBoundary: String = {
    val multipartChars = "_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray
    val buffer = new StringBuilder
    val rand = new Random()
    for (i <- 1 to 8) buffer.append(multipartChars.charAt(rand.nextInt(multipartChars.length)))
    buffer.result()
  }

  @elidable(FINE) def debug(a: Any) {
    println(a)
  }
}
