package com.douban.models

import net.liftweb.json.{FieldSerializer, DefaultFormats, Serialization, NoTypeHints}
import net.liftweb.json.Extraction._
import net.liftweb.json.JsonAST.{JString, JField}
import com.douban.common.{HttpRequest, Flatten}

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/26/12 8:07 PM
 * @version 1.0
 */
object Book extends API{
  def bookUrl=api_prefix+"book/"
  def bookSearchUrl=bookUrl+"search"
  def search(query:String,tag:String,page:Int=0,count:Int=20):BookSearchResult={
    val http= new HttpRequest(BookSearch(query,tag,page*count).searchUrl).get()
    http.parseJSON[BookSearchResult]()
  }
}
case class Tag(count:Int,title:String)
case class BookTag(count:Int,name:String)
case class Image(small:String,large:String,medium:String)
case class Photo()
case class Rating(max:Int,min:Int,value:Int)
case class BookRating(max:Int,min:Int,average:String,numRaters:Int)
case class Book(id:String,isbn10:String,isbn13:String,title:String,origin_title:String,
           alt_title:String,subtitle:String,url:String,alt:String,image:String,images:Image,
           author:List[String],translator:List[String],publisher:String,pubdate:String,
           rating:BookRating,tags:List[BookTag],binding:String,price:String,pages:String,
           author_intro:String,summary:String
                 )
case class Review(id:Long,title:String,alt:String,author:User,book:Book,rating:Rating,
                   votes:Int,useless:Int,comments:Int,summary:String,published:String,updated:String)
case class Annotation(id:String,book_id:String,book:Book,author_id:String,author_user:User,chapter:String,page_no:Int,privacy:Int,
                      content:String,`abstract`:String,abstract_photo:String,photos:List[Photo],last_photo:Int,comments_count:Int,hasmath:Boolean,time:String)
case class BookSearchResult(start:Int,count:Int,total:Int,books:List[Book])
case class BookSearch(q:String,tag:String,start:Int=0,count:Int=20) extends Bean{
  def searchUrl=flatten(Book.bookSearchUrl)
}
case class Collection(book:String,book_id:String,comment:String,id:Long,rating:Rating,status:String,tags:List[String],updated:String,user_id:String)

