package com.hackweek.fightingchick.utility;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.hackweek.fightingchick.R;
import com.hackweek.fightingchick.database.FocusList;
import com.hackweek.fightingchick.database.FocusListDataBase;

public class DeleteNotificationService extends IntentService {

    //TODO
    private FocusList focusList;
    private FocusListDataBase focusListDataBase;
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        focusList = (FocusList)intent.getSerializableExtra(getString(R.string.focusList_to_delete_notification));

    }

    public DeleteNotificationService(){
        super("DeleteNotificationService");
    }
}
