package com.douban.models

import com.douban.common.Req
import Req._
import java.util.Date


/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a> <br/> <br/>
 * <em>see:</em><br/>
 * <a href="http://developers.douban.com/wiki/?title=user_v2">豆瓣用户API</a>
 * @author joseph
 * @since 12/28/12 2:34 AM
 * @version 1.0
 * @see 豆瓣用户 http://developers.douban.com/wiki/?title=user_v2
 **/

object User extends API[UserInfo, UserSearchResult] {
  def url_prefix = api_prefix + "user"

  val meUrl = url_prefix + "/~me"
  val byIdUrl = url_prefix + "/%s"

  override def searchUrl = url_prefix


  /**
   * 获取当前授权用户信息
   * @return  UserInfo
   * @example val user=User.ofMe
   */
  def ofMe = get[UserInfo](meUrl, secured = true)
}


case class UserSearchResult(start: Int, count: Int, total: Int, users: List[User]) extends ListResult(start, count, total)

/**
 * 用户简版
 */
case class User(id: String, name: String, uid: String, alt: String, avatar: String) extends Flatten

/**
 * 用户完整版信息
 * @param avatar 头像
 * @param alt 豆瓣首页
 * @param created 加入豆瓣时间
 * @param loc_name 常居地点
 * @param signature  签名档
 * @param desc 个人描述
 */
case class UserInfo(id: String, uid: String, name: String, avatar: String, alt: String, created: Date, loc_id: String, loc_name: String, signature: String, desc: String)

class Relation extends Enumeration {
  val FRIEND = Value("friend")
  val CONTACT = Value("contact")
}
