package com.douban.models

import java.util.{Date, List}
import com.douban.common.Req._

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 8/17/13 10:22 AM
 * @version 1.0
 */
case class Place(id:Long,name:String,url:String,pic:String,zoom:Int,latitude:Double,longitude:Double)
case class TravelDate(year:Int,month:Int,day:Int)
case class Travel(status: String, comment: String, rating: Int,status_label: String,place: Place,date: TravelDate)
case class TravelResult(start: Int, count: Int, total: Int, places: List[Travel]) extends ListResult(start, count, total)

/**
 *
 * @param status 选填（想去：wish 在这儿：visiting 去过：visited）默认为所有状态
 */
case class TravelSearch(status:String="",start:Int=0,count:Int=20) extends Bean

object Place extends API[Place]{
  protected def url_prefix: String = api_prefix+"travel/place"
  protected val collection_url=api_prefix+"travel/user/%s/collections"
  def userCollections(userId:Long,s:TravelSearch=TravelSearch())=get[TravelResult](s.flatten(collection_url.format(userId)))
}
