package com.hackweek.fightingchick;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class MineFragment extends Fragment implements View.OnClickListener {

    TextView nickNameTextView;
    TextView fightingForeverSloganTextView;
    Button buttonForSetPersonalStatics;
    Button buttonForWatchGloryAndConfessions;
    Button buttonForWatchAboutUs;

    public MineFragment() {
        // Required empty public constructor
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



        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ButtonForChangePersonalDocument:
                //编辑个人资料点击事件
                MainActivity mainActivity=new MainActivity();
                mainActivity.setFragment(new EditPersonalDocumentFragment());
                break;
            case R.id.ButtonForWatchAboutUsInMineFragment:
                //关于我们点击事件

                break;
            case R.id.ButtonForWatchGloryAndConfessionInMineFragment:
                //查看光荣录和忏悔录点击事件

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