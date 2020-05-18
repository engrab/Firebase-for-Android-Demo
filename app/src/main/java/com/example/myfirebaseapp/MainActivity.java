package com.example.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    EditText edInput;
    EditText tvOutput;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("users");

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


    private void readData() {
        mRef.child("user1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String, Object> data= (Map<String, Object>) dataSnapshot.getValue();
                Log.d(TAG, "onDataChange: "+data.get("Name"));
                Log.d(TAG, "onDataChange: "+data.get("Age"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void initView() {
        edInput = findViewById(R.id.editTextData);
        tvOutput = findViewById(R.id.tvOutput);

    }

    private void writeData() {
        String name = edInput.getText().toString();
        int age = Integer.parseInt(tvOutput.getText().toString());

        Map<String, Object> databaseValue = new HashMap<>();

        databaseValue.put("user1/Name", name);
        databaseValue.put("user1/Age", age);

        databaseValue.put("-M7b5Fe-JzTLhlIv2iNQ/Name", name);
        databaseValue.put("-M7b5Fe-JzTLhlIv2iNQ/Age", age);
        mRef.updateChildren(databaseValue);
    }
}
