package com.example.yian.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yian.R;
import com.example.yian.view.MallAdapter;
import com.example.yian.view.MallInformation;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件描述
 *
 * @author Even.P
 * @since 2018/5/13 14:55
 */
public class MallFragment extends Fragment {

    private List<MallInformation> reccomendData = new ArrayList<>();
    private List<MallInformation> userData = new ArrayList<>();
    private List<MallInformation> goodsData = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mall, container, false);
        initMallData();
        RecyclerView recommendRecyclerView=(RecyclerView)view.findViewById(R.id.rv_mall_recommend);
        //recommendRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recommendRecyclerView.setLayoutManager(linearLayoutManager);
        MallAdapter mallRecommendAdapter=new MallAdapter();
        mallRecommendAdapter.setNewMallList(reccomendData);
        recommendRecyclerView.setAdapter(mallRecommendAdapter);



        RecyclerView goodsRecyclerView=(RecyclerView)view.findViewById(R.id.rv_mall_goods);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        goodsRecyclerView.setLayoutManager(gridLayoutManager);
        MallAdapter goodsAdapter=new MallAdapter();
        goodsAdapter.setNewMallList(goodsData);
        goodsRecyclerView.setAdapter(goodsAdapter);

        return view;
    }

    private void initMallData() {
        MallInformation recommend1=new MallInformation("清新绿植","尚新",R.drawable.third,1);
        reccomendData.add(recommend1);


        MallInformation goods1=new MallInformation("菩提手串","98",R.drawable.third,3);
        goodsData.add(goods1);
        MallInformation goods2=new MallInformation("菩提手串","98",R.drawable.third,3);
        goodsData.add(goods2);
    }
}
