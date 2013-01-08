package com.douban.models

import com.douban.common.Req
import Req._
import java.util.Date


/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a> <br/>
 * @author joseph
 * @since 12/28/12 2:34 AM
 * @version 1.0
 * @see 豆瓣用户 http://developers.douban.com/wiki/?title=user_v2
 **/

object User extends API {
  val userUrl = api_prefix + "user"
  val meUrl = userUrl + "/~me"
  val byIdUrl = userUrl + "/%s"

  /**
   * 获取用户信息
   * @param id  用户uid或者数字id
   * @return UserInfo
   * @example val user=User.byId("jinntrance")
   */
  def byId(id: String) = get[UserInfo](byIdUrl.format(id))

  /**
   * 获取当前授权用户信息
   * @return  UserInfo
   * @example val user=User.ofMe
   */
  def ofMe = get[UserInfo](meUrl, secured = true)

  /**
   * 搜索用户
   * @param query 全文检索的关键词
   * @param page 查询开始页码，默认为0
   * @param count 返回结果的数量，每页显示条数，默认为20
   * @return UserSearchResult
   * @example val user=User.search("刘瑜")
   */
  def search(query: String, page: Int = 0, count: Int = 20) = get[UserSearchResult](UserSearch(query, page * count, count).searchUrl)
}

/**
 * 搜索用户需要传的参数
 * @param q  全文检索的关键词
 * @param start 开始的数量
 * @param count 返回结果的数量
 */
case class UserSearch(q: String, start: Int = 0, count: Int = 20) extends Search(q, start, count) {
  def searchUrl = flatten(User.userUrl)
}

case class UserSearchResult(start: Int, count: Int, total: Int, users: List[User])

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
