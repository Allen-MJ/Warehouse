package cn.allen.warehouse.flower;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.allen.warehouse.BaseFragment;
import cn.allen.warehouse.R;
import cn.allen.warehouse.utils.Constants;

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
    private ActivityHelper actHelper;
    private SharedPreferences shared;
    private int uid;

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
        uid = shared.getInt(Constants.UserId, -1);
        barName.setText(shared.getString(Constants.UserName, "用户昵称"));
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
    }

    private void addEvent(View view) {
    }

    @OnClick(R.id.back_bt)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.back_bt:
                backPreFragment();
                break;
        }
    }
}
