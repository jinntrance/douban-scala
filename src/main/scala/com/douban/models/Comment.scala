package com.douban.models
import java.util.Date
import com.douban.common.Req._

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/16/13 8:03 PM
 * @version 1.0
 */
trait CommentTrait[T] extends API[T]{
  val commentsUrl=idUrl+"/comments"
  val commentUrl=idUrl+"/comment/%s"

  /**
   * 新发评论
   * @return
   */
  def postComment(targetId:String,content:String,withResult:Boolean=true)=post[Comment](commentsUrl.format(targetId),CommentContent(content),withResult)

  /**
   * 获取单条回复
   */
  def getComment(targetId:String,commentId:String)=get[Comment](commentUrl.format(targetId,commentId))

  /**
   * 删除回复
   */
  def deleteComment(targetId:String,commentId:String)=delete(commentUrl.format(targetId,commentId))
}
case class Comment(id:String,created:Date,content:String,author:User) extends Bean

case class CommentResult(start:Int,count:Int,total:Int,comments:List[Comment]) extends ListResult(start,count,total)

case class CommentContent(content:String) extends Bean
