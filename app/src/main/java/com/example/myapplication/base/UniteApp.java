package com.example.myapplication.base;

import android.app.Application;
import com.example.myapplication.db.DBManager;
import org.xutils.x;

public class UniteApp extends Application {
    // 因为这是自定义的application所以需在清单文件AndroidManifest中进行声明
    // 程序进入这个程序中时就会加载UniteApp的代码，然后执行OnCreate方法，再执行x.Ext框架声明

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);   // 在UniteApp中将xutils先进行初始化
        x.Ext.init(this);//它表示全局声明，在此处声明后在其它地方就不用再声明了
        DBManager.initDB(this);  //当项目工程一旦被创建，数据库也会被初始化
    }
}
