package com.example.game;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

public class Dashboard extends AppCompatActivity {
    private CardView ctn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ctn = findViewById(R.id.continuegame);
        DataDAO dataDAO = new DataDAO(Dashboard.this);
        dataDAO.open();
        Map<String, String> data = dataDAO.getAllDataAsMap();
        Double timegameover = Double.valueOf(data.get("Time"));
        String username = data.get("Name");
        TextView welcome = findViewById(R.id.welcomeuser);
        welcome.setText("Welcome," + username);
        if(timegameover != 0){
            ctn.setVisibility(View.VISIBLE);
        }
        dataDAO.close();
    }

    public void newGame(View view){
        DataDAO dataDAO = new DataDAO(Dashboard.this);
        dataDAO.open();
        dataDAO.updateData(1,"1","0");
        dataDAO.close();
        Intent intent = new Intent(Dashboard.this, DisplayGame.class);
        startActivity(intent);
    }

    public void continueGame(View view){
        Intent intent = new Intent(Dashboard.this, DisplayGame.class);
        startActivity(intent);
    }

    public void history(View view){
        Intent intent = new Intent(Dashboard.this, History.class);
        startActivity(intent);
    }

    public void rank(View view){
        Intent intent = new Intent(Dashboard.this, Rank.class);
        startActivity(intent);
    }

    public void about(View view){
        Intent intent = new Intent(Dashboard.this, About.class);
        startActivity(intent);
    }

    public void exit(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
        builder.setTitle("Bạn muốn thoát hay đăng xuất?");
        builder.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish(); // Thoát khỏi ứng dụng
            }
        });
        builder.setNegativeButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataDAO dataDAO = new DataDAO(Dashboard.this);
                dataDAO.open();
                dataDAO.deleteData(1);
                Intent loginIntent = new Intent(Dashboard.this, MainActivity.class);
                startActivity(loginIntent);
                finish(); // Kết thúc activity hiện tại
            }
        });
        builder.setNeutralButton("Huỷ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Đóng AlertDialog
            }
        });

        // Hiển thị AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}