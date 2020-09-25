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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import allen.frame.adapter.CommonAdapter;
import allen.frame.adapter.ViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.allen.warehouse.R;
import cn.allen.warehouse.entry.Order;
import cn.allen.warehouse.utils.Constants;

public class OrderInfoFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.bar_back)
    AppCompatImageView barBack;
    @BindView(R.id.bar_title)
    AppCompatTextView barTitle;
    @BindView(R.id.bar_notice)
    AppCompatImageView barNotice;
    @BindView(R.id.bar_name)
    AppCompatTextView barName;
    @BindView(R.id.order_id)
    AppCompatTextView orderId;
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
    @BindView(R.id.tv_salesman)
    AppCompatTextView tvSalesman;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_submit)
    AppCompatTextView tvSubmit;
    @BindView(R.id.item_layout)
    CardView itemLayout;
    private SharedPreferences shared;
    private ActivityHelper actHelper;
    private CommonAdapter<Order> adapter;
    private List<Order> list, sublist;
    private int uid;
    private int state;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_SUCCES, "");

                    adapter.setDatas(list);
                    break;
                case -1:

                    break;
            }
        }
    };

    public OrderInfoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OrderInfoFragment newInstance(String param1, String param2) {
        OrderInfoFragment fragment = new OrderInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_order_info, container, false);
        actHelper = new ActivityHelper(getActivity(), view);
        unbinder = ButterKnife.bind(this, view);
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
        uid = shared.getInt(Constants.UserId, -1);
        barName.setText(shared.getString(Constants.UserName, "用户昵称"));
        initAdapter();
        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START, "");
        loadData();
    }

    private void loadData() {

    }

    private void initAdapter() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        adapter = new CommonAdapter<Order>(getContext(), R.layout.order_item_layout) {
            @Override
            public void convert(ViewHolder holder, Order entity, int position) {
                holder.setText(R.id.order_id, "订单号" + entity.getOrder_number());
                holder.setText(R.id.order_name, entity.getCustomer_name());
                holder.setText(R.id.tv_order_address, entity.getHotel_address());
                holder.setText(R.id.tv_order_out_time, entity.getDelivery_time());
                holder.setText(R.id.tv_order_back_time, entity.getRecovery_date());
                holder.setText(R.id.tv_salesman, entity.getNumber_name());

            }
        };
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adapter);
    }

    private void addEvent(View view) {
        adapter.setOnItemClickListener(listener);
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
}