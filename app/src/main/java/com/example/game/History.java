package com.example.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import java.util.Map;

public class History extends AppCompatActivity {
    private DatabaseReference historyRef;
    private TableLayout table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        table = findViewById(R.id.myTableLayout);
        DataDAO dataDAO = new DataDAO(History.this);
        dataDAO.open();
        Map<String, String> data = dataDAO.getAllDataAsMap();
        String iduser = data.get("ID User");
        historyRef = FirebaseDatabase.getInstance().getReference("/user/"+ iduser +"/history");
        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    // Lặp qua các nút con (dates)
                    String date = dateSnapshot.getKey(); // Lấy ngày (key) của nút

                    for (DataSnapshot timeSnapshot : dateSnapshot.getChildren()) {
                        // Lặp qua các nút con (times) của mỗi ngày
                        String time = timeSnapshot.getKey(); // Lấy thời gian (key) của nút

                        // Lấy giá trị của "level" và "time" từ mỗi nút (time)
                        int level = timeSnapshot.child("level").getValue(Integer.class);
                        double timeValue = timeSnapshot.child("time").getValue(Double.class);

                        TableRow row = new TableRow(History.this);
                        row.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1
                        ));

                        TextView tv1 =new TextView(History.this);
                        tv1.setGravity(Gravity.CENTER);
                        tv1.setText(date);
                        tv1.setTextColor(getResources().getColor(R.color.white));
                        tv1.setTextSize(20);
                        tv1.setTypeface(ResourcesCompat.getFont(History.this, R.font.baloo));

                        TextView tv2=new TextView(History.this);
                        tv2.setGravity(Gravity.CENTER);
                        tv2.setText(time);
                        tv2.setTextColor(getResources().getColor(R.color.white));
                        tv2.setTextSize(20);
                        tv2.setTypeface(ResourcesCompat.getFont(History.this, R.font.baloo));

                        TextView tv3=new TextView(History.this);
                        tv3.setGravity(Gravity.CENTER);
                        tv3.setText(String.valueOf(level));
                        tv3.setTextColor(getResources().getColor(R.color.white));
                        tv3.setTextSize(20);
                        tv3.setTypeface(ResourcesCompat.getFont(History.this, R.font.baloo));

                        TextView tv4=new TextView(History.this);
                        tv4.setGravity(Gravity.CENTER);
                        tv4.setText(String.valueOf(timeValue));
                        tv4.setTextColor(getResources().getColor(R.color.white));
                        tv4.setTextSize(20);
                        tv4.setTypeface(ResourcesCompat.getFont(History.this, R.font.baloo));

                        row.addView(tv1);
                        row.addView(tv2);
                        row.addView(tv3);
                        row.addView(tv4);
                        table.addView(row);

                    }
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