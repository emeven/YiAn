package com.example.yian.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yian.R;

/**
 * 文件描述
 *
 * @author Even.P
 * @since 2018/5/13 14:55
 */
public class OnlineAppointmentFragment extends Fragment implements View.OnClickListener {
    private TextView sunday, monday, tuesday, wednesday, thursday, friday, saturday,appointment;
    private int[] check = {0, 0, 0, 0, 0, 0, 0};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_online_appointment, container, false);
        sunday = (TextView) view.findViewById(R.id.fragment_appointment_sun);
        monday = (TextView) view.findViewById(R.id.fragment_appointment_mon);
        tuesday = (TextView) view.findViewById(R.id.fragment_appointment_tue);
        wednesday = (TextView) view.findViewById(R.id.fragment_appointment_wed);
        thursday = (TextView) view.findViewById(R.id.fragment_appointment_thu);
        friday = (TextView) view.findViewById(R.id.fragment_appointment_fri);
        saturday = (TextView) view.findViewById(R.id.fragment_appointment_sat);
        appointment=(TextView)view.findViewById(R.id.fragment_appointment_appointment);
        sunday.setOnClickListener(this);
        monday.setOnClickListener(this);
        tuesday.setOnClickListener(this);
        wednesday.setOnClickListener(this);
        thursday.setOnClickListener(this);
        friday.setOnClickListener(this);
        saturday.setOnClickListener(this);
        appointment.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_appointment_sun:
                if (check[6] == 0) {
                    check[6] = 1;
                    sunday.setBackgroundColor(Color.parseColor("#c6c8cc"));
                } else {
                    check[6] = 0;
                    sunday.setBackgroundColor(Color.parseColor("#e5e6e8"));
                }
                break;
            case R.id.fragment_appointment_mon:
                if (check[0] == 0) {
                    check[0] = 1;
                    monday.setBackgroundColor(Color.parseColor("#c6c8cc"));
                } else {
                    check[0] = 0;
                    monday.setBackgroundColor(Color.parseColor("#e5e6e8"));
                }
                break;
            case R.id.fragment_appointment_tue:
                if (check[1] == 0) {
                    check[1] = 1;
                    tuesday.setBackgroundColor(Color.parseColor("#c6c8cc"));
                } else {
                    check[1] = 0;
                    tuesday.setBackgroundColor(Color.parseColor("#e5e6e8"));
                }
                break;
            case R.id.fragment_appointment_wed:
                if (check[2] == 0) {
                    check[2] = 1;
                    wednesday.setBackgroundColor(Color.parseColor("#c6c8cc"));
                } else {
                    check[2] = 0;
                    wednesday.setBackgroundColor(Color.parseColor("#e5e6e8"));
                }
                break;
            case R.id.fragment_appointment_thu:
                if (check[3] == 0) {
                    check[3] = 1;
                    thursday.setBackgroundColor(Color.parseColor("#c6c8cc"));
                } else {
                    check[3] = 0;
                    thursday.setBackgroundColor(Color.parseColor("#e5e6e8"));
                }
                break;
            case R.id.fragment_appointment_fri:
                if (check[4] == 0) {
                    check[4] = 1;
                    friday.setBackgroundColor(Color.parseColor("#c6c8cc"));
                } else {
                    check[4] = 0;
                    friday.setBackgroundColor(Color.parseColor("#e5e6e8"));
                }
                break;
            case R.id.fragment_appointment_sat:
                if (check[5] == 0) {
                    check[5] = 1;
                    saturday.setBackgroundColor(Color.parseColor("#c6c8cc"));
                } else {
                    check[5] = 0;
                    saturday.setBackgroundColor(Color.parseColor("#e5e6e8"));
                }
                break;
            case R.id.fragment_appointment_appointment:
                Toast.makeText(getContext(),"预约成功！",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
