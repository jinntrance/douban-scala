package com.douban.models

import java.util.Date

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a><br/>
 * <em>see:</em><br/>
 * <a href="http://developers.douban.com/wiki/?title=music_v2">豆瓣音乐API</a>
 * @author joseph
 * @since 1/11/13 1:19 AM
 * @version 1.0
 */
object Music extends BookMovieMusicAPI[Music, MusicSearchResult, MusicReview] {
  def url_prefix = api_prefix + "music/"

}

case class MusicAttribute(publisher: List[String], singer: List[String], discs: List[String], pubdate: List[String], title: List[String], media: List[String], tracks: List[String]) {
  def tracksList = {
    var track = List[String]()
    tracks.foreach {
      t => track ++= t.split('\n').toList
    }
    track
  }
}

case class MusicSearchResult(start: Int, count: Int, total: Int, musics: List[Music]) extends ListResult(start, count, total)

case class MusicReview(id: Long, title: String, alt: String, author: User, music: Music, rating: ReviewRating,
                       votes: Int, useless: Int, comments: Int, summary: String, published: Date, updated: Date) extends Review(id, title, alt, author, rating, votes, useless, comments, summary, published, updated)

/**
 *
 * @param music 评论所针对的music id
 * @param rating 1-5分，其他为不评分
 */
case class MusicReviewPosted(music: String, title: String, content: String, rating: Int = 0) extends ReviewPosted(title, content, rating)

case class Music(id: String, title: String, alt: String, author: List[Author], alt_title: String, tags: List[Tag], summary: String, image: String, mobile_link: String, attrs: MusicAttribute, rating: ItemRating)
