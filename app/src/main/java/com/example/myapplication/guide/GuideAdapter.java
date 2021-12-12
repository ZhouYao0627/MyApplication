package com.example.myapplication.guide;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class GuideAdapter extends PagerAdapter {
    List<View> viewList;

    //通过构造函数进行传递
    public GuideAdapter(List<View> viewList) {
        this.viewList = viewList;
    }

    @Override  //要加载的页数
    public int getCount() {
        return viewList.size();
    }

    @Override //判断生成的View和object对象是否为同一个对象
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override  //接下来要生成位置的view
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = viewList.get(position);
        container.addView(view);
        return view;
    }

    @Override  //销毁指定位置的view   当这个页面离开视线之后，它失去焦点，我们将其销毁
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = viewList.get(position);
        container.removeView(view);
    }
}
