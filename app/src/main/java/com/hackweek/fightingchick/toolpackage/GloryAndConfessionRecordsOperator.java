package com.hackweek.fightingchick.toolpackage;

import com.hackweek.fightingchick.database.GloryAndConfessionRecord;

import java.util.ArrayList;
import java.util.List;

public class GloryAndConfessionRecordsOperator {
    private List<GloryAndConfessionRecord> records;

    public void setRecords(List<GloryAndConfessionRecord> records) {
        this.records = records;
    }

    public List<GloryAndConfessionRecord> getGloryRecord()
    {
        ArrayList<GloryAndConfessionRecord> list=new ArrayList<>();
        for(GloryAndConfessionRecord temp : records)
        {
            if(temp.gloryOrConfession)list.add(temp);
        }
        return list;
    }

    public List<GloryAndConfessionRecord> getConfessionRecord()
    {
        ArrayList<GloryAndConfessionRecord> list=new ArrayList<>();
        for(GloryAndConfessionRecord temp : records)
        {
            if(!temp.gloryOrConfession)list.add(temp);
        }
        return list;
    }


}
