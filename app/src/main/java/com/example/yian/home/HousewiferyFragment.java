package com.example.yian.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yian.R;
import com.example.yian.test.LocationActivity;
import com.example.yian.test.TestActivity;
import com.example.yian.view.HouseWifery;
import com.example.yian.view.HouseWiferyAdapter;
import com.example.yian.view.Setting;
import com.example.yian.view.SettingAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件描述
 *
 * @author Even.P
 * @since 2018/5/13 14:55
 */
public class HousewiferyFragment extends Fragment {
    private List<HouseWifery> houseWiferyList=new ArrayList<>();
    private TextView houseTextView,wiferyTextView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_housewifery, container, false);
        initHouse();

        final HouseWiferyAdapter houseWiferyAdapter=new HouseWiferyAdapter(getContext(),R.layout.house_wifery_item,houseWiferyList);
        final ListView listView1=(ListView)view.findViewById(R.id.house_wifery_list);
        listView1.setDividerHeight(0);
        listView1.setAdapter(houseWiferyAdapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"还在联系中ψ(｀∇´)ψ",Toast.LENGTH_SHORT).show();
            }
        });
        houseTextView=(TextView)view.findViewById(R.id.fragment_house);
        wiferyTextView=(TextView)view.findViewById(R.id.fragment_wifery);
        houseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                houseTextView.setBackgroundColor(getResources().getColor(R.color.DarkGrey));
                wiferyTextView.setBackgroundColor(getResources().getColor(R.color.LightGray));
                houseWiferyList.clear();
                initHouse();
                listView1.setAdapter(houseWiferyAdapter);
            }
        });
        wiferyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                houseTextView.setBackgroundColor(getResources().getColor(R.color.LightGray));
                wiferyTextView.setBackgroundColor(getResources().getColor(R.color.DarkGrey));
                houseWiferyList.clear();
                initWifery();
                listView1.setAdapter(houseWiferyAdapter);
            }
        });

        return view;
    }

    private void initHouse() {
        HouseWifery dianyu=new HouseWifery("典誉高端家政公司","56860023","朝阳东路41号","1.1k",R.drawable.first);
        houseWiferyList.add(dianyu);
        HouseWifery junyi=new HouseWifery("军弈高端家政公司","66530123","北京西路113号","2.3k",R.drawable.second);
        houseWiferyList.add(junyi);
        HouseWifery pingjia=new HouseWifery("品嘉高端家政公司","33694399","西藏北路156号","0.3k",R.drawable.third);
        houseWiferyList.add(pingjia);
        HouseWifery chengyi=new HouseWifery("橙意高端家政公司","33691888","中山路288号","10k",R.drawable.forth);
        houseWiferyList.add(chengyi);
        HouseWifery xinxin=new HouseWifery("心兴高端家政公司","85864399","四川北路156号","5.3k",R.drawable.fifth);
        houseWiferyList.add(xinxin);
    }
    private void initWifery() {
        HouseWifery dianyu=new HouseWifery("岳女士","18384657893","中山路308号","0.18k",R.drawable.first_wifery);
        houseWiferyList.add(dianyu);
        HouseWifery junyi=new HouseWifery("林女士","13545620158","西藏北路134号","0.12k",R.drawable.second_wifery);
        houseWiferyList.add(junyi);
        HouseWifery pingjia=new HouseWifery("张女士","15845632587","北京西路123号","0.21k",R.drawable.third_wifery);
        houseWiferyList.add(pingjia);
        HouseWifery chengyi=new HouseWifery("陈女士","18935214762","朝阳东路68号","1.1k",R.drawable.forth_wifery);
        houseWiferyList.add(chengyi);
        HouseWifery xinxin=new HouseWifery("高女士","13248695231","四川北路189号","0.24k",R.drawable.fifth_wifery);
        houseWiferyList.add(xinxin);
    }
}
