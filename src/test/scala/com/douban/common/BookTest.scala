package com.douban.common

import com.douban.models._

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/3/13 11:56 PM
 * @version 1.0
 */
class BookTest extends BaseTest {
  val bookId = "6797555"
  val isbn = "9787508832074"
  test("the book search") {
    prettyJSON(Book.search("Book", ""))
  }
  test("the book byId") {
    prettyJSON(Book.byId(bookId))
  }
  test("the book byISBN") {
    prettyJSON(Book.byISBN(isbn))
  }
  test("the book postReview") {
    prettyJSON(Book.postReview(ReviewPosted))
  }
  test("the book collection") {
    prettyJSON(Book.collectionOf(bookId))
  }
}
