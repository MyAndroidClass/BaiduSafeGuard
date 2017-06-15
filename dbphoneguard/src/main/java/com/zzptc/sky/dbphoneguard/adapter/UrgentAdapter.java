package com.zzptc.sky.dbphoneguard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzptc.sky.dbphoneguard.R;
import com.zzptc.sky.dbphoneguard.entity.ContactInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/5 005.
 */

public class UrgentAdapter extends RecyclerView.Adapter<UrgentAdapter.ViewHolder>{

    private List<ContactInfo> contactInfos;

    public UrgentAdapter(List<ContactInfo> contactInfos) {
        this.contactInfos = contactInfos;
    }

    //添加联系人，并更新
    public void addContact(ContactInfo contactInfo){
        contactInfos.add(contactInfo);

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.urgent_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactInfo contactInfo = contactInfos.get(position);

        holder.tv_name.setText(contactInfo.getName());
        holder.tv_phone.setText(contactInfo.getPhone());
    }

    @Override
    public int getItemCount() {
        return contactInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_phone)
        TextView tv_phone;
        @BindView(R.id.iv_cancel)
        ImageView iv_cancel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
