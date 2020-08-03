package com.hackweek.fightingchick;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hackweek.fightingchick.toolpackage.InternetAccessHelper;

import org.json.JSONException;

import javax.annotation.Nonnull;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;

public class LoginActivity extends AppCompatActivity {

    private final String TAG="LoginActivity";
    public final static int ACCEPT_BACK_DATA=1;

    private TextView username;
    private TextView password;
    private ProgressBar progressBar;
    private Button confirm;
    private CircleImageView circleImageView;
    private InternetAccessHelper internetAccessHelper;
    private LoginActivityHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        handler=new LoginActivityHandler();
        internetAccessHelper=new InternetAccessHelper();
        internetAccessHelper.setMUrl("http://111.229.89.210/match.php");
        internetAccessHelper.setContext(LoginActivity.this);
        try {
            internetAccessHelper.setByKey(InternetAccessHelper.ACCOUNT_NUMBER,"123456");
            internetAccessHelper.setByKey(InternetAccessHelper.PASSWORD,"abcdefg");
            internetAccessHelper.setLoginActivityHandler(handler);
            internetAccessHelper.connectionPostStart();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class LoginActivityHandler extends Handler
    {

        @Override
        public void handleMessage(@Nonnull Message message)
        {
            switch (message.what)
            {
                case ACCEPT_BACK_DATA:
                    Log.d(TAG, "handleMessage: "+(String)message.obj);
                    break;
            }
        }
    }

}