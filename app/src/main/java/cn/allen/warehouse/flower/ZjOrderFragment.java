package cn.allen.warehouse.flower;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import allen.frame.tools.Logger;
import allen.frame.tools.MsgUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.allen.warehouse.BaseFragment;
import cn.allen.warehouse.R;
import cn.allen.warehouse.adapter.ChoiceFlowerAdapter;
import cn.allen.warehouse.data.WebHelper;
import cn.allen.warehouse.entry.Flower;
import cn.allen.warehouse.entry.Order;
import cn.allen.warehouse.entry.ShowOrder;
import cn.allen.warehouse.utils.Constants;

public class ZjOrderFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.bar_notice)
    AppCompatImageView barNotice;
    @BindView(R.id.bar_name)
    AppCompatTextView barName;
    @BindView(R.id.back_bt)
    AppCompatImageView backBt;
    @BindView(R.id.bar_title)
    AppCompatTextView barTitle;

    @BindView(R.id.choice_rv)
    RecyclerView choiceRv;
    @BindView(R.id.main_order_no)
    AppCompatTextView mainOrderNo;
    @BindView(R.id.add_order_kh_name)
    AppCompatTextView addOrderKhName;
    @BindView(R.id.add_order_state)
    AppCompatTextView addOrderState;
    @BindView(R.id.add_order_kh_address)
    AppCompatTextView addOrderKhAddress;
    @BindView(R.id.add_order_hl_date)
    AppCompatTextView addOrderHlDate;
    @BindView(R.id.add_order_ck_date)
    AppCompatTextView addOrderCkDate;
    @BindView(R.id.add_order_hs_date)
    AppCompatTextView addOrderHsDate;
    @BindView(R.id.add_order_remark)
    AppCompatTextView addOrderRemark;
    @BindView(R.id.order_money)
    AppCompatTextView orderMoney;
    @BindView(R.id.order_commit)
    AppCompatButton orderCommit;
    @BindView(R.id.add_order_kh_phone)
    AppCompatTextView addOrderKhPhone;

    private ActivityHelper actHelper;
    private SharedPreferences shared;
    private int uid;
    private ChoiceFlowerAdapter adapter;
    private Calendar c;
    private AddReciver addReciver;
    private String no;
    private Order order;
    private float money;
    private String list;

    public static ZjOrderFragment init(String no) {
        ZjOrderFragment fragment = new ZjOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("no", no);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zjorder, container, false);
        actHelper = new ActivityHelper(getActivity(), view);
        unbinder = ButterKnife.bind(this, view);
        addReciver = new AddReciver();
        IntentFilter filter = new IntentFilter("add_flower");
        getActivity().registerReceiver(addReciver, filter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().unregisterReceiver(addReciver);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi(view);
        addEvent(view);
    }

    private void initUi(View view) {
        no = getArguments().getString("no");
        mainOrderNo.setText(no);
        c = Calendar.getInstance();
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.UserId, -1);
        barName.setText(shared.getString(Constants.UserName, "用户昵称"));
        barTitle.setText("请注意：追加订单必须在回收之前");
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new ChoiceFlowerAdapter();
        choiceRv.setLayoutManager(manager);
        choiceRv.setAdapter(adapter);
        loaddata();
    }


    private void addEvent(View view) {
        adapter.setOnItemClickListener(listener);
    }

    ChoiceFlowerAdapter.OnItemClickListener listener = new ChoiceFlowerAdapter.OnItemClickListener() {
        @Override
        public void deleteClick(View v, int position) {
            orderMoney.setText("¥" + adapter.getMonney());
        }

        @Override
        public void addClick(View v) {
            startActivity(new Intent(getActivity(), FlowerChoiceActivity.class).putExtra("choice", adapter.getList()));
        }

        @Override
        public void numEdit() {
            orderMoney.setText("¥" + adapter.getMonney());
        }
    };

    private void loaddata() {
        actHelper.showProgressDialog("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                order = WebHelper.init().getOrderByNo(handler, no);
                Logger.e("debug", "order+++++");
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    private boolean checkIsOk() {
        list = adapter.getChoice();
        money = adapter.getMonney();
        if (list.length() == 2) {
            MsgUtils.showMDMessage(getActivity(), "请选择鲜花!");
            return false;
        }
        if(!adapter.checkIsOk()){
            MsgUtils.showMDMessage(getActivity(),"预定数量还有未填的鲜花!");
            return false;
        }
        return true;
    }

    private void additionalOrder() {
        actHelper.showProgressDialog("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().additionalOrder(handler, no, money, list);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showShortToast(getActivity(), (String) msg.obj);
                    backPreFragment();
                    break;
                case -1:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showMDMessage(getActivity(), (String) msg.obj);
                    break;
                case 1:
                    actHelper.dismissProgressDialog();
                    if (order != null) {
                        addOrderKhName.setText(order.getCustomer_name());
                        addOrderKhPhone.setText("客户电话：" + order.getCustomer_phone());
                        addOrderKhAddress.setText("地址：" + order.getHotel_address());
                        addOrderHlDate.setText("婚礼时间：" + order.getWedding_date().substring(0, 10));
                        addOrderCkDate.setText("出库时间：" + order.getDelivery_time().substring(0, 10));
                        addOrderHsDate.setText("回收时间：" + order.getRecovery_date().substring(0, 10));
                        addOrderRemark.setText("备注信息：" + order.getRemark());
                        addOrderState.setText(new ShowOrder().getStatus(order.getOrder_process()));
                    } else {
                        MsgUtils.showMDMessage(getActivity(), "数据查询失败!");
                        backPreFragment();
                    }
                    break;
            }
        }
    };

    @OnClick({R.id.order_commit,R.id.back_bt})
    public void onViewClicked(View view) {
        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.order_commit:
                if (checkIsOk()) {
                    additionalOrder();
                }
                break;
            case R.id.back_bt:
                backPreFragment();
                break;
        }
        view.setEnabled(true);
    }

    class AddReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("add_flower")) {
                Flower flower = (Flower) intent.getSerializableExtra("flower");
                adapter.addList(flower);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        choiceRv.scrollToPosition(adapter.getItemCount() - 1);
                    }
                });
                orderMoney.setText("¥" + adapter.getMonney());
            }
        }
    }
}
