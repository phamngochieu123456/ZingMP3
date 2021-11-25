package com.example.zingmp3fake.ViewModel;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zingmp3fake.Model.Music;
import com.example.zingmp3fake.R;

import java.io.IOException;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private ArrayList<Music> musics;
    private Activity context;

    public MusicAdapter(ArrayList<Music> musics, Activity activity) {
        this.musics = musics;
        this.context = activity;
    }

    @NonNull
    @Override
    public MusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_music, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.ViewHolder holder, int position) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(musics.get(position).getSource().getBytes().getFD());
            holder.tvMusicName.setText( mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
            holder.tvAuthorName.setText( mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            byte[] artBytes =  mmr.getEmbeddedPicture();
            if(artBytes!=null)
            {
                Bitmap bm = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.length);
                holder.ivMusic.setImageBitmap(bm);
            }
            else
            {
                holder.ivMusic.setImageResource(R.drawable.vinyl_record);
            }

            holder.tvMusicName.setOnClickListener(new View.OnClickListener() {
                Music music = musics.get(position);

                @Override
                public void onClick(View v) {
                    NavOptions.Builder navBuilder =  new NavOptions.Builder();
                    navBuilder.setEnterAnim(R.anim.fadeout).setExitAnim(R.anim.fadein).setPopEnterAnim(R.anim.fadeout).setPopExitAnim(R.anim.fadein);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Musics",musics);
                    bundle.putSerializable("Position",position);
                    Navigation.findNavController(v).navigate(R.id.playFragment,bundle,navBuilder.build());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return musics.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivMusic;
        public TextView tvMusicName;
        public TextView tvAuthorName;

        public ViewHolder(View view) {
            super(view);

            ivMusic = view.findViewById(R.id.iv_music);
            tvMusicName = view.findViewById(R.id.tv_music_name);
            tvAuthorName = view.findViewById(R.id.tv_author_name);
        }
    }

}