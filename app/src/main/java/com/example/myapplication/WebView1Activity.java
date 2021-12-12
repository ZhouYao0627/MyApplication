package com.example.myapplication;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class WebView1Activity extends AppCompatActivity {

    private WebView mWvMain1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view1);

        mWvMain1 = findViewById(R.id.wv1);
        mWvMain1.getSettings().setJavaScriptEnabled(true);   //  加载网络URL
        mWvMain1.setWebChromeClient(new MyWebChromeClient());//  设置标题，可有可无
        mWvMain1.loadUrl("https://tianqi.moji.com/liveview/china/jiangsu/pukou-district");//https://weather.cma.cn/web/weather/map.html    https://m.baidu.com

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
