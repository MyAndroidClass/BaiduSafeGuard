package com.zzptc.sky.dbphoneguard;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.socks.library.KLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zzptc.sky.dbphoneguard.common.Constants;
import com.zzptc.sky.dbphoneguard.dao.ContactInfoDao;
import com.zzptc.sky.dbphoneguard.dao.DaoMaster;
import com.zzptc.sky.dbphoneguard.dao.DaoSession;
import com.zzptc.sky.dbphoneguard.dao.FunctionTableDao;
import com.zzptc.sky.dbphoneguard.dao.MobileDao;
import com.zzptc.sky.dbphoneguard.entity.ContactInfo;
import com.zzptc.sky.dbphoneguard.entity.FunctionTable;
import com.zzptc.sky.dbphoneguard.utils.BaiduUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/4/17.
 */

public class BDApplication extends Application {

    private static Context mContext;

    private static FunctionTableDao mFunctionTableDao;

    private static MobileDao mobileDao;

    private static ContactInfoDao contactInfoDao;

    private static List<ContactInfo> contactInfos;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        initDatabase();
        initMobileDatabase();

        KLog.init(BuildConfig.LOG_DEBUG, Constants.APP_DEGUG_TAG);

        initActivityLifeCycle();

        initOkHttpUtils();


        new Thread(new Runnable() {
            @Override
            public void run() {
                initFunctions();

                try {
                    BaiduUtils.copyDatabase(mContext, "mobile.db");

                    contactInfos = BaiduUtils.querySystemContact();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private void initOkHttpUtils(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }


    public static Context getContext(){
        return mContext;
    }


    private void initDatabase(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"func_db");

        SQLiteDatabase db = helper.getWritableDatabase();

        DaoMaster daoMaster = new DaoMaster(db);

        DaoSession daoSession = daoMaster.newSession();

        mFunctionTableDao = daoSession.getFunctionTableDao();
        contactInfoDao = daoSession.getContactInfoDao();
    }

    private void initMobileDatabase(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"mobile.db");

        SQLiteDatabase db = helper.getWritableDatabase();

        DaoMaster daoMaster = new DaoMaster(db);

        DaoSession daoSession = daoMaster.newSession();

        mobileDao = daoSession.getMobileDao();
    }

    public static MobileDao getMobileDao() {
        return mobileDao;
    }

    public static FunctionTableDao getFunctionTableDao(){
        return mFunctionTableDao;
    }

    public static ContactInfoDao getContactInfoDao(){
        return contactInfoDao;
    }

    //在activity显示的时候，通过日志打印相应的生命周期
    private void initActivityLifeCycle(){
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                KLog.v("====== " + activity + " ActivityCreated ======");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                KLog.v("====== " + activity + " ActivityStarted ======");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                KLog.v("====== " + activity + " ActivityResumed ======");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                KLog.v("====== " + activity + " nActivityPaused ======");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                KLog.v("====== " + activity + " ActivityStopped ======");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                KLog.v("====== " + activity + " ActivitySaveInstanceState ======");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                KLog.v("====== " + activity + " ActivityDestroyed ======");
            }
        });
    }









    //此初始化是一个耗时的操作，因此在要子线程中执行
    private void initFunctions(){
        //首先判断是否已经初始化，如果已经初始化，则不执行任何操作，如果没有初始化，则初始化
        SharedPreferences spf = BaiduUtils.getSharedPreferences();

        boolean isInitFunction = spf.getBoolean("init_function", false);
        if(!isInitFunction){
            //初始化
            String[] functionNames = getResources().getStringArray(R.array.function_name);
            String[] functionIcons = getResources().getStringArray(R.array.function_icon);

            for(int i = 0;i < functionNames.length;i++){
                FunctionTable table = new FunctionTable(null, functionNames[i], functionIcons[i], i, false);

                if(i == 0 || i == 1 || i == 2){
                    table.setFuncFixed(true);
                }


                getFunctionTableDao().insert(table);
            }

            spf.edit().putBoolean("init_function", true).commit();
        }
    }

    public static List<ContactInfo> getContactInfos() {
        return contactInfos;
    }
}
