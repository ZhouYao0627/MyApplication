package com.example.myapplication.db;
// 这里面包含的就是存储进来的记录
public class DatabaseBean {
    private int _id;   //代表数据库的主键
    private String city;  //表示哪些城市被存储了
    private String content; //相关的天气信息内容 --->在WeatherBean中解析了JSON

    // 构造函数的生成 无参
    public DatabaseBean() { }

    // 构造函数的生成 全参
    public DatabaseBean(int _id, String city, String content) {
        this._id = _id;
        this.city = city;
        this.content = content;
    }

    // 使用getter setter方法生成
    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
