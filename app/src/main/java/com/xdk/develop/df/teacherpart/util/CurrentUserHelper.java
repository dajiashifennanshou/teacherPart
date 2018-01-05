package com.xdk.develop.df.teacherpart.util;

import android.app.Activity;


import com.xdk.develop.df.teacherpart.data.CurrentUser;
import com.xdk.develop.df.teacherpart.http.HttpHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/8.
 */
public class CurrentUserHelper {
    public static boolean getUserVip(Activity activity) {
        CurrentUser user = SharedPreferenceHelper.getCurrentUser(activity);
        if(user == null){
            return false;
        }
        int vip = HttpHelper.compare_date(user.getValiddate(), new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        if (vip == -1) {
            return false;
        }
        return true;
    }
}
