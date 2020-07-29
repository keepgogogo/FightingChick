package com.hackweek.fightingchick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button;
    private CheckBox checkBox;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        button = (Button)findViewById(R.id.enter);
        textView=(TextView)findViewById(R.id.privacy_policy);
        checkBox=(CheckBox)findViewById(R.id.agree_privacy);
        button.setOnClickListener(this);
        textView.setOnClickListener(this);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.agree_privacy:
                break;
            case R.id.privacy_policy:
                showDialog();
                break;
        }
    }

    private void showDialog(){
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);
        dialog.setContentView(view);
        dialog.show();
    }
}