package com.hackweek.fightingchick.recycler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hackweek.fightingchick.database.GloryAndConfessionRecord;

import java.util.List;

public class GloryAndConfessionViewModel extends ViewModel {
    private MutableLiveData<List<GloryAndConfessionRecord>> currentData;

    public MutableLiveData<List<GloryAndConfessionRecord>> getCurrentData()
    {
        if(currentData ==null)
        {
            currentData =new MutableLiveData<List<GloryAndConfessionRecord>>();
        }
        return currentData;
    }
}
