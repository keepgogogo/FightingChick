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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.hackweek.fightingchick.database.FocusList;
import com.hackweek.fightingchick.database.FocusListDao;
import com.hackweek.fightingchick.database.FocusListDataBase;
import com.hackweek.fightingchick.reminder.ReminderActivity;
import com.hackweek.fightingchick.toolpackage.ThreadHelper;
import com.hackweek.fightingchick.utility.TodoNotificationService;

import java.util.Date;
import java.util.UUID;


public class AddTaskFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    MainActivity mainActivity;

    private static final String TAG = "AddTaskFragment";

    private Button cancelAddTask;
    private TimePicker timePicker;
    private EditText editNewTask;
    private CheckBox checkBoxVibrate;
    private CheckBox checkBoxRing;
    private RadioGroup chooseRing;
    private RadioGroup chooseRing2;
    private MaterialRadioButton radioDeepEast;
    private MaterialRadioButton radioLingLingLing;
    private MaterialRadioButton radioQingShiXiang;
    private MaterialRadioButton radioLullaTone;
    private MaterialRadioButton radioGongJi;
    private MaterialRadioButton radioXiaoji;
    private MaterialRadioButton radioYaoGunJi;
    private MaterialRadioButton radioKuaiLeJi;
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
    private int newRing=1;
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
        chooseRing2 = (RadioGroup) view.findViewById(R.id.choose_ring_2);
        chooseRing.setOnCheckedChangeListener(this);
        chooseRing2.setOnCheckedChangeListener(this);
        radioDeepEast = (MaterialRadioButton) view.findViewById(R.id.radio_deepeast);
        radioGongJi = (MaterialRadioButton) view.findViewById(R.id.radio_gongji);
        radioKuaiLeJi = (MaterialRadioButton) view.findViewById(R.id.radio_kuaileji);
        radioLingLingLing = (MaterialRadioButton) view.findViewById(R.id.radio_linglingling);
        radioXiaoji = (MaterialRadioButton) view.findViewById(R.id.radio_xiaoji);
        radioYaoGunJi = (MaterialRadioButton) view.findViewById(R.id.radio_yaogunji);
        radioQingShiXiang = (MaterialRadioButton) view.findViewById(R.id.radio_qingshixiang);
        radioLullaTone = (MaterialRadioButton) view.findViewById(R.id.radio_lullatone);
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
            //处理选择铃声的逻辑
            case R.id.radio_deepeast:
                newRing = 1;
                resetRadioGroup(chooseRing2);
                break;
            case R.id.radio_linglingling:
                newRing = 2;
                resetRadioGroup(chooseRing2);
                break;
            case R.id.radio_qingshixiang:
                newRing = 3;
                resetRadioGroup(chooseRing2);
                break;
            case R.id.radio_lullatone:
                newRing = 4;
                resetRadioGroup(chooseRing2);
                break;
            case R.id.radio_gongji:
                newRing = 5;
                resetRadioGroup(chooseRing);
                break;
            case R.id.radio_xiaoji:
                newRing = 6;
                resetRadioGroup(chooseRing);
                break;
            case R.id.radio_yaogunji:
                newRing = 7;
                resetRadioGroup(chooseRing);
                break;
            case R.id.radio_kuaileji:
                newRing = 8;
                resetRadioGroup(chooseRing);
                break;
        }
    }

    // 应用在2个group切换时
    private void resetRadioGroup(RadioGroup radioGroup){
        radioGroup.setOnCheckedChangeListener(null);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(this);
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
                    radioYaoGunJi.setVisibility(View.INVISIBLE);
                    radioQingShiXiang.setVisibility(View.INVISIBLE);
                    radioXiaoji.setVisibility(View.INVISIBLE);
                    radioLingLingLing.setVisibility(View.INVISIBLE);
                    radioKuaiLeJi.setVisibility(View.INVISIBLE);
                    radioGongJi.setVisibility(View.INVISIBLE);
                    radioLullaTone.setVisibility(View.INVISIBLE);
                    radioDeepEast.setVisibility(View.INVISIBLE);
                    chooseRing.setClickable(false);
                    chooseRing2.setClickable(false);
                } else {
                    radioYaoGunJi.setVisibility(View.VISIBLE);
                    radioQingShiXiang.setVisibility(View.VISIBLE);
                    radioXiaoji.setVisibility(View.VISIBLE);
                    radioLingLingLing.setVisibility(View.VISIBLE);
                    radioKuaiLeJi.setVisibility(View.VISIBLE);
                    radioGongJi.setVisibility(View.VISIBLE);
                    radioLullaTone.setVisibility(View.VISIBLE);
                    radioDeepEast.setVisibility(View.VISIBLE);
                    chooseRing.setClickable(true);
                    chooseRing2.setClickable(true);
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
                if (noticeMethod == 1)
                    newRing = 0;
                if (newInterval == 0)//选择的不是自定义间隔
                    newInterval = getInterval();
                newDayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK);
                newDate = mainActivity.getNewDate();

                // save using Room
                FocusList newFocusList = new FocusList(newDate,
                        newHour, newMinute, 0, newWhatTodo,
                        noticeMethod, newRing, newInterval, newDayOfWeek, 0,
                        currentCalendar.get(Calendar.YEAR),
                        currentCalendar.get(Calendar.MONTH)+1,//系统的month少1
                        currentCalendar.get(Calendar.WEEK_OF_YEAR),
                        currentCalendar.get(Calendar.DAY_OF_MONTH), 0,
                        0, false);
                ThreadHelper newThreadHelper = new ThreadHelper();
                newThreadHelper.insertFocusList(FocusListDataBase.getDatabase(mainActivity), newFocusList);
                Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                // add alarm
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, newHour);
                calendar.set(Calendar.MINUTE, newMinute);
//                AlarmManager alarmManager = (AlarmManager)mainActivity.getSystemService(Context.ALARM_SERVICE);
//                Intent i = new Intent(mainActivity,AlarmReceiver.class);
//
//                PendingIntent pi = PendingIntent.getBroadcast(mainActivity,UUID.fromString(newFocusList.identifier).hashCode(),i,PendingIntent.FLAG_UPDATE_CURRENT);
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_HALF_HOUR,pi);
                Intent i = new Intent(mainActivity, TodoNotificationService.class);
                i.putExtra(MainActivity.TODOUUID,newFocusList.identifier);
                mainActivity.createAlarm(i, UUID.fromString(newFocusList.identifier).hashCode(),calendar.getTimeInMillis());
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




}