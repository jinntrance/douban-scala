package com.douban.models
import java.util.Date
import com.douban.common.Req._
import com.douban.common.Req

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/16/13 3:07 AM
 * @version 1.0
 */
object Doumail extends API[Doumail]{
  protected def url_prefix = api_prefix+"doumail"
  private val inboxUrl=url_prefix+"/inbox"
  private val outboxUrl=url_prefix+"/outbox"
  private val unreadUrl=inboxUrl+"/unread"
  private val readUrl=url_prefix+"/read"
  private val deleteUrl=url_prefix+"/delete"
  private val sendUrl=api_prefix+"/doumails"
  /**
   * 请求一封豆邮件状态还是未读
   */
  def byIdKeepUnread(doumailId:Long)=get[Doumail](s"${idUrl.format(doumailId)}?keep-unread=true",secured = true)
  def inbox=get[List[Doumail]](inboxUrl,secured = true)
  def outbox=get[List[Doumail]](outboxUrl,secured = true)

  /**
   * 获取用户未读邮件
   */
  def unreadMails=get[List[Doumail]](unreadUrl,secured = true)

  /**
   * 批量标记豆邮为已读
   * @param doumailIds  需要标记为已读的豆邮id
   * @return
   */
  def readMails(doumailIds:List[Long])=put(readUrl,new DoumailIds(doumailIds.mkString(",")),withResult=false)

  /**
   *批量删除豆邮
   * @return
   */
  def deleteMails(doumailIds:List[Long])=post(deleteUrl,new DoumailIds(doumailIds.mkString(",")),withResult=false)

  /**
   * 发送一封豆邮
   * @param m 请求参数
   */
  def send(m:DoumailSent)=post(sendUrl,m,withResult=false)

  def delete(doumailId:Long)=Req.delete(idUrl.format(doumailId))
}

/**
 *
 * @param status  U:表示这封邮还未读 R:表示已读
 */
case class Doumail(status:String,id:Long,sender:User,receiver:User,title:String,published:Date,content:String)
case class DoumailIds(ids:String) extends Bean

/**
 *
 * @param receiver_id 接收邮件的用户id
 * @param captcha_token 系统验证码 token ,当用户发送邮件过于频繁需要使用
 * @param captcha_string 用户输入验证码  //TODO
 */
case class DoumailSent(title:String,content:String,receiver_id:Long,captcha_token:String="",captcha_string:String="") extends Bean

/**
 *  用户发送邮件过于频繁,403 返回
 * @param captcha_url  验证码图片的url
 * @param captcha_token 再次提交时需要加的token
 * @param captcha_string 再次提交时需要加用户输入的验证码字符串
 */
case class Captcha(captcha_url:String,captcha_token:String,captcha_string:String)
