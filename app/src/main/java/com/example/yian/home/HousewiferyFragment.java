package com.example.yian.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yian.R;
import com.example.yian.test.LocationActivity;
import com.example.yian.test.TestActivity;

/**
 * 文件描述
 *
 * @author Even.P
 * @since 2018/5/13 14:55
 */
public class HousewiferyFragment extends Fragment {

    private Button testButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_housewifery, container, false);
        testButton=(Button)view.findViewById(R.id.testbutton);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), LocationActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
