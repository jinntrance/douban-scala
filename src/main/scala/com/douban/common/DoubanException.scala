package com.douban.common

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/8/13 12:48 AM
 * @version 1.0
 */
class DoubanException(e: Error) extends Exception(e.msg)

case class Error(msg: String, code: String, request: String)
