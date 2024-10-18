package com.dkinfo.inertia.Event.ToDo;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dkinfo.inertia.CalanderActivity;
import com.dkinfo.inertia.R;

import java.util.ArrayList;
import java.util.Calendar;

public class ToDoMain extends AppCompatActivity {

    private TodoDbHelper dbHelper;
    private TodoAdapter mAdapter;
    private ArrayList<TodoItem> todoList;
    private String datem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView tv = findViewById(R.id.Dis);
        datem = getIntent().getStringExtra("selectedDay");
        tv.setText(datem);

        dbHelper = new TodoDbHelper(this);
        todoList = new ArrayList<>();
        mAdapter = new TodoAdapter(this, todoList);

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                display(position);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteItem(position);
                return true;
            }
        });

        readItems(datem);

    }

    public void onCheckBoxClicked(View view) {
        CheckBox checkBox = (CheckBox) view;
        boolean isChecked = checkBox.isChecked();

        if (isChecked) {
            showPopUp("Habit completed!");
        } else {
            showPopUp("Habit undone!");
        }
    }
    private void showPopUp(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", null)
                .show();

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(Color.parseColor("#2c653b"));
            }
        });
    }

    private void display(final int position) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_show_task, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);

        final EditText TextTitle = dialogView.findViewById(R.id.text_title);
        final EditText TextDescription = dialogView.findViewById(R.id.text_description);
        final EditText TextDate = dialogView.findViewById(R.id.text_date);
        final EditText TextPriority = dialogView.findViewById(R.id.text_priority);

        TextTitle.setFocusable(false);
        TextDescription.setFocusable(false);
        TextDate.setFocusable(false);
        TextPriority.setFocusable(false);

        TodoItem item = todoList.get(position);
        TextTitle.setText(item.getTitle());
        TextDescription.setText(item.getDescription());
        TextDate.setText(item.getDate());
        TextPriority.setText(item.getPriority());

        dialogBuilder.setTitle("Task")
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        editItem(position);
                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem(position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });


        AlertDialog dialog = dialogBuilder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(Color.parseColor("#2c653b"));

                Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(Color.parseColor("#2c653b"));

                Button neturalButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL);
                neturalButton.setTextColor(Color.parseColor("#2c653b"));

            }
        });
        dialog.show();
    }

    public void onAddItemClick(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_item, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);

        final EditText editTextTitle = dialogView.findViewById(R.id.edit_text_title);
        final EditText editTextDescription = dialogView.findViewById(R.id.edit_text_description);
        final EditText editTextDate = dialogView.findViewById(R.id.edit_text_date);
        final EditText editTextPriority = dialogView.findViewById(R.id.edit_text_priority);

        editTextDate.setFocusable(false);
        editTextDate.setText(datem);

        editTextPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(ToDoMain.this, R.style.TimePickerDialogStyle, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = hourOfDay + ":" + minute;
                        editTextPriority.setText(selectedTime);
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        dialogBuilder.setTitle("Add Item")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String title = editTextTitle.getText().toString().trim();
                        String description = editTextDescription.getText().toString().trim();
                        String date = datem;
                        String priority = editTextPriority.getText().toString().trim();

                        if (!title.isEmpty()) {
                            addItem(title, description, date, priority);
                        } else {
                            Toast.makeText(ToDoMain.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = dialogBuilder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(Color.parseColor("#2c653b"));

                Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(Color.parseColor("#2c653b"));
            }
        });
        dialog.show();
    }

    private void addItem(String title, String description, String date, String priority) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TodoContract.TodoEntry.COLUMN_NAME_TITLE, title);
        values.put(TodoContract.TodoEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(TodoContract.TodoEntry.COLUMN_NAME_DATE, date);
        values.put(TodoContract.TodoEntry.COLUMN_NAME_PRIORITY, priority);

        long newRowId = db.insert(TodoContract.TodoEntry.TABLE_NAME, null, values);
        if (newRowId != -1) {
            TodoItem item = new TodoItem(newRowId, title, description, date, priority);
            todoList.add(item);
            mAdapter.notifyDataSetChanged();
        }

        db.close();
    }

    private void editItem(final int position) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_item, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);

        final EditText editTextTitle = dialogView.findViewById(R.id.edit_text_title);
        final EditText editTextDescription = dialogView.findViewById(R.id.edit_text_description);
        final TextView editTextDate = dialogView.findViewById(R.id.edit_text_date);
        final EditText editTextPriority = dialogView.findViewById(R.id.edit_text_priority);

        TodoItem item = todoList.get(position);
        editTextTitle.setText(item.getTitle());
        editTextDescription.setText(item.getDescription());
        editTextDate.setText(item.getDate());
        editTextPriority.setText(item.getPriority());

        /*editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar  calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(TodoMain.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Set the selected date to the EditText
                        calendar.set(year, monthOfYear, dayOfMonth);
                        String selectedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(calendar.getTime());
                        editTextDate.setText(selectedDate);
                    }
                },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });*/
        editTextDate.setText(datem);
        editTextDate.setFocusable(false);

        editTextPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(ToDoMain.this, R.style.TimePickerDialogStyle, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = hourOfDay + ":" + minute;
                        editTextPriority.setText(selectedTime);
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        dialogBuilder.setTitle("Edit Task")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String title = editTextTitle.getText().toString().trim();
                        String description = editTextDescription.getText().toString().trim();
                        String date = editTextDate.getText().toString().trim();
                        String priority = editTextPriority.getText().toString().trim();

                        if (!title.isEmpty()) {
                            updateItem(position, title, description, date, priority);
                        } else {
                            Toast.makeText(ToDoMain.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNeutralButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        display(position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = dialogBuilder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(Color.parseColor("#2c653b"));

                Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(Color.parseColor("#2c653b"));

                Button neutralButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL);
                neutralButton.setTextColor(Color.parseColor("#2c653b"));
            }
        });
        dialog.show();
    }

    private void updateItem(int position, String title, String description, String date, String priority) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TodoContract.TodoEntry.COLUMN_NAME_TITLE, title);
        values.put(TodoContract.TodoEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(TodoContract.TodoEntry.COLUMN_NAME_DATE, date);
        values.put(TodoContract.TodoEntry.COLUMN_NAME_PRIORITY, priority);

        TodoItem item = todoList.get(position);
        String selection = TodoContract.TodoEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(item.getId())};

        int updatedRows = db.update(TodoContract.TodoEntry.TABLE_NAME, values, selection, selectionArgs);
        if (updatedRows > 0) {
            item.setTitle(title);
            item.setDescription(description);
            item.setDate(date);
            item.setPriority(priority);
            mAdapter.notifyDataSetChanged();
        }

        db.close();
    }

    private void deleteItem(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        TodoItem item = todoList.get(position);
                        String selection = TodoContract.TodoEntry._ID + " = ?";
                        String[] selectionArgs = {String.valueOf(item.getId())};

                        int deletedRows = db.delete(TodoContract.TodoEntry.TABLE_NAME, selection, selectionArgs);
                        if (deletedRows > 0) {
                            todoList.remove(position);
                            mAdapter.notifyDataSetChanged();
                        }

                        db.close();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(Color.parseColor("#2c653b"));

                Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(Color.parseColor("#2c653b"));
            }
        });
        dialog.show();
    }

    /*private void readItems(String datem) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                TodoContract.TodoEntry._ID,
                TodoContract.TodoEntry.COLUMN_NAME_TITLE,
                TodoContract.TodoEntry.COLUMN_NAME_DESCRIPTION,
                TodoContract.TodoEntry.COLUMN_NAME_DATE,
                TodoContract.TodoEntry.COLUMN_NAME_PRIORITY
        };

        Cursor cursor = db.query(
                TodoContract.TodoEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        todoList.clear();

        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_NAME_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_NAME_DESCRIPTION));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_NAME_DATE));
            String priority = cursor.getString(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_NAME_PRIORITY));

            TodoItem item = new TodoItem(itemId, title, description, date, priority);
            todoList.add(item);
        }

        cursor.close();
        db.close();
    }*/
    private void readItems(String datem) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                TodoContract.TodoEntry._ID,
                TodoContract.TodoEntry.COLUMN_NAME_TITLE,
                TodoContract.TodoEntry.COLUMN_NAME_DESCRIPTION,
                TodoContract.TodoEntry.COLUMN_NAME_DATE,
                TodoContract.TodoEntry.COLUMN_NAME_PRIORITY
        };

        String selection = TodoContract.TodoEntry.COLUMN_NAME_DATE + " = ?";
        String[] selectionArgs = {datem};

        Cursor cursor = db.query(
                TodoContract.TodoEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        todoList.clear();

        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_NAME_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_NAME_DESCRIPTION));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_NAME_DATE));
            String priority = cursor.getString(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_NAME_PRIORITY));

            TodoItem item = new TodoItem(itemId, title, description, date, priority);
            todoList.add(item);
        }

        cursor.close();
        db.close();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(ToDoMain.this, CalanderActivity.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}