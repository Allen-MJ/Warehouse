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
import androidx.recyclerview.widget.RecyclerView;
import cn.allen.warehouse.R;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Type> list;
    private Map<String,Boolean> check;

    public MenuAdapter(){
    }

    public void setList(List<Type> list){
        this.list = list;
        check = new HashMap<>();
        for(Type t:list){
            check.put(t.getId(),false);
        }
        notifyDataSetChanged();
    }

    public void setCheck(String id){
        for(Type t:list){
            check.put(t.getId(),id.equals(t.getId()));
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false);
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

        private AppCompatTextView name,num;
        private AppCompatImageView icon;
        private View view;
        public ObjectHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.menu_item_name);
            num = itemView.findViewById(R.id.menu_item_count);
            icon = itemView.findViewById(R.id.menu_item_icon);
            view = itemView.findViewById(R.id.item_layout);
        }
        public void bind(final Type entry){
            if(entry!=null){
                name.setText(entry.getName());
                if(entry.getXjnum()==0){
                    num.setVisibility(View.INVISIBLE);
                }else{
                    num.setVisibility(View.VISIBLE);
                    num.setText(entry.getXjnum());
                }
                icon.setImageResource(entry.getResId());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setEnabled(false);
                        setCheck(entry.getId());
                        if(listener!=null){
                            listener.itemClick(view,entry);
                        }
                        view.setEnabled(true);
                    }
                });
                view.setSelected(check.get(entry.getId()));
            }
        }
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public interface OnItemClickListener{
        void itemClick(View v, Type entry);
    }
}