package com.example.zingmp3fake.View;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.zingmp3fake.MainActivity;
import com.example.zingmp3fake.Model.Music;
import com.example.zingmp3fake.Model.Source;
import com.example.zingmp3fake.R;
import com.example.zingmp3fake.ViewModel.MusicAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ListMusicFragment extends Fragment {
    private RecyclerView rv;

    private ArrayList<Music> musics;
    private MusicAdapter musicAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = view.findViewById(R.id.rv_music);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        musics = new ArrayList<Music>();
        musicAdapter = new MusicAdapter(musics,getActivity());
        rv.setAdapter(musicAdapter);
        fetchAudioUrlFromFirebase();

    }
    private void fetchAudioUrlFromFirebase() {
        final FirebaseStorage storage = FirebaseStorage.getInstance("gs://music-media-23f37.appspot.com");
        StorageReference storageRef = storage.getReference().child("/audios");

        final long ONE_MEGABYTE = 1024 * 1024 * 100;

        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference fileRef :listResult.getItems()){
                    Source source = new Source();
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            source.setUrl(url);
                        }
                    });
                    fileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            try {
                                File tempMp3 = File.createTempFile("temp", ".mp3");
                                tempMp3.deleteOnExit();
                                FileOutputStream fos = new FileOutputStream(tempMp3);
                                fos.write(bytes);
                                fos.close();
                                FileInputStream fis = new FileInputStream(tempMp3);
                                source.setBytes(fis);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            musics.add(new Music(source));
                            musicAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

    }
}