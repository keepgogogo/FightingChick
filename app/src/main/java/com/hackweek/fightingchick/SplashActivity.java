package com.hackweek.fightingchick;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;



public class SplashActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button;
    private CheckBox checkBox;
    private TextView textView;
    private final int SPLASH_TIME_OUT=200;
    private SharedPreferences SplashActivitySP;
    private static final String[] musicNames={"deepeast","linglingling","qingshixiang","lullatone","gongji","xiaoji","yaogunji","kuaileji"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        for(int i=0;i<8;i++){
            createNotificationChannel(musicNames[i]);
        }
        createNotificationChannel();
        button = (Button)findViewById(R.id.enter);
        textView=(TextView)findViewById(R.id.privacy_policy);
        checkBox=(CheckBox)findViewById(R.id.agree_privacy);
        button.setOnClickListener(this);
        textView.setOnClickListener(this);
        checkBox.setOnClickListener(this);

        SplashActivitySP = this.getPreferences(Context.MODE_PRIVATE);
        //检查是否有存储的key，没有则创建
        if(!SplashActivitySP.contains(getString(R.string.saved_agreed_key))){
            SharedPreferences.Editor editor = SplashActivitySP.edit();
            editor.putInt(getString(R.string.saved_agreed_key), 0);
            editor.apply();
        }

        if(SplashActivitySP.getInt(getString(R.string.saved_agreed_key),0)==1){
            //确认过了，隐藏“进入”按钮，“同意”设置为不可点击，倒计时1秒后进入下一活动
            button.setVisibility(View.INVISIBLE);
            button.setClickable(false);
            checkBox.setChecked(true);
            checkBox.setClickable(false);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }else{
            //没有确认过
            button.setBackgroundColor(getColor(R.color.colorGrey));
            checkBox.setChecked(false);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.enter:
                if(checkBox.isChecked()){
                    //把“已确认”存入SP
                    SharedPreferences.Editor editor = SplashActivitySP.edit();
                    editor.putInt(getString(R.string.saved_agreed_key), 1);
                    editor.apply();
                    //启动活动
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
//                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"请先同意用户协议和隐私条款！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.privacy_policy:
                showDialog();
                break;
            case R.id.agree_privacy:
                if(!checkBox.isChecked())
                    button.setBackgroundColor(getColor(R.color.colorGrey));
                else
                    button.setBackgroundColor(getColor(R.color.primaryColor));
        }
    }

    private void showDialog(){
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);
        dialog.setContentView(view);
        dialog.show();
    }
    //Vibrate only
    private void createNotificationChannel(){
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_LOW;//TODO perhaps wrong
            NotificationChannel channel = new NotificationChannel("vibrate", name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.Q){
                channel.setAllowBubbles(true);
            }

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100,200,200,200,300,400,200,100});
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



    private void createNotificationChannel(String id) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            Uri uri = Uri.parse("android.resource://"+getPackageName()+"/raw/"+id);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            channel.setSound(uri,audioAttributes);
            if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.Q){
                channel.setAllowBubbles(true);
            }

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100,200,200,200,300,400,200,100});
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}