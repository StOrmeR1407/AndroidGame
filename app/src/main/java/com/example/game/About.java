package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class About extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void linkfb(View view){
        String facebookUrl = "https://www.facebook.com/hidb1407/";
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + facebookUrl));
            startActivity(facebookIntent);
        } catch (Exception e) {
            // Nếu ứng dụng Facebook không được cài đặt, mở liên kết bằng trình duyệt web
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
            startActivity(webIntent);
        }
    }

    public void linkgit(View view){
        String githubUrl = "https://github.com/StOrmeR1407";
        try {
            getPackageManager().getPackageInfo("com.github.android", 0);
            Intent githubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl));
            githubIntent.setPackage("com.github.android");
            startActivity(githubIntent);
        } catch (Exception e) {
            // Nếu ứng dụng GitHub không được cài đặt, mở liên kết bằng trình duyệt web
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl));
            startActivity(webIntent);
        }
    }

    public void linkytb(View view){
        String youtubeUrl = "https://www.youtube.com/@Stormer1407";
        try {
            getPackageManager().getPackageInfo("com.google.android.youtube", 0);
            Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl));
            youtubeIntent.setPackage("com.google.android.youtube");
            startActivity(youtubeIntent);
        } catch (Exception e) {
            // Nếu ứng dụng YouTube không được cài đặt, mở liên kết bằng trình duyệt web
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl));
            startActivity(webIntent);
        }
    }
}