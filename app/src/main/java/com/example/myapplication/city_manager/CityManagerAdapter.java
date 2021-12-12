package com.example.myapplication.city_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.bean.WeatherBean;
import com.example.myapplication.db.DatabaseBean;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
//它的父类是BaseAdapter，这是listview适配器公共的父类，再去重写四个方法
public class CityManagerAdapter extends BaseAdapter{
    Context context;
    List<DatabaseBean>mDatas; //数据源

    //通过构造函数来进行传递
    public CityManagerAdapter(Context context, List<DatabaseBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    // 通过构造函数来进行传递
    @Override
    public int getCount() {
        return mDatas.size();
    }  //返回数据的长度

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    } //返回指定位置对应的元素

    @Override
    public long getItemId(int position) {
        return position;
    }   //返回它对应的位置

    @Override //对每一个Item的内容进行初始化，并进行itemview的设置
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;  // 声明ViewHolder
        if (convertView == null) {  //判断有没有可以复用的view   若 == null则没有可以复用的view
            convertView = LayoutInflater.from(context).inflate(R.layout.item_city_manager,null); //创建convertView对象
            holder = new ViewHolder(convertView);  //创建holder对象，传入控件所在的那个view也就是convertView
            convertView.setTag(holder);//传完之后，在构造方法之中我们就进行了声明，我们可以来设置一下标记
        }else{  //即不为空，也就是有复用的
            holder = (ViewHolder) convertView.getTag();
        }
        // 不过convertView是否为空，holder都设置成功了。接下来可以得到这些数据，设置到对应的TextView当中
        DatabaseBean bean = mDatas.get(position); //获取指定位置的数据，也就是我们的bean对象
        holder.cityTv.setText(bean.getCity()); // 这个内容是城市
                                                        //解析下方         //解析到这个类
        WeatherBean weatherBean = new Gson().fromJson(bean.getContent(), WeatherBean.class);  //将已经存储的字符串进行了解析，得到了weatherBean的对象
//        获取今日天气情况
        WeatherBean.DataBean.ObserveBean dataBean = weatherBean.getData().getObserve();
        holder.conTv.setText("天气 : "+dataBean.getWeather_short());
        holder.currentTempTv.setText(dataBean.getDegree()+"°C");
        holder.windTv.setText("湿度 ："+dataBean.getHumidity()+"% ");
        try {
            holder.tempRangeTv.setText(changeTime(dataBean.getUpdate_time()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    //    时间格式化
    private String changeTime(String update_time) throws ParseException {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat sf2 =new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String sfstr = "";
        sfstr = sf2.format(sf1.parse(update_time));
        return sfstr;
    }

    class ViewHolder{ //这是关于listview优化的一部分  名称在item_city_manager中
        TextView cityTv,conTv,currentTempTv,windTv,tempRangeTv;
        public ViewHolder(View itemView){ //构造方法
            cityTv = itemView.findViewById(R.id.item_city_tv_city);
            conTv = itemView.findViewById(R.id.item_city_tv_condition);
            currentTempTv = itemView.findViewById(R.id.item_city_tv_temp);
            windTv = itemView.findViewById(R.id.item_city_wind);
            tempRangeTv = itemView.findViewById(R.id.item_city_temprange);

        }
    }
}
