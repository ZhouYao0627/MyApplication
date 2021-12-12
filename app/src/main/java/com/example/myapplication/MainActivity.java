package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.myapplication.city_manager.CityManagerActivity;
import com.example.myapplication.db.DBManager;

import java.util.ArrayList;
import java.util.List;

// MainActivity中主要是对于ViewPaper和fragment的加载
public class MainActivity extends AppCompatActivity implements View.OnClickListener{  //实现OnClickListener的接口，再重写方法
    ImageView addCityIv,moreIv,webviewIv,webview2Iv;// 添加更多城市、跳转到更多界面、跳转到webview界面
    LinearLayout pointLayout; //这是添加指示器标志，即矩形
    RelativeLayout outLayout; //这是activity_main里面最大的RelativeLayout的ID
    ViewPager mainVp;

    //    ViewPager的数据源
    List<Fragment>fragmentList;  // 也就是CityFragmentPagerAdapter里写好的
    //    表示需要显示的城市的集合
    List<String>cityList;        // List集合，范型就是String
    //    表示ViewPager的页数指数器显示集合
    List<ImageView>imgList;      //集合的范型是ImageView

    private CityFragmentPagerAdapter adapter;
    private SharedPreferences pref;
    private int bgNum;
//    private Button mBtnWebView,mBtnWebView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webviewIv = findViewById(R.id.main_iv_webview);
        webview2Iv = findViewById(R.id.main_iv_webview2);
        addCityIv = findViewById(R.id.main_iv_add);
        moreIv = findViewById(R.id.main_iv_more);
        pointLayout = findViewById(R.id.main_layout_point);
        outLayout = findViewById(R.id.main_out_layout);
        exchangeBg();  //让它一进去的时候就调用一下这个函数
        mainVp = findViewById(R.id.main_vp);
//        添加点击事件
        // 因为当前类实现了这个接口，所以这个类可以被认为是这个接口类的子类，
        // 当前类的对象也就是接口类的对象，所以可以传入this
        addCityIv.setOnClickListener(this);
        moreIv.setOnClickListener(this);
        webviewIv.setOnClickListener(this);
        webview2Iv.setOnClickListener(this);

        //在未有数据库时，这些集合都是空的；待拥有数据库后，则有了city的个数
        //对fragment进行初始化，这是需要用到的三个集合，分别初始化
        fragmentList = new ArrayList<>();  // 有几个fragment就对应几个适配器，应该再写一个集合表示指示器的内容->表示ViewPaper的个数
//        cityList = new ArrayList<>();    //不需要去new了，现在需要的是下一行，这是获取城市的信息列表,是没有创建数据库之间进行的操作
        //获取数据库包含的城市信息列表，也就是获取数据库中存了多少个城市 --->CItyWeatherFragment
        // 因为在这里只是查询到了应该有的城市，但是实际添加还应该是将Fragment加至ViewPager中
        cityList = DBManager.queryAllCityName();
        imgList = new ArrayList<>();

        if (cityList.size()==0) { //如果它是第一次的话就没有数据，为它添加了北京
        // 做一个固定的添加，现在就是有内容的了，就可以把数据传入到fragment当中，有几个城市就写几个信息。
            cityList.add("北京");  // 那么数据库中增加的信息是在哪里增加的？是在Fragment中增加的-->CItyWeatherFragment
        }

        /* 因为可能搜索界面点击跳转此界面，会传值，所以此处获取一下*/
        try {
            Intent intent = getIntent();
            String city = intent.getStringExtra("city");
            //接收到了city，判断city是否存在于刚才已经获取到的城市列表cityList里，如果已经存在，那就不再添加了
            //  第一次进入该界面并没有搜索界面跳转进来，那么city获取到的是一个空
            //  与上city不等于空，若city为空那我们就不再添加了
            if (!cityList.contains(city)&&!TextUtils.isEmpty(city)) {
                cityList.add(city); //添加到集合中
            }
        }catch (Exception e){
            Log.i("zhou","程序出错！");
        }

//        初始化ViewPager页面的方法
        initPager(); //在这里面就已经把相关的页面初始化完成了,页面有了就可以进行操作了
        adapter = new CityFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);// 第二个是数据源
        mainVp.setAdapter(adapter);// 从上行得到adapter对象再设置给ViewPager（传入adapter）
//        创建指示器   有几个页面就创建几个指示器
        initPoint();
//        设置最后一个城市信息（也就是最近添加的城市）
        // setCurrentItem(int index)方法主要用来制定初始化的页面。例如加入3个页面通过setCurrentItem(0)制定第一个页面为当前页面
        mainVp.setCurrentItem(fragmentList.size()-1);  //最近添加
//        设置ViewPager页面监听  这样滑动页面时才能让白框变成蓝框
        setPagerListener();
    }

