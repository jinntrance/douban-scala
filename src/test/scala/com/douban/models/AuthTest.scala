package com.douban.models

import java.awt.Desktop
import java.net.URI
import com.douban.common._


/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/26/12 10:37 PM
 * @version 1.0
 */
class AuthTest extends BaseTest {
  test("the auth url") {
    Desktop.getDesktop.browse(new URI(Auth.getAuthUrl(Auth.api_key)))
    //    val codeUrl = readLine("please copy the url here after authorization>\n")
    //    Auth.code = Auth.extractCode(codeUrl)
  }
  test("the acess token url") {
    prettyJSON(Auth.getTokenByCode("20ed2c81eccbb031", api_key, secret))
  }
  test("the refresh token url") {
    val t = Auth.getTokenByFresh(refresh_token, api_key, secret)
    prettyJSON(t)

  }

}
