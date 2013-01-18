package com.douban.models

import com.douban.common.BaseTest

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/18/13 4:46 PM
 * @version 1.0
 * @see http://developers.douban.com/wiki/?title=api_v2
 */
 
 class BubblerTest extends BaseTest{
  test("bubbler"){
    prettyJSON(Bubbler.user(userId))
    prettyJSON(Bubbler.bubs(userId))
    prettyJSON(Bubbler.boards(7))
    prettyJSON(Bubbler.walls(userId))
  }

}
