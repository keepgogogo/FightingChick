package com.hackweek.fightingchick;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.hackweek.fightingchick.R;

import com.hackweek.fightingchick.database.FocusList;
import com.hackweek.fightingchick.database.FocusListDataBase;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    private FocusList focusList;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");


        int y=intent.getIntExtra("int",98);

        String x=intent.getStringExtra("String");
        focusList=(FocusList)intent.getSerializableExtra("FocusList");
        String yyy="";
    }
}
