package com.douban.book

import net.liftweb.json.{FieldSerializer, DefaultFormats, Serialization, NoTypeHints}

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/26/12 8:07 PM
 * @version 1.0
 */
class Bean{
}
case class Book(id:BigInt,isbn10:String,isbn13:String,title:String,origin_title:String,
           alt_title:String,subtitle:String,url:String) {

}
