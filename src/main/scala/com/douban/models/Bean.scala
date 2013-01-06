package com.douban.models

import com.douban.common.{Req, Flatten}

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/3/13 10:46 PM
 * @version 1.0
 */
class Bean extends Flatten{
}
class API{
  var secured=true
  def api_prefix:String= synchronized(API.api_prefix(secured))
}
object API{
  def api_prefix(secured:Boolean):String={
    if (!secured) "http" else "https"
  }+"://api.douban.com/v2/"

}
