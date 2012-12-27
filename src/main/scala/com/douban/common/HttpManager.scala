package com.douban.common

import java.net.{URL, HttpURLConnection}
import java.net.HttpURLConnection._
import java.io.{InputStreamReader, BufferedReader}
import net.liftweb.json._
import net.liftweb.json.JsonAST.JValue._
import xml.Null


/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/21/12 8:38 PM
 * @version 1.0
 */
class HttpManager(url: String) {
  var connection: HttpURLConnection = new URL(url).openConnection().asInstanceOf[HttpURLConnection]

  def post[R:Manifest]:R  = {
    connection.setRequestMethod("POST")
    connection.connect()
    connection.setDoOutput(true)
    parseJSON[R]()
  }
  def get[R:Manifest]:R={
    connection.setRequestMethod("GET")
    connection.connect()
    connection.setDoOutput(true)
    parseJSON[R]()
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
    }
  }
}
