package com.example.musicplayer;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created by erankatsav on 19/03/2018.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<Song> songs;
    private ISongListener listener;

    interface ISongListener {
        void onInfoClicked(int position, View view);
        void onSongClicked(int position, View view);
        void onSongLongClicked(int position,View view);
    }

    public void setListener(ISongListener listener) {
        this.listener = listener;
    }

    public SongAdapter(List<Song> songs) {
        this.songs = songs;
    }


    public class SongViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        TextView linkTv;
        ImageView picIv;
        ImageButton infoIB;

        public SongViewHolder(View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.song_name);
            picIv = itemView.findViewById(R.id.album_image);

            infoIB = itemView.findViewById(R.id.songInfo);
            infoIB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null)
                        listener.onInfoClicked(getAdapterPosition(),view);
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(listener!=null)
                        listener.onSongClicked(getAdapterPosition(),view);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(listener!=null)
                        listener.onSongLongClicked(getAdapterPosition(),view);
                    return false;
                }
            });
        }
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_cell,parent,false);
        SongViewHolder songViewHolder = new SongViewHolder(view);
        return songViewHolder;
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.nameTv.setText(song.getName());

        if(song.getPhotoPath()!= null)
            holder.picIv.setImageBitmap(BitmapFactory.decodeFile(song.getPhotoPath()));
        else
             holder.picIv.setImageResource(song.getPhotoPathInt());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


}




