package cn.allen.warehouse.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

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
import cn.allen.warehouse.adapter.ShowOrderAdapter;
import cn.allen.warehouse.entry.ShowOrder;

public class SaleHomeFragment extends Fragment {

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
    private ShowOrderAdapter adapter;
    private List<ShowOrder> showOrders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
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
        adapter = new ShowOrderAdapter();
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        showRv.setLayoutManager(manager);
        showRv.setAdapter(adapter);
        loadcount();
    }

    private void addEvent(View view) {

    }

    private void loadcount() {
        showOrders = new ArrayList<>();
        showOrders.add(new ShowOrder(0, "所有订单", 300));
        showOrders.add(new ShowOrder(1, "待配货", 300));
        showOrders.add(new ShowOrder(2, "待出库", 300));
        showOrders.add(new ShowOrder(3, "待回收", 300));
        showOrders.add(new ShowOrder(4, "已回收", 300));
        showOrders.add(new ShowOrder(5, "完成清算", 300));
        adapter.setList(showOrders);
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
}
