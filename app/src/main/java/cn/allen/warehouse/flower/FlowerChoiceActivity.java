package cn.allen.warehouse.flower;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenIMBaseActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    private List<Flower> list;
    private ArrayList<Flower> choice;
    private String name="";
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
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        GridLayoutManager manager = new GridLayoutManager(context,2);
        rv.setLayoutManager(manager);
        adapter = new FlowerChoiceAdapter();
        rv.setAdapter(adapter);
        choice = (ArrayList<Flower>) getIntent().getSerializableExtra("choice");
        loadFlowers();
    }

    @Override
    protected void addEvent() {
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
        adapter.setOnItemClickListener(new FlowerChoiceAdapter.OnItemClickListener() {
            @Override
            public void addClick(View v, Flower flower) {
                setResult(RESULT_OK,new Intent().putExtra("flower",flower));
                finish();
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
                    adapter.setList(list,choice);
                    break;
            }
        }
    };

}
