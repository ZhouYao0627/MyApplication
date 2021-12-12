package com.example.myapplication.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.bean_history.HistoryBean;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView emptyTv;  // 这个是暂无数据
    private ListView historyLv;
    private ImageView backIv;

    List<HistoryBean.ResultBean> mDatas;
    private HistoryAdapter adapter;
    HistoryBean historyBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        emptyTv = (TextView) findViewById(R.id.history_tv);
        historyLv = (ListView) findViewById(R.id.history_lv);
        backIv = (ImageView) findViewById(R.id.history_iv_back);
        backIv.setOnClickListener(this);
        mDatas = new ArrayList<>();
        adapter = new HistoryAdapter(this, mDatas);
        historyLv.setAdapter(adapter);

        try {  // 获取上一级的数据
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();  // 能够得到传来的Bundle对象
            historyBean = (HistoryBean)bundle.getSerializable("history");
            List<HistoryBean.ResultBean> list = historyBean.getResult();
            mDatas.addAll(list); // 将集合添加到mDatas当中
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            emptyTv.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.history_iv_back:
                finish();
                break;
        }
    }
}
