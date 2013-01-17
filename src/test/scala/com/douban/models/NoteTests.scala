package com.douban.models

import com.douban.common.BaseTest

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/17/13 12:30 AM
 * @version 1.0
 */
class NoteTests extends BaseTest{
  val noteId="84240297"
    test("note"){
      prettyJSON(Note.byId(noteId))
      prettyJSON(Note.like(noteId))
      prettyJSON(Note.unlike(noteId))
    }
    test("new note"){
      val n=Note.postNote(NotePosted("Test","testtesttestesttesetes")).get
      prettyJSON(n)
      prettyJSON(Note.update(n.id,NotePosted("Test","testtesttestesttesetes")))
      prettyJSON(Note.deleteNote(n.id))
    }
    test("notes list"){
      prettyJSON(Note.notesUserCreated(userId))
      prettyJSON(Note.notesUserLiked(userId))
    }
}
