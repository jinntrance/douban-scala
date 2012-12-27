package com.douban.common

import com.douban.user.API
import com.douban.user.API.{SearchResult, Search}

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/28/12 2:36 AM
 * @version 1.0
 */
class UserTest extends BaseTest{
     test("test the user"){
       val r=new HttpManager(new Search("阿",0,10).searchUrl).get().parseJSON[SearchResult]()
       prettyJSON(r)
     }
}
