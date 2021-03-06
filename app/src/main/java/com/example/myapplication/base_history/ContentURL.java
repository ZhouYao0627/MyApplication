package com.example.myapplication.base_history;

public class ContentURL {

    //    获取指定日期对应的历史上的今天的网址
    public static String getTodayHistoryURL(String version,int month,int day){ // 将变化的数据通过拼接的方式来拼接过来，然后让它传值传递过来
        String url = "http://api.juheapi.com/japi/toh?v="+version+"&month="+month+"&day="+day+"&key=6a877b186ccd134296d131183b74c9f4";
        return url;
    }

    //    获取指定日期的老黄历的网址
    public static String getLaohuangliURL(String time){
        String url = "http://v.juhe.cn/laohuangli/d?date="+time+"&key=c7c6d7da1062f007a33609571cdb17f2";
        return url;
    }

//    http://api.juheapi.com/japi/tohdet?key=6a877b186ccd134296d131183b74c9f4&v=1.0&id=18401114

    //    根据指定事件id获取指定事件详情信息
    public static String getHistoryDescURL(String version,String id){
        String url = "http://api.juheapi.com/japi/tohdet?key=6a877b186ccd134296d131183b74c9f4&v="+version+"&id="+id;
        return url;
    }
}

