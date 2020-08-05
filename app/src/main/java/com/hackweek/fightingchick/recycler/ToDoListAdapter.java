package com.hackweek.fightingchick.recycler;

import java.util.Calendar;


import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackweek.fightingchick.R;
import com.hackweek.fightingchick.database.FocusList;

import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder>
    implements View.OnClickListener {

    private List<FocusList> mData;
    private ToDoListViewHolder viewHolder;


    public void setMData(List<FocusList> mData) {
        this.mData = mData;
    }

    @Override
    public ToDoListViewHolder onCreateViewHolder(ViewGroup parent,int ViewType)
    {
        View itemView=(View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_for_to_do_recycler,parent,false);
        return new ToDoListViewHolder(itemView);
    }

    public List<FocusList> getMData() {
        return mData;
    }

    @Override
    public void onBindViewHolder(ToDoListViewHolder holder,int position)
    {

        holder.whatToDoTextView.setText(getTextFromData(mData.get(position)));

        isPlanMissed(mData.get(position),holder.incompleteText,holder.checkBox);

        holder.startFocus.setTag(position);
        holder.setEveryDayPlan.setTag(position);
        holder.deleteButton.setTag(position);
        if (mData.get(position).FocusTime==-1)holder.startFocus.setVisibility(View.INVISIBLE);
    }

    public String getTextFromData(FocusList list)
    {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(list.hour);
        stringBuilder.append(":");
        stringBuilder.append(list.minute);
        stringBuilder.append("  ");
        stringBuilder.append(list.whatTodo);
        return stringBuilder.toString();
    }


    public void isPlanMissed(FocusList plan,TextView textView,CheckBox checkBox)
    {
        if(plan.FocusTime!=0)
        {
            textView.setText("");
            checkBox.setChecked(true);
        }
        else
        {
            Calendar calendar=Calendar.getInstance();
            int hour=calendar.get(Calendar.HOUR_OF_DAY);
            int minute=calendar.get(Calendar.MINUTE);
            if (hour>plan.hour)
            {
                textView.setText("未完成");
                textView.setTextColor(Color.RED);
            }
            else if(hour==plan.hour)
            {
                if (minute>plan.minute)
                {
                    textView.setText("未完成");
                    textView.setTextColor(Color.RED);
                }
                else textView.setText("");
            }
            else textView.setText("");
        }
    }

    @Override
    public int getItemCount(){return mData.size();}

    public class ToDoListViewHolder extends RecyclerView.ViewHolder {
        public TextView whatToDoTextView;
        public TextView incompleteText;
        public Button deleteButton;
        public Button setEveryDayPlan;
        public Button startFocus;
        public CheckBox checkBox;
        public ToDoListViewHolder(@NonNull View itemView) {
            super(itemView);
            whatToDoTextView=itemView.findViewById(R.id.whatToDo_todo);
            deleteButton=itemView.findViewById(R.id.ButtonForDelete_todo);
            setEveryDayPlan=itemView.findViewById(R.id.ButtonForSetEveryDayPlan_todo);
            startFocus=itemView.findViewById(R.id.ButtonForStartFocus_todo);
            checkBox=itemView.findViewById(R.id.complete_todo);
            incompleteText=itemView.findViewById(R.id.InCompleteText_todo);
            startFocus.setOnClickListener(ToDoListAdapter.this);
            setEveryDayPlan.setOnClickListener(ToDoListAdapter.this);
            deleteButton.setOnClickListener(ToDoListAdapter.this);
            viewHolder=ToDoListViewHolder.this;
        }
    }

    private OnRecyclerViewClickListenerToDo  clickListenerToDo;

    public void setClickListenerToDo(OnRecyclerViewClickListenerToDo clickListenerToDo) {
        this.clickListenerToDo = clickListenerToDo;
    }

    public interface OnRecyclerViewClickListenerToDo
    {
        void onClick(View view,ViewNameInToDo viewName,int position);
    }

    @Override
    public void onClick(View view)
    {
        int position=(int)view.getTag();
        if(clickListenerToDo!=null)
        {
            switch (view.getId())
            {
                case R.id.ButtonForDelete_todo:
                    clickListenerToDo.onClick(view,ViewNameInToDo.DELETE,position);
                    break;
                case R.id.ButtonForStartFocus_todo:
                    clickListenerToDo.onClick(view,ViewNameInToDo.RECORD_FOCUS_TIME,position);
                    break;
                case R.id.ButtonForSetEveryDayPlan_todo:
                    if (mData.get(position).isEveryDayTask)
                    {
                        viewHolder.setEveryDayPlan.setText("设为每日待办");
                        viewHolder.setEveryDayPlan.setTypeface(null, Typeface.NORMAL);
                    }
                    else {
                        viewHolder.setEveryDayPlan.setText("已设为每日待办");
                        viewHolder.setEveryDayPlan.setTypeface(null, Typeface.BOLD);
                    }
                    clickListenerToDo.onClick(view,ViewNameInToDo.SET_AS_EVERYDAY_PLAN,position);

                    break;
                default:
                    break;
            }
        }
    }

    public enum ViewNameInToDo
    {
        DELETE,
        RECORD_FOCUS_TIME,
        SET_AS_EVERYDAY_PLAN
    }
}
