package com.douban.models

import com.douban.common.BaseTest

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 *
 * @author joseph
 * @since 1/12/13 4:47 PM
 * @version 1.0
 */
class EventTest extends BaseTest {
  val eventId = 18014972
  val lId=108288
  test("events getting") {
    prettyJSON(Event.eventsUserParticipated(userId))
    prettyJSON(Event.eventsUserCreated(userId))
    prettyJSON(Event.eventsUserWished(userId))
    prettyJSON(Event.events("chengdu"))

    prettyJSON(Event.participants(eventId))
    prettyJSON(Event.wishers(eventId))
    prettyJSON(Event.byId(eventId))

  }
  test("events writing") {
    println(Event.participate(eventId))
    println(Event.unParticipate(eventId))
    println(Event.wish(eventId))
    println(Event.unWish(eventId))
  }
  test("location byId") {
    prettyJSON(Loc.byId(lId))
  }
  test("locations") {
    prettyJSON(Loc.list)
  }
}
