package com.zzptc.sky.dbphoneguard.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zzptc.sky.dbphoneguard.R;
import com.zzptc.sky.dbphoneguard.activities.base.BaseActivity;

import butterknife.OnClick;

public class FamilyProtectedActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_family_protected;
    }

    @OnClick(R.id.rl_help)
    public void forHelp(View view){
        startActivity(new Intent(this, OnekeyForHelpActivity.class));
        overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
    }


    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }
}
