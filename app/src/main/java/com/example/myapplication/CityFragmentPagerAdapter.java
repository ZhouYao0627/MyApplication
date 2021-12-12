package com.example.myapplication;

//将CityWeatherFragment添加到MainActivity中的ViewPaper里，因此使用此适配器
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

//当ViewPaper发生变化时，继承的是FragmentStatePagerAdapter
public class CityFragmentPagerAdapter extends FragmentStatePagerAdapter { //重写两个方法
    List<Fragment>fragmentList;  // 在构造方法当中，我们可以将ViewPaper中的数据源传入进来也就是集合LIst，
                                 // LIst的范型就是CityWeatherFragment可以写上Fragment，通过构造方法进行传递
    public CityFragmentPagerAdapter(FragmentManager fm, List<Fragment>fragmentLis) {
        super(fm);
        this.fragmentList = fragmentLis;  //放到构造方法当中
    }

    @Override // 返回指定位置对应的fragment
    public Fragment getItem(int position) {  //返回指定位置对应的fragment
        return fragmentList.get(position);
    }

    @Override // 返回fragmentList的集合的长度
    public int getCount() { //返回fragment的集合长度
        return fragmentList.size();
    }

    int childCount = 0;   //表示ViewPager包含的页数

//    当ViewPager的页数发生改变时，必须要重写两个函数
//    它才能去做更新数据的操作，否则直接去用notifyDataSetChanged是无用的
    @Override
    public void notifyDataSetChanged() { //重写notifyDataSetChanged()方法
        this.childCount = getCount(); //要想改变ViewPaper的数量，首先需要先获取一下此时子页面的数量有几个
        super.notifyDataSetChanged(); //调用父类的notifyDataSetChanged()方法
    }

    @Override
    public int getItemPosition(@NonNull Object object) { //获取哪个位置的item
        if (childCount>0) {//看子页面的数量是否大于0    因为它是重新更新的，我们只加载一张
            childCount--;
            return POSITION_NONE;//返回一个位置的标识
        }
        return super.getItemPosition(object); //否则返回父类的内容
    }
}