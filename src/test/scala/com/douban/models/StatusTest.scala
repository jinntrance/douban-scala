package com.douban.models

import com.douban.common.BaseTest

/**
 * Copyright by <a href="http://www.josephjctang.com"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/19/13 1:05 AM
 * @version 1.0
 */
class StatusTest extends BaseTest {
  override val userId = 1242286
  val sId = 1151548432
  test("status getting") {
    prettyJSON(Status.byId(sId))
    prettyJSON(Status.byIdPacked(sId))
  }
}
