package com.myapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.myapp.dialog.NumberPickerDialog;
import com.myapp.model.Settings;
import com.myapp.utils.FileIO;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
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

    LocalTime startTime;
    LocalTime endTime;

    //Day buttons
    ToggleButton tb1, tb2, tb3, tb4, tb5, tb6, tb7;
    List<ToggleButton> toggleButtonGroup = new ArrayList<>();

    List<Integer> dayList = new ArrayList<>();
    Settings settings;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

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

        return view;
    }

    private void setEvent() {
        cbxRemindWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandLayout(cbxRemindWords.isChecked());
                cancelAlarm();
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
                initTimePicker(settings.getStartTime(), "Select start time");
                materialTimePicker.show(settingsActivity.getSupportFragmentManager(), "start_time_tag");
            }
        });
        lyEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTimePicker(settings.getEndTime(), "Select end time");
                materialTimePicker.show(settingsActivity.getSupportFragmentManager(), "end_time_tag");
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
        if (settings.getRemindDay().contains(8)) {
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
        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (materialTimePicker.getTag().equals("start_time_tag")) {
                    startTime = LocalTime.of(materialTimePicker.getHour(), materialTimePicker.getMinute());
                    tvStartTime.setText(startTime.toString());
                    settings.setStartTime(startTime);
                } else if (materialTimePicker.getTag().equals("end_time_tag")) {
                    endTime = LocalTime.of(materialTimePicker.getHour(), materialTimePicker.getMinute());
                    tvEndTime.setText(endTime.toString());
                    settings.setEndTime(endTime);
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

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                chosenDate = LocalDate.of(year, month, day);
                //etReceiptDate.setText(chosenDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
        };

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue() - 1;
        int day = now.getDayOfMonth();

        //etReceiptDate.setText(now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        datePickerDialog = new DatePickerDialog(requireActivity(), dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel 1";
            String description = "Đây là channel 1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL ID 1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void initNotification(String textTile) {
        //createNotificationChannel();
        String id = "my_channel_id_01";
        NotificationManager manager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = manager.getNotificationChannel(id);
            if (channel == null) {
                channel = new NotificationChannel(id, "Channel Title", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("hi there");
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 1000, 200, 340});
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                manager.createNotificationChannel(channel);
            }
        }

        Intent notificationIntent = new Intent(getContext(), NotificationActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), id)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentTitle("Hello")
                .setContentText("Hi there!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{100, 1000, 200, 340})
                .setSmallIcon(R.drawable.ic_baseline_arrow_right_24)
                .setAutoCancel(false)
                .setTicker("Notification");

        builder.setContentIntent(contentIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(settingsActivity.getApplicationContext());
        notificationManagerCompat.notify(1, builder.build());
    }

    private void cancelAlarm() {
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
        if (alarmManager == null) {
            alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(pendingIntent);
        Toast.makeText(getContext(), "Alarm Cancelled", Toast.LENGTH_SHORT).show();
    }

    private void setAlarm() {
        cancelAlarm();

        List<Integer> days = settings.getRemindDay();


        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

        for (int day : days) {
            List<Calendar> calendarList = setCalendar();
            for (Calendar calendar : calendarList) {
                calendar.set(Calendar.DAY_OF_WEEK, day);

                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY * 7, pendingIntent);
            }
        }

        Toast.makeText(getContext(), "Alarm set successfully", Toast.LENGTH_SHORT).show();
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
            Duration duration = Duration.between(startTime, endTime).dividedBy(settings.getNumberOfRemindADay());

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
}
