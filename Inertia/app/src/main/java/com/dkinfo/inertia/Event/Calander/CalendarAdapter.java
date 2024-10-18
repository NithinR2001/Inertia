package com.dkinfo.inertia.Event.Calander;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;


import com.dkinfo.inertia.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarAdapter extends BaseAdapter {
    private final Context context;
    private final List<Integer> days;
    private final LayoutInflater inflater;
    private Calendar calendar;

    public CalendarAdapter(Context context, Calendar calendar) {
        this.context = context;
        this.calendar = calendar;
        days = new ArrayList<>();
        inflater = LayoutInflater.from(context);

        updateCalendar(calendar);
    }

    public void updateCalendar(Calendar calendar) {
        this.calendar = calendar;
        days.clear();

        // Calculate the number of days in the current month
        int currentMonthDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Get the start day of the current month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int startDay = calendar.get(Calendar.DAY_OF_WEEK);

        // Populate the list with days of the current month
        for (int i = 1; i < startDay; i++) {
            days.add(null);
        }
        for (int i = 1; i <= currentMonthDays; i++) {
            days.add(i);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.item_calendar_day, parent, false);
            holder = new ViewHolder();
            holder.dayText = view.findViewById(R.id.dayText);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Integer day = days.get(position);
        if (day == null) {
            holder.dayText.setText("");
            holder.dayText.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        } else {
            holder.dayText.setText(String.valueOf(day));

            // Customize the appearance of the calendar day based on your requirements
            Calendar todayCalendar = Calendar.getInstance();
            if (day == todayCalendar.get(Calendar.DAY_OF_MONTH)
                    && calendar.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH)
                    && calendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) {
                holder.dayText.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                holder.dayText.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            } else {
                holder.dayText.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                holder.dayText.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            }
        }

        return view;
    }


    private static class ViewHolder {
        TextView dayText;
    }
}

