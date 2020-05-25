package com.example.myfirebaseapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


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
        mRef = FirebaseStorage.getInstance().getReference("docs/");


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
        StorageReference child = mRef.child("office/abc.txt");
        String fileData = "This is demo data";

        UploadTask uploadTask = child.putBytes(fileData.getBytes());
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(MainActivity.this, "Data upload successfully", Toast.LENGTH_LONG).show();
            }
        });



    }

}
