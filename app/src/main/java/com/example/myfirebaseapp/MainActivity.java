package com.example.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    EditText edInput;
    EditText tvOutput;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ChildEventListener mListener;
    private RecyclerView mRecyclerView;
    private UserAdapter mUserAdapter;
    private List<User> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        mList = new ArrayList<>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUserAdapter =  new UserAdapter(this, mList);
        mRecyclerView.setAdapter(mUserAdapter);

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


        mListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               User user = dataSnapshot.getValue(User.class);
               user.setUid(dataSnapshot.getKey());
               mList.add(user);
               mUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mRef.addChildEventListener(mListener);

    }

    @Override
    protected void onDestroy() {

        mRef.removeEventListener(mListener);
        super.onDestroy();

    }

    private void readData() {

    }

    private void initView() {
        edInput = findViewById(R.id.editTextData);
        tvOutput = findViewById(R.id.tvOutput);

        mRecyclerView = findViewById(R.id.user_recyclerview);


    }

    private void writeData() {
        String name = edInput.getText().toString();
        int age = Integer.parseInt(tvOutput.getText().toString());

        User user = new User(name, age);

        String key = mRef.push().getKey();
        mRef.child(key).setValue(user);


    }
}
