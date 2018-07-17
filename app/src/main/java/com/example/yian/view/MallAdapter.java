package com.example.yian.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yian.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MallInformation> mShopList = new ArrayList<>();
    private static final int GOODS_TYPE = 3;
    private static final int USER_TYPE = 2;
    private static final int RECOMMEND_TYPE = 1;
    private int type=1;

    private String titleName;//保存当前选中商店的商店名
    public double selectPrice = 0;

    @Override
    public int getItemViewType(int position) {
        return mShopList.get(position).getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mall_item_goods, parent, false);
                return new GoodsViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mall_item_user, parent, false);
                return new UserViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mall_item_recommend, parent, false);
                return new RecommendViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        switch (getItemViewType(position)) {
            case 3:
                final GoodsViewHolder goodsViewHolder = (GoodsViewHolder) holder;
                goodsViewHolder.imageView.setImageResource(mShopList.get(position).getImageId());
                goodsViewHolder.goodsName.setText(mShopList.get(position).getName());
                goodsViewHolder.goodsPrice.setText(mShopList.get(position).getPrice());
                break;
            case 2:
                UserViewHolder userViewHolder=(UserViewHolder)holder;
                userViewHolder.imageView.setImageResource(mShopList.get(position).getImageId());
                userViewHolder.userName.setText(mShopList.get(position).getName());
                userViewHolder.address.setText(mShopList.get(position).getPrice());
                break;
            default:
                RecommendViewHolder recommendViewHolder = (RecommendViewHolder) holder;
                recommendViewHolder.imageView.setImageResource(mShopList.get(position).getImageId());
                recommendViewHolder.goodsName.setText(mShopList.get(position).getName());
                recommendViewHolder.shopName.setText(mShopList.get(position).getPrice());
                break;

        }

    }

    public void setNewMallList(List<MallInformation> newMallList) {
        mShopList.clear();
        mShopList.addAll(newMallList);
        notifyDataSetChanged();
    }


    /**
     * 第一种ViewHolder
     */
    public class RecommendViewHolder extends RecyclerView.ViewHolder {
        TextView goodsName,shopName;
        ImageView imageView;
        public RecommendViewHolder(View itemView) {
            super(itemView);
            goodsName=(TextView)itemView.findViewById(R.id.mall_item_recommend_goodsname);
            shopName=(TextView)itemView.findViewById(R.id.mall_item_recommend_shopname);
            imageView=(ImageView)itemView.findViewById(R.id.mall_item_recommend_image);
        }

    }

    /**
     * 第二种ViewHolder
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView userName,address;
        public UserViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.mall_item_user_image);
            userName=(TextView)itemView.findViewById(R.id.mall_item_user_name);
            address=(TextView)itemView.findViewById(R.id.mall_item_user_address);
        }

    }
    /**
     * 第三种ViewHolder
     */
    public class GoodsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView goodsName,goodsPrice;
        public GoodsViewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.mall_item_goods_image);
            goodsName=(TextView)itemView.findViewById(R.id.mall_item_goods_goodsname);
            goodsPrice=(TextView)itemView.findViewById(R.id.mall_item_goods_goodsprice);
        }

    }

    @Override
    public int getItemCount() {
        return mShopList.size();
    }

}
