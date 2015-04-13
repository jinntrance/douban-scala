package com.douban.common

import com.douban.models._

/**
 * Copyright by <a href="http://www.josephjctang.com"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 8/17/13 10:58 AM
 * @version 1.0
 */
class AllTest extends BaseTest{
  test("test all classes") {
    val all=List[BaseTest](new AlbumTest(),new BookTest(),new BubblerTest(),new DiscussionTest(),new DoumailTest(),new EventTest(),new MovieTest(),new MusicTest(),
    new NoteTest(),new OnlineTest,new StatusTest(),new UserTest(),new TravelTest())
    all.foreach(_.execute())
  }
}
