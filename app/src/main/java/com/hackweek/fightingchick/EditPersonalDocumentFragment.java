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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class EditPersonalDocumentFragment extends Fragment implements View.OnClickListener{

    SharedPreferences preferences;
    EditText editNickName;
    EditText editFightForeverSlogan;
    ImageButton backFromEdit;
    ImageButton clearNickname;
    ImageButton clearSlogan;
    Button button;
    MainActivity mainActivity;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        editNickName = (EditText) view.findViewById(R.id.EditNickName);
        editFightForeverSlogan = (EditText) view.findViewById(R.id.EditFightForeverSlogan);
        button = (Button) view.findViewById(R.id.ButtonForSaveChangeInPersonalDocument);
        backFromEdit = (ImageButton)view.findViewById(R.id.back_from_edit_personal);
        clearNickname = (ImageButton)view.findViewById(R.id.clear_nickname);
        clearSlogan = (ImageButton)view.findViewById(R.id.clear_slogan);
        clearNickname.setImageAlpha(150);
        clearSlogan.setImageAlpha(150);

        button.setOnClickListener(this);
        backFromEdit.setOnClickListener(this);
        clearSlogan.setOnClickListener(this);
        clearNickname.setOnClickListener(this);


        mainActivity = (MainActivity) getActivity();
        preferences = mainActivity.getPreferences(Context.MODE_PRIVATE);
        editNickName.setText(preferences.getString(getString(R.string.nickname_key), "请在此输入新昵称"));
        editFightForeverSlogan.setText(preferences.getString(getString(R.string.resolutions_key), "请在此输入新的永动宣言"));


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ButtonForSaveChangeInPersonalDocument:
                String newNickName = editNickName.getText().toString();
                if (newNickName.length() > 9) newNickName = "";
                String newFightSlogan = editFightForeverSlogan.getText().toString();
                if (!newFightSlogan.equals("") && !newNickName.equals("")) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(getString(R.string.nickname_key), newNickName);
                    editor.putString(getString(R.string.resolutions_key), newFightSlogan);
                    editor.apply();
                    Toast.makeText(getContext(),"保存成功",Toast.LENGTH_SHORT).show();
                    mainActivity.setFragment(new MineFragment());
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity);
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
                break;
            case R.id.clear_nickname:
                editNickName.setText("");
                break;
            case R.id.clear_slogan:
                editFightForeverSlogan.setText("");
                break;
            case R.id.back_from_edit_personal:
                mainActivity.setFragment(new MineFragment());
        }
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