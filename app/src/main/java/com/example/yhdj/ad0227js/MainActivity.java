package com.example.yhdj.ad0227js;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn_js1;
    private Button btn_js2;
    private WebView mWebView;
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


    }
}
