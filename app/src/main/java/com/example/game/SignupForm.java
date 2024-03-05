package com.example.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class SignupForm extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private EditText repassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_form);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
    }

    public static String generateRandomNumber(ArrayList<String> number) {
        Random rand = new Random();
        boolean unique = false;
        String randomString = "";

        while (!unique) {
            StringBuilder sb = new StringBuilder();
            sb.append("g");
            for (int i = 0; i < 6; i++) {
                sb.append(rand.nextInt(10));
            }
            randomString = sb.toString();
            if (!number.contains(randomString)) {
                unique = true;
            }
        }

        return randomString;
    }

    public void register(View view){
        if(username.getText().toString().length() < 8){
            Toast.makeText(getApplicationContext(), "Username phải trên 8 kí tự", Toast.LENGTH_SHORT).show();
        }
        else if(password.getText().toString().length() < 8){
            Toast.makeText(getApplicationContext(), "Password phải trên 8 kí tự", Toast.LENGTH_SHORT).show();
        }
        else if(!password.getText().toString().equals(repassword.getText().toString())){
            Log.d("Saimkps", password.getText().toString());
            Log.d("Saimkps", repassword.getText().toString());
            Toast.makeText(getApplicationContext(), "Xác nhận mật khẩu chưa đúng", Toast.LENGTH_SHORT).show();
        }
        else{
            DatabaseReference iduserlist = FirebaseDatabase.getInstance().getReference("user");
            ArrayList<String> list = new ArrayList<>();
            iduserlist.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot userSnapshot : snapshot.getChildren()){
                            String iduser = userSnapshot.getKey();
                            list.add(iduser);
                            Log.d("Dataiduser", iduser);
                        }
                    }
                    else{
                        Log.d("Exeption", "Getfailed");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Exeption", "Getfailed");
                }
            });

            String newiduser = generateRandomNumber(list);
            FirebaseDatabase.getInstance().getReference("user")
                    .child(newiduser)
                    .child("username")
                    .setValue(username.getText().toString());
            FirebaseDatabase.getInstance().getReference("user")
                    .child(newiduser)
                    .child("password")
                    .setValue(password.getText().toString());

            Toast.makeText(getApplicationContext(), "Đăng kí thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignupForm.this, LoginForm.class);
            startActivity(intent);
            finish();
        }
    }
}