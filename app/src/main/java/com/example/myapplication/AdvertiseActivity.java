package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.guide.GuideActivity;

public class AdvertiseActivity extends AppCompatActivity {
    private Button mBtnadvertise;
    int time = 5; // 设定默认跳转时间为5s
    SharedPreferences preferences;// 存储键值对数据 ->我们需要将是否是第一次进入的状态给保存起来，若是则...，若不是则...
    private SharedPreferences.Editor editor;
    Handler handler = new Handler(){  // 写上重写handler的方法
        @Override
        public void handleMessage(Message msg) {  // 这是接收信息的方法
            if (msg.what == 1) { // 若编号为1
                time--;
                if (time == 0) {
                //跳转页面
                   Intent intent =  new Intent();
                    boolean isfirst = preferences.getBoolean("isfirst", true);
                    if(isfirst){ // 即以前没有存储过，第一次进入
                        intent.setClass(AdvertiseActivity.this,GuideActivity.class);
                        editor.putBoolean("isfirst",false);  //写入不是第一次进入的纪录 使得下一次进入不是首次进入
                        editor.commit();    // 提交本次修改纪录
                    }else{ // 不是第一次
                        intent.setClass(AdvertiseActivity.this,MainActivity.class);
                    }
                   startActivity(intent);
                   finish();
                }else{ // 不为0就要进行设置的工作
                    mBtnadvertise.setText(time+"");//这里是int型的，直接设置time会报错
                    handler.sendEmptyMessageDelayed(1,1000); // 再次发送
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);

        mBtnadvertise = findViewById(R.id.btn_advertise);

        preferences = getSharedPreferences("health_pref",MODE_PRIVATE);// 获取数据
        editor = preferences.edit();//写入数据的操作

        handler.sendEmptyMessageDelayed(1,1000); // 发送编号为1 ，发送信息在1秒钟之后
    }
}
