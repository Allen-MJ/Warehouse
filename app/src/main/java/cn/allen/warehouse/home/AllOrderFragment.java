package cn.allen.warehouse.home;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import allen.frame.tools.StringUtils;
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
import cn.allen.warehouse.order.DeliverFragment;
import cn.allen.warehouse.order.DeliverXsFragment;
import cn.allen.warehouse.order.InventoryFragment;
import cn.allen.warehouse.order.InventoryXsFragment;
import cn.allen.warehouse.order.ReturnedFragment;
import cn.allen.warehouse.order.ReturnedXsFragment;
import cn.allen.warehouse.order.ToBeReturnedFragment;
import cn.allen.warehouse.order.ToBeReturnedXsFragment;
import cn.allen.warehouse.order.WarehouseOutFragment;
import cn.allen.warehouse.order.WarehouseOutXsFragment;
import cn.allen.warehouse.utils.Constants;
import cn.allen.warehouse.widget.SearchEditText;

public class AllOrderFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.bar_search)
    SearchEditText barSearch;
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
    private int uid,type;
    private String no;

    public static AllOrderFragment init(String no) {
        AllOrderFragment fragment = new AllOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("no",no);
        fragment.setArguments(bundle);
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
        no = getArguments().getString("no");
        barSearch.setHint("输入客户名字或订单号");
        barSearch.setText(no);
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.UserId, -1);
        type = shared.getInt(Constants.UserType,-1);
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
        barSearch.setOnSerchListenner(new SearchEditText.onSerchListenner() {
            @Override
            public void onSerchEvent() {
                no = barSearch.getText().toString().trim();
                page = 1;
                actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START,"");
                isRefresh = true;
                loadData();
            }
        });
        barSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    view.setEnabled(false);
                    no = barSearch.getText().toString().trim();
                    page = 1;
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START,"");
                    isRefresh = true;
                    loadData();
                    view.setEnabled(true);
                    return true;
                }else if(i==KeyEvent.KEYCODE_DEL){
                    no = barSearch.getText().toString().trim();
                    int len = StringUtils.empty(no)?0:no.length();
                    if(len>0){
                        barSearch.setText(no.substring(0,len-1));
                    }
                }
                return true;
            }
        });
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
        public void itemClick(View v, Order order) {
            int statu = order.getOrder_process();// 1为待配货 2为待出库 3为待回库  4为已回库  5为完成清点
            String id = order.getOrder_number();
            switch (statu) {
                case 1:
                    if (type == 0) {
                        onStartFragment(DeliverFragment.newInstance(id));
                    } else if (type == 1) {
                        onStartFragment(DeliverXsFragment.newInstance(id));
                    }
                    break;
                case 2:
                    if (type == 0) {
                        onStartFragment(WarehouseOutFragment.newInstance(id));
                    } else if (type == 1) {
                        onStartFragment(WarehouseOutXsFragment.newInstance(id));
                    }
                    break;
                case 3:
                    if (type == 0) {
                        onStartFragment(ToBeReturnedFragment.newInstance(id));
                    } else if (type == 1) {
                        onStartFragment(ToBeReturnedXsFragment.newInstance(id));
                    }
                    break;
                case 4:
                    if (type == 0) {
                        onStartFragment(ReturnedFragment.newInstance(id));
                    } else if (type == 1) {
                        onStartFragment(ReturnedXsFragment.newInstance(id));
                    }
                    break;
                case 5:
                    if (type == 0) {
                        onStartFragment(InventoryFragment.newInstance(id));
                    } else if (type == 1) {
                        onStartFragment(InventoryXsFragment.newInstance(id));
                    }
                    break;
            }
        }
    };

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sublist = WebHelper.init().getAllOrder(uid, no, page++, pagesize).getList();
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
