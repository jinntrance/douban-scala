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
  val annoId = "23475182"
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
    prettyJSON(Book.postReview(new ReviewPosted(bookId, "呵呵", "我们太久没有毫无保留地开心过了。 在水泥钢筋的都市里，你是否常常被浮躁、焦虑的情绪包裹着，不能畅快呼吸？你是否觉得茫茫人海，却始终难觅知己，心无所依？ 素黑这样对你说：爱自己的首要条件不是要承担什么，反而要懂得放下：放下面子，放下乱想，不问理由地先强壮自己的身体。回归生活的细节，不管际遇和心情如何，我们有责任先吃好一顿饭，睡好一个觉，打点自己，收拾自己。 《自爱，无须等待》像一面照见事实的镜子，让读者在了解关于“爱”的实相过程中，开始真正学会爱自己。全书围绕“修养”、“平静”、“成长”、“情爱”等18项人生大命题，告诉读者：爱自己，从看清自己开始。 呼吸就是生命，活着就是幸福。 爱自己，从即刻就开始！")))
  }
  test("the book collection") {
    prettyJSON(Book.collectionOf(bookId))
  }
  test("the annotation by Id collection") {
    val a = Book.annotation(annoId)
    prettyJSON(a)
  }
}
