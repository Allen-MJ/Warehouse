package cn.allen.warehouse.flower;

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

import java.util.Calendar;
import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.allen.warehouse.BaseFragment;
import cn.allen.warehouse.R;
import cn.allen.warehouse.adapter.FlowerAdapter;
import cn.allen.warehouse.adapter.FlowerParentMenuAdapter;
import cn.allen.warehouse.data.WebHelper;
import cn.allen.warehouse.entry.Flower;
import cn.allen.warehouse.entry.FlowerType;
import cn.allen.warehouse.utils.Constants;

public class FlowerFragment extends BaseFragment {

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
    @BindView(R.id.oder_bt)
    CardView oderBt;
    private ActivityHelper actHelper;
    private SharedPreferences shared;
    private int uid,id;
    private List<FlowerType> types;
    private FlowerParentMenuAdapter menuAdapter;
    private FlowerAdapter adapter;
    private List<Flower> list;
    private String startTime,endTime,name;
    private Calendar c;

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
        c = Calendar.getInstance();
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.UserId, -1);
        barName.setText(shared.getString(Constants.UserName, "用户昵称"));
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        menuRv.setLayoutManager(manager);
        menuAdapter = new FlowerParentMenuAdapter();
        menuRv.setAdapter(menuAdapter);

        GridLayoutManager manager1 = new GridLayoutManager(getActivity(), 4);
        flowerRv.setLayoutManager(manager1);
        adapter = new FlowerAdapter();
        flowerRv.setAdapter(adapter);
        laodType();
    }

    private void addEvent(View view) {
        menuAdapter.setOnItemClickListener(new FlowerParentMenuAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View v, FlowerType entry) {
                id = entry.getId();
                loadFlowers();
            }
        });
        barSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    view.setEnabled(false);
                    name = barSearch.getText().toString().trim();
                    loadFlowers();
                    view.setEnabled(true);
                    return true;
                }
                return true;
            }
        });
    }

    private void laodType() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                types = WebHelper.init().getGrade();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void loadFlowers() {
        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START, "");
        new Thread(new Runnable() {
            @Override
            public void run() {
                list = WebHelper.init().getAllType(id,endTime,startTime,name);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    @OnClick({R.id.ch_date, R.id.hs_date, R.id.oder_bt})
    public void onViewClicked(View view) {
        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.ch_date:
                DatePickerDialog ch = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                startTime = year+"-"+(month+1>9?month+1:"0"+(month+1))+"-"+(dayOfMonth>9?dayOfMonth:"0"+dayOfMonth);
                                chDate.setText(startTime);
                                loadFlowers();
                            }
                        },
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                ch.show();
                break;
            case R.id.hs_date:
                DatePickerDialog hs = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                endTime = year+"-"+(month+1>9?month+1:"0"+(month+1))+"-"+(dayOfMonth>9?dayOfMonth:"0"+dayOfMonth);
                                hsDate.setText(endTime);
                                loadFlowers();
                            }
                        },
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                hs.show();
                break;
            case R.id.oder_bt:
                onStartFragment(XsOrderFragment.init());
                break;
        }
        view.setEnabled(true);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    menuAdapter.setList(types);
                    break;
                case 1:
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_SUCCES, "");
                    adapter.setList(list);
                    break;
            }
        }
    };

}
