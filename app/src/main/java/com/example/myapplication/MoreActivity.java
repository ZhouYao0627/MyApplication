package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.db.DBManager;
import com.example.myapplication.history.MainHistoryActivity;
import com.example.myapplication.login.loginActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;


public class MoreActivity extends AppCompatActivity implements View.OnClickListener{
    TextView bgTv,versionTv,shareTv,settingTv,historyTv,loginTv;
    RadioGroup exbgRg;
    ImageView backIv;
    private SharedPreferences pref;  // 声明SharedPreferences为全局变量

    ViewPager aboutVp; // 即MoreActivity页面中的ViewPager
    LinearLayout pointLayout; // 指示器滚动的布局，也就是LinearLayout
    List<View> viewList; //ViewPager的数据源在此处是一个集合,可以显示不同的View
    int[]picIds = {R.mipmap.ab1,R.mipmap.ab2,R.mipmap.ab3,R.mipmap.ab4,R.mipmap.ab5}; // 将图片所在的内容存放到数组里
    private AboutAdapter adapter;                                                 // 具体应该有几个数据应该看有几个图片
    List<ImageView>pointList;//存放显示器的集合

//    进行循环播放  用的是Android Studio自带的定时器的效果   handler是Android中消息通讯机制的代表
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {  // 重写一个函数，是用来接收消息的
            if (msg.what == 1) { //若消息是准确的
                //接收到消息后，需要使ViewPaper页面向下滑动一页
                int currentItem = aboutVp.getCurrentItem();  // 获取当前的页面
                aboutVp.setCurrentItem(currentItem+1);  // 设置显示下一页
                handler.sendEmptyMessageDelayed(1,3000);//3秒后再发一条
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        bgTv = findViewById(R.id.more_tv_exchangebg);
//        cacheTv = findViewById(R.id.more_tv_cache);
        versionTv = findViewById(R.id.more_tv_version);
        shareTv = findViewById(R.id.more_tv_share);
        backIv = findViewById(R.id.more_iv_back);
        exbgRg = findViewById(R.id.more_rg);
        settingTv = findViewById(R.id.more_tv_setting);
        historyTv = findViewById(R.id.more_tv_history);
//        liveTv = findViewById(R.id.more_tv_live);
//        gameTv = findViewById(R.id.more_tv_game);
//        helpTv = findViewById(R.id.more_tv_help);
//        addressTv = findViewById(R.id.more_tv_address);
        aboutVp = findViewById(R.id.about_vp);
        pointLayout = findViewById(R.id.about_layout_point);
        loginTv = findViewById(R.id.more_tv_login);

        bgTv.setOnClickListener(this);
//        cacheTv.setOnClickListener(this);
        shareTv.setOnClickListener(this);
        backIv.setOnClickListener(this);
        settingTv.setOnClickListener(this);
        historyTv.setOnClickListener(this);
//        liveTv.setOnClickListener(this);
//        gameTv.setOnClickListener(this);
//        helpTv.setOnClickListener(this);
//        addressTv.setOnClickListener(this);
        loginTv.setOnClickListener(this);

        // 存储的是我们的背景，所以叫做bg_pref，只在当前的应用获取到，所以是MODE_PRIVATE
        // 获取数据的key值与存入数据的key值的数据类型要一致，否则查找不到数据
        pref = getSharedPreferences("bg_pref", MODE_PRIVATE);

        viewList = new ArrayList<>();  // 初始化
        pointList = new ArrayList<>();

        //初始化ViewPaper的页面信息
        for(int i = 0; i<picIds.length; i++){
            View view = LayoutInflater.from(this).inflate(R.layout.item_aboutvp,null);//将这个布局转换成view对象添加到数据源的集合当中
            ImageView iv = view.findViewById(R.id.item_aboutvp_iv); //就可以找到其内部包含的ImageView，找到其内部的item_aboutvp_iv
            iv.setImageResource(picIds[i]);//显示数组当中的内容
            viewList.add(view); //添加到集合里
            //创建指示器内容
            ImageView pointIv = new ImageView(this);  // 指示器应该展示在ImageView控件上
            //在代码中设置控件的宽高和外边距等属性
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0,0,20,0);
            //将布局参数设置给ImageView
            pointIv.setLayoutParams(lp);
            pointIv.setImageResource(R.mipmap.rectangle1); // 先将所有的指示器显示为白色
            pointList.add(pointIv);//添加到集合当中便于统一管理
            pointLayout.addView(pointIv);//添加到布局当中显示出来 只有写了这句话指示器才会显示
        }
        pointList.get(0).setImageResource(R.mipmap.rectangle2);//设置第一个指示器为选中状态

        //创建适配器对象  --->AboutAdapter
        adapter = new AboutAdapter(viewList); // 传入viewList的集合
        aboutVp.setAdapter(adapter); //设置适配器
        //发送切换页面消息，what代表的是消息的符号，写上1   在handleMessage里接收
        handler.sendEmptyMessageDelayed(1,3000);
        setVpListener(); //设置ViewPaper页面监听器  设置了这个才能使得页面的改变带动指示器的改变

        setRGListener();  //设置RadioGroup的Listener      // 是将其设置到内部存储当中
    }

