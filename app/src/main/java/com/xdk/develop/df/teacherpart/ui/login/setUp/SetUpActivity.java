package com.xdk.develop.df.teacherpart.ui.login.setUp;

import android.view.View;
import android.widget.LinearLayout;

import com.xdk.develop.df.teacherpart.R;
import com.xdk.develop.df.teacherpart.base.BaseActivity;
import com.xdk.develop.df.teacherpart.ui.login.LoginActivity;
import com.xdk.develop.df.teacherpart.ui.login.changePass.ChangePassActivity;
import com.xdk.develop.df.teacherpart.ui.login.contactUs.ContactUsActivity;
import com.xdk.develop.df.teacherpart.util.DataCleanManager;
import com.xdk.develop.df.teacherpart.util.FileUtils;
import com.xdk.develop.df.teacherpart.util.SharedPreferenceHelper;
import com.xdk.develop.df.teacherpart.util.ToastUtils;

public class SetUpActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backll;
    @Override
    public void initView() {
        setContentView(R.layout.activity_set_up);
        backll = (LinearLayout) findViewById(R.id.set_up_back);
        backll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set_up_back:
                finish();
                break;
        }
    }
    public void setUpClick(View view){
        switch (view.getId()){
            case R.id.more_contact_us:
                goActivity(ContactUsActivity.class);
                break;
            case R.id.more_change_pass:
                goActivity(ChangePassActivity.class);
                break;
            case R.id.more_change_clear_cache:
                FileUtils fileUtils = new FileUtils(this);
                fileUtils.deleteFile();
                DataCleanManager.cleanExternalCache(this);
                ToastUtils.showShort(this,"已清除缓存！");
                break;
            case R.id.more_change_update:
                ToastUtils.showShort(this,"已是最新版本！");
                break;
            case R.id.more_user_out:
                SharedPreferenceHelper.putCurrentUser(this, null);
                goActivity(LoginActivity.class);
                toastShort(getString(R.string.str_user_out));
                finish();
                break;}
    }
}
