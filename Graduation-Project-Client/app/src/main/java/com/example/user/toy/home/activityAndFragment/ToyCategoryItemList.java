package com.example.user.toy.home.activityAndFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.user.toy.R;
import com.example.user.toy.home.entity.HomeListItemBean;
import com.example.user.toy.home.entity.Img;
import com.example.user.toy.home.entity.Toy;
import com.example.user.toy.home.util.RecyclerViewGridAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.widget.GridLayout.VERTICAL;

public class ToyCategoryItemList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewGridAdapter recyclerViewGridAdapter;
    private ArrayList<HomeListItemBean> dateBeanArrayList;
    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toy_category_list);
        recyclerView = findViewById(R.id.category_recycler_view);
        okHttpClient = new OkHttpClient();

        //隐藏标题栏
        getSupportActionBar().hide();
        //获取typeId
        Intent intent = this.getIntent();
        int typeId = intent.getIntExtra("typeId",0);
        Log.e("typeID", String.valueOf(typeId));
        //loadTrueData();
    }

    /**
     * 真实数据设置adapter
     */
    private void loadTrueData() {
        dateBeanArrayList = getDataFromRequest();
        //创建适配器adapter对象 参数1.上下文 2.数据加载集合
        recyclerViewGridAdapter = new RecyclerViewGridAdapter(this, dateBeanArrayList);
        //设置适配器
        recyclerView.setAdapter(recyclerViewGridAdapter);

        //布局管理器对象 参数1.上下文 2.规定显示的列数
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1) {
            @Override
            public boolean canScrollVertically() {
                // 直接禁止垂直滑动
                return false;
            }
        };
        //通过布局管理器可以控制条目排列的顺序
        gridLayoutManager.setReverseLayout(false);
        //设置RecycleView显示的方向
        gridLayoutManager.setOrientation(VERTICAL);
        //设置布局管理器， 参数linearLayoutManager对象
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    /**
     * 数据库数据调用 获取推荐裂列表
     */
    private ArrayList<HomeListItemBean> getDataFromRequest() {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), "1");
        //记得最后补上url 条件查询：根据typeId
        Request request = new Request.Builder().url("").post(body).build();
        Call call = okHttpClient.newCall(request);
        final ArrayList<HomeListItemBean> dateBeanList = new ArrayList<>();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("1", e.getMessage());
            }

            @SuppressLint("ResourceType")
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseList = response.body().string();
                List<Toy> list = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(responseList);
                    JSONArray array = (JSONArray) jsonObject.get("list");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Toy toy = new Toy();
                        toy.setAgeId(object.getString("ageId"));
                        toy.setBranchId(object.getInt("branchId"));
                        toy.setId(object.getInt("id"));
                        toy.setSex(object.getString("sex"));
                        toy.setState(object.getInt("state"));
                        toy.setTypeId(object.getInt("typeId"));
                        toy.setUserId(object.getInt("userId"));
                        toy.setProduce(object.getString("produce"));
                        toy.setPrice(object.getString("price"));
                        //images是在服务器端通过toyId找到该玩具的所有图片的集合
                        JSONArray imageArray = new JSONArray(object.getString("images"));
                        Set<Img> imageSet = new HashSet<>();
                        for (int j = 0; j < imageArray.length(); j++) {
                            JSONObject imageObject = imageArray.getJSONObject(j);
                            Img image = new Img();
                            image.setId(imageObject.getInt("id"));
                            image.setSrc(imageObject.getString("imageName"));
                            image.setToyId(imageObject.getInt("toyId"));
                            imageSet.add(image);
                        }
                        toy.setImages(imageSet);
                        list.add(toy);
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                //把获取到的toys中首页需要的字段提取出来
                for(Toy toy:list){
                    HomeListItemBean bean = new HomeListItemBean();
                    switch (toy.getAgeId()){
                        case "1":
                            bean.setAge("0-2岁");
                            break;
                        case "2":
                            bean.setAge("3-5岁");
                            break;
                        case "3":
                            bean.setAge("6-8岁");
                            break;
                        case "4":
                            bean.setAge("9-12岁");
                            break;
                    }
                    switch (toy.getTypeId()){
                        case 1:
                            bean.setType("益智玩具");
                            break;
                        case 2:
                            bean.setType("动手玩具");
                            break;
                        case 3:
                            bean.setType("装饰玩具");
                            break;

                        case 4:
                            bean.setType("机械玩具");
                            break;
                        case 5:
                            bean.setType("图纸玩具");
                            break;
                        case 6:
                            bean.setType("声音玩具");
                            break;


                    }
                    bean.setProduce(toy.getProduce());
                    bean.setId(toy.getId());
                    bean.setPrice(toy.getPrice());
                    Set<Img> images = toy.getImages();
                    List<Img> imageList = new ArrayList<Img>(images);
                    bean.setImages(images);
                    //选用Image列表第一张图片作为展示图片
                    //直接通过url访问服务器的图片
                    bean.setShowImg("" + imageList.get(0).getSrc());
                    dateBeanList.add(bean);
                    break;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(recyclerViewGridAdapter);
                    }
                });
            }
        });
        return dateBeanList;
    }

}
