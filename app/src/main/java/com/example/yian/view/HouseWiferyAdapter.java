package com.example.yian.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yian.R;

import java.util.List;

public class HouseWiferyAdapter extends ArrayAdapter<HouseWifery> {
    private int resourceId;
    public HouseWiferyAdapter(Context context, int textViewResourceId, List<HouseWifery> objests){
        super(context,textViewResourceId,objests);
        resourceId=textViewResourceId;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HouseWifery houseWifery=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView title=(TextView)view.findViewById(R.id.house_wifery_title);
        title.setText(houseWifery.getTitle());
        TextView tel=(TextView)view.findViewById(R.id.house_wifery_tel);
        tel.setText(houseWifery.getTel());
        TextView address=(TextView)view.findViewById(R.id.house_wifery_address);
        address.setText(houseWifery.getAddress());
        TextView likenum=(TextView)view.findViewById(R.id.house_wifery_likenum);
        likenum.setText(houseWifery.getLikenum());
        ImageView imageView=(ImageView)view.findViewById(R.id.house_wifery_img);
        imageView.setImageResource(houseWifery.getImageId());
        return view;
    }
}
