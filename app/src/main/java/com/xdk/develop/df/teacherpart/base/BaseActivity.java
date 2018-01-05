package com.xdk.develop.df.teacherpart.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.xdk.develop.df.teacherpart.R;
import com.xdk.develop.df.teacherpart.application.XdkApplication;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/5.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(R.anim.ac_slide_right_in, R.anim.ac_slide_left_out);
        ButterKnife.bind(this);
        XdkApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public abstract void initView();
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void goActivity(Class activity) {
        startActivity(new Intent(this, activity));
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }
    @Override
    public void finish() {
        overridePendingTransition(R.anim.ac_slide_left_in, R.anim.ac_slide_right_out);
        OkGo.getInstance().cancelTag(this);
        super.finish();
    }
    public void toastShort(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
    public void toastShort(int s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
    public void toastLong(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }
    public void toastLong(int s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }
}
