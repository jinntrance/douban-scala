package com.douban.models
import java.util.Date
import com.douban.common.Req._
import java.util.List
/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/18/13 3:58 PM
 * @version 1.0
 * @see http://developers.douban.com/wiki/?title=api_v2
 */
 object Bubbler extends API[Bubbler]{
  protected def url_prefix=bub_prefix
  private val userUrl=url_prefix+"/user/%s"
  private val userBubsUrl=url_prefix+"/user/%s/bubs"
  private val boardUrl=url_prefix+"/board/%s"
  private val wallUrl=url_prefix+"/wall/%s"

  def user(userId:Long)=get[BubUser](userUrl.format(userId))
  def bubs(userId:Long)=get[BubblerList](userBubsUrl.format(userId))
  def boards(boardId:Long)=get[BoardList](boardUrl.format(boardId))
  def walls(userId:Long)=get[Wall](wallUrl.format(userId))
}
case class Bubbler(id:Long,content:String,time:Date,song:Song)
case class Song(album:String,song_name:String,cover:String,artist:String)
case class SongList(song:Song,bubs:List[Bubbler])
case class BubblerList(r:Int,result:List[Bubbler])
case class Board(id:Long,title:String,thumbnail:String,create_time:Date,description:String,song_list:List[Long],user:BubUser)
case class BubUser(id:Long,uid:String,title:String,homepage:String,icon:String,stats:BubStatus)
case class BubStatus(bub:Int,collect:Int,board:Int)
case class BoardList(r:Int,board:Board,song_list:List[SongList])
case class Wall(user:BubUser,board_list:List[Board])