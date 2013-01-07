package com.douban.common

import java.net.{HttpURLConnection, URL}
import java.net.HttpURLConnection._
import java.io._
import net.liftweb.json._
import com.douban.models.Bean


/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/21/12 8:38 PM
 * @version 1.0
 */
class Req

object Req {
  val PUT = "PUT"
  val POST = "POST"
  val GET = "GET"
  val DELETE = "DELETE"
  val ENCODING = "UTF-8"
  implicit val formats = Serialization.formats(NoTypeHints)

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
    newUrl = addApiKey(newUrl)
    val c = getData(newUrl, secured)
    parseJSON[RESULT](c)
  }

  def put[RESULT: Manifest](url: String, request: Bean): RESULT = {
    val c: HttpURLConnection = putData(url, request)
    parseJSON[RESULT](c)
  }

  def putNoResult(url: String, request: Bean): Boolean = {
    val c = putData(url, request)
    val code = c.getResponseCode
    c.disconnect()
    succeed(code)
  }

  def delete(url: String): Boolean = {
    val c = deleteData(url)
    val code = c.getResponseCode
    c.disconnect()
    succeed(code)
  }

  private def connect(c: HttpURLConnection, request: Bean, authorized: Boolean = true) {
    c.setUseCaches(true)
    c.setConnectTimeout(8000)
    c.setReadTimeout(8000)
    c.setRequestProperty("Connection", "Keep-Alive")
    if (authorized)
      c.setRequestProperty("Authorization", "Bearer " + Auth.access_token)
    c.setRequestProperty("Charset", ENCODING)
    println(c.getRequestMethod + "ing " + c.getURL)
    if ((c.getRequestMethod == POST || c.getRequestMethod == PUT) && null != request) {
      val paras = request.toParas
      val out = new BufferedOutputStream(c.getOutputStream)
      out.write(paras.getBytes(ENCODING))
      out.flush()
      out.close()
    }
    c.connect()
  }

  /**
   *
   * @tparam R 返回值类型
   * @return
   */
  def parseJSON[R: Manifest](c: HttpURLConnection): R = {
    implicit val formats = net.liftweb.json.DefaultFormats
    val v: JsonAST.JValue = JsonParser.parse(getResponse(
      if (succeed(c.getResponseCode)) c.getInputStream else c.getErrorStream
    ))
    println("response code-->" + c.getResponseCode)
    c.disconnect()
    println(pretty(render(v)))
    if (succeed(c.getResponseCode)) v.extract[R] else throw new DoubanException(v.extract[Error])
  }

  private def getResponse(inputStream: InputStream): String = {
    val reader = new BufferedReader(new InputStreamReader(inputStream))
    val content = new StringBuilder
    var line = reader.readLine()
    while (line != null) {
      content.append(line)
      line = reader.readLine()
    }
    content.result()
  }

  private def postData(url: String, request: Bean, withFile: Boolean): HttpURLConnection = {
    val c: HttpURLConnection = getConnection(url)
    c.setDoOutput(true)
    c.setRequestMethod(POST)
    if (withFile)
      c.setRequestProperty("Content-Type", "multipart/form-data;")
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
    this.connect(c, null, authorized)
    c
  }

  private def succeed(code: Int): Boolean = {
    code >= HTTP_OK && code <= HTTP_ACCEPTED
  }

  private def addApiKey(url: String) = {
    if (-1 == url.indexOf('?')) url + "?" + Auth.addApiKey
    else url + "&" + Auth.addApiKey
  }

  private def getConnection(url: String) = new URL(url).openConnection().asInstanceOf[HttpURLConnection]
}
