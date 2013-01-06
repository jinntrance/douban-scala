package com.douban.models

import net.liftweb.json.{FieldSerializer, DefaultFormats, Serialization, NoTypeHints}
import net.liftweb.json.Extraction._
import net.liftweb.json.JsonAST.{JString, JField}
import com.douban.common.{Req, Flatten}
import Req._

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/26/12 8:07 PM
 * @version 1.0
 */
object Book extends API{
  private val bookUrl=api_prefix+"book/"
  private val byIdUrl=bookUrl+"%s"
  private val byISBNUrl=bookUrl+"isbn/%s"
  private val bookSearchUrl=bookUrl+"search"
  private val popTagsUrl=bookUrl+"%s/tags"
  private val userTagsUrl=bookUrl+"user/%s/tags"
  private val userCollectionsUrl=bookUrl+"user/%s/collections"
  private val collectionUrl=bookUrl+"%s/collection"
  private val userAnnotationsUrl=bookUrl+"user/%s/annotations"
  private val annotationsUrl=bookUrl+"%s/annotations"
  private val annotationUrl=bookUrl+"annotation/%s"
  private val annotationPostUrl=bookUrl+"%s/annotations"
  private val reviewsPostUrl=bookUrl+"reviews"
  private val reviewUpdateUrl=bookUrl+"review/%s"

  def search(query:String,tag:String,page:Int=0,count:Int=20)= get[BookSearchResult](BookSearch(query,tag,page*count).searchUrl)
  def byId(id:String)=get[Book](byIdUrl.format(id))
  def byISBN(isbn:String)=get[Book](byISBNUrl.format(isbn))
  def popTags(id:String)=get[PopTag](popTagsUrl.format(id))

  def postReview(r:ReviewPosted):Boolean=postNoResult(reviewsPostUrl,r)
  def updateReview(r:ReviewPosted):Boolean=putNoResult(reviewUpdateUrl.format(r.book),r)
  def deleteReview(r:ReviewPosted):Boolean=delete(reviewUpdateUrl.format(r.book))

  def tags(userId:String)=get(userTagsUrl.format(userId))
  def collectionsOfUser(userId:String,c:CollectionSearch=CollectionSearch)=get(c.flatten(userCollectionsUrl.format(userId)))
  def collectionOf(bookId:String)=get[Collection](collectionUrl.format(bookId))

  def postCollection(bookId:String,c:CollectionPosted)=postNoResult(collectionUrl.format(bookId),c)
  def updateCollection(bookId:String,c:CollectionPosted)=putNoResult(collectionUrl.format(bookId),c)
  def deleteCollection(bookId:String)=delete(collectionUrl.format(bookId))

  def annotationsOfUser(userId:String)=get[AnnotationSearchResult](userAnnotationsUrl.format(userId))
  def annotationsOf(bookId:String,a:AnnotationSearch=AnnotationSearch)=get[AnnotationSearchResult](a.flatten(annotationsUrl.format(bookId)))
  def postAnnotation(bookId:String,a:AnnotationPosted)=postNoResult(annotationPostUrl.format(bookId),a)  //TODO上传图片的问题还需要解决

}

case class BookSearch(q:String,tag:String,start:Int=0,count:Int=20) extends Bean{
  def searchUrl=flatten(Book.bookSearchUrl)
}

case class ReviewPosted(book:String,title:String,content:String,rating:Int=0) extends Bean
case class CollectionSearch(status:String="",tag:String="",rating:Int=0,from:String="",to:String="") extends Bean
case class CollectionPosted(status:String,tags:String,comment:String,rating:Int=0,privacy:String="") extends Bean
case class AnnotationSearch(format:String="text",order:String="rank",page:Int=1) extends Bean
case class AnnotationPosted(content:String,page:Int,chapter:String,privacy:String="") extends Bean
//TODO 设置privacy为 private使仅自己可见
case class Tag(count:Int,title:String)
case class Status(value:String) extends Enumeration{
    val WISH=Value("wish")
    val READING=Value("reading")
    val READ=Value("read")
}
case class Order(value:String) extends Enumeration{
   val COLLECT=Value("collect")
   val RANK=Value("rank")
   val PAGE=Value("page")
}
case class PopTag(start:Int,count:Int,total:Int,tags:List[Tag])
case class BookTag(count:Int,name:String)
case class Image(small:String,large:String,medium:String)
case class Photo() //TODO
case class Rating(max:Int,min:Int,value:Int)
case class BookRating(max:Int,min:Int,average:String,numRaters:Int)
case class Book(id:String,isbn10:String,isbn13:String,title:String,origin_title:String,
           alt_title:String,subtitle:String,url:String,alt:String,image:String,images:Image,
           author:List[String],translator:List[String],publisher:String,pubdate:String,
           rating:BookRating,tags:List[BookTag],binding:String,price:String,pages:String,
           author_intro:String,summary:String
                 )

case class BookReview(id:Long,title:String,alt:String,author:User,book:Book,rating:Rating,
                   votes:Int,useless:Int,comments:Int,summary:String,published:String,updated:String)

case class Annotation(id:String,book_id:String,book:Book,author_id:String,author_user:User,chapter:String,page_no:Int,privacy:Int,
                      content:String,`abstract`:String,abstract_photo:String,photos:List[Photo],last_photo:Int,comments_count:Int,hasmath:Boolean,time:String)

case class AnnotationSearchResult(start:Int,count:Int,total:Int,annotations:List[Annotation])
case class BookSearchResult(start:Int,count:Int,total:Int,books:List[Book])
case class Collection(book:String,book_id:String,comment:String,id:Long,rating:Rating,status:String,tags:List[String],updated:String,user_id:String)

