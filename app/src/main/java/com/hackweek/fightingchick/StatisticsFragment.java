package com.hackweek.fightingchick;

import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;

import android.os.ParcelUuid;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.hackweek.fightingchick.database.FocusList;
import com.hackweek.fightingchick.database.FocusListDao;
import com.hackweek.fightingchick.database.FocusListDataBase;
import com.hackweek.fightingchick.toolpackage.ThreadHelper;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class StatisticsFragment extends Fragment {

    final String TAG="StatisticsFragment";
    public final static int RECEIVE_TODAY_FOCUS_LIST=555;
    public final static int RECEIVE_WEEK_FOCUS_LIST=556;
    public final static int RECEIVE_MONTH_FOCUS_LIST=557;

    private MainActivity mainActivity;
    private FocusListDataBase focusListDataBase;
    private FocusListDao focusListDao;
    private SharedPreferences preferences;
    private SharedPreferences.Editor preferenceEditor;
    private ThreadHelper threadHelper;
    private StatisticsFragmentHandler handler;

    private BarChart barChartForToday;
    private SwipeRecyclerView swipeRecyclerView;
    private TextView textViewTodayTotalPower;
    private TextView textViewTodayCompletionRate;
    private TextView textViewTodayTotalFocusTime;
    private TextView textViewComplete;
    private TextView textViewIncomplete;
    private TextView textViewWeekTotalPower;
    private TextView textViewWeekFocusTime;
    private TextView textViewWeekCompletionRate;
    private TextView textViewMonthTotalPower;
    private TextView textViewMonthFocusTime;
    private TextView textViewMonthCompletionRate;
    private TextView textViewWeekTotalPlan;
    private TextView textViewMonthTotalPlan;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        mainActivity=(MainActivity)getActivity();
        assert mainActivity != null;

        barChartForToday=(BarChart)view.findViewById(R.id.BarChartForToday);
        barChartForToday.setNoDataText("正在初始化");

        textViewTodayCompletionRate=(TextView)view.findViewById(R.id.TextView_TodayCompletionRate);
        textViewTodayTotalFocusTime=(TextView)view.findViewById(R.id.TextView_TodayTotalFocusTime);
        textViewTodayTotalPower=(TextView)view.findViewById(R.id.TextView_TodayTotalPower);
        textViewComplete=(TextView)view.findViewById(R.id.TextViewForCompleteStatistics);
        textViewIncomplete=(TextView)view.findViewById(R.id.TextViewForIncompleteStatistics);
        textViewWeekCompletionRate=(TextView)view.findViewById(R.id.TextView_WeekCompletionRate);
        textViewWeekTotalPower=(TextView)view.findViewById(R.id.TextView_WeekTotalPower);
        textViewWeekFocusTime=(TextView)view.findViewById(R.id.TextView_WeekTotalFocusTime);
        textViewMonthCompletionRate=(TextView)view.findViewById(R.id.TextView_MonthCompletionRate);
        textViewMonthTotalPower=(TextView)view.findViewById(R.id.TextView_MonthTotalPower);
        textViewMonthFocusTime=(TextView)view.findViewById(R.id.TextView_MonthTotalFocusTime);
        textViewMonthTotalPlan=(TextView)view.findViewById(R.id.TextView_MonthTotalPlan);
        textViewWeekTotalPlan=(TextView)view.findViewById(R.id.TextView_WeekTotalPlan);




        preferences=mainActivity.getPreferences(Context.MODE_PRIVATE);
        preferenceEditor=preferences.edit();
        preferenceEditor.apply();
        focusListDataBase=FocusListDataBase.getDatabase(mainActivity);
        focusListDao=focusListDataBase.FocusListDao();
        handler=new StatisticsFragmentHandler();
        threadHelper=new ThreadHelper();

        threadHelper.loadTodayForStatisticsFragment(handler,focusListDao);

    }

    public boolean isCompleted(FocusList focusList)
    {
        return !(0==focusList.FocusTime);
    }

    public class StatisticsFragmentHandler extends Handler{
        List<FocusList> lists;
        StringBuilder stringBuilder=new StringBuilder();
        @Override
        public void handleMessage(Message message)
        {
            if(lists!=null)lists.clear();
            stringBuilder.delete(0,stringBuilder.length());
            switch (message.what)
            {
                case RECEIVE_TODAY_FOCUS_LIST:
                    Description description=new Description();
                    description.setText("");
                    lists=(List<FocusList>)message.obj;

//                    lists.get(0).FocusTime=120;
//                    lists.get(1).FocusTime=12;
                    stringBuilder=new StringBuilder(textViewTodayCompletionRate.getText());
                    stringBuilder.append(getDayCompletionRate(lists));
                    textViewTodayCompletionRate.setText(stringBuilder.toString());

                    stringBuilder.delete(0,stringBuilder.length());
                    stringBuilder.append(textViewTodayTotalFocusTime.getText());
                    stringBuilder.append(getTotalFocusTime(lists));
                    textViewTodayTotalFocusTime.setText(stringBuilder.toString());

                    stringBuilder.delete(0,stringBuilder.length());
                    stringBuilder.append(textViewTodayTotalPower.getText());
                    stringBuilder.append(getTotalPower(lists));
                    textViewTodayTotalPower.setText(stringBuilder.toString());



                    List<BarEntry> entries=new ArrayList<>();
                    for (int i=0;i<lists.size();i++)
                    {
                        entries.add(new BarEntry((float)i,lists.get(i).FocusTime));
                    }
                    BarDataSet barDataSet=new BarDataSet(entries,"Label");
                    barDataSet.setColor(0xff00bcd4);
                    barDataSet.setValueTextColor(0xff000000);
                    barDataSet.setValueTextSize(12F);
                    BarData barData=new BarData(barDataSet);
                    barData.setBarWidth(0.2f);
                    makeXDataString(lists,barChartForToday);
                    barChartForToday.setData(barData);
                    barChartForToday.setDescription(description);
                    barChartForToday.getAxisRight().setDrawZeroLine(true);
                    barChartForToday.getAxisRight().setDrawLabels(false);
                    barChartForToday.getAxisRight().setDrawAxisLine(false);
                    barChartForToday.getXAxis().setDrawGridLines(false);
                    barChartForToday.getAxisLeft().setAxisMinimum(0f);
                    barChartForToday.getAxisLeft().setDrawGridLines(false);
                    barChartForToday.getAxisRight().setDrawGridLines(false);
                    barChartForToday.invalidate();

                    String[] data=getFormatDetailString(lists);
                    textViewComplete.setText(data[0]);
                    textViewIncomplete.setText(data[1]);
                    threadHelper.loadWeekForStatisticsFragment(handler,focusListDao);
                    break;

                case RECEIVE_WEEK_FOCUS_LIST:
                    lists=(List<FocusList>)message.obj;

                    stringBuilder=new StringBuilder(textViewWeekCompletionRate.getText());
                    stringBuilder.append(getDayCompletionRate(lists));
                    textViewWeekCompletionRate.setText(stringBuilder.toString());

                    stringBuilder.delete(0,stringBuilder.length());
                    stringBuilder.append(textViewWeekTotalPlan.getText());
                    stringBuilder.append(lists.size());
                    textViewWeekTotalPlan.setText(stringBuilder.toString());


                    stringBuilder.delete(0,stringBuilder.length());
                    stringBuilder.append(textViewWeekTotalPower.getText());
                    stringBuilder.append(getTotalPower(lists));
                    textViewWeekTotalPower.setText(stringBuilder.toString());

                    stringBuilder.delete(0,stringBuilder.length());
                    stringBuilder.append(textViewWeekFocusTime.getText());
                    stringBuilder.append(getTotalFocusTime(lists));
                    textViewWeekFocusTime.setText(stringBuilder.toString());
                    threadHelper.loadMonthForStatisticsFragment(handler,focusListDao);
                    break;

                case RECEIVE_MONTH_FOCUS_LIST:
                    lists=(List<FocusList>)message.obj;

                    stringBuilder=new StringBuilder(textViewMonthCompletionRate.getText());
                    stringBuilder.append(getDayCompletionRate(lists));
                    textViewMonthCompletionRate.setText(stringBuilder.toString());


                    stringBuilder.delete(0,stringBuilder.length());
                    stringBuilder.append(textViewMonthTotalPlan.getText());
                    stringBuilder.append(lists.size());
                    textViewMonthTotalPlan.setText(stringBuilder.toString());


                    stringBuilder.delete(0,stringBuilder.length());
                    stringBuilder.append(textViewMonthTotalPower.getText());
                    stringBuilder.append(getTotalPower(lists));
                    textViewMonthTotalPower.setText(stringBuilder.toString());

                    stringBuilder.delete(0,stringBuilder.length());
                    stringBuilder.append(textViewMonthFocusTime.getText());
                    stringBuilder.append(getTotalFocusTime(lists));
                    textViewMonthFocusTime.setText(stringBuilder.toString());
                    break;


            }
        }
    }

    public String getTotalPower(List<FocusList> lists)
    {
        int power=0;
        for(FocusList temp : lists)
        {
            power+=temp.energyValue;
        }
        return String.valueOf(power);
    }

    public String getTotalFocusTime(List<FocusList> lists)
    {
        int time=0;
        for(FocusList temp : lists)
        {
            time+=temp.FocusTime;
        }
        return String.valueOf(time);
    }

    public String getDayCompletionRate(List<FocusList> lists)
    {
        double i=0;
        for(FocusList temp : lists)
        {
            if (temp.FocusTime!=-1)
            {
                if(isCompleted(temp))i++;
            }
            else
            {
                if (temp.status==2 || temp.status==1)i++;
            }

        }
        double x=(i/lists.size());
        x*=100;

        x=(int)x;
        return String.valueOf(x)+"%";
    }

    public void makeXDataString(List<FocusList> lists,BarChart barChart)
    {
        XAxis xAxis=barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return lists.get((int)value).whatTodo;
            }
        });

        barChart.getLegend().setEnabled(false);
        xAxis.setLabelCount(lists.size(),false);
    }

    public String[] getFormatDetailString(List<FocusList> lists)
    {
        StringBuilder completedStringBuilder=new StringBuilder();
        StringBuilder incompleteStringBuilder=new StringBuilder();
        for(FocusList list : lists)
        {
            if(list.FocusTime!=0)
            {
                completedStringBuilder.append(list.whatTodo);
                completedStringBuilder.append(":     共响铃");
                completedStringBuilder.append(1);
                completedStringBuilder.append("次,专注");
                completedStringBuilder.append(list.FocusTime);
                completedStringBuilder.append("分钟,共获得");
                completedStringBuilder.append(list.energyValue);
                completedStringBuilder.append("点动力值");
                completedStringBuilder.append('\n');
            }

            else
            {
                incompleteStringBuilder.append(list.whatTodo);
                incompleteStringBuilder.append(":     未完成");
                incompleteStringBuilder.append('\n');
            }

        }
        String[] array=new String[2];
        array[0]=completedStringBuilder.toString();
        array[1]=incompleteStringBuilder.toString();
        return array;

    }


    public StatisticsFragment() {
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
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }
}