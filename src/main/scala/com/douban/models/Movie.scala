package com.douban.models

import java.util.Date
import com.douban.common.Req
import Req._

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a><br/>
 * <em>see:</em><br/>
 * <a href="http://developers.douban.com/wiki/?title=movie_v2">豆瓣电影API</a>
 * @author joseph
 * @since 1/10/13 1:55 PM
 * @version 1.0
 */

object Movie extends BookMovieMusicAPI[Movie, MovieSearchResult, MovieReview]{
  protected def url_prefix = api_prefix + "movie/subject/"

  private val byImdbUrl = url_prefix + "imdb/%s"

  def byImdb(imdb: String) = get[Movie](byImdbUrl.format(imdb))


}

case class Author(name: String)

case class MovieAttribute(language: List[String], pubdate: List[String], title: List[String], country: List[String], writer: List[String], director: List[String], cast: List[String], movie_duration: List[String], year: List[String], movie_type: List[String])

case class MovieReview(id: Long, title: String, alt: String, author: User, movie: Movie, rating: ReviewRating,
                       votes: Int, useless: Int, comments: Int, summary: String, published: Date, updated: Date)
  extends Review(id, title, alt, author, rating, votes, useless, comments, summary, published, updated)

case class Movie(id: Long, title: String, author: List[Author], images: Image, rating: MovieRating, summary: String,aka:List[String],year:String,ratings_count:Int,wish_count:Int,collect_count:Int,comments_count:Int,reviews_count:Int,countries:List[String],genres:List[String],current_season:String,directors:List[CelebritySimple],casts:List[CelebritySimple],writers:List[CelebritySimple],subtype:String,seasons_count:Int){
  val url="http://movie.douban.com/subject/"
  def alt=url+id
  def mobile_url=alt+"/mobile"
}
case class MovieSimple(id:Long,title:String,year:String,alt:String,images:Image,original_title:String,pubdates:List[String],rating:ItemRating)

/**
 *
 * @param movie 电影id
 * @param title 标题
 * @param content 内容
 * @param rating  打分1-5
 */
case class MovieReviewPosted(movie: String, title: String, content: String, rating: Int = 0) extends ReviewPosted(title, content, rating)

case class MovieSearchResult(start: Int, count: Int, total: Int, movies: List[Movie])

case class Celebrity(id:Long,name:String,name_en:String,alt:String,mobile_url:String,aka:List[String],aka_en:List[String],gender:String,born_place:String,avatars:Image,works:List[Work])

case class MoviePhoto(id:Long,alt:String,icon:String,image:String,thumb:String,cover:String)
case class CelebritySimple(id:Long,name:String,alt:String,avatars:Image)

case class MovieRating(min:Int,max:Int,average:Float,stars:Int)

case class Work(subject:Subject,roles:List[String])
case class Subject(id:Long,title:String,origin_title:String,alt:String,images:Image,rating:MovieRating,year:String,subtype:String)