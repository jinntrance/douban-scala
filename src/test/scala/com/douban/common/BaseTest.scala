package com.douban.common

import org.scalatest.FunSuite
import com.google.gson.GsonBuilder

/**
 * Copyright by <a href="http://www.josephjctang.com"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/27/12 11:31 PM
 * @version 1.0
 */
trait BaseTest extends FunSuite {
  var api_key = "0f86acdf44c03ade2e94069dce40b09a"
  var secret = "95125490b60b01ee"
  var access_token = "8ad892f109966347a089b8d31cc329d5"
  var refresh_token = "a6b5693e74b31cc2f4203bbb5e195646"
  val userId = 38702920

  def picPath: String = getClass.getClassLoader.getResource("headshot.jpg").getPath

  val gp = new GsonBuilder().setPrettyPrinting().create()
  Req.init(access_token)

  def prettyJSON(p: Any) {
    if (p == None) println(p)
    else println(gp.toJson(p))
  }
}
