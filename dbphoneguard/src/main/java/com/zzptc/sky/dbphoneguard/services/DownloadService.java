package com.zzptc.sky.dbphoneguard.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zzptc.sky.dbphoneguard.R;
import com.zzptc.sky.dbphoneguard.activities.UpdateActivity;
import com.zzptc.sky.dbphoneguard.utils.AutoInstall;
import com.zzptc.sky.dbphoneguard.utils.BaiduUtils;

import java.io.File;

import okhttp3.Call;

public class DownloadService extends Service {
    private int id = 0;

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //根据传过来的downloadurl下载apk文件

        //1、通过intent接收downloadurl
        //2、设置网络权限  SD卡权限（读、写、连接）
        //3、判断是否有SD卡，如果有SD卡并且SD卡可读写，就存储到SD卡，否则存储到系统的内部存储当中
        //3、设置通知栏
        //4、下载apk文件
        if(intent != null){
            String apkUrl = intent.getStringExtra("downloadUrl");

            File downloadPath = BaiduUtils.getPath();

            createNotification();

            downloadAndInstall(apkUrl, downloadPath);
        }




        return super.onStartCommand(intent, flags, startId);
    }

    NotificationCompat.Builder notificationBuild;
    NotificationManager notificationManager;
    //创建通知栏
    private void createNotification(){
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuild = new NotificationCompat.Builder(this);

        notificationBuild.setContentTitle("下载中……")
                            .setContentText("准备下载……")
                            .setSmallIcon(R.mipmap.ic_logo)
                            .setOngoing(true)
                            .setAutoCancel(false);

        notificationManager.notify(id, notificationBuild.build());

    }
    //下载并且安装
    private void downloadAndInstall(String url, File path){
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(path.getAbsolutePath(), "baidu_safeguard.apk") {
                    @Override  //下载出错
                    public void onError(Call call, Exception e, int i) {
                        notificationBuild.setContentText("下载失败").setAutoCancel(true).setOngoing(false);
                        notificationManager.notify(id, notificationBuild.build());
                    }

                    @Override  //下载完成
                    public void onResponse(File file, int i) {
                        //进行安装
                        notificationBuild.setOngoing(false);
                        notificationBuild.setAutoCancel(true);
                        notificationManager.notify(id, notificationBuild.build());

                        //AutoInstall.install(DownloadService.this, file);
                        //2、下载的不是apk，不能安装  判断 文件的后缀是不是apk文件   如果下载的文件大小与服务器的大小一致

                        //1、不能直接安装，需要用户点击安装
                        Intent intent = AutoInstall.getInstallIntent(DownloadService.this, file);
                        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(DownloadService.this);
                        taskStackBuilder.addParentStack(UpdateActivity.class);
                        taskStackBuilder.addNextIntent(intent);
                        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                        notificationBuild.setContentIntent(pendingIntent);
                        notificationBuild.setContentTitle("下载完成");
                        notificationBuild.setContentText("点击安装");
                        notificationBuild.setProgress(0, 0, false); // remove progress bar
                        notificationManager.notify(id, notificationBuild.build());
                        //3、下载的过程中，使用手机卫士的时候 卡  为什么？ 因为服务在主线程中执行
                        //解决办法有两个：1、intentservice  2、远程服务 process remote  3、疑问：OkHttpUtils下载文件应该在子线程中执行

                        //2、自动更新
                    }

                    @Override  //正在下载
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);

                        //System.out.println(progress + "###################" + total);

                        //int currentProgress = (int) ((float)progress / (float) total * 100);

                        notificationBuild.setProgress(100, (int) (progress * 100), false);
                        notificationBuild.setContentText("已经下载" + (int)(progress * 100) + "%");
                        notificationManager.notify(id, notificationBuild.build());

                        //System.out.println("*********************###############" + currentProgress);
                    }
                });



    }
}
