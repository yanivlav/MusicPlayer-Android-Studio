package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

//media
import android.content.Intent;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    ArrayList<Song> playList;

    @Override
    public void onResume(){
        super.onResume();
        updatePlaylist();
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadPlaylist();

        if (playList==null){
            playList = new ArrayList<>();
            playList.add(new Song("eminem1", "https://audio.jukehost.co.uk/DnvjjljYd3CMKkqDYnkFAB4u0l0MSRas",R.drawable.eminem1));
            playList.add(new Song("eminem2", "https://audio.jukehost.co.uk/vV9P99y9bpcprhImcY70NP1pd9RBk1GG",R.drawable.eminem2));
            playList.add(new Song("eminem3", "https://audio.jukehost.co.uk/iKq1cijo2874lJ1i8z0YJp8Au90c3Gzi",R.drawable.eminem3));
            playList.add(new Song("post_malone1", "https://audio.jukehost.co.uk/5OZSRlmWI3p517HlUANqQSfVn3mthQ4d",R.drawable.post_malone1));
            playList.add(new Song("bob1", "http://www.syntax.org.il/xtra/bob.m4a",R.drawable.bob1));
            playList.add(new Song("bob2", "http://www.syntax.org.il/xtra/bob1.m4a",R.drawable.bob2));
            playList.add(new Song("bob3", "https://www.syntax.org.il/xtra/bob2.mp3",R.drawable.bob3));
            updatePlaylist();
//            uploadPlaylist();

        }

        Button addSong = findViewById(R.id.addSongButton);
        addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddSongActivity.class);
                startActivity(intent);
//                updatePlaylist();
                uploadPlaylist();


            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final SongAdapter songAdapter = new SongAdapter(playList);
        songAdapter.setListener(new SongAdapter.ISongListener() {

            @Override
            public void onInfoClicked(int position, View view) {
                Intent intent = new Intent(MainActivity.this, SongInfo.class);

//                if (playList.get(position).get() != null)
                    intent.putExtra("path",playList.get(position).getPhotoPath());
//                else
//                    intent.putExtra("path",playList.get(position).getPhotoPathInt());

                intent.putExtra("drawableUri",playList.get(position).getName());
                intent.putExtra("name",playList.get(position).getName());
                intent.putExtra("link",playList.get(position).getLink());
                startActivity(intent);
            }

            @Override
            public void onSongClicked(int position, View view) {



                ArrayList<String> names = new ArrayList<>();
                ArrayList<String> links = new ArrayList<>();

                for (int i = 0; i < playList.size(); i++ ){
                    names.add(playList.get(i).getName());
                    links.add(playList.get(i).getLink());
                }

                Intent intent = new Intent(MainActivity.this,MusicServiceNew.class);
                intent.putExtra("names",names);
                intent.putExtra("links",links);
                intent.putExtra("command","new_instance");
                //?
                intent.putExtra("current",position);
//                Toast.makeText(MainActivity.this,names.get(position), Toast.LENGTH_SHORT).show();
//                updatePlaylist();

                stopService(intent);
                startService(intent);
            }

            @Override
            public void onSongLongClicked(int position, View view) {

            }
        });

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

            @Override // reorder songs
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPos = viewHolder.getAdapterPosition();
                int toPos = target.getAdapterPosition();
                Collections.swap(playList, fromPos, toPos);
                updatePlaylist();
                Toast.makeText(MainActivity.this, "Changes will be effected while playing:\npress any song to reload reordered playlist", Toast.LENGTH_LONG).show();

                recyclerView.getAdapter().notifyItemMoved(fromPos,toPos);
                return false;
            }


            @Override //delete song
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Remove Song Confirmation").setMessage("Are you sure you want to remove that song?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                playList.remove(viewHolder.getAdapterPosition());
                                songAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                                updatePlaylist();


                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                songAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                            }
                        })
                        .show();
                updatePlaylist();
            }
        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(songAdapter);
    }


    public void updatePlaylist() {
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

    public void uploadPlaylist() {
        //check if there is already a playlist created
        try {
            FileInputStream fis = openFileInput("playList");
            ObjectInputStream ois = new ObjectInputStream(fis);
            playList = (ArrayList<Song>)ois.readObject();
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



