package com.douban.models
import java.util.Date
import com.douban.common.{Req, Auth}
import Req._

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/16/13 2:12 AM
 * @version 1.0
 */
object Status extends API[Status]{
  protected def url_prefix = shuo_prefix+"statuses/"
  private val postUrl=url_prefix
  private val feedsUrl=url_prefix+"home_timeline"
  private val userFeedsUrl=feedsUrl+"/%s"
  private val commentsUrl=idUrl+"/%s/comments"
  private val commentUrl=idUrl+"comment/%s"
  private val reshareUrl=idUrl+"/%s/reshare"



  /**
   * 发布一条广播信息。请求必须用POST方式提交。豆瓣广播类型繁多，但是对外只支持‘我说’（可带图）和‘推荐网址’两种。
   * @param p  请求参数 ,上传图片大小限制为<3M
   * @return
   */
  def postStatus(p:StatusPosted,withResult:Boolean=true)=post[Status](postUrl,p,withResult)

  /**
   * 获取当前登录用户及其所关注用户的最新广播消息。
   * @param s 请求参数
   */
  def feeds(s:StatusSearch=new StatusSearch)=get[List[BookStatus]](s.flatten(feedsUrl),secured = true)

  /**
   * 获取用户发布的广播列表
   * @param userId user_id/screen_name
   * @param s 请求参数
   */
  def feedsOfUser(userId:String,s:StatusSearch=new StatusSearch)=get[List[BookStatus]](s.flatten(userFeedsUrl.format(userId)),secured = true)

  /**
   * 删除一条广播
   */
  def delete(statusId:String)=Req.delete(idUrl.format(statusId))

  /**
   *打包的信息
   * @return
   */
  def byIdPacked(statusId:String)=get[PackedStatus](s"$idUrl?pack=true")

  /**
   * 获取一条广播的回复列表
   */
  def comments(statusId:String,start:Int=0,count:Int=20)=get[List[Comment]](commentsUrl.format(statusId)+s"?start=$start&count=$count")

  /**
   * 添加一条评论
   */
  def comment(statusId:String,comment:String,withResult:Boolean=true)=post[Comment](commentsUrl.format(statusId),CommentContent(comment),withResult)

  /**
   * 获取单条回复的内容
   */
  def commentById(commentId:String)=get[Comment](commentUrl.format(commentId))

  /**
   * 删除该回复
   */
  def deleteCommentById(commentId:String)=Req.delete(commentUrl.format(commentId))


}
case class Status(category:String,reshared_count:Int,text:String,created_at:Date,title:String,can_reply:Int,liked:Boolean,attachments:List[Attachment]
                  ,source:Source,like_count:Int,comments_count:Int,user:StatusUser,is_follow:Boolean,has_photo:Boolean,`type`:String,id:Long,reshared_status:Status)
case class Source   //TODO
case class StatusUser(uid:String,id:String,`type`:String,description:String,small_avatar:String,large_avatar:String,screen_name:String)
case class StatusSize(small:List[Int],raw:List[Int],median:List[Int])
case class Media(src:String,sizes:StatusSize,secret_pid:String,original_src:String,href:String,`type`:String)
case class Properties(href:String,uid:String,name:String)
case class Topic(text:String,indices:List[Int])
//TODO user_mentions
case class Entities(user_mentions:List[String],topics:List[Topic],urls:List[String])
case class Attachment(description:String,title:String,expaned_href:String,caption:String,href:String,`type`:String,properties:Properties,media:List[Media])
case class StatusPosted(text:String,img:String="",rec_title:String="",rec_url:String="",rec_desc:String="",rec_image:String="",source:String=Auth.api_key) extends Bean{
  override def files={
   Map("image"->img)
  }
}

/**
 * @param since_id   int64	若指定此参数，则只返回ID比since_id大的广播消息（即比since_id发表时间晚的广播消息）。
 * @param until_id   int64	若指定此参数，则返回ID小于或等于until_id的广播消息
 */
case class StatusSearch(since_id:String=null,until_id:String=null,start:Int=0,count:Int=20) extends Bean

/**
 *
 * @param status  广播数据
 * @param reshare_users 转播的用户列表
 * @param comments  评论列表,
 * @param like_users  赞的用户列表
 */
case class PackedStatus(status:BookStatus,reshare_users:List[StatusUser],comments:List[String],like_users:List[StatusUser])