package com.douban.common

import org.scalatest.FunSuite
import net.liftweb.json.Extraction._
import net.liftweb.json.JsonAST._
import net.liftweb.json.Printer._

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/27/12 11:31 PM
 * @version 1.0
 */
trait BaseTest extends FunSuite {
  implicit val formats = net.liftweb.json.DefaultFormats
  var api_key = "0f86acdf44c03ade2e94069dce40b09a"
  var secret = "95125490b60b01ee"
  var access_token = "5f7e502f58a34e221e7af08e09e9ea66"
  var refresh_token = "7ef20435c750227a7cd60ca155f0676e"
  val userId = 38702920
  val picPath="headshot.jpg"
  Req.init(access_token)

  def prettyJSON(p: Any) {
    if (p==None) println(p)
    else println(pretty(render(decompose(p))))
  }
}
