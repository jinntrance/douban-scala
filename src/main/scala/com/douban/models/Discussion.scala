package com.douban.models

import com.douban.common.Req._
import java.util.Date

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/11/13 3:32 AM
 * @version 1.0
 */
object Discussion extends API[Discussion] {
  override def url_prefix = api_prefix + "discussion/"

  def updateDiscussion(discussionId: String, d: DiscussionPosted) = putNoResult(idUrl.format(discussionId), d)

  def updateDiscussionWithResult(discussionId: String, d: DiscussionPosted) = put[Discussion](idUrl.format(discussionId), d)

  def deleteDiscussion(discussionId: String) = delete(idUrl.format(discussionId))

}

trait DiscussionTrait[T] extends API[T]{

  val discussionsUrl = idUrl + "/discussions"

  def postDiscussion(targetId: String, d: DiscussionPosted) = postNoResult(discussionsUrl.format(targetId), d)

  def postDiscussionWithResult(targetId: String, d: DiscussionPosted) = post[Discussion](discussionsUrl.format(targetId), d)

  def discussions(targetId: String, page: Int = 0, count: Int = 20, query: String = "") = get[DiscussionsResult](new Search(query, "", page * count, count).flatten(discussionsUrl.format(targetId)))
}

case class Discussion(id: String, title: String, alt: String, created: Date, updated: Date, content: String, comments_count: Int, author: User)

case class DiscussionPosted(title: String, content: String) extends Bean

case class DiscussionsResult(start: Int, count: Int, total: Int, discussions: List[Discussion]) extends ListResult(start, count, total)
