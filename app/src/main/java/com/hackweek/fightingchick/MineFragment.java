package com.hackweek.fightingchick;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;



import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;




import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;



import de.hdodenhof.circleimageview.CircleImageView;


public class MineFragment extends Fragment implements View.OnClickListener {

    final static String HEAD_PORTRAIT="portrait.jpg";
    private static final String TAG = "MineFragment";

    final int TRANSFER_DATABASE_TO_MINE_FRAGMENT=566;

    SharedPreferences preferences;
    TextView nickNameTextView;
    TextView fightingForeverSloganTextView;
    Button buttonForSetPersonalStatics;
    Button buttonForWatchGloryAndConfessions;
    Button buttonForWatchAboutUs;
    CircleImageView circleImageView;

    MainActivity mainActivity;



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
        mainActivity=(MainActivity)getActivity();

        circleImageView=(CircleImageView)view.findViewById(R.id.CircleImageViewForHeadPortraitInMineFragment);

        if(isPortraitExisted()) {
            try {
                setViewHeadPortrait();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        nickNameTextView=(TextView)view.findViewById(R.id.NickName);
        fightingForeverSloganTextView=(TextView)view.findViewById(R.id.FightingForeverSlogan);
        buttonForSetPersonalStatics=(Button)view.findViewById(R.id.ButtonForChangePersonalDocument);
        buttonForWatchAboutUs=(Button)view.findViewById(R.id.ButtonForWatchAboutUsInMineFragment);
        buttonForWatchGloryAndConfessions=(Button)view.findViewById(R.id.ButtonForWatchGloryAndConfessionInMineFragment);
        buttonForSetPersonalStatics.setOnClickListener(this);
        buttonForWatchAboutUs.setOnClickListener(this);
        buttonForWatchGloryAndConfessions.setOnClickListener(this);

        MainActivity mainActivity=(MainActivity)getActivity();
        assert mainActivity != null;
        preferences=mainActivity.getPreferences( Context.MODE_PRIVATE);
        nickNameTextView.setText(preferences.getString(getString(R.string.nickname_key),"昵称"));
        fightingForeverSloganTextView.setText(preferences.getString(getString(R.string.resolutions_key),"永动宣言"));

        return view;
    }


    @Override
    public void onClick(View view) {
        MainActivity mainActivity=(MainActivity)getActivity();
        switch (view.getId())
        {
            case R.id.ButtonForChangePersonalDocument:
                //编辑个人资料点击事件
                assert mainActivity != null;
                mainActivity.setFragment(new EditPersonalDocumentFragment());
                break;
            case R.id.ButtonForWatchAboutUsInMineFragment:
                //关于我们点击事件
                assert mainActivity != null;
                mainActivity.setFragment(new AboutUsFragment());
                break;
            case R.id.ButtonForWatchGloryAndConfessionInMineFragment:
                //查看光荣录和忏悔录点击事件
                assert mainActivity != null;
                mainActivity.setFragment(new AllGloryAndConfessionFragment());
                break;
            default:
                break;
        }
    }

    public void setViewHeadPortrait() throws FileNotFoundException {
        FileInputStream inputStream=mainActivity.openFileInput(HEAD_PORTRAIT);
        Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
        circleImageView.setImageBitmap(bitmap);
    }

    public boolean isPortraitExisted()
    {
        File file=new File(mainActivity.getFilesDir()+"/"+HEAD_PORTRAIT);
        Log.d(TAG, "isPortraitExisted: portrait loaded  "+file.length());
        return file.exists();
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