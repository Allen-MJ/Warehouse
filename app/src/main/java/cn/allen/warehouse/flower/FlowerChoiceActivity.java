package cn.allen.warehouse.flower;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import cn.allen.warehouse.widget.SearchEditText;

public class FlowerChoiceActivity extends AllenIMBaseActivity {

    @BindView(R.id.bar_search)
    SearchEditText barSearch;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.iv_back)
    ImageView ivBack;
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
        barSearch.setOnSerchListenner(new SearchEditText.onSerchListenner() {
            @Override
            public void onSerchEvent() {
                name = barSearch.getText().toString().trim();
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
                }else if(i==KeyEvent.KEYCODE_DEL){
                    name = barSearch.getText().toString().trim();
                    int len = StringUtils.empty(name)?0:name.length();
                    if(len>0){
                        barSearch.setText(name.substring(0,len-1));
                    }
                }
                return true;
            }
        });
        adapter.setOnItemClickListener(new FlowerChoiceAdapter.OnItemClickListener() {
            @Override
            public void addClick(View v, Flower flower) {
//                setResult(RESULT_OK,new Intent().putExtra("flower",flower));
//                finish();
                sendBroadcast(new Intent("add_flower").putExtra("flower", flower));
            }
        });
        barSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name = barSearch.getText().toString().trim();
                loadFlowers();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

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


    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
        view.setEnabled(true);
    }
}
