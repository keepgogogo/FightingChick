package com.hackweek.fightingchick.recycler;



import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hackweek.fightingchick.database.FocusList;


import java.util.List;

public class ToDoListViewModel extends ViewModel {
    private MutableLiveData<List<FocusList>> currentData;
    public MutableLiveData<List<FocusList>> getCurrentData()
    {
        if(currentData ==null)
        {
            currentData =new MutableLiveData<List<FocusList>>();
        }
        return currentData;
    }
}
