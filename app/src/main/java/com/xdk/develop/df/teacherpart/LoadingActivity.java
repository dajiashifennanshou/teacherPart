package com.xdk.develop.df.teacherpart;

import android.os.AsyncTask;
import android.os.Handler;


import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.xdk.develop.df.teacherpart.base.BaseActivity;
import com.xdk.develop.df.teacherpart.ui.login.LoginActivity;
import com.xdk.develop.df.teacherpart.util.CurrentUserHelper;
import com.xdk.develop.df.teacherpart.util.SharedPreferenceHelper;
import com.xdk.develop.df.teacherpart.util.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by df on 2016/8/5.
 */
public class LoadingActivity extends BaseActivity {
    private long currentTime;
    private static final long loadingTime = 2000;
    private Handler handler = new Handler();

    @Override
    public void initView() {
        setContentView(R.layout.activity_loading);
        new LoadingTask().execute("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }

    class LoadingTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            currentTime = System.currentTimeMillis();
            if (SharedPreferenceHelper.getUserToken(LoadingActivity.this) == null) {
                return false;
            } else {
                long a = SharedPreferenceHelper.getLoginTime(LoadingActivity.this);
                if (currentTime - SharedPreferenceHelper.getLoginTime(LoadingActivity.this) > 24 * 60 * 60 * 1000) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            toastShort(R.string.str_user_info_overTime);
                        }
                    });
                    return false;
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (System.currentTimeMillis() - currentTime < loadingTime) {
                if (aBoolean) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            goActivity(MainActivity.class);
                            SharedPreferenceHelper.putLoginTime(LoadingActivity.this, System.currentTimeMillis());
                            finish();
                        }
                    }, loadingTime - (System.currentTimeMillis() - currentTime));
                } else {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            goActivity(LoginActivity.class);
                            finish();
                        }
                    }, loadingTime - (System.currentTimeMillis() - currentTime));
                }

            } else {
                if (aBoolean) {
                    goActivity(MainActivity.class);
                    SharedPreferenceHelper.putLoginTime(LoadingActivity.this, System.currentTimeMillis());
                    finish();
                } else {
                    goActivity(LoginActivity.class);
                    finish();
                }
            }
        }
    }
}
