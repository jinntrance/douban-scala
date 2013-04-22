package com.douban.common

import org.scalatest.FunSuite
import collection.JavaConverters._
import com.google.gson.GsonBuilder
import java.util.ResourceBundle

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/27/12 11:31 PM
 * @version 1.0
 */
trait BaseTest extends FunSuite {
  var api_key = "0f86acdf44c03ade2e94069dce40b09a"
  var secret = "95125490b60b01ee"
  var access_token = "e8063598da028fa3b812a0ca8e07aee9"
  var refresh_token = "939b85bb5e82502ebad291321b86f08a"
  val userId = 38702920
  def picPath:String=getClass.getClassLoader.getResource("headshot.jpg").getPath
  val gp=new GsonBuilder().setPrettyPrinting().create()
  Req.init(access_token)

  def prettyJSON(p: Any) {
    if (p==None) println(p)
    else println(gp.toJson(p))
  }
}
