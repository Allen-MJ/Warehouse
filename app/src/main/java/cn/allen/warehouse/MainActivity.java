package cn.allen.warehouse;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.warehouse.adapter.MenuAdapter;
import cn.allen.warehouse.home.HomeFragment;

public class MainActivity extends AllenBaseActivity {
    @BindView(R.id.menu_rv)
    RecyclerView menuRv;
    @BindView(R.id.back_bt)
    AppCompatImageView backBt;
    @BindView(R.id.container)
    FrameLayout container;
    private MenuAdapter adapter;
    private List<Type> list;

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
        list.add(new Type("1","花材目录",R.drawable.menu_flower_selecor));
        list.add(new Type("2","待配货    ",R.drawable.menu_dph_selecor));
        list.add(new Type("3","待出库    ",R.drawable.menu_dck_selecor));
        list.add(new Type("4","待回收    ",R.drawable.menu_dhk_selecor));
        list.add(new Type("5","已回收    ",R.drawable.menu_yhk_selecor));
        list.add(new Type("6","完成清点",R.drawable.menu_wcqd_selecor));
        adapter.setList(list);
    }

    private MenuAdapter.OnItemClickListener listener = new MenuAdapter.OnItemClickListener() {
        @Override
        public void itemClick(View v, Type entry) {
            bindFragment(entry.getId());
        }
    };

    private void bindFragment(String id){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, getFragmentById(id), null)
                .addToBackStack(null)
                .commit();
    }

    private Fragment getFragmentById(String id){
        Fragment fragment = null;
        switch (id){
            case "0":
                fragment = new HomeFragment();
            break;
            case "1":
                fragment = new HomeFragment();
            break;
            case "2":
                fragment = new HomeFragment();
            break;
            case "3":
                fragment = new HomeFragment();
            break;
            case "4":
                fragment = new HomeFragment();
            break;
        }
        return fragment;
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
}
