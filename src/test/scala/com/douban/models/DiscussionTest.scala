package com.douban.models

import com.douban.common.BaseTest

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/12/13 12:35 AM
 * @version 1.0
 */
class DiscussionTest extends BaseTest {
  val discussionId = "51126818"
  val eventId = "17946058"

  test("test discussions list") {
    prettyJSON(Event.discussions(eventId))
  }
  test("test discussion") {
    val d = Event.postDiscussion(eventId, new DiscussionPosted("Test", "test"))
    prettyJSON(d)
    prettyJSON(Discussion.updateDiscussion(d.id, new DiscussionPosted("Test2", "test2")))
    prettyJSON(Discussion.deleteDiscussion(d.id))
  }

}
