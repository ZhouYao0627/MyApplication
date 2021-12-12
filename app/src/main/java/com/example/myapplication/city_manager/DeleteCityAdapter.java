package com.example.myapplication.city_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapplication.R;
import java.util.List;

public class DeleteCityAdapter extends BaseAdapter{
    Context context; // 传入上下文对象
    List<String>mDatas; // 数据源
    List<String>deleteCitys;//表示要删除的城市记录

    //通过构造方法进行传递
    public DeleteCityAdapter(Context context, List<String> mDatas,List<String>deleteCitys) {
        this.context = context;
        this.mDatas = mDatas;
        this.deleteCitys = deleteCitys;
    }

    @Override
    public int getCount() {
        return mDatas.size(); //返回集合的长度
    }
    @Override
    public Object getItem(int position) {
        return mDatas.get(position); //返回指定位置的数据
    }
    @Override
    public long getItemId(int position) {
        return position;  //返回位置
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {  //看是否能有复用的convertView，若没有则需要生成
            convertView = LayoutInflater.from(context).inflate(R.layout.item_deletecity,null);//将指定的布局进行生成
            holder = new ViewHolder(convertView);//生成holder对象，，因为此时需要对于convertView中的内容进行初始化，
            // 将convertView传入进去，在构造函数当中就可以完成初始化
            convertView.setTag(holder);//设置标记
        }else{ //获取holder对象
            holder = (ViewHolder) convertView.getTag();
        }
        final String city = mDatas.get(position);//获取指定位置对应的数据源(获取指定位置对应的city)设置到TextView上
        holder.tv.setText(city);
        holder.iv.setOnClickListener(new View.OnClickListener() { //imageview是为了点击删除这一项内容的
            @Override
            public void onClick(View v) {
                mDatas.remove(city);//将mDatas指定位置的这一条记录删除掉，删除这个城市
                deleteCitys.add(city);//一旦记录被删除了就将他记录到deleteCitys当中 // 确定删除是在圆号的那个按钮里删除的，这里只是做了一个临时的删除工作
                notifyDataSetChanged();//删除了并提示适配器更新，只是在listview中删除了，并没有对数据库进行操作，也没用保存这条删除的记录
            }
        });
        return convertView;
    }

    class ViewHolder{ //使用ViewHolder进行声明
        TextView tv;
        ImageView iv;
        public ViewHolder(View itemView){ //通过构造方法进行传递
            tv = itemView.findViewById(R.id.item_delete_tv);
            iv = itemView.findViewById(R.id.item_delete_iv);
        }
    }
}
