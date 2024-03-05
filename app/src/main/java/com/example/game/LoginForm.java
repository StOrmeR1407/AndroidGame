package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginForm extends AppCompatActivity {
    private static final String ALLOWED_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private TextView letsignup;
    private Button login_btn;
    private Intent intent;
    private Loading loading;
    private DatabaseReference databaseRef;
    private EditText loginusername, loginpassword;
    private DataDAO dataDAO;

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        String currentTime = sdf.format(new Date());
        return currentTime;
    }

    public void loginUser(String enteredUsername, String enteredPassword) {
        loading.show();
        databaseRef = FirebaseDatabase.getInstance().getReference("user");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean found = false;
                    String iduser = "";
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String username = userSnapshot.child("username").getValue(String.class);
                        String password = userSnapshot.child("password").getValue(String.class);
                        if (username != null && password != null && username.equals(enteredUsername) && password.equals(enteredPassword)) {
                            String current_time = getCurrentDateTime();
                            String cookie = generateRandomString(10);
                            iduser = userSnapshot.getKey();

                            FirebaseDatabase.getInstance().getReference("cookie").child(iduser).child(current_time).setValue(cookie);
                            dataDAO.insertData(iduser,username,cookie,"1","0");
                            int count = dataDAO.getDataCount();
                            Log.d("write data",String.valueOf(count));
                            found = true;
                            break;
                        }
                    }
                    loading.cancel();
                    if (found) {
                        Toast.makeText(getApplicationContext(), "Login success, " + iduser, Toast.LENGTH_SHORT).show();
                        intent = new Intent(LoginForm.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No user data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        letsignup = findViewById(R.id.letsignup);
        loginusername = findViewById(R.id.username);
        loginpassword = findViewById(R.id.password);
        loading = new Loading(this);
        dataDAO = new DataDAO(this);
        dataDAO.open();
        letsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Check", "Nut sign up da bam!");
                intent = new Intent(LoginForm.this, SignupForm.class);
                startActivity(intent);
            }
        });
        login_btn = findViewById(R.id.loginbtn);
        login_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loginUser(loginusername.getText().toString(),loginpassword.getText().toString());
            }
        });
    }


}