package com.example.user.toy.home.activityAndFragment;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.user.toy.R;
import com.example.user.toy.home.util.RecyclerViewGridAdapter;
import com.example.user.toy.personal.entity.User;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.OkHttpClient;

import static android.widget.GridLayout.VERTICAL;

public class HomeFragment extends Fragment {

    private GridView gridView;
    private List<Map<String, Object>> dataClassificationList;
    private SimpleAdapter adapter;

    private RecyclerView recyclerView;
    //首页推荐列表实体类没有写
    /*private static ArrayList<ToyListItemBean> dateBeanArrayList;*/
    private RecyclerViewGridAdapter recyclerViewGridAdapter;

    private SmartRefreshLayout smartRefreshLayout;
    private OkHttpClient okHttpClient;

    //实体类还没有写
    /*private List<Toy> toyList;*/
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
                /*Intent intent = new Intent(getContext(), ToyCategoryItemDetail.class);
                intent.putExtra("分类信息", dataClassificationList.get(arg2).get("text").toString());
                startActivity(intent);*/

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
        //dateBeanArrayList = getDataFromRequest();
        //创建适配器adapter对象 参数1.上下文 2.数据加载集合
        //recyclerViewGridAdapter = new RecyclerViewGridAdapter(this.getContext(), dateBeanArrayList);
        //设置适配器
        //recyclerView.setAdapter(recyclerViewGridAdapter);

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


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
