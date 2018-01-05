package com.xdk.develop.df.teacherpart.ui.login.aboutMe;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.xdk.develop.df.teacherpart.R;
import com.xdk.develop.df.teacherpart.base.BaseActivity;
import com.xdk.develop.df.teacherpart.data.CurrentUser;
import com.xdk.develop.df.teacherpart.http.Urls;
import com.xdk.develop.df.teacherpart.http.XDKHttpUtils;
import com.xdk.develop.df.teacherpart.http.wait.DialogCallback;
import com.xdk.develop.df.teacherpart.ui.login.LoginActivity;
import com.xdk.develop.df.teacherpart.util.SharedPreferenceHelper;
import com.xdk.develop.df.teacherpart.widget.CircleImageView;

public class AboutUserActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout backll;
    private TextView name;
    private TextView position;
    private TextView teachWhat;
    private Button outBt;
    private TextView inTime;
    private TextView restmoney;
    private TextView userTime;
    private byte[] studentbm;
    private CurrentUser user;
    private CircleImageView studentPhoto;
    @Override
    public void initView() {
        setContentView(R.layout.activity_about_user);
        XDKHttpUtils.postRequest(this, Urls.userInfo(), null, new DialogCallback(this) {

            @Override
            public void onSuccess(String respose) {
                user = new Gson().fromJson(respose,CurrentUser.class);
                setData();
            }
        });
        findView();
    }
    private void setData() {
        if(user.getName() == null){
            return;
        }
        name.setText(user.getName());
        position.setText(user.getTea_title());
        teachWhat.setText(user.getTea_course());
        inTime.setText(user.getTea_indate());
        restmoney.setText(user.getCardmoney()+"");
        userTime.setText(user.getValiddate());
        studentbm = user.getHeadPortrait();
        studentPhoto.setImageBitmap(BitmapFactory.decodeByteArray(studentbm, 0, studentbm.length));
    }
    private void findView() {
        studentPhoto = (CircleImageView) findViewById(R.id.tearcher_info_img);
        backll = (LinearLayout) findViewById(R.id.about_me_back);
        backll.setOnClickListener(this);
        name = (TextView) findViewById(R.id.student_info_name);
        outBt = (Button) findViewById(R.id.more_user_out);
        outBt.setOnClickListener(this);
        position = (TextView) findViewById(R.id.student_info_code);
        teachWhat = (TextView) findViewById(R.id.student_info_yuanxi);
        inTime = (TextView) findViewById(R.id.student_info_zhuanye);
        restmoney = (TextView) findViewById(R.id.student_info_restMoney);
        userTime = (TextView) findViewById(R.id.student_info_time);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.about_me_back:
                finish();
                break;
            case R.id.more_user_out:
                goActivity(LoginActivity.class);
                toastShort(getString(R.string.str_user_out));
                finish();
                break;
        }
    }
}
