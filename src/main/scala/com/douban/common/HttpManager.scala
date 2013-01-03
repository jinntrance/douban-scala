package com.douban.common

import java.net.{HttpURLConnection, URL}
import java.net.HttpURLConnection._
import java.io._
import net.liftweb.json._
import net.liftweb.json.Serialization.{read, write}
import com.douban.models.Bean
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

  def post[REQUEST <:Bean](request:REQUEST):HttpManager = {
    method="POST"
    connection.setDoOutput(true)

    this.connect[REQUEST](request)
  }
  def get():HttpManager=get[Bean](new Bean)
  def get[REQUEST <:Bean](request:REQUEST):HttpManager={
   method="GET"
   this.connect(request)
  }
  def put[REQUEST <:Bean](request:REQUEST):HttpManager={
    method="PUT"
    this.connect[REQUEST](request)
  }
  def delete(id:String):Boolean={
    method="DELETE"
    val code =getCode
    code==HTTP_OK || code ==HTTP_NO_CONTENT
  }
  private def connect[REQUEST <:Bean](request:REQUEST=null):HttpManager={
    val c=connection
    if(c.getRequestMethod != method ) c.setRequestMethod(method)
//    c.setChunkedStreamingMode(0)
    c.setUseCaches(true)
    c.setConnectTimeout(8000)
    c.setReadTimeout(8000)
    c.setRequestProperty("Connection", "Keep-Alive")
    c.setRequestProperty("Content-Type", "application/json")
    c.setRequestProperty("Charset", "UTF-8")
    println(c.getRequestMethod+"ing "+c.getURL)

    if (c.getRequestMethod=="POST"||c.getRequestMethod=="PUT"){
//      val paras=Serialization.write[REQUEST](request)
      val paras=request.toParas
      val out = new BufferedOutputStream(connection.getOutputStream)
      out.write(paras.getBytes("UTF-8"))
    }
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
    val reader = new BufferedReader(new InputStreamReader(connection.getInputStream))
    val content = new StringBuilder
    var line = reader.readLine()
    while (line!= null) {
      content.append(line)
      line = reader.readLine()
    }
    getCode match {
      case c:Int if c>=HTTP_OK && c<= HTTP_PARTIAL  => {
        val v:JsonAST.JValue=JsonParser.parse(content.result())
        v.extract[R]
      }
      case c:Int => {
        println("http status code -->"+c)
        throw new HTTPException(c)}
    }
  }
  def getCode:Int={
    val code=connection.getResponseCode
    connection.disconnect()
    code
  }
}
