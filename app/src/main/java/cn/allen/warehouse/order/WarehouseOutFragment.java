package cn.allen.warehouse.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import allen.frame.ActivityHelper;
import allen.frame.AllenManager;
import allen.frame.adapter.CommonAdapter;
import allen.frame.adapter.ViewHolder;
import allen.frame.tools.FileUtils;
import allen.frame.tools.MsgUtils;
import allen.frame.tools.StringUtils;
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

public class WarehouseOutFragment extends BaseFragment {
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
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.tv_order_remark)
    AppCompatTextView tvOrderRemark;
    @BindView(R.id.layout_remark)
    LinearLayoutCompat layoutRemark;
    private SharedPreferences shared;
    private ActivityHelper actHelper;
    private CommonAdapter<OrderInfoEntity.ChildrenBean> childrenAdapter;
    private CommonAdapter<OrderInfoEntity.MainchildrenBean> mainAdapter;
    private List<OrderInfoEntity.ChildrenBean> childrenList;
    private List<OrderInfoEntity.MainchildrenBean> mainList;
    private String numberID;
    private OrderInfoEntity orderInfoEntity;
    private List<ImageEntity> imageEntityList = new ArrayList<>();
    private CommonAdapter<ImageEntity> imageAdapter;

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
                    layoutRemark.setVisibility(View.VISIBLE);
                    tvOrderRemark.setText(StringUtils.notEmpty(orderInfoEntity.getRemark())?orderInfoEntity.getRemark():"");
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
                    int childrensize = childrenList == null ? 0 : childrenList.size();
                    if (childrensize == 0) {
                        layoutChildren.setVisibility(View.GONE);
                    } else {
                        layoutChildren.setVisibility(View.VISIBLE);
                        childrenAdapter.setDatas(childrenList);
                    }
                    mainList = orderInfoEntity.getMainchildren();
                    int mainsize = mainList == null ? 0 : mainList.size();
                    if (mainsize == 0) {
                        layoutMain.setVisibility(View.GONE);
                    } else {
                        layoutMain.setVisibility(View.VISIBLE);
                        mainAdapter.setDatas(mainList);
                    }

                    break;
                case 101:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showLongToast(getContext(), (String) msg.obj);
                    loadData();
                    break;
                case 103:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showLongToast(getContext(), (String) msg.obj);
                    backPreFragment();
                    break;
                case 102:
                    actHelper.dismissProgressDialog();
                    String url = (String) msg.obj;
                    ImageEntity imageEntity = new ImageEntity();
                    imageEntity.setMess(url);
                    imageEntityList.add(imageEntity);
                    imageAdapter.setDatas(imageEntityList);
                    break;
                case -1:
                    actHelper.dismissProgressDialog();
                    MsgUtils.showLongToast(getContext(), (String) msg.obj);
                    break;
            }
        }
    };

    public WarehouseOutFragment() {
    }

    public static WarehouseOutFragment newInstance(String numberID) {
        WarehouseOutFragment fragment = new WarehouseOutFragment();
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
        View view = inflater.inflate(R.layout.fragment_warehouse_out, container, false);
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

    private void warehouseOut(JSONArray array) {
        new Thread() {
            @Override
            public void run() {
                WebHelper.init().warehouseOut(handler, numberID, orderInfoEntity.getCustomer_name(), array);
            }
        }.start();
    }

    private void upload() {
        actHelper.showProgressDialog("正在上传图片,请稍候...");
        new Thread() {
            @Override
            public void run() {
                WebHelper.init().uploadFile(handler, file);
            }
        }.start();
    }

    private void initAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        childrenAdapter = new CommonAdapter<OrderInfoEntity.ChildrenBean>(getContext(), R.layout.order_info_item_layout) {
            @Override
            public void convert(ViewHolder holder, OrderInfoEntity.ChildrenBean entity, int position) {
                holder.setText(R.id.tv_name, (position+1)+"."+entity.getFlower_name());
                holder.setText(R.id.tv_count, "数量:" + entity.getScheduled_quantity());
                int status = entity.getId_check();
                if (status == 0) {
                    holder.setVisible(R.id.btn_submit, true);
                    holder.setVisible(R.id.tv_submit, false);
                } else {
                    holder.setVisible(R.id.btn_submit, false);
                    holder.setVisible(R.id.tv_submit, true);
                }
                holder.setOnClickListener(R.id.btn_submit, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
            }
        };
        recyclerviewChildren.setLayoutManager(manager);
        recyclerviewChildren.setAdapter(childrenAdapter);

        mainAdapter = new CommonAdapter<OrderInfoEntity.MainchildrenBean>(getContext(), R.layout.order_info_item_layout) {
            @Override
            public void convert(ViewHolder holder, OrderInfoEntity.MainchildrenBean entity, int position) {
                holder.setText(R.id.tv_name, (position+1)+"."+entity.getFlower_name());
                holder.setText(R.id.tv_count, "数量:" + entity.getScheduled_quantity());
                int status = entity.getId_check();
                if (status == 0) {
                    holder.setVisible(R.id.btn_submit, true);
                    holder.setVisible(R.id.tv_submit, false);
                } else {
                    holder.setVisible(R.id.btn_submit, false);
                    holder.setVisible(R.id.tv_submit, true);
                }
                holder.setOnClickListener(R.id.btn_submit, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
            }
        };
        recyclerviewMain.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerviewMain.setAdapter(mainAdapter);

        imageAdapter = new CommonAdapter<ImageEntity>(getContext(), R.layout.order_image_item_layout) {
            @Override
            public void convert(ViewHolder holder, ImageEntity entity, int position) {
                holder.setImageByUrl(R.id.image, entity.getMess(), R.drawable.mis_default_error);
                if (entity.getPan() == 1) {
                    holder.setTextColorRes(R.id.tv_state, R.color.state_text_color1);
                    holder.setDrawableLeft(R.id.tv_state, getActivity().getResources().getDrawable(R.mipmap.chuku_icon));
                    holder.setText(R.id.tv_state, "出库");
                } else if (entity.getPan() == 2) {
                    holder.setTextColorRes(R.id.tv_state, R.color.state_text_color2);
                    holder.setDrawableLeft(R.id.tv_state, getActivity().getResources().getDrawable(R.mipmap.huiku_icon));
                    holder.setText(R.id.tv_state, "回库");
                } else if (entity.getPan() == -1) {
                    holder.setTextColorRes(R.id.tv_state, R.color.state_text_color3);
                    holder.setDrawableLeft(R.id.tv_state, getActivity().getResources().getDrawable(R.mipmap.shangchuan_icon));
                    holder.setText(R.id.tv_state, "待上传");
                } else {
                    holder.setVisible(R.id.tv_state, false);
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
                Intent intent = new Intent(getActivity(), ShowPicActivity.class);
                intent.putExtra("url", imageEntityList.get(position).getMess());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                // 拍照并进行裁剪
                case REQUEST_TAKE_PHOTO:
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    if (isAndroidQ) {
                        intent.setData(imgUri);
                    } else {
                        intent.setData(Uri.fromFile(imgFile));
                    }
                    getActivity().sendBroadcast(intent);
                    commitFile(imgUri);
                    break;
            }
        }
    }

    private File file;

    private void commitFile(Uri uri) {
        String path = FileUtils.getPath(getContext(), uri);
        file = new File(path);
        upload();
    }

    @OnClick({R.id.tv_submit, R.id.back_bt, R.id.image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image:
                takePhone();
                break;
            case R.id.tv_submit:

                JSONArray array = new JSONArray();
                int imagesize = imageEntityList == null ? 0 : imageEntityList.size();
                if (imagesize > 0) {
                    for (int i = 0; i < imageEntityList.size(); i++) {
                        try {
                            ImageEntity imageEntity = imageEntityList.get(i);
                            JSONObject object = new JSONObject();
                            object.put("imgs", imageEntity.getMess());
                            array.put(object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                actHelper.showProgressDialog("");
                warehouseOut(array);
                break;
            case R.id.back_bt:
                backPreFragment();
                break;
        }
    }

    private Uri imgUri; // 拍照时返回的uri
    private File imgFile;// 拍照保存的图片文件
    private static final int REQUEST_TAKE_PHOTO = 0;// 拍照
    /**
     * 是否是Android 10以上手机
     */
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

    private void takePhone() {
        // 要保存的文件名
        String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
        String fileName = "ckgl_" + time;
        // 创建一个文件夹
        // 要保存的图片文件
        imgFile = FileUtils.getInstance().creatNewFile("take_photo", fileName + ".jpg");
        // 将file转换成uri
        if (isAndroidQ) {
            // 适配android 10
            imgUri = createImageUri();
        } else {
            // 注意7.0及以上与之前获取的uri不一样了，返回的是provider路径
            imgUri = getUriForFile(getActivity(), imgFile);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 添加Uri读取权限
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        // 或者
//        getActivity().grantUriPermission(getActivity().getPackageName(), imgUri, Intent
//                .FLAG_GRANT_READ_URI_PERMISSION);
        // 添加图片保存位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     *
     * @return 图片的uri
     */
    private Uri createImageUri() {
        //设置保存参数到ContentValues中
        ContentValues contentValues = new ContentValues();
        //设置文件名
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + "");
        //兼容Android Q和以下版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
            //TODO RELATIVE_PATH是相对路径不是绝对路径;照片存储的地方为：内部存储/Pictures/preventpro
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CkglCache");
        }
        //设置文件类型
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
        //执行insert操作，向系统文件夹中添加文件
        //EXTERNAL_CONTENT_URI代表外部存储器，该值不变
        Uri uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        return uri;

    }


    private Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "cn.allen.warehouse" +
                    ".provider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

}