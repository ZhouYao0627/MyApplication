package com.example.myapplication.base;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import androidx.fragment.app.Fragment;

/*xutils加载网络数据的步骤
* 1.声明整体模块（一般是在app当中声明的->UniteApp，并让它在onCreate方法中声明我们的信息）
*  2.执行网络请求操作（一般是x.http().）
* */

// 将其整体封装
public class BaseFragment extends Fragment implements Callback.CommonCallback<String>{
    // 这是一个回调接口，我们为了能让写的清楚一些，在类当中实现这个接口，
    // 里面是我们得到的数据类型，是String类型的，再重写四个方法

    public void loadData(String path){ //加载数据的方法，传入需加载的urll对象
        RequestParams params = new RequestParams(path); // 将请求网址路径给包装进去 传入路径path
        x.http().get(params,this); // get网络请求数据 //params为请求参数信息，将参数信息包装进去，第二个需要传入的是CommonCallback
        //既然当前的类已经实现了这个接口，那么当前类的对象就是这个接口对象，那么this这个当前类的对象，就是这个接口对象
    }

//  已经将其定义到一个类当中了，那么这些方法就是这个类当中的方法，等下继承这个BaseFragment，想重写哪个方法就重写拿个方法

//  获取数据成功时，会回调的接口
    @Override
    public void onSuccess(String result) {}

//  获取数据失败时，会调用的接口
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {}

//  取消请求时，会调用的接口
    @Override
    public void onCancelled(CancelledException cex) {}

//  请求完成后，会回调的接口
    @Override
    public void onFinished() {}
}
