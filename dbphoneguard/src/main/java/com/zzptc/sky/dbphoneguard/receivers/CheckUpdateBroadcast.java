package com.zzptc.sky.dbphoneguard.receivers;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.socks.library.KLog;
import com.zzptc.sky.dbphoneguard.BDApplication;
import com.zzptc.sky.dbphoneguard.common.Constants;
import com.zzptc.sky.dbphoneguard.entity.UpdateInfo;
import com.zzptc.sky.dbphoneguard.fragments.UpdateDialogFragment;
import com.zzptc.sky.dbphoneguard.services.UpdateService;
import com.zzptc.sky.dbphoneguard.utils.BaiduUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/5/11.
 */
//此广播接收者，接收到广播之后，检查更新
public class CheckUpdateBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //System.out.println("我接收到广播啦，我做准备更新……");
        //1、判断检查日期（保存到共享首选项当中），如果检查日期为空，表示第一次进行安装使用，设置当前日期为检查日期
        //                 如果检查日期不为空，则表示已经检查过更新
        //2、如果不为空，判断当前日期- 检查日期 >= 设置的检查间隔  如果大于  检查  如果小于  不检查
        //3、检查更新
        SharedPreferences sp = BaiduUtils.getSharedPreferences();

        String checkDate = sp.getString("CheckDate","2017-5-5");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //第一次安装
        if(checkDate == null || "".equals(checkDate)){
            Date date = new Date(); // 当前日期   date ==> string --> 保存到共享首选项当中
            String strDate = sdf.format(date);

            sp.edit().putString("CheckDate",strDate).commit();
        }else{
            //不为空，说明不是第一次安装，已经使用了
            try {
                Date nowDate = new Date();
                //string --> date
                Date ckDate = sdf.parse(checkDate);
                // nowDate - ckDate >= 7
                int shijiacha = getShiJianCha(nowDate, ckDate);

                if(shijiacha >= 7){
                    //检查更新
                    checkUpdate();

                    //保存当前检查的时间
                    sp.edit().putString("CheckDate",sdf.format(nowDate)).commit();
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
    }

    private int getShiJianCha(Date nowDate , Date checkDate){
        // 年*365 + 月 * 31  +  日 -  年*365 + 月 * 31  +  日
        //在减之前，先得到年，如果是同一年，不是同一年
        return getDayInYear(nowDate) - getDayInYear(checkDate);
    }

    private int getDayInYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int day = calendar.get(Calendar.DAY_OF_YEAR);

        return day;
    }

    private void checkUpdate(){
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
                    //System.out.println("检查到更新啦，赶紧弹出对话框提示用户下载吧！");
                    showDialog(info);
                }
            }

            @Override
            public void onFailure(Call<UpdateInfo> call, Throwable t) {

            }
        });
    }

    private Activity act;

    public CheckUpdateBroadcast(Activity act) {
        this.act = act;
    }

    private void showDialog(UpdateInfo updateInfo){
        FragmentManager fragmentManager = act.getFragmentManager();
        UpdateDialogFragment updateDialogFragment = UpdateDialogFragment.newInstance(updateInfo);

        updateDialogFragment.show(fragmentManager, "dialog");
    }

}
