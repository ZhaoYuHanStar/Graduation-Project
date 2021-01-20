package com.example.user.toy;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.user.toy.home.activityAndFragment.HomeFragment;
import com.example.user.toy.personal.activityAndFragment.PersonalFragment;

import java.util.HashMap;
import java.util.Map;

public class TabHostActivity extends AppCompatActivity {
    private FragmentTabHost tabHost = null;
    private String [] tabStr = {"首页","我的"};
    private Class[] fragementArr = {HomeFragment.class, PersonalFragment.class};
    //一般状态的显示图标
    private int [] imageArr = {R.drawable.tabhost_home_normal,R.drawable.tabhost_personal_normal};
    //选中状态的显示图标
    private int [] imgArr = {R.drawable.tabhost_home_selected,R.drawable.tabhost_personal_selected};
    //TabSpex显示的imageView所属的view
    private Map<String,View> map = new HashMap<>(  );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.tabhost_layout );

        //去除标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        initTabHost();

        setCurImage( tabStr[0] );
        //设置选中状态的监听器
        tabHost.setOnTabChangedListener( new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                setCurImage( tabId );
            }
        } );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("AAA","Resume");
    }

    //完成fragmentTabHost的初始化
    private void initTabHost() {
        //1.获取fragmenttabhost控件对象
        tabHost = findViewById( android.R.id.tabhost );
        //2.初始化获取fragmenttabhost控件对象
        tabHost.setup( this,getSupportFragmentManager(),android.R.id.tabcontent );
        //3.创建tabspec并添加到fragmenttabhost中
        for (int i=0;i<fragementArr.length;i++){
            View newView = getTabSpecView( i );
            TabHost.TabSpec tabSpec = tabHost.newTabSpec( tabStr[i] ).setIndicator( newView );
            map.put( tabStr[i],newView );
            tabHost.addTab( tabSpec,fragementArr[i],null );
        }


    }
    //创建TabSpec显示的view
    private View getTabSpecView(int i) {
        View newView=getLayoutInflater().inflate( R.layout.tabweight_layout,null );

        ImageView imageView = newView.findViewById( R.id.iv_image  );
        imageView.setImageResource( imageArr[i] );

        TextView textView = newView.findViewById(R.id.tv_text  );
        textView.setText( tabStr[i] );

        return newView;
    }

    private   void setCurImage(String tabId){
        for(int i = 0;i<tabStr.length;++i){
            ImageView imageView = map.get(tabStr[i]).findViewById(R.id.iv_image);
            TextView textView = map.get(tabStr[i]).findViewById(R.id.tv_text);
            if(tabId == tabStr[i]){
                imageView.setImageResource( imgArr[i] );
                textView.setTextColor(getResources().getColor(R.color.tabweight_font));

            }else {
                imageView.setImageResource( imageArr[i] );
                textView.setTextColor(Color.BLACK);
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(false);
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
