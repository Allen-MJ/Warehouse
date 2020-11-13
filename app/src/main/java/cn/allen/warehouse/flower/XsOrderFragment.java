package cn.allen.warehouse.flower;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.FileFilter;
import java.util.Calendar;

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

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
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
import cn.allen.warehouse.utils.Constants;
import cn.allen.warehouse.utils.DateTimePickerDialog;

public class XsOrderFragment extends BaseFragment {
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
    private ActivityHelper actHelper;
    private SharedPreferences shared;
    private int uid;
    private ChoiceFlowerAdapter adapter;
    private Calendar c;
    private String customerName, hotelAddress, customerPhone, weddingDate,deliveryTime, recoveryDate, remark;
    private float money;
    private String list;
    private AddReciver addReciver;
    public static XsOrderFragment init() {
        XsOrderFragment fragment = new XsOrderFragment();
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
        getActivity().registerReceiver(addReciver,filter);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        save();
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

    private String preOrder;
    private void initUi(View view) {
        c = Calendar.getInstance();
        shared = AllenManager.getInstance().getStoragePreference();
        preOrder = shared.getString("preOrder","");
        uid = shared.getInt(Constants.UserId, -1);
        barName.setText(shared.getString(Constants.UserName, "用户昵称"));
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new ChoiceFlowerAdapter();
        choiceRv.setLayoutManager(manager);
        choiceRv.setAdapter(adapter);
    }

    private void addEvent(View view) {
        adapter.setOnItemClickListener(listener);
        if(StringUtils.notEmpty(preOrder)){
            MsgUtils.showMDMessage(getActivity(), "提示", "上次下单还未提交，是否继续?", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            }, "继续", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    Order order = new Gson().fromJson(preOrder,Order.class);
                    orderKhName.setText(order.getCustomer_name());
                    orderKhAddress.setText(order.getHotel_address());
                    orderKhPhone.setText(order.getCustomer_phone());
                    orderDateHl.setText(order.getWedding_date());
                    orderDateCk.setText(order.getDelivery_times());
                    orderDateHs.setText(order.getRecovery_date());
                    orderRemark.setText(order.getRemark());
                    adapter.addList(order.getMainchildren());
                }
            });
        }
    }

    ChoiceFlowerAdapter.OnItemClickListener listener = new ChoiceFlowerAdapter.OnItemClickListener() {
        @Override
        public void deleteClick(View v, int position) {
            orderMoney.setText("¥"+adapter.getMonney());
        }

        @Override
        public void addClick(View v) {
            startActivity(new Intent(getActivity(),FlowerChoiceActivity.class).putExtra("choice",adapter.getList()));
        }

        @Override
        public void numEdit() {
            orderMoney.setText("¥"+adapter.getMonney());
        }
    };

    @OnClick({R.id.back_bt,R.id.order_date_hl, R.id.order_date_ck, R.id.order_date_hs, R.id.order_commit})
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
                if(checkIsOk()){
                    placingOrder();
                }
                break;
        }
    }

    private void placingOrder(){
        actHelper.showProgressDialog("正在提交，请稍等...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().placingOrder(handler,customerName, hotelAddress, customerPhone, weddingDate,deliveryTime, recoveryDate, remark,uid,money,list);
            }
        }).start();
    }

    private boolean checkIsOk(){
//        customerName, hotelAddress, customerPhone, weddingDate,deliveryTime, recoveryDate, remark,numberId,money,list
        customerName = orderKhName.getText().toString().trim();
        hotelAddress = orderKhAddress.getText().toString().trim();
        customerPhone = orderKhPhone.getText().toString().trim();
        weddingDate = orderDateHl.getText().toString().trim();
        deliveryTime = orderDateCk.getText().toString().trim();
        recoveryDate = orderDateHs.getText().toString().trim();
        remark = orderRemark.getText().toString().trim();
        list = adapter.getChoice();
        money = adapter.getMonney();
        if(StringUtils.empty(customerName)){
            MsgUtils.showMDMessage(getActivity(),"请输入客户姓名!");
            return false;
        }
        if(StringUtils.empty(hotelAddress)){
            MsgUtils.showMDMessage(getActivity(),"请输入酒店地址!");
            return false;
        }
        if(StringUtils.empty(customerPhone)){
            MsgUtils.showMDMessage(getActivity(),"请输入客户电话!");
            return false;
        }
        if (!CheckUtils.phoneIsOk(customerPhone)) {
            MsgUtils.showMDMessage(getActivity(), "请输入正确的手机号码!");
            return false;
        }
        if(StringUtils.empty(weddingDate)){
            MsgUtils.showMDMessage(getActivity(),"请输入婚礼日期!");
            return false;
        }
        if(StringUtils.empty(deliveryTime)){
            MsgUtils.showMDMessage(getActivity(),"请输入出库日期!");
            return false;
        }
        if(StringUtils.empty(recoveryDate)){
            MsgUtils.showMDMessage(getActivity(),"请输入回收日期!");
            return false;
        }
        if(list.length()<=2){
            MsgUtils.showMDMessage(getActivity(),"请选择鲜花!");
            return false;
        }
        if(!adapter.checkIsOk()){
            MsgUtils.showMDMessage(getActivity(),"预定数量还有未填的鲜花!");
            return false;
        }

        return true;
    }

    private void save(){
        customerName = orderKhName.getText().toString().trim();
        hotelAddress = orderKhAddress.getText().toString().trim();
        customerPhone = orderKhPhone.getText().toString().trim();
        weddingDate = orderDateHl.getText().toString().trim();
        deliveryTime = orderDateCk.getText().toString().trim();
        recoveryDate = orderDateHs.getText().toString().trim();
        remark = orderRemark.getText().toString().trim();
        list = adapter.getChoice();
        money = adapter.getMonney();
        boolean isNochange = true;
        isNochange = isNochange&&StringUtils.empty(customerName);
        isNochange = isNochange&&StringUtils.empty(hotelAddress);
        isNochange = isNochange&&StringUtils.empty(customerPhone);
        isNochange = isNochange&&StringUtils.empty(weddingDate);
        isNochange = isNochange&&StringUtils.empty(deliveryTime);
        isNochange = isNochange&&StringUtils.empty(recoveryDate);
        isNochange = isNochange&&(list.length()<=2);
        if(!isOK&&!isNochange){
            Order order = new Order();
            order.setCustomer_name(customerName);
            order.setHotel_address(hotelAddress);
            order.setCustomer_phone(customerPhone);
            order.setWedding_date(weddingDate);
            order.setDelivery_times(deliveryTime);
            order.setRecovery_date(recoveryDate);
            order.setRemark(remark);
            order.setMainchildren(adapter.getList());
            String save = new Gson().toJson(order);
            Logger.e("debug","save:"+save);
            shared.edit().putString("preOrder",save).apply();
        }else{
            shared.edit().putString("preOrder","").apply();
        }
    }

    private boolean isOK = false;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showShortToast(getActivity(), (String) msg.obj);
                    isOK = true;
                    backPreFragment();
                    break;
                case -1:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showMDMessage(getActivity(), (String) msg.obj);
                    break;
            }
        }
    };

    class AddReciver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("add_flower")){
                Flower flower = (Flower) intent.getSerializableExtra("flower");
                adapter.addList(flower);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        choiceRv.scrollToPosition(adapter.getItemCount()-1);
                    }
                });
                orderMoney.setText("¥"+adapter.getMonney());
            }
        }
    }
}
