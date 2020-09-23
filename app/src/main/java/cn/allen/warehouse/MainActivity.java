package cn.allen.warehouse;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import allen.frame.AllenBaseActivity;
import allen.frame.AllenManager;
import allen.frame.entry.Type;
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
import cn.allen.warehouse.flower.FlowerFragment;
import cn.allen.warehouse.home.SaleHomeFragment;
import cn.allen.warehouse.home.WHHomeFragment;
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
    protected void addEvent() {
        adapter.setOnItemClickListener(listener);
    }

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
    }

    private MenuAdapter.OnItemClickListener listener = new MenuAdapter.OnItemClickListener() {
        @Override
        public void itemClick(View v, Type entry) {
            bindFragment(entry.getId());
        }
    };

    private void bindFragment(String id){
        switch (id){
            case "0":
                if(type==1){
                    startFragmentAdd(SaleHomeFragment.init());
                }else{
                    startFragmentAdd(WHHomeFragment.init());
                }
                break;
            case "-1":
                startFragmentAdd(FlowerFragment.init());
                break;
            case "1":
                break;
            case "2":
                break;
            case "3":
                break;
            case "4":
                break;
        }
    }

    @OnClick(R.id.back_bt)
    public void onViewClicked(View v) {
        v.setEnabled(false);
        switch (v.getId()){
            case R.id.back_bt:
                AllenManager.getInstance().back2Activity(LoginActivity.class);
                break;
        }
        v.setEnabled(true);
    }

    // fragment的切换
    private void startFragmentAdd(Fragment fragment) {
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

}
