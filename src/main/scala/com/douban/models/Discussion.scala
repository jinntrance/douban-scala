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
object Discussion extends API[Discussion, DiscussionsResult] {
  override def url_prefix = api_prefix + "discussion/"

  override def searchUrl = api_prefix + "/target/%s/discussions"

  val discussionsUrl = "/target/%s/discussions"

  def postDiscussion(id: String, d: Discussion) = postNoResult(discussionsUrl.format(id), d)

  def updateDiscussion(discussionId: String, d: Discussion) = putNoResult(byIdUrl.format(discussionId), d)

  def deleteDiscussion(discussionId: String) = delete(byIdUrl.format(discussionId))

  def discussions(id: String) = get[DiscussionsResult](discussionsUrl.format(id))

}

case class Discussion(id: String, title: String, alt: String, created: Date, updated: Date, content: String, comments_count: Int, author: User) extends Bean

case class DiscussionsResult(start: Int, count: Int, total: Int, discussions: List[Discussion]) extends ListResult(start, count, total)
