package com.douban.models
import java.util.Date
import com.douban.common.Req._

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/18/13 3:29 AM
 * @version 1.0
 */
object Online extends ParticipationTrait[Online,OnlineList] with DiscussionTrait[Online] with PhotosTrait[Online]{
  protected def url_prefix = api_prefix+"online/"
  private val onlinesUrl=api_prefix+"onlines"
  private val likeUrl=idUrl+"/like"

  /**
   *
   * @param cate  day，week，latest分别对应每天，每周，最新,默认每周
   * @return
   */
  def onlinesList(cate:String="week")=get[OnlineList](s"$onlinesUrl?cate=$cate",secured=true)

  /**
   * 创建线上活动
   */
  def createOnline(o:OnlinePosted,withResult:Boolean=true)=post[Online](onlinesUrl,o,withResult)
  /**
   * 更新线上活动
   */
  def updateOnline(onlineId:Long,o:OnlinePosted,withResult:Boolean=true)=put[Online](idUrl.format(onlineId),o,withResult)
    /**
   * 删除线上活动
   */
  def deleteOnline(onlineId:Long)=delete(idUrl.format(onlineId))

  /**
   * 喜欢线上活动
   */
  def likeOnline(onlineId:Long):Boolean=None==post(likeUrl.format(onlineId),null,withResult = false)

  /**
   * 取消喜欢线上活动
   */
  def unlikeOnline(onlineId:Long)=delete(likeUrl.format(onlineId))

  /**
   *获取用户参加的线上活动列表
   * @param exclude_expired   是否包括过期活动	true，false，默认为包含过期
   */
  def targetsUserParticipated(userId:Long,exclude_expired:Boolean=false)=get[OnlineList](participantsUrl.format(userId)+s"?exclude_expired=$exclude_expired")
}

/**
 *
 * @param shuo_topic  对应广播的#主题#
 * @param cascade_invite 用户能不能邀请友邻加入
 * @param group_id 关联小组的id
 * @param album_id   对应相册的id
 * @param participant_count  参加人数
 * @param recs_count  推荐数
 * @param liked  当前用户是否喜欢，参加
 */
case class Online(id:Long,title:String,desc:String,tags:List[String],created:Date,begin_time:Date,end_time:Date,related_url:String,shuo_topic:String,cascade_invite:Boolean,
                  group_id:Long,album_id:Long,participant_count:Int,photo_count:Int,liked_count:Int,recs_count:Int,thumb:String,cover:String,image:String,owner:User,liked:Boolean,participated:Boolean){
  val alt=s"http://www.douban.com/online/$id"
}
case class OnlineList(start:Int,count:Int,total:Int,user:User,onlines:List[Online]) extends ListResult(start,count,total)

/**
 *
 * @param begin_time 不是是过去的时间，时间格式"%Y-%m-%d %H:%M"
 * @param end_time 不能早于开始时间，活动期限不能长于3个月(90天)
 * @param tags  不超过4个，用空格分开，默认为空
 * @param related_url   关联的url或者小组链接
 * @param cascade_invite  是否允许参与的成员邀请朋友参加
 */
case class OnlinePosted(title:String,desc:String,begin_time:Date,end_time:Date,tags:String="",related_url:String="",cascade_invite:Boolean=false) extends Bean