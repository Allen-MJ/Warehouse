package cn.allen.warehouse.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import allen.frame.entry.Type;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.allen.warehouse.R;
import cn.allen.warehouse.entry.FlowerType;

public class FlowerParentMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FlowerType> list;
    private Map<Integer,Boolean> check;
    private Map<Integer,Boolean> item;

    public FlowerParentMenuAdapter(){
        check = new HashMap<>();
        item = new HashMap<>();
    }

    public void setList(List<FlowerType> list){
        this.list = list;
        for (FlowerType t:list ){
            check.put(t.getId(),false);
            initItem(t.getChildren());
        }
        notifyDataSetChanged();
    }

    private void initItem(List<FlowerType> list){
        for (FlowerType t:list ){
            item.put(t.getId(),false);
        }
    }

    private void setCheck(int id){
        check.put(id,!check.get(id));
        notifyDataSetChanged();
    }

    private void setChildCheck(int id){
        for (int mid:item.keySet()){
            item.put(mid,mid==id);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flower_type, parent, false);
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

        private AppCompatTextView name;
        private RecyclerView child;
        private FlowerItemMenuAdapter adapter;

        public ObjectHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.type_item_name);
            child = itemView.findViewById(R.id.type_item_child);
            LinearLayoutManager manager = new LinearLayoutManager(name.getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            child.setLayoutManager(manager);
            adapter = new FlowerItemMenuAdapter();
            child.setAdapter(adapter);
        }
        public void bind(final FlowerType entry){
            if(entry!=null){
                name.setText(entry.getName());
                if(entry.getChildren().size()==0){
                    child.setVisibility(View.GONE);
                }else{
                    adapter.setList(entry.getChildren(),item);
                    if(check.get(entry.getId())){
                        child.setVisibility(View.VISIBLE);
                    }else{
                        child.setVisibility(View.GONE);
                    }
                }
                name.setCompoundDrawablesRelativeWithIntrinsicBounds(check.get(entry.getId())?R.drawable.ic_arrow_down:R.drawable.ic_arrow_right,0,0,0);
                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setEnabled(false);
                        setCheck(entry.getId());
                        view.setEnabled(true);
                    }
                });
                adapter.setOnItemClickListener(new FlowerItemMenuAdapter.OnItemClickListener() {
                    @Override
                    public void itemClick(View v, FlowerType entry) {
                        setChildCheck(entry.getId());
                        if(listener!=null){
                            listener.itemClick(v,entry);
                        }
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
        void itemClick(View v, FlowerType entry);
    }
}