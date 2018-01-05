package com.xdk.develop.df.teacherpart.ui.login.sendMessage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xdk.develop.df.teacherpart.MainActivity;
import com.xdk.develop.df.teacherpart.R;
import com.xdk.develop.df.teacherpart.adapter.ClassStudentsAdapter;
import com.xdk.develop.df.teacherpart.adapter.StudentsAdapter;
import com.xdk.develop.df.teacherpart.base.BaseActivity;
import com.xdk.develop.df.teacherpart.data.ClassTeacher;
import com.xdk.develop.df.teacherpart.data.CurrentUser;
import com.xdk.develop.df.teacherpart.data.HttpData;
import com.xdk.develop.df.teacherpart.data.StudentLogin;
import com.xdk.develop.df.teacherpart.data.StudentsSendMessage;
import com.xdk.develop.df.teacherpart.http.HttpHelper;
import com.xdk.develop.df.teacherpart.http.Urls;
import com.xdk.develop.df.teacherpart.http.XDKHttpUtils;
import com.xdk.develop.df.teacherpart.http.wait.DialogCallback;
import com.xdk.develop.df.teacherpart.ui.login.askLeave.StudentsListActivity;
import com.xdk.develop.df.teacherpart.util.SharedPreferenceHelper;
import com.xdk.develop.df.teacherpart.util.ToastUtils;

import org.feezu.liuli.timeselector.TimeSelector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SendMessageActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backll;
    private TextView chooseManTx;
    private EditText zhutiTx;
    private EditText contentTx;
    private TextView userTimeTx;
    private TextView sendTx;
    private List<CurrentUser> userrs = new ArrayList<>();
    private Handler handler = new Handler();
    private String noticeid;
    private List<ClassTeacher> classTeachers = new ArrayList<>();

    @Override
    public void initView() {
        setContentView(R.layout.activity_send_message);
        findView();
    }

    private void findView() {
        chooseManTx = (TextView) findViewById(R.id.send_message_people);
        zhutiTx = (EditText) findViewById(R.id.send_message_them);
        contentTx = (EditText) findViewById(R.id.send_message_content);
        userTimeTx = (TextView) findViewById(R.id.send_message_usertime);
        sendTx = (TextView) findViewById(R.id.send_message_send);
        chooseManTx.setOnClickListener(this);
        sendTx.setOnClickListener(this);
        userTimeTx.setOnClickListener(this);
        backll = (LinearLayout) findViewById(R.id.send_message_back);
        backll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);
        date = calendar.getTime();
        switch (view.getId()) {
            case R.id.send_message_back:
                finish();
                break;
            case R.id.send_message_usertime:
                TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        userTimeTx.setText(time);
                    }
                }, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date));
                timeSelector.show();
                break;
            case R.id.send_message_send:
                if (readyLeave()) {
                    if(classTeachers.size() == 0){
                        HashMap map = new HashMap();
                        map.put("issuer",SharedPreferenceHelper.getLoginUser(this).getName());
                        map.put("issuedate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                        map.put("validdate",userTimeTx.getText().toString());
                        map.put("content",contentTx.getText().toString());
                        map.put("motif",zhutiTx.getText().toString());
                        map.put("users",userrs);
                        XDKHttpUtils.postRequest(this, Urls.sendStudentsMessage(), map, new DialogCallback(this) {
                            @Override
                            public void onSuccess(String respose) {
                                ToastUtils.showShort(SendMessageActivity.this,"发送成功");
                            }
                        });
                    }else{
                        HashMap map = new HashMap();
                        map.put("issuer",SharedPreferenceHelper.getLoginUser(this).getName());
                        map.put("issuedate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                        map.put("validdate",userTimeTx.getText().toString());
                        map.put("content",contentTx.getText().toString());
                        map.put("motif",zhutiTx.getText().toString());
                        map.put("teachers",classTeachers);
                        XDKHttpUtils.postRequest(this, Urls.sendClassMessage(), map, new DialogCallback(this) {
                            @Override
                            public void onSuccess(String respose) {
                                ToastUtils.showShort(SendMessageActivity.this,"发送成功");
                            }
                        });
                    }
                }
                break;
            case R.id.send_message_people:
                String[] a = {"班级通知", "个人通知"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("请选择通知对象")
                        .setItems(a, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 1) {
                                    startActivityForResult(new Intent(SendMessageActivity.this, StudentsListActivity.class), 2);
                                    dialog.dismiss();
                                }
                                if (which == 0) {
                                    startActivityForResult(new Intent(SendMessageActivity.this, ClassMessageActivity.class), 2);
                                    dialog.dismiss();
                                }
                            }
                        });
                builder.show();
                break;
        }
    }
    private boolean readyLeave() {
        if (TextUtils.isEmpty(chooseManTx.getText())) {
            ToastUtils.showShort(this, "请选择通知对象");
            return false;
        }
        if (TextUtils.isEmpty(zhutiTx.getText())) {
            ToastUtils.showShort(this, "请填写主题");
            return false;
        }
        if (TextUtils.isEmpty(contentTx.getText())) {
            ToastUtils.showShort(this, "请填写内容");
            return false;
        }
        if (TextUtils.isEmpty(userTimeTx.getText())) {
            ToastUtils.showShort(this, "请选择有效期");
            return false;
        }
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == StudentsListActivity.RESULT) {
            chooseManTx.setText("");
            if (userrs != null) {
                userrs.clear();
            }
            classTeachers.clear();
            userrs = (List<CurrentUser>) data.getSerializableExtra(StudentsListActivity.INTENTDATA);
            if (userrs.size() == 0) {
                return;
            }
            StringBuilder builder = new StringBuilder();
            boolean a = false;
            for (int i = 0; i < userrs.size(); i++) {
                if (a) {
                    builder.append("," + userrs.get(i).getName());
                } else {
                    builder.append(userrs.get(i).getName());
                }
                if (!a) {
                    a = true;
                }
            }
            chooseManTx.setText(builder.toString());
            StudentsAdapter.getCheckedUser().clear();
        } else if (requestCode == 2 && resultCode == 1) {
            chooseManTx.setText("");
            userrs.clear();
            classTeachers.clear();
            classTeachers = (List<ClassTeacher>) data.getSerializableExtra(ClassMessageActivity.CLASSMESSAGE);
            StringBuilder builder = new StringBuilder();
            boolean a = false;
            for (int i = 0; i < classTeachers.size(); i++) {
                if (a) {
                    builder.append("," + classTeachers.get(i).getGrade() + "级" + classTeachers.get(i).getsClass() + "班");
                } else {
                    builder.append(classTeachers.get(i).getGrade() + "级" + classTeachers.get(i).getsClass() + "班");
                }
                if (!a) {
                    a = true;
                }
            }
            chooseManTx.setText(builder.toString());
            ClassStudentsAdapter.getCheckedUser().clear();
        }
    }
}
