package com.hackweek.fightingchick;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hackweek.fightingchick.database.FocusList;
import com.hackweek.fightingchick.database.FocusListDao;
import com.hackweek.fightingchick.database.FocusListDataBase;



import com.hackweek.fightingchick.recycler.ToDoListAdapter;
import com.hackweek.fightingchick.recycler.ToDoListViewModel;
import com.hackweek.fightingchick.toolpackage.ThreadHelper;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.hackweek.fightingchick.MineFragment.HEAD_PORTRAIT;


public class TodoListFragment extends Fragment implements View.OnClickListener{

    final int UPDATE_UI=1;
    final int DELETE_PLAN=2;
    final int APPEND_EVERY_DAY=3;

    private static final String TAG = "TodoListFragment";
    private Button addTask ;
    private TextView dateTextView;
    private TextView nickNameTextView;
    private TextView resolutionsTextView;
    private SharedPreferences todoListSp;
    private SharedPreferences.Editor todoListSpEditor;
    private MainActivity mainActivity;
    private ToDoListFragmentHandler handler;

    private RecyclerView recyclerView;
    private ToDoListAdapter adapter;
    private ToDoListViewModel viewModel;
    private RecyclerView.LayoutManager layoutManager;
    private ThreadHelper threadHelper;
    private FocusListDataBase focusListDataBase;
    private FocusListDao focusListDao;
    private ImageView background;
    private TextView completedTextView;
    private CircleImageView topProfile;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mainActivity=(MainActivity)getActivity();
        focusListDataBase=FocusListDataBase.getDatabase(mainActivity);
        focusListDao=focusListDataBase.FocusListDao();
        handler=new ToDoListFragmentHandler();
        threadHelper=new ThreadHelper();

        background=(ImageView)view.findViewById(R.id.todo_bg);
        completedTextView=(TextView)view.findViewById(R.id.todo_completed);
        addTask = (Button)view.findViewById(R.id.add_task);
        dateTextView = (TextView)view.findViewById(R.id.top_date_todo);
        nickNameTextView = (TextView)view.findViewById(R.id.top_nickname_todo);
        topProfile=(CircleImageView)view.findViewById(R.id.top_profile_todo);

        if(isPortraitExisted()) {
            try {
                setViewHeadPortrait();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        recyclerView=(RecyclerView)view.findViewById(R.id.RecyclerInToDoFragment);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        viewModel=new ViewModelProvider(this).get(ToDoListViewModel.class);
        adapter=new ToDoListAdapter();

        final Observer<List<FocusList>> ToDoFragmentObserver= new Observer<List<FocusList>>() {
            @Override
            public void onChanged(List<FocusList> records) {
                adapter.setMData(records);
                recyclerView.setAdapter(adapter);
                if(records.size()>0){
                    background.setVisibility(View.INVISIBLE);
                    completedTextView.setVisibility(View.INVISIBLE);
                }
                else{
                    background.setVisibility(View.VISIBLE);
                    completedTextView.setVisibility(View.VISIBLE);
                }

            }
        };


        viewModel.getCurrentData().observe(getViewLifecycleOwner(),ToDoFragmentObserver);
        loadAllRecords(focusListDao);

        //recyclerView item右上角应用到闹钟按钮点击事件
        adapter.setClickListenerToDo(new ToDoListAdapter.OnRecyclerViewClickListenerToDo() {
            @Override
            public void onClick(View view, ToDoListAdapter.ViewNameInToDo viewName, int position) {
                if (viewName == ToDoListAdapter.ViewNameInToDo.DELETE)
                {
                    FocusList record=adapter.getMData().get(position);
                    Message message=new Message();
                    message.what=DELETE_PLAN;
                    message.obj=record;
//                    FragmentHandler handler=new FragmentHandler();
                    handler.sendMessage(message);
                }

                //TODO
                //用户点击设为每日待办按钮后逻辑
                else if(viewName == ToDoListAdapter.ViewNameInToDo.SET_AS_EVERYDAY_PLAN)
                {
                    FocusList record=adapter.getMData().get(position);
                    if (record.isEveryDayTask)
                    {
                        record.isEveryDayTask=false;
                        Toast.makeText(getContext(),"已取消每日待办",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        record.isEveryDayTask=true;
                        Toast.makeText(getContext(),"已设为每日待办",Toast.LENGTH_SHORT).show();
                    }
                    threadHelper.updateFocusList(focusListDataBase,record);

                }
                //TODO
                //用户点击开始记录专注时长按钮后逻辑
                else if (viewName == ToDoListAdapter.ViewNameInToDo.RECORD_FOCUS_TIME)
                {
                    FocusList record=adapter.getMData().get(position);
                    Intent intent = new Intent(mainActivity,ChronometerActivity.class);
                    intent.putExtra(getString(R.string.focusList_to_chronometer),record);
                    startActivity(intent);
                }
            }
        });

        resolutionsTextView = (TextView)view.findViewById(R.id.top_resolutions_todo);
        addTask.setOnClickListener(this);
        // init sp
        todoListSp = this.getActivity().getPreferences(Context.MODE_PRIVATE);
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
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.add_task:
                Fragment addTaskFragment = new AddTaskFragment();
                MainActivity activity = (MainActivity)getActivity();
                activity.setFragment(addTaskFragment);
                break;

        }
    }


    public class ToDoListFragmentHandler extends Handler {

        @Override
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case UPDATE_UI:
                    List<FocusList> lists=(List<FocusList>)message.obj;
                    viewModel.getCurrentData().setValue(lists);
                    break;
                case DELETE_PLAN:
                    FocusList list=(FocusList)message.obj;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            focusListDao.delete(list);
                            loadAllRecords(focusListDao);
                        }
                    }).start();
                    break;

            }
        }
    }

    private void loadAllRecords(FocusListDao dao)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Calendar currentCalendar = Calendar.getInstance();
                SimpleDateFormat fmt =new SimpleDateFormat("yyyyMMdd");
                String newDateString = fmt.format(currentCalendar.getTime());

                List<FocusList> records=dao.getByDate(Integer.parseInt(newDateString));
                List<FocusList> extraEveryDay=dao.getEveryDayTask(true);

                records.addAll(extraEveryDay);
                Message message=new Message();
                message.what=UPDATE_UI;
                message.obj=records;

//                FragmentHandler handler=new FragmentHandler();
                handler.sendMessage(message);
            }
        }).start();
    }


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




    //从MineFragment抄过来的
    public void setViewHeadPortrait() throws FileNotFoundException {
        FileInputStream inputStream=mainActivity.openFileInput(HEAD_PORTRAIT);
        Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
        topProfile.setImageBitmap(bitmap);
    }
    public boolean isPortraitExisted()
    {
        File file=new File(mainActivity.getFilesDir()+"/"+HEAD_PORTRAIT);
        Log.d(TAG, "isPortraitExisted: portrait loaded  "+file.length());
        return file.exists();
    }



}