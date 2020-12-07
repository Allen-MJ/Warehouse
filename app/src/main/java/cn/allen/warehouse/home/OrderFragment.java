package cn.allen.warehouse.home;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import allen.frame.tools.Logger;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import allen.frame.adapter.CommonAdapter;
import allen.frame.adapter.ViewHolder;
import allen.frame.widget.MaterialRefreshLayout;
import allen.frame.widget.MaterialRefreshListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.allen.warehouse.BaseFragment;
import cn.allen.warehouse.R;
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

public class OrderFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.bar_search)
    SearchEditText barSearch;
    @BindView(R.id.bar_notice)
    AppCompatImageView barNotice;
    @BindView(R.id.bar_name)
    AppCompatTextView barName;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    MaterialRefreshLayout refreshLayout;

    private SharedPreferences shared;
    private ActivityHelper actHelper;
    private CommonAdapter<Order> adapter;
    private List<Order> list, sublist;
    private boolean isRefresh = false;
    private int page = 1;
    private int pagesize = 20;
    private int uid;
    private int type;//0为仓库管理员  1为销售员
    private int state;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_SUCCES, "");

                    if (isRefresh) {
                        list = sublist;
                        refreshLayout.finishRefresh();
                    } else {
                        if (page == 2) {
                            list = sublist;
                        } else {
                            list.addAll(sublist);
                        }
                        refreshLayout.finishRefreshLoadMore();
                    }
                    if (list.size() == 0) {
                        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_FAIL, "");
                    }
                    actHelper.setCanLoadMore(refreshLayout, pagesize, list);
                    adapter.setDatas(list);
                    break;
                case -1:

                    break;
            }
        }
    };


    public static OrderFragment init(int state) {
        OrderFragment fragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("state", state);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        actHelper = new ActivityHelper(getActivity(), view);
        unbinder = ButterKnife.bind(this, view);
        Bundle b = getArguments();
        if (b != null) {
            state = b.getInt("state");
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUi(view);
        addEvent(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.e("debug","OrderFragment:onResume");
    }

    private void initUi(View view) {
        Logger.e("debug","OrderFragment:initUi");
        shared = AllenManager.getInstance().getStoragePreference();
        type = shared.getInt(Constants.UserType, -1);
        uid = shared.getInt(Constants.UserId, -1);
        barSearch.setVisibility(View.INVISIBLE);
        barName.setText(shared.getString(Constants.UserName, "用户昵称"));
        initAdapter();
        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START, "");
        loadData();
    }

    private void initAdapter() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        adapter = new CommonAdapter<Order>(getContext(), R.layout.order_item_layout) {
            @Override
            public void convert(ViewHolder holder, Order entity, int position) {
                holder.setText(R.id.order_id, "订单号" + entity.getOrder_number());
                holder.setText(R.id.order_name, entity.getCustomer_name());
                holder.setText(R.id.tv_order_address, entity.getHotel_address());
                holder.setText(R.id.tv_order_out_time, entity.getDelivery_times().substring(0,10));
                holder.setText(R.id.tv_order_back_time, entity.getRecovery_dates().substring(0,10));
                holder.setText(R.id.tv_salesman, entity.getNumber_name());
                int statu = entity.getOrder_process();// 1为待配货 2为待出库 3为待回库  4为已回库  5为完成清点
                switch (statu) {
                    case 1:
                        holder.setText(R.id.order_state, "待配货");
                        holder.setDrawableLeft(R.id.order_state, getActivity().getResources().getDrawable(R.mipmap.ic_logo_06));
                        break;
                    case 2:
                        holder.setText(R.id.order_state, "待出库");
                        holder.setDrawableLeft(R.id.order_state, getActivity().getResources().getDrawable(R.mipmap.ic_logo_02));
                        break;
                    case 3:
                        holder.setText(R.id.order_state, "待回库");
                        holder.setDrawableLeft(R.id.order_state, getActivity().getResources().getDrawable(R.mipmap.ic_logo_04));
                        break;
                    case 4:
                        holder.setText(R.id.order_state, "已回库");
                        holder.setDrawableLeft(R.id.order_state, getActivity().getResources().getDrawable(R.mipmap.ic_logo_28));
                        break;
                    case 5:
                        holder.setText(R.id.order_state, "完成清点");
                        holder.setDrawableLeft(R.id.order_state, getActivity().getResources().getDrawable(R.mipmap.ic_logo_16));
                        if (entity.getIs_ornot()==1){
                            holder.setText(R.id.order_state, "已结账");
                            holder.setTextColor(R.id.order_state,getResources().getColor(R.color.state_text_color4));
                            holder.setDrawableLeft(R.id.order_state, getActivity().getResources().getDrawable(R.mipmap.ic_yjz));
                        }
                        break;

                }
            }
        };
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adapter);
    }

    private void addEvent(View view) {
        refreshLayout.setMaterialRefreshListener(materListener);
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

    private CommonAdapter.OnItemClickListener listener = new CommonAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
            Order order = list.get(position);
            int statu = order.getOrder_process();// 1为待配货 2为待出库 3为待回库  4为已回库  5为完成清点
            String id = order.getOrder_number();
            Logger.e("type:",type+"");
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

        @Override
        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
            return false;
        }
    };

    private void loadData() {
        new Thread() {
            @Override
            public void run() {
                sublist = WebHelper.init().getOrderBystate(uid, state, page++, pagesize).getList();
                handler.sendEmptyMessage(0);
            }
        }.start();

    }


    @OnClick({R.id.bar_notice, R.id.bar_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bar_notice:
                break;
            case R.id.bar_name:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}