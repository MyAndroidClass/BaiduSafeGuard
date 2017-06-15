package com.zzptc.sky.dbphoneguard.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;

import com.github.lzyzsd.randomcolor.RandomColor;
import com.zzptc.sky.dbphoneguard.BDApplication;
import com.zzptc.sky.dbphoneguard.common.Constants;
import com.zzptc.sky.dbphoneguard.dao.MobileDao;
import com.zzptc.sky.dbphoneguard.entity.ContactInfo;
import com.zzptc.sky.dbphoneguard.entity.Mobile;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public class BaiduUtils {

    public static SharedPreferences getSharedPreferences(){
        return BDApplication.getContext().getSharedPreferences(Constants.SHARED_DATA, Context.MODE_PRIVATE);
    }

    //判断是否在SD卡
    public static boolean isExistSDCard(){
        String states = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(states)){
            return true;
        }else{
            return false;
        }
    }

    //如果有SD卡并且SD卡可读写，就存储到SD卡，否则存储到系统的内部存储当中
    public static File getPath(){
        File path = null;
        if(isExistSDCard()){
            path = Environment.getExternalStorageDirectory();
        }else{
            path = Environment.getDownloadCacheDirectory();
        }

        return path;
    }

    public static int getVersionCode(Context context)//获取版本号(内部识别号)
    {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }


    //将string R.id.xxxx ==> bitmap/drawable
    public static Drawable getFunctionIcon(String functionPic){
        Context context = BDApplication.getContext();
        int resId = context.getResources().getIdentifier(functionPic,"mipmap",context.getPackageName());

        Drawable drawable = ContextCompat.getDrawable(context, resId);
        return drawable;
    }


    /************************************************ 查询联系人：姓名，电话、手机归属地（如何使用greendao查询第三方数据库）*****************************************************/
    //目的：查询系统的联系人：姓名，电话号码；根据你查询出来的电话号码从第三方数据库：mobile.db中查询手机归属地;
    // 难点：  如何查询系统联系人信息；   如何使用greendao查询外部数据库；
    // 思路：
    //     1、系统以内容提供者(ContentProvider)的形式对外提供联系人的查询(ContentResolver)；
    //     2、将数据库文件拷贝到/data/data/包名/databases/目录下；
    //     3、使用greendao的注解功能生成与数据库文件中的表名、字段名一致的实体类；
    //     4、使用DaoMaster指定数据库文件的名字与第三方数据库的文件名一致；
    //     5、根据电话号码查询手机归属地；
    //     2->封装（姓名、电话号码、手机归属地） 集合(List)-> 1-> 3 -> 4 -> 5

    private static String path = "/data/data/%s/databases";

    //数据库的保存路径(directory)
    private static String getDatabasePath(Context context){
        return String.format(path, context.getPackageName());
    }

    //数据库文件的路径
    private static String databaseFilePath(Context context,String dbName){
        return getDatabasePath(context) + "/" + dbName;
    }

    //将数据库文件从assets/mobile.db ==> /data/data/packageName/databases/mobile.db
    //在这里给大家介绍一个第三方文件复制的库：commons-io  apache
    public static void copyDatabase(Context context, String dbName) throws Exception{
        File directory = new File(getDatabasePath(context));
        if(!directory.exists()){
            directory.mkdir();
        }

        File dbFile = new File(databaseFilePath(context, dbName));
        if(!dbFile.exists()){
            dbFile.createNewFile();
        }

        //拷贝
        URL source = context.getClass().getClassLoader().getResource("assets/" + dbName);

        FileUtils.copyURLToFile(source, dbFile);
    }


    /**
     * 查询系统联系人 将联系人保存到数据库
     * @return
     */
    public static List<ContactInfo> querySystemContact(){
        //第二次进来的时候，就不能再去new一个集合了，需要从数据库中读取出来
        //真正开发的话，手机归属地的查询是一个： 云查询
        List<ContactInfo> contactInfos = BDApplication.getContactInfoDao().queryBuilder().list();

        if(contactInfos == null || contactInfos.size() == 0){
            ContentResolver resolver = BDApplication.getContext().getContentResolver();
            //首先查询ContactsContract.Contacts 表，此表中保存的是原始联系人：三张表关联的id，联系人的姓名，是否存在电话号码
            //电话号码等详细信息保存在ContactsContract.Data 表中，
            Cursor originContactCursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            while(originContactCursor.moveToNext()){
                ContactInfo contactInfo = new ContactInfo();
                //id
                int contactId = originContactCursor.getInt(originContactCursor.getColumnIndex(ContactsContract.Contacts._ID));
                //姓名
                String contactName = originContactCursor.getString(originContactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contactInfo.setName(contactName);
                //判断是否存在电话号码
                int hasPhoneNumber = originContactCursor.getInt(originContactCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if(hasPhoneNumber > 0){
                    //表示 至少有一个电话号码
                    String phone = queryPhoneNumberById(contactId);
                    contactInfo.setPhone(phone);

                    //根据电话号码查询手机归属地
                    //任务：1、如何判断号码是手机还是座机号码
                    //      2、如果是座机号码，又应该如何查询？
                    String phoneAddress = queryAddressByPhone(phone);
                    contactInfo.setAttribute(phoneAddress);

                    //添加颜色
                    int randomColor = new RandomColor().randomColor();
                    contactInfo.setHeadColor(randomColor);
                }

                contactInfos.add(contactInfo);
            }

            originContactCursor.close();
            //循环读取所有联系人之后，统一保存到数据库
            //BDApplication.getFunctionTableDao().insertInTx(contactInfos);
            BDApplication.getContactInfoDao().insertInTx(contactInfos);
        }


        return contactInfos;
    }

    //根据id查询电话号码：ContactsContract.CommonDataKinds.Phone
    private static String queryPhoneNumberById(int contactId){
        ContentResolver resolver = BDApplication.getContext().getContentResolver();

        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                new String[]{contactId + ""}, null);

        if(phoneCursor != null && phoneCursor.moveToFirst()){
            String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            return phone.replace("-","").replace(" ","");
        }
        phoneCursor.close();
        return null;
    }

    //根据联系人号码查询归属地
    private static String queryAddressByPhone(String phoneNumber){
        //思考：昨天还是好的，今天就出错了，为什么？因为刚才我添加了一些座机号码与一些短号码，截取前七位 10086
        //如何判断是手机号码？
        String address = "未知";

        if(phoneNumber.length() == 11 && phoneNumber.matches("^1[34578]\\d{9}$")){
            String phone = phoneNumber.substring(0, 7);

            Mobile mobile = queryMobile(phone);

            if(mobile != null){
                address = mobile.getMobileArea() + "\n" + mobile.getMobileType();
            }
        }else{
            //其它号码：座机号码
            //哪些情况下可能是座机号码？   3位区号+ 七位号码 / 3位区号 + 八位号码  / 4位区号 + 七位号码 / 4位区号 + 八位号码 /  七位号码  / 八位号码 / 099110086 01010086
            int length = phoneNumber.length();
            Mobile mobile = null;
            switch (length){
                case 8:

                    break;
                case 9:

                    break;
                case 10:
                    mobile = queryZuojiMobile(phoneNumber, 3);
                    if(mobile != null){
                        address = mobile.getMobileArea();
                    }
                    break;
                case 11:
                    mobile = queryZuojiMobile(phoneNumber, 4);
                    if(mobile == null){
                        mobile = queryZuojiMobile(phoneNumber, 3);
                    }

                    if(mobile != null){
                        address = mobile.getMobileArea();
                    }

                    break;
                case 12:
                    mobile = queryZuojiMobile(phoneNumber, 4);
                    if(mobile != null){
                        address = mobile.getMobileArea();
                    }
                    break;
                default:
                    address = "未知";
                    break;
            }
            //如果是座机号码的话，又该如何查询？
        }

        return address;
    }

    //查询手机号码的相关信息
    private static Mobile queryMobile(String phoneNumber){
        return BDApplication.getMobileDao().queryBuilder().where(MobileDao.Properties.MobileNumber.eq(phoneNumber)).distinct().unique();
    }

    private static Mobile queryZuojiMobile(String phoneNumber, int endPosition){
        return BDApplication.getMobileDao().queryBuilder().where(MobileDao.Properties.AreaCode.eq(phoneNumber.substring(0, endPosition))).limit(1).distinct().unique();
    }













}
