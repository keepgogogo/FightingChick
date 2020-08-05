package com.hackweek.fightingchick.utility;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.hackweek.fightingchick.MainActivity;
import com.hackweek.fightingchick.R;
import com.hackweek.fightingchick.database.FocusList;
import com.hackweek.fightingchick.database.FocusListDataBase;
import com.hackweek.fightingchick.reminder.ReminderActivity;
import com.hackweek.fightingchick.toolpackage.ThreadHelper;

import java.util.List;
import java.util.UUID;

public class TodoNotificationService extends IntentService {

    private static final int NOTIFICATION=2;
    private String uuid;
    private FocusList mFocusList;
    private List<FocusList> mTodayFocusList;
    private static final String[] musicNames = {"vibrate", "deepeast", "linglingling", "qingshixiang", "lullatone", "gongji", "xiaoji", "yaogunji", "kuaileji"};
    private static final String TAG = "TodoNotificationService";

    public TodoNotificationService() {
        super("TodoNotificationService");
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: ");

        Handler handler = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message msg){
                switch (msg.what){
                    case NOTIFICATION:
                        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                        Intent i = new Intent(getApplicationContext(), ReminderActivity.class);
                        i.putExtra(getString(R.string.focusList_to_reminder), mFocusList);

                        Intent deleteIntent = new Intent(getApplicationContext(), DeleteNotificationService.class);
                        deleteIntent.putExtra(getString(R.string.focusList_to_delete_notification), mFocusList);

                        NotificationCompat.Builder mBuilder;
                        if (mFocusList.notice == 1) {
                            mBuilder = new NotificationCompat.Builder(TodoNotificationService.this, "vibrate");
                        } else {
                            assert mFocusList.noticeMusic > 0 && mFocusList.noticeMusic < 9;
                            mBuilder = new NotificationCompat.Builder(TodoNotificationService.this, musicNames[mFocusList.noticeMusic]);
                        }

                        mBuilder.setContentTitle("该" + mFocusList.whatTodo + "啦！")
                                .setSmallIcon(R.drawable.logo)
                                .setAutoCancel(true)
                                .setDeleteIntent(PendingIntent.getService(getApplicationContext(), UUID.fromString(mFocusList.identifier).hashCode(),
                                        deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), UUID.fromString(mFocusList.identifier).hashCode(),
                                        i, PendingIntent.FLAG_UPDATE_CURRENT));
                        Notification notificationCompat = mBuilder.build();
                        manager.notify(100, notificationCompat);
                        break;
                }
            }

        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                FocusListDataBase focusListDataBase = FocusListDataBase.getDatabase(getApplicationContext());

                mTodayFocusList = focusListDataBase.FocusListDao().loadAll();
                uuid = intent.getStringExtra(MainActivity.TODOUUID);
                for(FocusList focusList: mTodayFocusList){
                    if(focusList.identifier.equals(uuid))
                        mFocusList=focusList;
                }
                Message messageNotification = new Message();
                messageNotification.what=NOTIFICATION;
                handler.sendMessage(messageNotification);

            }
        }).start();

    }
}
