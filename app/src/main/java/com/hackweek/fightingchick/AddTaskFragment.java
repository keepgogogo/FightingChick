package com.hackweek.fightingchick;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class AddTaskFragment extends Fragment implements View.OnClickListener{

    private Button cancelAddTask;
    private TimePicker timePicker;
    private EditText editNewTask;
    private CheckBox checkBoxVibrate;
    private CheckBox checkBoxRing;
    private RadioGroup chooseRing;
    private RadioGroup chooseInterval;
    private LinearLayout customIntervalLayout;
    private EditText editNewTaskInterval;
    private TextView editInervalText;
    private Button saveNewTask;
    private String newWhatTodo;
    private int newHour;
    private int newMinute;
    private int newInterval;
    private int noticeMethod;//0,1,2,3
    private AlertDialog alertDialog;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cancelAddTask = (Button)view.findViewById(R.id.cancel_add_task);
        cancelAddTask.setOnClickListener(this);
        timePicker = (TimePicker)view.findViewById(R.id.new_task_timepicker);
        editNewTask= (EditText)view.findViewById(R.id.edit_new_task);
        checkBoxVibrate = (CheckBox)view.findViewById(R.id.checkbox_vibrate);
        checkBoxRing = (CheckBox)view.findViewById(R.id.checkbox_ring);
        chooseRing = (RadioGroup)view.findViewById(R.id.choose_ring);
        chooseInterval = (RadioGroup)view.findViewById(R.id.choose_interval);
        chooseRing.setOnClickListener(this);
        customIntervalLayout = (LinearLayout)view.findViewById(R.id.interval_custom_layout);
        editNewTaskInterval = (EditText)view.findViewById(R.id.edit_new_task_interval);

        saveNewTask = (Button)view.findViewById(R.id.save_new_task);
        saveNewTask.setOnClickListener(this);

    }

    private void onRadioButtonClicked(View view){
        switch(view.getId()){
            case R.id.interval_custom:
                if(chooseInterval.getCheckedRadioButtonId()==R.id.interval_custom){
                    customIntervalLayout.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

    //根据checkbox判断
    private int getNoticeMethod(){
        if(checkBoxVibrate.isChecked() && checkBoxRing.isChecked())
            return 2;//ring and vibrate
        if(checkBoxVibrate.isChecked() && !checkBoxRing.isChecked())
            return 1;//vibrate only
        if(!checkBoxVibrate.isChecked() && checkBoxRing.isChecked())
            return 0;//ring only
        return 3;//error

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_add_task:
                //不保存，返回上一界面
                MainActivity mainActivity = (MainActivity)getActivity();
                Fragment todoListFragment = new TodoListFragment();
                mainActivity.setFragment(todoListFragment);
                break;
            case R.id.checkbox_ring:
                //动态展示选择铃声界面
                if(checkBoxRing.isChecked()){
                    chooseRing.setVisibility(View.INVISIBLE);
                    chooseRing.setClickable(false);
                }else{
                    chooseRing.setVisibility(View.VISIBLE);
                    chooseRing.setClickable(true);
                }
                break;
            case R.id.save_new_task:
                //检查任务名
                newWhatTodo = editNewTask.getText().toString();
                if(TextUtils.isEmpty(newWhatTodo)){
                    showAlertDialog("请填写任务名");
                    break;
                }
                //检查提醒方式
                noticeMethod = getNoticeMethod();
                if(noticeMethod == 3){
                    showAlertDialog("请选择提醒方式");
                    break;
                }
                newHour = timePicker.getHour();
                newMinute = timePicker.getMinute();
                newInterval = chooseInterval.getCheckedRadioButtonId();
                break;


        }
    }

    public AddTaskFragment() {
        // Required empty public constructor
    }

    //show AlertDialog in which title is blank, message is the parameter, with a single button to cancel
    public void showAlertDialog(String message){
        alertDialog = new MaterialAlertDialogBuilder(getContext())
                .setMessage(message)
                .setPositiveButton("好的", new DialogInterface.OnClickListener(){
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