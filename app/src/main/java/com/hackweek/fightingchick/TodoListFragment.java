package com.hackweek.fightingchick;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TodoListFragment extends Fragment {
    private static final String TAG = "TodoListFragment";
    private Button addTask ;
    private TextView dateTextView;
    private TextView nickNameTextView;
    private TextView resolutionsTextView;
    private SharedPreferences todoListSp;
    private SharedPreferences.Editor todoListSpEditor;


    public TodoListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        addTask = (Button)view.findViewById(R.id.add_task);
        dateTextView = (TextView)view.findViewById(R.id.top_date_todo);
        nickNameTextView = (TextView)view.findViewById(R.id.top_nickname_todo);
        resolutionsTextView = (TextView)view.findViewById(R.id.top_resolutions_todo);
        addTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Fragment addTaskFragment = new AddTaskFragment();
                MainActivity activity = (MainActivity)getActivity();
                activity.setFragment(addTaskFragment);
            }
        });
        // init sp
        todoListSp = getActivity().getPreferences(Context.MODE_PRIVATE);
        todoListSpEditor = todoListSp.edit();
        //init top view
        nickNameTextView.setText(todoListSp.getString(getString(R.string.nickname_key),"昵称"));
        resolutionsTextView.setText(todoListSp.getString(getString(R.string.resolutions_key),"永动宣言"));
        // set date
        Date today = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("MM'月'dd'日'");
        dateTextView.setText(ft.format(today));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }
}