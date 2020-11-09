package cn.allen.warehouse.flower;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import allen.frame.tools.CheckUtils;
import allen.frame.tools.Logger;
import allen.frame.tools.MsgUtils;
import allen.frame.tools.StringUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
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
import cn.allen.warehouse.order.DeliverXsFragment;
import cn.allen.warehouse.order.ToBeReturnedFragment;
import cn.allen.warehouse.order.ToBeReturnedXsFragment;
import cn.allen.warehouse.order.WarehouseOutFragment;
import cn.allen.warehouse.order.WarehouseOutXsFragment;
import cn.allen.warehouse.utils.Constants;
import cn.allen.warehouse.utils.DateTimePickerDialog;

public class XGOrderFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.bar_notice)
    AppCompatImageView barNotice;
    @BindView(R.id.bar_name)
    AppCompatTextView barName;
    @BindView(R.id.back_bt)
    AppCompatImageView backBt;
    @BindView(R.id.bar_title)
    AppCompatTextView barTitle;
    @BindView(R.id.order_kh_name)
    AppCompatEditText orderKhName;
    @BindView(R.id.order_kh_address)
    AppCompatEditText orderKhAddress;
    @BindView(R.id.order_kh_phone)
    AppCompatEditText orderKhPhone;
    @BindView(R.id.order_date_hl)
    AppCompatTextView orderDateHl;
    @BindView(R.id.order_date_ck)
    AppCompatTextView orderDateCk;
    @BindView(R.id.order_date_hs)
    AppCompatTextView orderDateHs;
    @BindView(R.id.order_remark)
    AppCompatEditText orderRemark;
    @BindView(R.id.choice_rv)
    RecyclerView choiceRv;
    @BindView(R.id.order_money)
    AppCompatTextView orderMoney;
    @BindView(R.id.order_commit)
    AppCompatButton orderCommit;
    @BindView(R.id.base_update)
    AppCompatTextView baseUpdate;
    @BindView(R.id.date_update)
    AppCompatTextView dateUpdate;
    @BindView(R.id.remark_update)
    AppCompatTextView remarkUpdate;
    @BindView(R.id.flower_update)
    AppCompatTextView flowerUpdate;
    @BindView(R.id.delete)
    AppCompatTextView delete;
    private ActivityHelper actHelper;
    private SharedPreferences shared;
    private int uid;
    private ChoiceFlowerAdapter adapter;
    private Calendar c;
    private String customerName, hotelAddress, customerPhone, weddingDate, deliveryTime, recoveryDate, remark;
    private float money;
    private String list;
    private AddReciver addReciver;
    private String no;
    private Order order;

    public static XGOrderFragment init(String orderno) {
        XGOrderFragment fragment = new XGOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("no", orderno);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xs_order, container, false);
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
        c = Calendar.getInstance();
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.UserId, -1);
        barName.setText(shared.getString(Constants.UserName, "用户昵称"));
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new ChoiceFlowerAdapter();
        choiceRv.setLayoutManager(manager);
        choiceRv.setAdapter(adapter);
        baseUpdate.setVisibility(View.VISIBLE);
        dateUpdate.setVisibility(View.VISIBLE);
        remarkUpdate.setVisibility(View.VISIBLE);
        flowerUpdate.setVisibility(View.VISIBLE);
        setBaseUpdate(true);
        setDateUpdate(true);
        setRemarkUpdate(true);
        loaddata();
    }

    private void setBaseUpdate(boolean isUpdate){
        orderKhName.setEnabled(isUpdate);
        orderKhAddress.setEnabled(isUpdate);
        orderKhPhone.setEnabled(isUpdate);
    }

    private void setDateUpdate(boolean isUpdate){
        orderDateHl.setEnabled(isUpdate);
        orderDateCk.setEnabled(isUpdate);
        orderDateHs.setEnabled(isUpdate);
    }

    private void setRemarkUpdate(boolean isUpdate){
        orderRemark.setEnabled(isUpdate);
    }


    private void addEvent(View view) {
        adapter.setOnItemClickListener(listener);
    }

    private Flower deleteFlower;
    ChoiceFlowerAdapter.OnItemClickListener listener = new ChoiceFlowerAdapter.OnItemClickListener() {
        @Override
        public void deleteClick(View v, int position) {
            actHelper.showProgressDialog("");
            deleteFlower=order.getMainchildren().get(position);
            delete(deleteFlower.getFlower_id());
            orderMoney.setText("¥" + adapter.getMonney());

        }

        @Override
        public void addClick(View v) {
            startActivity(new Intent(getActivity(), FlowerChoiceActivity.class).putExtra("choice", adapter.getList()));
        }

        @Override
        public void numEdit(View v) {
            orderMoney.setText("¥" + adapter.getMonney());
        }
    };

    @OnClick({R.id.back_bt, R.id.order_date_hl, R.id.order_date_ck, R.id.order_date_hs, R.id.order_commit,R.id.base_update,
            R.id.date_update, R.id.remark_update, R.id.flower_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_bt:
                backPreFragment();
                break;
            case R.id.order_date_hl:
                DateTimePickerDialog hl = new DateTimePickerDialog(getActivity(),
                        new DateTimePickerDialog.OnDateTimeChangeListener() {
                            @Override
                            public void onDateTimeSet(int year, int month, int day, int hour, int minute, int second) {
                                weddingDate = year + "-" + (month + 1 > 9 ? month + 1 : "0" + (month + 1)) + "-" + (day > 9 ? day : "0" + day);
                                orderDateHl.setText(weddingDate);
                                deliveryTime = "";
                                orderDateCk.setText(deliveryTime);
                                recoveryDate = "";
                                orderDateHs.setText(recoveryDate);
                            }
                        },0);
                hl.show();
                break;
            case R.id.order_date_ck:
                DateTimePickerDialog ck = new DateTimePickerDialog(getActivity(), new DateTimePickerDialog.OnDateTimeChangeListener() {
                    @Override
                    public void onDateTimeSet(int year, int month, int day, int hour, int minute, int second) {
                        deliveryTime = year + "-" + (month + 1 > 9 ? month + 1 : "0" + (month + 1)) + "-" + (day > 9 ? day : "0" + day)
                                +" "+(hour>9?hour:"0"+hour)+":"+(minute>9?minute:"0"+minute)+":"+(second>9?second:"0"+second);
                        if(CheckUtils.timeIsBefore(deliveryTime,recoveryDate)&&CheckUtils.timeIsBefore(deliveryTime,weddingDate)){
                            orderDateCk.setText(deliveryTime);
                        }else{
                            deliveryTime = orderDateCk.getText().toString().trim();
                            MsgUtils.showMDMessage(getActivity(),"出库时间不能晚于回收时间以及婚礼时间!");
                        }
                    }
                },1);
                ck.show();
                break;
            case R.id.order_date_hs:
                DateTimePickerDialog hs = new DateTimePickerDialog(getActivity(),
                        new DateTimePickerDialog.OnDateTimeChangeListener() {
                            @Override
                            public void onDateTimeSet(int year, int month, int day, int hour, int minute, int second) {
                                recoveryDate = year + "-" + (month + 1 > 9 ? month + 1 : "0" + (month + 1)) + "-" + (day > 9 ? day : "0" + day);
                                if(CheckUtils.timeIsAfter(recoveryDate,deliveryTime)&&CheckUtils.timeIsAfter(recoveryDate,weddingDate)){
                                    orderDateHs.setText(recoveryDate);
                                }else{
                                    recoveryDate = orderDateHs.getText().toString().trim();
                                    MsgUtils.showMDMessage(getActivity(),"回收时间不能早于出库时间以及婚礼时间!");
                                }
                            }
                        },0);
                hs.show();
                break;
            case R.id.order_commit:
                if (checkIsOk()) {
                    updateOrder();
                }
                break;
            case R.id.base_update:
                setBaseUpdate(true);
                break;
            case R.id.date_update:
                setDateUpdate(true);
                break;
            case R.id.remark_update:
                setRemarkUpdate(true);
                break;
            case R.id.flower_update:
                break;
        }
    }

    private void delete(int id){
        new Thread(){
            @Override
            public void run() {
                WebHelper.init().delete(handler,no,id);
            }
        }.start();
    }

    private void updateOrder() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().updateOrder(handler, no, customerName, hotelAddress, customerPhone, weddingDate, deliveryTime, recoveryDate, remark, uid, money, list);
            }
        }).start();
    }

    private void loaddata(){
        actHelper.showProgressDialog("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                order = WebHelper.init().getOrderByNo(handler,no);
                Logger.e("debug","order+++++");
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    private boolean checkIsOk() {
//        customerName, hotelAddress, customerPhone, weddingDate,deliveryTime, recoveryDate, remark,numberId,money,list
        customerName = orderKhName.getText().toString().trim();
        hotelAddress = orderKhAddress.getText().toString().trim();
        customerPhone = orderKhPhone.getText().toString().trim();
        weddingDate = orderDateHl.getText().toString().trim();
        deliveryTime = orderDateCk.getText().toString().trim();
        recoveryDate = orderDateHs.getText().toString().trim();
        remark = orderRemark.getText().toString().trim();
        list = adapter.getXGChoice();
        money = adapter.getMonney();
        if (StringUtils.empty(customerName)) {
            MsgUtils.showMDMessage(getActivity(), "请输入客户姓名!");
            return false;
        }
        if (StringUtils.empty(hotelAddress)) {
            MsgUtils.showMDMessage(getActivity(), "请输入酒店地址!");
            return false;
        }
        if (StringUtils.empty(customerPhone)) {
            MsgUtils.showMDMessage(getActivity(), "请输入客户电话!");
            return false;
        }
        if (!CheckUtils.phoneIsOk(customerPhone)) {
            MsgUtils.showMDMessage(getActivity(), "请输入正确的手机号码!");
            return false;
        }
        if (StringUtils.empty(weddingDate)) {
            MsgUtils.showMDMessage(getActivity(), "请输入婚礼日期!");
            return false;
        }
        if (StringUtils.empty(deliveryTime)) {
            MsgUtils.showMDMessage(getActivity(), "请输入出库日期!");
            return false;
        }
        if (StringUtils.empty(recoveryDate)) {
            MsgUtils.showMDMessage(getActivity(), "请输入回收日期!");
            return false;
        }
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

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showShortToast(getActivity(), (String) msg.obj);
                    Logger.e(".....",msg.arg1+"");
                    if (msg.arg1==1){
                        onStartFragment(DeliverXsFragment.newInstance(no));
                    }else {
                        backPreFragment();
                    }
                    break;
                case -1:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showMDMessage(getActivity(), (String) msg.obj);
                    break;
                case 1:
                    actHelper.dismissProgressDialog();
                    if(order!=null){
                        orderKhName.setText(order.getCustomer_name());
                        orderKhAddress.setText(order.getHotel_address());
                        orderKhPhone.setText(order.getCustomer_phone());
                        orderDateHl.setText(order.getWedding_date().substring(0,10));
                        orderDateCk.setText(order.getDelivery_times());
                        orderDateHs.setText(order.getRecovery_date().substring(0,10));
                        orderRemark.setText(order.getRemark());
                        adapter.addList(order.getMainchildren());
                        orderMoney.setText("¥" + adapter.getMonney());
                    }else{
                        MsgUtils.showMDMessage(getActivity(),"数据查询失败!");
                        backPreFragment();
                    }
                    break;
                case 2:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showLongToast(getActivity(), (String) msg.obj);
                    break;
                case -2:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showLongToast(getActivity(), (String) msg.obj);
                    adapter.addList(deleteFlower);
                    break;
            }
        }
    };

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
