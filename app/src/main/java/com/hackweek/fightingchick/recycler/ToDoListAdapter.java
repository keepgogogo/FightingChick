package com.hackweek.fightingchick.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackweek.fightingchick.R;
import com.hackweek.fightingchick.database.FocusList;

import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder>
    implements View.OnClickListener {

    private List<FocusList> mData;

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
        holder.whatToDoTextView.setText(mData.get(position).whatTodo);
        holder.startFocus.setTag(position);
        holder.setEveryDayPlan.setTag(position);
        holder.deleteButton.setTag(position);
    }

    @Override
    public int getItemCount(){return mData.size();}

    public class ToDoListViewHolder extends RecyclerView.ViewHolder {
        public TextView whatToDoTextView;
        public Button deleteButton;
        public Button setEveryDayPlan;
        public Button startFocus;
        public ToDoListViewHolder(@NonNull View itemView) {
            super(itemView);
            whatToDoTextView=itemView.findViewById(R.id.whatToDo_todo);
            deleteButton=itemView.findViewById(R.id.ButtonForDelete_todo);
            setEveryDayPlan=itemView.findViewById(R.id.ButtonForSetEveryDayPlan_todo);
            startFocus=itemView.findViewById(R.id.ButtonForStartFocus_todo);
            startFocus.setOnClickListener(ToDoListAdapter.this);
            setEveryDayPlan.setOnClickListener(ToDoListAdapter.this);
            deleteButton.setOnClickListener(ToDoListAdapter.this);
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
