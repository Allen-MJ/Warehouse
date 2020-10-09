package cn.allen.warehouse.home;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import allen.frame.AllenManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.allen.warehouse.BaseFragment;
import cn.allen.warehouse.R;
import cn.allen.warehouse.adapter.NoticeAdapter;
import cn.allen.warehouse.adapter.ShowOrderAdapter;
import cn.allen.warehouse.data.WebHelper;
import cn.allen.warehouse.entry.Notice;
import cn.allen.warehouse.entry.ShowOrder;
import cn.allen.warehouse.flower.XsOrderFragment;
import cn.allen.warehouse.order.DeliverFragment;
import cn.allen.warehouse.order.DeliverXsFragment;
import cn.allen.warehouse.order.ReturnedFragment;
import cn.allen.warehouse.order.ReturnedXsFragment;
import cn.allen.warehouse.order.ToBeReturnedFragment;
import cn.allen.warehouse.order.ToBeReturnedXsFragment;
import cn.allen.warehouse.order.WarehouseOutFragment;
import cn.allen.warehouse.order.WarehouseOutXsFragment;
import cn.allen.warehouse.utils.Constants;
import cn.allen.warehouse.widget.SearchEditText;

public class SaleHomeFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.bar_search)
    SearchEditText barSearch;
    @BindView(R.id.bar_notice)
    AppCompatImageView barNotice;
    @BindView(R.id.bar_name)
    AppCompatTextView barName;
    @BindView(R.id.start_date)
    AppCompatTextView startDate;
    @BindView(R.id.end_date)
    AppCompatTextView endDate;
    @BindView(R.id.show_rv)
    RecyclerView showRv;
    @BindView(R.id.notice_rv)
    RecyclerView noticeRv;
    @BindView(R.id.oder_bt)
    CardView oderBt;
    private ShowOrderAdapter adapter;
    private List<ShowOrder> showOrders;
    private SharedPreferences shared;
    private NoticeAdapter noticeAdapter;
    private List<Notice> notices;
    private Calendar c;
    private int type;

    public static SaleHomeFragment init() {
        SaleHomeFragment fragment = new SaleHomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_home, container, false);
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
        c = Calendar.getInstance();
        shared = AllenManager.getInstance().getStoragePreference();
        type = shared.getInt(Constants.UserType,-1);
        if(type==1){
            oderBt.setVisibility(View.VISIBLE);
        }else{
            oderBt.setVisibility(View.GONE);
        }
        barName.setText(shared.getString(Constants.UserName, "用户昵称"));
        adapter = new ShowOrderAdapter();
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        showRv.setLayoutManager(manager);
        showRv.setAdapter(adapter);
        GridLayoutManager manager1 = new GridLayoutManager(getActivity(), 2);
        noticeRv.setLayoutManager(manager1);
        noticeAdapter = new NoticeAdapter();
        noticeRv.setAdapter(noticeAdapter);
        loadcount();
        loadMsg();
    }

    private void addEvent(View view) {
        adapter.setOnItemClickListener(new ShowOrderAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View v, ShowOrder entry) {
                if (OnItemMenuClickLisenter != null) {
                    OnItemMenuClickLisenter.itemMenu(entry);
                }
            }
        });
        barSearch.setOnSerchListenner(new SearchEditText.onSerchListenner() {
            @Override
            public void onSerchEvent() {
                String no = barSearch.getText().toString().trim();
                onStartFragment(AllOrderFragment.init(no));
            }
        });
        barSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    view.setEnabled(false);
                    String no = barSearch.getText().toString().trim();
                    onStartFragment(AllOrderFragment.init(no));
                    view.setEnabled(true);
                    return true;
                }
                return true;
            }
        });
        noticeAdapter.setOnItemClickListener(new NoticeAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View v, Notice entry) {
                read(entry.getId());
                int statu = entry.getOrder_process();// 1为待配货 2为待出库 3为待回库  4为已回库  5为完成清点
                String id = entry.getOrder_id();
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
                }
            }
        });
    }

    private void loadcount() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Integer> map = WebHelper.init().getOrderNumber(shared.getInt(Constants.UserId,-1));
                int num1 = map.get("1") == null ? 0 : map.get("1");
                int num2 = map.get("2") == null ? 0 : map.get("2");
                int num3 = map.get("3") == null ? 0 : map.get("3");
                int num4 = map.get("4") == null ? 0 : map.get("4");
                int num5 = map.get("5") == null ? 0 : map.get("5");
                showOrders = new ArrayList<>();
                showOrders.add(new ShowOrder(0, "所有订单", (num1 + num2 + num3 + num4 + num5)));
                showOrders.add(new ShowOrder(1, "待配货", num1));
                showOrders.add(new ShowOrder(2, "待出库", num2));
                showOrders.add(new ShowOrder(3, "待回收", num3));
                showOrders.add(new ShowOrder(4, "已回收", num4));
                showOrders.add(new ShowOrder(5, "完成清算", num5));
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void loadMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                notices = WebHelper.init().getInformation();
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    private void read(int id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().readMsg(handler,id);
            }
        }).start();
    }

    @OnClick({R.id.bar_notice, R.id.start_date, R.id.end_date, R.id.oder_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bar_notice:

                break;
            case R.id.start_date:
                DatePickerDialog start = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                startDate.setText(year + "-" + (month + 1 > 9 ? month + 1 : "0" + (month + 1)) + "-" + (dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth));
                            }
                        },
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                start.show();
                break;
            case R.id.end_date:
                DatePickerDialog end = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                endDate.setText(year + "-" + (month + 1 > 9 ? month + 1 : "0" + (month + 1)) + "-" + (dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth));
                            }
                        },
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                end.show();
                break;
            case R.id.oder_bt:
                onStartFragment(XsOrderFragment.init());
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    adapter.setList(showOrders);
                    break;
                case 1:
                    noticeAdapter.setList(notices);
                    break;
            }
        }
    };

    private OnItemMenuClickLisenter OnItemMenuClickLisenter;

    public Fragment setOnItemMenuClickLisenter(OnItemMenuClickLisenter OnItemMenuClickLisenter) {
        this.OnItemMenuClickLisenter = OnItemMenuClickLisenter;
        return this;
    }

    public interface OnItemMenuClickLisenter {
        void itemMenu(ShowOrder order);
    }
}
