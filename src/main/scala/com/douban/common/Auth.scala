package com.douban.common

import net.liftweb.json._
import Extraction._
import com.douban.models.Bean
import java.net.URLEncoder


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
  var code="94e4a2200be75286"
  val auth_url="https://www.douban.com/service/auth2/auth"
  val token_url="https://www.douban.com/service/auth2/token"
  val redirect_url="http://crazyadam.net/"
  val response_type="code"
  val grant_type="authorization_code"
  val refresh_token_string="refresh_token"
  var access_token="ffefa697cfce085affcef3c76f6a190e"
  var refresh_token="99294f78905ada31cb7bee69b1108e93"
  var douban_user_id="38702920"
  def extractCode(url:String):String={
    val code="code="
    val index=url.indexOf(code)
    url.substring(index+code.length)
  }
}
trait Flatten{
  implicit val formats = DefaultFormats+ NoTypeHints
  def flatten(urlPrefix:String,bean:Flatten=this):String={
    urlPrefix+"?"+toParas
  }
  def toParas:String={
    var para="apikey="+Auth.api_key
    val json=decompose(this)
    for {JField(k,JString(v))<-json
    } para+='&'+k+'='+URLEncoder.encode(v,"UTF-8")
    para
  }

}
case class AuthorizationCode(client_id:String=Auth.api_key,redirect_uri:String=Auth.redirect_url,response_type:String=Auth.response_type) extends Bean{
  var (scope,state)=("","")
  def authUrl:String=flatten(Auth.auth_url)
}
case class AuthorizationConfirm(ck:String,ssid:String,confirm:String="授权")
case class Token( client_id:String=Auth.api_key, client_secret:String=Auth.secret, redirect_uri:String=Auth.redirect_url,grant_type:String=Auth.grant_type,code:String=Auth.code,refresh_token:String=Auth.refresh_token_string) extends Bean with Flatten{
  def tokenUrl:String=flatten(Auth.token_url)
}

object AccessToken extends Token(grant_type=Auth.grant_type)
class AccessToken extends Token(grant_type=Auth.grant_type)
case class AccessTokenResult(access_token:String,expires_in:Long,refresh_token:String,douban_user_id:String) extends Bean

object RefreshToken extends Token(grant_type = Auth.refresh_token_string,refresh_token=Auth.refresh_token_string);
