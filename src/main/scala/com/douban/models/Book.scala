package com.douban.models

import java.util.Date
import com.douban.common.Req
import Req._
import java.util

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a> <br/>
 * <em>see:</em><br/>
 * <a href="http://developers.douban.com/wiki/?title=book_v2">豆瓣图书API</a>
 * @author joseph
 * @since 12/26/12 8:07 PM
 * @version 1.0
 * @see http://developers.douban.com/wiki/?title=book_v2
 */
object Book extends BookMovieMusicAPI[Book, BookSearchResult, BookReview] {


  protected def url_prefix = api_prefix + "book/"

  private val byISBNUrl = url_prefix + "isbn/%s"
  private val userCollectionsUrl = url_prefix + "user/%s/collections"
  private val collectionUrl = url_prefix + "%s/collection"
  private val userAnnotationsUrl = url_prefix + "user/%s/annotations"
  private val bookAnnotationsUrl = url_prefix + "%s/annotations"
  private val annotationUrl = url_prefix + "annotation/%s"
  private val annotationPostUrl = url_prefix + "%s/annotations"

  /**
   * 根据isbn获取图书信息
   */
  def byISBN(isbn: String) = get[Book](byISBNUrl.format(isbn))

  /**
   * 获取某个用户的所有图书收藏信息
   */
  def collectionsOfUser(userId: Long, c: CollectionSearch = new CollectionSearch) = get[CollectionSearchResult](c.flatten(userCollectionsUrl.format(userId)))

  /**
   * 获取用户对某本图书的收藏信息
   */
  def collectionOf(bookId: Long,userId:String="") = get[Collection](s"$collectionUrl?user_id=%s".format(bookId,userId), secured = true)

  /**
   * 用户收藏某本图书
   */
  def postCollection(bookId: Long, c: CollectionPosted, withResult: Boolean = true) = post[Collection](collectionUrl.format(bookId), c, withResult)

  /**
   * 用户修改对某本图书的收藏
   */
  def updateCollection(bookId: Long, c: CollectionPosted, withResult: Boolean = true) = put[Collection](collectionUrl.format(bookId), c, withResult)

  /**
   * 用户删除对某本图书的收藏
   */
  def deleteCollection(bookId: Long) = delete(collectionUrl.format(bookId))

  /**
   * 获取某个用户的所有笔记
   * 默认按update_time倒序排列
   */
  def annotationsOfUser(userId: String) = get[AnnotationSearchResult](userAnnotationsUrl.format(userId))

  /**
   * 获取某本图书的所有笔记
   */
  def annotationsOf(bookId: Long, a: AnnotationSearch = new AnnotationSearch) = get[AnnotationSearchResult](a.flatten(bookAnnotationsUrl.format(bookId)))

  /**
   * 获取某篇笔记的信息
   * @param format 返回content字段格式	选填（编辑伪标签格式：text, HTML格式：html），默认为text
   */
  def annotation(annotationId: Long, format: String = "text") = get[Annotation](new AnnotationSearch(format).flatten(annotationUrl.format(annotationId)))

  /**
   * 用户给某本图书写笔记
   */
  def postAnnotation(bookId: Long, a: AnnotationPosted, withResult: Boolean = true) = post[Annotation](annotationPostUrl.format(bookId), a, withResult)

  /**
   * 用户修改某篇笔记
   */
  def updateAnnotation(annotationId: Long, a: AnnotationPosted, withResult: Boolean = true) = put[Annotation](annotationUrl.format(annotationId), a, withResult)


  /**
   * 用户刪除某篇笔记
   */
  def deleteAnnotation(annotationId: Long) = delete(annotationUrl.format(annotationId))

  /**
   * 添加笔记图片，
   * @param index 图片序号，本笔记中第几个图片
   * @return 图片展示的tag,直接添加到笔记内容中
   */
  def addAnnotationPicture(index: Int) = s"<图片$index>"
}

/**
 * 发表新评论内容
 * @param book 评论所针对的book id
 * @param title  评论头
 * @param content 评论内容，且多于150字
 * @param rating  打分，数字1～5为合法值，其他信息默认为不打分
 * @see ReviewPosted
 */
case class BookReviewPosted(book: String, title: String, content: String, rating: Int = 0) extends ReviewPosted(title, content, rating)

/**
 * 图书收藏信息
 * @param status  收藏状态,（想读：wish 在读：reading 读过：read）默认为所有状态
 * @param tag  收藏标签
 * @param rating 数字1～5为合法值，其他信息默认为不区分星评
 * @param from 按收藏更新时间过滤的起始时间,格式为符合rfc3339的字符串，例如"2012-10-19T17:14:11"，其他信息默认为不传此项
 * @param to 按收藏更新时间过滤的结束时间
 */
