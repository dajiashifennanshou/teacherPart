package com.xdk.develop.df.teacherpart.ui.login;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.xdk.develop.df.teacherpart.MainActivity;
import com.xdk.develop.df.teacherpart.R;
import com.xdk.develop.df.teacherpart.base.BaseActivity;
import com.xdk.develop.df.teacherpart.data.StudentLogin;
import com.xdk.develop.df.teacherpart.http.Urls;
import com.xdk.develop.df.teacherpart.http.XDKHttpUtils;
import com.xdk.develop.df.teacherpart.http.wait.DialogCallback;
import com.xdk.develop.df.teacherpart.util.ExitAppHelper;
import com.xdk.develop.df.teacherpart.util.ImportCsvValidate;
import com.xdk.develop.df.teacherpart.util.SharedPreferenceHelper;
import com.xdk.develop.df.teacherpart.util.ToastUtils;
import com.xdk.develop.df.teacherpart.widget.AuthCode;

import java.util.HashMap;

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private EditText schoolCodeEdit;
    private EditText studentCodeEdit;
    private EditText passwordEdit;
    private ImageView loginBt;
    private RelativeLayout relativeLayout;
    private ExitAppHelper appExitHelper;
    public static final String LStudent = "studentLogin";
    private TextView sendCode;
    private AuthCode myCount;

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
        OkGo.getInstance().getCommonHeaders().clear();
        sendCode = (TextView) findViewById(R.id.login_send_code);
        relativeLayout = (RelativeLayout) findViewById(R.id.login_code_layout);
        loginBt = (ImageView) findViewById(R.id.login_button_login);
        schoolCodeEdit = (EditText) findViewById(R.id.login_school_number);
        passwordEdit = (EditText) findViewById(R.id.login_user_password);
        studentCodeEdit = (EditText) findViewById(R.id.login_student_number);
        loginBt.setOnClickListener(this);
        sendCode.setOnClickListener(this);
        appExitHelper=new ExitAppHelper(this);
        myCount = new AuthCode(60 * 1000, 1000, this, sendCode);
        schoolCodeEdit.setText(SharedPreferenceHelper.getLoginSchoolCode(this));
//        studentCodeEdit.setText(SharedPreferenceHelper.getLoginStudentCode(this));
        if(SharedPreferenceHelper.gettPhoneValidate(LoginActivity.this)){
            relativeLayout.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(appExitHelper.onClickBack(keyCode, event)) return true;
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_button_login:
                if(TextUtils.isEmpty(schoolCodeEdit.getText())){
                    toastShort(R.string.str_login_no_school_code);
                    return;
                }
                if(TextUtils.isEmpty(passwordEdit.getText())){
                    toastShort(R.string.str_login_no_password);
                    return;
                }
                if(!ImportCsvValidate.isMobile(schoolCodeEdit.getText().toString())){
                    toastShort(R.string.str_login_wrong_phone);
                    return;
                }
                if(!SharedPreferenceHelper.gettPhoneValidate(LoginActivity.this)){
                    if(TextUtils.isEmpty(studentCodeEdit.getText())){
                        toastShort(R.string.str_login_no_user_name);
                        return;
                    }
                    if(!schoolCodeEdit.getText().toString().equals( SharedPreferenceHelper.getPhoneNumber(LoginActivity.this))){
                        toastShort("登录手机号与发送验证码手机号不一致");
                        return;
                    }
                }
                HashMap map = new HashMap();
                map.put("phoneNumber",schoolCodeEdit.getText().toString());
                map.put("code", studentCodeEdit.getText().toString());
                map.put("passWord", passwordEdit.getText().toString());
                HttpHeaders headers = new HttpHeaders();
                headers.put("token", null);
                if(!SharedPreferenceHelper.gettPhoneValidate(LoginActivity.this)){
                    XDKHttpUtils.postRequest(this, Urls.loginUrl(), map, new DialogCallback(this) {
                        @Override
                        public void onSuccess(String respose) {
                            StudentLogin login  =  new Gson().fromJson(respose, StudentLogin.class);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(LStudent,login);
                            startActivity(intent);
                            SharedPreferenceHelper.putLoginUser(LoginActivity.this,login);
                            SharedPreferenceHelper.putPhoneValidate(LoginActivity.this,true);
                            SharedPreferenceHelper.putLoginTime(LoginActivity.this, System.currentTimeMillis());
                            SharedPreferenceHelper.putLoginSchoolCode(LoginActivity.this,schoolCodeEdit.getText().toString());
//                            SharedPreferenceHelper.putLoginStudentCode(LoginActivity.this,studentCodeEdit.getText().toString());
                            finish();
                        }
                    },headers);
                }else {
                    XDKHttpUtils.postRequest(this, Urls.loginUrlNoCode(), map, new DialogCallback(this) {
                        @Override
                        public void onSuccess(String respose) {
                            StudentLogin login  =  new Gson().fromJson(respose, StudentLogin.class);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(LStudent,login);
                            startActivity(intent);
                            SharedPreferenceHelper.putLoginUser(LoginActivity.this,login);
                            SharedPreferenceHelper.putLoginTime(LoginActivity.this, System.currentTimeMillis());
                            SharedPreferenceHelper.putLoginSchoolCode(LoginActivity.this,schoolCodeEdit.getText().toString());
                            SharedPreferenceHelper.putLoginStudentCode(LoginActivity.this,studentCodeEdit.getText().toString());
                            finish();
                        }
                    },headers);
                }
                break;
            case R.id.login_send_code:
                if(TextUtils.isEmpty(schoolCodeEdit.getText())){
                    toastShort(R.string.str_login_no_school_code);
                    return;
                }
                if(!ImportCsvValidate.isMobile(schoolCodeEdit.getText().toString())){
                    toastShort(R.string.str_login_wrong_phone);
                    return;
                }
                SharedPreferenceHelper.putPhoneNumber(this,schoolCodeEdit.getText().toString());
                HashMap map1 = new HashMap();
                map1.put("phoneNumber",schoolCodeEdit.getText().toString());
                XDKHttpUtils.postRequest(this, Urls.sendCode(), map1, new DialogCallback(this) {

                    @Override
                    public void onSuccess(String respose) {
                        myCount.start();
                        ToastUtils.showShort(LoginActivity.this,"发送成功");
                    }
                });
                break;
        }
    }
}
