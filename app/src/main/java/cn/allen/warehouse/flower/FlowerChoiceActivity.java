package cn.allen.warehouse.flower;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import allen.frame.AllenIMBaseActivity;
import allen.frame.tools.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.warehouse.R;
import cn.allen.warehouse.adapter.FlowerChoiceAdapter;
import cn.allen.warehouse.data.WebHelper;
import cn.allen.warehouse.entry.Flower;

public class FlowerChoiceActivity extends AllenIMBaseActivity {

    @BindView(R.id.bar_search)
    AppCompatEditText barSearch;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_send)
    ImageView ivSend;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    private List<Flower> list;
    private ArrayList<Flower> choice;
    private String name = "";
    private FlowerChoiceAdapter adapter;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_choice_flower;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
        barSearch.setHint("查询鲜花名称");
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        GridLayoutManager manager = new GridLayoutManager(context, 2);
        rv.setLayoutManager(manager);
        adapter = new FlowerChoiceAdapter();
        rv.setAdapter(adapter);
        choice = (ArrayList<Flower>) getIntent().getSerializableExtra("choice");
        loadFlowers();
    }

    @Override
    protected void addEvent() {
        barSearch.addTextChangedListener(watcher);
//        barSearch.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    view.setEnabled(false);
//                    name = barSearch.getText().toString().trim();
//                    loadFlowers();
//                    view.setEnabled(true);
//                    return true;
//                }
//                return true;
//            }
//        });
        adapter.setOnItemClickListener(new FlowerChoiceAdapter.OnItemClickListener() {
            @Override
            public void addClick(View v, Flower flower) {
//                setResult(RESULT_OK,new Intent().putExtra("flower",flower));
//                finish();
                sendBroadcast(new Intent("add_flower").putExtra("flower", flower));
            }
        });
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (StringUtils.notEmpty(s.toString())) {
                // edittext加刪除按鈕
                ivClear.setVisibility(View.VISIBLE);
            }else {
                ivClear.setVisibility(View.GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            name = barSearch.getText().toString().trim();
            loadFlowers();
        }
    };

    private void loadFlowers() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                list = WebHelper.init().getSeachFlower("", "", name);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    adapter.setList(list, choice);
                    break;
            }
        }
    };


    @OnClick({R.id.iv_back, R.id.iv_send,R.id.iv_clear})
    public void onViewClicked(View view) {
        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.iv_clear:
                barSearch.setText("");
                ivClear.setVisibility(View.GONE);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_send:

                name = barSearch.getText().toString().trim();
                loadFlowers();

                break;
        }
        view.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
