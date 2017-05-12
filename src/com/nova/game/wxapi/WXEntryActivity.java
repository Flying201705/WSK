package com.nova.game.wxapi;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.nova.game.MainActivity;
import com.nova.game.R;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private final static String APP_ID = "wx1234567890";
    private final static String APP_SECRET = "????";
    
    private IWXAPI mWXapi;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Log.i("liuhao", "WXEntryActivity onCreate");
        
        mWXapi = WXAPIFactory.createWXAPI(this, APP_ID);
        
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
        
    }

    @Override
    public void onResp(BaseResp resp) {
        
        Toast.makeText(this, "baseresp.getType = " + resp.getType(), Toast.LENGTH_LONG).show();
        
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
                        if (entityStr != null) {
                            entryGame();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
