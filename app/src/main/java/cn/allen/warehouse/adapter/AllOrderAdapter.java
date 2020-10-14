package cn.allen.warehouse.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import cn.allen.warehouse.R;
import cn.allen.warehouse.entry.Order;
import cn.allen.warehouse.utils.Constants;

public class AllOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Order> list;

    public AllOrderAdapter(){
    }

    public void setList(List<Order> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_all_order, parent, false);
        v.setLayoutParams(new ViewGroup
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ObjectHolder(v);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ObjectHolder objectHolder = (ObjectHolder) holder;
        objectHolder.bind(list.get(position));
    }

    public class ObjectHolder extends RecyclerView.ViewHolder{

        private AppCompatTextView name,no,status,address,ch,hs,person;
        private AppCompatImageView work;
        private View view;
        public ObjectHolder(@NonNull View itemView) {
            super(itemView);
            no = itemView.findViewById(R.id.order_item_number);
            work = itemView.findViewById(R.id.order_item_work);
            status = itemView.findViewById(R.id.order_item_status);
            name = itemView.findViewById(R.id.order_item_name);
            address = itemView.findViewById(R.id.order_item_address);
            ch = itemView.findViewById(R.id.order_item_chdate);
            hs = itemView.findViewById(R.id.order_item_hsdate);
            person = itemView.findViewById(R.id.order_item_whperson);
            view = itemView.findViewById(R.id.item_layout);
        }
        public void bind(final Order entry){
            if(entry!=null){
                no.setText("订单号:"+entry.getOrder_number());
                name.setText(entry.getCustomer_name());
                address.setText(entry.getHotel_address());
                ch.setText("出库时间:"+entry.getDelivery_times().substring(0,10));
                hs.setText("回收时间:"+entry.getRecovery_dates().substring(0,10));
                person.setText("销售员:"+entry.getNumber_name());
                address.setText(entry.getHotel_address());
                status.setText(Constants.getStatusName(entry.getOrder_process()));
                status.setCompoundDrawablesRelativeWithIntrinsicBounds(Constants.getStatusResId(entry.getOrder_process()),0,0,0);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setEnabled(false);
                        if(listener!=null){
                            listener.itemClick(view,entry);
                        }
                        view.setEnabled(true);
                    }
                });
            }
        }
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public interface OnItemClickListener{
        void itemClick(View v, Order entry);
    }
}