package com.douban.models

import com.douban.common.Auth
import net.liftweb.json.{NoTypeHints, DefaultFormats}
import net.liftweb.json.Extraction._
import net.liftweb.json.JsonAST.{JString, JField}
import java.net.URLEncoder

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/3/13 10:46 PM
 * @version 1.0
 */
trait Flatten {
  implicit val formats = DefaultFormats + NoTypeHints

  def flatten(urlPrefix: String, bean: Flatten = this): String = {
    urlPrefix + "?" + toParas
  }

  def toParas: String = {
    var para = Auth.addApiKey()
    val json = decompose(this)
    for {JField(k, JString(v)) <- json
    } para += '&' + k + '=' + URLEncoder.encode(v, "UTF-8")
    para
  }
}

class Bean extends Flatten {
}

class Search(q: String, start: Int, count: Int) extends Bean

class API {
  var secured = true
  val api_prefix: String = "https://api.douban.com/v2/"
}
