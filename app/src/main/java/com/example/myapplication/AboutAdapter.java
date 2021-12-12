package com.example.myapplication;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;
// 希望的是在第五张图片之后还能继续滑动
public class AboutAdapter extends PagerAdapter {

    List<View>viewList; // 传入数据源

    //通过构造函数进行传递
    public AboutAdapter(List<View> viewList) {
        this.viewList = viewList;
    }

    @Override     //要加载的页数   返回的是可以滑动的页面的数目
    public int getCount() {  // 它返回的是我们可以滑动的页面的个数
        return Integer.MAX_VALUE; // 将页数设置成无限,使得即使超过了5页依旧可以滑动
    }

    @Override     //判断生成的View和object对象是否为同一个对象
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override      //接下来要生成位置的view
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = viewList.get(position%viewList.size());//让位置只在0-4之间发生变化
        container.addView(view);//添加到容器当中，容器就是viewpaper的页面
        return view;
    }

    @Override     //销毁指定位置的view   当这个页面离开视线之后，它失去焦点，我们将其销毁
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = viewList.get(position%viewList.size());
        container.removeView(view);
    }
}
