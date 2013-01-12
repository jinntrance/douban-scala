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
  def toParas: String = Auth.addApiKey + flat(decompose(this))

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

  private def encode(value: String) = URLEncoder.encode(value, "utf-8")
}

class Bean extends Flatten {
}

abstract class API[+B: Manifest] {
  var secured = false
  val api_prefix: String = "https://api.douban.com/v2/"

  protected def url_prefix: String

  protected val idUrl = url_prefix + "/%s"


  /**
   * 通过id获取
   */
  def byId(id: String) = get[B](idUrl.format(id))

}

abstract class BookMovieMusicAPI[+B: Manifest, +RT: Manifest, +RV: Manifest] extends API[B] {
  private val popTagsUrl = url_prefix + "%s/tags"
  private val reviewsPostUrl = url_prefix + "reviews"
  private val reviewUpdateUrl = url_prefix + "review/%s"

  protected def searchUrl = url_prefix + "/search"

  private val tagsUrl = url_prefix + "user_tags/%s"

  /**
   * 获取某个Item中标记最多的标签
   */
  def popTags(id: String) = get[TagsResult](popTagsUrl.format(id))

  /**
   * 发表新评论
   */
  def postReview[R <: ReviewPosted](r: R): Boolean = postNoResult(reviewsPostUrl, r)

  def postReviewWithResult[R <: ReviewPosted](r: R) = post[RV](reviewsPostUrl, r)

  /**
   * 修改评论
   */
  def updateReview[R <: ReviewPosted](reviewId: String, r: R): Boolean = putNoResult(reviewUpdateUrl.format(reviewId), r)

  def updateReviewWithResult[R <: ReviewPosted](reviewId: String, r: R) = put[RV](reviewUpdateUrl.format(reviewId), r)

  /**
   * 删除评论
   */
  def deleteReview(reviewId: String): Boolean = delete(reviewUpdateUrl.format(reviewId))


  /**
   * 获取用户对Items的所有标签
   */
  def tags(userId: String) = get[TagsResult](tagsUrl.format(userId))

  /**
   * 搜索，query/tag必传其一
   * @param query  查询关键字
   * @param tag   查询标签
   * @param page  显示第几页
   * @param count  每页显示数量
   * @return
   */
  def search(query: String, tag: String, page: Int = 1, count: Int = 20) = get[RT](new Search(query, tag, (page - 1) * count, count).flatten(searchUrl))

}

/**
 * 标签信息 图书、电影用name，音乐、图书用title
 */
case class Tag(count: Int, name: String, title: String)

/**
 *
 * @param q 查询关键字
 * @param start 开始数量
 * @param count 返回总数
 * @param tag  图书 电影可以传tags
 */
case class Search(q: String, tag: String, start: Int = 0, count: Int = 20) extends Bean

/**
 *
 * @param max  5 最大值
 * @param min 0  最小值
 * @param value  評分
 */
case class ReviewRating(max: Int, min: Int, value: String)

/**
 * @param max 10 評分結果最大值
 * @param min  0 評分結果最小值
 * @param average 平均评分
 * @param numRaters 评分人数
 */
case class ItemRating(max: Int, min: Int, average: String, numRaters: Int)

class ListResult(start: Int, count: Int, total: Int)

class Review(id: String, title: String, alt: String, author: User, rating: ReviewRating,
             votes: Int, useless: Int, comments: Int, summary: String, published: Date, updated: Date)

/**
 *
 * @param title 标题
 * @param content 内容
 * @param rating  打分1-5 //TODO no ever case class
 */
class ReviewPosted protected(title: String, content: String, rating: Int = 0) extends Bean

