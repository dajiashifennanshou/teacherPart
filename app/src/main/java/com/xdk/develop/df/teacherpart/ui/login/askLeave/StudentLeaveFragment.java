package com.xdk.develop.df.teacherpart.ui.login.askLeave;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xdk.develop.df.teacherpart.R;
import com.xdk.develop.df.teacherpart.adapter.StudentsAdapter;
import com.xdk.develop.df.teacherpart.base.BaseFragment;
import com.xdk.develop.df.teacherpart.data.CurrentUser;
import com.xdk.develop.df.teacherpart.http.Urls;
import com.xdk.develop.df.teacherpart.http.XDKHttpUtils;
import com.xdk.develop.df.teacherpart.http.wait.DialogCallback;
import com.xdk.develop.df.teacherpart.util.ToastUtils;

import org.feezu.liuli.timeselector.TimeSelector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class StudentLeaveFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private TextView inputTime;
    private TextView inputTimeBack;
    private TextView inputTimName;
    private TextView okTx;
    private EditText reasonEx;
    private RadioGroup checkRadio;
    private RadioButton illBt;
    private RadioButton thingBt;
    private String typeTx;
    private List<CurrentUser> userrs;
    private Handler hander = new Handler();

    @Override
    public void initView() {
        findView();
    }

    private void findView() {
        okTx = (TextView) view.findViewById(R.id.student_leave_ok);
        reasonEx = (EditText) view.findViewById(R.id.student_leave_reason);
        checkRadio = (RadioGroup) view.findViewById(R.id.student_leave_choose);
        illBt = (RadioButton) view.findViewById(R.id.student_leave_ill);
        thingBt = (RadioButton) view.findViewById(R.id.student_leave_thing);
        inputTime = (TextView) view.findViewById(R.id.leave_ask_time);
        inputTimeBack = (TextView) view.findViewById(R.id.leave_ask_time_back);
        inputTimName = (TextView) view.findViewById(R.id.leave_ask_name);
        okTx.setOnClickListener(this);
        inputTime.setOnClickListener(this);
        inputTimeBack.setOnClickListener(this);
        inputTimName.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_leave, null);
        return view;
    }

    @Override
    public void onClick(View view) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);
        date = calendar.getTime();
        switch (view.getId()) {
            case R.id.leave_ask_time:
                TimeSelector timeSelector = new TimeSelector(context, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        inputTime.setText(time);
                    }
                }, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date));
                timeSelector.show();
                break;
            case R.id.leave_ask_time_back:
                TimeSelector timeSelector1 = new TimeSelector(context, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        inputTimeBack.setText(time);
                    }
                }, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date));
                timeSelector1.show();
                break;
            case R.id.leave_ask_name:
                startActivityForResult(new Intent(context, StudentsListActivity.class), 1);
                break;
            case R.id.student_leave_ok:
                if (!readyLeave()) {
                    return;
                }
                if (userrs.size() == 0){
                    return;
                }
                String[] a = inputTime.getText().toString().split(" ");
                String[] b = inputTimeBack.getText().toString().split(" ");
                switch (checkRadio.getCheckedRadioButtonId()) {
                    case R.id.student_leave_ill:
                        typeTx = illBt.getText().toString();
                        break;
                    case R.id.student_leave_thing:
                        typeTx = thingBt.getText().toString();
                        break;
                }
                HashMap map = new HashMap();
                map.put("students",userrs);
                map.put("leavetype",typeTx);
                map.put("leavereason",reasonEx.getText().toString());
                map.put("startdate",a[0]);
                map.put("starttime",a[1]);
                map.put("enddate",b[0]);
                map.put("endtime",b[1]);
                XDKHttpUtils.postRequest(context, Urls.askLeaveStudents(), map, new DialogCallback(context) {
                    @Override
                    public void onSuccess(String respose) {
                        ToastUtils.showShort(context,"请假录入成功");
                    }
                });
                break;
        }
    }

    private boolean readyLeave() {
        if (TextUtils.isEmpty(inputTimName.getText())) {
            ToastUtils.showShort(context, "请选择请假人");
            return false;
        }
        if (TextUtils.isEmpty(reasonEx.getText())) {
            ToastUtils.showShort(context, "请填写请假理由");
            return false;
        }
        if (checkRadio.getCheckedRadioButtonId() == -1) {
            ToastUtils.showShort(context, "请选择请假类型");
            return false;
        }
        if (TextUtils.isEmpty(inputTime.getText())) {
            ToastUtils.showShort(context, "请填写请假时间");
            return false;
        }
        if (TextUtils.isEmpty(inputTimeBack.getText())) {
            ToastUtils.showShort(context, "请填写返回时间");
            return false;
        }
        if (compare_date(inputTime.getText().toString(), inputTimeBack.getText().toString()) != -1) {
            ToastUtils.showShort(context, "请核对请假时间，返回时间");
            return false;
        }
        return true;
    }

    public int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == StudentsListActivity.RESULT) {
            inputTimName.setText("");
            if (userrs != null) {
                userrs.clear();
            }
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
            inputTimName.setText(builder.toString());
            StudentsAdapter.getCheckedUser().clear();
        }
    }
}
