package com.douban.models

import com.douban.common.Auth
import net.liftweb.json.{NoTypeHints, DefaultFormats}
import net.liftweb.json.Extraction._
import net.liftweb.json.JsonAST._
import scala._
import net.liftweb.json.JsonAST.JField
import net.liftweb.json.JsonAST.JObject
import net.liftweb.json.JsonAST.JString

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/3/13 10:46 PM
 * @version 1.0
 */
trait Flatten {
  implicit val formats = DefaultFormats + NoTypeHints

  /**
   * 将Bean与一个url组合
   * @param urlPrefix 请求的原始路径
   * @param bean 需要参数话的Bean
   * @return  含参数的url
   */
  def flatten(urlPrefix: String, bean: Flatten = this): String = {
    urlPrefix + "?" + toParas
  }

  /**
   *
   * @return 把Bean转化为key=value&key1=value1的序列
   */
  def toParas: String = {
    val para = Auth.addApiKey()
    val json: JValue = decompose(this)
    para + flat(json)
  }

  private def flat(json: JValue): String = {
    var para = ""
    for {JField(k, JString(v)) <- json
    } {
      //      para += '&' + k + '=' + URLEncoder.encode(v, "UTF-8")
      para += '&' + k + '=' + v
    }
    for {JField(k, JObject(List(l))) <- json
    } {
      para += flat(l.value)
    }
    para
  }
}

class Bean extends Flatten {
}

class Search(q: String, start: Int, count: Int) extends Bean

class API {
  var secured = false
  val api_prefix: String = "https://api.douban.com/v2/"
}
