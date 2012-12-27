package com.douban.common
import org.scalatest.FunSuite
import net.liftweb.json.Extraction._
import net.liftweb.json.JsonAST._
import net.liftweb.json.Printer._
/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/27/12 11:31 PM
 * @version 1.0
 */
trait BaseTest extends FunSuite{
  implicit val formats = net.liftweb.json.DefaultFormats
   def prettyJSON(p:Any){
     println(pretty(render(decompose(p))))
   }
}
