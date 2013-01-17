package com.douban.models

import java.util.Date
import com.douban.common.Req
import Req._

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/12/13 3:40 PM
 * @version 1.0
 */
object Event extends API[Event] with CommentTrait[Event] with DiscussionTrait[Event] {
  def url_prefix = api_prefix + "event/"

  val participantsUrl = url_prefix + "%s/participants"
  val wishersUrl = url_prefix + "%s/wishers"
  val user_createdUrl = url_prefix + "user_created/%s"
  val user_participatedUrl = url_prefix + "user_participated/%s"
  val user_wishedUrl = url_prefix + "user_wished/%s"
  val listUrl = url_prefix + "list"

  /**
   * 参加活动
   * @param p 时间格式：“％Y-％m-％d”，无此参数则时间待定
   */
  def participate(eventId: String, p: ParticipateDate = null) = post(participantsUrl.format(eventId), p)

  /**
   * 取消参加活动
   */
  def unParticipate(eventId: String) = delete(participantsUrl.format(eventId))

  /**
   * 感兴趣
   */
  def wish(eventId: String) = post(wishersUrl.format(eventId), null)

  /**
   * 不感兴趣
   */
  def unWish(eventId: String) = delete(wishersUrl.format(eventId))

  /**
   * 活动参与人
   */
  def participants(eventId: String) = get[UserSearchResult](participantsUrl.format(eventId))

  /**
   * 活动感兴趣的
   */
  def wishers(eventId: String) = get[UserSearchResult](wishersUrl.format(eventId))

  /**
   * 用户创建的活动
   */
  def eventsUserCreated(userId: String) = get[EventList](user_createdUrl.format(userId))

  /**
   * 用户参加的
   */
  def eventsUserParticipated(userId: String) = get[EventList](user_participatedUrl.format(userId))

  /**
   * 用户感兴趣的活动
   * @return EventList
   */
  def eventsUserWished(userId: String) = get[EventList](user_wishedUrl.format(userId))

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

case class Loc(parent: String, habitable: String, id: String, name: String, uid: String)

case class LocList(start: Int, count: Int, total: Int, locs: List[Loc]) extends ListResult(start, count, total)

case class EventSearch(loc: String, day_type: String = "", `type`: String = "") extends Bean

case class Event(is_priv: String, participant_count: Int, image: String, adapt_url: String, begin_time: Date, owner: User, alt: String, geo: String, id: String, album: String, title: String,
                 wisher_count: Int, content: String, `image-hlarge`: String, end_time: Date, `image-lmobile`: String, has_invited: String, can_invite: String, address: String, loc_name: String, loc_id: String)

case class GPS(latitude: Double, longitude: Double)

case class EventList(start: Int, count: Int, total: Int, events: List[Event]) extends ListResult(start, count, total)

/**
 * @param participate_date  时间格式：“％Y-％m-％d”，无此参数则时间待定
 */
case class ParticipateDate(participate_date: String) extends Bean

