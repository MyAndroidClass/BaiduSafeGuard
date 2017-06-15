package com.zzptc.sky.dbphoneguard.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.zzptc.sky.dbphoneguard.R;
import com.zzptc.sky.dbphoneguard.activities.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

//给欢迎界面一个渐变：alpha => 1.0 -> 0.0  2000
public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.activity_welcome)
    RelativeLayout activityWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {
        //给布局添加渐变动画 ：属性动画
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(activityWelcome,"alpha",1.0f, 0.0f);
        alphaAnimator.setDuration(2000);
        alphaAnimator.start();

        //动画结束 之后，跳转到主界面
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                //activity跳转动画 overriding
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim); // startActivity()/finish()后面，必须紧跟着
                finish();
            }
        });
    }
}
