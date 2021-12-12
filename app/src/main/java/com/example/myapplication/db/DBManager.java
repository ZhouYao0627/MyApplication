package com.example.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    public static SQLiteDatabase database;  //初始化数据库的信息内容 // database就成为了一个全局的对象
    /* 初始化数据库信息*/
    //去UnitApp中初始化数据库，所谓的数据库被初始化也就是在DBManager中执行initDB方法能够得到database对象，下面便可以使用database来进行操作
    public static void initDB(Context context){
        //创建数据库辅助类对象，传入context // 只执行这句话是不会创建或打开连接的
        DBHelper dbHelper = new DBHelper(context);
        // 获取数据库对象 调用getReadableDatabase()或getWritableDatabase()才算真正创建或打开数据库
        database = dbHelper.getWritableDatabase();
    }

    /* 查找数据库当中城市列表*/
    public static List<String>queryAllCityName(){  //获取全部的城市名称
        // 所有方法将返回一个Cursor对象，代表数据集的游标，即返回一个Cursor对象：由数据库查询返回的结果集对象
        Cursor cursor = database.query("info", null, null, null, null, null,null);
        //List为父类接口，提供标准的method。ArrayList是其子类，即对父类接口List的不同实现。
        List<String>cityList = new ArrayList<>();
        while (cursor.moveToNext()) {
            //现在使用游标来看一下info对应的数据库这张表中的信息，如果包含一个就向集合cityList中添加一条记录
            // 遍历游标，看它是否有下一个，即将光标移动到下一行，从而判断该结果集是否还有下一条数据
            // 根据 name的名称获得它的索引  cursor.getColumnIndex(String columnName);//返回某列名对应的列索引值
            String city = cursor.getString(cursor.getColumnIndex("city")); //获取
            cityList.add(city); //添加到城市当中    --->MainActivity
        }
        return cityList;
    }
    /* 根据城市名称，替换信息内容*/
    public static int updateInfoByCity(String city,String content){  // 替换的内容content也就是获取到的JSON数据
        ContentValues values = new ContentValues(); //如果说用命令行操作数据库太麻烦了，android提供了一个 ContentValues可以操作数据库
        values.put("content",content); // 改变对应的content，改变为新增加的内容
        return database.update("info",values,"city=?",new String[]{city});
        // 调用update方法修改数据库
        // 第一个参数String：表名
        // 第二个参数ContentValues：ContentValues对象（需要修改的）
        // 第三个参数String：WHERE表达式，where选择语句, 选择那些行进行数据的更新, 如果该参数为 null, 就会修改所有行;？号是占位符
        // 第四个参数String[]：where选择语句的参数, 逐个替换 whereClause 中的占位符;
    }
    /* 新增一条城市记录*/
    public static long addCityInfo(String city,String content){
        ContentValues values = new ContentValues();
        values.put("city",city);
        values.put("content",content);
        return database.insert("info",null,values);
        // 调用insert()方法将数据插入到数据库当中
        // 第一个参数：要操作的表名称
        // 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值
        // 第三个参数：ContentValues对象
    }
    /* 根据城市名，查询数据库当中的内容*/
    public static String queryInfoByCity(String city){
        Cursor cursor = database.query("info", null, "city=?", new String[]{city}, null, null, null);
        if (cursor.getCount()>0) { //说明是有数据的
            cursor.moveToFirst();//因为城市是唯一存储的，所以将其移动到第一个
            String content = cursor.getString(cursor.getColumnIndex("content")); //获取的是它的content内容
            return content;
        }
        return null; //否则返回一个null
    }

    /* 存储城市天气要求最多存储6个城市的信息，一旦超过6个城市就不能存储了，获取目前已经存储的数量*/
    // 这是CityManagerActivity跳转SearchActivity的需要判断的城市数量的方法
    public static int getCityCount(){  //查询整张表
        Cursor cursor = database.query("info", null, null, null, null, null, null);
        int count = cursor.getCount(); //这是获取能查到多少个的，得到其数量
        //  getCount() 返回Cursor 中的行数
        return count;
    }

    /* 查询数据库当中的全部信息*/
    public static List<DatabaseBean>queryAllInfo(){ //它要的是这个集合，而集合的范型是DatabaseBean
        Cursor cursor = database.query("info", null, null, null, null, null, null);
        List<DatabaseBean>list = new ArrayList<>(); //先创建这个集合
        while (cursor.moveToNext()) { //数据都在cursor当中了，我们先遍历一下cursor  判断是否有下一个
            //getColumnIndex(String columnName) 返回指定列的名称，如果不存在返回-1
            int id = cursor.getInt(cursor.getColumnIndex("_id")); //ID
            String city = cursor.getString(cursor.getColumnIndex("city"));  //城市名
            String content = cursor.getString(cursor.getColumnIndex("content")); //详情信息
            DatabaseBean bean = new DatabaseBean(id, city, content); //一条就相当于一个对象，我们可以创建一个对象
            list.add(bean); //将对象添加到集合当中，有一个对象我们就向集合中添加一条，有一行我们就有一个对象
        }
        return list; //返回这个集合
    }

    /* 根据城市名称，删除这个城市在数据库当中的数据*/
    public static int deleteInfoByCity(String city){
        return database.delete("info","city=?",new String[]{city});
        //第一个参数String：需要操作的表名
        //第二个参数String：where选择语句, 选择哪些行要被删除, 如果为null, 就删除所有行;
        //第三个参数String[]： where语句的参数, 逐个替换where语句中的 "?" 占位符;
    }

    /* 删除表当中所有的数据信息*/
    public static void deleteAllInfo(){
        String sql = "delete from info"; //只是删除表中的内容，表本身未被删除
        database.execSQL(sql);
    }
}
