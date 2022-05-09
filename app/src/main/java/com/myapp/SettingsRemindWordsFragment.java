package com.myapp;

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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SettingsRemindWordsFragment extends Fragment implements NumberPickerDialog.Listener {
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
    ToggleButton tb2, tb3, tb4, tb5, tb6, tb7, tb8;
    List<ToggleButton> toggleButtonGroup = new ArrayList<>();

    List<Integer> dayList = new ArrayList<>();
    Settings settings;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SettingsActivity) {
            settingsActivity = (SettingsActivity) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        toggleButtonGroup.add(tb8);

        settings = settingsActivity.getSettings();
        initToggleButtonGroup();
        expandLayout(settings.isRemindWords());
        tvNumberOfRemindADay.setText(Integer.toString(settings.getNumberOfRemindADay()));
        tvStartTime.setText(settings.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        tvEndTime.setText(settings.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));

        return view;
    }

    private void setEvent() {
        cbxRemindWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandLayout(cbxRemindWords.isChecked());
            }
        });

        lyNumberOfRemindADay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberPickerDialog numberPickerDialog = new NumberPickerDialog("hello");
                numberPickerDialog.show(getParentFragmentManager(), "tag");
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
        tb8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDay();
            }
        });
    }

    private void changeDay() {
        dayList.clear();
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
        if (tb8.isChecked()) {
            dayList.add(8);
        }
        settings.setRemindDay(dayList);
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
            tb8.setChecked(true);
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
        tb8 = (ToggleButton) view.findViewById(R.id.tb8);

        lyOption = view.findViewById(R.id.lyOption);
        lyMain = view.findViewById(R.id.lyMain);
    }

    private void initNotification(String textTile) {
        String id = "hello";
        NotificationManager manager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = manager.getNotificationChannel(id);
            if (channel == null) {
                channel = new NotificationChannel(id, "Channel Title", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("hi there");
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 1000, 200, 340});
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            }
        }

        Intent notificationIntent = new Intent(getContext(), NotificationActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), id)
                .setStyle(new NotificationCompat.BigPictureStyle())
                .setLargeIcon(null)
                .setContentTitle("Hello")
                .setContentText("Hi there!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{100, 1000, 200, 340})
                .setAutoCancel(false)
                .setTicker("Notification");

        builder.setContentIntent(contentIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(settingsActivity.getApplicationContext());
        notificationManagerCompat.notify(1, builder.build());
    }

    @Override
    public void sendDialogResult(int selectNumber) {
        tvNumberOfRemindADay.setText(Integer.toString(selectNumber));
    }
}
