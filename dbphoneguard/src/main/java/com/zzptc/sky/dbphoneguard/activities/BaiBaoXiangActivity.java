package com.zzptc.sky.dbphoneguard.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.zzptc.sky.dbphoneguard.BDApplication;
import com.zzptc.sky.dbphoneguard.R;
import com.zzptc.sky.dbphoneguard.activities.base.BaseActivity;
import com.zzptc.sky.dbphoneguard.adapter.BaiBaoXiangAdapter;
import com.zzptc.sky.dbphoneguard.dao.FunctionTableDao;
import com.zzptc.sky.dbphoneguard.entity.FunctionTable;
import com.zzptc.sky.dbphoneguard.listener.MyItemTouchHelper;
import com.zzptc.sky.dbphoneguard.listener.OnRecyclerItemClickListener;
import com.zzptc.sky.dbphoneguard.utils.BaiduUtils;
import com.zzptc.sky.dbphoneguard.utils.DividerGridItemDecoration;

import java.util.List;

import butterknife.BindView;

//1、初始化百宝箱的数据并保存到数据库：List<FunctionTable>
     //****** 在哪个地方初始比较好  application  此操作是一个耗时的操作，一定要在子线程中执行
     //****** 初始化几次  只初始化一次  如何判断已经初始化了？  共享首选项判断是否初始化
     // 如何 初始化   先把数据放到list集合当中，然后通过遍历list将数据保存到数据库中
                   // 因为这些数据基本是固定的，因此我们可以将这些数据保存到String.xml中
    //****** 数据如何保存到string.xml，保存哪些数据  <string-array>
   // ****** 以及如何读取string.xml中的数据   getResource().getStringArray();
     //****  读取与保存功能数据到数据库中 使用的是第三方框架：greendao

//2、使用RecyclerView显示网格
    //原始数据 在数据库中，使用greenDao读取数据库中的数据 List<FunctionTable>
    //适配器  在准备适配器之前，要先准备条目布局，然后准备适配器   extends RecyclerView.Adapter<VH>
    //通过RecyclerView设置适配器,在设置适配器之前，一定要记得设置布局管理器。 GridLayoutManager
//3、实现可拖拽：保存位置信息；

//实现可拖拽的功能思路：
//1、使用recyclerview实现网格布局
//2、因为ItemTouchHelper==> MyItemTouchHelper implements ItemTouchHelper.Callback
//3、通过
/*itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(adapter).setOnDragListener(this));
itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemTouchListener({
        itemTouchHelper.startDrag(vh);
        })
实现可拖拽*/
//4、在adapter中记得移动位置
//5、保存



public class BaiBaoXiangActivity extends BaseActivity implements MyItemTouchHelper.FinishDragListener{

    @BindView(R.id.rv_functions)
    RecyclerView rv_functions;

    private BaiBaoXiangAdapter adapter;

    private List<FunctionTable> functions;

    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_bai_bao_xiang;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {
        //在读取的时候，要按照索引顺序排序  从小到大
        functions = BDApplication.getFunctionTableDao().queryBuilder().orderAsc(FunctionTableDao.Properties.FuncIndex).build().list();

        adapter = new BaiBaoXiangAdapter(functions);

        rv_functions.setLayoutManager(new GridLayoutManager(this, 3));
        rv_functions.addItemDecoration(new DividerGridItemDecoration(this));
        rv_functions.setAdapter(adapter);

        //************************************************************************************
        MyItemTouchHelper callback = new MyItemTouchHelper();
        callback.setMoveListener(adapter);
        callback.setFinishDragListener(this);

        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rv_functions);

        rv_functions.addOnItemTouchListener(new OnRecyclerItemClickListener(rv_functions){
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                    if(!functions.get(vh.getLayoutPosition()).getFuncFixed()){
                        itemTouchHelper.startDrag(vh);
                    }

            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                //Toast.makeText(BaiBaoXiangActivity.this, "您点击的位置：" + vh.getLayoutPosition(), Toast.LENGTH_SHORT).show();
                FunctionTable table = functions.get(vh.getLayoutPosition());

                if(table.getFuncName().equals("回首页")){
                    finish();
                    overridePendingTransition(R.anim.main_exit_amin, R.anim.down_exit_anim);
                }else if(table.getFuncName().equals("家人守护")){
                    startActivity(new Intent(BaiBaoXiangActivity.this, FamilyProtectedActivity.class));
                    overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                }


            }
        });


    }

    @Override
    public void finidDrag() {
        //当用户停止拖拽的时候，就会触发此事件
        //停止拖拽的时候，要保存用户的数据
        //因为数据保存在数据库中，因此我们需要读取数据库中的数据 functions，然后修改 index
        for(int i = 0;i < functions.size();i++){
            FunctionTable table = functions.get(i);
            //System.out.println(table.getFuncName()  + "**********" + table.getFuncIndex());
            table.setFuncIndex(i);
            //System.out.println(table.getFuncName()  + "##########" + table.getFuncIndex());
            //BDApplication.getFunctionTableDao().update(table);
        }
        //保存到数据库中
       new Thread(new Runnable() {
           @Override
           public void run() {
               BDApplication.getFunctionTableDao().updateInTx(functions);
           }
       }).start();
    }
}
