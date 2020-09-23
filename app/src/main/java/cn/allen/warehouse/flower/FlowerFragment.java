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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.allen.warehouse.R;
import cn.allen.warehouse.utils.Constants;

public class FlowerFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.bar_search)
    AppCompatEditText barSearch;
    @BindView(R.id.bar_notice)
    AppCompatImageView barNotice;
    @BindView(R.id.bar_name)
    AppCompatTextView barName;
    @BindView(R.id.ch_date)
    AppCompatTextView chDate;
    @BindView(R.id.hs_date)
    AppCompatTextView hsDate;
    @BindView(R.id.menu_rv)
    RecyclerView menuRv;
    @BindView(R.id.flower_rv)
    RecyclerView flowerRv;
    private ActivityHelper actHelper;
    private SharedPreferences shared;
    private int uid;

    public static FlowerFragment init() {
        FlowerFragment fragment = new FlowerFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flower, container, false);
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

    }

    private void addEvent(View view) {

    }

    @OnClick({R.id.ch_date, R.id.hs_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ch_date:
                break;
            case R.id.hs_date:
                break;
        }
    }
}
