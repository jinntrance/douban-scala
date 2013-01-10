package com.douban.models

import java.util.Date
import com.douban.common.Req
import Req._

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/10/13 1:55 PM
 * @version 1.0
 * @see http://developers.douban.com/wiki/?title=api_v2
 */

object Movie extends BookMovieMusicAPI[Movie, MovieSearchResult] {
  def url_prefix = api_prefix + "movie/"

  val byImdbUrl = url_prefix + "imdb/%s"

  def byImdb(imdb: String) = get[Movie](byImdbUrl.format(imdb))

  def postReview(r: MovieReviewPosted) = super.postReview(r)
}

case class Author(name: String)

case class Attribute(language: List[String], pubdate: List[String], title: List[String], country: List[String], writer: List[String], director: List[String], cast: List[String], movie_duration: List[String], year: List[String], movie_type: List[String])

case class MovieReview(id: Long, title: String, alt: String, author: User, movie: Movie, rating: Rating,
                       votes: Int, useless: Int, comments: Int, summary: String, published: Date, updated: Date)
  extends Review(id, title, alt, author, rating, votes, useless, comments, summary, published, updated)

case class Movie(id: String, title: String, author: List[Author], image: String, rating: RatingDetail, summary: String, tags: List[Tag], alt: String, alt_title: String, mobile_link: String, attrs: Attribute)

/**
 *
 * @param movie 电影id
 * @param title 标题
 * @param content 内容
 * @param rating  打分1-5
 */
case class MovieReviewPosted(movie: String, title: String, content: String, rating: Int = 0) extends ReviewPosted(title, content, rating)

case class MovieSearchResult(start: Int, count: Int, total: Int, movies: List[Movie])
