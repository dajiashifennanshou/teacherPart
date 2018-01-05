package com.xdk.develop.df.teacherpart.http;

import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.xdk.develop.df.teacherpart.http.wait.DialogCallback;
import com.xdk.develop.df.teacherpart.http.wait.WithoutDialogCallback;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/10/24.
 */
public class XDKHttpUtils {
    public static void postRequest(Context context, String url, HashMap hashMap, DialogCallback callback){
        OkGo.post(url).tag(context).upJson(new Gson().toJson(hashMap)).execute(callback);
    }
    public static void postRequest(Context context, String url, HashMap hashMap, DialogCallback callback, HttpHeaders headers){
        OkGo.post(url).headers(headers).tag(context).upJson(new Gson().toJson(hashMap)).execute(callback);
    }
    public static void postRequestWithoutDialog(Context context, String url, HashMap hashMap, WithoutDialogCallback callback){
        OkGo.post(url).tag(context).upJson(new Gson().toJson(hashMap)).execute(callback);
    }
}
