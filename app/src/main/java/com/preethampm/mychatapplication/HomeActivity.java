package com.preethampm.mychatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.preethampm.mychatapplication.adapter.UserAdapter;
import com.preethampm.mychatapplication.model_class.Users;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {


    FirebaseAuth firebaseAuth;
    RecyclerView homeUserRecyclerView;
    UserAdapter userAdapter;
    FirebaseDatabase firebaseDatabase;
    ArrayList<Users> userArrayList;
    ImageView powerImageView, settingImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        userArrayList = new ArrayList<>();

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("User");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    if (users.getUid().equals(firebaseAuth.getUid())) {} else {
                        userArrayList.add(users);
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        powerImageView = findViewById(R.id.logout_image);

        powerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(HomeActivity.this, R.style.Dialogue);
                dialog.setContentView(R.layout.dialogue_layout);

                TextView yesBtn, noBtn;
                yesBtn = dialog.findViewById(R.id.yes_btn_of_dialogue);
                noBtn = dialog.findViewById(R.id.no_btn_of_dialogue);

                dialog.show();

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });


        settingImageView = findViewById(R.id.setting_image);
        settingImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
            }
        });

        homeUserRecyclerView = findViewById(R.id.home_user_recycler_view);
        homeUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(HomeActivity.this, userArrayList);
        homeUserRecyclerView.setAdapter(userAdapter);

        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }
    }
}