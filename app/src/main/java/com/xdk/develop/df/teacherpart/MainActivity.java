package com.xdk.develop.df.teacherpart;

import android.annotation.TargetApi;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdk.develop.df.teacherpart.base.BaseActivity;
import com.xdk.develop.df.teacherpart.data.StudentLogin;
import com.xdk.develop.df.teacherpart.ui.login.LoginActivity;
import com.xdk.develop.df.teacherpart.ui.login.aboutMe.AboutUserActivity;
import com.xdk.develop.df.teacherpart.ui.login.askLeave.AskLeaveActivity;
import com.xdk.develop.df.teacherpart.ui.login.askLeave.TeacherAskLeaveActivity;
import com.xdk.develop.df.teacherpart.ui.login.changePass.ChangePassActivity;
import com.xdk.develop.df.teacherpart.ui.login.contactUs.ContactUsActivity;
import com.xdk.develop.df.teacherpart.ui.login.sendMessage.SendMessageActivity;
import com.xdk.develop.df.teacherpart.util.CurrentUserHelper;
import com.xdk.develop.df.teacherpart.util.DataCleanManager;
import com.xdk.develop.df.teacherpart.util.ExitAppHelper;
import com.xdk.develop.df.teacherpart.util.FileUtils;
import com.xdk.develop.df.teacherpart.util.SharedPreferenceHelper;
import com.xdk.develop.df.teacherpart.util.ToastUtils;
import com.xdk.develop.df.teacherpart.widget.CircleImageView;


public class MainActivity extends BaseActivity {
    private ExitAppHelper appExitHelper;
    private ImageView studentPhoto;
    private TextView studentInfo;
    private CircleImageView circleImageView;
    private OneMainFragment oneMainFragment = new OneMainFragment();
    private TwoMainFragment twoMainFragment = new TwoMainFragment();
    private ThreeMainFragment threeMainFragment = new ThreeMainFragment();
    private TextView oneTx;
    private TextView twoTx;
    private StudentLogin studentLogin;
    private TextView threeTx;
    private int currentPage = 1;
    private FrameLayout frameLayout;

    @Override
    public void initView() {
        setContentView(R.layout.activity_main_new);
        findView();
        if(getIntent().getSerializableExtra(LoginActivity.LStudent) == null){
            studentLogin = SharedPreferenceHelper.getLoginUser(this);
        }else{
            studentLogin = (StudentLogin) getIntent().getSerializableExtra(LoginActivity.LStudent);
        }
        studentInfo.setText( studentLogin.getName());
        circleImageView.setImageBitmap(BitmapFactory.decodeByteArray(studentLogin.getHeadPortrait(),0,studentLogin.getHeadPortrait().length));
    }
    private void findView() {
        appExitHelper = new ExitAppHelper(this);
        circleImageView = (CircleImageView) findViewById(R.id.main_img_student_photo);
        studentInfo = (TextView) findViewById(R.id.main_tx_student_info);
        studentPhoto = (ImageView) findViewById(R.id.main_img_student_photo);
        frameLayout = (FrameLayout) findViewById(R.id.main_frame);
        oneTx = (TextView) findViewById(R.id.main_choose_one);
        twoTx = (TextView) findViewById(R.id.main_choose_two);
        threeTx = (TextView) findViewById(R.id.main_choose_three);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new OneMainFragment()).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (appExitHelper.onClickBack(keyCode, event)) return true;
        return super.onKeyDown(keyCode, event);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void choosePageClick(View view){
        switch (view.getId()){
            case R.id.main_choose_one:
                if(currentPage == 1){
                    return;
                }else if(currentPage == 2){
                    twoTx.setTextColor(getResources().getColor(R.color.white));
                    twoTx.setBackground(null);
                }else if(currentPage == 3){
                    threeTx.setTextColor(getResources().getColor(R.color.white));
                    threeTx.setBackground(null);
                }
                oneTx.setBackground(getResources().getDrawable(R.drawable.list_bg));
                oneTx.setTextColor(getResources().getColor(R.color.themeColor));
                currentPage = 1;
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,oneMainFragment).commit();
                break;
            case R.id.main_choose_two:
                if(currentPage == 2){
                    return;
                }else if(currentPage == 1){
                    oneTx.setTextColor(getResources().getColor(R.color.white));
                    oneTx.setBackground(null);
                }else if(currentPage == 3){
                    threeTx.setTextColor(getResources().getColor(R.color.white));
                    threeTx.setBackground(null);
                }
                twoTx.setBackground(getResources().getDrawable(R.drawable.list_bg));
                twoTx.setTextColor(getResources().getColor(R.color.themeColor));
                currentPage = 2;
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,twoMainFragment).commit();
                break;
            case R.id.main_choose_three:
                if(currentPage == 3){
                    return;
                }else if(currentPage == 2){
                    twoTx.setTextColor(getResources().getColor(R.color.white));
                    twoTx.setBackground(null);
                }else if(currentPage == 1){
                    oneTx.setTextColor(getResources().getColor(R.color.white));
                    oneTx.setBackground(null);
                }
                threeTx.setBackground(getResources().getDrawable(R.drawable.list_bg));
                threeTx.setTextColor(getResources().getColor(R.color.themeColor));
                currentPage = 3;
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,threeMainFragment).commit();
                break;
        }
    }
    public void optionClick(View view) {
        switch (view.getId()) {
            case R.id.main_send_message:
                goActivity(SendMessageActivity.class);
                break;
            case R.id.main_ask_leave:
                goActivity(AskLeaveActivity.class);
                break;
            case R.id.main_teacher_leave:
                goActivity(TeacherAskLeaveActivity.class);
                break;
            case R.id.main_user_info:
                goActivity(AboutUserActivity.class);
                break;
            case R.id.main_contact_us:
                goActivity(ContactUsActivity.class);
                break;
            case R.id.main_change_pass:
                goActivity(ChangePassActivity.class);
                break;
            case R.id.main_clean:
                FileUtils fileUtils = new FileUtils(this);
                fileUtils.deleteFile();
                DataCleanManager.cleanExternalCache(this);
                ToastUtils.showShort(this,"已清除缓存！");
                break;
            case R.id.main_check_update:
                ToastUtils.showLong(this,"检查更新中...");
                ToastUtils.showShort(this,"已是最新版本！");
                break;
            case R.id.main_img_student_photo:
                goActivity(AboutUserActivity.class);
                break;
        }
    }
}
