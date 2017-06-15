package com.zzptc.sky.dbphoneguard.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.socks.library.KLog;
import com.zzptc.sky.dbphoneguard.R;
import com.zzptc.sky.dbphoneguard.activities.base.BaseActivity;
import com.zzptc.sky.dbphoneguard.common.Constants;
import com.zzptc.sky.dbphoneguard.receivers.CheckUpdateBroadcast;
import com.zzptc.sky.dbphoneguard.utils.DanceWageTimer;
import com.zzptc.sky.dbphoneguard.view.RatingBar;
import com.zzptc.sky.dbphoneguard.view.RatingView;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

//toolbar 右边的两个图片：1）以菜单的形式显示；2）以toolbar的子控件的形式显示；
//自动更新： 百度手机卫士官方自动更新：  通过通知栏提醒用户更新
//通知发送广播检查更新
public class MainActivity extends BaseActivity {

    @BindView(R.id.rv_score)
    RatingView rvScore;

    //三个随机数，用于生成RatingBar的分值
    int securityScore = new Random().nextInt(10) + 1;
    int fluencyScore = new Random().nextInt(10) + 1;
    int clearScore = new Random().nextInt(10) + 1;
    int totalScore = 0;
    @BindView(R.id.tv_score)
    TextView tvScore;

    CheckUpdateBroadcast checkUpdateBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //注册广播 一定要记得取消注册，否则会报错
        checkUpdateBroadcast = new CheckUpdateBroadcast(this);
        IntentFilter filter = new IntentFilter(Constants.CHECKUPDATE_BROADCST);
        this.registerReceiver(checkUpdateBroadcast, filter);

        //发送广播
        Intent intent = new Intent();
        intent.setAction(Constants.CHECKUPDATE_BROADCST);
        sendBroadcast(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        // score >= 8 高  5 =< score <= 7 中 score <= 4 低
        // 三元运算符   表达式 ？ 条件成立执行  :  条件不成立执行
        //           score >= 8 ? “安全度高” : ( score >= 5 && score <= 7 ? "安全度中" : “安全度低”);



        rvScore.addRatingBar(new RatingBar(securityScore, securityScore>=8 ? "安全度高" : ((securityScore >= 5 && securityScore<=7) ? "安全度中" : "安全度低")));  // 50%
        rvScore.addRatingBar(new RatingBar(fluencyScore, fluencyScore>=8 ? "流畅度高" : ((fluencyScore >= 5 && fluencyScore<=7) ? "流畅度中" : "流畅度低")));   // 30%
        rvScore.addRatingBar(new RatingBar(clearScore, clearScore>=8 ? "清洁度高" : ((clearScore >= 5 && clearScore<=7) ? "清洁度中" : "清洁度低")));     // 20%
        rvScore.show();

        rvScore.setAnimatorListener(new RatingView.AnimatorListener() {
            @Override
            public void onRotateStart() {

            }

            @Override
            public void onRotateEnd() {
                KLog.i("security = " + securityScore + " fluency = " + fluencyScore + " clear = " + clearScore);

                totalScore = (int) ((securityScore * 0.5 + fluencyScore * 0.3 + clearScore * 0.2) * 10);
                //tvScore.setText(totalScore + "");
                int totalTime = DanceWageTimer.getTotalExecuteTime(totalScore, 50);

                DanceWageTimer wageTimer = new DanceWageTimer(totalTime, 50, tvScore, totalScore);
                wageTimer.start();
            }

            @Override
            public void onRatingStart() {

            }

            @Override
            public void onRatingEnd() {

            }
        });
    }

    @OnClick(R.id.iv_center)
    public void goUserCenter(View view){
        startActivity(new Intent(this, UserCenterActivity.class));
        overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
    }

    @OnClick(R.id.baiBaoXiang)
    public void goToBaiBaoXiang(View view){
        startActivity(new Intent(this, BaiBaoXiangActivity.class));
        overridePendingTransition(R.anim.bbx_enter_anim, R.anim.main_exit_amin);
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        if(checkUpdateBroadcast != null){
            unregisterReceiver(checkUpdateBroadcast);
        }
    }
}
