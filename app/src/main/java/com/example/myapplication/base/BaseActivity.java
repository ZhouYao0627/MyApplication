package com.example.myapplication.base;

//联网的操作都封装到这里
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import androidx.appcompat.app.AppCompatActivity;

// 将Activity中联网的操作都封装到BaseActivity中
//继承activity的公共类AppCompatActivity再实现接口再重写方法
public class BaseActivity extends AppCompatActivity implements Callback.CommonCallback<String>{

    //将联网的方法进行封装
    public void loadData(String url){   //传入我们需要获取的url地址 // 这里需要将xutils进行初始化->UniteApp
        RequestParams params = new RequestParams(url); //封装请求参数  获取到params
        x.http().get(params,this); //进行联网请求
    }

    @Override
    public void onSuccess(String result) { }
    @Override
    public void onError(Throwable ex, boolean isOnCallback) { }
    @Override
    public void onCancelled(CancelledException cex) { }
    @Override
    public void onFinished() { }
}
