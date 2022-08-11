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


public class MainActivity extends AppCompatActivity {

    ArrayList<Song> playList;

//    RecyclerView recyclerView;

//    @Override
//    protected void onPause() {
//        super.onPause();
//    }

    @Override
    public void onResume(){
        super.onResume();
//        getPlaylist();
//        updateData();

//        setContentView(R.layout.activity_main);

//        RecyclerView recyclerView = findViewById(R.id.recycler);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        recyclerView = findViewById(R.id.recycler);
////        recyclerView.setHasFixedSize(true);
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.getAdapter().notifyDataSetChanged();


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPlaylist();

        if (playList==null){
            playList = new ArrayList<>();
            playList.add(new Song("eminem1", "https://audio.jukehost.co.uk/DnvjjljYd3CMKkqDYnkFAB4u0l0MSRas",R.drawable.eminem1));
            playList.add(new Song("eminem2", "https://audio.jukehost.co.uk/vV9P99y9bpcprhImcY70NP1pd9RBk1GG",R.drawable.eminem2));
            playList.add(new Song("eminem3", "https://audio.jukehost.co.uk/iKq1cijo2874lJ1i8z0YJp8Au90c3Gzi",R.drawable.eminem3));
            playList.add(new Song("post_malone1", "https://audio.jukehost.co.uk/5OZSRlmWI3p517HlUANqQSfVn3mthQ4d",R.drawable.post_malone1));
            playList.add(new Song("post_malone__cooped_up_with_roddy_ricch", "https://audio.jukehost.co.uk/ajfudfsgSn7zseRxSXiEKJYPYTjknxLj",R.drawable.post_malone__cooped_up_with_roddy_ricch));
            playList.add(new Song("acdc__thunderstruck", "https://audio.jukehost.co.uk/iPBLTU7lohqTK2H7SQBhd98xN518eWOo",R.drawable.acdc__thunderstruck));
            playList.add(new Song("creedence_clearwater_revival__fortunate_son", "https://audio.jukehost.co.uk/oSfCKaoLb5DBKif3Krr0zPCoUiGxilAY",R.drawable.creedence_clearwater_revival__fortunate_son));
            playList.add(new Song("drake_ft_21_savage__jimmy_cooks", "https://audio.jukehost.co.uk/ZOitsiDL38Q0sAMWCsNonBkfEpjF0A43",R.drawable.drake_ft_21_savage__jimmy_cooks));
            playList.add(new Song("green_day__boulevard_of_broken_dreams", "https://audio.jukehost.co.uk/56HlQ68SbvP0hkyLyU6CsPcG1cfoEzhw",R.drawable.green_day__boulevard_of_broken_dreams));
            playList.add(new Song("kodak_black__super_gremlin", "https://audio.jukehost.co.uk/6b2VNRquXrddkgXCzDWkxf1Qlt2QcqAe",R.drawable.kodak_black__super_gremlin));
            playList.add(new Song("ram_jam_black_betty", "https://audio.jukehost.co.uk/aPoUBy6VSpsaSSEWOdHTW85ekmo7ZyVh",R.drawable.ram_jam_black_betty));
            playList.add(new Song("the_jimi_hendrix_experience__all_along_the_watcht", "https://audio.jukehost.co.uk/XxqvS0i5C5Cj6UT46XgGOoJ92RBsJfis",R.drawable.the_jimi_hendrix_experience__all_along_the_watcht));
            playList.add(new Song("nickelback_how_you_remind_me", "https://audio.jukehost.co.uk/wO5qpQjxUV16MM4rgjDxxaX7ZxIoReQB",R.drawable.nickelback_how_you_remind_me));



            playList.add(new Song("bob1", "http://www.syntax.org.il/xtra/bob.m4a",R.drawable.bob1));
            playList.add(new Song("bob2", "http://www.syntax.org.il/xtra/bob1.m4a",R.drawable.bob2));
            playList.add(new Song("bob3", "https://www.syntax.org.il/xtra/bob2.mp3",R.drawable.bob3));
            setPlaylist();
        }

        final SongAdapter songAdapter = new SongAdapter(playList);

        Button addSong = findViewById(R.id.addSongButton);
        addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddSongActivity.class);
                finish();
                startActivity(intent);
                songAdapter.notifyDataSetChanged();


            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        final SongAdapter songAdapter = new SongAdapter(playList);
        songAdapter.setListener(new SongAdapter.ISongListener() {

            @Override
            public void onInfoClicked(int position, View view) {
                Intent intent = new Intent(MainActivity.this, SongInfo.class);
                intent.putExtra("path",playList.get(position).getPhotoPath());
                intent.putExtra("drawableUri",playList.get(position).getName());
                intent.putExtra("name",playList.get(position).getName());
                intent.putExtra("link",playList.get(position).getLink());
                startActivity(intent);
            }

            @Override
            public void onSongClicked(int position, View view) {
                Intent intent = new Intent(MainActivity.this,MusicServiceNew.class);
                intent.putExtra("command","new_instance");
                intent.putExtra("current",position);

                stopService(intent);
                startService(intent);
            }

            @Override
            public void onSongLongClicked(int position, View view) {}
        });

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

            @Override // reorder songs
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPos = viewHolder.getAdapterPosition();
                int toPos = target.getAdapterPosition();
                Collections.swap(playList, fromPos, toPos);
                setPlaylist();
                recyclerView.getAdapter().notifyItemMoved(fromPos,toPos);
                return true;
            }


            @Override //delete song
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.remove_song_confirmation).setMessage(R.string.r_u_sure)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                playList.remove(viewHolder.getAdapterPosition());
                                setPlaylist();
                                songAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                songAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//                                songAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                songAdapter.notifyDataSetChanged();



                            }

                        })
                        .show();

            }
        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(songAdapter);
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

    private void updateData(){
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        recyclerView = findViewById(R.id.recycler);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getPlaylist();
        final SongAdapter songAdapter = new SongAdapter(playList);
        songAdapter.setListener(new SongAdapter.ISongListener() {

            @Override
            public void onInfoClicked(int position, View view) {
                Intent intent = new Intent(MainActivity.this, SongInfo.class);
                intent.putExtra("path",playList.get(position).getPhotoPath());
                intent.putExtra("drawableUri",playList.get(position).getName());
                intent.putExtra("name",playList.get(position).getName());
                intent.putExtra("link",playList.get(position).getLink());
                startActivity(intent);
            }

            @Override
            public void onSongClicked(int position, View view) {
                Intent intent = new Intent(MainActivity.this,MusicServiceNew.class);
                intent.putExtra("command","new_instance");
                intent.putExtra("current",position);

                stopService(intent);
                startService(intent);
            }

            @Override
            public void onSongLongClicked(int position, View view) {}
        });

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

            @Override // reorder songs
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPos = viewHolder.getAdapterPosition();
                int toPos = target.getAdapterPosition();
                Collections.swap(playList, fromPos, toPos);
                setPlaylist();
                recyclerView.getAdapter().notifyItemMoved(fromPos,toPos);
                return true;
            }


            @Override //delete song
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Remove Song Confirmation").setMessage("Are you sure you want to remove that song?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                playList.remove(viewHolder.getAdapterPosition());
                                setPlaylist();
                                songAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                songAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//                                songAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                songAdapter.notifyDataSetChanged();

                            }

                        })
                        .show();

            }
        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(songAdapter);
    }
}



