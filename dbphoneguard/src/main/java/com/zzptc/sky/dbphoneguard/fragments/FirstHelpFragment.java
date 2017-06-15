package com.zzptc.sky.dbphoneguard.fragments;


import android.support.v4.app.Fragment;
import android.view.View;

import com.zzptc.sky.dbphoneguard.R;
import com.zzptc.sky.dbphoneguard.activities.OnekeyForHelpActivity;
import com.zzptc.sky.dbphoneguard.fragments.base.BaseFragment;

import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstHelpFragment extends BaseFragment {

    @Override
    protected int getContentView() {
        return R.layout.fragment_first_help;
    }

    @OnClick(R.id.btn_openHelp)
    public void replace(View view){
        //因为fragment由activity统一管理，因此替换工作应该由activity做
        if(getActivity() instanceof OnekeyForHelpActivity){
            OnekeyForHelpActivity activity = (OnekeyForHelpActivity) getActivity();

            activity.switchFragment();
        }
    }


    @Override
    protected void initViewsAndData(View view) {

    }

}
