package com.example.yian.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yian.R;
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
public class SettingsFragment extends Fragment {
    private List<Setting> settingList=new ArrayList<>();
    private List<Setting> settingList2=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initSettings();
        SettingAdapter settingAdapter=new SettingAdapter(getContext(),R.layout.setting_item,settingList);
        ListView listView1=(ListView)view.findViewById(R.id.fragment_setting_list1);
        listView1.setAdapter(settingAdapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"正在开发中ψ(｀∇´)ψ",Toast.LENGTH_SHORT).show();
            }
        });
        SettingAdapter settingAdapter2=new SettingAdapter(getContext(),R.layout.setting_item,settingList2);
        ListView listView2=(ListView)view.findViewById(R.id.fragment_setting_list2);
        listView2.setAdapter(settingAdapter2);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"正在开发中ψ(｀∇´)ψ",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void initSettings() {
        Setting language=new Setting("语言",R.drawable.ic_language);
        settingList.add(language);
        Setting basicSettings=new Setting("基本设置",R.drawable.ic_basic_settings);
        settingList.add(basicSettings);
        Setting explain=new Setting("使用说明",R.drawable.ic_explain);
        settingList.add(explain);
        Setting notic=new Setting("通知栏",R.drawable.ic_notice);
        settingList.add(notic);
        Setting information=new Setting("关于我们",R.drawable.ic_information);
        settingList2.add(information);
        Setting update=new Setting("检查更新",R.drawable.ic_update);
        settingList2.add(update);
    }


}
