package com.example.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Rank extends AppCompatActivity {
    private DatabaseReference RankRef;
    private TableLayout table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        table = findViewById(R.id.tablerank);

        RankRef = FirebaseDatabase.getInstance().getReference("/rank");
        RankRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int stt = 1;
                List<RankUser> userList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Lặp qua các nút con (dates)
                    String userId = userSnapshot.getKey();
                    int level = userSnapshot.child("level").getValue(Integer.class);
                    double timeValue = userSnapshot.child("time").getValue(Double.class);

                    userList.add(new RankUser(userId, level, timeValue));

                    Log.d("Getdata","Stt: " + stt + ", User: " + userId + ", Level: " + level + ", TimeValue: " + timeValue);
                }
                Collections.sort(userList, new Comparator<RankUser>() {
                    @Override
                    public int compare(RankUser u1, RankUser u2) {
                        // Sắp xếp theo level giảm dần
                        int levelComparison = Integer.compare(u2.getLevel(), u1.getLevel());
                        if (levelComparison != 0) {
                            return levelComparison;
                        } else {
                            // Nếu level bằng nhau, sắp xếp theo time tăng dần
                            return Double.compare(u1.getTime(), u2.getTime());
                        }
                    }
                });
                for (RankUser user : userList) {
                    TableRow row = new TableRow(Rank.this);
                    row.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1
                    ));

                    TextView tv1 =new TextView(Rank.this);
                    tv1.setGravity(Gravity.CENTER);
                    tv1.setText(String.valueOf(stt));
                    tv1.setTextColor(getResources().getColor(R.color.white));
                    tv1.setTextSize(20);
                    tv1.setTypeface(ResourcesCompat.getFont(Rank.this, R.font.baloo));

                    TextView tv2=new TextView(Rank.this);
                    tv2.setGravity(Gravity.CENTER);
                    tv2.setText(String.valueOf(user.getUserId()));
                    tv2.setTextColor(getResources().getColor(R.color.white));
                    tv2.setTextSize(20);
                    tv2.setTypeface(ResourcesCompat.getFont(Rank.this, R.font.baloo));

                    TextView tv3=new TextView(Rank.this);
                    tv3.setGravity(Gravity.CENTER);
                    tv3.setText(String.valueOf(String.valueOf(user.getLevel())));
                    tv3.setTextColor(getResources().getColor(R.color.white));
                    tv3.setTextSize(20);
                    tv3.setTypeface(ResourcesCompat.getFont(Rank.this, R.font.baloo));

                    TextView tv4=new TextView(Rank.this);
                    tv4.setGravity(Gravity.CENTER);
                    tv4.setText(String.valueOf(String.valueOf(user.getTime())));
                    tv4.setTextColor(getResources().getColor(R.color.white));
                    tv4.setTextSize(20);
                    tv4.setTypeface(ResourcesCompat.getFont(Rank.this, R.font.baloo));

                    row.addView(tv1);
                    row.addView(tv2);
                    row.addView(tv3);
                    row.addView(tv4);
                    table.addView(row);
                    stt++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                System.out.println("Lỗi: " + databaseError.getMessage());
            }
        });
    }
}