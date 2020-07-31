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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class GloriesConfessionsFragment extends Fragment implements View.OnClickListener {

    private TextView notificationConfessions;
    private TextView notificationGlories;
    private EditText editGlories;
    private EditText editConfessions;
    private MaterialButton saveGlories;
    private MaterialButton saveConfessions;
    private SharedPreferences gloriesConfessionsSp;
    private SharedPreferences.Editor gloriesConfessionsSpEditor;
    private String content;

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
        saveGlories = (MaterialButton) view.findViewById(R.id.save_glories);
        saveConfessions = (MaterialButton) view.findViewById(R.id.save_confessions);
        editConfessions = (EditText) view.findViewById(R.id.edit_confessions);
        editGlories = (EditText) view.findViewById(R.id.edit_glories);
        notificationGlories.setOnClickListener(this);
        notificationConfessions.setOnClickListener(this);
        saveGlories.setOnClickListener(this);
        saveConfessions.setOnClickListener(this);
        gloriesConfessionsSp = getActivity().getPreferences(Context.MODE_PRIVATE);
        gloriesConfessionsSpEditor = gloriesConfessionsSp.edit();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_confessions:
                Toast.makeText(getContext(), "已保存", Toast.LENGTH_SHORT).show();
                break;
            case R.id.save_glories:
                Toast.makeText(getContext(), "已保存", Toast.LENGTH_SHORT).show();
                break;
            //保存忏悔录到闹钟提示界面
            case R.id.add_confessions_notification:
                content = editConfessions.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getContext(), "提示内容不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    gloriesConfessionsSpEditor.putString(getString(R.string.confessions_to_show_key), content);
                    gloriesConfessionsSpEditor.apply();
                    Toast.makeText(getContext(), "已应用到闹钟提示界面", Toast.LENGTH_SHORT).show();
                }
                break;
            //保存光荣录到闹钟提示界面
            case R.id.add_glories_notification:
                content = editConfessions.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getContext(), "提示内容不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    gloriesConfessionsSpEditor.putString(getString(R.string.glories_to_show_key), content);
                    gloriesConfessionsSpEditor.apply();
                    Toast.makeText(getContext(), "已应用到闹钟提示界面", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}