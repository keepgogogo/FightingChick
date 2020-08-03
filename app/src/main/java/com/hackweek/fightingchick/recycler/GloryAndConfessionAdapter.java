package com.hackweek.fightingchick.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackweek.fightingchick.R;
import com.hackweek.fightingchick.database.GloryAndConfessionRecord;

import java.text.BreakIterator;
import java.util.List;

public class GloryAndConfessionAdapter extends RecyclerView
        .Adapter<GloryAndConfessionAdapter.GloryAndConfessionViewHolder>
        implements View.OnClickListener{

    final int GET_DATE=0;
    final int GET_GLORY_OR_CONFESSION=1;

    private List<GloryAndConfessionRecord> mData;

    public void setMData(List<GloryAndConfessionRecord> mData){this.mData=mData;}

    public List<GloryAndConfessionRecord> getMData(){return mData;}


    @Override
    public GloryAndConfessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView=(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_all_glory_recycler,
                parent,false);
        return new GloryAndConfessionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GloryAndConfessionViewHolder holder,int position)
    {
        holder.dateTextView.setText(getDataFromList(position,GET_DATE));
        holder.gloryAndConfessionTextView.setText(getDataFromList(position,GET_GLORY_OR_CONFESSION));
        holder.buttonForSaveTheGloryAsAlarm.setId(position);
    }

    public String getDataFromList(int position,int statement)
    {
        StringBuilder stringBuilder=new StringBuilder();
        GloryAndConfessionRecord record=mData.get(position);
        switch (statement)
        {
            case GET_DATE:
                stringBuilder.append((record.date)/10000);
                stringBuilder.append("\\");
                int temp=record.date/100;
                temp=temp%100;
                stringBuilder.append(temp);
                stringBuilder.append("\\");
                stringBuilder.append(record.date%100);
                break;
            case GET_GLORY_OR_CONFESSION:
                stringBuilder.append(record.content);
                break;
            default:
                break;
        }
        return stringBuilder.toString();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class GloryAndConfessionViewHolder extends RecyclerView.ViewHolder
    {
        public TextView gloryAndConfessionTextView;
        public TextView dateTextView;
        public Button buttonForSaveTheGloryAsAlarm;
        public GloryAndConfessionViewHolder(@NonNull View itemView) {
            super(itemView);
            gloryAndConfessionTextView=itemView.findViewById(R.id.TextViewForShowGloryAndConfessionInRecycler);
            dateTextView=itemView.findViewById(R.id.TextViewForShowDateInRecycler);
            buttonForSaveTheGloryAsAlarm=itemView.findViewById(R.id.ButtonForUseTheGloryToAlarmInRecycler);
            buttonForSaveTheGloryAsAlarm.setOnClickListener(GloryAndConfessionAdapter.this);
        }
    }

    //button点击处理
    private OnRecyclerViewClickListener mClickListener;

    public void setMClickListener(OnRecyclerViewClickListener listener){mClickListener=listener;}

    public enum ViewName{
        BUTTON_FOR_SET_GLORY_TO_ALARM
    }

    public interface OnRecyclerViewClickListener
    {
        void onClick(View view,ViewName viewName,int position);
    }

    @Override
    public void onClick(View view)
    {
        int position=(int)view.getId();
        if(mClickListener!=null)
        {
//            switch (view.getId())
//            {
//                case R.id.ButtonForUseTheGloryToAlarmInRecycler:
//                    mClickListener.onClick(view,ViewName.BUTTON_FOR_SET_GLORY_TO_ALARM,position);
//                    break;
//                default:
//                    break;
//            }
            mClickListener.onClick(view,ViewName.BUTTON_FOR_SET_GLORY_TO_ALARM,position);
        }
    }
}
