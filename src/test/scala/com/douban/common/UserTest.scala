package com.douban.common

import com.douban.models.UerSearch
import javax.naming.directory.SearchResult

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/28/12 2:36 AM
 * @version 1.0
 */
class UserTest extends BaseTest{
     test("test the user"){
       prettyJSON(Req.get[SearchResult](new UerSearch("阿",0,10).searchUrl))
     }
}
