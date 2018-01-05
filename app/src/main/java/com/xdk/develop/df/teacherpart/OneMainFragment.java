package com.xdk.develop.df.teacherpart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xdk.develop.df.teacherpart.base.BaseFragment;


/**
 * Created by Administrator on 2016/10/27.
 */
public class OneMainFragment extends BaseFragment {
    View view;
    @Override
    public void initView() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       if(view == null){
           view =  inflater.inflate(R.layout.mian_frame_one,null);
       }
        return view;
    }
}
