package com.zzptc.sky.dbphoneguard.activities;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ImageView;

import com.socks.library.KLog;
import com.zzptc.sky.dbphoneguard.BDApplication;
import com.zzptc.sky.dbphoneguard.R;
import com.zzptc.sky.dbphoneguard.activities.base.BaseActivity;
import com.zzptc.sky.dbphoneguard.common.Constants;
import com.zzptc.sky.dbphoneguard.entity.UpdateInfo;
import com.zzptc.sky.dbphoneguard.services.UpdateService;
import com.zzptc.sky.dbphoneguard.utils.BaiduUtils;

import java.io.IOException;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//两个动画：红色球的动画（集：先放大后缩小）   黄色球的动画（集：先放大后缩小）
//使用属性动画实现
public class CheckUpdateActivity extends BaseActivity {

    @BindView(R.id.iv_bigger1)
    ImageView ivBigger1;
    @BindView(R.id.iv_bigger2)
    ImageView ivBigger2;

    private AnimatorSet animatorOne; //红色球的动画
    private AnimatorSet animatorTwo; //黄色球的动画

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_check_update;
    }

    @Override
    protected void initViews() {
        //加载第一个动画   改成传统动画
        animatorOne = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.check_update_anim);
        animatorOne.setTarget(ivBigger1);
        animatorOne.start();

        animatorTwo = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.check_update_anim);
        animatorTwo.setTarget(ivBigger2);

        animatorOne.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                animatorTwo.start();
            }
        });

        animatorTwo.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                animatorOne.start();
            }
        });
    }

    @Override
    protected void initData() {
        //1  无网   2  有网，但网速比较慢，无法到达服务器/4、网络畅通且无法到达服务器  3、网络畅通，并且能到达服务器  4.最新版本，无需更新
        boolean hasNet = checkNet();
        if(hasNet){
            //告诉updateActivity，有网：网速慢  达到服务器   无法到达服务器
            //搭建服务器，访问服务器得到数据  versionName versionCode  apkUrl  description
            //客户端与服务进行交互有哪几种数据交换格式：JSON/XML  使用GsonFormat实现json数据的封闭
            //使用Retrofit访问服务器

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.1.210:8080/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                UpdateService updateService = retrofit.create(UpdateService.class);

                Call<UpdateInfo> info = updateService.getUpdateInfo();

                //execute() 默认在当前线程中执行
                //Response<UpdateInfo> updateInfoResponse = info.execute();
                info.enqueue(new Callback<UpdateInfo>() {
                    @Override
                    public void onResponse(Call<UpdateInfo> call, Response<UpdateInfo> response) {
                        UpdateInfo info = response.body();

                        int serverCode = info.getVersionCode();
                        int clientCode = BaiduUtils.getVersionCode(BDApplication.getContext());

                        KLog.d("servercode = " + serverCode + "clientCode = " + clientCode);
                        if(serverCode > clientCode){
                            Intent data = new Intent();
                            //如何将对象存储到意图当中：Serializable Parcelable  Bundle
                            data.putExtra("updateInfo",info);

                            setResult(Constants.NEED_UPDATE, data);
                            finish();
                        }else{
                            setResult(Constants.NOT_NEED_UPDATE);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateInfo> call, Throwable t) {
                        setResult(Constants.NOT_FIND_SERVER);
                        finish();
                    }
                });


        }else{

            //告诉updateActivity,没有网啦 有返回值的activity
            setResult(Constants.NO_INTERNET);
            finish();
        }
    }



    //检查网络：无网   网速慢   服务器数据
    public boolean checkNet(){
        //检查是否有网
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            // If no connectivity, cancel task and update Callback with null data.
           return false;
        }
        return  true;
    }



}
