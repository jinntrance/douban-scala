package com.douban.common

import net.liftweb.json._
import Extraction._
import com.douban.book.Bean


/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/26/12 8:12 PM
 * @version 1.0
 */

 object Auth {
  val api_key="0f86acdf44c03ade2e94069dce40b09a"
  val secret="95125490b60b01ee"
  val code="41c5a7272efe6e2f"
  val auth_url="https://www.douban.com/service/auth2/auth?"
  val token_url="https://www.douban.com/service/auth2/token?"
  val redirect_url="http://crazyadam.net/"
  val response_type="code"
  val grant_type="authorization_code"
  val refresh_token="refresh_token"

}
case class AuthorizationCode(client_id:String=Auth.api_key,redirect_uri:String=Auth.redirect_url,response_type:String=Auth.response_type) extends Bean {
  var (scope,state)=("","")
  implicit val formats = net.liftweb.json.DefaultFormats
  def authUrl:String={
    var url=Auth.auth_url
    val json=decompose(this)
    for {JField(k,JString(v))<-json
    } url+='&'+k+'='+v
    url
  }
}
class Token(val client_id:String=Auth.api_key,val client_secret:String=Auth.secret,val redirect_uri:String=Auth.redirect_url,grant_type:String=Auth.grant_type) extends Bean
case class AccessToken(code:String=Auth.code) extends Token {

}
case class AccessTokenResult(access_token:String,expires_in:Long,refresh_token:String,douban_user_id:Long) extends Bean
case class RefreshToken(refresh_token:String=Auth.refresh_token) extends Token(grant_type = Auth.refresh_token)
