package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Firebase;

import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000;
    private Loading loading;
    private DatabaseReference myRef;
    private DataDAO dataDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataDAO = new DataDAO(this);
        dataDAO.open();

        loading = new Loading(this);
        loading.show();

        if(dataDAO.getDataCount() == 0) {
            Log.d("Check cookie", "Khong co data");
            GotoLogin();
        }
        else{
            Map<String, String> data = dataDAO.getAllDataAsMap();
            String iduser = data.get("ID User");
            String cookie = data.get("Cookie");
            dataDAO.close();
            Log.d("Layid",data.get("ID"));
            myRef = FirebaseDatabase.getInstance().getReference("/cookie/" + iduser);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean check = false;
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getValue().toString().equals(cookie)) {
                                check = true;
                                break;
                            }
                        }
                    }
                    if(check == true){
                        Log.d("Check cookie", "Tin chuan");
                        GotoDashboard();
                    }
                    else{
                        Log.d("Check cookie", "Tin chua chuan");
                        GotoLogin();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Check", "Failed to read value.", error.toException());
                }
            });
        }
    }

    public void GotoLogin(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Chuyển đến màn hình LoginForm sau khi thời gian chờ kết thúc
                Log.d("Check", "Chuyen toi log in");
                Intent loginIntent = new Intent(MainActivity.this, LoginForm.class);
                startActivity(loginIntent);
                loading.cancel();
                finish(); // Đóng MainActivity để ngăn người dùng quay lại sau khi đã chuyển đến LoginForm
            }
        }, SPLASH_TIME_OUT);
    }

    public void GotoDashboard(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Chuyển đến màn hình LoginForm sau khi thời gian chờ kết thúc
                Log.d("Check", "Chuyen toi dashboard");
                Intent loginIntent = new Intent(MainActivity.this, Dashboard.class);
                startActivity(loginIntent);
                loading.cancel();
                finish(); // Đóng MainActivity để ngăn người dùng quay lại sau khi đã chuyển đến LoginForm
            }
        }, SPLASH_TIME_OUT);
    }
}