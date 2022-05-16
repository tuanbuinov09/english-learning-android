package com.myapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.myapp.dialog.NumberPickerDialog;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.model.EnWord;
import com.myapp.model.Settings;
import com.myapp.utils.FileIO;
import com.myapp.utils.FileIO3;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SettingsRemindWordsFragment extends Fragment {
    DatePickerDialog datePickerDialog;
    MaterialTimePicker materialTimePicker;
    LocalDate chosenDate = null;
    TextView tvRemindFolders, tvNumberOfRemindADay, tvStartTime, tvEndTime;
    View view;
    SettingsActivity settingsActivity;
    LinearLayout lyRemindFolders, lyNumberOfRemindADay, lyStartTime, lyEndTime, lyOption, lyMain;
    CheckBox cbxRemindWords;
    Button btnTest;

    LocalTime startTime;
    LocalTime endTime;

    //Day buttons
    ToggleButton tb1, tb2, tb3, tb4, tb5, tb6, tb7;
    List<ToggleButton> toggleButtonGroup = new ArrayList<>();

    List<Integer> dayList = new ArrayList<>();
    List<Integer> alarmList = new ArrayList<>();
    Settings settings;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    NotificationManagerCompat notificationManagerCompat;

    private final static String TimePickerLabel1 = "Chọn thời gian bắt đầu";
    private final static String TimePickerLabel2 = "Chọn thời gian kết thúc";
    private final static String TimePickerTag1 = "start_time_tag";
    private final static String TimePickerTag2 = "end_time_tag";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SettingsActivity) {
            settingsActivity = (SettingsActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_remind_words, container, false);

        setControl();
        setEvent();

        toggleButtonGroup.add(tb2);
        toggleButtonGroup.add(tb3);
        toggleButtonGroup.add(tb4);
        toggleButtonGroup.add(tb5);
        toggleButtonGroup.add(tb6);
        toggleButtonGroup.add(tb7);
        toggleButtonGroup.add(tb1);

        settings = settingsActivity.getSettings();

        startTime = settings.getStartTime();
        endTime = settings.getEndTime();

        initToggleButtonGroup();
        expandLayout(settings.isRemindWords());
        tvNumberOfRemindADay.setText(Integer.toString(settings.getNumberOfRemindADay()));
        tvStartTime.setText(startTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        tvEndTime.setText(endTime.format(DateTimeFormatter.ofPattern("HH:mm")));

        notificationManagerCompat = NotificationManagerCompat.from(getContext());

        File path = getContext().getApplicationContext().getFilesDir();
        File file = new File(path, GlobalVariables.FILE_ALARM_SET);

        //CREATE SETTINGS FILE
        if (!file.exists()) {
            FileIO3.writeToFile(new ArrayList<Integer>(), getContext().getApplicationContext());
        }
        alarmList = FileIO3.readFromFile(getContext());

        return view;
    }

    private void setEvent() {
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

        cbxRemindWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandLayout(cbxRemindWords.isChecked());
                if (cbxRemindWords.isChecked()) {
                    setAlarm();
                } else {
                    cancelAlarm();
                }
            }
        });

        lyNumberOfRemindADay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberPickerDialog numberPickerDialog = new NumberPickerDialog("Choose the number of remind a day", settings.getNumberOfRemindADay());
                numberPickerDialog.show(getParentFragmentManager(), "tag");
                //initNotification("Xin chào");
            }
        });
        lyStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTimePicker(settings.getStartTime(), TimePickerLabel1);
                materialTimePicker.show(settingsActivity.getSupportFragmentManager(), TimePickerTag1);
            }
        });
        lyEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTimePicker(settings.getEndTime(), TimePickerLabel2);
                materialTimePicker.show(settingsActivity.getSupportFragmentManager(), TimePickerTag2);
            }
        });
        tb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDay();
            }
        });
        tb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDay();
            }
        });
        tb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDay();
            }
        });
        tb5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDay();
            }
        });
        tb6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDay();
            }
        });
        tb7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDay();
            }
        });
        tb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDay();
            }
        });
    }

    private void changeDay() {
        dayList.clear();
        if (tb1.isChecked()) {
            dayList.add(1);
        }
        if (tb2.isChecked()) {
            dayList.add(2);
        }
        if (tb3.isChecked()) {
            dayList.add(3);
        }
        if (tb4.isChecked()) {
            dayList.add(4);
        }
        if (tb5.isChecked()) {
            dayList.add(5);
        }
        if (tb6.isChecked()) {
            dayList.add(6);
        }
        if (tb7.isChecked()) {
            dayList.add(7);
        }
        settings.setRemindDay(dayList);
        setAlarm();
        FileIO.writeToFile(settings, getContext());
        Log.i("dayList", dayList.toString());
    }

    private void initToggleButtonGroup() {
        for (ToggleButton button : toggleButtonGroup) {
            button.setChecked(false);
        }
        if (settings.getRemindDay().contains(2)) {
            tb2.setChecked(true);
        }
        if (settings.getRemindDay().contains(3)) {
            tb3.setChecked(true);
        }
        if (settings.getRemindDay().contains(4)) {
            tb4.setChecked(true);
        }
        if (settings.getRemindDay().contains(5)) {
            tb5.setChecked(true);
        }
        if (settings.getRemindDay().contains(6)) {
            tb6.setChecked(true);
        }
        if (settings.getRemindDay().contains(7)) {
            tb7.setChecked(true);
        }
        if (settings.getRemindDay().contains(1)) {
            tb1.setChecked(true);
        }
    }

    private void initTimePicker(LocalTime time, String title) {
        MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder();
        builder.setTimeFormat(TimeFormat.CLOCK_24H);
        builder.setHour(time.getHour());
        builder.setMinute(time.getMinute());
        builder.setTitleText(title);
        builder.setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD);

        materialTimePicker = builder.build();
        //materialTimePicker.setStyle(DialogFragment.STYLE_NORMAL, R.style.timePicker);
        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (materialTimePicker.getTag().equals("start_time_tag")) {
                    startTime = LocalTime.of(materialTimePicker.getHour(), materialTimePicker.getMinute());

                    if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
                        Toast.makeText(getContext(), "Thời gian bắt đầu phải sớm hơn thời gian kết thúc", Toast.LENGTH_SHORT).show();

                        initTimePicker(settings.getStartTime(), TimePickerLabel1);
                        materialTimePicker.show(settingsActivity.getSupportFragmentManager(), TimePickerTag1);
                        return;
                    } else {
                        tvStartTime.setText(startTime.toString());
                        settings.setStartTime(startTime);
                    }

                } else if (materialTimePicker.getTag().equals("end_time_tag")) {
                    endTime = LocalTime.of(materialTimePicker.getHour(), materialTimePicker.getMinute());
                    if (endTime.isBefore(startTime) || startTime.equals(endTime)) {
                        Toast.makeText(getContext(), "Thời gian kết thúc phải trễ hơn thời gian bắt đầu", Toast.LENGTH_SHORT).show();

                        initTimePicker(settings.getStartTime(), TimePickerLabel2);
                        materialTimePicker.show(settingsActivity.getSupportFragmentManager(), TimePickerTag2);
                        return;
                    } else {
                        tvEndTime.setText(endTime.toString());
                        settings.setEndTime(endTime);
                    }
                }

                setAlarm();
                FileIO.writeToFile(settings, getContext());
            }
        });
    }

    private void expandLayout(boolean isExpanded) {
        if (isExpanded) {
            lyOption.setVisibility(View.VISIBLE);
            lyMain.setBackground(getContext().getDrawable(R.drawable.linear_layout_border_bottom));
        } else {
            lyOption.setVisibility(View.GONE);
            lyMain.setBackground(null);
        }
        cbxRemindWords.setChecked(isExpanded);
        settings.setRemindWords(isExpanded);
        FileIO.writeToFile(settings, getContext());
    }

    private void setAlarm() {
        cancelAlarm();
        List<Integer> days = settings.getRemindDay();
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        Calendar today = Calendar.getInstance();

        for (int day : days) {
            List<Calendar> calendarList = setCalendar();
            for (Calendar calendar : calendarList) {
                if (today.get(Calendar.DAY_OF_WEEK) != day) {
                    calendar.set(Calendar.DAY_OF_WEEK, day);
                }

                int alarmId = 0;
                do {
                    alarmId = random(1, 5000);
                }
                while (alarmList.contains(alarmId));

                Intent intent = new Intent(getContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), alarmId, intent, 0);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY * 7, pendingIntent);

                alarmList.add(alarmId);
            }
        }

        FileIO3.writeToFile(alarmList, getContext());
        Toast.makeText(getContext(), "Notification set successfully", Toast.LENGTH_SHORT).show();
    }

    private void cancelAlarm() {
        for (int alarmId : alarmList
        ) {
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), alarmId, intent, 0);
            if (alarmManager == null) {
                alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            }
            alarmManager.cancel(pendingIntent);
        }

        alarmList.clear();
        FileIO3.writeToFile(alarmList, getContext());
        Toast.makeText(getContext(), "Alarm Cancelled", Toast.LENGTH_SHORT).show();
    }

    private List<Calendar> setCalendar() {
        List<Calendar> calendarList = new ArrayList<>();


        if (settings.getNumberOfRemindADay() == 1) {
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, startTime.getHour());
            calendar.set(Calendar.MINUTE, startTime.getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            calendarList.add(calendar);

        } else if (settings.getNumberOfRemindADay() == 2) {
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, startTime.getHour());
            calendar.set(Calendar.MINUTE, startTime.getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            calendarList.add(calendar);

            calendar.set(Calendar.HOUR_OF_DAY, endTime.getHour());
            calendar.set(Calendar.MINUTE, endTime.getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            calendarList.add(calendar);

        } else if (settings.getNumberOfRemindADay() > 2) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, startTime.getHour());
            calendar.set(Calendar.MINUTE, startTime.getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            calendarList.add(calendar);

            calendar.set(Calendar.HOUR_OF_DAY, endTime.getHour());
            calendar.set(Calendar.MINUTE, endTime.getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            calendarList.add(calendar);

            Duration duration = Duration.between(startTime, endTime).dividedBy(settings.getNumberOfRemindADay());
            LocalTime addedTime = startTime;

            for (int i = 0; i < settings.getNumberOfRemindADay() - 2; i++) {
                addedTime = addedTime.plus(duration);

                calendar.set(Calendar.HOUR_OF_DAY, addedTime.getHour());
                calendar.set(Calendar.MINUTE, addedTime.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                calendarList.add(calendar);
            }
        }
        return calendarList;
    }

    public void sendDialogResult(int selectNumber) {
        tvNumberOfRemindADay.setText(Integer.toString(selectNumber));
        settings.setNumberOfRemindADay(selectNumber);
        FileIO.writeToFile(settings, getContext());
    }

    private void scheduleAlarm(int dayOfWeek) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        // Check we aren't setting it in the past which would trigger it to fire instantly
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }

        // Set this to whatever you were planning to do at the given time
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY * 7, pendingIntent);
    }

    private void setControl() {
        tvRemindFolders = view.findViewById(R.id.tvRemindFolders);
        tvNumberOfRemindADay = view.findViewById(R.id.tvNumberOfRemindADay);
        tvStartTime = view.findViewById(R.id.tvStartTime);
        tvEndTime = view.findViewById(R.id.tvEndTime);

        lyRemindFolders = view.findViewById(R.id.lyRemindFolders);
        lyNumberOfRemindADay = view.findViewById(R.id.lyNumberOfRemindADay);
        lyStartTime = view.findViewById(R.id.lyStartTime);
        lyEndTime = view.findViewById(R.id.lyEndTime);

        cbxRemindWords = view.findViewById(R.id.cbxRemindWords);
        btnTest = view.findViewById(R.id.btnTest);

        tb2 = (ToggleButton) view.findViewById(R.id.tb2);
        tb3 = (ToggleButton) view.findViewById(R.id.tb3);
        tb4 = (ToggleButton) view.findViewById(R.id.tb4);
        tb5 = (ToggleButton) view.findViewById(R.id.tb5);
        tb6 = (ToggleButton) view.findViewById(R.id.tb6);
        tb7 = (ToggleButton) view.findViewById(R.id.tb7);
        tb1 = (ToggleButton) view.findViewById(R.id.tb8);

        lyOption = view.findViewById(R.id.lyOption);
        lyMain = view.findViewById(R.id.lyMain);
    }

    private int getNotificationId() {
        return (int) new Date().getTime();
    }

    private int random(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public EnWord getWord(Integer enWordId) {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext().getApplicationContext());
        databaseAccess.open();
        EnWord enWord = databaseAccess.getOneEnWord(enWordId);
        databaseAccess.close();
        return enWord;
    }
}
