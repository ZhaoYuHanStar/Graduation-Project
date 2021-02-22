package com.example.user.toy.home.activityAndFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.toy.R;
import com.example.user.toy.home.entity.HomeListItemBean;
import com.example.user.toy.home.entity.Img;
import com.example.user.toy.home.entity.Toy;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ToyListItemDetail extends AppCompatActivity {

    private Gson gson;
    private HomeListItemBean bean;
    private Banner banner;
    private TextView price;
    private TextView produce;
    private TextView age;
    private TextView type;
    private Button pay;
    private int id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toy_detail);
        //隐藏标题栏
        getSupportActionBar().hide();
        init();
        initBanner();
    }

    private void init(){
        Intent intent = this.getIntent();
        String toyStr = intent.getStringExtra("toyBean");
        bean = new Gson().fromJson(toyStr,HomeListItemBean.class);
        id = bean.getId();

        banner = findViewById(R.id.banner_img);
        price = findViewById(R.id.tv_price);
        produce = findViewById(R.id.tv_produce);
        age = findViewById(R.id.tv_age);
        type = findViewById(R.id.tv_type);
        pay = findViewById(R.id.btn_pay);

        price.setText(bean.getPrice());
        produce.setText(bean.getProduce());
        type.setText(bean.getType());
        age.setText(bean.getAge());
        id = bean.getId();
    }

    /**
     * 初始化banner
     */
    private void initBanner() {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new MyLoader())
                //从Presenter中取出图片资源
                .setImages(getBannerImages())
                .setBannerAnimation(Transformer.Default)
                .isAutoPlay(true)
                .setDelayTime(3000)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
    }

    /**
     * 获取Banner的图片资源
     */
    public ArrayList<String> getBannerImages() {
        Set<Img> images1 = bean.getImages();
        ArrayList<String> listPath = new ArrayList<>();
        List<Img> mBannerImages = new ArrayList<com.example.user.toy.home.entity.Img>(images1);//B是set型的
        for (com.example.user.toy.home.entity.Img image : images1){
            //记得写url!!!!
            listPath.add(image.getSrc() );
        }
        return listPath;
    }

    /**
     *  banner图片加载方法
     */
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load((String) path)
                    .into(imageView);
        }
    }
}
