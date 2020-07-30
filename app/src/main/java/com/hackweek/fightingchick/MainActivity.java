package com.hackweek.fightingchick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private int lastIndex;
    List<Fragment> mFragments;

    //private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBottomNavigation();
        initData();
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

    private void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = mFragments.get(position);
        Fragment lastFragment = mFragments.get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.frame_for_fragment, currentFragment);
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



}