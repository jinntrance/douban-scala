package com.douban.models

import com.douban.common.Req._
import java.util.Date
import java.util.List

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/11/13 3:32 AM
 * @version 1.0
 */
object Discussion extends DiscussionTrait[Discussion] with CommentTrait[Discussion] {
  override def url_prefix = api_prefix + "discussion/"

  def updateDiscussion(discussionId: Long, d: DiscussionPosted,withResult:Boolean=true) = put[Discussion](idUrl.format(discussionId), d,withResult)

  def deleteDiscussion(discussionId: Long) = delete(idUrl.format(discussionId))

}

trait DiscussionTrait[T] extends API[T]{

  val discussionsUrl = idUrl + "/discussions"

  def postDiscussion(targetId: Long, d: DiscussionPosted,withResult:Boolean=true) = post[Discussion](discussionsUrl.format(targetId), d,withResult)

  def discussions(targetId: Long, page: Int = 0, count: Int = 20, query: String = "") = get[DiscussionsResult](new Search(query, "", page * count, count).flatten(discussionsUrl.format(targetId)))
}

case class Discussion(id: Long, title: String, alt: String, created: Date, updated: Date, content: String, comments_count: Int, author: User)

case class DiscussionPosted(title: String, content: String) extends Bean

case class DiscussionsResult(start: Int, count: Int, total: Int, discussions: List[Discussion]) extends ListResult(start, count, total)
