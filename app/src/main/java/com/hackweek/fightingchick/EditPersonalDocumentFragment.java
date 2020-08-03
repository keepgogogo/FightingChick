package com.hackweek.fightingchick;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ActivityChooserView;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditPersonalDocumentFragment extends Fragment implements View.OnClickListener{

    private static final int READ_REQUEST_CODE = 42;
    private static final String TAG ="EditPersonalDocumentFragment" ;
    final int ADD_TIME_OUT=200;


    SharedPreferences preferences;
    EditText editNickName;
    EditText editFightForeverSlogan;
    ImageButton backFromEdit;
    ImageButton clearNickname;
    ImageButton clearSlogan;
    CircleImageView circleImageView;
    Button button;
    MainActivity mainActivity;
    Uri newHeadPortraitUri;
    ProgressBar progressBar;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        editNickName = (EditText) view.findViewById(R.id.EditNickName);
        editFightForeverSlogan = (EditText) view.findViewById(R.id.EditFightForeverSlogan);
        button = (Button) view.findViewById(R.id.ButtonForSaveChangeInPersonalDocument);
        backFromEdit = (ImageButton)view.findViewById(R.id.back_from_edit_personal);
        clearNickname = (ImageButton)view.findViewById(R.id.clear_nickname);
        clearSlogan = (ImageButton)view.findViewById(R.id.clear_slogan);
        circleImageView=(CircleImageView)view.findViewById(R.id.CircleImageViewForEditInEditFragment);
        progressBar=(ProgressBar)view.findViewById(R.id.ProgressBarInEditPersonalDocumentFragment);
        progressBar.setVisibility(View.INVISIBLE);

        if(isPortraitExisted()) {
            try {
                setViewHeadPortrait();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        clearNickname.setImageAlpha(150);
        clearSlogan.setImageAlpha(150);

        button.setOnClickListener(this);
        backFromEdit.setOnClickListener(this);
        clearSlogan.setOnClickListener(this);
        clearNickname.setOnClickListener(this);
        circleImageView.setOnClickListener(this);



        preferences = mainActivity.getPreferences(Context.MODE_PRIVATE);
        editNickName.setHint(preferences.getString(getString(R.string.nickname_key), "请在此输入新昵称"));
        editFightForeverSlogan.setHint(preferences.getString(getString(R.string.resolutions_key), "请在此输入新的永动宣言"));


    }

    public void setViewHeadPortrait() throws FileNotFoundException {
        FileInputStream inputStream=mainActivity.openFileInput(MineFragment.HEAD_PORTRAIT);
        Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
        circleImageView.setImageBitmap(bitmap);
    }

    public boolean isPortraitExisted()
    {
        File file=new File(mainActivity.getFilesDir()+"/"+MineFragment.HEAD_PORTRAIT);
        return file.exists();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //保存昵称和永动宣言
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

                    //启动线程用于保存头像图片
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ParcelFileDescriptor parcelFileDescriptor = null;
                            try {
                                parcelFileDescriptor = mainActivity.getContentResolver()
                                        .openFileDescriptor(newHeadPortraitUri, "r");
                                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                                Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                                File file=new File(mainActivity.getFilesDir()+"/"+MineFragment.HEAD_PORTRAIT);
                                if(file.exists())
                                {
                                    file.delete();
                                    file.createNewFile();
                                }
                                FileOutputStream outputStream=new FileOutputStream(file);
                                image.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                                outputStream.flush();
                                outputStream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            mainActivity.setFragment(new MineFragment());
                        }
                    }, ADD_TIME_OUT);


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
                //昵称输入处退格
            case R.id.clear_nickname:
                StringBuilder sb=new StringBuilder(editNickName.getText());
                sb.deleteCharAt(sb.length()-1);
                editNickName.setText(sb.toString());
                break;
                //永动宣言输入处退格
            case R.id.clear_slogan:
                StringBuilder sp=new StringBuilder(editFightForeverSlogan.getText());
                sp.deleteCharAt(sp.length()-1);
                editFightForeverSlogan.setText(sp.toString());
                break;
                //从修改个人资料fragment返回 我的 fragment
            case R.id.back_from_edit_personal:
                mainActivity.setFragment(new MineFragment());
                break;
                //点击头像启动图片选择
            case R.id.CircleImageViewForEditInEditFragment:
                Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent,READ_REQUEST_CODE);
                break;
        }
    }

    /**
     * 接受相片选择后的返回数据
     */
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            newHeadPortraitUri = null;
            if (resultData != null) {
                newHeadPortraitUri = resultData.getData();
                Log.i(TAG, "Uri: " + newHeadPortraitUri.toString());
//                showImage(uri);

                try {
                    ParcelFileDescriptor parcelFileDescriptor =
                            mainActivity.getContentResolver().openFileDescriptor(newHeadPortraitUri, "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    circleImageView.setImageBitmap(image);
                    parcelFileDescriptor.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
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