package com.douban.models

import java.awt.Desktop
import java.net.URI
import com.douban.common._
import com.douban.common.AuthorizationCode
import com.douban.common.Token


/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/26/12 10:37 PM
 * @version 1.0
 */
class AuthTest extends BaseTest {
  test("the auth url") {
    val url = AuthorizationCode().authUrl
    Desktop.getDesktop.browse(new URI(url))
    val codeUrl = readLine("please copy the url here after authorization>\n")
    Auth.code = Auth.extractCode(codeUrl)
  }
  test("the acess token url") {
    val token: Token = new AccessToken(Auth.code, Auth.redirect_url)
    val url = token.tokenUrl
    prettyJSON(Req.post[AccessTokenResult](url, token))
  }
  test("the refresh token url") {
    val token: Token = new RefreshToken(Auth.refresh_token, Auth.redirect_url)
    val url = token.tokenUrl
    prettyJSON(Req.post[AccessTokenResult](url, token))
  }

}
