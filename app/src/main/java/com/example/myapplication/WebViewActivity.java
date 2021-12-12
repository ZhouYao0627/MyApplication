package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {

    private WebView mWvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mWvMain = findViewById(R.id.wv);
        mWvMain.getSettings().setJavaScriptEnabled(true);   //  加载网络URL
        mWvMain.setWebChromeClient(new MyWebChromeClient());//  设置标题，可有可无
        mWvMain.loadUrl("https://weather.cma.cn/web/weather/map.html");//https://weather.cma.cn/web/weather/map.html    https://m.baidu.com

    }

    //设置标题，可有可无
    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }
    }

}
