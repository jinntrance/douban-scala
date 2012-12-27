package com.douban.common

import org.scalatest.FunSuite
import net.liftweb.json.Extraction._
import net.liftweb.json.JsonAST._
import net.liftweb.json.Printer._
import com.douban.book.Bean

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/26/12 10:37 PM
 * @version 1.0
 */
class HttpManagerTest extends BaseTest{
   test("the auth url"){
       val url=AuthorizationCode().authUrl
       val con= (new HttpManager(url).get()).connection
        println(con.getURL)
    }
  test("the token url"){
    val url=Auth.token_url
    val http= (new HttpManager(url).post[AccessTokenResult,AccessToken](new AccessToken))
    prettyJSON(http.parseJSON[AccessTokenResult]())
  }
}
