package com.xdk.develop.df.teacherpart.ui.login.outStudent;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.xdk.develop.df.teacherpart.R;
import com.xdk.develop.df.teacherpart.base.BaseActivity;

public class OutStudentActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backll;
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    private ListView listView;


    @Override
    public void initView() {
        setContentView(R.layout.activity_out_student);
        backll = (LinearLayout) findViewById(R.id.out_student_back);
        backll.setOnClickListener(this);
        findView();
        initData();
    }

    private void initData() {
        ptrClassicFrameLayout.setLoadMoreEnable(true);
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrClassicFrameLayout.refreshComplete();
            }
        });
        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                ptrClassicFrameLayout.loadMoreComplete(true);
            }
        });
    }

    private void findView() {
        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.out_students_refresh);
        listView = (ListView) findViewById(R.id.out_students_list);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.out_student_back:
                finish();
                break;
        }
    }
}
