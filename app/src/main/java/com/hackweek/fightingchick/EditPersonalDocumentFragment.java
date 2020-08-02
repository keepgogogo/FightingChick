package com.hackweek.fightingchick;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class EditPersonalDocumentFragment extends Fragment {

    SharedPreferences preferences;

    TextView originNickName;
    TextView originFightForeverSlogan;
    EditText editNickName;
    EditText editFightForeverSlogan;
    Button button;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        originFightForeverSlogan=(TextView)view.findViewById(R.id.OriginFightForeverSlogan);
        originNickName=(TextView)view.findViewById(R.id.TextViewForOriginNickName);
        editNickName=(EditText)view.findViewById(R.id.EditNickName);
        editFightForeverSlogan=(EditText)view.findViewById(R.id.EditFightForeverSlogan);
        button=(Button)view.findViewById(R.id.ButtonForSaveChangeInPersonalDocument);

        MainActivity mainActivity=new MainActivity();
        preferences=mainActivity.getSharedPreferences("PersonalDocument", Context.MODE_PRIVATE);
        originNickName.setText(preferences.getString("NickName","昵称"));
        originFightForeverSlogan.setText(preferences.getString("FightForeverSlogan","永动宣言"));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newNickName=editNickName.getText().toString();
                if (newNickName.length()>9)newNickName="";
                String newFightSlogan=editFightForeverSlogan.getText().toString();
                if(!newFightSlogan.equals("") && !newNickName.equals(""))
                {
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("NickName",newNickName);
                    editor.putString("FightForeverSlogan",newFightSlogan);
                    editor.apply();
                    mainActivity.setFragment(new MineFragment());
                }
                else
                {
                    AlertDialog.Builder dialog=new AlertDialog.Builder(mainActivity);
                    dialog.setTitle("提示");
                    dialog.setCancelable(true);
                    dialog.setMessage("请确保新昵称和新永动宣言不为空，且昵称字数不超过9个");
                    dialog.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dialog.show();
                }
            }
        });

    }





    public EditPersonalDocumentFragment() {
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
        return inflater.inflate(R.layout.fragment_edit_personal_document, container, false);
    }
}