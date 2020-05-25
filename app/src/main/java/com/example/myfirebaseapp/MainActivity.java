package com.example.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private static final int READ_EXTERNAL_STORAGE_CODE = 1000;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1002;
    private static final int PICK_IMAGE_REQUEST = 1001;


    StorageReference mRef;
    private boolean mReadGranted, mWriteGranted;
    private ProgressBar mProgressBar;
    private TextView mProgressText;
    ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        mProgressText.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!mWriteGranted) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
                    Log.d(TAG, "readData: Permission");
                    return;
                }
            }
        }


         final File outputFile = new File(Environment.getExternalStorageDirectory(), "aliya.jpg");

        long IMAGE_SIZE = 1024*1024*5;
        mRef.child("images/image:222975").getBytes(IMAGE_SIZE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        mImageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                            fileOutputStream.write(bytes);
                            fileOutputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onSuccess: Image set");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });


    }

    private void initView() {
        mProgressBar = findViewById(R.id.progressBar);
        mProgressText = findViewById(R.id.tv_progress);
        mImageView = findViewById(R.id.imageView);

    }

    private void writeData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!mReadGranted) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
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
        if (requestCode == READ_EXTERNAL_STORAGE_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mReadGranted = true;
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Permission needed", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mWriteGranted = true;
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Permission needed", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null) {
            Uri imageUri = data.getData();
            UploadTask uploadTask = mRef.child("images/" + imageUri.getLastPathSegment()).putFile(imageUri);
            mProgressText.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setIndeterminate(false);

            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    mProgressText.setText(progress + " %");
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setMax(100);
                    mProgressBar.setProgress((int) progress);

                }
            })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(MainActivity.this, "Image uploaded successfuly", Toast.LENGTH_LONG).show();
                            mProgressText.append("Upload Finished");
                        }
                    });
        }
    }
}
