package cn.allen.warehouse.order;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import allen.frame.adapter.CommonAdapter;
import allen.frame.adapter.ViewHolder;
import allen.frame.tools.MsgUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.allen.warehouse.BaseFragment;
import cn.allen.warehouse.R;
import cn.allen.warehouse.data.WebHelper;
import cn.allen.warehouse.entry.OrderInfoEntity;
import cn.allen.warehouse.flower.XGOrderFragment;
import cn.allen.warehouse.flower.XsOrderFragment;
import cn.allen.warehouse.utils.Constants;

public class WarehouseOutXsFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.back_bt)
    AppCompatImageView barBack;
    @BindView(R.id.bar_title)
    AppCompatTextView barTitle;
    @BindView(R.id.bar_notice)
    AppCompatImageView barNotice;
    @BindView(R.id.bar_name)
    AppCompatTextView barName;
    @BindView(R.id.order_name)
    AppCompatTextView orderName;
    @BindView(R.id.order_state)
    AppCompatTextView orderState;
    @BindView(R.id.tv_order_address)
    AppCompatTextView tvOrderAddress;
    @BindView(R.id.tv_order_out_time)
    AppCompatTextView tvOrderOutTime;
    @BindView(R.id.tv_order_back_time)
    AppCompatTextView tvOrderBackTime;
    @BindView(R.id.tv_submit)
    AppCompatButton tvSubmit;
    @BindView(R.id.item_layout)
    CardView itemLayout;
    @BindView(R.id.recyclerview_children)
    RecyclerView recyclerviewChildren;
    @BindView(R.id.recyclerview_main)
    RecyclerView recyclerviewMain;
    @BindView(R.id.tv_children_title)
    AppCompatTextView tvChildrenTitle;
    @BindView(R.id.layout_children)
    LinearLayoutCompat layoutChildren;
    @BindView(R.id.tv_main_title)
    AppCompatTextView tvMainTitle;
    @BindView(R.id.layout_main)
    LinearLayoutCompat layoutMain;
    @BindView(R.id.tv_order_id)
    AppCompatTextView tvOrderId;
    @BindView(R.id.layout_bottom)
    LinearLayoutCompat layoutBottom;
    @BindView(R.id.tv_phone)
    AppCompatTextView tvPhone;
    @BindView(R.id.tv_date1)
    AppCompatTextView tvDate1;
    @BindView(R.id.tv_remark)
    AppCompatTextView tvRemark;
    private SharedPreferences shared;
    private ActivityHelper actHelper;
    private CommonAdapter<OrderInfoEntity.ChildrenBean> childrenAdapter;
    private CommonAdapter<OrderInfoEntity.MainchildrenBean> mainAdapter;
    private List<OrderInfoEntity.ChildrenBean> childrenList;
    private List<OrderInfoEntity.MainchildrenBean> mainList;
    private String numberID;
    private OrderInfoEntity orderInfoEntity;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    actHelper.dismissProgressDialog();
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_SUCCES, "");
                    tvOrderAddress.setText(orderInfoEntity.getHotel_address());
                    tvOrderId.setText("订单号:" + orderInfoEntity.getOrder_number());
                    orderName.setText(orderInfoEntity.getCustomer_name());
                    tvOrderOutTime.setText(orderInfoEntity.getDelivery_times());
                    tvOrderBackTime.setText(orderInfoEntity.getRecovery_dates());
                    tvPhone.setText(orderInfoEntity.getCustomer_phone());
                    tvDate1.setText(orderInfoEntity.getWedding_dates());
                    tvRemark.setText(orderInfoEntity.getRemark());
                    int statu = orderInfoEntity.getCollect_status();// 1为待配货 2为待出库 3为待回库  4为已回库  5为完成清点
                    switch (statu) {
                        case 1:
                            orderState.setText("待配货");
                            break;
                        case 2:
                            orderState.setText("待出库");
                            break;
                        case 3:
                            orderState.setText("待回库");
                            break;
                        case 4:
                            orderState.setText("已回库");
                            break;
                        case 5:
                            orderState.setText("完成清点");
                            break;
                    }
                    childrenList = orderInfoEntity.getChildren();
                    if (childrenList == null || childrenList.isEmpty()) {
                        layoutChildren.setVisibility(View.GONE);
                    } else {
                        layoutChildren.setVisibility(View.VISIBLE);
                        childrenAdapter.setDatas(childrenList);
                    }
                    mainList = orderInfoEntity.getMainchildren();
                    if (mainList == null || mainList.isEmpty()) {
                        layoutMain.setVisibility(View.GONE);
                    } else {
                        layoutMain.setVisibility(View.VISIBLE);
                        mainAdapter.setDatas(mainList);
                    }

                    break;
                case 101:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showLongToast(getContext(), (String) msg.obj);
                    loadData();
                    break;
                case -1:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showLongToast(getContext(), (String) msg.obj);
                    break;
            }
        }
    };

    public WarehouseOutXsFragment() {
    }

    public static WarehouseOutXsFragment newInstance(String numberID) {
        WarehouseOutXsFragment fragment = new WarehouseOutXsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ID", numberID);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_warehouse_out_xs, container, false);
        actHelper = new ActivityHelper(getActivity(), view);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            numberID = bundle.getString("ID");
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUi(view);
        addEvent(view);
    }

    private void initUi(View view) {
        shared = AllenManager.getInstance().getStoragePreference();
        barName.setText(shared.getString(Constants.UserName, "用户昵称"));
        initAdapter();
        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START, "");
        loadData();
    }

    private void loadData() {
        new Thread() {
            @Override
            public void run() {
                orderInfoEntity = WebHelper.init().getOrderInfo(numberID);
                handler.sendEmptyMessage(0);
            }
        }.start();
    }


    private void initAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        childrenAdapter = new CommonAdapter<OrderInfoEntity.ChildrenBean>(getContext(), R.layout.order_info_item_layout) {
            @Override
            public void convert(ViewHolder holder, OrderInfoEntity.ChildrenBean entity, int position) {
                holder.setText(R.id.tv_name, entity.getFlower_name());
                holder.setText(R.id.tv_count, "数量:" + entity.getScheduled_quantity());
                int status = entity.getId_check();
                if (status == 0) {
                    holder.setVisible(R.id.btn_submit, true);
                    holder.setVisible(R.id.tv_submit, false);
                } else {
                    holder.setVisible(R.id.btn_submit, false);
                    holder.setVisible(R.id.tv_submit, true);
                }
                holder.setOnClickListener(R.id.btn_submit, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
            }
        };
        recyclerviewChildren.setLayoutManager(manager);
        recyclerviewChildren.setAdapter(childrenAdapter);

        mainAdapter = new CommonAdapter<OrderInfoEntity.MainchildrenBean>(getContext(), R.layout.order_info_item_layout) {
            @Override
            public void convert(ViewHolder holder, OrderInfoEntity.MainchildrenBean entity, int position) {
                holder.setText(R.id.tv_name, entity.getFlower_name());
                holder.setText(R.id.tv_count, "数量:" + entity.getScheduled_quantity());
                int status = entity.getId_check();
                if (status == 0) {
                    holder.setVisible(R.id.btn_submit, true);
                    holder.setVisible(R.id.tv_submit, false);
                } else {
                    holder.setVisible(R.id.btn_submit, false);
                    holder.setVisible(R.id.tv_submit, true);
                }
                holder.setOnClickListener(R.id.btn_submit, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
            }
        };
        recyclerviewMain.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerviewMain.setAdapter(mainAdapter);
    }

    private void addEvent(View view) {
    }


    private CommonAdapter.OnItemClickListener listener = new CommonAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
            return false;
        }
    };

    @OnClick({R.id.tv_submit, R.id.back_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                onStartFragment(XGOrderFragment.init(numberID));
                break;
            case R.id.back_bt:
                backPreFragment();
                break;
        }
    }
}