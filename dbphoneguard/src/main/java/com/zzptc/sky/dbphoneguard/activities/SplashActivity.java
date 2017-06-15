package com.zzptc.sky.dbphoneguard.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzptc.sky.dbphoneguard.R;
import com.zzptc.sky.dbphoneguard.activities.base.BaseActivity;
import com.zzptc.sky.dbphoneguard.adapter.SplashAdapter;
import com.zzptc.sky.dbphoneguard.common.Constants;
import com.zzptc.sky.dbphoneguard.utils.BaiduUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

//如何实现引导界面 ViewPager(翻页的效果) + views（三个view ==> layout）  欢迎界面
// 如何修改复选框的样子  选择器 selector  控件有两种状态：选中  非选中 我们可能通过选择器指定选中的图片  非选中的图片
//如何使用viewpager实现引导界面  viewpager => 原始数据(view => list<view>) ==> adapter => 设置适配器

// 进入第三个页面：无跳过  如何知道用户进入到第几个界面？  viewpager监听
// 三个点的问题 只能选中一个：单选按钮组  shape
// 全屏的问题
// 点击进入卫士 跳转到主界面，而且下次要进入欢迎界面

public class SplashActivity extends BaseActivity {

    @BindView(R.id.vp_splash)
    ViewPager vpSplash;
    @BindView(R.id.activity_splash)
    RelativeLayout activitySplash;
    @BindView(R.id.tv_go)
    TextView tvGo;
    @BindView(R.id.rg_dots)
    RadioGroup rgDots;

    private List<View> pagers;

    private SplashAdapter splashAdapter;

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews() {
        pagers = new ArrayList<>();
        //layout  => view ==> list<view>
        View firstView = View.inflate(this, R.layout.splash_first_layout, null);
        View secondView = View.inflate(this, R.layout.splash_second_layout, null);
        View thirdView = View.inflate(this, R.layout.splash_third_layout, null);

        pagers.add(firstView);
        pagers.add(secondView);
        pagers.add(thirdView);

        Button btn_go = (Button) thirdView.findViewById(R.id.btn_go);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPreferences = BaiduUtils.getSharedPreferences();
                //修改共享首选项中的值
                mSharedPreferences.edit().putBoolean(Constants.IS_FIRST_LAUNCH,false).commit();
               // boolean flag = mSharedPreferences.getBoolean(Constants.IS_FIRST_LAUNCH, true);

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
        tvGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳到第三个页面
                vpSplash.setCurrentItem(2);
            }
        });

    }

    @Override
    protected void initData() {
        splashAdapter = new SplashAdapter(pagers);

        vpSplash.setAdapter(splashAdapter);

        vpSplash.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rgDots.check(R.id.rb_first_dot);
                        tvGo.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        rgDots.check(R.id.rb_second_dot);
                        tvGo.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        rgDots.check(R.id.rb_third_dot);
                        tvGo.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