//            换壁纸的函数
    public void exchangeBg(){
        // 获取数据的key值与存入数据的key值的数据类型要一致，否则查找不到数据
        pref = getSharedPreferences("bg_pref", MODE_PRIVATE); //获取一下之前存储的是哪一个
        bgNum = pref.getInt("bg", 2); // 获取数据 //默认是2号被选中
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

    private void setPagerListener() {
        /* 设置监听事件*/
        mainVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override // 方法一
            /* 这个方法会在屏幕滚动过程中不断被调用。 有三个参数，第一个position。
            positionOffset是当前页面滑动比例，如果页面向右翻动，这个值不断变大，
            最后在趋近1的情况后突变为0。如果页面向左翻动，这个值不断变小，最后变为0。
            positionOffsetPixels是当前页面滑动像素，变化情况和positionOffset一致。 */
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override // 方法二
//            onPageSelected(int position)：这个方法有一个参数position，代表哪个页面被选中。当用手指滑动翻页的时候，
//            如果翻动成功了（滑动的距离够长），手指抬起来就会立即执行这个方法，position就是当前滑动到的页面。
            public void onPageSelected(int position) {   //表示页面所选择的位置
                for (int i = 0; i < imgList.size(); i++) {
                    imgList.get(i).setImageResource(R.mipmap.rectangle1);  //遍历指示器的集合，并将所有的先都改为白色
                }
                imgList.get(position).setImageResource(R.mipmap.rectangle2);//这是被选中的，设置为蓝色
            }
            @Override // 方法三
            /*这个方法在手指操作屏幕的时候发生变化。有三个值：0（END）,1(PRESS) , 2(UP) 。
            当用手指滑动翻页时，手指按下去的时候会触发这个方法，state值为1，手指抬起时，如果发生了滑动（即使很小），
            这个值会变为2，然后最后变为0 。总共执行这个方法三次。一种特殊情况是手指按下去以后一点滑动也没有发生，
            这个时候只会调用这个方法两次，state值分别是1,0 。*/
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //创建指示器的方法
    private void initPoint() {
//        创建指示器      ViewPager页面指示器的函数
        for (int i = 0; i < fragmentList.size(); i++) {  //有几个fragment就有几个指示器
            ImageView pIv = new ImageView(this);  // 矩形框应该展示在ImageView控件上
            pIv.setImageResource(R.mipmap.rectangle1);  //设置展示的背景(白色矩形框)
            //传入宽度和高度
            pIv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            // 需要先获取它的LayoutParams，然后才能设置外边距
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) pIv.getLayoutParams();
            lp.setMargins(0,0,20,0);  //设置外间距
            imgList.add(pIv);//添加到imgList中
            pointLayout.addView(pIv); //将指示器添加到pointLayout中
        }
        imgList.get(imgList.size()-1).setImageResource(R.mipmap.rectangle2);//获取一下最后一个被选中     (蓝色矩形框)
    }

    private void initPager() { //遍历cityList的长度，创建对应的Fragment页面
        /* 创建Fragment对象，添加到ViewPager数据源当中*/
        for (int i = 0; i < cityList.size(); i++) { // 看集合的长度也就是cityList.size()
            CityWeatherFragment cwFrag = new CityWeatherFragment();
            Bundle bundle = new Bundle(); // 因为CityWeatherFragment里是用Bundle来获取城市地址的，所以MainActivity里也需要传入这个城市
            bundle.putString("city",cityList.get(i)); // 得到bundle对象然后再进行添加
            cwFrag.setArguments(bundle); // 存储到bundle之后就要开始传递了，接收是getArgument，那么传递就是setArgument，传入bundle    CityWeatherFragment第75行
            fragmentList.add(cwFrag);  // 传结束后将fragment添加到集合中
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.main_iv_add:  //跳转到城市管理界面
                intent.setClass(this, CityManagerActivity.class);
                break;
            case R.id.main_iv_more:  //跳转到设置界面
                intent.setClass(this,MoreActivity.class);
                break;
            case R.id.main_iv_webview:
                intent.setClass(this,WebViewActivity.class);
                break;
            case R.id.main_iv_webview2:
                intent.setClass(this,WebView1Activity.class);
                break;
        }
        startActivity(intent);
        Toast.makeText(MainActivity.this,"点击成功！(*･ω･) ",Toast.LENGTH_SHORT).show();
    }

    /*当我们点击了删除按钮之后就会更改它所对应的内容，再次回到MainActivity会回到它哪个生命周期呢，回到onRestart生命周期*/
    /* 当页面重写加载时会调用的函数，这个函数在页面获取焦点之前进行调用，此处完成ViewPager页数的更新*/
    @Override
    protected void onRestart() {
        super.onRestart();
//        获取数据库当中还剩下的城市集合，也就是VIewPaper还剩几个页面
        List<String> list = DBManager.queryAllCityName(); //得到所有城市名称
        if (list.size()==0) { //如果全都删除了，就只增加一个北京
            list.add("北京");
        }
        cityList.clear();    //重写加载之前，清空原本数据源
        cityList.addAll(list); //添加新的list数据源,这就是新的集合
//        剩余城市也要创建对应的fragment页面
        fragmentList.clear();  //将原来的fragmentList集合清空
        initPager(); //调用initPager()方法，创建对应的Fragment内容，并添加到FragmentList当中
        adapter.notifyDataSetChanged(); //提示adapter进行更新
//        页面数量发生改变，指示器的数量也会发生变化，重写设置添加指示器
        imgList.clear();  //原来指示器的集合是imgList
        pointLayout.removeAllViews();   //将布局当中所有元素全部移除
        initPoint(); //调用initPoint()方法
        mainVp.setCurrentItem(fragmentList.size()-1);   //依然是显示在最后一个页面上
                                                        //setCurrentItem(int index)方法主要用来制定初始化的页面。
                                                        //例如加入3个页面通过setCurrentItem(0)制定第一个页面为当前页面
    }
}
