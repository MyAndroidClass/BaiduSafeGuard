package com.zzptc.sky.dbphoneguard.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zzptc.sky.dbphoneguard.BDApplication;
import com.zzptc.sky.dbphoneguard.R;
import com.zzptc.sky.dbphoneguard.activities.AddContactsActivity;
import com.zzptc.sky.dbphoneguard.activities.OnekeyForHelpActivity;
import com.zzptc.sky.dbphoneguard.adapter.ShowContactsAdapter;
import com.zzptc.sky.dbphoneguard.entity.ContactInfo;
import com.zzptc.sky.dbphoneguard.fragments.base.BaseFragment;
import com.zzptc.sky.dbphoneguard.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
//思考：如何让这个头像的颜色不变？  把查询出来的联系人 保存到数据库
    //  如何点击条目使复选框被选中？  RecyclerView比listview更 flexible 灵活更强大，所有的点击事件需要自己添加
    //   非手机号码都不允许选中？
    //   选中的数量不能超过三个？
    //

//任务：1、仅第一次搜索有效果？
     //   2、当第一次搜索，用户点击条目时，应该清空搜索框？
//      3、显示选择联系的数量？
    // 4、点击确定将联系人显示在添加联系人的界面；




public class ShowAllContactFragment extends BaseFragment {
    @BindView(R.id.rv_contacts)
    RecyclerView rv_contacts;

    private List<ContactInfo> contactInfos;

    private ShowContactsAdapter adapter;

    private List<Integer> checkedItems;

    TextView tv_count;

    @Override
    protected int getContentView() {
        return R.layout.fragment_show_all_contact;
    }

    @Override
    protected void initViewsAndData(final View view) {
        checkedItems = OnekeyForHelpActivity.getCheckedItems();
        contactInfos = BDApplication.getContactInfos();

        if(getActivity() instanceof AddContactsActivity){
            AddContactsActivity addContactsActivity = (AddContactsActivity) getActivity();


            tv_count = (TextView) addContactsActivity.findViewById(R.id.tv_count);
        }

        adapter = new ShowContactsAdapter(contactInfos);

        rv_contacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_contacts.setAdapter(adapter);

        //设置点击事件
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {
                //点击的时候，联系人选择


                tv_count.setText("(" + checkedItems.size() + ")");

            }
        });

    }
    //此fragment从不可见到可见会调用哪个方法，然后我们就可以在可见的生命周期中，将用户选中的那个条目给勾上


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(!hidden){
            //当显示联系人的界面可见时，选中用户点击的联系人
            //因为当用户点击搜索结果时，我们已经将用户单击的联系人保存到checkedItems
            if(checkedItems.size() == 0){
                Toast.makeText(getActivity(), "请选择联系人", Toast.LENGTH_SHORT).show();
                return;
            }


            int checkPosition = checkedItems.get(checkedItems.size() - 1);
            rv_contacts.smoothScrollToPosition(checkPosition);


            //搜索
            tv_count.setText("(" + checkedItems.size() + ")");



            //显示联系人的时候，其实什么事情都 没做，刷新当前界面，通知适配器发生改变
            adapter.notifyDataSetChanged();
        }




    }
}
