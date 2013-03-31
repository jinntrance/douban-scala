package com.douban.common

import com.douban.models.Bean


/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/26/12 8:12 PM
 * @version 1.0
 */

object Auth {
  var api_key = "0f86acdf44c03ade2e94069dce40b09a"
  var secret = "95125490b60b01ee"
  var code = ""
  var scope = ""
  val auth_url = "https://www.douban.com/service/auth2/auth"
  val token_url = "https://www.douban.com/service/auth2/token"
  var redirect_url = "http://crazyadam.net/"
  val response_type = "code"
  val grant_type = "authorization_code"
  val refresh_token_string = "refresh_token"
  var access_token = ""
  var refresh_token = ""
  var douban_user_id = 38702920

  def extractCode(url: String): String = {
    val code = "code="
    val index = url.indexOf(code)
    if(-1==index) return ""
    url.substring(index + code.length)
  }

  def addApiKey() = "apikey=" + api_key
}

case class AuthorizationCode(client_id: String = Auth.api_key, redirect_uri: String = Auth.redirect_url, scope: String = Auth.scope, response_type: String = Auth.response_type) extends Bean {
  def authUrl: String = flatten(Auth.auth_url)
}

case class AuthorizationConfirm(ck: String, ssid: String, confirm: String = "授权")

class Token(redirect_uri: String, client_id: String, client_secret: String, grant_type: String) extends Bean {
  def tokenUrl: String = flatten(Auth.token_url)
}

case class AccessToken(code: String, redirect_uri: String, client_id: String = Auth.api_key, client_secret: String = Auth.secret, grant_type: String = Auth.grant_type)
  extends Token(redirect_uri, client_id, client_secret, grant_type)

case class AccessTokenResult(access_token: String, expires_in: Long, refresh_token: String, douban_user_id: Long) extends Bean

case class RefreshToken(refresh_token: String, redirect_uri: String, client_id: String = Auth.api_key, client_secret: String = Auth.secret, grant_type: String = Auth.refresh_token_string)
  extends Token(redirect_uri, client_id, client_secret, grant_type)
