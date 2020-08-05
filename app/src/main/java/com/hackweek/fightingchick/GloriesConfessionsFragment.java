package com.hackweek.fightingchick;

import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.hackweek.fightingchick.database.GloryAndConfessionDataBase;
import com.hackweek.fightingchick.database.GloryAndConfessionRecord;
import com.hackweek.fightingchick.toolpackage.ThreadHelper;

public class GloriesConfessionsFragment extends Fragment implements View.OnClickListener {

    private TextView notificationConfessions;
    private TextView notificationGlories;
    private EditText editGlories;
    private EditText editConfessions;
    private Button saveGlories;
    private Button saveConfessions;
    private SharedPreferences gloriesConfessionsSp;
    private SharedPreferences.Editor gloriesConfessionsSpEditor;
    private String content;
    MainActivity mainActivity;

    public GloriesConfessionsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_glories_confessions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        notificationConfessions = (TextView) view.findViewById(R.id.add_confessions_notification);
        notificationGlories = (TextView) view.findViewById(R.id.add_glories_notification);
        saveGlories = (Button) view.findViewById(R.id.save_glories);
        saveConfessions = (Button) view.findViewById(R.id.save_confessions);
        editConfessions = (EditText) view.findViewById(R.id.edit_confessions);
        editGlories = (EditText) view.findViewById(R.id.edit_glories);
        notificationGlories.setOnClickListener(this);
        notificationConfessions.setOnClickListener(this);
        saveGlories.setOnClickListener(this);
        saveConfessions.setOnClickListener(this);
        mainActivity = (MainActivity)getActivity();
        //init SP
        gloriesConfessionsSp = mainActivity.getSharedPreferences(getString(R.string.bigSp_key),Context.MODE_PRIVATE);
        gloriesConfessionsSpEditor = gloriesConfessionsSp.edit();

    }

    private void saveGloriesConfessionsToRoom(GloryAndConfessionRecord gloryAndConfessionRecord){
        ThreadHelper mThreadHelper = new ThreadHelper();
        mThreadHelper.insertGloryAndConfession(
                GloryAndConfessionDataBase.getDataBase(mainActivity),gloryAndConfessionRecord);
    }

    // save to Sp:alarm
    private void saveGloriesConfessionsToAlarm(String mContent){
        if (TextUtils.isEmpty(mContent)) {
            Toast.makeText(getContext(), "提示内容不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        //把sp中的2号光荣/忏悔挪到3,1挪到2,空出1留给最新的
        gloriesConfessionsSpEditor.putString(
                getString(R.string.glories_confessions_to_show_3_key),
                gloriesConfessionsSp.getString(getString(R.string.glories_confessions_to_show_2_key),""));
        gloriesConfessionsSpEditor.putString(
                getString(R.string.glories_confessions_to_show_2_key),
                gloriesConfessionsSp.getString(getString(R.string.glories_confessions_to_show_1_key),""));
        gloriesConfessionsSpEditor.putString(getString(R.string.glories_confessions_to_show_1_key), mContent);
        gloriesConfessionsSpEditor.apply();
        Toast.makeText(getContext(), "已应用到闹钟提示界面", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //保存忏悔录到Room
            case R.id.save_confessions:
                content = editConfessions.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getContext(), "内容不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    GloryAndConfessionRecord newConfession = new GloryAndConfessionRecord(
                            mainActivity.getNewDate(),content,false);
                    saveGloriesConfessionsToRoom(newConfession);
                    Toast.makeText(getContext(), "已保存", Toast.LENGTH_SHORT).show();
                }
                break;
            //保存光荣录到Room
            case R.id.save_glories:
                content = editGlories.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getContext(), "内容不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    GloryAndConfessionRecord newGlory = new GloryAndConfessionRecord(
                            mainActivity.getNewDate(),content,true);
                    saveGloriesConfessionsToRoom(newGlory);
                    Toast.makeText(getContext(), "已保存", Toast.LENGTH_SHORT).show();
                }
                break;
            //保存忏悔录或光荣录到sp
            case R.id.add_confessions_notification:
                content = editConfessions.getText().toString().trim();
                saveGloriesConfessionsToAlarm(content);
                break;
            case R.id.add_glories_notification:
                content = editGlories.getText().toString().trim();
                saveGloriesConfessionsToAlarm(content);
                break;
        }
    }


}