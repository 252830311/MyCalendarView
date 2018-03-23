package com.study.gyl.mycalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 作者：Administrator on 2018/03/19 19:38
 * 邮箱：252830311@qq.com
 */
public class NewCalendar extends LinearLayout {
    private ImageView btnPre;
    private ImageView btnNext;
    private TextView txtDate;
    private GridView grid;
    private Calendar curDate = Calendar.getInstance();
    private String displayFormat;
    private NewCalendarListener listener;

    public void setOnNewCalendarListener(NewCalendarListener listener) {
        this.listener = listener;
    }

    public NewCalendar(Context context) {
        super(context);
    }

    public NewCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, null);
    }

    public NewCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    private void initControl(Context context, AttributeSet attrs) {
        bindControl(context);
        bindControlEvent();
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.NewCalendar);

        try {
            String format = ta.getString(R.styleable.NewCalendar_dateFormat);
            displayFormat = format;
            if (displayFormat == null) {
                displayFormat = "MMM yyyy";
            }
        } finally {
            ta.recycle();
        }
        renderCalendar();


    }

    private void bindControlEvent() {
        btnPre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                curDate.add(Calendar.MONTH, -1);
                renderCalendar();
            }
        });
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                curDate.add(Calendar.MONTH, 1);
                renderCalendar();
            }
        });
    }

    private void renderCalendar() {
        SimpleDateFormat format = new SimpleDateFormat(displayFormat);
        txtDate.setText(format.format(curDate.getTime()));
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) curDate.clone();
        //将日历设置到当月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //获得当月第一天是星期几，如果是星期一则返回1此时1-1=0证明上个月没有多余天数
        int prevDays = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        //将calendar在1号的基础上向前推prevdays天。
        calendar.add(Calendar.DAY_OF_MONTH, -prevDays);
        int maxCellCount = 6 * 7;
        while (cells.size() < maxCellCount) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        grid.setAdapter(new CalendarAdapter(getContext(), cells));
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listener == null) {
                    return false;
                }
                listener.onItemLongPress((Date) adapterView.getItemAtPosition(i));
                return true;
            }
        });
    }

    private class CalendarAdapter extends ArrayAdapter<Date> {

        LayoutInflater inflater;

        public CalendarAdapter(@NonNull Context context, ArrayList<Date> list) {
            super(context, R.layout.calendar_text_day, list);
            inflater = LayoutInflater.from(context);

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Date date = getItem(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.calendar_text_day, parent, false);
            }
            int day = date.getDate();
            ((TextView) convertView).setText(String.valueOf(day));


//            Calendar calendar = (Calendar) curDate.clone();
//            calendar.set(Calendar.DAY_OF_MONTH,1);
//            int daysInMoth = calendar.getActualMaximum(Calendar.DATE);
            Date now = new Date();
            boolean isTheSameMonth = false;
            //判断是否是当前月份
            if (date.getMonth() == now.getMonth()) {
                isTheSameMonth = true;
            }
            if (isTheSameMonth) {
                //本月的日期
                ((CalendarTextView) convertView).setTextColor(Color.parseColor("#000000"));
            } else {
                ((CalendarTextView) convertView).setTextColor(Color.parseColor("#666666"));
            }


            if (now.getDate() == date.getDate() && now.getMonth() == date.getMonth()
                    && now.getYear() == date.getYear()) {
                //当天属于选中状态
                ((CalendarTextView) convertView).setTextColor(Color.parseColor("#ff0000"));
                ((CalendarTextView) convertView).isToday = true;

            }
            return convertView;
        }
    }

    private void bindControl(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.calendar_view, this);
        btnPre = findViewById(R.id.iv_prev);
        btnNext = findViewById(R.id.iv_next);
        txtDate = findViewById(R.id.tv_date);
        grid = findViewById(R.id.calendar_grid);
    }

    public interface NewCalendarListener {
        void onItemLongPress(Date day);
    }
}
