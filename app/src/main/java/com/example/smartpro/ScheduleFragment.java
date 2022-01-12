package com.example.smartpro;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.smartpro.databinding.ActivityMain2Binding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

public class ScheduleFragment extends Fragment {
    String dataString;
    String condition;
    private MaterialTimePicker picker;
    private Calendar calendar;
    TextView selectedText;
    Button selectedTimeBtn;
    Button setAlarmBtn;
    Button cancelAlarmBtn;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;


    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_schedule, container, false);
        WebView myWebView = myView.findViewById(R.id.webView3);
        myWebView.loadUrl("file:///android_asset/weatheriframe.html");
        myWebView.getSettings().setJavaScriptEnabled(true);

        selectedText = myView.findViewById(R.id.selectedTime);
        selectedTimeBtn = myView.findViewById(R.id.selectTimeBtn);
        setAlarmBtn = myView.findViewById(R.id.setAlarmBtn);
        cancelAlarmBtn = myView.findViewById(R.id.cancelAlarmBtn);

        DatabaseReference database2 = FirebaseDatabase.getInstance().getReference().child("weather");
        database2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                condition = Objects.requireNonNull(snapshot.getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        selectedTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        setAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });


        cancelAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        return myView;
    }

    private void cancelAlarm() {
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
        if (alarmManager == null) {
            alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(getContext(), "Scheduling Canceled", Toast.LENGTH_SHORT).show();

    }

    private void setAlarm() {
        if(condition.equals("Irrigation Allowed")){
            createNotificationChannel();
            alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getActivity(), AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Toast.makeText(getContext(), "Irrigation Scheduled Successfully", Toast.LENGTH_SHORT).show();

        }
        else {
            createWeatherNotificationChannel();
            alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getActivity(), IrrigationReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Toast.makeText(getContext(), "Irrigation Scheduling Successful", Toast.LENGTH_SHORT).show();

        }

    }

    private void showTimePicker() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Scheduling Time")
                .build();
        picker.show(requireActivity().getSupportFragmentManager(), picker.toString());
        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onClick(View v) {
                if (picker.getHour() > 12) {
                    dataString= String.valueOf(picker.getHour())+ picker.getMinute() +"PM";
                    selectedText.setText(String.format("%02d:%02d", picker.getHour(), picker.getMinute()) + " PM");

                } else {
                    dataString= String.valueOf(picker.getHour())+ picker.getMinute() +"AM";
                    selectedText.setText(String.format("%02d:%02d", picker.getHour(), picker.getMinute()) + " AM");
                }

                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                calendar.set(Calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

            }
        });
    }


    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
           CharSequence name= "IrrigationSchedulerChannel";
            String description= "Channel for Scheduler";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("smartFarmer",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
   }

    private void createWeatherNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "weatherSchedulerChannel";
            String description = "Channel for Weather Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("weather", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


    }
}
