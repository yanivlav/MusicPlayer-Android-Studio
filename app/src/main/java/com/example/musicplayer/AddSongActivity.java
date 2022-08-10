package com.example.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class AddSongActivity extends AppCompatActivity {

    ArrayList<Song> playList;

    EditText name, link;
    Button save, cam, gallery;
    final int CAMERA_REQUEST = 1;
    final int SELECT_PICTURE = 200;
    final int WRITE_PERMISSION_REQUEST = 1;

    Bitmap bitmap;
    ImageView resultIv;
    File photo;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);

        getPlaylist();

        resultIv = findViewById(R.id.click_image);
        name = findViewById(R.id.songName);
        link = findViewById(R.id.songlink);
        save = findViewById(R.id.saveBtn);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String nameS = name.getText().toString();
                String linkS = link.getText().toString();

                Song song;
                if (photo == null){
                     song = new Song(nameS, linkS,path);
                }
                else{
                     song = new Song(nameS, linkS,photo.getAbsolutePath());
                }

                getPlaylist();
                playList.add(song);
                setPlaylist();

                name.setText("");
                link.setText("");
                resultIv.setImageBitmap(null);
//
                Intent intent = new Intent(AddSongActivity.this, MainActivity.class);
                finish();
                startActivity(intent);

            }
        });

        gallery = findViewById(R.id.gallery_btn);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create an instance of the
                // intent of the type image
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                // pass the constant to compare it
                // with the returned requestCod
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

        cam = findViewById(R.id.cam_btn);
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                photo = new File(Environment.getExternalStorageDirectory(),"song"+(playList != null ? playList.size() : 0)+".jpg");

                Uri imageUri = FileProvider.getUriForFile(AddSongActivity.this,
                        "com.example.musicplayer.provider", //(use your app signature + ".provider" )
                        photo);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

//                photo = new File(Environment.getExternalStorageDirectory(),"song"+(playList != null ? playList.size() : 0)+".jpg");
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                startActivityForResult(intent,CAMERA_REQUEST);
            }
        });

        if(Build.VERSION.SDK_INT>=23) {
            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(hasWritePermission!= PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION_REQUEST);
            }
            else cam.setVisibility(View.VISIBLE);
        }
        else cam.setVisibility(View.VISIBLE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            // bitmap = (Bitmap)data.getExtras().get("data");
            resultIv.setImageBitmap(BitmapFactory.decodeFile(photo.getAbsolutePath()));
        }

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {

            // Get the url of the image from data
            Uri selectedImageUri = data.getData();
            path = getRealPathFromURI(selectedImageUri);

            if (null != selectedImageUri) {

                // update the preview image in the layout
//                    resultIv.setImageURI(selectedImageUri);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                resultIv.setImageBitmap(bitmap);
            }
        }
    }


    public String getRealPathFromURI(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void setPlaylist() {
        //write new object to the playlist
        try {
            FileOutputStream fos = openFileOutput("playList",MODE_PRIVATE);
            ObjectOutputStream oos  = new ObjectOutputStream(fos);
            oos.writeObject(playList);
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getPlaylist() {
        //check if there is already a playlist created
        try {
            FileInputStream fis = openFileInput("playList");
            ObjectInputStream ois = new ObjectInputStream(fis);
            playList = (ArrayList<Song>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}


