package com.hackweek.fightingchick;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.hackweek.fightingchick.database.FocusList;
import com.hackweek.fightingchick.database.FocusListDao;
import com.hackweek.fightingchick.database.FocusListDataBase;
import com.hackweek.fightingchick.toolpackage.ThreadHelper;

import java.util.Date;


public class AddTaskFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    MainActivity mainActivity;

    private static final String TAG = "AddTaskFragment";

    private Button cancelAddTask;
    private TimePicker timePicker;
    private EditText editNewTask;
    private CheckBox checkBoxVibrate;
    private CheckBox checkBoxRing;
    private RadioGroup chooseRing;
    private MaterialRadioButton radioMildRing;
    private MaterialRadioButton radioCrazyRing;
    private MaterialRadioButton radioHappyRing;
    private MaterialRadioButton radioScreamRing;
    private RadioGroup chooseInterval;
    private MaterialRadioButton radioIntervalCustom;
    private EditText editNewTaskInterval;
    private TextView editInervalText;
    private Button saveNewTask;
    private String newWhatTodo;
    private Calendar currentCalendar;
    private int newHour;
    private int newMinute;
    private int newInterval;
    private int newDate;
    private int newRing;
    private int noticeMethod;//0,1,2,3
    private int newDayOfWeek;
    private AlertDialog alertDialog;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        cancelAddTask = (Button) view.findViewById(R.id.cancel_add_task);
        cancelAddTask.setOnClickListener(this);
        timePicker = (TimePicker) view.findViewById(R.id.new_task_timepicker);
        timePicker.setIs24HourView(true);
        editNewTask = (EditText) view.findViewById(R.id.edit_new_task);
        checkBoxVibrate = (CheckBox) view.findViewById(R.id.checkbox_vibrate);
        checkBoxRing = (CheckBox) view.findViewById(R.id.checkbox_ring);
        checkBoxRing.setOnClickListener(this);
        // choose ring
        chooseRing = (RadioGroup) view.findViewById(R.id.choose_ring);
        radioCrazyRing = (MaterialRadioButton) view.findViewById(R.id.radio_crazy_ring);
        radioMildRing = (MaterialRadioButton) view.findViewById(R.id.radio_mild_ring);
        radioHappyRing = (MaterialRadioButton) view.findViewById(R.id.radio_happy_ring);
        radioScreamRing = (MaterialRadioButton) view.findViewById(R.id.radio_scream_ring);
        // Interval
        chooseInterval = (RadioGroup) view.findViewById(R.id.choose_interval);
        chooseInterval.setOnCheckedChangeListener(this);
        radioIntervalCustom = (MaterialRadioButton) view.findViewById(R.id.radio_interval_custom);
        editNewTaskInterval = (EditText) view.findViewById(R.id.edit_new_task_interval);
        editInervalText = (TextView) view.findViewById(R.id.edit_interval_text);
        // save task
        saveNewTask = (Button) view.findViewById(R.id.save_new_task);
        saveNewTask.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            // 选择自定义响铃间隔
            case R.id.radio_interval_custom:
                editNewTaskInterval.setVisibility(View.VISIBLE);
                editInervalText.setVisibility(View.VISIBLE);
                break;
            //选择其它时间间隔，隐藏两个控件
            case R.id.radio_interval_3min:
            case R.id.radio_interval_5min:
            case R.id.radio_interval_10min:
                hideCustomView();
                break;
        }
    }

    //隐藏自定义响铃间隔的两个控件
    private void hideCustomView() {
        editNewTaskInterval.setVisibility(View.GONE);
        editInervalText.setVisibility(View.GONE);
    }


    //根据checkbox判断响铃/震动
    private int getNoticeMethod() {
        if (checkBoxVibrate.isChecked() && checkBoxRing.isChecked())
            return 2;//ring and vibrate
        if (checkBoxVibrate.isChecked() && !checkBoxRing.isChecked())
            return 1;//vibrate only
        if (!checkBoxVibrate.isChecked() && checkBoxRing.isChecked())
            return 0;//ring only
        return 3;//error

    }

    //判断铃声
    private int getNoticeRing() {
        if (noticeMethod == 1)
            return 0;//vibrate only
        switch (chooseRing.getCheckedRadioButtonId()) {
            case R.id.radio_mild_ring:
                return 1;
            case R.id.radio_crazy_ring:
                return 2;
            case R.id.radio_happy_ring:
                return 3;
            case R.id.radio_scream_ring:
                return 4;
            default:
                return 0;

        }
    }

    //判断间隔
    private int getInterval() {
        switch (chooseInterval.getCheckedRadioButtonId()) {
            case R.id.radio_interval_3min:
                return 3;
            case R.id.radio_interval_5min:
                return 5;
            case R.id.radio_interval_10min:
                return 10;
            default:
                return 0;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_add_task:
                //不保存，返回上一界面
                Fragment todoListFragment = new TodoListFragment();
                mainActivity.setFragment(todoListFragment);
                break;
            case R.id.checkbox_ring:
                //动态展示选择铃声界面
                if (!checkBoxRing.isChecked()) {
                    radioScreamRing.setVisibility(View.INVISIBLE);
                    radioHappyRing.setVisibility(View.INVISIBLE);
                    radioMildRing.setVisibility(View.INVISIBLE);
                    radioCrazyRing.setVisibility(View.INVISIBLE);
                    chooseRing.setClickable(false);
                } else {
                    radioCrazyRing.setVisibility(View.VISIBLE);
                    radioMildRing.setVisibility(View.VISIBLE);
                    radioScreamRing.setVisibility(View.VISIBLE);
                    radioHappyRing.setVisibility(View.VISIBLE);
                    chooseRing.setClickable(true);
                }
                break;
            case R.id.save_new_task:
                //检查时间在此刻之后
                newHour = timePicker.getHour();
                newMinute = timePicker.getMinute();
                Calendar currentCalendar = Calendar.getInstance();
                if (newHour < currentCalendar.get(Calendar.HOUR_OF_DAY)
                        || newHour == currentCalendar.get(Calendar.HOUR_OF_DAY)
                        && newMinute <= currentCalendar.get(Calendar.MINUTE)) {
                    showAlertDialog("请选择恰当的时间！");
                    break;
                }
                //检查任务名
                newWhatTodo = editNewTask.getText().toString();
                if (TextUtils.isEmpty(newWhatTodo)) {
                    showAlertDialog("请填写任务名");
                    break;
                }
                //检查提醒方式
                noticeMethod = getNoticeMethod();
                if (noticeMethod == 3) {
                    showAlertDialog("请选择提醒方式");
                    break;
                }
                //检查自定义的时间
                newInterval = 0;
                if (chooseInterval.getCheckedRadioButtonId() == R.id.radio_interval_custom) {
                    String newIntervalText = editNewTaskInterval.getText().toString();
                    if (TextUtils.isEmpty(newIntervalText)) {
                        showAlertDialog("请输入响铃间隔");
                        break;
                    } else {
                        newInterval = Integer.parseInt(newIntervalText);
                    }
                }
                newRing = getNoticeRing();
                if (newInterval == 0)//选择的不是自定义间隔
                    newInterval = getInterval();
                newDayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK);
                newDate = mainActivity.getNewDate();

                // save using Room
                FocusList newFocusList = new FocusList(newDate,
                        newHour, newMinute, 0, newWhatTodo,
                        noticeMethod, newRing, newInterval, newDayOfWeek,0,
                        currentCalendar.get(Calendar.YEAR),
                        currentCalendar.get(Calendar.MONTH),
                        currentCalendar.get(Calendar.WEEK_OF_YEAR),
                        currentCalendar.get(Calendar.DAY_OF_MONTH),0);
                ThreadHelper newThreadHelper = new ThreadHelper();
                newThreadHelper.insertFocusList(FocusListDataBase.getDatabase(mainActivity), newFocusList);
                Toast.makeText(getContext(),"保存成功",Toast.LENGTH_SHORT).show();
                mainActivity.setFragment(new TodoListFragment());
                break;

        }
    }

    public AddTaskFragment() {
        // Required empty public constructor
    }

    //show AlertDialog in which title is blank, message is the parameter, with a single button to cancel
    public void showAlertDialog(String message) {
        alertDialog = new MaterialAlertDialogBuilder(getContext())
                .setMessage(message)
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.cancel();
                    }
                })
                .create();
        alertDialog.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    private AlarmManager getAlarmManager() {
        return (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
    }

    private void createAlarm(Intent i, int requestCode, long timeInMillis) {
        AlarmManager am = getAlarmManager();
        PendingIntent pi = PendingIntent.getService(getContext(), requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, timeInMillis, pi);
        Log.d(TAG, "createAlarm " + requestCode + " time: " + timeInMillis + " PI " + pi.toString());
    }

    private boolean doesPendingIntentExist(Intent i, int requestCode) {
        PendingIntent pi = PendingIntent.getService(getContext(), requestCode, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }


    private void deleteAlarm(Intent i, int requestCode) {
        if (doesPendingIntentExist(i, requestCode)) {
            PendingIntent pi = PendingIntent.getService(getContext(), requestCode, i, PendingIntent.FLAG_NO_CREATE);
            pi.cancel();
            getAlarmManager().cancel(pi);
            Log.d(TAG, "PI Cancelled " + doesPendingIntentExist(i, requestCode));
        }
    }


}