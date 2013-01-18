package com.douban.models

import com.douban.common.BaseTest
import java.util.Date

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/18/13 2:22 PM
 * @version 1.0
 * @see http://developers.douban.com/wiki/?title=api_v2
 */
 
 class OnlineTest extends BaseTest{
    val oId=11438442
    test("test online"){
      prettyJSON(Online.onlinesList("day"))
      prettyJSON(Online.byId(oId))
      prettyJSON(Online.discussions(oId))
      prettyJSON(Online.participants(oId))
      prettyJSON(Online.targetsUserParticipated(userId))
      prettyJSON(Online.targetsUserCreated(userId))
    }
    test("user interaction"){
      prettyJSON(Online.participate(oId))
      prettyJSON(Online.unParticipate(oId))
      prettyJSON(Online.likeOnline(oId))
      prettyJSON(Online.unlikeOnline(oId))
    }
    test("test creaation"){
      val op=new OnlinePosted("曾經","那是回不去的曾經",new Date(),new Date(2013,3,1))
      val o=Online.createOnline(op).get
      prettyJSON(o)
      prettyJSON(Online.updateOnline(o.id,op))
      prettyJSON(Online.deleteOnline(o.id))
    }
}
