package com.hackweek.fightingchick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hackweek.fightingchick.database.FocusListDataBase;
import com.hackweek.fightingchick.database.GloryAndConfessionDataBase;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class MainActivity extends AppCompatActivity {

    final int TRANSFER_DATABASE_TO_MINE_FRAGMENT=566;

    private BottomNavigationView mBottomNavigationView;
    private int lastIndex;
    List<Fragment> mFragments;
    FocusListDataBase focusListDataBase;
    GloryAndConfessionDataBase gloryAndConfessionDataBase;

    //private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBottomNavigation();
        initData();

        focusListDataBase= Room.databaseBuilder(getApplicationContext(), FocusListDataBase.class,
                "FocusDataBase").build();
        gloryAndConfessionDataBase=Room.databaseBuilder(getApplicationContext(),GloryAndConfessionDataBase.class,
                "GloryAndConfessionDataBase").build();
        //setupViews();
    }

    public void initBottomNavigation() {
        mBottomNavigationView = findViewById(R.id.bottomNavView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.todoListFragment:
                        setFragmentPosition(0);
                        break;
                    case R.id.recordsFragment:
                        setFragmentPosition(1);
                        break;
                    case R.id.mineFragment:
                        setFragmentPosition(2);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(new TodoListFragment());
        mFragments.add(new RecordsFragment());
        mFragments.add(new MineFragment());
        // 初始化展示TodoListFragment
        setFragmentPosition(0);
    }

    //用来操作子碎片
    public void setFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_for_fragment,fragment).commit();
    }

    //用来自动操作主要的3个碎片
    public void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = mFragments.get(position);
        Fragment lastFragment = mFragments.get(lastIndex);
        lastIndex = position;
        if(lastFragment.isVisible())
            ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();//只保留一份该碎片的实例
            ft.replace(R.id.frame_for_fragment, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }

    /**
    private void setupViews(){
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.fragNavHost);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController((BottomNavigationView) findViewById(R.id.bottomNavView),navHostFragment.getNavController());
    }
     **/

    public class MainActivityHandler extends Handler
    {

        @Override
        public void handleMessage(@Nonnull Message message)
        {
            switch (message.what)
            {
                case TRANSFER_DATABASE_TO_MINE_FRAGMENT:
                    MineFragment mineFragment=(MineFragment)getSupportFragmentManager()
                            .findFragmentById(R.id.mineFragment);
                    if(mineFragment!=null)
                    {
                        mineFragment.setBothDataBase(focusListDataBase,gloryAndConfessionDataBase);
                    }
                    else
                    {
                        MineFragment newFragment=new MineFragment();
                        setFragment(newFragment);
                    }
                    break;
                default:
                    break;

            }
        }
    }



}