    private void setVpListener() {
        //设置ViewPaper的监听器
        aboutVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {  // 重写三个函数
            @Override  // 页面发生滚动时会调用的方法
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {   //表示页面所选择的位置
                for (int i = 0; i < pointList.size(); i++) {
                    pointList.get(i).setImageResource(R.mipmap.rectangle1); //将所有的先都改为白色
                }
                pointList.get(position%pointList.size()).setImageResource(R.mipmap.rectangle2);//这是被选中的，改为蓝色
            }

            @Override // 表示滚动的状态
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void setRGListener() {  // --->需要到MainActivity中判断上一次保存的是哪一个记录，咱们就去对应改变
        /* 设置改变背景图片的单选按钮的监听*/
        exbgRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                获取目前的默认壁纸
                int bg = pref.getInt("bg", 0); //默认的是0，一进来就是0
                SharedPreferences.Editor editor = pref.edit(); //写入工作   获取写入的对象editor，然后它去执行写入操作
                Intent intent = new Intent(MoreActivity.this,MainActivity.class); //跳转
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);//开启一个新栈，把原来栈清空
                switch (checkedId) {  // 获取哪一个RadioButton被点击到了
                    case R.id.more_rb_green:
                        if (bg==0) {
                            Toast.makeText(MoreActivity.this,"您选择的为当前背景，无需更改！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putInt("bg",0); // 存入数据
                        editor.commit(); //提交修改，也可使用apply()提交
                        break;
                    case R.id.more_rb_pink:
                        if (bg==1) {
                            Toast.makeText(MoreActivity.this,"您选择的为当前背景，无需更改！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putInt("bg",1);
                        editor.commit();
                        break;
                    case R.id.more_rb_blue:
                        if (bg==2) {
                            Toast.makeText(MoreActivity.this,"您选择的为当前背景，无需更改！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putInt("bg",2);
                        editor.commit();
                        break;
                }
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.more_iv_back:
                finish();    //返回
                break;
            case R.id.more_tv_setting:
                clearSetting(); //恢复出厂设置 就是将数据库当中的信息给删除掉
                break;
            case R.id.more_tv_share:
                shareSoftwareMsg("ZY天气！"); //分享信息
                break;
            case R.id.more_tv_history:
                Intent intent = new Intent();
                intent.setClass(this, MainHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.more_tv_exchangebg:                   // 判断它的状态
                if (exbgRg.getVisibility() == View.VISIBLE) {//若是显示状态就让其隐藏
                    exbgRg.setVisibility(View.GONE);
                }else{
                    exbgRg.setVisibility(View.VISIBLE);//否则，就让其显示
                }
                break;
            case R.id.more_tv_login:
                Intent intent1 = new Intent();
                intent1.setClass(this, loginActivity.class);
                startActivity(intent1);
                break;
        }
        Toast.makeText(MoreActivity.this,"点击成功！(*･ω･) ",Toast.LENGTH_SHORT).show();
    }

    private void shareSoftwareMsg(String s) {
        /* 分享软件的函数*/
        //系统自带的分享文本的这样一个函数
        // ACTION_SEND  intent可以把自己的应用添加到系统的发送（分享）列表中。
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");//这代表分享的是文本类型的
        //分享的内容s就是我们传进来的这个值，然后将其分享出去
        intent.putExtra(Intent.EXTRA_TEXT,s);
        startActivity(Intent.createChooser(intent,"分享"));//进行跳转
    }

    private void clearSetting() {
        /* 恢复出厂设置的函数*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("确定要恢复出厂设置？").setIcon(R.drawable.icon_hint);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { // 确定后是进行清空数据库的操作
                DBManager.deleteAllInfo();
                Toast.makeText(MoreActivity.this,"已恢复出厂设置！",Toast.LENGTH_SHORT).show();
                /*删除数据库中的内容会默认跳转回首界面，而跳转回首界面最好的方法是开一个新栈，把原来栈中内容全部清空
                 *也就是将activity的后退项全部清空，不考虑后退了，直接进入新栈跳转到MainActivity的界面*/
                Intent intent = new Intent(MoreActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).setNegativeButton("取消",null).setNeutralButton("再想想",null);
        builder.create().show();
    }
}
