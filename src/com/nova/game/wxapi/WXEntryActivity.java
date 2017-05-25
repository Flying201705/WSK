package com.nova.game.wxapi;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.nova.game.MainActivity;
import com.nova.game.R;
import com.nova.game.wsk.WXInfo;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";
    
    private static final String APP_ID = "wx545d7fe9d45d8912";
    private static final String APP_SECRET = "3600b2c8aee4cb5db7ace53653fc93c7";
    
    private IWXAPI mWXapi;
    private WXInfo mWxInfo;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Log.i(TAG, "onCreate");
        
        mWXapi = WXAPIFactory.createWXAPI(this, APP_ID, true);
        mWXapi.handleIntent(getIntent(), this);
        mWXapi.registerApp(APP_ID);
        
        mWxInfo = WXInfo.getInstance();
        
        Button sign_in = (Button) findViewById(R.id.wx_sign_in);
        sign_in.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                final SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wsk_sign_in";
                mWXapi.sendReq(req);
            }
        });
        
        Button entry_game = (Button) findViewById(R.id.entry_game);
        entry_game.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                entryGame();
            }
        });
    }
    
    @Override
    public void onReq(BaseReq req) {
        Log.i(TAG, "public void onReq(BaseReq req) !!!!!!!");
        switch (req.getType()) {
        case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
            Log.i(TAG, "onReq:COMMAND_GETMESSAGE_FROM_WX");
            break;
        case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
            Log.i(TAG, "onReq:COMMAND_SHOWMESSAGE_FROM_WX");
            break;
        default:
            break;
        }

        Log.i(TAG, "onReq:" + req.getType());
    }

    @Override
    public void onResp(BaseResp resp) {
        
        Log.i(TAG, "onResp: =" + resp.getType());
        
        switch (resp.errCode) {
        case BaseResp.ErrCode.ERR_OK:
            requestTokenWithHttpClient(((SendAuth.Resp) resp).code);
            break;
        case BaseResp.ErrCode.ERR_USER_CANCEL:
            break;
        case BaseResp.ErrCode.ERR_AUTH_DENIED:
            break;
        case BaseResp.ErrCode.ERR_UNSUPPORT:
            break;
        default:
            break;
        }
    }

    private void entryGame() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    
    private void requestTokenWithHttpClient(final String code) {
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                String uri = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", APP_ID, APP_SECRET, code);
                HttpGet httpGet = new HttpGet(uri);
                try {
                    HttpResponse response = httpClient.execute(httpGet);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = response.getEntity();
                        String entityStr = EntityUtils.toString(entity);
                        JSONObject object = new JSONObject(entityStr);
                        String access_token = object.getString("access_token");
                        String refresh_token = object.getString("refresh_token");
                        String openid = object.getString("openid");
                        
                        Log.i(TAG, "requestTokenWithHttpClient - mAccessToken:" + access_token + "; mRefreshToken:" + refresh_token + "; mOpenId:" + openid);
                        
                        if (!TextUtils.isEmpty(access_token) && !TextUtils.isEmpty(openid)) {
                            uri = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s", access_token, openid);
                            HttpGet httpGet_2 = new HttpGet(uri);
                            HttpResponse response_2 = httpClient.execute(httpGet_2);
                            if (response_2.getStatusLine().getStatusCode() == 200) {
                                HttpEntity entity_2 = response_2.getEntity();
                                String entityStr_2 = EntityUtils.toString(entity_2, HTTP.UTF_8);
                                Log.i(TAG, "requestTokenWithHttpClient - entityStr_2:" + entityStr_2);
                                JSONObject object_2 = new JSONObject(entityStr_2);
                                mWxInfo.setNickName(object_2.getString("nickname"));
                                mWxInfo.setSex(object_2.getInt("sex"));
                                mWxInfo.setProvince(object_2.getString("province"));
                                mWxInfo.setCity(object_2.getString("city"));
                                mWxInfo.setCountry(object_2.getString("country"));
                                mWxInfo.setPrivilege(object_2.getString("privilege"));
                                mWxInfo.setUnionid(object_2.getString("unionid"));
                                String headimgurl = object_2.getString("headimgurl");
                                mWxInfo.setHeadimgurl(headimgurl);
                                
                                if (headimgurl.contains("/0")) {
                                    headimgurl = headimgurl.replace("/0", "/132");
                                }
                                
                                URL url = new URL(headimgurl);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setConnectTimeout(5000);
                                conn.setRequestMethod("GET");
                                if (conn.getResponseCode() == 200) {
                                    InputStream inputStream = conn.getInputStream();
                                    mWxInfo.setHead(inputStream);
                                }
                                
                                entryGame();
                            }
                        }

                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        }).start();
    }
    
    // 文本分享
    private void shareText() {
        // 初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = "微信文本分享测试";
        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj; // 发送文本类型的消息时，title字段不起作用
        msg.description = "微信文本分享测试"; 
        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
        req.message = msg; // 分享或收藏的目标场景，通过修改scene场景值实现。
        // 发送到聊天界面 —— WXSceneSession
        // 发送到朋友圈 —— WXSceneTimeline
        // 添加到微信收藏 —— WXSceneFavorite
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        // 调用api接口发送数据到微信
        mWXapi.sendReq(req);
    }
    
    // 网页分享
    private void shareWebpage() {
        // 初始化一个WXTextObject对象
        WXWebpageObject webpageObj = new WXWebpageObject();
        webpageObj.webpageUrl = "网页url";
        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.title = "网页标题";
        msg.description = "网页描述"; 
        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage"); // transaction字段用于唯一标识一个请求
        req.message = msg; // 分享或收藏的目标场景，通过修改scene场景值实现。
        // 发送到聊天界面 —— WXSceneSession
        // 发送到朋友圈 —— WXSceneTimeline
        // 添加到微信收藏 —— WXSceneFavorite
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        // 调用api接口发送数据到微信
        mWXapi.sendReq(req);
    }
    
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
