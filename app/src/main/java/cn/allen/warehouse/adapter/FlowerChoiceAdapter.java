package cn.allen.warehouse.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import allen.frame.tools.MsgUtils;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import cn.allen.warehouse.R;
import cn.allen.warehouse.entry.Flower;

public class FlowerChoiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Flower> list;

    private List<Flower> choice;
    private Map<Integer,Boolean> select;

    public FlowerChoiceAdapter(){
        list = new ArrayList<>();
        choice = new ArrayList<>();
        select = new HashMap<>();
    }

    public void setList(List<Flower> list,List<Flower> choice){
        this.list = list;
        int len = list==null?0:list.size();
        if(len>0){
            for(Flower flower:list){
                select.put(flower.getId(),false);
            }
        }
        this.choice = choice;
        int size = choice==null?0:choice.size();
        if(size>0){
            for(Flower flower:choice){
                select.put(flower.getId(),true);
            }
        }
        notifyDataSetChanged();
    }

    public void add(int index){
        choice.add(list.get(index));
        select.put(list.get(index).getId(),true);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flower_choice, parent, false);
        v.setLayoutParams(new ViewGroup
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ObjectHolder(v);
    }

    @Override
    public int getItemCount() {
        return list==null?1:list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ObjectHolder objectHolder = (ObjectHolder) holder;
        objectHolder.bind(list.get(position),position);
    }

    public class ObjectHolder extends RecyclerView.ViewHolder{

        private AppCompatTextView name,stock;
        private AppCompatImageView add;
        public ObjectHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.flower_item_name);
            stock = itemView.findViewById(R.id.flower_item_count);
            add = itemView.findViewById(R.id.flower_item_add);
        }
        public void bind(final Flower entry,int position){
            if(entry!=null){
                name.setText(entry.getName());
                stock.setText(String.valueOf(entry.getStock()));
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setEnabled(false);
                        if(listener!=null&&!select.get(entry.getId())){
                            add(position);
                            MsgUtils.showShortToast(view.getContext(),"已添加!");
                            listener.addClick(view,entry);
                        }else{
                            MsgUtils.showShortToast(view.getContext(),"已在列表中!");
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
        void addClick(View v,Flower flower);
    }
}