package com.example.user.toy.home.util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Util;
import com.example.user.toy.R;
import com.example.user.toy.home.entity.HomeListItemBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author 于旭东
 */
public class RecyclerViewGridAdapter extends RecyclerView.Adapter <RecyclerViewGridAdapter.GridViewHolder> {

    private static Context mContext;
    private List <HomeListItemBean> dateBean;
    private GsonBuilder builder;
    private Gson gson;
    private String jsonString;

    public RecyclerViewGridAdapter( Context context , List <HomeListItemBean> datesBean ) {
        mContext = context;
        this.dateBean = datesBean;
    }

    public void load(List <HomeListItemBean> dateBeans){
        this.dateBean = dateBeans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder( @NonNull ViewGroup viewGroup , int i ) {

        //转换一个ViewHolder对象，决定了item的样式，参数1.上下文 2.XML布局资源 3.null
        View itemView = View.inflate (mContext , R.layout.grid_item , null);
        //创建一个ViewHolder对象
        //把ViewHolder传出去
        return new GridViewHolder (itemView);

    }



    /**
     * 绑定gridViewHolder
     * 内含item点击事件
     *
     * @param gridViewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder( @NonNull final GridViewHolder gridViewHolder , final int i ) {
        //从集合里拿对应的item的数据对象
        HomeListItemBean listBean = dateBean.get (i);
        //给Holder里面的控件对象设置数据
        //gridViewHolder.setData (listBean);

        gridViewHolder.itemView.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick( View v ) {
                Log.e ("点击位置" , "你点的第" + i + "个。");
                //Intent intent = new Intent (mContext , ToyListItemDetail.class);
                builder = new GsonBuilder ( );
                gson = builder.create ( );
                jsonString = gson.toJson (dateBean.get (i));
                //intent.putExtra ("玩具数据" , jsonString);
               // mContext.startActivity (intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if (dateBean != null && dateBean.size ( ) > 0) {
            return dateBean.size ( );
        }
        return 0;
    }

    public static class GridViewHolder extends RecyclerView.ViewHolder {
        /**
         * 1.显示图片
         * 2.简介
         * 3.用户名
         * 4.点赞数
         */
        private ImageView showImage;
        private TextView myName;
        private TextView myUser;
        private TextView myLikeNum;

        public GridViewHolder( @NonNull View itemView ) {
            super (itemView);
            showImage = itemView.findViewById (R.id.item_list_icon);
            myName = itemView.findViewById (R.id.item_list_name);
            myUser = itemView.findViewById (R.id.item_list_user);
            myLikeNum = itemView.findViewById (R.id.item_list_num);
        }

        /*public void setData( HomeListItemBean data ) {
            RequestOptions requestOptions = new RequestOptions ().bitmapTransform(new CropCircleTransformation());
            //RequestOptions requestOptions = new RequestOptions().centerCrop();
            if (Util.isOnMainThread()) {
                //Glide.with(context).load(path).into(imageView);
                Glide.with (mContext)
                        .load (data.showImg)
                        .apply (requestOptions)
                        .into (showImage);
                myName.setText (data.name);
                myUser.setText (data.user);
                myLikeNum.setText (data.likeNum);
            }

        }*/
    }

    public void add( List <HomeListItemBean> addMessageList ) {
        //增加数据
        int position = dateBean.size ( );
        dateBean.addAll (position , addMessageList);
        notifyItemInserted (position);
    }

    public void refresh( List <HomeListItemBean> newList ) {
        //刷新数据
        dateBean.removeAll (dateBean);
        dateBean.addAll (newList);
        notifyDataSetChanged ( );
    }

    /**
     * 搜索结果展示方法
     */
    public void showFoodSearchResult( ArrayList <HomeListItemBean> homeListItemBeans ) {
        dateBean = new ArrayList <> ( );
        dateBean.addAll (homeListItemBeans);
        notifyDataSetChanged ( );
    }

}
