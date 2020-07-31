package com.hackweek.fightingchick;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hackweek.fightingchick.database.FocusListDataBase;
import com.hackweek.fightingchick.database.GloryAndConfessionDataBase;


public class MineFragment extends Fragment implements View.OnClickListener {

    final int TRANSFER_DATABASE_TO_MINE_FRAGMENT=566;

    SharedPreferences preferences;
    TextView nickNameTextView;
    TextView fightingForeverSloganTextView;
    Button buttonForSetPersonalStatics;
    Button buttonForWatchGloryAndConfessions;
    Button buttonForWatchAboutUs;

    FocusListDataBase focusListDataBase;
    GloryAndConfessionDataBase gloryAndConfessionDataBase;

    /**
     * 以下为fragment向MainActivity传递信息模块
     */
    onFragmentSetListener callBack;

    public void setOnFragmentSetListener(onFragmentSetListener callback)
    {
        callBack=callback;
    }

    public interface onFragmentSetListener
    {

    }


    /**
     * 以上为fragment向MainActivity传递信息模块
     */









    public MineFragment() {
        // Required empty public constructor
    }

    public void setBothDataBase(FocusListDataBase focusListDataBase,GloryAndConfessionDataBase gloryAndConfessionDataBase)
    {
        this.focusListDataBase=focusListDataBase;
        this.gloryAndConfessionDataBase=gloryAndConfessionDataBase;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        nickNameTextView=(TextView)view.findViewById(R.id.NickName);
        fightingForeverSloganTextView=(TextView)view.findViewById(R.id.FightingForeverSlogan);
        buttonForSetPersonalStatics=(Button)view.findViewById(R.id.ButtonForChangePersonalDocument);
        buttonForWatchAboutUs=(Button)view.findViewById(R.id.ButtonForWatchAboutUsInMineFragment);
        buttonForWatchGloryAndConfessions=(Button)view.findViewById(R.id.ButtonForWatchGloryAndConfessionInMineFragment);

        MainActivity mainActivity=new MainActivity();
        preferences=mainActivity.getSharedPreferences("PersonalDocument", Context.MODE_PRIVATE);

        nickNameTextView.setText(preferences.getString("NickName","昵称"));
        fightingForeverSloganTextView.setText(preferences.getString("FightForeverSlogan","永动宣言"));



        return view;
    }

    @Override
    public void onClick(View view) {
        MainActivity mainActivity=new MainActivity();
        switch (view.getId())
        {
            case R.id.ButtonForChangePersonalDocument:
                //编辑个人资料点击事件
                mainActivity.setFragment(new EditPersonalDocumentFragment());
                break;
            case R.id.ButtonForWatchAboutUsInMineFragment:
                //关于我们点击事件
                mainActivity.setFragment(new AboutUsFragment());
                break;
            case R.id.ButtonForWatchGloryAndConfessionInMineFragment:
                //查看光荣录和忏悔录点击事件
                mainActivity.setFragment(new AllGloryAndConfessionFragment());
                break;
            default:
                break;
        }
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        aboutUs = (Button)view.findViewById(R.id.ButtonForWatchAboutUsInMineFragment);
//        aboutUs.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                MainActivity activity = (MainActivity)getActivity();
//                Fragment fragment = new TodoListFragment();
//                activity.setFragment(fragment);
//            }
//        });
//    }
}