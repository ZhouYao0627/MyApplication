package com.example.myapplication.history;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.base_history.ContentURL;
import com.example.myapplication.bean_history.HistoryBean;
import com.example.myapplication.bean_history.LaoHuangliBean;
import com.google.gson.Gson;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainHistoryActivity extends BaseActivity implements View.OnClickListener {

    private ListView mainLv;
    private ImageButton imgBtn;
    List<HistoryBean.ResultBean> mDatas;
    private HistoryAdapter adapter;
    private Calendar calendar;
    private Date date; // 表示时间
    HistoryBean historyBean;
    //    声明头布局当中的TextView
    TextView yinliTv, dayTv, weekTv, yangliTv, baijiTv, wuxingTv, chongshaTv, jishenTv, xiongshenTv, yiTv, jiTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_main);
        mainLv = (ListView) findViewById(R.id.main_1v);
        imgBtn = (ImageButton) findViewById(R.id.main_imgbtn);
        imgBtn.setOnClickListener(this);

        mDatas = new ArrayList<>();
        adapter = new HistoryAdapter(this, mDatas);
        mainLv.setAdapter(adapter); // 设置给Listview
//        获取日历对象
        calendar = Calendar.getInstance();
        date = new Date();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH)+1; // 获取月份
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        addHeaderAndFooterView();  // 增加头尾布局的方法

        String todayHistoryURL = ContentURL.getTodayHistoryURL("1.0", month, day); // 获取网址
        loadData(todayHistoryURL); // 加载网络数据的方法,一旦成功了就在onSuccess中显示
        /* 因为ListView添加头布局了，所以position对应集合的位置发生变化，集合第0个数据，position为第1个数据，所以要减掉一个*/

    }

    private void addHeaderAndFooterView() {
//        给ListView添加头尾布局函数
        View headerView = LayoutInflater.from(this).inflate(R.layout.main_headerview,null);// 将xml文件转换为布局的对象
        initHeaderView(headerView); // 初始化
        mainLv.addHeaderView(headerView);

        View footerView = LayoutInflater.from(this).inflate(R.layout.main_footer,null);
        footerView.setTag("footer");
        footerView.setOnClickListener(this);
        mainLv.addFooterView(footerView);
    }

    private void initHeaderView(View headerView) {
        /* 初始化ListView头布局当中的每一个控件*/
        yinliTv = headerView.findViewById(R.id.main_header_tv_nongli);
        dayTv = headerView.findViewById(R.id.main_header_tv_day);
        weekTv = headerView.findViewById(R.id.main_header_tv_week);
        yangliTv = headerView.findViewById(R.id.main_header_tv_yangli);
        baijiTv = headerView.findViewById(R.id.main_header_tv_baiji);
        wuxingTv = headerView.findViewById(R.id.main_header_tv_wuxing);
        chongshaTv = headerView.findViewById(R.id.main_header_tv_chongsha);
        jishenTv = headerView.findViewById(R.id.main_header_tv_jishen);
        xiongshenTv = headerView.findViewById(R.id.main_header_tv_xiongshen);
        yiTv = headerView.findViewById(R.id.main_header_tv_yi);
        jiTv = headerView.findViewById(R.id.main_header_tv_ji);
//        将日期对象转换成指定格式的字符串形式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 年月日
        String time = sdf.format(date); // 得到时间对象
        String laohuangliURL = ContentURL.getLaohuangliURL(time);
        loadHeaderData(laohuangliURL);
    }

    private void loadHeaderData(String laohuangliURL) {
        /* 获取老黄历接口的数据*/
        RequestParams params = new RequestParams(laohuangliURL);
        x.http().get(params, new CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LaoHuangliBean huangliBean = new Gson().fromJson(result, LaoHuangliBean.class); // 解析。解析result，解析成老黄历对象
                LaoHuangliBean.ResultBean resultBean = huangliBean.getResult();
                yinliTv.setText("农历 "+resultBean.getYinli()+" (阴历)");
                String[] yangliArr = resultBean.getYangli().split("-"); // 分割
                String week = getWeek(Integer.parseInt(yangliArr[0]), Integer.parseInt(yangliArr[1]), Integer.parseInt(yangliArr[2]));
                yangliTv.setText("公历 "+yangliArr[0]+"年"+yangliArr[1]+"月"+yangliArr[2]+"日 "+week+"(阳历)");

                dayTv.setText(yangliArr[2]);
                weekTv.setText(week);
                baijiTv.setText("彭祖百忌:"+resultBean.getBaiji());
                wuxingTv.setText("五行:"+resultBean.getWuxing());
                chongshaTv.setText("冲煞:"+resultBean.getChongsha());
                jishenTv.setText("吉神宜趋:"+resultBean.getJishen());
                xiongshenTv.setText("凶神宜忌:"+resultBean.getXiongshen());
                yiTv.setText("宜:"+resultBean.getYi());
                jiTv.setText("忌:"+resultBean.getJi());
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) { }
            @Override
            public void onCancelled(CancelledException cex) { }
            @Override
            public void onFinished() { }
        });
    }

    private String getWeek(int year, int month, int day) {
//        根据年月日获取对应的星期
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month-1,day);
        String weeks[] = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
        int index = calendar.get(Calendar.DAY_OF_WEEK)-1; // 获取一下星期几的角标  美国1是代表星期日，2是代表星期一，所以需要减一
        if (index<0){
            index = 0;
        }
        return weeks[index];
    }

    @Override
    public void onSuccess(String result) {
        mDatas.clear(); // 先将原来的内容先清空
        historyBean = new Gson().fromJson(result, HistoryBean.class);
        List<HistoryBean.ResultBean> list = historyBean.getResult();
        for (int i = 0; i < 5; i++) {  // 添加五条进去
            mDatas.add(list.get(i));
        }
        adapter.notifyDataSetChanged(); // 通过Adapter更新
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_imgbtn) {
            popCalendarDialog();  // 弹出日历对话框
            return;
        }
        String tag = (String) v.getTag();
        if (tag.equals("footer")) { // 若点击的是加载更多
            Intent intent = new Intent(this, HistoryActivity.class);
            if (historyBean!=null) {  // 若不等于空则去传值，否则就不传值
                Bundle bundle = new Bundle();
                bundle.putSerializable("history",historyBean);
                intent.putExtras(bundle);
            }
            startActivity(intent);
        }
    }

    private void popCalendarDialog() {
        //        弹出日历对话框
        // 只要在对话框中选择了其它日期，则需要改变老黄历显示的内容，历史上的今天数据
        Calendar calendar = Calendar.getInstance();
        // 采用弹窗作为日期控件的显示方式，这个以弹窗方式显示的日期控件叫做DatePickerDialog
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                改变日历显示的内容
                String time = year+"-"+(month+1)+"-"+dayOfMonth; // 获取年月日
                String laohuangliURL = ContentURL.getLaohuangliURL(time); // 获取一个新的网址，传入时间
                loadHeaderData(laohuangliURL); // 加载数据

//                改变历史上的今天数据
                String todayHistoryURL = ContentURL.getTodayHistoryURL("1.0", (month + 1), dayOfMonth); // 获取内容
                loadData(todayHistoryURL); // 加载数据
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

}