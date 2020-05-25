package com.example.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
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
    private static final int EXTERNAL_STORAGE_CODE = 1000;
    private static final int PICK_IMAGE_REQUEST = 1001;
    EditText edInput;
    EditText tvOutput;

    StorageReference mRef;
    private boolean mGranted;


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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!mGranted) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_CODE);
                    return;
                }
            }
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select Image"), PICK_IMAGE_REQUEST);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_CODE && grantResults.length>0){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                mGranted = true;
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(MainActivity.this, "Permission needed", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null){
            Uri imageUri = data.getData();
            UploadTask uploadTask = mRef.child("images/" + imageUri.getLastPathSegment()).putFile(imageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MainActivity.this, "Image uploaded successfuly", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
