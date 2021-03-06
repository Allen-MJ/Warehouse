package cn.allen.warehouse.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import allen.frame.tools.MsgUtils;
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
        if(list!=null){
            this.list = list;
        }
        notifyDataSetChanged();
        if(listener!=null){
            listener.numEdit();
        }
    }

    public void addList(List<Flower> list){
        if(list!=null){
            this.list.addAll(list);
        }
        notifyDataSetChanged();
        if(listener!=null){
            listener.numEdit();
        }
    }
    public void addList(Flower entry){
        entry.setScheduled_quantity(1);
        list.add(entry);
        notifyDataSetChanged();
        if(listener!=null){
            listener.numEdit();
        }
    }

    public void delete(int index){
        list.remove(index);
        notifyDataSetChanged();
        if(listener!=null){
            listener.numEdit();
        }
    }

    public boolean checkIsOk(){
        boolean isok = true;
        for(Flower flower:list){
            isok = isok&&flower.getScheduled_quantity()!=0;
        }
        return true;
    }

    public String getChoice(){
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for(Flower flower:list){
            sb.append(",{");
            sb.append("\"id\":");
            sb.append(flower.getId()+",");
            sb.append("\"name\":");
            sb.append("\""+(flower.getFlower_id()>0?flower.getFlower_name():flower.getName())+"\",");
            sb.append("\"count\":");
            sb.append(flower.getScheduled_quantity());
            sb.append("}");
        }
        sb.append("]");
        return sb.toString().replaceFirst(",","");
    }

    public String getXGChoice(){
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for(Flower flower:list){
            sb.append(",{");
            sb.append("\"id\":");
            sb.append((flower.getFlower_id()!=0?flower.getFlower_id():flower.getId())+",");
            sb.append("\"name\":");
            sb.append("\""+(flower.getFlower_id()>0?flower.getFlower_name():flower.getName())+"\",");
            sb.append("\"count\":");
            sb.append(flower.getScheduled_quantity());
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
            money = money + flower.getRent()*flower.getScheduled_quantity();
        }
        String s=String.format("%.1f",money);
        return Float.valueOf(s);
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

        private AppCompatTextView name,stock,sort;
        private AppCompatImageView delete;
        private AppCompatEditText rent;
        private View view,deleteView;
        public ObjectHolder(@NonNull View itemView) {
            super(itemView);
            sort = itemView.findViewById(R.id.choice_item_sort);
            name = itemView.findViewById(R.id.choice_item_name);
            rent = itemView.findViewById(R.id.choice_item_rent);
            stock = itemView.findViewById(R.id.choice_item_stock);
            delete = itemView.findViewById(R.id.choice_item_delete);
            view = itemView.findViewById(R.id.item_layout);
            deleteView = itemView.findViewById(R.id.item_delete);
        }
        public void bind(final Flower entry,int position){
            if(entry!=null){
                sort.setText(""+(position+1));
                name.setText(entry.getFlower_id()>0?entry.getFlower_name():entry.getName());
                stock.setText(String.valueOf(entry.getStock()));
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
                /*if(rent.getTag() instanceof TextWatcher){
                    rent.removeTextChangedListener((TextWatcher) rent.getTag());
                }*/
                rent.setText(String.valueOf(entry.getScheduled_quantity()));
                TextWatcher watcher =new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(position<list.size()){
//                            String num = StringUtils.empty(charSequence.toString())?"0":charSequence.toString();
//                            int count = Integer.parseInt(num);
//                            if(count>entry.getStock()){
//                                MsgUtils.showShortToast(view.getContext(),"库存不足!");
//                                list.get(position).setScheduled_quantity(entry.getStock());
//                                rent.setText(""+entry.getStock());
//                            }else if(count==0){
//                                list.get(position).setScheduled_quantity(0);
//                                rent.setText("");
//                            }else{
//                                list.get(position).setScheduled_quantity(count);
//                                rent.setText(String.valueOf(count));
//                            }
//                            rent.setSelection(rent.getText().length());
//                            if(listener!=null){
//                                listener.numEdit();
//                            }
                            if(!String.valueOf(list.get(position).getScheduled_quantity()==0?"":list.get(position).getScheduled_quantity()).equals(charSequence.toString())){
                                String num = StringUtils.empty(charSequence.toString())?"0":charSequence.toString();
                                int count = Integer.parseInt(num);
                                if(count>entry.getStock()){
                                    MsgUtils.showShortToast(view.getContext(),"库存不足!");
                                    list.get(position).setScheduled_quantity(entry.getStock());
                                    rent.setText(""+entry.getStock());
                                }else if(count==0){
                                    list.get(position).setScheduled_quantity(0);
                                    rent.setText("");
                                }else{
                                    list.get(position).setScheduled_quantity(count);
                                    rent.setText(String.valueOf(count));
                                }
                                rent.setSelection(rent.getText().length());
                                if(listener!=null){
                                    listener.numEdit();
                                }
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                };
                rent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        AppCompatEditText v = (AppCompatEditText) view;
                        if(b){
                            v.addTextChangedListener(watcher);
                        }else{
                            v.removeTextChangedListener(watcher);
                        }
                    }
                });
//                rent.addTextChangedListener(watcher);
//                rent.setTag(watcher);
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
        void numEdit();
    }
}