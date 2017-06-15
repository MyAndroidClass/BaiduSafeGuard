package com.zzptc.sky.dbphoneguard.activities;

import android.os.SystemClock;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.zzptc.sky.dbphoneguard.BDApplication;
import com.zzptc.sky.dbphoneguard.R;
import com.zzptc.sky.dbphoneguard.activities.base.BaseActivity;
import com.zzptc.sky.dbphoneguard.entity.ContactInfo;
import com.zzptc.sky.dbphoneguard.fragments.SearchFragment;
import com.zzptc.sky.dbphoneguard.fragments.ShowAllContactFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 查询联系人：姓名，电话、手机归属地（如何使用greendao查询第三方数据库）
 * 使用RecyclerView显示出来
 * 选择联系人，点击确定显示联系人
 * 手动输入联系人（逻辑 复杂）
 * 点击确定，发短信  如果有紧急情况：给紧急联系人发短信
 */


/**
 * 1、当输入多个字时，出现异常？
 *    当每输入一个字时，就会调用一次before,而我们在before 里面addfragment(searchfragment)，
 *    当我们输入两个字里，那就上面的方法就会调用两次，然后就会出现异常：IllegalStateException :Fragment already added:SearchFragment
 *    解决办法：就是多次输入，多次调用before，而我只调用一次addFragment
 * 2、当已经输入一个字时，按回退键，也会出现异常？
 * 3、当输入手机号码的时候，没有搜索结果？
 * 4、点击搜索结果，选中指定的条目；而且不能超过三个
 *
 *
 *
 *
 */

public class AddContactsActivity extends BaseActivity {

    private ShowAllContactFragment showAllContactFragment;

    private SearchFragment searchFragment;

    @BindView(R.id.et_search)
    EditText et_search;
    //原始的集合
    private List<ContactInfo> originContactList;
    //搜索的结果
    private List<ContactInfo> searchResult;

    private boolean isFirstSearch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_contacts;
    }

    @Override
    protected void initViews() {
        originContactList = BDApplication.getContactInfos();
        searchResult = new ArrayList<>();

        initFragment();

        //当输入框中输入内容的时候，显示searchfragment
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //说明：fragment的初始化需要一定的时间
                if(isFirstSearch){
                    switchFragment();

                    isFirstSearch = false;

                    System.out.println("####################");
                }
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                //当用户输入内容时，这里就会有变化，只要一变化，马上通知searchfragment去更新界面
                searchResult.clear();

                System.out.println("*****************");
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       //SystemClock.sleep(500);

                       if(s.length() > 0){
                           //将你输入的东西从原来的集合中进行搜索
                           //System.out.println(s.toString() + "#############################");
                           for(int i = 0;i < originContactList.size();i++){
                               ContactInfo contactInfo = originContactList.get(i);

                               if(contactInfo.getName().contains(s) || contactInfo.getPhone().contains(s)){
                                   searchResult.add(contactInfo);
                               }
                           }
                       }

                       //控件只能在主线程进行更新
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               searchFragment.updateFragment(searchResult);

                           }
                       });

                   }
               }).start();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    //onBackPressed();

                    if(getFragmentManager().getBackStackEntryCount() > 1){
                        getFragmentManager().popBackStack();
                    }

                    isFirstSearch = true;

                    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    //第一次进来的时候，加载此fragment
    private void initFragment(){
        if(showAllContactFragment == null){
            showAllContactFragment = new ShowAllContactFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_contacts, showAllContactFragment).addToBackStack("contact").commit();
    }

    private void switchFragment(){
        if(searchFragment == null){
            searchFragment = new SearchFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addToBackStack("contact");

        if(searchFragment.isAdded()){
            transaction.hide(showAllContactFragment).show(searchFragment).commit();
        }else{
            transaction.hide(showAllContactFragment).add(R.id.fl_contacts, searchFragment).commit();
        }

        //getSupportFragmentManager().beginTransaction().add(R.id.fl_contacts, searchFragment).addToBackStack("contact").commit();
    }

    //用户点击确定
    @OnClick(R.id.tv_sure)
    public void clickSure(View view){
        finish();
    }











    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 1){
            getSupportFragmentManager().popBackStack();
        }else{
            finish();
        }
    }
}
