package cn.allen.warehouse.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import allen.frame.adapter.CommonAdapter;
import allen.frame.adapter.ViewHolder;
import allen.frame.tools.MsgUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.allen.warehouse.BaseFragment;
import cn.allen.warehouse.R;
import cn.allen.warehouse.ShowPicActivity;
import cn.allen.warehouse.data.WebHelper;
import cn.allen.warehouse.entry.ImageEntity;
import cn.allen.warehouse.entry.OrderInfoEntity;
import cn.allen.warehouse.utils.Constants;

public class InventoryFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.back_bt)
    AppCompatImageView barBack;
    @BindView(R.id.bar_title)
    AppCompatTextView barTitle;
    @BindView(R.id.bar_notice)
    AppCompatImageView barNotice;
    @BindView(R.id.bar_name)
    AppCompatTextView barName;
    @BindView(R.id.order_name)
    AppCompatTextView orderName;
    @BindView(R.id.order_state)
    AppCompatTextView orderState;
    @BindView(R.id.tv_order_address)
    AppCompatTextView tvOrderAddress;
    @BindView(R.id.tv_order_out_time)
    AppCompatTextView tvOrderOutTime;
    @BindView(R.id.tv_order_back_time)
    AppCompatTextView tvOrderBackTime;
    @BindView(R.id.tv_salesman)
    AppCompatTextView tvSalesman;
    @BindView(R.id.tv_submit)
    AppCompatButton tvSubmit;
    @BindView(R.id.item_layout)
    CardView itemLayout;
    @BindView(R.id.recyclerview_children)
    RecyclerView recyclerviewChildren;
    @BindView(R.id.recyclerview_main)
    RecyclerView recyclerviewMain;
    @BindView(R.id.tv_children_title)
    AppCompatTextView tvChildrenTitle;
    @BindView(R.id.layout_children)
    LinearLayoutCompat layoutChildren;
    @BindView(R.id.tv_main_title)
    AppCompatTextView tvMainTitle;
    @BindView(R.id.layout_main)
    LinearLayoutCompat layoutMain;
    @BindView(R.id.tv_order_id)
    AppCompatTextView tvOrderId;
    @BindView(R.id.layout_bottom)
    LinearLayoutCompat layoutBottom;
    @BindView(R.id.recyclerview_image)
    RecyclerView recyclerviewImage;
    private SharedPreferences shared;
    private ActivityHelper actHelper;
    private CommonAdapter<OrderInfoEntity.ChildrenBean> childrenAdapter;
    private CommonAdapter<OrderInfoEntity.MainchildrenBean> mainAdapter;
    private CommonAdapter<OrderInfoEntity.ImagesBean> imageAdapter;
    private List<OrderInfoEntity.ChildrenBean> childrenList;
    private List<OrderInfoEntity.MainchildrenBean> mainList;
    private List<OrderInfoEntity.ImagesBean> imageList;
    private String numberID;
    private OrderInfoEntity orderInfoEntity;
    private List<ImageEntity> imageEntityList = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    actHelper.dismissProgressDialog();
                    actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_SUCCES, "");
                    tvOrderAddress.setText(orderInfoEntity.getHotel_address());
                    tvOrderId.setText("订单号:" + orderInfoEntity.getOrder_number());
                    orderName.setText(orderInfoEntity.getCustomer_name());
                    tvOrderOutTime.setText(orderInfoEntity.getDelivery_times());
                    tvOrderBackTime.setText(orderInfoEntity.getRecovery_dates());
                    int statu = orderInfoEntity.getOrder_process();// 1为待配货 2为待出库 3为待回库  4为已回库  5为完成清点
                    switch (statu) {
                        case 1:
                            orderState.setText("待配货");
                            break;
                        case 2:
                            orderState.setText("待出库");
                            break;
                        case 3:
                            orderState.setText("待回库");
                            break;
                        case 4:
                            orderState.setText("已回库");
                            break;
                        case 5:
                            orderState.setText("完成清点");
                            break;
                    }
                    childrenList = orderInfoEntity.getChildren();
                    int chrildrensize=childrenList==null?0:childrenList.size();
                    if (chrildrensize==0) {
                        layoutChildren.setVisibility(View.GONE);
                    } else {
                        layoutChildren.setVisibility(View.VISIBLE);
                        childrenAdapter.setDatas(childrenList);
                    }
                    mainList = orderInfoEntity.getMainchildren();
                    int mainsize=mainList==null?0:mainList.size();
                    if (mainsize==0) {
                        layoutMain.setVisibility(View.GONE);
                    } else {
                        layoutMain.setVisibility(View.VISIBLE);
                        mainAdapter.setDatas(mainList);
                    }
                    imageList = orderInfoEntity.getImages();
                    int imagesize=imageList==null?0:imageList.size();
                    if (imagesize>0) {
                        imageAdapter.setDatas(imageList);
                    }

                    break;
                case 101:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showLongToast(getContext(), (String) msg.obj);
                    loadData();
                    break;
                case 102:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showLongToast(getContext(), (String) msg.obj);
                    backPreFragment();
                    break;
                case -1:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showLongToast(getContext(), (String) msg.obj);
                    break;
            }
        }
    };

    public InventoryFragment() {
    }

    public static InventoryFragment newInstance(String numberID) {
        InventoryFragment fragment = new InventoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ID", numberID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_returned, container, false);
        actHelper = new ActivityHelper(getActivity(), view);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            numberID = bundle.getString("ID");
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUi(view);
        addEvent(view);
    }

    private void initUi(View view) {
        layoutBottom.setVisibility(View.GONE);
        shared = AllenManager.getInstance().getStoragePreference();
        barName.setText(shared.getString(Constants.UserName, "用户昵称"));
        initAdapter();
        actHelper.setLoadUi(ActivityHelper.PROGRESS_STATE_START, "");
        loadData();
    }

    private void loadData() {
        new Thread() {
            @Override
            public void run() {
                orderInfoEntity = WebHelper.init().getOrderInfo(numberID);
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        childrenAdapter = new CommonAdapter<OrderInfoEntity.ChildrenBean>(getContext(), R.layout.order_return_item_layout) {
            @Override
            public void convert(ViewHolder holder, OrderInfoEntity.ChildrenBean entity, int position) {
                holder.setText(R.id.tv_name, entity.getFlower_name());
                holder.setText(R.id.tv_count, "数量:" + entity.getScheduled_quantity());
                AppCompatEditText et_count = holder.getView(R.id.et_sunhao);
                et_count.setText(entity.getLoss_quantity()+"");
                et_count.setFocusable(false);
                et_count.setFocusableInTouchMode(false);

            }
        };
        recyclerviewChildren.setLayoutManager(manager);
        recyclerviewChildren.setAdapter(childrenAdapter);

        mainAdapter = new CommonAdapter<OrderInfoEntity.MainchildrenBean>(getContext(), R.layout.order_return_item_layout) {
            @Override
            public void convert(ViewHolder holder, OrderInfoEntity.MainchildrenBean entity, int position) {
                holder.setText(R.id.tv_name, entity.getFlower_name());
                holder.setText(R.id.tv_count, "数量:" + entity.getScheduled_quantity());
                AppCompatEditText et_count = holder.getView(R.id.et_sunhao);
                et_count.setText(entity.getLoss_quantity()+"");
                et_count.setFocusable(false);
                et_count.setFocusableInTouchMode(false);
            }
        };
        recyclerviewMain.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerviewMain.setAdapter(mainAdapter);

        imageAdapter = new CommonAdapter<OrderInfoEntity.ImagesBean>(getContext(), R.layout.order_image_item_layout) {
            @Override
            public void convert(ViewHolder holder, OrderInfoEntity.ImagesBean entity, int position) {
                holder.setImageByUrl(R.id.image, entity.getImg(), R.drawable.mis_default_error);
                if (entity.getPan()==1){
                    holder.setTextColorRes(R.id.tv_state,R.color.state_text_color1);
                    holder.setDrawableLeft(R.id.tv_state, getActivity().getResources().getDrawable(R.mipmap.chuku_icon));
                    holder.setText(R.id.tv_state,"出库");
                }else if (entity.getPan()==2){
                    holder.setTextColorRes(R.id.tv_state,R.color.state_text_color2);
                    holder.setDrawableLeft(R.id.tv_state, getActivity().getResources().getDrawable(R.mipmap.huiku_icon));
                    holder.setText(R.id.tv_state,"回库");
                }else if (entity.getPan()==-1){
                    holder.setTextColorRes(R.id.tv_state,R.color.state_text_color3);
                    holder.setDrawableLeft(R.id.tv_state, getActivity().getResources().getDrawable(R.mipmap.shangchuan_icon));
                    holder.setText(R.id.tv_state,"待上传");
                }else {
                    holder.setVisible(R.id.tv_state,false);
                }
            }
        };
        recyclerviewImage.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerviewImage.setAdapter(imageAdapter);
    }

    private void addEvent(View view) {
        imageAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(getActivity(), ShowPicActivity.class);
                intent.putExtra("url",imageList.get(position).getImg());
                startActivity(intent);

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }


    @OnClick({R.id.tv_submit,R.id.back_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                break;
            case R.id.back_bt:
                backPreFragment();
                break;
        }

    }


}