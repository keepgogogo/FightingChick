package com.hackweek.fightingchick.utility;

import android.app.IntentService;

import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Intent;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.hackweek.fightingchick.R;
import com.hackweek.fightingchick.reminder.ReminderActivity;

import java.util.UUID;

public class TodoNotificationService extends IntentService {
    public static final String WHATTODO = "com.fightingchick.todonotifationservicewhattodo";
    public static final String NOTICE ="com.fightingchick.todonotifationservicenotice";
    public static final String NOTICEMUSIC = "com.fightingchick.todonotifationservicenoticemusic";
    public static final String NOTICEINTERVAL="com.fightingchick.todonotifationserviceinterval";
    public static final String TODOUUID = "com.fightingchick.todonotifationserviceuuid";

    private String mWhatTodo;
    private UUID mTodoUUID;
    private int mNotice;
    private int mNoticeMusic;
    private int mNoticeInterval;
    private static final String TAG = "TodoNotificationService";

    public TodoNotificationService(){
        super("TodoNotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mWhatTodo = intent.getStringExtra(WHATTODO);
        mNotice = intent.getIntExtra(NOTICE,0);
        mNoticeMusic = intent.getIntExtra(NOTICEMUSIC,1);
        mNoticeInterval = intent.getIntExtra(NOTICEINTERVAL,3);
        mTodoUUID = (UUID)intent.getSerializableExtra(TODOUUID);

        Log.d(TAG, "onHandleIntent: ");
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, ReminderActivity.class);
        i.putExtra(TodoNotificationService.TODOUUID,mTodoUUID);
        Intent deleteIntent = new Intent(this,DeleteNotificationService.class);
        deleteIntent.putExtra(TODOUUID,mTodoUUID);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,getString(R.string.channel_id));
        mBuilder.setContentTitle("该"+mWhatTodo+"啦！")
                .setAutoCancel(true)
                .setDeleteIntent(PendingIntent.getService(this,mTodoUUID.hashCode(),
                        deleteIntent,PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentIntent(PendingIntent.getActivity(this,mTodoUUID.hashCode(),
                        i,PendingIntent.FLAG_UPDATE_CURRENT));
        switch (mNotice){

        }
    }
}
