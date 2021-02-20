package com.example.user.toy.home.activityAndFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.user.toy.LoginByPhoneActivity;
import com.example.user.toy.R;
import com.example.user.toy.TabHostActivity;
import com.example.user.toy.home.entity.HomeListItemBean;
import com.example.user.toy.home.entity.Img;
import com.example.user.toy.home.entity.Toy;
import com.example.user.toy.home.util.RecyclerViewGridAdapter;
import com.example.user.toy.personal.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.widget.GridLayout.VERTICAL;

public class HomeFragment extends Fragment {

    //搜索框
    private SearchView searchView;
    private GridView gridView;
    private List<Map<String, Object>> dataClassificationList;
    private SimpleAdapter adapter;

    private RecyclerView recyclerView;
    private RecyclerViewGridAdapter recyclerViewGridAdapter;
    private ArrayList<HomeListItemBean> dateBeanArrayList;

    private SmartRefreshLayout smartRefreshLayout;
    private OkHttpClient okHttpClient;

    private User user;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        findViews(view);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) this.getContext(), PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
        initClassificationData();
        setClassification();
        setSearchView();
        loadTrueData();
        refresh();
        return view;
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    public void findViews(View view) {
        searchView = view.findViewById(R.id.home_searchView);
        gridView = view.findViewById(R.id.home_classification);
        recyclerView = view.findViewById(R.id.home_recycler_view);
        smartRefreshLayout = view.findViewById(R.id.home_smartRefreshLayout);
        //启用刷新
        smartRefreshLayout.setEnableRefresh(true);
        //启用加载
        smartRefreshLayout.setEnableLoadmore(true);
        //
        okHttpClient = new OkHttpClient();

    }

   /*
   * 搜索框
   */
   public void setSearchView(){
       //设置该SearchView显示搜索按钮
       searchView.setSubmitButtonEnabled(true);
       //设置默认提示文字
       searchView.setQueryHint("输入您想查找的内容");
       //配置监听器
       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           // 当点击搜索按钮时将搜索字段传个下个页面
           @Override
           public boolean onQueryTextSubmit(String query) {
               Intent intent = new Intent(getContext(),SearchListActivity.class);
               intent.putExtra("搜索字段",query);
               startActivity(intent);
               return false;
           }

           @Override
           public boolean onQueryTextChange(String s) {
               return false;
           }
       });

   }


    /**
     * 初始化Classification数据
     */
    public void initClassificationData() {
        //图标
        int icon[] = {R.drawable.i1, R.drawable.i1, R.drawable.i1,
                R.drawable.i1, R.drawable.i1, R.drawable.i1};
        //图标下的文字
        String name[] = {"益智玩具","动手玩具","装饰玩具","机械玩具","图纸玩具","声音玩具"};
        dataClassificationList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", icon[i]);
            map.put("text", name[i]);
            dataClassificationList.add(map);
        }
    }

    /**
     * 通过适配器将数据添加到GridView控件中
     */
    public void setClassification() {
        String[] from = {"img", "text"};
        int[] to = {R.id.home_classification_img, R.id.home_classification_text};

        adapter = new SimpleAdapter(this.getContext(), dataClassificationList, R.layout.home_classification_item, from, to);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Toast.makeText(getContext(), dataClassificationList.get(arg2).get("text").toString(), Toast.LENGTH_SHORT).show();
                //跳转到分类详情页面
                Intent intent = new Intent(getContext(), ToyCategoryItemDetail.class);
                intent.putExtra("分类信息", dataClassificationList.get(arg2).get("text").toString());
                startActivity(intent);

            }
        });
    }

    /**
     * 上拉加载下拉刷新
     */
    public void refresh() {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                recyclerViewGridAdapter.notifyDataSetChanged();
                refreshlayout.finishRefresh();
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                recyclerViewGridAdapter.notifyDataSetChanged();
                refreshlayout.finishLoadmore();
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

    /**
     * 真实数据设置adapter
     */
    private void loadTrueData() {
        dateBeanArrayList = getDataFromRequest();
        //创建适配器adapter对象 参数1.上下文 2.数据加载集合
        recyclerViewGridAdapter = new RecyclerViewGridAdapter(this.getContext(), dateBeanArrayList);
        //设置适配器
        recyclerView.setAdapter(recyclerViewGridAdapter);

        //布局管理器对象 参数1.上下文 2.规定显示的行数
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2) {
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
     * 数据库数据调用
     */
    private ArrayList<HomeListItemBean> getDataFromRequest() {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), "1");
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(recyclerViewGridAdapter);
                    }
                });
            }
        });
        return dateBeanList;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
