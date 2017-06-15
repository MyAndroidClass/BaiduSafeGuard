package com.zzptc.sky.dbphoneguard.services;

import com.zzptc.sky.dbphoneguard.entity.UpdateInfo;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017/5/8.
 */

public interface UpdateService {

    @GET("UpdateServer/UpdateServlet")
    Call<UpdateInfo> getUpdateInfo();
}
