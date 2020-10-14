package cn.allen.warehouse.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.github.lany192.picker.DatePicker;
import com.github.lany192.picker.DateTimePicker;

import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;

public class DateTimePickerDialog {
    private Context context;
    private Calendar c;
    private int type;
    private int myear, mmonthOfYear, mdayOfMonth, mhourOfDay, mminute, msecond;

    private AlertDialog dialog;
    DateTimePicker picker;
    DatePicker datePicker;

    public DateTimePickerDialog(Context context,OnDateTimeChangeListener listener,int type) {
        this.context = context;
        this.type = type;
        this.listener = listener;
        c = Calendar.getInstance();
        myear = c.get(Calendar.YEAR);
        mmonthOfYear = c.get(Calendar.MONTH);
        mdayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        mhourOfDay = c.get(Calendar.HOUR_OF_DAY);
        mminute = c.get(Calendar.MINUTE);
        msecond = c.get(Calendar.SECOND);
        picker = new DateTimePicker(context);
        datePicker = new DatePicker(context);
        dialog = new AlertDialog.Builder(context).setTitle("请选择日期").setView(type==0?datePicker:picker).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                listener.onDateTimeSet(myear, mmonthOfYear, mdayOfMonth, mhourOfDay, mminute, msecond);
            }
        }).create();
        picker.setOnChangedListener(new DateTimePicker.OnChangedListener() {
            @Override
            public void onChanged(DateTimePicker picker, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute, int second) {
                myear = year;
                mmonthOfYear = monthOfYear;
                mdayOfMonth = dayOfMonth;
                mhourOfDay = hourOfDay;
                mminute = minute;
                msecond = second;
            }
        });
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myear = year;
                mmonthOfYear = monthOfYear;
                mdayOfMonth = dayOfMonth;
            }
        });

    }

    public void show(){
        dialog.show();
    }

    private OnDateTimeChangeListener listener;

    public interface OnDateTimeChangeListener{
        void onDateTimeSet(int year,int month,int day,int hour,int minute,int second);
    }
}
