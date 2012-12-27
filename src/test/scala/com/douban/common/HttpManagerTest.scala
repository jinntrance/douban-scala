package com.douban.common

import org.scalatest.FunSuite
import net.liftweb.json.Extraction._
import net.liftweb.json.JsonAST.{JObject, JString, JValue, JField}

/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 12/26/12 10:37 PM
 * @version 1.0
 */
class HttpManagerTest extends FunSuite{
    test("the auth url"){
        println(new AuthorizationCode().flat)
    }
}
