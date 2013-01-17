package com.douban.models

import com.douban.common.BaseTest

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/28/12 2:36 AM
 * @version 1.0
 */
class UserTest extends BaseTest {
  test("test the user search") {
    prettyJSON(User.search("Jinn"))
  }
  test("test my user info") {
    prettyJSON(User.ofMe)
  }
  test("test by Id") {
    prettyJSON(User.byId(userId.toLong))
  }
}
