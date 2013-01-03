package com.douban.models

import com.douban.models.{API, Bean}
import com.douban.common.Flatten

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/28/12 2:34 AM
 * @version 1.0
 */
case class UerSearch(q:String,start:Int=0,count:Int=20) extends Bean{
  def searchUrl=flatten(User.userSearchUrl)
}
case class UserSearchResult(start:Int,count:Int,total:Int,users:List[User])
case class User(id:Long,name:String,uid:String,alt:String,avatar:String) extends Flatten{

}
object User extends API{
  def userUrl=API.api_prefix+"user/"
  def userSearchUrl=userUrl
}