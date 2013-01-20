package com.douban.models

import java.util.Date
import com.douban.common.Req._

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/16/13 2:25 PM
 * @version 1.0
 */
object Note extends API[Note] with CommentTrait[Note]{
  protected def url_prefix = api_prefix+"note/"
  private val userCreatedUrl=url_prefix+"user_created/%s"
  private val userLikedUrl=url_prefix+"user_liked/%s"
  private val likeUrl=idUrl+"/like"
  private val composeUrl=api_prefix+"notes"
  /**
   * text	默认格式，文本默认，图片和url使用类BBCode标签
   * html_full	html格式的全部内容
   * html_short	html格式的摘要内容
   * abstract	文本格式的摘要内容
   * @param format format	日记内容格式	取值为html_full, html_short, abstract, text，默认为text
   */
  def byId(noteId:Long,format:String="text")=get[Note](idUrl.format(noteId)+s"?format=$format")

  /**
   * 喜欢一篇日记
   */
  def like(noteId:Long):Boolean=None==post(likeUrl.format(noteId),null,withResult = false)

  /**
   * 取消喜欢一篇日记
   */
  def unlike(noteId:Long)=delete(likeUrl.format(noteId))

  def postNote(n:NotePosted)=post[Note](composeUrl,n)

  /**
   * 更新一条日记
   * @param n 更新内容
   * @return
   */
  def update(noteId:Long,n:NotePosted,withResult:Boolean=true)=put[Note](idUrl.format(noteId),n,withResult)

  /**
   * 上传图片到日记
   * @param noteId 对应日记id
   * @param n 上传信息，可以不填写title
   * @return
   */
  def uploadPicture(noteId:Long,n:NotePosted,withResult:Boolean=true)=post[Note](idUrl.format(idUrl),n,withResult)

  /**
   * 删除一条日记
   */
  def deleteNote(noteId:Long)=delete(idUrl.format(noteId))

  /**
   *
   * @param id 图片顺序号，每篇日记内不能重复
   * @param layout 排版，有L，C，R，分别对应居左，居中，居右3种排版
   * @return
   */
  def genPicFormat(id:Int,layout:String="C",desc:String="")=s"[img=$id:$layout]$desc[/img]"

  def genUrlFormat(url:String,desc:String="")=s"[url='$url']$desc[/url]"

  /**
   *
   * @param format format	日记内容格式	取值为html_full, html_short, abstract, text，默认为text
   */
  def notesUserCreated(userId:Long,start:Int=0,count:Int=20,format:String="text")=get[NotesResult](userCreatedUrl.format(userId)+s"?format=$format",secured=true)
  /**
   *
   * @param format format	日记内容格式	取值为html_full, html_short, abstract, text，默认为text
   */
  def notesUserLiked(userId:Long,,start:Int=0,count:Int=20,format:String="text")=get[NotesResult](userLikedUrl.format(userId)+s"?format=$format",secured=true)

}

/**
 *
 * @param privacy 可见权限 public:表示所有人可见 friend:只朋友可见 private:只有自己可见
 * @param update_time  更新时间
 * @param publish_time 发布时间
 * @param photos  日记关联的图片,id->url
 * @param recs_count  被推荐的次数
 * @param alt 日记网址
 */
case class Note(id:Long,title:String,privacy:String,summary:String,content:String,update_time:Date,publish_time:Date,photos:Map[String,String]=Map(),comments_count:Int,liked_count:Int,recs_count:Int,alt:String,can_reply:Boolean)

case class NotePosted(title:String,var content:String,pics:Map[String,String]=Map(),privacy:String="public",can_reply:Boolean=true) extends Bean

case class NotesResult(start:Int,count:Int,total:Int,notes:List[Note]) extends ListResult(start,count,total)