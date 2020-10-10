package cn.allen.warehouse;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import allen.frame.AllenBaseActivity;
import allen.frame.AllenManager;
import allen.frame.entry.Type;
import allen.frame.tools.MsgUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.warehouse.adapter.MenuAdapter;
import cn.allen.warehouse.data.WebHelper;
import cn.allen.warehouse.entry.ShowOrder;
import cn.allen.warehouse.flower.FlowerFragment;
import cn.allen.warehouse.home.OrderFragment;
import cn.allen.warehouse.home.SaleHomeFragment;
import cn.allen.warehouse.home.SaleHomeFragment.OnItemMenuClickLisenter;
import cn.allen.warehouse.home.AllOrderFragment;
import cn.allen.warehouse.utils.Constants;

public class MainActivity extends AllenBaseActivity {
    @BindView(R.id.menu_rv)
    RecyclerView menuRv;
    @BindView(R.id.back_bt)
    AppCompatImageView backBt;
    @BindView(R.id.container)
    FrameLayout container;
    private MenuAdapter adapter;
    private List<Type> list;
    private SharedPreferences shared;
    private int type;
    private Fragment curfragment;
    private Map<String,Integer> map;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
        shared = AllenManager.getInstance().getStoragePreference();
        type = shared.getInt(Constants.UserType,0);
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        menuRv.setLayoutManager(manager);
        adapter = new MenuAdapter();
        menuRv.setAdapter(adapter);
        loadMenu();
    }

    @Override
    public void onBackPressed() {
        actHelper.doClickTwiceExit(menuRv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNum();
    }

    @Override
    protected void addEvent() {
        adapter.setOnItemClickListener(listener);
    }

    private OnItemMenuClickLisenter menulistener = new OnItemMenuClickLisenter() {
        @Override
        public void itemMenu(ShowOrder order) {
            if(order.getId()!=0){
                bindFragment(String.valueOf(order.getId()));
                adapter.setCheck(String.valueOf(order.getId()));
            }else{
                adapter.setCheck(String.valueOf(order.getId()));
                startNextFragment(AllOrderFragment.init(""));
            }
        }
    };

    private void loadMenu(){
        list = new ArrayList<>();
        list.add(new Type("0","主页        ",R.drawable.menu_home_selecor));
        if(type==1){
            list.add(new Type("-1","花材目录",R.drawable.menu_flower_selecor));
        }
        list.add(new Type("1","待配货    ",R.drawable.menu_dph_selecor));
        list.add(new Type("2","待出库    ",R.drawable.menu_dck_selecor));
        list.add(new Type("3","待回收    ",R.drawable.menu_dhk_selecor));
        list.add(new Type("4","已回收    ",R.drawable.menu_yhk_selecor));
        list.add(new Type("5","完成清点",R.drawable.menu_wcqd_selecor));
        adapter.setList(list);
        adapter.setCheck(list.get(0).getId());
        bindFragment(String.valueOf(list.get(0).getId()));
    }

    private void loadNum(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                map = WebHelper.init().getOrderNumber(shared.getInt(Constants.UserId,-1));
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private MenuAdapter.OnItemClickListener listener = new MenuAdapter.OnItemClickListener() {
        @Override
        public void itemClick(View v, Type entry) {
            bindFragment(entry.getId());
            loadNum();
        }
    };

    private void bindFragment(String id){
        switch (id){
            case "0":
                startFragmentAdd(SaleHomeFragment.init().setOnItemMenuClickLisenter(menulistener));
                break;
            case "-1":
                startFragmentAdd(FlowerFragment.init());
                break;
            case "1":
                startFragmentAdd(OrderFragment.init(1));
                break;
            case "2":
                startFragmentAdd(OrderFragment.init(2));
                break;
            case "3":
                startFragmentAdd(OrderFragment.init(3));
                break;
            case "4":
                startFragmentAdd(OrderFragment.init(4));
                break;
            case "5":
                startFragmentAdd(OrderFragment.init(5));
                break;
        }
    }

    @OnClick(R.id.back_bt)
    public void onViewClicked(View v) {
        v.setEnabled(false);
        switch (v.getId()){
            case R.id.back_bt:
                MsgUtils.showMDMessage(context, "确定退出账号？", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        AllenManager.getInstance().back2Activity(LoginActivity.class);
                    }
                },"取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                break;
        }
        v.setEnabled(true);
    }

    private void startNextFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, null)
                .addToBackStack(null)
                .commit();
    }

    // fragment的切换
    public void startFragmentAdd(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        if (curfragment == null) {
            fragmentTransaction.add(R.id.container, fragment).commit();
            curfragment = fragment;
        }
        if (curfragment != fragment) {
            // 先判断是否被add过
            if (!fragment.isAdded()) {
                // 隐藏当前的fragment，add下一个到Activity中
                fragmentTransaction.hide(curfragment)
                        .add(R.id.container, fragment).commit();
            } else {
                // 隐藏当前的fragment，显示下一个
                fragmentTransaction.hide(curfragment).show(fragment)
                        .commit();
            }
            curfragment = fragment;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    adapter.setNumber(map);
                    break;
            }
        }
    };

}
