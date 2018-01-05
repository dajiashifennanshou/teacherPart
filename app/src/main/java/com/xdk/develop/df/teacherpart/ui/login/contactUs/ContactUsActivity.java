package com.xdk.develop.df.teacherpart.ui.login.contactUs;

import android.view.View;
import android.widget.LinearLayout;

import com.xdk.develop.df.teacherpart.R;
import com.xdk.develop.df.teacherpart.base.BaseActivity;


public class ContactUsActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backll;

    @Override
    public void initView() {
        setContentView(R.layout.activity_contact_us_avtivity);
        findView();
        backll.setOnClickListener(this);
    }

    private void findView() {
        backll = (LinearLayout) findViewById(R.id.contact_us_back);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contact_us_back:
                finish();
                break;
        }
    }
}
