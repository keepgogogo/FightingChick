package com.hackweek.fightingchick.toolpackage;

import android.content.res.AssetManager;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetsOperate extends AppCompatActivity {

        public String textFileGet(String fileName, AssetManager assetManager)
        {
            StringBuilder message=new StringBuilder();

            try {
                InputStream promptMessage=assetManager.open(fileName);
                BufferedReader reader=new BufferedReader(new InputStreamReader(promptMessage));
                String line="";
                while((line=reader.readLine())!=null)
                {
                    message.append(line);
                    message.append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return message.toString();
        }
}

