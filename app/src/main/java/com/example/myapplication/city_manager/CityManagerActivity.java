package com.example.myapplication.city_manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.myapplication.R;
import com.example.myapplication.db.DBManager;
import com.example.myapplication.db.DatabaseBean;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

public class CityManagerActivity extends AppCompatActivity implements View.OnClickListener{ //让当前类实现接口OnClickListener
    ImageView addIv,backIv,deleteIv;
    ListView cityLv;
    List<DatabaseBean> mDatas;  /*显示列表数据源   也包含数据源信息显示在listview当中,
    listview加载的是数据库的信息,数据库中每一条就相当于一个对象,是用DataBaseBean这个类来封装的,
    应该把这个类中每一个对象存储到list集合当中,作为listview的一条记录*/
    private CityManagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        //查找控件以及对数据源进行初始化
        addIv = findViewById(R.id.city_iv_add);
        backIv = findViewById(R.id.city_iv_back);
        deleteIv = findViewById(R.id.city_iv_delete);
        cityLv = findViewById(R.id.city_lv);
        mDatas = new ArrayList<>();
//        添加点击监听事件
        addIv.setOnClickListener(this);
        deleteIv.setOnClickListener(this);
        backIv.setOnClickListener(this);
//        设置适配器    是关于ListView的
        adapter = new CityManagerAdapter(this, mDatas);
        cityLv.setAdapter(adapter);
    }

/*  获取数据库当中真实数据源，添加到原有数据源当中，提示适配器更新*/
    @Override
    protected void onResume() { // 删除之后需要重新获取焦点
        super.onResume();
        List<DatabaseBean> list = DBManager.queryAllInfo(); //获取全部信息
        mDatas.clear(); //清空原来的，因为每次返回都会获取焦点
        mDatas.addAll(list); //添加
        adapter.notifyDataSetChanged();  //提示更新
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city_iv_add:  // SearchCityActivity
                int cityCount = DBManager.getCityCount();
                if (cityCount<6) {
                    Intent intent = new Intent(this, SearchCityActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(this,"对不起，你的城市数量已达上限，请删除后再增加",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.city_iv_back:
                finish();  // 返回到上一级，直接finish()便可以了
                break;
            case R.id.city_iv_delete: // DeleteCityActivity
                Intent intent1 = new Intent(this, DeleteCityActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
