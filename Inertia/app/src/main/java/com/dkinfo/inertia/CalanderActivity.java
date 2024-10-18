package com.dkinfo.inertia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.dkinfo.inertia.Event.Calander.CalendarAdapter;
import com.dkinfo.inertia.Event.ToDo.ToDoMain;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class CalanderActivity extends AppCompatActivity {

    private boolean exitDialogShown = false;
    private TextView monthYearText;
    private GridView calendarGrid;
    private CalendarAdapter calendarAdapter;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        monthYearText = findViewById(R.id.monthYearText);
        calendarGrid = findViewById(R.id.calendarGrid);
        ImageButton previousButton = findViewById(R.id.previousButton);
        ImageButton nextButton = findViewById(R.id.nextButton);

        calendar = Calendar.getInstance();

        // Set up the adapter for the calendar grid
        calendarAdapter = new CalendarAdapter(this, calendar);
        calendarGrid.setAdapter(calendarAdapter);

        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        String monthYearString = getMonthForInt(currentMonth) + " " + currentYear;
        monthYearText.setText(monthYearString);

        updateMonthYearTitle();

        // Handle previous button click
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                updateMonthYearTitle();
                calendarAdapter.updateCalendar(calendar);
            }
        });

        // Handle next button click
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                updateMonthYearTitle();
                calendarAdapter.updateCalendar(calendar);
            }
        });


        // Handle grid item click events
        calendarGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click event here
                Integer day = (Integer) calendarAdapter.getItem(position);
                if (day != null) {
                    int clickedDay = day;
                    int clickedMonth = calendar.get(Calendar.MONTH) + 1; // Add +1 to match the month numbering (January is 1)
                    int clickedYear = calendar.get(Calendar.YEAR);

                    String clickedDate = day + "/" + clickedMonth + "/" + clickedYear;

                    Intent i = new Intent(CalanderActivity.this, ToDoMain.class);
                    i.putExtra("selectedDay",clickedDate);
                    startActivity(i);
                }
            }
        });

        calendarGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click event here
                Integer day = (Integer) calendarAdapter.getItem(position);
                if (day != null) {
                    String clickedDate = day + " " + getMonthForInt(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
                    Toast.makeText(CalanderActivity.this, "Selected Date: " + clickedDate, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    private void updateMonthYearTitle() {
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        String monthYearString = getMonthForInt(currentMonth) + " " + currentYear;
        monthYearText.setText(monthYearString);
    }

    private String getMonthForInt(int num) {
        String[] months = new DateFormatSymbols().getMonths();
        return months[num];
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!exitDialogShown) {
                showExitDialog();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        exitApp();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        exitDialogShown = false;
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                exitDialogShown = true;
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                exitDialogShown = false;
            }
        });
        dialog.show();
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        positiveButton.setTextColor(Color.parseColor("#2c653b"));
        negativeButton.setTextColor(Color.parseColor("#2c653b"));
    }

    private void exitApp() {
        finishAffinity(); // Close all activities
        System.exit(0); // Exit the app
    }
}