case class CollectionSearch(status: String = "", tag: String = "", rating: Int = 0, from: String = "", to: String = "",start:Int=0,count:Int=20) extends ListSearch(start,count)

/**
 * 收藏某本图书
 * @param status 收藏状态 ,必填（想读：wish 在读：reading 读过：read）
 * @param tags 收藏标签字符串, 用空格分隔
 * @param comment 短评文本,最多350字
 * @param rating 星评	数字1～5为合法值，其他信息默认为不评星
 * @param privacy 隐私设置,值为'private'为设置成仅自己可见，其他默认为公开
 */
case class CollectionPosted(status: String, tags: String, comment: String, rating: Int = 0, privacy: String = "") extends Bean

/**
 * 获取某本图书的所有笔记
 * @param format 返回content字段格式,选填（编辑伪标签格式：text, HTML格式：html），默认为text
 * @param order 排序	选填（最新笔记：collect, 按有用程度：rank, 按页码先后：page），默认为rank
 * @param page 按图书页码过滤
 * @param start 查询起始index
 * @param count 当前页显示数量
 */
case class AnnotationSearch(order: String = "rank", start:Int=0, count:Int=20 ,page: String = "", format: String = "text") extends ListSearch(start,count)

/**
 * 给某本图书写笔记
 * @param content 笔记内容	必填，需多于15字,上传图片后，可为空
 * @param page 页码	页码或章节名选填其一，最多6位正整数
 * @param chapter 章节名	页码或章节名选填其一，最多100字
 * @param privacy 隐私设置	选填，值为'private'为设置成仅自己可见，其他默认为公开  TODO 设置privacy为 private使仅自己可见
 * @see  http://developers.douban.com/wiki/?title=book_v2#post_book_annotation
 */
case class AnnotationPosted(var content: String, page: Int, chapter: String, privacy: String = "public") extends Bean {
  override def files = {
    for (i <- 1 to _files.size if !content.contains(s"<图片$i>")) {
      content += s"<图片$i>"
    }
    _files
  }
}

case class BookStatus(value: String) extends Enumeration {
  val WISH = Value("wish")
  val READING = Value("reading")
  val READ = Value("read")
}

case class Order(value: String) extends Enumeration {
  val COLLECT = Value("collect")
  val RANK = Value("rank")
  val PAGE = Value("page")
}

case class Image(small: String, large: String, medium: String)

/**
 * 图书信息
 */
case class Book(id: Long, isbn10: String, isbn13: String, title: String, origin_title: String,
                alt_title: String, subtitle: String, alt: String, images: Image,
                author: util.List[String], translator: util.List[String], publisher: String, pubdate: String,
                rating: ItemRating, tags: util.List[Tag], binding: String, price: String, pages: String,
                author_intro: String, var current_user_collection: Collection,summary: String = "",catalog: String = "")  extends Bean{
  def image = images.medium
  def updateCollection(c:Collection):Collection = {
    current_user_collection=c
    c
  }
}

/**
 * 评论信息
 */
case class BookReview(id: Long, title: String, alt: String, author: User, book: Book, rating: ReviewRating, votes: Int, useless: Int,
                      comments: Int, summary: String, published: Date, updated: Date)
  extends Review(id, title, alt, author, rating, votes, useless, comments, summary, published, updated)

/**
 * 笔记信息
 * @param photos 图片按照Photo中的1,2,3...获取并对应图片url
 */
case class Annotation(id: Long, book_id: String, book: Book, author_id: String, author_user: User, chapter: String, page_no: Int, privacy: Int,
                      content: String, `abstract`: String, abstract_photo: String, photos: util.Map[String, String], last_photo: Int, comments_count: Int, hasmath: Boolean, time: String)  extends Bean

/**
 * 收藏信息
 */
case class Collection(status: String, book_id: Long, book: Book, comment: String, id: Long, rating: ReviewRating = null, tags: util.List[String], updated: String, user_id: String, user: User)  extends Bean

case class AnnotationSearchResult(start: Int, count: Int, total: Int, annotations: util.List[Annotation]) extends ListResult(start, count, total)

case class BookSearchResult(start: Int, count: Int, total: Int, books: util.List[Book]) extends ListResult(start, count, total)

case class TagsResult(start: Int, count: Int, total: Int, tags: util.List[Tag]) extends ListResult(start, count, total)

case class CollectionSearchResult(start: Int, count: Int, total: Int, collections: util.List[Collection]) extends ListResult(start, count, total)
