###豆瓣 API v2的非官方Scala/Java/Android SDK

java也可使用本SDK，但不如Scala使用便利。

本SDK使用scala 2.11，json處理使用GSON。

其他语言SDK请参看豆瓣API小组<http://www.douban.com/group/dbapi/>

使用时也可参照test下的内容。

Scala <https://github.com/jinntrance/douban-scala/blob/master/src/test/scala/com/douban/models/AuthTest.scala>

Java <https://github.com/jinntrance/douban-scala/blob/master/src/test/java/com/douban/models/AuthJavaTest.java>

####开发注意事项

1.请求成功返回结果形式如Option<AccessTokenResult>，使用其get()获取想要结构AccessTokenResult；失败则返回None。

2.很多接口添加了withResult参数，默认为true。例如发表评论后，知道成功失败与否即可，不必返回发表成功后的评论信息，则可设置其为false节省流量。

3.最好使用maven添加函數依赖，这样可以同时下载source和javadoc便于开发

####java/android使用注意

1.getter直接使用"属性+()"的方法，如Auth.api_key()，setter使用Auth.api_key$_eq("you key")

2.Android开发需要添加proguard 参数，参照链接中proguard 配置文件 <https://github.com/jinntrance/douban-android/blob/master/proguard.cfg>

3.其他使用可refer to <http://twitter.github.io/scala_school/java.html>


目前已完成的接口有：
```
* 用户 User
* 读书 Book
* 电影 Movie
* 音乐 Music
* 同城活动 Event
* 论坛 Discussion
* 日记 Note
* 相册 Album
* 图片 Photo
* 回复 Comment
```
待完成的(后面几个接口很难申请权限,接口中部分已测试通过)：

```
* 广播 Status
* 豆邮 Doumail
* 线上活动 Online
* 我去 Place
```

### 开发配置
使用maven开发，在pom.xml中添加如下配置
```

<dependency>
    <groupId>com.douban</groupId>
    <artifactId>scala-api_2.11.0-RC3</artifactId>
    <version>2.4.5</version>
</dependency>

```
使用SBT作scala开发，在build.sbt中添加如下配置(注意scala需要是2.10)

使用sbt作android开发可以参照配置<https://github.com/jinntrance/douban-android>
```

libraryDependencies += "com.douban" % "scala-api_2.11.0-RC3" % "2.4.5" withSources() withJavadoc()

```


### 使用说明

#### OAuth 2.0 认证
Scala 参见<https://github.com/jinntrance/douban-scala/blob/master/src/test/scala/com/douban/models/AuthTest.scala>

Java <https://github.com/jinntrance/douban-scala/blob/master/src/test/java/com/douban/models/AuthJavaTest.java>
## 引导用户授权

```
    Desktop.getDesktop.browse(new URI(Auth.getAuthUrl(Auth.api_key)))
    val codeUrl = readLine("please copy the url here after authorization>\n")
    val code = Auth.extractCode(codeUrl) //取出 authorization code
    val token:AccessTokenResult=Auth.getTokenByCode(code,api_key,secret) //取回 access token
```
##如果有之前有refresh token，则可用

```
    val refresh_token="your access token"
    val token: AccessTokenResult = Auth.getTokenByFresh(refresh_token,api_key,secret)

```

#### 接口说明

默认参数（参考豆瓣官方文档）,查询时分页需要:
```
start: 0
count: 20

```
所有返回数据以豆瓣官方文档为准，各接口末尾处均有相应链接入口。

__所有 __
```
通过id查找当前对象.byId(id),例如 获取一本图书信息 Book.byId(id)
使用任何接口前，需要设置授权后的Access Token：Req.init(access_token) 
Scala <https://github.com/jinntrance/douban-scala/blob/master/src/test/scala/com/douban/common/BaseTest.scala>
Java <https://github.com/jinntrance/douban-scala/blob/master/src/test/java/com/douban/common/BaseJavaTest.java>
```
__图书、电影、音乐 Book/Movie/Music__
```
获取图书标签 .tagsOf(id) ,id为当前对象id
获取某个Item中标记最多的标签 .popTags(id)
发表新评论 .postReview(r),其中r为BookReviewPosted/MovieReviewPosted/MusicReviewPosted
修改评论  .updateReview(reviewId,r),其中rF为ReviewPosted
删除评论 .deleteReview(reviewId)
搜索 .search(query: String, tag: String, page: Int = 1, count: Int = 20)，其中query为关键字，tag为标签，page为查询第几页，count为每页显示数量
```

__用户 User__
<http://developers.douban.com/wiki/?title=user_v2>
```
# 以下 id 指用户数字 id
当前用户 User.ofMe
指定用户 User.byId(id)
搜索用户 User.search(q,page=1,count=20)       // q: 搜索的关键词,page查询第几页，count每页多少
```
__读书 Book__
<http://developers.douban.com/wiki/?title=book_v2>
```
# 以下 id 指图书条目数字 id
# q: 关键词, tag: 标签

通过isbn获取信息 Book.byISBN(isbn_number)
获取某个用户的所有图书收藏信息 Book.collectionsOfUser(userId)
获取用户对某本图书的收藏信息 Book.collectionOf(bookId)
用户收藏某本图书 postCollection(bookId,c: CollectionPosted)
用户修改对某本图书的收藏 updateCollection(bookId,c: CollectionPosted)
用户删除对某本图书的收藏  deleteCollection(bookId)
获取某个用户的所有笔记 annotationsOfUser(userId)
获取某本图书的所有笔记 annotationsOf(bookId)
获取某篇笔记的信息 annotation(annotationId)
用户给某本图书写笔记 postAnnotation(bookId,a: AnnotationPosted)
用户修改某篇笔记 updateAnnotation((annotationId: Long, a: AnnotationPosted)
用户刪除某篇笔记 deleteAnnotation(annotationId)

```

