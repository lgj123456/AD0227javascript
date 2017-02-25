package com.example.yhdj.ad0227js;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btn_js1;
    private Button btn_js2;
    private WebView mWebView;
    private final int REQUEST_CODE_CALL_PERMISSION = 0;
    private final int REQUEST_CODE_SEND_MSG = 1;
    private boolean isCanCall = false;
    private boolean isCanSendMsg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        btn_js1 = (Button) findViewById(R.id.btn_js1);
        btn_js2 = (Button) findViewById(R.id.btn_js2);
        mWebView = (WebView) findViewById(R.id.webView);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/web.html");
        mWebView.addJavascriptInterface(MainActivity.this, "android");

        btn_js1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:javacalljs()");

            }
        });



        btn_js2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:javacalljswith(" + "'http://blog.csdn.net/Leejizhou'" + ")");
            }
        });

    }

    //检查是否有打电话的权限
    private boolean ishasCallPermission() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
    }

    private boolean ishasSendMsgPermission() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
    }

    private void applyCallPermission(){
        String permissions[] = {Manifest.permission.CALL_PHONE,Manifest.permission.SEND_SMS};
        ActivityCompat.requestPermissions(this,permissions,REQUEST_CODE_CALL_PERMISSION);
    }

    @JavascriptInterface
    public void startFunction(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "js调用JAVA代码", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @JavascriptInterface
    public void startFunction(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(MainActivity.this).setMessage(text).show();
            }
        });
    }

    @JavascriptInterface
    public void call(String num){
        if(!ishasCallPermission()){
            applyCallPermission();
        }
        if(num.isEmpty()){
            Toast.makeText(this, "电话号码不能为空！！！", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (!isCanCall){
//            return;
//        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + num));
        startActivity(intent);
    }

    @JavascriptInterface
    public void sendMsg(String num, String msg){
        if(!ishasSendMsgPermission()){
            applyCallPermission();
        }
        if(msg.isEmpty() || num.isEmpty()){
            Toast.makeText(this, "电话号码和短信内容不能为空！！！", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (!isCanSendMsg){
//            return;
//        }
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> list = smsManager.divideMessage(msg);
        for(String text : list){
            smsManager.sendTextMessage(num,null,text,null,null);
        }
        Log.d("msg", "sendMsg:消息发送完成！！！ ");
        Toast.makeText(this, "消息发送完成！！！", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       switch (requestCode){
           case REQUEST_CODE_CALL_PERMISSION:
               if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   Toast.makeText(this, "申请打电话权限成功！！！", Toast.LENGTH_SHORT).show();
                   isCanCall = true;

               }else{
                   Toast.makeText(this, "申请打电话权限失败！！！", Toast.LENGTH_SHORT).show();
               }
               break;
           case REQUEST_CODE_SEND_MSG :
               if (grantResults[1] == PackageManager.PERMISSION_GRANTED){
                   Toast.makeText(this, "申请发短信权限成功！！！", Toast.LENGTH_SHORT).show();
                   isCanSendMsg = true;
               }else{
                   Toast.makeText(this, "申请发短信权限失败！！！", Toast.LENGTH_SHORT).show();

               }
               break;
       }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
