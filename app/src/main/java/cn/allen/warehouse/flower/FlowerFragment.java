package cn.allen.warehouse.flower;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.allen.warehouse.R;
import cn.allen.warehouse.adapter.FlowerAdapter;
import cn.allen.warehouse.adapter.FlowerParentMenuAdapter;
import cn.allen.warehouse.data.WebHelper;
import cn.allen.warehouse.entry.Flower;
import cn.allen.warehouse.entry.FlowerType;
import cn.allen.warehouse.utils.Constants;

public class FlowerFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.bar_search)
    AppCompatEditText barSearch;
    @BindView(R.id.bar_notice)
    AppCompatImageView barNotice;
    @BindView(R.id.bar_name)
    AppCompatTextView barName;
    @BindView(R.id.ch_date)
    AppCompatTextView chDate;
    @BindView(R.id.hs_date)
    AppCompatTextView hsDate;
    @BindView(R.id.menu_rv)
    RecyclerView menuRv;
    @BindView(R.id.flower_rv)
    RecyclerView flowerRv;
    private ActivityHelper actHelper;
    private SharedPreferences shared;
    private int uid;
    private List<FlowerType> types;
    private FlowerParentMenuAdapter menuAdapter;
    private FlowerAdapter adapter;
    private List<Flower> list;

    public static FlowerFragment init() {
        FlowerFragment fragment = new FlowerFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flower, container, false);
        actHelper = new ActivityHelper(getActivity(), view);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi(view);
        addEvent(view);
    }

    private void initUi(View view) {
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.UserId, -1);
        barName.setText(shared.getString(Constants.UserName, "用户昵称"));
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        menuRv.setLayoutManager(manager);
        menuAdapter = new FlowerParentMenuAdapter();
        menuRv.setAdapter(menuAdapter);

        GridLayoutManager manager1 = new GridLayoutManager(getActivity(),4);
        flowerRv.setLayoutManager(manager1);
        adapter = new FlowerAdapter();
        flowerRv.setAdapter(adapter);
        laodType();
    }

    private void addEvent(View view) {
        menuAdapter.setOnItemClickListener(new FlowerParentMenuAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View v, FlowerType entry) {
                loadFlowers(entry.getId());
            }
        });
    }

    private void laodType(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                types = WebHelper.init().getGrade();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void loadFlowers(int id){
        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START,"");
        new Thread(new Runnable() {
            @Override
            public void run() {
                list = WebHelper.init().getFlowers(id);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    @OnClick({R.id.ch_date, R.id.hs_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ch_date:
                break;
            case R.id.hs_date:
                break;
        }
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    menuAdapter.setList(types);
                    break;
                case 1:
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_SUCCES,"");
                    adapter.setList(list);
                    break;
            }
        }
    };
}
