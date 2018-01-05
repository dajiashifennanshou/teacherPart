package com.xdk.develop.df.teacherpart.ui.login.askLeave;

import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.google.gson.reflect.TypeToken;
import com.xdk.develop.df.teacherpart.R;
import com.xdk.develop.df.teacherpart.adapter.StudentsAdapter;
import com.xdk.develop.df.teacherpart.base.BaseActivity;
import com.xdk.develop.df.teacherpart.data.CurrentUser;
import com.xdk.develop.df.teacherpart.http.Urls;
import com.xdk.develop.df.teacherpart.http.XDKHttpUtils;
import com.xdk.develop.df.teacherpart.http.wait.WithoutDialogCallback;
import com.xdk.develop.df.teacherpart.util.GsonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class StudentsListActivity extends BaseActivity implements View.OnClickListener {
    PtrClassicFrameLayout ptrClassicFrameLayout;
    private int currentPage = 0;
    private int pageLength = 7;
    private StudentsAdapter adapter;
    private EditText inputEx;
    private TextView notingTx;
    private TextView exitTx;
    private TextView comfirmTx;
    private ArrayList<CurrentUser> currentUsers = new ArrayList<>();
    private ListView listView;
    private Handler handler = new Handler();
    public static final int RESULT = 2;
    public static final String INTENTDATA = "StudentsListActivitydata";

    @Override
    public void initView() {
        setContentView(R.layout.activity_students_list);
        findView();
        initData();
    }

    private void initData() {
        ptrClassicFrameLayout.setLoadMoreEnable(true);
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                currentUsers.clear();
                currentPage = 0;
                if (!ptrClassicFrameLayout.isLoadMoreEnable()) {
                    ptrClassicFrameLayout.setLoadMoreEnable(true);
                    ptrClassicFrameLayout.loadMoreComplete(true);
                }
                if (TextUtils.isEmpty(inputEx.getText())) {
                    HashMap map = new HashMap();
                    map.put("currentPage", currentPage);
                    map.put("pageLenth",pageLength);
                    XDKHttpUtils.postRequestWithoutDialog(StudentsListActivity.this, Urls.studentsList(), map, new WithoutDialogCallback(StudentsListActivity.this) {
                        @Override
                        public void onSuccess(String respose) {
                            ArrayList  users  = new ArrayList();
                            users = (ArrayList<CurrentUser>) GsonUtil.fromJson(respose,new TypeToken<ArrayList<CurrentUser>>(){}.getType());
                            if(users.size() == 0){
                                notingTx.setVisibility(View.VISIBLE);
                                ptrClassicFrameLayout.setVisibility(View.GONE);
                                return;
                            }
                            if(ptrClassicFrameLayout.getVisibility() == View.GONE){
                                notingTx.setVisibility(View.GONE);
                                ptrClassicFrameLayout.setVisibility(View.VISIBLE);
                            }
                            currentUsers.addAll(users);
                            if(adapter == null){
                                adapter = new StudentsAdapter(currentUsers,StudentsListActivity.this);
                                listView.setAdapter(adapter);
                            }else{
                                adapter.notifyDataSetChanged();
                            }
                            currentPage ++;
                            ptrClassicFrameLayout.refreshComplete();
                            Log.e("users.account",currentUsers.get(1).getAccountid());
                        }
                    });
                } else {
                    HashMap map = new HashMap();
                    map.put("currentPage", currentPage);
                    map.put("pageLenth",pageLength);
                    map.put("like",inputEx.getText().toString());
                    XDKHttpUtils.postRequestWithoutDialog(StudentsListActivity.this, Urls.studentsListLike(), map, new WithoutDialogCallback(StudentsListActivity.this) {
                        @Override
                        public void onSuccess(String respose) {
                            ArrayList  users  = new ArrayList();
                            users = (ArrayList<CurrentUser>) GsonUtil.fromJson(respose,new TypeToken<ArrayList<CurrentUser>>(){}.getType());
                            if(users.size() == 0){
                                notingTx.setVisibility(View.VISIBLE);
                                ptrClassicFrameLayout.setVisibility(View.GONE);
                                return;
                            }
                            if(ptrClassicFrameLayout.getVisibility() == View.GONE){
                                notingTx.setVisibility(View.GONE);
                                ptrClassicFrameLayout.setVisibility(View.VISIBLE);
                            }
                            currentUsers.addAll(users);
                            if(adapter == null){
                                adapter = new StudentsAdapter(currentUsers,StudentsListActivity.this);
                                listView.setAdapter(adapter);
                            }else{
                                adapter.notifyDataSetChanged();
                            }
                            currentPage ++;
                            ptrClassicFrameLayout.refreshComplete();
                        }
                    });
                }
            }
        });
        ptrClassicFrameLayout.autoRefresh(true);

        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                if (TextUtils.isEmpty(inputEx.getText())) {
                    HashMap map = new HashMap();
                    map.put("currentPage", currentPage);
                    map.put("pageLenth",pageLength);
                    XDKHttpUtils.postRequestWithoutDialog(StudentsListActivity.this, Urls.studentsList(), map, new WithoutDialogCallback(StudentsListActivity.this) {
                        @Override
                        public void onSuccess(String respose) {
                            ArrayList  users  = new ArrayList();
                            users   = (ArrayList<CurrentUser>) GsonUtil.fromJson(respose,new TypeToken<ArrayList<CurrentUser>>(){}.getType());
                            if (users.size() == 0) {
                                ptrClassicFrameLayout.loadMoreComplete(false);
                            } else {
                                currentUsers.addAll(users);
                                adapter.notifyDataSetChanged();
                                currentPage++;
                                ptrClassicFrameLayout.loadMoreComplete(true);
                            }
                        }
                    });
                } else {
                    HashMap map = new HashMap();
                    map.put("currentPage", currentPage);
                    map.put("pageLenth",pageLength);
                    map.put("like",inputEx.getText().toString());
                    XDKHttpUtils.postRequestWithoutDialog(StudentsListActivity.this, Urls.studentsListLike(), map, new WithoutDialogCallback(StudentsListActivity.this) {
                        @Override
                        public void onSuccess(String respose) {
                            ArrayList  users  = new ArrayList();
                            users   = (ArrayList<CurrentUser>) GsonUtil.fromJson(respose,new TypeToken<ArrayList<CurrentUser>>(){}.getType());
                            if (users.size() == 0) {
                                ptrClassicFrameLayout.loadMoreComplete(false);
                            } else {
                                currentUsers.addAll(users);
                                adapter.notifyDataSetChanged();
                                currentPage++;
                                ptrClassicFrameLayout.loadMoreComplete(true);
                            }
                        }
                    });
                }
            }
        });

        inputEx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentPage = 0;
                currentUsers.clear();
                StudentsAdapter.getCheckedUser().clear();
                HashMap map = new HashMap();
                map.put("currentPage", currentPage);
                map.put("pageLenth",pageLength);
                map.put("like",s.toString());
                XDKHttpUtils.postRequestWithoutDialog(StudentsListActivity.this, Urls.studentsListLike(), map, new WithoutDialogCallback(StudentsListActivity.this) {
                    @Override
                    public void onSuccess(String respose) {
                        ArrayList  users  = new ArrayList();
                        users = (ArrayList<CurrentUser>) GsonUtil.fromJson(respose,new TypeToken<ArrayList<CurrentUser>>(){}.getType());
                        if(users.size() == 0){
                            notingTx.setVisibility(View.VISIBLE);
                            ptrClassicFrameLayout.setVisibility(View.GONE);
                            return;
                        }
                        if(ptrClassicFrameLayout.getVisibility() == View.GONE){
                            notingTx.setVisibility(View.GONE);
                            ptrClassicFrameLayout.setVisibility(View.VISIBLE);
                        }
                        currentUsers.addAll(users);
                        if(adapter == null){
                            adapter = new StudentsAdapter(currentUsers,StudentsListActivity.this);
                            listView.setAdapter(adapter);
                        }else{
                            adapter.notifyDataSetChanged();
                        }
                        currentPage ++;
                        ptrClassicFrameLayout.refreshComplete();
                    }
                });
            }
        });
    }

    private void findView() {
        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.students_refresh);
        listView = (ListView) findViewById(R.id.students_list);
        exitTx = (TextView) findViewById(R.id.students_exit);
        inputEx = (EditText) findViewById(R.id.students_input_ex);
        notingTx = (TextView) findViewById(R.id.students_noting);
        comfirmTx = (TextView) findViewById(R.id.students_comfirm);
        exitTx.setOnClickListener(this);
        comfirmTx.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.students_exit:
                StudentsAdapter.getCheckedUser().clear();
                finish();
                break;
            case R.id.students_comfirm:
                Intent intent = new Intent();
                intent.putExtra(INTENTDATA, (Serializable) StudentsAdapter.getCheckedUser());
                setResult(RESULT, intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            StudentsAdapter.getCheckedUser().clear();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
