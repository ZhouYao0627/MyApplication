package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.example.myapplication.base.BaseFragment;
import com.example.myapplication.bean.WeatherBean;
import com.example.myapplication.db.DBManager;
import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
// 这是一个fragment
// 将原本继承的Fragment改为自己的父类BaseFragment,
// 这样可以方便重写BaseFragment里的方法(BaseFragment也是实现Callback.CommonCallback<String>)
public class CityWeatherFragment extends BaseFragment implements View.OnClickListener{
    TextView tempTv,cityTv,conditionTv,windTv,tempRangeTv,dateTv,clothIndexTv,carIndexTv,coldIndexTv,sportIndexTv,raysIndexTv,tipTv,umbrellaTv;
    ImageView dayIv; // 今天天气的图标 --->无用
    LinearLayout futureLayout; // 放置未来三天的天气情况的LinearLayout
    ScrollView outLayout;  // fragment_city_weather的整体ScrollView

    //对网址进行切割；地址我们可以从MainActivity中进行传递，就通过MainActivity向它所包含的fragment传递地址，我们把除了地址之外的复制过来
    String url1 = "https://wis.qq.com/weather/common?source=pc&weather_type=observe|index|rise|alarm|air|tips|forecast_24h&province=";
    String url2 = "&city=";
    String city;
    String provice;
    private WeatherBean.DataBean.IndexBean index;
    private SharedPreferences pref;
    private int bgNum;

//      换壁纸的函数
    public void exchangeBg(){
        pref = getActivity().getSharedPreferences("bg_pref", MODE_PRIVATE);
        bgNum = pref.getInt("bg", 2);
        switch (bgNum) {
            case 0:
                outLayout.setBackgroundResource(R.mipmap.bg);
                break;
            case 1:
                outLayout.setBackgroundResource(R.mipmap.bg7);
                break;
            case 2:
                outLayout.setBackgroundResource(R.mipmap.bg6);
                break;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
        initView(view);// 在onCreateView中定义的方法，将view传入进去；然后在方法中来查找内容，因为定义的控件比较多，所以提到方法中来进行编写
        exchangeBg();
//      可以通过activity传值获取到当前fragment加载的是那个地方的省份和天气情况
        // 当Activity类动态加载fragment时可以通过fragment的setArguments()传入值，
        // 并在fragment类中通过fragment的getArguments()方法获得传入的值；
        Bundle bundle = getArguments(); // 这是获取MainActivity信息的方法
        String provice_city = bundle.getString("city"); // 接收，得到城市信息
//        获取省份
        if(provice_city.split(" ").length>1)
        {   provice =provice_city.split(" ")[0];
            city = provice_city.split(" ")[1];
        }
        else
        {
            city = provice_city.split(" ")[0];
            provice = provice_city.split(" ")[0];
        }
        String url = url1+provice+url2+city;  //把网址来进行拼接；网址有了，接下来就能加载数据了，
        // 为了让这个代码比较少，故将加载数据的过程整体的提出来，提到BaseFrgment中，让它在父类当中来加载数据

        // 调用父类获取数据的方法
        loadData(url); //在其获得网址之后调用父类获取数据的方法；那么成功的时候会走父类的OnSuccess方法，
                       // 我们想在它成功的时候在我们CItyWeatherFragment中就需要重写方法，用到了多态以及继承重写
        return view;

    }

//Android Studio里给我们写好了一个工具，settings里面的Plugins写上GsonFormat安装一下
// 我们新建一个包用来放置已经生成好的bean类
    @Override // 成功了就进行解析数据的操作
    public void onSuccess(String result) { //数据加载成功时会调用的方法
//        解析并展示数据 ---> WeatherBean
        try {
            parseShowData(result); //在这里也封装一个方法，将获取成功的信息result也传入进来
        } catch (ParseException e) {
            e.printStackTrace();
        }
//         插入，更新数据信息记录    即获取数据成功了，就将获取到的数据信息插入到数据库当中  ->方法写于DBManger中
        int i = DBManager.updateInfoByCity(city, result); // 若已存在就更新
        if (i<=0) {  // 如果0条数据被改变了
//            更新数据库失败，说明没有这条城市信息，那么增加这个城市记录
            DBManager.addCityInfo(city,result);
        }
    }
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {  //数据获取失败时，去数据库中查找上一次获取到的城市信息
//        数据库当中查找上一次信息显示在Fragment当中
        String s = DBManager.queryInfoByCity(city);
        if (!TextUtils.isEmpty(s)) { //若它不为空，则解析
            try {
                parseShowData(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }
    // 信息获取成功的方法
    private void parseShowData(String result) throws ParseException {
//      使用gson解析数据
        // 选择要解析的数据字符串result，要解析成WeatherBean这个类的对象   就会将整体字符串放到这个对象当中
        // 即提供两个参数，分别是json字符串以及需要转换对象的类型
        // Gson提供了fromJson()方法来实现从Json相关对象到java实体的方法。
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class); // 使用 Gson 将数据解析成一个对象
        // 下方 想要的数据都在它的集合当中（集合当中只有一个对象，所以.get(0)，但我未使用）
        WeatherBean.DataBean resultsBean = weatherBean.getData();
//      获取指数信息的列表存放到一个集合里
        index = resultsBean.getIndex();
//        设置TextView
        cityTv.setText(city);  // 即江苏省 泗阳县
        tipTv.setText(resultsBean.getTips().getObserve().get_$0()); // 即铁肩担道义，妙手著文章
//        获取今日天气情况
        WeatherBean.DataBean.ObserveBean todayDataBean = resultsBean.getObserve();
        String time = changeTime(todayDataBean.getUpdate_time());
        dateTv.setText("发布时间  "+time);
        windTv.setText("湿度 "+todayDataBean.getHumidity()+"%");
        tempRangeTv.setText("气压  "+todayDataBean.getPressure()+"hPa");
        conditionTv.setText(todayDataBean.getWeather_short());
//        获取实时天气温度情况，需要处理字符串(因为提供的网址中的实时天气是比较复杂的，所以需要处理一下)
        tempTv.setText(todayDataBean.getDegree()+"°C");
        //设置显示的天气情况图片
//        Picasso.with(getActivity()).load(todayDataBean.getDayPictureUrl()).into(dayIv);
//        获取未来三天的天气情况，加载到layout当中  --->可以用循环写，但有些困难，未使用
        // 明天
        WeatherBean.DataBean.Forecast24hBean futureList = resultsBean.getForecast_24h(); //获取未来三天情况
        //将中间表示未来三天的布局转换成view对象
        View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_main_center, null);
        //设置宽高
        itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //对其内部的内容来进行设置；先将它先放到布局里，到布局当中来添加itemView，把itemView添加进去，有几个就添加几次
        futureLayout.addView(itemView);
        TextView idateTv = itemView.findViewById(R.id.item_center_tv_date);  //时间
        TextView iconTv = itemView.findViewById(R.id.item_center_tv_con);  //天气情况
        TextView itemprangeTv = itemView.findViewById(R.id.item_center_tv_temp); //温度范围
        TextView wind = itemView.findViewById(R.id.item_center_tv_winddirection); //风向
//       获取对应的位置的天气情况
        idateTv.setText(futureList.get_$2().getTime()+"   明天");
        iconTv.setText(futureList.get_$2().getDay_weather());
        wind.setText(futureList.get_$2().getDay_wind_direction());
        itemprangeTv.setText(futureList.get_$2().getMin_degree()+"~"+futureList.get_$2().getMax_degree()+"°C");

        // 后天
        View itemView3 = LayoutInflater.from(getActivity()).inflate(R.layout.item_main_center, null);
        itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        futureLayout.addView(itemView3);
        TextView idateTv3 = itemView3.findViewById(R.id.item_center_tv_date);
        TextView iconTv3 = itemView3.findViewById(R.id.item_center_tv_con);
        TextView itemprangeTv3 = itemView3.findViewById(R.id.item_center_tv_temp);
        TextView wind3 = itemView3.findViewById(R.id.item_center_tv_winddirection);
//          获取对应的位置的天气情况
        idateTv3.setText(futureList.get_$3().getTime()+"   后天");
        iconTv3.setText(futureList.get_$3().getDay_weather());
        wind3.setText(futureList.get_$3().getDay_wind_direction());
        itemprangeTv3.setText(futureList.get_$3().getMin_degree()+"~"+futureList.get_$3().getMax_degree()+"°C");

        // 大后天
        View itemView2 = LayoutInflater.from(getActivity()).inflate(R.layout.item_main_center, null);
        itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        futureLayout.addView(itemView2);
        TextView idateTv2 = itemView2.findViewById(R.id.item_center_tv_date);
        TextView iconTv2 = itemView2.findViewById(R.id.item_center_tv_con);
        TextView itemprangeTv2 = itemView2.findViewById(R.id.item_center_tv_temp);
        TextView wind2 = itemView2.findViewById(R.id.item_center_tv_winddirection);
//          获取对应的位置的天气情况
        idateTv2.setText(futureList.get_$4().getTime()+" 大后天");
        iconTv2.setText(futureList.get_$4().getDay_weather());
        wind2.setText(futureList.get_$4().getDay_wind_direction());
        itemprangeTv2.setText(futureList.get_$4().getMin_degree()+"~"+futureList.get_$4().getMax_degree()+"°C");

    }

//    时间格式化
    private String changeTime(String update_time) throws ParseException {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat sf2 =new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String sfstr = "";
            sfstr = sf2.format(sf1.parse(update_time));
            return sfstr;
    }

    private void initView(View view) {
//        用于初始化控件操作
        tipTv  = view.findViewById(R.id.frag_tv_tips);
        tempTv = view.findViewById(R.id.frag_tv_currenttemp);
        cityTv = view.findViewById(R.id.frag_tv_city);
        conditionTv = view.findViewById(R.id.frag_tv_condition);
        windTv = view.findViewById(R.id.frag_tv_wind);
        tempRangeTv = view.findViewById(R.id.frag_tv_temprange);
        dateTv = view.findViewById(R.id.frag_tv_date);
        clothIndexTv = view.findViewById(R.id.frag_index_tv_dress);
        carIndexTv = view.findViewById(R.id.frag_index_tv_washcar);
        coldIndexTv = view.findViewById(R.id.frag_index_tv_cold);
        sportIndexTv = view.findViewById(R.id.frag_index_tv_sport);
        raysIndexTv = view.findViewById(R.id.frag_index_tv_rays);
        dayIv = view.findViewById(R.id.frag_iv_today); // 天气图片 --->无用
        futureLayout = view.findViewById(R.id.frag_center_layout);
        outLayout = view.findViewById(R.id.out_layout);//关于壁纸的
        umbrellaTv = view.findViewById(R.id.frag_index_tv_umbrella);

//        设置点击事件的监听
        clothIndexTv.setOnClickListener(this);
        carIndexTv.setOnClickListener(this);
        coldIndexTv.setOnClickListener(this);
        sportIndexTv.setOnClickListener(this);
        raysIndexTv.setOnClickListener(this);
        umbrellaTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // 生成对话框
        switch (v.getId()) {
            case R.id.frag_index_tv_dress:
                builder.setTitle("衣物建议");
                WeatherBean.DataBean.IndexBean.ClothesBean cloth = index.getClothes(); //获取指数信息
                String msg = cloth.getInfo()+"\n"+cloth.getDetail(); //将指数信息放到字符串当中进行显示
//           * info : 炎热、* name : 穿衣、* detail : 天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。
                builder.setMessage(msg);
                builder.setPositiveButton("好的",null);
//                builder.setNeutralButton("保持怀疑",null);
//                builder.setNegativeButton("我不信",null);
                break;
            case R.id.frag_index_tv_washcar:
                builder.setTitle("出行建议");
                WeatherBean.DataBean.IndexBean.CarwashBean car = index.getCarwash();
                msg = car.getInfo()+"\n"+car.getDetail();
                builder.setMessage(msg);
                builder.setPositiveButton("好的",null);
                break;
            case R.id.frag_index_tv_cold:
                builder.setTitle("医院建议");
                WeatherBean.DataBean.IndexBean.ColdBean cold = index.getCold();
                msg = cold.getInfo()+"\n"+cold.getDetail();
                builder.setMessage(msg);
                builder.setPositiveButton("好的",null);
                break;
            case R.id.frag_index_tv_sport:
                builder.setTitle("运动建议");
                WeatherBean.DataBean.IndexBean.SportsBean sport = index.getSports();
                msg = sport.getInfo()+"\n"+sport.getDetail();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_rays:
                builder.setTitle("温度建议");
                WeatherBean.DataBean.IndexBean.UltravioletBean ultraviolet = index.getUltraviolet();
                msg = ultraviolet.getInfo()+"\n"+ultraviolet.getDetail();
                builder.setMessage(msg);
                builder.setPositiveButton("好的",null);
                break;
            case R.id.frag_index_tv_umbrella:
                builder.setTitle("气候建议");
                WeatherBean.DataBean.IndexBean.UmbrellaBean umbrella = index.getUmbrella();
                msg = umbrella.getInfo()+"\n"+umbrella.getDetail();
                builder.setMessage(msg);
                builder.setPositiveButton("好的",null);
                break;
        }
        builder.create().show(); // 创建和显示
    }
}
