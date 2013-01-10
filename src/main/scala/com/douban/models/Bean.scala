package com.douban.models

import com.douban.common.Auth
import net.liftweb.json.{NoTypeHints, DefaultFormats}
import net.liftweb.json.Extraction._
import net.liftweb.json.JsonAST._
import scala._
import java.net.URLEncoder
import java.util.Date
import com.douban.common.Req._
import net.liftweb.json.JsonAST.JField
import net.liftweb.json.JsonAST.JObject
import net.liftweb.json.JsonAST.JArray

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/3/13 10:46 PM
 * @version 1.0
 */
trait Flatten {
  implicit val formats = DefaultFormats + NoTypeHints

  /**
   * 将Bean与一个url组合
   * @param urlPrefix 请求的原始路径
   * @param bean 需要参数话的Bean
   * @return  含参数的url
   */
  def flatten(urlPrefix: String, bean: Flatten = this): String = {
    urlPrefix + "?" + toParas
  }

  /**
   *
   * @return 把Bean转化为key=value&key1=value1的序列 ,添加apikey
   */
  def toParas: String =Auth.addApiKey+flat(decompose(this))

  /**
   * 层级参数全部flattened 成一层的key-value形式，
   * List的values用 n=value,n=1,2,3,4
   */
  private def flat(json: JValue): String = {
    var para = ""
    for {JField(k, v) <- json
    } v match {
      case JObject(List()) | JArray(List()) => para
      case JObject(List(fields)) => para += flat(fields.value)
      case JObject(List(values)) => {
        var i = 0
        for {
          v: JValue <- values} {
          i += 1
          para += '&' + i + '=' + encode(v.extract[String])
        }
      }
      case v: JValue => para += '&' + k + '=' + encode(json.\(k).extract[String])
    }
    para
  }
  private def encode(value:String)=URLEncoder.encode(value,"utf-8")
}

class Bean extends Flatten {
}

abstract class API[T:Manifest] {
  var secured = false
  val api_prefix: String = "https://api.douban.com/v2/"
  def url_prefix:String
  private val byIdUrl = url_prefix + "%s"
  private val searchUrl = url_prefix + "search"

  /**
   * 通过id获取
   */
  def byId(id: String) = get[T](byIdUrl.format(id))
  /**
   * 搜索
   */
  protected def search[R<:ListResult](query: String, page: Int = 0, count: Int = 20, tag: String="") = get[R](new Search(query, page * count, count,tag).flatten(searchUrl))
}
abstract class BookMovieMusicAPI[T:Manifest] extends API[T]{
  private val popTagsUrl = url_prefix + "%s/tags"
  private val reviewsPostUrl = url_prefix + "reviews"
  private val reviewUpdateUrl = url_prefix + "review/%s"
  private val tagsUrl = url_prefix + "user/user_tags/%s"
  /**
   * 获取某个Item中标记最多的标签
   */
  def popTags(id: String) = get[TagsResult](popTagsUrl.format(id))

  /**
   * 发表新评论
   */
  def postReview[R<:ReviewPosted](r: ReviewPosted): Boolean = postNoResult(reviewsPostUrl, r)

  /**
   * 修改评论
   */
  def updateReview(reviewId: String, r: ReviewPosted): Boolean = putNoResult(reviewUpdateUrl.format(reviewId), r)

  /**
   * 删除评论
   */
  def deleteReview(reviewId: String): Boolean = delete(reviewUpdateUrl.format(reviewId))


  /**
   * 获取用户对图书的所有标签
   */
  def tags(userId: String) = get[TagsResult](tagsUrl.format(userId))

}
/**
 * 标签信息
 */
case class Tag(count: Int, name: String)

/**
 *
 * @param q 查询关键字
 * @param start 开始数量
 * @param count 返回总数
 * @param tags  图书 电影可以传tags
 */
class Search(q: String, start: Int=0, count: Int=20,tags:String="") extends Bean

case class Rating(max: Int, min: Int, value: String)

/**
 *
 * @param average 平均评分
 * @param numRaters 评分人数
 */
case class RatingDetail(max: Int, min: Int, average: String, numRaters: Int)

class ListResult(start: Int, count: Int, total: Int)

class Review(id: Long, title: String, alt: String, author: User, rating: Rating,
                  votes: Int, useless: Int, comments: Int, summary: String, published: Date, updated: Date)
class ReviewPosted(title: String, content: String, rating: Int = 0) extends Bean

