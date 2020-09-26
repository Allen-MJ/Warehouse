package cn.allen.warehouse.flower;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import cn.allen.warehouse.entry.Flower;
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
        adapter = new ChoiceFlowerAdapter();
        choiceRv.setLayoutManager(manager);
        choiceRv.setAdapter(adapter);
    }

    private void addEvent(View view) {
        adapter.setOnItemClickListener(listener);
    }

    ChoiceFlowerAdapter.OnItemClickListener listener = new ChoiceFlowerAdapter.OnItemClickListener() {
        @Override
        public void deleteClick(View v, int position) {

        }

        @Override
        public void addClick(View v) {
            Flower flower = new Flower();
            flower.setId(1);
            flower.setName("ceshiasdasd");
            flower.setStock(1000);
            flower.setRent(11);
            adapter.addList(flower);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    choiceRv.scrollToPosition(adapter.getItemCount()-1);
                }
            });
        }

        @Override
        public void numEdit(View v) {

        }
    };

    @OnClick({R.id.back_bt,R.id.order_date_hl, R.id.order_date_ck, R.id.order_date_hs, R.id.order_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_bt:
                backPreFragment();
                break;
            case R.id.order_date_hl:
                break;
            case R.id.order_date_ck:
                break;
            case R.id.order_date_hs:
                break;
            case R.id.order_commit:
                break;
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){

            }
        }
    };
}
