package com.douban.models;

import com.douban.common.BaseJavaTest;
import org.testng.annotations.Test;
import scala.collection.immutable.HashMap;
import scala.collection.immutable.Map;


/**
 * @author joseph
 * @since 4/22/13  2:14 PM
 */
public class BookJavaTest extends BaseJavaTest {
    Long bookId = 5026742l;
    String isbn = "9787508832074";
    String content = "《网络至死:如何在喧嚣的互联网时代重获我们的创造力和思维力》内容简介：我们也许已经晓悟，但也许并未察觉，我们正陷入空前的“网络统治一切”的危机之中，就像赫胥黎在《美丽新世界》中忧虑的那样，人们会渐渐爱上压迫，崇拜那些使他们丧失思考能力的技术，而现在这技术等同于网络。我们越来越依赖虚拟世界——网络所创造的一切，人们已经无法想象没有电脑、没有网络的生活，我们好像患上了某种强迫症，一遍遍地刷新网页，而短暂的断网也使我们心绪不宁。注意力极易分散，记忆力严重退化，想象力和创造力被极度扼杀……我们在网络越来越强大的信息世界面前，越来越措手不及，越来越被机器所主宰。";
    @Test
    public void testTheAnnotationsOfBook() {
        prettyJSON(Book.annotationsOf(bookId, new AnnotationSearch("collect", 2,"")));
    }
    @Test public void testTheBookAnnotationOfUser() {
        prettyJSON(Book.annotationsOfUser("jinntrance"));
    }
    @Test public void testPostAnnotation() {
    Map photos = new HashMap();
    photos.updated( "1" , picPath);

      AnnotationPosted a2p = new AnnotationPosted(content, 2, "第三章", "public");
        a2p.files_$eq(photos);
        Annotation a = Book.postAnnotation(bookId, a2p, true).get();
        prettyJSON(a);
        prettyJSON(Book.annotation(a.id(),""));
        a2p.content_$eq("add new"+a2p.content());
        prettyJSON(Book.updateAnnotation(a.id(), a2p,true)) ;
        prettyJSON(Book.deleteAnnotation(a.id()));
    }
    @Test public void testSearch() {
        Book.search("Book", "",1,20);
    }
    @Test public void testById() {
        Book.byId(bookId);
    }
    @Test public void testByISBN() {
        prettyJSON(Book.byISBN(isbn));
    }
    @Test public void testPopTags() {
        prettyJSON(Book.popTags(bookId));
    }
    @Test public void testUserTags() {
    }
    @Test public void testBookReview() {
        Book.postReview(new BookReviewPosted(bookId.toString(), "呵呵", content, 4), true).get();
//        prettyJSON(Book.updateReview(review.id, new BookReviewPosted("", "呵呵", content, 4)));
//        prettyJSON(Book.deleteReview(review.id))
        prettyJSON(Book.deleteCollection(bookId));
    }
    @Test public void testCollectionOfUser() {
        prettyJSON(Book.collectionsOfUser(userId,new CollectionSearch("read","",0,"","")));
    }
    @Test public void testBookCollection() {
        CollectionPosted p = new CollectionPosted("read", "文政哲 Internet", "對於工具，我們應該更好的應用，而不是爲所累。信息過載的時代，利用好工具，攫取於我們最有益的信息 。", 5,"public");
        Collection c = Book.postCollection(bookId, p, true).get();
        prettyJSON(c);
        prettyJSON(Book.collectionOf(bookId)) ;
        prettyJSON(Book.updateCollection(bookId, p,true));
        prettyJSON(Book.deleteCollection(bookId));
    }
}
