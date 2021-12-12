package com.example.myapplication.city_manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.db.DBManager;

import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

public class DeleteCityActivity extends AppCompatActivity implements View.OnClickListener{ //实现接口OnClickListener,重写onCLick方法
    ImageView errorIv,rightIv;
    ListView deleteLv;
    List<String>mDatas;   //listview的数据源，因为只显示城市，所以是String
    List<String>deleteCitys;  //表示存储了删除的城市信息(存储它的目的是为了等下做统一的删除)
    private DeleteCityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_city);
        errorIv = findViewById(R.id.delete_iv_error);
        rightIv = findViewById(R.id.delete_iv_right);
        deleteLv = findViewById(R.id.delete_lv);
        mDatas = DBManager.queryAllCityName();
        deleteCitys = new ArrayList<>();
//        设置点击监听事件
        errorIv.setOnClickListener(this);
        rightIv.setOnClickListener(this);
//        适配器的设置
        adapter = new DeleteCityAdapter(this, mDatas, deleteCitys);
        deleteLv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_iv_error:  //按的是叉号  取消删除
                AlertDialog.Builder builder = new AlertDialog.Builder(this); //弹出对话框
                builder.setTitle("提示信息").setMessage("是否放弃更改？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    finish();   //关闭当前的activity
                            }
                        });
                builder.setNegativeButton("取消",null);
                builder.create().show();
                break;
            case R.id.delete_iv_right://按的是圆号  也就是确认删除
                for (int i = 0; i < deleteCitys.size(); i++) { // 删除的是deleteCitys当中的内容
                    String city = deleteCitys.get(i); //获取要删除的内容
                    int i1 = DBManager.deleteInfoByCity(city); // 调用删除城市的函数  根据城市名来删除这条城市的信息记录
                }
//                删除成功返回上一级页面
                finish();
                break;
        }
    }
}
