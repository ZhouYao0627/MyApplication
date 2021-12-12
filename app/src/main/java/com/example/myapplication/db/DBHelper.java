package com.example.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    // 构造方法
    public DBHelper(Context context){ // 传入context，上下文对象
        //参数说明  第一个是context，第二个是数据库的名称，第三个是工厂，第四个是版本号
        //context:上下文对象
        //name:数据库名称
        //param:factory
        //version:当前数据库的版本，值必须是整数并且是递增的状态
        super(context,"forecast.db",null,1); //需要调用父类的构造方法
    }

    // 重写两个方法
    @Override
    //数据库第一次被创建时会执行的方法
    public void onCreate(SQLiteDatabase db) {
//  创建表的操作   表的名称叫info；主键为_id并且自增长；城市名称city，城市最大的长度为20，唯一，不能为空；城市获取的天气情况，不能为空
        String sql = "create table info(_id integer primary key autoincrement,city varchar(20) unique not null,content text not null)";
        //execSQL用于执行SQL语句
        //完成数据库的创建
        db.execSQL(sql);
        //可进行增删改操作, 不能进行查询操作  写一个数据库创建的辅助类(对数据库的管理)--->DBManger
        //数据库实际上是没有被创建或者打开的，直到getWritableDatabase() 或者 getReadableDatabase() 方法中的一个被调用时才会进行创建或者打开
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//数据库版本更新时会执行的方法
    }
}
