package com.example.yian.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yian.R;
import com.example.yian.view.BottomBar;

public class HomeActivity extends AppCompatActivity {

    private BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomBar = findViewById(R.id.home_bottom_bar);
        initBottomBar();
    }

    void initBottomBar(){
        bottomBar.setContainer(R.id.fl_container)
                .setTitleBeforeAndAfterColor("#999999", "#6e8bc0")
                .addItem(HomeFragment.class,
                        "首页",
                        R.drawable.ic_home_unselected,
                        R.drawable.ic_home_selected)
                .addItem(MallFragment.class,
                        "商城",
                        R.drawable.ic_mall_unselected,
                        R.drawable.ic_mall_selected)
                .addItem(HousewiferyFragment.class,
                        "家政护工",
                        R.drawable.ic_housewifery_unselected,
                        R.drawable.ic_housewifery_selected)
                .addItem(OnlineAppointmentFragment.class,
                        "送医预约",
                        R.drawable.ic_online_appointment_unselected,
                        R.drawable.ic_online_appointment_selected)
                .addItem(SettingsFragment.class,
                        "设置",
                        R.drawable.ic_settings_unselected,
                        R.drawable.ic_settings_selected)
                .build();
    }
}
