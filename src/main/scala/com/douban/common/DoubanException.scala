package com.douban.common

import scala.Exception

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/8/13 12:48 AM
 * @version 1.0
 */
class DoubanException(e: Error) extends Exception(e.msg + " when requesting " + e.request + " ,error code:" + e.code) {
  def tokenExpired = e.code == 106
}

class AuthErrorException(e: String) extends Exception(e)

case class Error(msg: String, code: String, request: String) {
  val error = Map(
    106 -> "access_token 过期" //需要重新请求获取
  )
}
