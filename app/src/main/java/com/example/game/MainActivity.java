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
    private Firebase firebase;

    private DatabaseReference myRef;

    private DataDAO dataDAO;
    boolean checkCookie(DataDAO dataDAO){
        if(dataDAO.getDataCount() == 0) {
            return false;
        }
        else{
            Map<String, String> data = dataDAO.getAllDataAsMap();
            String iduser = data.get("ID User");
            String cookie = data.get("Cookie");
            boolean check = false;
            myRef = FirebaseDatabase.getInstance().getReference("/cookie/" + iduser);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getValue().equals(cookie)) {
                                Log.d("Check cookie", "Tin chuan");
                                break;
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Check", "Failed to read value.", error.toException());
                }
            });
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataDAO = new DataDAO(this);
        dataDAO.open();
        loading = new Loading(this);
        loading.show();
        myRef = FirebaseDatabase.getInstance().getReference("/user/g140702");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Kiểm tra xem nút hiện tại có tên là "user" hoặc "password" không
                        if (snapshot.getKey().equals("username") || snapshot.getKey().equals("password")) {
                            // Lấy dữ liệu từ nút con hiện tại và hiển thị trong Toast
                            String key = snapshot.getKey();
                            String value = snapshot.getValue(String.class);
                            Log.d("Check", "Value is: " + key + " " + value);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Check", "Failed to read value.", error.toException());
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Chuyển đến màn hình LoginForm sau khi thời gian chờ kết thúc
                Log.d("Check", "Chuyen canh");
                Intent loginIntent = new Intent(MainActivity.this, LoginForm.class);
                startActivity(loginIntent);
                loading.cancel();
                finish(); // Đóng MainActivity để ngăn người dùng quay lại sau khi đã chuyển đến LoginForm
            }
        }, SPLASH_TIME_OUT);
    }
}