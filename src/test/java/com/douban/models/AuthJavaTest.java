package com.douban.models;

import com.douban.common.AccessTokenResult;
import com.douban.common.Auth;
import com.douban.common.BaseJavaTest;
import org.testng.annotations.Test;
import scala.Option;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author joseph
 * @since 4/22/13  12:03 PM
 */
public class AuthJavaTest extends BaseJavaTest{
    @Test public void testAuth() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI(Auth.getAuthUrl(Auth.api_key(), Auth.redirect_url(), "")));
    }
    @Test public void testAccessToken() {
        prettyJSON(Auth.getTokenByCode("d2fed1a8ac7edf66",api_key,secret,Auth.redirect_url()));
    }
    @Test public void testRefreshToken() {
        Option<AccessTokenResult> t=Auth.getTokenByFresh(refresh_token, api_key, secret, Auth.redirect_url());
        prettyJSON(t.get());

    }

}
