package allen.frame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import allen.frame.R;
import allen.frame.entry.City;

public class CityAdapter extends BaseAdapter {

	private List<City> list;
	private Context context;
	private boolean ismore = false;
	private boolean isShow = false;
	private int mindex = -1;
	private HashMap<Integer, Boolean> isSelect;
	public CityAdapter(Context context){
		isSelect = new HashMap<Integer, Boolean>();
		this.context = context;
	}
	
	public void setData(List<City> list){
		this.list = list;
		if(isShow){
			isSelect = new HashMap<Integer, Boolean>();
			int len = list==null?0:list.size();
			for(int i=0;i<len;i++){
				isSelect.put(i, false);
			}
		}
		notifyDataSetChanged();
	}
	
	public void setShow(boolean isShow){
		this.isShow = isShow;
		isSelect = new HashMap<Integer, Boolean>();
		int len = list==null?0:list.size();
		for(int i=0;i<len;i++){
			isSelect.put(i, false);
		}
		notifyDataSetChanged();
	}
	public CityAdapter(Context context, List<City> list){
		isSelect = new HashMap<Integer, Boolean>();
		this.context = context;
		this.list = list;
	}
	public CityAdapter(Context context, List<City> list, boolean ismore) {
		isSelect = new HashMap<Integer, Boolean>();
		this.context = context;
		this.list = list;
		this.ismore = ismore;
		int len = list==null?0:list.size();
		for(int i=0;i<len;i++){
			isSelect.put(i, false);
		}
	}
	private void setClick(int index){
		if(ismore){
			boolean ischoice = isSelect.get(index);
			isSelect.put(index, !ischoice);
		}else{
			int len = list==null?0:list.size();
			for(int i=0;i<len;i++){
				if(i==index){
					mindex = index;
					isSelect.put(i, true);
				}else{
					isSelect.put(i, false);
				}
			}
		}
		notifyDataSetChanged();
	}
	
	public HashMap<Integer, Boolean> getSelect(){
		return isSelect;
	}
	
	public City getSingle(){
		if(!ismore&&mindex>=0){
			return list.get(mindex);
		}
		return null;
	}
	
	@Override
	public int getCount() {
		return list==null?0:list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int arg0, View v, ViewGroup arg2) {
		HoldView hv;
		if(v==null){
			hv = new HoldView();
			v = LayoutInflater.from(context).inflate(R.layout.alen_type_item, null);
			hv.name = (TextView) v.findViewById(R.id.name_tv);
			v.setTag(hv);
		}else{
			hv = (HoldView) v.getTag();
		}
		hv.name.setText(list.get(arg0).getName());
		if(isShow){
			if(isSelect.get(arg0)){
				hv.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cxxt_checked_true,
						0, 0, 0);
			}else{
				hv.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cxxt_checked_false,
						0, 0, 0);
			}
			hv.name.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setClick(arg0);
				}
			});
		}
		if(ismore){
			if(isSelect.get(arg0)){
				hv.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cxxt_checked_true,
						0, 0, 0);
			}else{
				hv.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cxxt_checked_false,
						0, 0, 0);
			}
			hv.name.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setClick(arg0);
				}
			});
		}
		return v;
	}
	class HoldView {
		public TextView name;
	}
}
