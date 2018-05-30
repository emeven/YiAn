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

public class SettingAdapter extends ArrayAdapter<Setting>{
    private int resourceId;
    public SettingAdapter(Context context, int textViewResourceId, List<Setting> objests){
        super(context,textViewResourceId,objests);
        resourceId=textViewResourceId;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Setting setting=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView textView=(TextView)view.findViewById(R.id.setting_item_txt);
        textView.setText(setting.getName());
        ImageView imageView=(ImageView)view.findViewById(R.id.setting_item_img);
        imageView.setImageResource(setting.getImageId());
        return view;
    }
}
