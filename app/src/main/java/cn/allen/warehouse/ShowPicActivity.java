package cn.allen.warehouse;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import allen.frame.AllenIMBaseActivity;
import allen.frame.widget.PhotoView;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowPicActivity extends AllenIMBaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.pic)
    PhotoView pic;
    private String url;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_show_pic;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        url = getIntent().getStringExtra("url");
        Glide.with(context).load(url).into(pic);
    }

    @Override
    protected void addEvent() {

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked(View view) {
        view.setEnabled(false);
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
        view.setEnabled(true);
    }
}
