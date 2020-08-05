package com.hackweek.fightingchick.toolpackage;

import android.Manifest;


import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Message;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hackweek.fightingchick.LoginActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InternetAccessHelper {

    public final static String ACCOUNT_NUMBER="account_numbers";
    public final static String PASSWORD="password";
    final String TAG="InternetAccessHelper";

    private String mUrl;
    private Context context;
    private Request request;
    private LoginActivity.LoginActivityHandler loginActivityHandler;
    private OkHttpClient client=new OkHttpClient();
    private JSONObject jsonObject=new JSONObject();
    private MediaType JSON=MediaType.parse("application/json;charset=utf-8");

    public void connectionGetStart()
    {

    }

    public void connectionPostStart()
    {
        if (ContextCompat.checkSelfPermission(context, Manifest.
                permission.INTERNET)== PackageManager.PERMISSION_GRANTED)
        {
            RequestBody requestBody=RequestBody.create(JSON,String.valueOf(jsonObject));
        Request request=new Request.Builder()
                .url(mUrl)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d(TAG, headers.name(i) + ":" + headers.value(i));
                }
                Log.d(TAG, "onResponse: " + response.body().string());
                Message message=new Message();
                message.obj=response.body().string();
                message.what=LoginActivity.ACCEPT_BACK_DATA;
                loginActivityHandler.sendMessage(message);
            }
        });
        }
        else {
            ActivityCompat.requestPermissions((LoginActivity) context,
                    new String[]{Manifest.permission.INTERNET},1);
        }
    }

    public void setMUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setLoginActivityHandler(LoginActivity.LoginActivityHandler loginActivityHandler) {
        this.loginActivityHandler = loginActivityHandler;
    }

    public void setByKey(String key, String s) throws JSONException {
        switch (key)
        {
            case ACCOUNT_NUMBER:
                jsonObject.put(ACCOUNT_NUMBER,s);
                break;
            case PASSWORD:
                jsonObject.put(PASSWORD,s);
                break;
            default:
                break;
        }
    }

}