__电影 Movie__
<http://developers.douban.com/wiki/?title=movie_v2>
```
# 以下 id 指电影条目数字 id
通过imdb获取电影 Movie.byImdb(imdb_number)

```

__音乐 Music__
<http://developers.douban.com/wiki/?title=music_v2>
```
见"图书、电影、音乐 Book/Movie/Music"

```

__日记 Note__
<http://developers.douban.com/wiki/?title=note_v2>
```
# 以下 id 指日记数字 id
# format: html_full, html_short, abstract, text，默认为text
获取一篇日记 Note.byId(id, format='text')
新写一篇日记 Note.postNote(n:NotePosted)
更新一篇日记 Note.update(noteId:Long,n:NotePosted)
上传图片到日记 Note.uploadPicture(noteId:Long,n:NotePosted)//Note.genPicFormat,genUrlFormat生成图片和url格式放在日记内容中
删除一篇日记 Note.deleteNote(id)

喜欢一篇日记     Note.like(id)
取消喜欢一篇日记 Note.unlike(id)

获取用户日记列表       Note.notesUserCreated(userId, start, count)
获取用户喜欢的日记列表 Note.notesUserLiked(userId, start, count)

```

__相册 Album__
<http://developers.douban.com/wiki/?title=album_v2>
<http://developers.douban.com/wiki/?title=photo_v2>
```
# 以下 id 指相册数字 id
# desc 描述文字
获取相册或图片 Album.byId Photo.byId
获取相册图片列表 Album.photos(id, s:AlbumSearch)

```

__线上活动 Online__
<http://developers.douban.com/wiki/?title=online_v2>

```
# 以下 id 指线上活动数字 id
# begin_time, end_time 格式为 '%Y-%m-%d %H:%M:%S'
# cate 可选值: day, week, latest
发表一条线上活动 Online.createOnline(o:OnlinePosted)
更新一条线上活动 Online.updateOnline(onlineId:Long,o:OnlinePosted)
删除一条线上活动 Online.deleteOnline(id)

喜欢一条线上活动 Online.likeOnline(id)
取消喜欢线上活动 Online.unlikeOnline(id)

获取线上活动图片列表 Online.photos(id, start, count)
上传图片到线上活动   Online.upload(id, image) # image = open('xxx.jpg')

获取参加线上活动成员列表 Online.targetsUserParticipated(id)


参加活动 Online.participate(targetId,p: ParticipateDate )
取消参加活动 Online.participate(targetId)
活动参与人 Online.participants
获取线上活动列表 Online.onlinesList(cate="week")
获取参加过的活动 Online.targetsUserParticipated(userId)
获取创建过的活动 Online.targetsUserCreated(userId)

```


__同城活动 Event__
<http://developers.douban.com/wiki/?title=event_v2>
```
# 以下 id 指同城活动 id
# Loc: 城市
# dayType: dayType 时间类型	future, week, weekend, today, tomorrow
# type: 活动类型	all,music, film, drama, commonweal, salon, exhibition, party, sports, travel, others
搜索同城活动 Event.events(location,dayType, type)

参加同城活动 Event.participate(id)
取消参加活动 Event.unParticipate(id)

对同城活动感兴趣 Event.wish(id)
取消同城活动兴趣 Event.unwish(id)

某同城活动参加者   Event.participants(id)
某同城活动感兴趣者 Event.wishers(id)

获取用户创建过的同城活动 Event.targetsUserCreated(userId)
获取用户参加过的同城活动 Event.participated(userId)
获取用户感兴趣的同城活动 Event.eventsUserWished(userId)

获取城市列表 Loc.list
获取某個城市列表信息 Loc.byId
```

__论坛 Discussion__
<http://developers.douban.com/wiki/?title=discussion_v2>
```
# 以下 discussionId 指论坛帖子 id
# target 指相应产品线（如 online, event 等）
# targetId 指相应产品 id
获取帖子 Discussion.byId(id)
发表帖子 Discussion.postDiscussion(targetId: Long, d: DiscussionPosted)
更新帖子 Discussion.updateDiscussion(discussionId, d: DiscussionPosted)
删除帖子 Discussion.deleteDiscussion(discussionId)

```
__评论 Comment__
<http://developers.douban.com/wiki/?title=comment_v2>

```
// target 指相应产品线（如 Online, Event，Note,Album 等）
新加一条回复 postComment(targetId:Long,content:String,
获取某条回复 getComment(targetId:Long,commentId:String)
删除某条回复 deleteComment(targetId:Long,commentId:String)
```
__豆瓣实验室 Bubbler__
<http://developers.douban.com/wiki/?title=labs>

```
用戶信息 Bubbler.user(userId)
用戶Bubs Bubbler.bubs(userId)
用戶boards Bubbler.boards(boardId)
用戶walls Bubbler.walls(userId)

```

__我去接口 Place__
<http://developers.douban.com/wiki/?title=travel_v2>
```
获取地点信息 Place.byId(placeId)
获取某个用户的地点收藏 Place.userCollections(userId)   //可选参数 TravelSearch

```


### 联系
* 使用 douban-scala 过程中遇到 bug, 可以到 [Issues](https://github.com/jinntrance/douban-scala/issues) 或 [豆瓣小组] (http://www.douban.com/group/topic/36158803/) 反馈
* 本文档很多参照豆瓣Python SDK的文档，多谢。

