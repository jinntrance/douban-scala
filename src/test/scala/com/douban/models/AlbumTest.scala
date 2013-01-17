package com.douban.models

import com.douban.common.BaseTest

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/17/13 9:17 PM
 * @version 1.0
 */
class AlbumTest extends BaseTest{
  val aId=63212049
  val pId=1782344133
  test("test album"){
    prettyJSON(Album.byId(aId))
    prettyJSON(Album.photos(aId))
    prettyJSON(Photo.byId(pId))
  }
}
