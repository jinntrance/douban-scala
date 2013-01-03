package com.douban.common

import com.douban.models._
/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/3/13 11:56 PM
 * @version 1.0
 */
class BookTest extends BaseTest{
  test("the book search url"){
    val s=new BookSearch("Book","")

    val http= new HttpManager(s.searchUrl).get()
    prettyJSON(http.parseJSON[BookSearchResult]())
  }
}
