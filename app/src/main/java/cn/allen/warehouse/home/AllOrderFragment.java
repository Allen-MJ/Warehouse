package cn.allen.warehouse.home;

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
import allen.frame.widget.MaterialRefreshLayout;
import allen.frame.widget.MaterialRefreshListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.allen.warehouse.BaseFragment;
import cn.allen.warehouse.R;
import cn.allen.warehouse.adapter.AllOrderAdapter;
import cn.allen.warehouse.data.WebHelper;
import cn.allen.warehouse.entry.Order;
import cn.allen.warehouse.utils.Constants;

public class AllOrderFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.bar_search)
    AppCompatEditText barSearch;
    @BindView(R.id.bar_notice)
    AppCompatImageView barNotice;
    @BindView(R.id.bar_name)
    AppCompatTextView barName;
    @BindView(R.id.show_rv)
    RecyclerView showRv;
    @BindView(R.id.refreshLayout)
    MaterialRefreshLayout mater;
    @BindView(R.id.back_bt)
    AppCompatImageView backBt;
    private SharedPreferences shared;
    private AllOrderAdapter adapter;
    private ActivityHelper actHelper;
    private List<Order> list, sublist;
    private boolean isRefresh = false;
    private int page = 1;
    private int pagesize = 10;
    private int uid;

    public static AllOrderFragment init() {
        AllOrderFragment fragment = new AllOrderFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wh_home, container, false);
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
        adapter = new AllOrderAdapter();
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        showRv.setLayoutManager(manager);
        showRv.setAdapter(adapter);
        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START, "");
        loadData();
    }

    private void addEvent(View view) {
        mater.setMaterialRefreshListener(materListener);
        adapter.setOnItemClickListener(listener);
    }

    private MaterialRefreshListener materListener = new MaterialRefreshListener() {
        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            isRefresh = true;
            page = 1;
            loadData();
        }

        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
            isRefresh = false;
            loadData();
        }
    };

    private AllOrderAdapter.OnItemClickListener listener = new AllOrderAdapter.OnItemClickListener() {
        @Override
        public void itemClick(View v, Order entry) {

        }
    };

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sublist = WebHelper.init().getAllOrder(uid, page++, pagesize).getList();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_SUCCES, "");
                    if (isRefresh) {
                        list = sublist;
                        mater.finishRefresh();
                    } else {
                        if (page == 2) {
                            list = sublist;
                        } else {
                            list.addAll(sublist);
                        }
                        mater.finishRefreshLoadMore();
                    }
                    if (list.size() == 0) {
                        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_FAIL, "暂无订单");
                    }
                    actHelper.setCanLoadMore(mater, pagesize, list);
                    adapter.setList(list);
                    break;
            }
        }
    };

    @OnClick(R.id.back_bt)
    public void onViewClicked(View view) {
        view.setEnabled(false);
        switch (view.getId()){
            case R.id.back_bt:
                backPreFragment();
                break;
        }
        view.setEnabled(true);
    }
}
