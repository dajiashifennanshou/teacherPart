package com.xdk.develop.df.teacherpart.http.wait;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.request.BaseRequest;
import com.xdk.develop.df.teacherpart.R;
import com.xdk.develop.df.teacherpart.ui.login.LoginActivity;
import com.xdk.develop.df.teacherpart.util.SharedPreferenceHelper;
import com.xdk.develop.df.teacherpart.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/24.
 */
public abstract class DialogCallback extends StringCallback {
    Context context;
    DialogPolicy waitPolicy;

    public abstract void onSuccess(String respose);

    public DialogCallback(Context context) {
        this.context = context;
        waitPolicy = new DialogPolicy(context);
    }

    @Override
    public void onSuccess(String s, Call call, Response response) {
        Log.e("respose===============", s);
        int code = 2;
        String message = null;
        String token = null;
        String jsonrespose = null;
        try {
            JSONObject object = new JSONObject(s);
            code = object.getInt("code");
            Log.e("code+++", object.getInt("code") + "");
            if (code == 0) {
                message = object.getString("message");
                Log.e("message+++", message);
                if (message.equals("token out of time,please login again") || message.equals("invalid token")) {
                    context.startActivity(new Intent(context, LoginActivity.class));
                    ToastUtils.showShort(context, "登录过期，请重新登录");
                } else if (message.equals("send message wrong")) {
                    ToastUtils.showShort(context, R.string.str_login_wrong_school_code);
                } else if (message.equals(("into codeRecord wrong"))) {
                    ToastUtils.showShort(context, R.string.str_login_studentCode_notExist);
                } else if (message.equals("pass wrong")) {
                    ToastUtils.showShort(context, R.string.str_login_password_wrong);
                } else if (message.equals("no permission,using time out")) {
                    ToastUtils.showShort(context, R.string.str_user_no_vip);
                } else if (message.equals("code wrong")) {
                    ToastUtils.showShort(context, "验证码输入错误");
                } else if (message.equals("no this phoneNumber")) {
                    ToastUtils.showShort(context, "请使用绑定的电话号码手机登录");
                } else if (message.equals("oldpass wrong")) {
                    ToastUtils.showShort(context, "原始密码输入错误");
                } else if (message.equals("change fail")) {
                    ToastUtils.showShort(context, "修改密码失败");
                } else if (message.equals("no permission")) {
                    ToastUtils.showShort(context, "对不起，您没有此功能权限");
                } else if (message.equals("code out of time")) {
                    ToastUtils.showShort(context, "验证码过期，请重新发送");
                } else if (message.equals("status wrong")) {
                    ToastUtils.showShort(context, "调皮，请登录学生端");
                } else if(message.equals("phonenumber invalid")){
                    ToastUtils.showShort(context, "请使用绑定的电话号码发送验证码");
                }else {
                    ToastUtils.showShort(context, message);
                }
            } else if (code == 1) {
                if (object.has("token")) {
                    if (object.get("token") != null && !object.get("token").equals("")) {
                        token = object.getString("token");
                        Log.e("token+++", token);
                        if (token != null) {
                            SharedPreferenceHelper.putUserToken(context, token);
                            HttpHeaders headers = new HttpHeaders();
                            headers.put("token", SharedPreferenceHelper.getUserToken(context));
                            OkGo.getInstance().addCommonHeaders(headers);
                        }
                    }
                }
                if (object.has("data")) {
                    if (object.get("data") != null && !object.get("data").equals("")) {
                        jsonrespose = object.get("data").toString();
                    }
                }
                Log.e("jsonrespose+++", s);
                onSuccess(jsonrespose);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        waitPolicy.displayLoading();
    }

    @Override
    public void onCacheSuccess(String s, Call call) {
        super.onCacheSuccess(s, call);

    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        waitPolicy.displayRetry("服务器连接超时，请重试");
    }

    @Override
    public void onCacheError(Call call, Exception e) {
        super.onCacheError(call, e);
        ToastUtils.showShort(context, "读取缓存出错");
    }

    @Override
    public void parseError(Call call, Exception e) {
        super.parseError(call, e);
    }

    @Override
    public void onAfter(@Nullable String s, @Nullable Exception e) {
        super.onAfter(s, e);
        waitPolicy.disappear();
    }

    @Override
    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
        super.upProgress(currentSize, totalSize, progress, networkSpeed);
    }

    @Override
    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
        super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
    }
}
