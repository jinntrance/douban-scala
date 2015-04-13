package com.douban

/**
 * Copyright by <a href="http://www.josephjctang.com"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 4/21/13 10:37 AM
 * @version 1.0
 */

import scala.collection.JavaConverters._
import com.google.gson.Gson
import java.lang.reflect.Type
import com.google.gson.reflect.TypeToken
import java.util

object Main {
  def main(args: Array[String]) {
    val gson = new Gson()
    val list: util.List[String] = Seq("Dario", "Dario2").asJava
    val map = Map(1 -> 7).asJava
    val tmp: MyType = new MyType("A", list, 2, 7, false, map)

    println("Object :" + tmp)

    val json: String = gson.toJson(tmp)

    println("Object2json: " + json)

    val jsonString = """[{x:"A",y:["Dario","Dario2"],z:7,m:10,t:true,map:{h:7,y:8}}, {"x":"B","y":["Dario","Dario2"],"z":7,"m":10}]"""

    // listType konieczny do odpowiedniego przekonwertowania Jsona przez biblioteke Gson
    val listType: Type = new TypeToken[util.List[MyType]]() {}.getType
    val tmp2: util.List[MyType] = (gson.fromJson(jsonString, listType))

    println("Json2object: " + tmp2)

  }
}


case class MyType(x: String, y: util.List[String], z: Int, m: Long, t: Boolean, map: util.Map[Int, Int]) {
  def toJson = new Gson().toJson(this)

  override def toString: String = {
    s"{$x,$y,$z,$m,$t,$map}"
  }
}

object MyType