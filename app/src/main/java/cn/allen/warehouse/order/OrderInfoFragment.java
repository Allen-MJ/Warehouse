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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import allen.frame.adapter.CommonAdapter;
import allen.frame.adapter.ViewHolder;
import allen.frame.tools.Logger;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.allen.warehouse.BaseFragment;
import cn.allen.warehouse.R;
import cn.allen.warehouse.data.WebHelper;
import cn.allen.warehouse.entry.OrderInfoXsEntity;
import cn.allen.warehouse.utils.Constants;

public class OrderInfoFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.bar_name)
    AppCompatTextView barName;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.back_bt)
    AppCompatImageView backBt;
    @BindView(R.id.bar_title)
    AppCompatTextView barTitle;
    @BindView(R.id.bar_notice)
    AppCompatImageView barNotice;
    @BindView(R.id.tv_name)
    AppCompatTextView tvName;
    @BindView(R.id.tv_address)
    AppCompatTextView tvAddress;

    public static final String ORDER_NUMBER = "orderNumber";
    @BindView(R.id.tv_total)
    AppCompatTextView tvTotal;
    @BindView(R.id.layout_total)
    LinearLayoutCompat layoutTotal;
    @BindView(R.id.tv_sh_total)
    AppCompatTextView tvShTotal;
    @BindView(R.id.layout_sh_total)
    LinearLayoutCompat layoutShTotal;
    @BindView(R.id.tv_sh_count)
    AppCompatTextView tvShCount;
    @BindView(R.id.tv_sh_rent)
    AppCompatTextView tvShRent;
    @BindView(R.id.item_layout)
    CardView itemLayout;
    @BindView(R.id.tv_sh_price)
    AppCompatTextView tvShPrice;
    private SharedPreferences shared;
    private ActivityHelper actHelper;
    private CommonAdapter<OrderInfoXsEntity.ChildrenBean> adapter;
    private List<OrderInfoXsEntity.ChildrenBean> list = new ArrayList<>();
    private OrderInfoXsEntity orderInfoXsEntity;
    private boolean isRefresh = false;
    private int page = 1;
    private int pagesize = 20;
    private int uid;
    private int type;//0为仓库管理员  1为销售员
    private String orderNo;
    private int state = 0;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_SUCCES, "");
                    state = orderInfoXsEntity.getOrder_process();// 1为待配货 2为待出库 3为待回库  4为已回库  5为完成清点
                    if (state == 5) {
                        layoutShTotal.setVisibility(View.VISIBLE);
                        tvShCount.setVisibility(View.VISIBLE);
                        tvShRent.setVisibility(View.VISIBLE);
                        tvShPrice.setVisibility(View.VISIBLE);
                        tvShTotal.setText("￥" + orderInfoXsEntity.getActual_loss_rent());
                    } else {
                        layoutShTotal.setVisibility(View.GONE);
                        tvShCount.setVisibility(View.GONE);
                        tvShRent.setVisibility(View.GONE);
                        tvShPrice.setVisibility(View.GONE);
                    }
                    tvTotal.setText("￥" + orderInfoXsEntity.getRent());
                    list = orderInfoXsEntity.getChildren();
                    tvAddress.setText("地址:" + orderInfoXsEntity.getHotel_address());
                    tvName.setText("客户姓名:" + orderInfoXsEntity.getCustomer_name());
                    int len = list == null ? 0 : list.size();
                    if (len == 0) {
                        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_FAIL, "");
                    }
                    adapter.setDatas(list);
                    break;
                case -1:

                    break;
            }
        }
    };


    public static OrderInfoFragment init(String orderNumber) {
        OrderInfoFragment fragment = new OrderInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_NUMBER, orderNumber);
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
        View view = inflater.inflate(R.layout.fragment_xs_order_info, container, false);
        actHelper = new ActivityHelper(getActivity(), view);
        unbinder = ButterKnife.bind(this, view);
        Bundle b = getArguments();
        if (b != null) {
            orderNo = b.getString(ORDER_NUMBER);
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
        Logger.e("debug", "OrderFragment:onResume");
    }

    private void initUi(View view) {
        shared = AllenManager.getInstance().getStoragePreference();
        type = shared.getInt(Constants.UserType, -1);
        uid = shared.getInt(Constants.UserId, -1);
        initAdapter();
        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START, "");
        loadData();
    }

    private void initAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = new CommonAdapter<OrderInfoXsEntity.ChildrenBean>(getContext(), R.layout.order_info_xs_item_layout) {
            @Override
            public void convert(ViewHolder holder, OrderInfoXsEntity.ChildrenBean entity, int position) {
                holder.setText(R.id.tv_number,position+1+"");
                holder.setText(R.id.tv_name, entity.getFlower_name());
                double price = entity.getRent();
                holder.setText(R.id.tv_price, price + "");
                int count = entity.getScheduled_quantity();
                holder.setText(R.id.tv_count, count + "");
                double total = price * count;
                holder.setText(R.id.tv_total, String.format("%.1f", total));
                if (state == 5) {
                    holder.setVisible(R.id.tv_sh_count, true);
                    holder.setVisible(R.id.tv_sh_rent, true);
                    holder.setVisible(R.id.tv_sh_price, true);
                    holder.setText(R.id.tv_sh_count, entity.getLoss_quantity() + "");
                    holder.setText(R.id.tv_sh_price, entity.getLoss_rent() + "");
                    double r = entity.getLoss_rent() * entity.getLoss_quantity();
                    holder.setText(R.id.tv_sh_rent, String.format("%.1f", r));
                } else {
                    holder.setVisible(R.id.tv_sh_count, false);
                    holder.setVisible(R.id.tv_sh_rent, false);
                    holder.setVisible(R.id.tv_sh_price, false);
                }

            }
        };
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adapter);
    }

    private void addEvent(View view) {
    }

    private void loadData() {
        new Thread() {
            @Override
            public void run() {
                orderInfoXsEntity = WebHelper.init().getOrderInfoXsByNo(handler, orderNo);
                handler.sendEmptyMessage(0);
            }
        }.start();

    }

    @OnClick({R.id.bar_notice, R.id.bar_name, R.id.back_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_bt:
                backPreFragment();
                break;
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