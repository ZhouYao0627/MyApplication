package com.example.myapplication.history;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.bean_history.HistoryBean;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoryAdapter extends BaseAdapter{
    Context context;
    List<HistoryBean.ResultBean> mDatas;

    public HistoryAdapter(Context context, List<HistoryBean.ResultBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;  // 进行声明
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_main_timeline,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        HistoryBean.ResultBean resultBean = mDatas.get(position); // 得到指定位置的数据源
//        判断当前位置的年份和上一个位置是否相同
        if (position!=0) { // 不是第一个
            HistoryBean.ResultBean lastBean = mDatas.get(position - 1); // 获取上一个位置
            if (resultBean.getYear()==lastBean.getYear()) { // 若当前位置的年份等于上一个年份，就说明一年当中有两个事件，就不用显示那么多
                holder.timeLayout.setVisibility(View.GONE);
            }else {  // 即当前位置的年份与上一个位置的年份不同，就让其显示出来
                holder.timeLayout.setVisibility(View.VISIBLE);
            }
        }else { // 是第一个
            holder.timeLayout.setVisibility(View.VISIBLE);  // 设置为显示状态
        }

        holder.timeTv.setText(resultBean.getYear()+"-"+resultBean.getMonth()+"-"+resultBean.getDay()); // 年份的显示 // 拼接年月日
        holder.titleTv.setText(resultBean.getTitle()); // 获取标题
        String picURL = resultBean.getPic(); // 获取图片的地址
        if (TextUtils.isEmpty(picURL)) {
            holder.picIv.setVisibility(View.GONE); // 若图片的地址为空就不显示图片
        }else {
            holder.picIv.setVisibility(View.VISIBLE);  // 若图片的地址不为空就显示图片
            Picasso.with(context).load(picURL).into(holder.picIv); // 加载
        }
        return convertView;
    }

    class ViewHolder{    // 慧言：为了减少itemView.findViewById的次数
        TextView timeTv,titleTv;
        ImageView picIv;
        LinearLayout timeLayout;
        public ViewHolder(View itemView){
            timeTv = itemView.findViewById(R.id.item_main_time);
            titleTv = itemView.findViewById(R.id.item_main_title);
            picIv = itemView.findViewById(R.id.item_main_pic);
            timeLayout = itemView.findViewById(R.id.item_main_ll);
        }
    }

}
