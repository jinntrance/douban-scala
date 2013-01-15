package com.douban.models

import com.douban.common.BaseTest

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/3/13 11:56 PM
 * @version 1.0
 */
class BookTest extends BaseTest {
  val userId: String = "jinntrance"
  val bookId = "20497889"
  val isbn = "9787508832074"
  val content = "《网络至死:如何在喧嚣的互联网时代重获我们的创造力和思维力》内容简介：我们也许已经晓悟，但也许并未察觉，我们正陷入空前的“网络统治一切”的危机之中，就像赫胥黎在《美丽新世界》中忧虑的那样，人们会渐渐爱上压迫，崇拜那些使他们丧失思考能力的技术，而现在这技术等同于网络。我们越来越依赖虚拟世界——网络所创造的一切，人们已经无法想象没有电脑、没有网络的生活，我们好像患上了某种强迫症，一遍遍地刷新网页，而短暂的断网也使我们心绪不宁。注意力极易分散，记忆力严重退化，想象力和创造力被极度扼杀……我们在网络越来越强大的信息世界面前，越来越措手不及，越来越被机器所主宰。"
  test("the annotations of Book") {
    prettyJSON(Book.annotationsOf(bookId, new AnnotationSearch("collect", 2)))
  }
  test("the book annotationOfUser") {
    prettyJSON(Book.annotationsOfUser("jinntrance"))
  }
  test("the book postAnnotation") {
    //    val a = Book.postAnnotationWithResult(bookId, new AnnotationPosted(content, 2, "第一章"))
    val photos = Map(
      "1" -> "/home/joseph/Downloads/Coffee-Book-1565471.jpg"
      , "2" -> "/home/joseph/Downloads/8062382-a-cup-of-coffee-on-the-book.jpg"
    )
    val a2p = new AnnotationPosted(content, 2, "第三章")
    a2p.files = photos
    val a = Book.postAnnotationWithResult(bookId, a2p)
    prettyJSON(a)
    prettyJSON(Book.annotation(a.id))
    //    prettyJSON(Book.updateAnnotationWithResult(a.id, new AnnotationPosted(content, 3, "第二章")))
    //    prettyJSON(Book.deleteAnnotation(a.id))
  }
  test("the book search") {
    prettyJSON(Book.search("Book", ""))
  }
  test("the book byId") {
    prettyJSON(Book.byId(bookId))
  }
  test("the book byISBN") {
    prettyJSON(Book.byISBN(isbn))
  }
  test("the book popTags") {
    prettyJSON(Book.popTags(bookId))
  }
  test("the user tags") {
    prettyJSON(Book.tags(userId))
  }
  test("the book review") {
    val review = Book.postReviewWithResult(new BookReviewPosted(bookId, "呵呵", content, 4))
    prettyJSON(review)
    prettyJSON(Book.updateReviewWithResult(review.id, new BookReviewPosted("", "呵呵", content, 4)))
    prettyJSON(Book.deleteReview(review.id))
    prettyJSON(Book.deleteCollection(bookId))
  }
  test("the book collectionOfUser") {
    prettyJSON(Book.collectionsOfUser(userId))
  }
  test("the book collection") {
    val p = new CollectionPosted("read", "文政哲 Internet", "對於工具，我們應該更好的應用，而不是爲所累。信息過載的時代，利用好工具，攫取於我們最有益的信息 。", 5)
    val c = Book.postCollection(bookId, p)
    prettyJSON(c)
    prettyJSON(Book.collectionOf(bookId))
    prettyJSON(Book.updateCollectionWithResult(bookId, p))
    prettyJSON(Book.deleteCollection(bookId))
  }
}
