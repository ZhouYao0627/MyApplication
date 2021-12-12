package com.example.myapplication.city_manager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.bean.WeatherBean;
import com.google.gson.Gson;
//将原本继承的AppCompatActivity改为BaseActivity
public class SearchCityActivity extends BaseActivity implements View.OnClickListener{ //实现OnClickListener的接口
    EditText searchEt;
    ImageView submitIv;
    GridView searchGv;
    String[]hotCitys = {"北京","上海","广州","深圳","珠海","佛山","南京","苏州","厦门", // 把这个数据源添加到GridView中
    "长沙","成都","福州","杭州","武汉","青岛","西安","太原","沈阳","重庆","天津","南宁"}; // 因为这里面只有TextView，所以可以只使用简单的适配器
    private ArrayAdapter<String> adapter;
    String url1 = "https://wis.qq.com/weather/common?source=pc&weather_type=observe|index|rise|alarm|air|tips|forecast_24h&province=";
    String url2 = "&city=";
    String city;
    String provice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        searchEt = findViewById(R.id.search_et);
        submitIv = findViewById(R.id.search_iv_submit);
        searchGv = findViewById(R.id.search_gv);
        submitIv.setOnClickListener(this);

//      设置适配器，加载到item_hotcity布局当中，数据源是hotCitys
        // 第一个参数为当前的上下文对象，第二个参数为加载到哪个布局当中，第三个为我们的数据源
        adapter = new ArrayAdapter<>(this, R.layout.item_hotcity, hotCitys);
        searchGv.setAdapter(adapter); //设置Adapter
        setListener();
    }

/* 设置监听事件*/
    private void setListener() {
        searchGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {// setOnItemClickListener单击时选中点击的Items
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city = hotCitys[position]; //获取指定位置的城市
//                获取省份
                provice=GetProvice(city);
                String url = url1+provice+url2+city;  //字符串的拼接
                loadData(url);//调用其父类加载网络的方法  若成功则会调用onSuccess方法
            }
        });
    }
/*    (AdapterView<?> parent, View view, int position, long id)
arg0，即parent：相当于listview Y适配器的一个指针，可以通过它来获得Y里装着的一切东西，再通俗点就是说告诉你，你点的是Y，不是X
arg1,即view：是你点的b这个view的句柄，就是你可以用这个view，来获得b里的控件的id后操作控件，通过它可以获得该项中的各个组件，例如arg1.textview.settext(“asd”);
arg2,即position：是b在Y适配器里的位置（生成listview时，适配器一个一个的做item，然后把他们按顺序排好队，在放到listview里，意思就是这个b是第position号做好的）
arg3，即id：是b在listview Y里的第几行的位置（很明显是第2行），在没有headerView，用户添加的view以及footerView的情况下position和id的值是一样的
*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_iv_submit:                                   // 将Activity中联网的操作都封装到BaseActivity中
                city = searchEt.getText().toString();//获取city的内容     需要进行联网的操作->BaseActivity
                if (!TextUtils.isEmpty(city)) { //若不为空
//                  判断是否能够找到这个城市   是根据联网返回的error的值来判断是否找到这个城市
                    provice=GetProvice(city);
                    String url = url1+provice+url2+city;
                    loadData(url);
                }else {  //若为空
                    Toast.makeText(this,"您未输入内容，请重新输入！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onSuccess(String result) { //result就是我们获取到的数据
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class); //对其进行解析 解析为WeatherBean类
        if (weatherBean.getData().getIndex().getClothes()!=null) { // 判断城市是否存在   //说明城市存在
            /*若存在就应该跳转回首界面  跳转回首界面最好的方法是开一个新栈，把原来栈中内容全部清空
            *activity的后退项全部清空，咱们就不考虑后退了，直接进入新栈跳转到MainActivity的界面*/
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); //清空原来的栈，再开一个新栈
            city=provice+" "+city;
            intent.putExtra("city",city);
            startActivity(intent);
        }else{ //城市不存在
            Toast.makeText(this,"对不起，未搜索到此城市信息，请重新输入！",Toast.LENGTH_SHORT).show();
        }
    }

    private String GetProvice(String city) {
        String[]Citys = {"北京","上海","广东省 广州","广东省 深圳","广东省 珠海","广东省 佛山",
                "江苏省 南京","江苏省 苏州","福建省 厦门","湖南省 长沙","四川省 成都","福建省 福州","浙江省 杭州",
                "湖北省 武汉","山东省 青岛","陕西省 西安","山西省 太原","辽宁省 沈阳","重庆","天津","广西省 南宁"};
        for(int i=0;i<Citys.length;i++){
            if(Citys[i].contains(city)){
                if(Citys[i].split(" ").length>1)
                {   provice =Citys[i].split(" ")[0];
                }
                else
                {
                    provice = Citys[i].split(" ")[0];
                }
                break;
            }
        }
        return provice;
    }
}
