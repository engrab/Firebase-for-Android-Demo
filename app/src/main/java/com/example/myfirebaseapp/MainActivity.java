package com.example.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    EditText edInput;
    EditText tvOutput;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    //    private ChildEventListener mListener;
    private RecyclerView mRecyclerView;
    private UserAdapter mUserAdapter;
    private List<User> mList;
    private ValueEventListener mQueryListener;
    private AlertDialog mAlertDialog;
    EditText edName, edCity, edAge, edProfession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("users");

        mList = new ArrayList<>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUserAdapter = new UserAdapter(this, mList);
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


//        mListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//               User user = dataSnapshot.getValue(User.class);
//               user.setUid(dataSnapshot.getKey());
//               mList.add(user);
//               mUserAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                User user = dataSnapshot.getValue(User.class);
//                user.setUid(dataSnapshot.getKey());
//                mList.remove(user);
//                mUserAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
        mQueryListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    mList.add(user);
                }
                mUserAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
//        mRef.addChildEventListener(mQueryListener);

    }

    @Override
    protected void onDestroy() {

        mRef.removeEventListener(mQueryListener);
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
//        inputDialog();

//        String name = edInput.getText().toString();
//        int age = Integer.parseInt(tvOutput.getText().toString());
//
//        User user = new User(name, age);
//
//        String key = mRef.push().getKey();
//        mRef.child(key).setValue(user);
        //insert data here

        //1. select * from tablename

//        mRef.addValueEventListener(mQueryListener);

        //2. select * from table name where name = "Ali"

//        Query query1=mRef.orderByChild("name").equalTo("Ali");
//        query1.addValueEventListener(mQueryListener);

        //3. select * from table name order by name
//        Query query1=mRef.orderByChild("name");
//        query1.addValueEventListener(mQueryListener);
        //4. select * from table name where age > 30
//        Query query1=mRef.orderByChild("age").startAt(30);
//        query1.addValueEventListener(mQueryListener);

        //5. select * from table name where age<30
//        Query query1=mRef.orderByChild("age").endAt(30);
//        query1.addValueEventListener(mQueryListener);

        //6. select * from table name limit by 3
        mRef.limitToLast(3).addValueEventListener(mQueryListener);
        //select * from table name="ALi"


    }

    private void inputDialog() {


        try {
            View view = View.inflate(this, R.layout.customview_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(view);
            edName =view.findViewById(R.id.name);
            edAge = view.findViewById(R.id.age);
            edCity = view.findViewById(R.id.city);
            edProfession = view.findViewById(R.id.profession);


//            ((TextView) view.findViewById(R.id.dialog_heading)).setText("Want to Exit?");
//            ((TextView) view.findViewById(R.id.exit_app)).setText("Yes");
//            ((TextView) view.findViewById(R.id.cancel_app)).setText("No");
            view.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    String profession = edProfession.getText().toString();
                    String city = edCity.getText().toString();
                    String name = edName.getText().toString();
                    String age = edAge.getText().toString();

                    User user = new User(profession, city , name,age);

                    String key = mRef.push().getKey();
                    mRef.child(key).setValue(user);
                    Toast.makeText(MainActivity.this, "Data Inserted Successfuly", Toast.LENGTH_LONG).show();
                    mAlertDialog.dismiss();


                }
            });
            view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                }
            });

            mAlertDialog = alertDialogBuilder.create();
            mAlertDialog.setCancelable(false);
            mAlertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
