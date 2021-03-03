package com.example.androidsubject.MVVM;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.androidsubject.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddNote extends AppCompatActivity {

    public static final String EXTRA_ID =
            "com.codinginflow.architectureexample.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.codinginflow.architectureexample.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.codinginflow.architectureexample.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "com.codinginflow.architectureexample.EXTRA_PRIORITY";
    private EditText editTextTitle, date, time;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;
    Button datepick, timepick;
    Switch setalaram;
    int yr, mnth, day, min, hour;
    String ampm;
    static NotificationManager notificationManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    OneTimeWorkRequest oneTimeWorkRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        sharedPreferences = (SharedPreferences) getSharedPreferences("mytimepreference", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        // notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //    oneTimeWorkRequest=new OneTimeWorkRequest.Builder(Myworker.class).build();
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);
        setalaram = findViewById(R.id.setalaram);
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);
        date = findViewById(R.id.edit_text_date);
        time = findViewById(R.id.edit_text_time);
        datepick = findViewById(R.id.selectdate);
        timepick = findViewById(R.id.selecttime);

        setalaram.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (TextUtils.isEmpty(date.getText()) || TextUtils.isEmpty(time.getText())) {
                        Toast.makeText(AddNote.this, "Please select date and time to set alaram", Toast.LENGTH_SHORT).show();
                        setalaram.setChecked(false);
                    } else {


                        Calendar objCalendar = Calendar.getInstance();

                        objCalendar.set(Calendar.YEAR, yr);

                        objCalendar.set(Calendar.MONTH, mnth);
                        objCalendar.set(Calendar.DAY_OF_MONTH, day);

                        objCalendar.set(Calendar.HOUR_OF_DAY, hour);
                        objCalendar.set(Calendar.MINUTE, min);
                        AlarmManager objAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        Intent alamShowIntent = new Intent(getApplicationContext(), AlaramBroadcast.class);
                        alamShowIntent.putExtra("key", editTextDescription.getText().toString());
                        alamShowIntent.putExtra("msg",editTextTitle.getText().toString());
                        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alamShowIntent, 0);

                        assert objAlarmManager != null;
                        objAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, objCalendar.getTimeInMillis(), alarmPendingIntent);


                    }
                } else {
                    return;
                }
            }
        });
        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calen = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // for 2 mm  ,it will be minute.
                        yr = year;
                        mnth = month;
                        day = dayOfMonth;

                        String myformat = "EEE,d MMM yyyy";
                        DateFormat df = new SimpleDateFormat(myformat, Locale.getDefault());
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        date.setText(df.format(calendar.getTime()));
                        String check = df.format(calendar.getTime());
                        if (check.contains("AM")) {
                            ampm = "AM";
                        }
                        if (check.contains("PM")) {
                            ampm = "PM";
                        }

                    }
                };
                //adding 1 to month because by default it will show 1 less than actual.

                int mYear = calen.get(Calendar.YEAR);
                int mMonth = calen.get(Calendar.MONTH);
                int nDay = calen.get(Calendar.DAY_OF_MONTH);
// using calender class make us to put default date as current date
                DatePickerDialog dp = new DatePickerDialog(AddNote.this, listener, mYear, mMonth, nDay);
                dp.getDatePicker().setMinDate(System.currentTimeMillis());
                dp.show();
            }
        });
        timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cd = Calendar.getInstance();
                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        min = minute;
                        hour = hourOfDay;
                        String myformat = "h:mm a";
                        DateFormat df = new SimpleDateFormat(myformat, Locale.getDefault());
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        time.setText(df.format(calendar.getTime()));
                    }
                };

                int nHour = cd.get(Calendar.HOUR_OF_DAY);
                int nMinute = cd.get(Calendar.MINUTE);
// using calender class make us to put default date as current time
                TimePickerDialog tp = new TimePickerDialog(AddNote.this, listener, nHour, nMinute, false);
                tp.show();
            }
        });
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
            date.setText(intent.getStringExtra("mydate"));
            if (sharedPreferences.getBoolean(intent.getStringExtra(EXTRA_DESCRIPTION), false)) {
                time.setText("");
            } else {
                time.setText(intent.getStringExtra("mytime"));
            }

        } else {
            setTitle("Add Note");
        }
    }

    private void savenote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();
        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);
        data.putExtra("mydate", date.getText().toString());
        data.putExtra("mytime", time.getText().toString());
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                savenote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
