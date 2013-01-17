package com.douban.models

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/18/13 3:29 AM
 * @version 1.0
 */
object Online extends API[Online] with DiscussionTrait[Online]{
  protected def url_prefix = api_prefix+"online"
}
case class Online
case class OnlineResult(start:Int,count:Int,total:Int,user:User,onlines:List[Online]) extends ListResult(start,count,total)
