package cn.allen.warehouse.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import allen.frame.tools.StringUtils;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.allen.warehouse.R;
import cn.allen.warehouse.entry.Flower;

public class ChoiceFlowerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Flower> list;
    private final int Type_Add = 0;
    private final int Type_List = 1;

    public ChoiceFlowerAdapter(){
        list = new ArrayList<>();
    }

    public void setList(List<Flower> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<Flower> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    public void addList(Flower entry){
        entry.setRent(1);
        list.add(entry);
        notifyDataSetChanged();
    }

    public void delete(int index){
        list.remove(index);
        notifyDataSetChanged();
    }

    public String getChoice(){
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for(Flower flower:list){
            sb.append(",{");
            sb.append("\"id\":");
            sb.append(flower.getId()+",");
            sb.append("\"name\":");
            sb.append("\""+flower.getName()+"\",");
            sb.append("\"count\":");
            sb.append(flower.getRent());
            sb.append("}");
        }
        sb.append("]");
        return sb.toString().replaceFirst(",","");
    }

    public ArrayList<Flower> getList(){
        return (ArrayList<Flower>)list;
    }

    public float getMonney(){
        float money = 0f;
        for(Flower flower:list){
            money = money + flower.getUnit_price()*flower.getRent();
        }
        return money;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType==Type_List){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_choice_flower, parent, false);
            v.setLayoutParams(new ViewGroup
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new ObjectHolder(v);
        }else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_choice_flower_add, parent, false);
            v.setLayoutParams(new ViewGroup
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new AddHolder(v);
        }

    }

    @Override
    public int getItemCount() {
        return list==null?1:list.size()+1;
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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==Type_Add){
            AddHolder addHolder = (AddHolder) holder;
            addHolder.bind();
        }else{
            ObjectHolder objectHolder = (ObjectHolder) holder;
            objectHolder.bind(list.get(position),position);
        }
    }

    public class ObjectHolder extends RecyclerView.ViewHolder{

        private AppCompatTextView name,stock;
        private AppCompatImageView delete;
        private AppCompatEditText rent;
        private View view;
        public ObjectHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.choice_item_name);
            rent = itemView.findViewById(R.id.choice_item_rent);
            stock = itemView.findViewById(R.id.choice_item_stock);
            delete = itemView.findViewById(R.id.choice_item_delete);
            view = itemView.findViewById(R.id.item_layout);
        }
        public void bind(final Flower entry,int position){
            if(entry!=null){
                name.setText(entry.getName());
                stock.setText(String.valueOf(entry.getStock()));
                rent.setText(String.valueOf(entry.getRent()));
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setEnabled(false);
                        delete(position);
                        if(listener!=null){
                            listener.deleteClick(view,position);
                        }
                        view.setEnabled(true);
                    }
                });
                rent.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String num = StringUtils.empty(charSequence.toString())?"1":charSequence.toString();
                        list.get(position).setRent(Integer.parseInt(num));
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        }
    }
    public class AddHolder extends RecyclerView.ViewHolder{

        private View view;
        public AddHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.item_add);
        }
        public void bind(){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setEnabled(false);
                    if(listener!=null){
                        listener.addClick(view);
                    }
                    view.setEnabled(true);
                }
            });
        }
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public interface OnItemClickListener{
        void deleteClick(View v,int position);
        void addClick(View v);
        void numEdit(View v);
    }
}