package com.douban.models

import com.douban.common.Auth
import net.liftweb.json.{NoTypeHints, DefaultFormats}
import net.liftweb.json.Extraction._
import net.liftweb.json.JsonAST._
import scala._
import net.liftweb.json.JsonAST.JField
import net.liftweb.json.JsonAST.JObject
import net.liftweb.json.JsonAST.JString
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
   * @return 把Bean转化为key=value&key1=value1的序列 ,添加apikey
   */
  def toParas: String =Auth.addApiKey+flat(decompose(this))

  /**
   * 层级参数全部flattened 成一层的key-value形式，
   * List的values用 n=value,n=1,2,3,4
   */
  private def flat(json: JValue): String = {
    var para = ""
    for {JField(k, v) <- json
    } v match {
      case JObject(List()) | JArray(List()) => para
      case JObject(List(fields)) => para += flat(fields.value)
      case JObject(List(values)) => {
        var i = 0
        for {
          v: JValue <- values} {
          i += 1
          para += '&' + i + '=' + v.extract[String]
        }
      }
      case v: JValue => para += '&' + k + '=' + json.\(k).extract[String]
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
