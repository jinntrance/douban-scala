package com.douban.user

import com.douban.book.Bean
import com.douban.common.Flatten

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/28/12 2:34 AM
 * @version 1.0
 */
object API{
  implicit val api_prefix="https://api.douban.com/"
  def userUrl=api_prefix+"/v2/user/:name"
  def userSearchUrl=api_prefix+"/v2/user"
  case class Search(q:String,start:Int,count:Int) extends Bean with Flatten{
    def searchUrl=flatten(API.userSearchUrl)
  }
  case class SearchResult(start:Int,count:Int,total:Int,users:List[User])
}

case class User(id:Long,name:String,uid:String,alt:String,avatar:String) extends Flatten{

}