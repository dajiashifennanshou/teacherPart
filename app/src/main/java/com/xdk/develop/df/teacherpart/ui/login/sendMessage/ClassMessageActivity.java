package com.xdk.develop.df.teacherpart.ui.login.sendMessage;

import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.google.gson.reflect.TypeToken;
import com.xdk.develop.df.teacherpart.R;
import com.xdk.develop.df.teacherpart.adapter.ClassStudentsAdapter;
import com.xdk.develop.df.teacherpart.base.BaseActivity;
import com.xdk.develop.df.teacherpart.data.ClassTeacher;
import com.xdk.develop.df.teacherpart.http.Urls;
import com.xdk.develop.df.teacherpart.http.XDKHttpUtils;
import com.xdk.develop.df.teacherpart.http.wait.WithoutDialogCallback;
import com.xdk.develop.df.teacherpart.util.GsonUtil;
import com.xdk.develop.df.teacherpart.util.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClassMessageActivity extends BaseActivity implements View.OnClickListener {
    private TextView exitTx;
    private TextView okTx;
    private TextView nothingTx;
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    private ListView listView;
    private List<ClassTeacher> classes = new ArrayList<>();
    private Handler handler = new Handler();
    private ClassStudentsAdapter adapter;
    public static final String CLASSMESSAGE="classmessage";

    @Override
    public void initView() {
        setContentView(R.layout.activity_class_message);
        findView();
        initData();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            ClassStudentsAdapter.getCheckedUser().clear();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void initData() {
        classes.clear();
        ptrClassicFrameLayout.setLoadMoreEnable(false);
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                XDKHttpUtils.postRequestWithoutDialog(ClassMessageActivity.this, Urls.teacherClass(), null, new WithoutDialogCallback(ClassMessageActivity.this) {
                    @Override
                    public void onSuccess(String respose) {
                        classes.clear();
                        ArrayList<ClassTeacher>  users  = new ArrayList();
                        users   = (ArrayList<ClassTeacher>) GsonUtil.fromJson(respose,new TypeToken<ArrayList<ClassTeacher>>(){}.getType());
                        if(users.size() == 0){
                            nothingTx.setVisibility(View.VISIBLE);
                            ptrClassicFrameLayout.setVisibility(View.GONE);
                            return;
                        }
                        if(ptrClassicFrameLayout.getVisibility() == View.GONE){
                            nothingTx.setVisibility(View.GONE);
                            ptrClassicFrameLayout.setVisibility(View.VISIBLE);
                        }
                        classes.addAll(users);
                        if(adapter == null){
                            adapter = new ClassStudentsAdapter(classes,ClassMessageActivity.this);
                            listView.setAdapter(adapter);
                        }else{
                            adapter.notifyDataSetChanged();
                        }
                        ptrClassicFrameLayout.refreshComplete();
                    }
                });
            }
        });
        ptrClassicFrameLayout.autoRefresh(true);
    }

    private void findView() {
        exitTx = (TextView) findViewById(R.id.class_exit);
        okTx = (TextView) findViewById(R.id.class_comfirm);
        nothingTx = (TextView) findViewById(R.id.class_noting);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.class_refresh);
        listView = (ListView) findViewById(R.id.class_list);
        exitTx.setOnClickListener(this);
        okTx.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.class_comfirm:
                Intent inten  = new Intent();
                inten.putExtra(CLASSMESSAGE, (Serializable) ClassStudentsAdapter.getCheckedUser());
                setResult(1,inten);
                finish();
                break;
            case R.id.class_exit:
                ClassStudentsAdapter.getCheckedUser().clear();
                finish();
                break;
        }
    }
}
