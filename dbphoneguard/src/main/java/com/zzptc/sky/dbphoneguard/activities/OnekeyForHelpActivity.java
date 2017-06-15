package com.zzptc.sky.dbphoneguard.activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.zzptc.sky.dbphoneguard.R;
import com.zzptc.sky.dbphoneguard.activities.base.BaseActivity;
import com.zzptc.sky.dbphoneguard.fragments.FirstHelpFragment;
import com.zzptc.sky.dbphoneguard.fragments.SecondHelpFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OnekeyForHelpActivity extends BaseActivity {

    @BindView(R.id.iv_question)
    ImageView iv_question;
    @BindView(R.id.tlb_help)
    Toolbar tlb_help;

    private FirstHelpFragment firstHelpFragment;

    private SecondHelpFragment secondHelpFragment;

    //用户选中的联系人   选中的位置
    private static List<Integer> checkedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_onekey_for_help;
    }

    @Override
    protected void initViews() {
        checkedItems = new ArrayList<>();

        setSupportActionBar(tlb_help);
        tlb_help.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initFragment();
    }

    /**
     * 第一次加载的时候，初始化第一个fragment
     */
    private void initFragment(){
        if(firstHelpFragment == null){
            firstHelpFragment = new FirstHelpFragment();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, firstHelpFragment).addToBackStack("help").commit();
    }

    /**
     * 切换fragment
     */
    public void switchFragment(){
        if(secondHelpFragment == null){
            secondHelpFragment = new SecondHelpFragment();
        }
        iv_question.setVisibility(View.GONE);

        //切换
        getSupportFragmentManager().beginTransaction().hide(firstHelpFragment).add(R.id.fl_content, secondHelpFragment).addToBackStack("help").commit();
    }


    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 1){
            getSupportFragmentManager().popBackStack();

            iv_question.setVisibility(View.VISIBLE);
        }else{
            finish();
        }
    }

    @Override
    protected void initData() {

    }

    public static List<Integer> getCheckedItems() {
        return checkedItems;
    }

    public static void setCheckedItems(List<Integer> checkedItems) {
        OnekeyForHelpActivity.checkedItems = checkedItems;
    }
}
