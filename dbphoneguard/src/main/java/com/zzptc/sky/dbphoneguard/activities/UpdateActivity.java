package com.zzptc.sky.dbphoneguard.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.zzptc.sky.dbphoneguard.R;
import com.zzptc.sky.dbphoneguard.activities.base.BaseActivity;
import com.zzptc.sky.dbphoneguard.common.Constants;
import com.zzptc.sky.dbphoneguard.entity.UpdateInfo;
import com.zzptc.sky.dbphoneguard.fragments.UpdateDialogFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class UpdateActivity extends BaseActivity {

    @BindView(R.id.tlb_update)
    Toolbar tlbUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_update;
    }

    @Override
    protected void initViews() {
        setSupportActionBar(tlbUpdate);

        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //给返回按钮一个点击事件
        tlbUpdate.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
            }
        });
    }


    /********************************版本更新***************************************************/
    @OnClick(R.id.ll_update)
    public void update(View view){
        //点击更新，弹出activity，但是我们要将弹出activity设置成对话框
       // startActivity(new Intent(this, CheckUpdateActivity.class));
        //启动有返回值的activity
        Intent intent = new Intent(this, CheckUpdateActivity.class);

        startActivityForResult(intent,Constants.REQUEST_CODE);
    }


    //1、当CheckUpdateActivity返回不同的信息时，我们需要弹出不同的对话框   如何实现activity与fragment的通信
    //2、在UpdateDialogFragment中添加butterKnife；
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case Constants.NO_INTERNET:
                //Toast.makeText(this, "没网啦。。。。。", Toast.LENGTH_SHORT).show();
                showDialog("你进入网络的异次元世界！");
                break;
            case Constants.NOT_FIND_SERVER:
                //Toast.makeText(this, "服务器走丢了。。。。。", Toast.LENGTH_SHORT).show();
                showDialog("服务器走丢啦！");
                break;
            case Constants.NOT_NEED_UPDATE:
                showDialog("您使用的是最新版本");
                break;
            case Constants.NEED_UPDATE:
                //showDialog("有版本更新啦！");
                UpdateInfo info = (UpdateInfo) data.getSerializableExtra("updateInfo");

                //Toast.makeText(this, "url: " + info.getApkUrl(), Toast.LENGTH_SHORT).show();
                showDialog(info);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void showDialog(String msg){
        FragmentManager fragmentManager = getFragmentManager();
        UpdateDialogFragment updateDialogFragment = UpdateDialogFragment.newInstance(msg);

        updateDialogFragment.show(fragmentManager, "dialog");
    }


    private void showDialog(UpdateInfo updateInfo){

        FragmentManager fragmentManager = this.getFragmentManager();
        UpdateDialogFragment updateDialogFragment = UpdateDialogFragment.newInstance(updateInfo);

        updateDialogFragment.show(fragmentManager, "dialog");
    }



    @Override
    protected void initData() {

    }
}
