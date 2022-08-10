package com.example.musicplayer;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SongInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_info);

        String path = getIntent().getStringExtra("path");
        String drawableUri = getIntent().getStringExtra("drawableUri");
        String name = getIntent().getStringExtra("name");
        String link = getIntent().getStringExtra("link");


        ImageView imageView = findViewById(R.id.songImage);
        if(path == null){
//            Toast.makeText(this, "@drawable/"+drawableUri, Toast.LENGTH_LONG).show();
            String uri = "@drawable/"+drawableUri;  // where myresource (without the extension) is the file
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            imageView.setImageDrawable(res);
        }
        else{
//            Toast.makeText(this, "showing photo from camera", Toast.LENGTH_SHORT).show();
            imageView.setImageDrawable(Drawable.createFromPath(path));

        }

        TextView nameTV = findViewById(R.id.songName);
        nameTV.setText(name);


        TextView linkTV = findViewById(R.id.songLink);
        linkTV.setText(link);

        Button back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SongInfo.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }
}