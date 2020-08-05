package com.hackweek.fightingchick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;





import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;



import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.hackweek.fightingchick.database.FocusList;
import com.hackweek.fightingchick.database.FocusListDataBase;
import com.hackweek.fightingchick.database.GloryAndConfessionDataBase;






public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String CHANGE_OCCURRED="com.hackweek.fightingchick.changeoccurred";
    public static final String TODOUUID="com.hackweek.fightingchick.todouuid";

    final int TRANSFER_DATABASE_TO_MINE_FRAGMENT=566;

    private BottomNavigationView mBottomNavigationView;
    public FocusListDataBase focusListDataBase;
    public GloryAndConfessionDataBase gloryAndConfessionDataBase;
    private SharedPreferences mainSp;
    private FocusList focusList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBottomNavigation();
        Fragment todoListFragment = new TodoListFragment();
        setFragment(todoListFragment);
        focusListDataBase = FocusListDataBase.getDatabase(getApplicationContext());
        gloryAndConfessionDataBase = GloryAndConfessionDataBase.getDataBase(getApplicationContext());
        mainSp = getSharedPreferences(getString(R.string.bigSp_key),MODE_PRIVATE);
        if(mainSp.getBoolean(CHANGE_OCCURRED,false)){//新增几分钟后的闹钟
            mainSp.edit().putBoolean(CHANGE_OCCURRED,false);
            focusList=(FocusList)getIntent().getSerializableExtra(getString(R.string.focusList_to_main));
            //TODO create alarm
        }
    }

    public void initBottomNavigation() {
        mBottomNavigationView = findViewById(R.id.bottomNavView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.todoListFragment:
                        Fragment todoListFragment = new TodoListFragment();
                        setFragment(todoListFragment);
                        //setFragmentPosition(0);
                        break;
                    case R.id.recordsFragment:
                        Fragment recordsFragment = new RecordsFragment();
                        setFragment(recordsFragment);
                        //setFragmentPosition(1);
                        break;
                    case R.id.mineFragment:
                        Fragment mineFragment = new MineFragment();
                        setFragment(mineFragment);

//                        handler.sendMessage(message);
                        //setFragmentPosition(2);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    //以形如20200803的int值返回当前日期
    public int getNewDate(){
        Calendar currentCalendar = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        String newDateString = fmt.format(currentCalendar.getTime());
        return Integer.parseInt(newDateString);
    }



    //用来操作碎片
    public void setFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_for_fragment,fragment).commit();
    }



    /**
     * 改变fragment中物理返回键的点击事件，防止返回直接退出app
     */
    private AllGloryAndConfessionFragment.AllGloryFragmentBackListener allGloryFragmentBackListener;
    private boolean inInterception;
    public AllGloryAndConfessionFragment.AllGloryFragmentBackListener getAllGloryFragmentBackListener()
    {
        return allGloryFragmentBackListener;
    }

    /**
     * 设置返回键监听
     */
    public void setAllGloryFragmentBackListener(AllGloryAndConfessionFragment.AllGloryFragmentBackListener backListener)
    {
        this.allGloryFragmentBackListener=backListener;
    }

    private AlarmManager getAlarmManager() {
        return (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    public void createAlarm(Intent i, int requestCode, long timeInMillis) {
        AlarmManager am = getAlarmManager();
        PendingIntent pi = PendingIntent.getService(MainActivity.this, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, timeInMillis, pi);
        //am.setRepeating(AlarmManager.RTC_WAKEUP,timeInMillis,);
        Log.d(TAG, "createAlarm " + requestCode + " time: " + timeInMillis + " PI " + pi.toString());
    }

    public boolean doesPendingIntentExist(Intent i, int requestCode) {
        PendingIntent pi = PendingIntent.getService(MainActivity.this, requestCode, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }


    public void deleteAlarm(Intent i, int requestCode) {
        if (doesPendingIntentExist(i, requestCode)) {
            PendingIntent pi = PendingIntent.getService(MainActivity.this, requestCode, i, PendingIntent.FLAG_NO_CREATE);
            pi.cancel();
            getAlarmManager().cancel(pi);
            Log.d(TAG, "PI Cancelled " + doesPendingIntentExist(i, requestCode));
        }
    }

    public boolean isInterception(){return inInterception;}

    /**看不懂的废弃方法
     public void setFragmentPosition(int position) {
     FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
     Fragment currentFragment = mFragments.get(position);
     Fragment lastFragment = mFragments.get(lastIndex);
     lastIndex = position;
     //if(lastFragment.isVisible())
     //ft.hide(lastFragment);
     if (!currentFragment.isAdded()) {
     getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();//只保留一份该碎片的实例
     //ft.replace(R.id.frame_for_fragment, currentFragment);
     }
     ft.replace(R.id.frame_for_fragment, currentFragment);
     //ft.show(currentFragment);
     ft.commitAllowingStateLoss();
     }

     private void setupViews(){
     NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.fragNavHost);
     navController = navHostFragment.getNavController();
     NavigationUI.setupWithNavController((BottomNavigationView) findViewById(R.id.bottomNavView),navHostFragment.getNavController());
     }
     **/

}