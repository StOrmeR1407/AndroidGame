package com.example.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DisplayGame extends AppCompatActivity {
    private ImageView red, green, blue, yellow;
    private boolean isLongPressed = false;
    private int level;
    private int number_quiz;
    private int duration;
    private ArrayList<String> game_quiz = new ArrayList<>();
    private ArrayList<String> user_input = new ArrayList<>();
    private long timegameover = 0;
    private static String[] colors = {"red", "green", "blue", "yellow"};
    private int timestart;
    private Handler mhandler = new Handler();
    private long currenttime;
    private DataDAO dataDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_game);
        dataDAO = new DataDAO(this);
        dataDAO.open();
        Map<String,String> data = dataDAO.getAllDataAsMap();
        Log.d("Getdata", data.get("Time") + " " + data.get("Level"));
        red = findViewById(R.id.red);
        green = findViewById(R.id.green);
        blue = findViewById(R.id.blue);
        yellow = findViewById(R.id.yellow);
        level = Integer.parseInt(data.get("Level"));
        timegameover = Integer.parseInt(data.get("Time"));
        number_quiz = 4 + (level/2);
        timestart = 0;
        duration = Integer.max(100, 1100 - 100 *(level%2));
        StartGame();
    }

    private void setLongPressListener(final ImageView imageView) {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Khi người dùng giữ màn hình, thay đổi hình ảnh sang white.xml
                        imageView.setImageResource(R.drawable.whitetriangle);
                        isLongPressed = true;
                        return true;
                    case MotionEvent.ACTION_UP:
                        // Khi người dùng thả màn hình, trả lại hình ảnh ban đầu
                        if (isLongPressed) {
                            String id = ImgtoString(imageView);
                            Log.d("Da bam", id);
                            user_input.add(id);
                            imageView.setImageResource(getOriginalImageResource(imageView.getId()));
                            isLongPressed = false;
                        }
                        return true;
                }
                return false;
            }
        });


    }

    private int getOriginalImageResource(int viewId) {
        if (viewId == R.id.red) {
            return R.drawable.redtriangle;
        } else if (viewId == R.id.green) {
            return R.drawable.greentriangle;
        } else if (viewId == R.id.blue) {
            return R.drawable.bluetriangle;
        } else if (viewId == R.id.yellow) {
            return R.drawable.yellowtriangle;
        } else {
            return 0; // Trường hợp không xác định
        }
    }

    private void blink(final ImageView imageView, final int timestart, final int duration) {

        // Thực hiện chuyển đổi trong khoảng thời gian duration

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(R.drawable.whitetriangle);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageResource(getOriginalImageResource(imageView.getId()));
                    }
                }, duration);
            }
        }, timestart);

    }

    private ImageView StringtoImg(String a){
        if(a == "red"){
            return red;
        }
        else if(a == "blue"){
            return blue;
        }
        else if(a == "green"){
            return green;
        }
        else {
            return yellow;
        }
    }

    private String ImgtoString(ImageView a){
        if(a == red){
            return "red";
        }
        else if(a == blue){
            return "blue";
        }
        else if(a == green){
            return "green";
        }
        else {
            return "yellow";
        }
    }

    public void Repeat(){
        mRunable.run();
    }

    private void StartGame(){
        Random random = new Random();
        TextView levelgame = findViewById(R.id.levelgame);
        levelgame.setText("Level: " + String.valueOf(level));
        for (int i = 0; i < number_quiz; i++) {
            int index = random.nextInt(colors.length);
            String randomValue = colors[index];
            game_quiz.add(randomValue);
            blink(StringtoImg(randomValue),timestart,duration);
            timestart += duration + 100;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currenttime = System.currentTimeMillis();
                setLongPressListener(red);
                setLongPressListener(green);
                setLongPressListener(blue);
                setLongPressListener(yellow);
                Repeat();
            }
        }, timestart + 100);
    }

    private Runnable mRunable = new Runnable() {
        @Override
        public void run() {
            if (user_input.size() < game_quiz.size()) {
                Log.d("Failed", "not ok");
                mhandler.postDelayed(this, 500);
            } else {
                if (user_input.equals(game_quiz)) {
                    level++;
                    timestart = 0;
                    timegameover += System.currentTimeMillis() - currenttime;
                    if(level%2==0){
                        number_quiz++;
                    }
                    else{
                        if(duration <= 100){
                            duration = 100;
                        }
                        else{
                            duration -= 100;
                        }
                    }
                    mhandler.removeCallbacks(mRunable);
                    StartGame();
                } else {
                    if(timegameover != 0){
                        level--;
                        Toast.makeText(getApplicationContext(), "Game over,level:" + level + "time:" + (timegameover), Toast.LENGTH_SHORT).show();
                        Map<String, String> data = dataDAO.getAllDataAsMap();
                        String iduser = data.get("ID User").toString();
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                        String current_date = sdf1.format(new Date());
                        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                        String current_time = sdf2.format(new Date());
                        Double dbtime = (double) timegameover / 1000;
                        FirebaseDatabase.getInstance().getReference("user")
                                .child(iduser)
                                .child("history")
                                .child(current_date)
                                .child(current_time)
                                .child("level")
                                .setValue(level);
                        FirebaseDatabase.getInstance().getReference("user")
                                .child(iduser)
                                .child("history")
                                .child(current_date)
                                .child(current_time)
                                .child("time")
                                .setValue(dbtime);
                        DatabaseReference dtbref = FirebaseDatabase.getInstance().getReference("/rank");
                        dtbref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                        // Lặp qua các nút con (dates)
                                        String user = userSnapshot.getKey();
                                        int getrankfb = userSnapshot.child("level").getValue(Integer.class);
                                        double gettimefb = userSnapshot.child("time").getValue(Double.class);
                                        Log.d("userfb", user +" "+ iduser);
                                        if(user.equals(iduser)){
                                            if(level > getrankfb || (level == getrankfb && gettimefb > dbtime)){
                                                FirebaseDatabase.getInstance().getReference("rank")
                                                        .child(iduser)
                                                        .child("level")
                                                        .setValue(level);
                                                FirebaseDatabase.getInstance().getReference("rank")
                                                        .child(iduser)
                                                        .child("time")
                                                        .setValue(dbtime);
                                            }
                                            break;
                                        }
                                    }
                                } else {
                                    Log.d("FirebaseData", "Data does not exist");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    mhandler.removeCallbacks(mRunable);
                    dataDAO.updateData(1,"1","0");
                    dataDAO.close();
                    Intent intent = new Intent(DisplayGame.this, Dashboard.class);
                    startActivity(intent);
                }
            }
        }
    };

    public void exit(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayGame.this);
        builder.setTitle("Bạn muốn thoát khỏi trò chơi ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(level > 1){
                    dataDAO.updateData(1,String.valueOf(level),String.valueOf(timegameover));
                }
                Intent loginIntent = new Intent(DisplayGame.this, Dashboard.class);
                startActivity(loginIntent);
                finish();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
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