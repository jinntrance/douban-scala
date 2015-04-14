package com.douban.models

import java.awt.Desktop
import java.net.URI
import com.douban.common._


/**
 * Copyright by <a href="http://www.josephjctang.com"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/26/12 10:37 PM
 * @version 1.0
 */
class AuthTest extends BaseTest {
  test("the auth url") {
    val url=new URI(Auth.getAuthUrl(Auth.api_key))
    println(url)
    Desktop.getDesktop.browse(url)
    //    val codeUrl = readLine("please copy the url here after authorization>\n")
    //    Auth.code = Auth.extractCode(codeUrl)
  }
  test("the acess token url") {
    prettyJSON(Auth.getTokenByCode("6ad1537d694028e4", api_key, secret))
  }
  test("the refresh token url") {
    val t = Auth.getTokenByFresh(refresh_token, api_key, secret)
    prettyJSON(t)

  }

}
