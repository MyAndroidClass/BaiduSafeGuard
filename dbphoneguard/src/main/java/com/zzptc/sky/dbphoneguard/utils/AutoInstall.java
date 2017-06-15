package com.zzptc.sky.dbphoneguard.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by Administrator on 2017/5/10.
 */

public class AutoInstall {


    /**
     * 安装
     *
     * @param context
     *            接收外部传进来的context
     */
    public static void install(Context context, File file) {
        // 核心是下面几句代码
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    public static Intent getInstallIntent(Context context, File file){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");

        return intent;
    }







}
