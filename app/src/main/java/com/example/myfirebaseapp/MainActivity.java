package com.example.myfirebaseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    EditText edInput;
    EditText tvOutput;

    StorageReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        mRef = FirebaseStorage.getInstance().getReference("doc/");


        findViewById(R.id.btn_write).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeData();
            }
        });
        findViewById(R.id.btn_read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData();
            }
        });




    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    private void readData() {


    }

    private void initView() {
        edInput = findViewById(R.id.editTextData);
        tvOutput = findViewById(R.id.tvOutput);



    }

    private void writeData() {

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(getFilesDir(), "unnamed.jpg"));

            Log.d(TAG, "writeData: ");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        UploadTask uploadTask = null;
        if (inputStream != null) {
            uploadTask = mRef.child("image/unnamed.jpg").putStream(inputStream);
            Log.d(TAG, "writeData: inputstream");
        }
        final InputStream finalInputStream = inputStream;
        if (uploadTask != null) {
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (finalInputStream != null){
                        try {
                            finalInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(MainActivity.this, "Picture Uploaded Successfuly", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private Bitmap readImage(){
        InputStream inputStream = null;
        try {

            inputStream = getAssets().open("unnamed.jpg");

            BitmapDrawable bitmapDrawable = (BitmapDrawable) Drawable.createFromStream(inputStream, null);

            return bitmapDrawable.getBitmap();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (inputStream != null){
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
