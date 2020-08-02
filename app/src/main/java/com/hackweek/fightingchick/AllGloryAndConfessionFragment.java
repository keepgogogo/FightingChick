package com.hackweek.fightingchick;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewTreeLifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Handler;
import android.os.Message;
import android.telephony.IccOpenLogicalChannelResponse;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.hackweek.fightingchick.database.FocusListDataBase;
import com.hackweek.fightingchick.database.GloryAndConfessionDao;
import com.hackweek.fightingchick.database.GloryAndConfessionDataBase;
import com.hackweek.fightingchick.database.GloryAndConfessionRecord;
import com.hackweek.fightingchick.recycler.GloryAndConfessionAdapter;
import com.hackweek.fightingchick.recycler.GloryAndConfessionViewModel;
import com.hackweek.fightingchick.toolpackage.GloryAndConfessionRecordsOperator;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.widget.DefaultItemDecoration;

import java.util.List;

public class AllGloryAndConfessionFragment extends Fragment implements View.OnClickListener{

    final int UPDATE_UI=1;

    MainActivity mainActivity;
    FocusListDataBase focusListDataBase;
    GloryAndConfessionDataBase gloryAndConfessionDataBase;
    GloryAndConfessionDao gloryAndConfessionDao;
    DefaultItemDecoration itemDecoration;
    ProgressBar progressBar;
    List<GloryAndConfessionRecord> recordsCopy;

    private SwipeRecyclerView swipeRecyclerView;
    private GloryAndConfessionAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private GloryAndConfessionViewModel viewModel;
    private Button buttonForGlory;
    private Button buttonForConfession;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        mainActivity=new MainActivity();
        focusListDataBase=FocusListDataBase.getDatabase(mainActivity);
        gloryAndConfessionDataBase=GloryAndConfessionDataBase.getDataBase(mainActivity);
        gloryAndConfessionDao=gloryAndConfessionDataBase.GloryAndConfessionDao();

        progressBar=view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        buttonForGlory=view.findViewById(R.id.ButtonForWatchConfessionInAllGloryFragment);
        buttonForGlory.setOnClickListener(this);
        buttonForConfession=view.findViewById(R.id.ButtonForWatchGloryInAllGloryFragment);
        buttonForConfession.setOnClickListener(this);

        layoutManager=new LinearLayoutManager(getContext());
        swipeRecyclerView=(SwipeRecyclerView)view.findViewById(R.id.recyclerAllGlory);
        swipeRecyclerView.setHasFixedSize(true);
        swipeRecyclerView.setLayoutManager(layoutManager);

        viewModel=new ViewModelProvider(this).get(GloryAndConfessionViewModel.class);
        adapter=new GloryAndConfessionAdapter();

        final Observer<List<GloryAndConfessionRecord>> GloryAndConfessionObserver= new Observer<List<GloryAndConfessionRecord>>() {
            @Override
            public void onChanged(List<GloryAndConfessionRecord> records) {
                adapter.setMData(records);
                swipeRecyclerView.setAdapter(adapter);
            }
        };

        viewModel.getCurrentData().observe(getViewLifecycleOwner(),GloryAndConfessionObserver);
        loadAllRecords(gloryAndConfessionDao);
    }



    public AllGloryAndConfessionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_glory_and_confession, container, false);
    }

    private void loadAllRecords(GloryAndConfessionDao dao)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<GloryAndConfessionRecord> records=dao.loadAll();

                Message message=new Message();
                message.what=UPDATE_UI;
                message.obj=records;

                FragmentHandler handler=new FragmentHandler();
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        GloryAndConfessionRecordsOperator operator=new GloryAndConfessionRecordsOperator();
        operator.setRecords(recordsCopy);
        switch (view.getId())
        {
            case R.id.ButtonForWatchGloryInAllGloryFragment:
                buttonForGlory.setTextColor(0x000000);
                buttonForConfession.setTextColor(0xbfb0b0);
                setView(operator.getGloryRecord());
                break;
            case R.id.ButtonForWatchConfessionInAllGloryFragment:
                buttonForConfession.setTextColor(0x000000);
                buttonForGlory.setTextColor(0xbfb0b0);
                setView(operator.getConfessionRecord());
                break;
            default:
                break;


        }
    }

    private class FragmentHandler extends Handler{

        @Override
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case UPDATE_UI:
                    List<GloryAndConfessionRecord> records=(List<GloryAndConfessionRecord>)message.obj;
                    recordsCopy=records;
                    buttonForGlory.setTextColor(0x000000);
                    adapter.setMData(records);
                    progressBar.setVisibility(View.GONE);
                    setView(records);
                    break;
            }
        }
    }

    private void setView(List<GloryAndConfessionRecord> records)
    {
        viewModel.getCurrentData().setValue(records);
    }
}