package com.douban.common

import java.net.{HttpURLConnection, URL}
import java.net.HttpURLConnection._
import java.io._
import net.liftweb.json._
import net.liftweb.json.Serialization.{read, write}
import com.douban.models.Bean
import javax.xml.ws.http.HTTPException
import net.liftweb.json.Extraction._


/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/21/12 8:38 PM
 * @version 1.0
 */
class Req(url: String) {
  implicit val formats = Serialization.formats(NoTypeHints)
  var connection: HttpURLConnection = new URL(url).openConnection().asInstanceOf[HttpURLConnection]
  var method="GET"

  def post[REQUEST <:Bean](request:REQUEST):Req = {
    method="POST"
    connection.setDoOutput(true)
    connection.setDoInput(false)
    this.connect[REQUEST](request)
    this
  }
  def get():Req=get[Bean](new Bean)
  def get[REQUEST <:Bean](request:REQUEST):Req={
   method="GET"
   this.connect(request)
   this
  }
  def put[REQUEST <:Bean](request:REQUEST):Req={
    method="PUT"
    connection.setDoOutput(true)
    connection.setDoInput(false)
    this.connect[REQUEST](request)
    this
  }
  def delete(id:String):Boolean={
    method="DELETE"
    connection.setDoInput(false)
    val code =getCode
    code==HTTP_OK || code ==HTTP_NO_CONTENT
  }
  private def connect[REQUEST <:Bean](request:REQUEST=null):Int={
    val c=connection
    if(c.getRequestMethod != method ) c.setRequestMethod(method)
//    c.setChunkedStreamingMode(0)
    c.setUseCaches(true)
    c.setConnectTimeout(8000)
    c.setReadTimeout(8000)
    c.setRequestProperty("Connection", "Keep-Alive")
//    c.setRequestProperty("Content-Type", "application/json")
    c.setRequestProperty("Charset", "UTF-8")
    println(c.getRequestMethod+"ing "+c.getURL)

    if (c.getRequestMethod=="POST"||c.getRequestMethod=="PUT"){
//      val paras=Serialization.write[REQUEST](request)
      val paras=request.toParas
      val out = new BufferedOutputStream(connection.getOutputStream)
      out.write(paras.getBytes("UTF-8"))
      out.flush()
      out.close()
    }
    c.connect()
    getCode
  }
  /**
   *
   * @tparam R 返回值类型
   * @return
   */
  def parseJSON[R:Manifest](): R = {
    implicit val formats = net.liftweb.json.DefaultFormats

    getCode match {
      case c:Int if c>=HTTP_OK && c<= HTTP_PARTIAL  => {
        val v:JsonAST.JValue=JsonParser.parse(getResponse(connection.getInputStream))
        connection.disconnect()
        v.extract[R]
      }
      case c:Int => {
        val v:JsonAST.JValue=JsonParser.parse(getResponse(connection.getErrorStream))
        connection.disconnect()
        println(pretty(render(decompose(v.extract[com.douban.error.Error]))))
        throw new HTTPException(c)}
    }
  }
  def getCode:Int={
    val code=connection.getResponseCode
    code
  }
  def getResponse(inputStream:InputStream):String ={
    val reader = new BufferedReader(new InputStreamReader(inputStream))
    val content = new StringBuilder
    var line = reader.readLine()
    while (line != null) {
      content.append(line)
      line = reader.readLine()
    }
    content.result()
  }
}
