package com.douban.models

import net.liftweb.json.{FieldSerializer, DefaultFormats, Serialization, NoTypeHints}
import net.liftweb.json.Extraction._
import net.liftweb.json.JsonAST.{JString, JField}
import com.douban.common.Flatten
import com.douban.models.{Bean, API}

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/26/12 8:07 PM
 * @version 1.0
 */
object Book extends API{
  def bookUrl=API.api_prefix+"book/"
  def bookSearchUrl=bookUrl+"search"
}
case class Tag(count:String,title:String)
case class Rating(max:String,min:String,value:String)
case class Collection(book:String,book_id:String,comment:String,id:String,rating:Rating,status:String,tags:List[String],updated:String,user_id:String)
case class Book(id:String,isbn10:String,isbn13:String,title:String,origin_title:String,
           alt_title:String,subtitle:String,url:String) {

}
case class BookSearchResult(start:Int,count:Int,total:Int,books:List[Book])

case class BookSearch(q:String,tag:String,start:Int=0,count:Int=20) extends Bean{
  def searchUrl=flatten(Book.bookSearchUrl)
}
