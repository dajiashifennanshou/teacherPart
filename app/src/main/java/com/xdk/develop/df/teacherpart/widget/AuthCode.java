package com.xdk.develop.df.teacherpart.widget;


import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.xdk.develop.df.teacherpart.R;


/**
 * 
 * 定时器  获取验证码
 * @author systemyc
 * 
 */
public class AuthCode extends CountDownTimer {
	/**
	 * 发送验证码 的控件是textView
	 */
	private TextView mAuthCode;
	private Context mContext;

	/**
	 * @param millisInFuture  总时长
	 * @param countDownInterval  计时的时间间隔
	 */
	public AuthCode(long millisInFuture, long countDownInterval,
					Context context, TextView sendAuthCode) {
		super(millisInFuture, countDownInterval);
		this.mAuthCode = sendAuthCode;
		this.mContext = context;
	}

	/**
	 * 
	 * onTick(Long m)中的代码是你倒计时开始时要做的事情，参数m是直到完成的时间
	 */
	@Override
	public void onTick(long millisUntilFinished) {
		setAuthCodeClickable(mAuthCode, false, millisUntilFinished);

	}

	/**
	 * onFinish()中的代码是计时器结束的时候要做的事情
	 */
	@Override
	public void onFinish() {
		setAuthCodeClickable(mAuthCode, true, 0);

	}

	/**
	 * 发送验证码 按钮是否可用, 不可用时一定设置一个时间在view上显示, 可用时不需要设置时间
	 * 
	 * @param
	 * @param millisUntilFinished
	 */
	public void setAuthCodeClickable(TextView sendAuthCode, boolean isSend, long millisUntilFinished) {
		if (isSend) {
			// 显示再次发送
			sendAuthCode.setClickable(true);
			sendAuthCode.setText("重新发送");
		} else {
			// 显示还剩多少秒
			sendAuthCode.setClickable(false);
			sendAuthCode.setText(millisUntilFinished / 1000 + "秒后重发");
		}
	}

	/**
	 * 显示再次发送情况
	 */
	public void sendAgain() {
		setAuthCodeClickable(mAuthCode, true, 0);
	}
}
