package com.douban.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import scala.None;

/**
 * @author joseph
 * @since 4/22/13  1:50 PM
 */
public class BaseJavaTest {
    protected static String api_key = "0f86acdf44c03ade2e94069dce40b09a";
    protected static String secret = "95125490b60b01ee" ;
    protected static String access_token = "002411ca2b87e288006c355e8297a798" ;
    protected static String refresh_token = "f0d5cfd2e0060a9dd2816f78a0121fd7" ;
    protected long userId = 3705311;
    protected String picPath="/home/joseph/Downloads/ChineseToHiragana.jpg" ;
    protected static Gson gp=new GsonBuilder().setPrettyPrinting().create();
    static {
        Req.init(access_token,api_key,"");
    }

    public void prettyJSON(Object p) {
        if (None.canEqual(p)) println(p);
        else println(gp.toJson(p));
    }
    public void println(Object s){
        System.out.println(s);
    }
}
