package com.douban.models
import java.util.Date
import com.douban.common.Req._

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 1/17/13 5:26 PM
 * @version 1.0
 * @see http://developers.douban.com/wiki/?title=api_v2
 */
 
 object Album extends API[Album] with PhotosTrait[Album]{
  protected def url_prefix: String = api_prefix+"album"

}

object Photo extends API[Photo] with CommentTrait[Photo]{
  protected def url_prefix: String = api_prefix+"photo"

}

trait PhotosTrait[T] extends API[T]{
  private val photosUrl=idUrl+"/photos"
  def photos(targetId:Long,s:AlbumSearch=new AlbumSearch)=get[AlbumsResult](s.flatten(photosUrl.format(targetId)),secured = true)
}
case class AlbumSize(icon:List[Int],thumb:List[Int],cover:List[Int],image:List[Int])
case class Photo(id:Long,alt:String,album_id:String,album_title:String,icon:String,thumb:String,cover:String,image:String,desc:String,created:Date,privacy:String,position:Int,prev_photo:String,next_photo:String,liked_count:Int,recs_count:Int,author:User,liked:Boolean,sizes:AlbumSize)
case class Album(id:Long,alt:String,title:String,icon:String,cover:String,thumb:String,image:String,privacy:String,created:Date,updated:Date,liked_count:Int,recs_count:Int,size:Int,desc:String,author:User,liked:Boolean,sizes:AlbumSize)

/**
 * @param order 排序	asc, desc, 默认为相册本身的排序
 * @param sortby 排序方式	time 上传时间，vote 推荐数，comment 回复数，默认为time
 */
case class AlbumSearch(start:Int=0,count:Int=20,order:String="",sortby:String="time") extends Bean
case class AlbumsResult(start:Int,count:Int,total:Int,photos:List[Photo]) extends ListResult(start,count,total)

