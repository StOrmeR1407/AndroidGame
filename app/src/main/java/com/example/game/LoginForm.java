package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginForm extends AppCompatActivity {
    private TextView letsignup;
    private Button login_btn;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        letsignup = findViewById(R.id.letsignup);

        letsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Check", "Nut sign up da bam!");
                intent = new Intent(LoginForm.this, SignupForm.class);
                startActivity(intent);
            }
        });

        login_btn = findViewById(R.id.loginbtn);

    }


}