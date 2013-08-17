package com.douban.models

import com.douban.common.BaseTest

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 8/17/13 10:39 AM
 * @version 1.0
 */
class TravelTest extends BaseTest{
  test("test place by id") {
    prettyJSON(Place.byId(1015176))
  }
  test("test user collected places") {
    prettyJSON(Place.userCollections(38702920))
  }
}
