package com.douban.models

import com.douban.common.Req
import Req._


/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/28/12 2:34 AM
 * @version 1.0
 */
case class UserSearch(q: String, start: Int = 0, count: Int = 20) extends Search(q, start, count) {
  def searchUrl = flatten(User.userUrl)
}

case class UserSearchResult(start: Int, count: Int, total: Int, users: List[User])

case class User(id: String, name: String, uid: String, alt: String, avatar: String) extends Flatten

case class UserInfo(id: String, uid: String, name: String, avatar: String, alt: String, created: String, loc_id: String, loc_name: String, signature: String, desc: String)

class Relation extends Enumeration {
  val FRIEND = Value("friend")
  val CONTACT = Value("contact")
}

object User extends API {
  val userUrl = api_prefix + "user"
  val meUrl = userUrl + "/~me"
  val byIdUrl = userUrl + "/%s"

  def byId(id: String) = get[UserInfo](byIdUrl.format(id))

  def ofMe = get[UserInfo](meUrl, secured = true)

  def search(query: String, page: Int = 0, count: Int = 20) = get[UserSearchResult](UserSearch(query, page * count, count).searchUrl)
}