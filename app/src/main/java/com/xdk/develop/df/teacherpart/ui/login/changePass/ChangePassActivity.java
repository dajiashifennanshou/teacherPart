package com.xdk.develop.df.teacherpart.ui.login.changePass;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xdk.develop.df.teacherpart.R;
import com.xdk.develop.df.teacherpart.base.BaseActivity;
import com.xdk.develop.df.teacherpart.http.Urls;
import com.xdk.develop.df.teacherpart.http.XDKHttpUtils;
import com.xdk.develop.df.teacherpart.http.wait.DialogCallback;
import com.xdk.develop.df.teacherpart.ui.login.LoginActivity;
import com.xdk.develop.df.teacherpart.util.SharedPreferenceHelper;

import java.util.HashMap;

public class ChangePassActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backll;
    private Button saveChangeBt;
    private EditText oldPassTx, newPassTx, repeatPassTx;
    @Override
    public void initView() {
        setContentView(R.layout.activity_change_pass);
        findView();
        backll.setOnClickListener(this);
        saveChangeBt.setOnClickListener(this);
    }

    private void findView() {
        saveChangeBt = (Button) findViewById(R.id.change_pass_save_change);
        backll = (LinearLayout) findViewById(R.id.change_pass_back);
        oldPassTx = (EditText) findViewById(R.id.change_pass_oldpass);
        newPassTx = (EditText) findViewById(R.id.change_pass_newpass);
        repeatPassTx = (EditText) findViewById(R.id.change_pass_repeatpass);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_pass_back:
                finish();
                break;
            case R.id.change_pass_save_change:
                if(!isCanSave()){
                    break;
                }
                HashMap map = new HashMap();
                map.put("newPass",newPassTx.getText().toString());
                map.put("oldPass",oldPassTx.getText().toString());
                XDKHttpUtils.postRequest(this, Urls.changePass(), map, new DialogCallback(this) {
                    @Override
                    public void onSuccess(String respose) {
                        toastShort(R.string.str_change_pass_success);
                        SharedPreferenceHelper.putUserToken(ChangePassActivity.this,null);
                        goActivity(LoginActivity.class);
                        finish();
                    }
                });
                break;
        }
    }

    private boolean isCanSave() {
        if (TextUtils.isEmpty(oldPassTx.getText())) {
            toastShort(R.string.str_change_pass_nopass);
            return false;
        }
        if (TextUtils.isEmpty(newPassTx.getText())) {
            toastShort(R.string.str_change_pass_nonewpass);
            return false;
        }
        if (TextUtils.isEmpty(repeatPassTx.getText())) {
            toastShort(R.string.str_change_pass_repeat_newpass);
            return false;
        }
        if (!newPassTx.getText().toString().equals(repeatPassTx.getText().toString())) {
            toastShort(R.string.str_change_pass_repeat_wrong);
            return false;
        }
        return  true;
    }
}
