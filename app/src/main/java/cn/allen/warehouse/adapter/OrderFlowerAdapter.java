package cn.allen.warehouse.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import cn.allen.warehouse.R;
import cn.allen.warehouse.entry.Flower;

public class OrderFlowerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Flower> list;
    private int Type_List = 0;
    private int Type_Add = 1;

    public OrderFlowerAdapter(){
    }

    public void setList(List<Flower> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType==Type_Add){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_choice_flower, parent, false);
        }else{
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_choice_flower, parent, false);
        }
        v.setLayoutParams(new ViewGroup
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ObjectHolder(v);
    }

    @Override
    public int getItemCount() {
        return (list==null?0:list.size())+1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==Type_List){
            ObjectHolder objectHolder = (ObjectHolder) holder;
            objectHolder.bind(list.get(position));
        }else{
            AddHolder addHolder = (AddHolder) holder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int size = list==null?0:list.size();
        if(size==0){
            return Type_Add;
        }else if(position<size){
            return Type_List;
        }else{
            return Type_Add;
        }
    }

    public class ObjectHolder extends RecyclerView.ViewHolder{

        private AppCompatTextView name,count;
        private AppCompatImageView icon;
        private View view;
        public ObjectHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.flower_item_name);
            count = itemView.findViewById(R.id.flower_item_count);
            icon = itemView.findViewById(R.id.flower_item_icon);
            view = itemView.findViewById(R.id.item_layout);
        }
        public void bind(final Flower entry){
            if(entry!=null){
                name.setText(entry.getName());
                count.setText(String.valueOf(entry.getStock()));
                Glide.with(view.getContext()).load(entry.getImg()).placeholder(R.mipmap.ic_flower).error(R.mipmap.ic_flower).into(icon);
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

    public class AddHolder extends RecyclerView.ViewHolder{
        public AddHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public interface OnItemClickListener{
        void itemClick(View v, Flower entry);
    }
}