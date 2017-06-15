package com.zzptc.sky.dbphoneguard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.zzptc.sky.dbphoneguard.R;
import com.zzptc.sky.dbphoneguard.activities.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class UserCenterActivity extends BaseActivity {

    @BindView(R.id.tlb_center)
    Toolbar tlbCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_user_center;
    }

    @Override
    protected void initViews() {
        setSupportActionBar(tlbCenter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //给返回按钮一个点击事件
        tlbCenter.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
            }
        });
    }

    @OnClick(R.id.ll_update)
    public void update(View view){
        startActivity(new Intent(this, UpdateActivity.class));
        overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
    }

    @Override
    protected void initData() {

    }
}
