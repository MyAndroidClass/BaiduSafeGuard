package com.zzptc.sky.dbphoneguard.fragments;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zzptc.sky.dbphoneguard.BDApplication;
import com.zzptc.sky.dbphoneguard.R;
import com.zzptc.sky.dbphoneguard.activities.AddContactsActivity;
import com.zzptc.sky.dbphoneguard.activities.OnekeyForHelpActivity;
import com.zzptc.sky.dbphoneguard.adapter.UrgentAdapter;
import com.zzptc.sky.dbphoneguard.entity.ContactInfo;
import com.zzptc.sky.dbphoneguard.fragments.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
//1、如何避免重复添加
    //2、如果删除指定位置
    //3、最多只允许添加三个紧急联系人
    //4、


public class SecondHelpFragment extends BaseFragment {


    //使用recyclerview显示用户选择的联系人
    @BindView(R.id.rv_contacts)
    RecyclerView rv_contacts;

    private List<ContactInfo> contactInfos;

    private UrgentAdapter adapter;

    private List<Integer> checkedItems;

    private List<ContactInfo> originContacts;


    @Override
    protected int getContentView() {
        return R.layout.fragment_second_help;
    }

    @OnClick(R.id.iv_addContact)
    public void addContacts(View view){
        startActivity(new Intent(getActivity(), AddContactsActivity.class));
        getActivity().overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
    }


    @Override
    protected void initViewsAndData(View view) {
        contactInfos = new ArrayList<>();

        adapter = new UrgentAdapter(contactInfos);

        rv_contacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_contacts.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();

        checkedItems = OnekeyForHelpActivity.getCheckedItems();

        originContacts = BDApplication.getContactInfos();

        if(checkedItems.size() > 0){
            //开始遍历，
            for(int i = 0;i < checkedItems.size();i++){
                ContactInfo contactInfo = originContacts.get(checkedItems.get(i));

                adapter.addContact(contactInfo);
            }
        }

    }
}
