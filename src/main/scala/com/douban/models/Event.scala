package com.douban.models

import java.util.Date
import com.douban.common.Req
import Req._
import java.util
import scala.language.implicitConversions

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/12/13 3:40 PM
 * @version 1.0
 */
object Event extends  ParticipationTrait[Event,EventList] with CommentTrait[Event] with DiscussionTrait[Event] {
  def url_prefix = api_prefix + "event/"

  val wishersUrl = url_prefix + "%s/wishers"
  val user_wishedUrl = url_prefix + "user_wished/%s"
  val listUrl = url_prefix + "list"

  /**
   * 感兴趣
   */
  def wish(eventId: Long):Boolean = None==post(wishersUrl.format(eventId), null,withResult = false)

  /**
   * 不感兴趣
   */
  def unWish(eventId: Long) = delete(wishersUrl.format(eventId))


  /**
   * 活动感兴趣的
   */
  def wishers(eventId: Long) = get[UserSearchResult](wishersUrl.format(eventId))


  /**
   * 用户感兴趣的活动
   * @return EventList
   */
  def eventsUserWished(userId: Long) = get[EventList](user_wishedUrl.format(userId))

  /**
   * 查询活动
   * @param location 城市id
   * @param dayType 时间类型	future, week, weekend, today, tomorrow
   * @param eventType 活动类型	all,music, film, drama, commonweal, salon, exhibition, party, sports, travel, others
   * @return
   */
  def events(location: String, dayType: String = "", eventType: String = "") = get[EventList](new EventSearch(location, dayType, eventType).flatten(listUrl))


  implicit def string2geo(s: String): GPS = {
    val locs = s.split(' ')
    GPS(locs(0).toDouble, locs(1).toDouble)
  }

  implicit def string2bool(s: String): Boolean = if (s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("true")) true else false
}

object Loc extends API[Loc] {
  def url_prefix = api_prefix + "loc"

  val listUrl = url_prefix + "/list"

  def list = get[LocList](listUrl)
}

abstract class ParticipationTrait[T:Manifest,L:Manifest] extends API[T]{
  val participantsUrl = url_prefix + "/%s/participants"
  val user_createdUrl = url_prefix + "/user_created/%s"
  val user_participatedUrl = url_prefix + "/user_participated/%s"
  /**
   * 参加活动
   * @param p 时间格式：“％Y-％m-％d”，无此参数则时间待定
   */
  def participate(targetId: Long, p: ParticipateDate = null):Boolean = None==post(participantsUrl.format(targetId), p,withResult = false)

  /**
   * 取消参加活动
   */
  def unParticipate(targetId: Long) = delete(participantsUrl.format(targetId))
  /**
   * 活动参与人
   */
  def participants(targetId: Long) = get[UserSearchResult](participantsUrl.format(targetId))


  /**
   * 用户创建的活动
   */
  def targetsUserCreated(userId: Long) = get[L](user_createdUrl.format(userId),secured=true)

  /**
   * 用户参加的
   */
  def targetsUserParticipated(userId: Long) = get[L](user_participatedUrl.format(userId),secured=true)
}

case class Loc(parent: String, habitable: String, id: Long, name: String, uid: String)  extends Bean

case class LocList(start: Int, count: Int, total: Int, locs: util.List[Loc]) extends ListResult(start, count, total)

case class EventSearch(loc: String, day_type: String = "", `type`: String = "") extends Bean

case class Event(is_priv: String, participant_count: Int, image: String, adapt_url: String, begin_time: Date, owner: User, alt: String, geo: String, id: Long, album: String, title: String,
                 wisher_count: Int, content: String, `image-hlarge`: String, end_time: Date, `image-lmobile`: String, has_invited: String, can_invite: String, address: String, loc_name: String, loc_id: Long)  extends Bean

case class GPS(latitude: Double, longitude: Double)   extends Bean

case class EventList(start: Int, count: Int, total: Int, events: util.List[Event]) extends ListResult(start, count, total)

/**
 * @param participate_date  时间格式：“％Y-％m-％d”，无此参数则时间待定
 */
case class ParticipateDate(participate_date: String) extends Bean

