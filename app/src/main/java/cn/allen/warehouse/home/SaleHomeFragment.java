package cn.allen.warehouse.home;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import allen.frame.AllenManager;
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
import cn.allen.warehouse.adapter.NoticeAdapter;
import cn.allen.warehouse.adapter.ShowOrderAdapter;
import cn.allen.warehouse.data.WebHelper;
import cn.allen.warehouse.entry.Notice;
import cn.allen.warehouse.entry.ShowOrder;
import cn.allen.warehouse.utils.Constants;

public class SaleHomeFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.bar_search)
    AppCompatEditText barSearch;
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
    private ShowOrderAdapter adapter;
    private List<ShowOrder> showOrders;
    private SharedPreferences shared;
    private NoticeAdapter noticeAdapter;
    private List<Notice> notices;

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
        shared = AllenManager.getInstance().getStoragePreference();
        barName.setText(shared.getString(Constants.UserName, "用户昵称"));
        adapter = new ShowOrderAdapter();
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        showRv.setLayoutManager(manager);
        showRv.setAdapter(adapter);
        GridLayoutManager manager1 = new GridLayoutManager(getActivity(),2);
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
                if(OnItemMenuClickLisenter!=null){
                    OnItemMenuClickLisenter.itemMenu(entry);
                }
            }
        });
    }

    private void loadcount() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,Integer> map = WebHelper.init().getOrderNumber();
                showOrders = new ArrayList<>();
                showOrders.add(new ShowOrder(0, "所有订单", 0));
                showOrders.add(new ShowOrder(1, "待配货", map.get("1")==null?0:map.get("1")));
                showOrders.add(new ShowOrder(2, "待出库", map.get("2")==null?0:map.get("2")));
                showOrders.add(new ShowOrder(3, "待回收", map.get("3")==null?0:map.get("3")));
                showOrders.add(new ShowOrder(4, "已回收", map.get("4")==null?0:map.get("4")));
                showOrders.add(new ShowOrder(5, "完成清算", map.get("5")==null?0:map.get("5")));
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void loadMsg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                notices = WebHelper.init().getInformation();
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    @OnClick({R.id.bar_notice, R.id.start_date, R.id.end_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bar_notice:
                break;
            case R.id.start_date:
                break;
            case R.id.end_date:
                break;
        }
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
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
    public Fragment setOnItemMenuClickLisenter(OnItemMenuClickLisenter OnItemMenuClickLisenter){
        this.OnItemMenuClickLisenter = OnItemMenuClickLisenter;
        return this;
    }
    public interface OnItemMenuClickLisenter{
        void itemMenu(ShowOrder order);
    }
}
