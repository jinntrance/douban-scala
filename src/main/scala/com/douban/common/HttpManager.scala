package com.douban.common

import java.net.{HttpURLConnection, URL}
import java.net.HttpURLConnection._
import java.io._
import net.liftweb.json._
import net.liftweb.json.Extraction._
import net.liftweb.json.JsonAST.JValue._
import net.liftweb.json.Serialization.{read, write}
import xml.Null
import java.util.zip.GZIPInputStream
import com.douban.book.Bean
import javax.xml.ws.http.HTTPException


/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/21/12 8:38 PM
 * @version 1.0
 */
class HttpManager(url: String) {
  implicit val formats = Serialization.formats(NoTypeHints)
  var connection: HttpURLConnection = new URL(url).openConnection().asInstanceOf[HttpURLConnection]
  var method="GET"

  def post[B <: Bean,R <:Bean](request:R):HttpManager = {
    method="POST"
    this.connect[B,R](request)
  }
  def get():HttpManager=get[Bean,Bean](new Bean)
  def get[B <: Bean,R <:Bean](request:R):HttpManager={
   method="GET"
    this.connect[B,R](request)
  }
  def put[B <: Bean,R <:Bean](request:R):HttpManager={
    method="PUT"
    this.connect[B,R](request)
  }
  def delete(id:String):Boolean={
    method="DELETE"
    val code =this.connect[Bean,Bean]().connection.getResponseCode
    code==HTTP_OK || code ==HTTP_NO_CONTENT
  }
  def connect[B <: Bean,R <:Bean](request:R=null):HttpManager={
    val c=connection
    c.setRequestMethod(method)
    c.setDoOutput(true)
    c.setDoInput(true)
    c.setChunkedStreamingMode(0)
    c.setUseCaches(true)
    c.setConnectTimeout(5000)
    c.setReadTimeout(8000)
    c.setRequestProperty("Connection", "Keep-Alive")
    c.setRequestProperty("Content-Type", "application/json")
    c.setRequestProperty("Charset", "UTF-8")
    val out = new BufferedOutputStream(c.getOutputStream)
    val paras=Serialization.write[R](request)
    out.write(paras.getBytes("UTF-8"))
    c.connect()
    this
  }
  /**
   *
   * @tparam R 返回值类型
   * @return
   */
  def parseJSON[R:Manifest](): R = {
    implicit val formats = net.liftweb.json.DefaultFormats
    connection.getResponseCode match {
      case HTTP_OK => {
        val reader = new BufferedReader(new InputStreamReader(connection.getInputStream))
        val content = new StringBuilder
        var line = ""
        while ( (line = reader.readLine()) != null) {
          content.append(line)
        }
        val v:JsonAST.JValue=JsonParser.parse(content.result())
        v.extract[R]
      }
      case c:Int => throw new HTTPException(c)
    }
  }
}
