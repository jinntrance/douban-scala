package com.douban.common

import com.douban.models.Bean
import java.io._
import java.net.HttpURLConnection._
import java.net.{URLEncoder, URLDecoder, HttpURLConnection, URL}
import java.util.Random
import com.google.gson.{JsonElement, GsonBuilder}
import com.google.gson.reflect.TypeToken
import scala.reflect.ClassTag
import scala.reflect.classTag
import collection.JavaConverters._


/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/21/12 8:38 PM
 * @version 1.0
 */
object Req {
  var accessToken=""
  var apiKey=Auth.api_key
  //20 seconds
  val timeout = 20 * 1000
  //10 minutes
  val persistenceTimeout = 10 * 60
  val PUT = "PUT"
  val POST = "POST"
  val GET = "GET"
  val DELETE = "DELETE"
  val ENCODING = "UTF-8"
  val emptyJSON="""{}"""
  val g=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
  val gp=new GsonBuilder().setPrettyPrinting().create()


  def init(accessToken:String,apiKey:String=Auth.api_key,scope:String=""){
    this.accessToken=accessToken
    this.apiKey=apiKey
    Auth.scope=scope
    this
  }

  /**
   *
   * @param request 参数Bean
   * @param withResult 是否读取返回数据，默认读取 ，不读取数据时返回None为成功
   * @tparam RESULT 返回的类型。不读取数据时返回None为成功，抛出异常为读取数据不成功
   * @return
   */
  def post[RESULT: Manifest](url: String, request: Bean,withResult:Boolean=true):Option[RESULT]= {
    val c: HttpURLConnection = postData(url, request)
    if(withResult) Some(parseJSON[RESULT](c))
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
  def put[RESULT: Manifest](url: String, request: Bean,withResult:Boolean=true): Option[RESULT] = {
    val c: HttpURLConnection = putData(url, request)
    if(withResult) Some(parseJSON[RESULT](c))
    else  {
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
        upload(b, out, g.toJsonTree(request))
        request.files.asScala.foreach {
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
  def parseJSON[R: ClassTag](c: HttpURLConnection): R = {
    val v=getResponse(
      if (succeed(c.getResponseCode)) c.getInputStream else c.getErrorStream
    )
    println(s"response code-->${c.getResponseCode}")
    c.disconnect()
    println(Req.gp.toJson(v))
    val t = new TypeToken[ClassTag[R]]() {}.getType
    if (succeed(c.getResponseCode)) g.fromJson(v,classTag[R].runtimeClass).asInstanceOf[R] else throw new DoubanException(g.fromJson(v,classOf[Error]))
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
//    println(content.result())
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
    val key="apikey=" + apiKey
    if (-1 == url.indexOf('?')) url + "?" + key
    else url + "&" + key
  }

  private def getConnection(url: String) = new URL(url).openConnection().asInstanceOf[HttpURLConnection]

  private def upload(boundary: String, out: BufferedOutputStream, json: JsonElement) {
    json.getAsJsonObject.entrySet().asScala.foreach(e=>uploadFile(boundary, out, e.getKey,e.getValue.toString))

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
      val contentType="Content-Type: image/"+value.substring(value.lastIndexOf('.')+1).trim.toLowerCase
      val s = s"${boundaryString}Content-Disposition:form-data;name=$k;filename=$v$lineEnd$contentType$lineEnd$lineEnd"
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
