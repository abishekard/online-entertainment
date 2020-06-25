package com.abishek534.entertainment.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.abishek534.entertainment.R;
import com.abishek534.entertainment.audio.AudioInfo;
import com.abishek534.entertainment.audio.HomeAudioAdapter;
import com.abishek534.entertainment.image.HomeImageAdapter;
import com.abishek534.entertainment.image.ImageInfo;
import com.abishek534.entertainment.video.HomeVideoAdapter;
import com.abishek534.entertainment.video.VideoAdapter;
import com.abishek534.entertainment.video.VideoInfo;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Main extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static ArrayList<VideoInfo> homeVideoList;
    public static ArrayList<AudioInfo> homeAudioList;
    public static ArrayList<ImageInfo> homeImageList;
    private RecyclerView homeVideoRecyclerview;
    private RecyclerView homeAudioRecyclerview;
    private RecyclerView homeImageRecyclerview;
    private HomeVideoAdapter videoAdapter;
    private HomeAudioAdapter audioAdapter;
    private HomeImageAdapter imageAdapter;
    private LinearLayout homeLayout;
    private LottieAnimationView loadingFrame;

    public Main() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Main.
     */
    // TODO: Rename and change types and number of parameters
    public static Main newInstance(String param1, String param2) {
        Main fragment = new Main();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        inItUi(view);
        getVideoFromDatabase();
        getAudiosFromDatabase();
        getImagesFromDatabase();

        populateVideoList();
        populateAudioList();
        populateImageList();
        return view;
    }

    public void inItUi(View view) {
        homeVideoList = new ArrayList<>();
        homeAudioList = new ArrayList<>();
        homeImageList = new ArrayList<>();
        homeVideoRecyclerview = view.findViewById(R.id.home_video_recyclerview);
        homeAudioRecyclerview = view.findViewById(R.id.home_audio_recyclerview);
        homeImageRecyclerview = view.findViewById(R.id.home_image_recyclerview);
        homeLayout = view.findViewById(R.id.home_layout);
        loadingFrame = view.findViewById(R.id.loading_frame);


    }

    public void getVideoFromDatabase() {
        FirebaseDatabase.getInstance().getReference().child("videos").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String name = dataSnapshot.child("video_title").getValue().toString();
                String duration = dataSnapshot.child("video_duration").getValue().toString();
                String thumbUrl = dataSnapshot.child("thumb_url").getValue().toString();
                String videoUrl = dataSnapshot.child("video_url").getValue().toString();
                String videoLike = dataSnapshot.child("likes").getValue().toString();
                String videoViews = dataSnapshot.child("views").getValue().toString();
                String videoId = dataSnapshot.getKey().toString();
                String date = dataSnapshot.child("date").getValue().toString();
                date = date.substring(0, 8);


                VideoInfo videoItem = new VideoInfo(name, duration, videoUrl, date, videoLike, videoViews, thumbUrl, videoId);
                homeVideoList.add(videoItem);
                videoAdapter.notifyDataSetChanged();
                loadingFrame.setVisibility(View.GONE);
                homeLayout.setVisibility(View.VISIBLE);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAudiosFromDatabase() {
        FirebaseDatabase.getInstance().getReference().child("audios").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String name = dataSnapshot.child("audio_title").getValue().toString();
                String duration = dataSnapshot.child("audio_duration").getValue().toString();
                String thumbUrl = dataSnapshot.child("thumb_url").getValue().toString();
                String audioUrl = dataSnapshot.child("audio_url").getValue().toString();
                String audioLike = dataSnapshot.child("likes").getValue().toString();
                String audioViews = dataSnapshot.child("views").getValue().toString();
                String audioId = dataSnapshot.getKey().toString();
                String date = dataSnapshot.child("date").getValue().toString();
                date = date.substring(0, 8);


                AudioInfo audioItem = new AudioInfo(name, duration, audioUrl, date, audioLike, audioViews, thumbUrl, audioId);
                homeAudioList.add(audioItem);
                audioAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getImagesFromDatabase() {
        FirebaseDatabase.getInstance().getReference().child("wallpapers").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String name = dataSnapshot.child("image_title").getValue().toString();
                String thumbUrl = dataSnapshot.child("image_url").getValue().toString();
                String imageLike = dataSnapshot.child("likes").getValue().toString();
                String imageViews = dataSnapshot.child("views").getValue().toString();
                String imageId = dataSnapshot.getKey().toString();
                String date = dataSnapshot.child("date").getValue().toString();
                date = date.substring(0, 8);


                ImageInfo imageItem = new ImageInfo(name, imageLike, imageViews, date, imageId, thumbUrl);
                homeImageList.add(imageItem);
                imageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void populateVideoList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        homeVideoRecyclerview.setLayoutManager(linearLayoutManager);
        videoAdapter = new HomeVideoAdapter(homeVideoList, getContext());
        homeVideoRecyclerview.setAdapter(videoAdapter);
        videoAdapter.notifyDataSetChanged();
    }

    public void populateAudioList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        homeAudioRecyclerview.setLayoutManager(linearLayoutManager);
        audioAdapter = new HomeAudioAdapter(homeAudioList, getContext());
        homeAudioRecyclerview.setAdapter(audioAdapter);
        audioAdapter.notifyDataSetChanged();
    }

    public void populateImageList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        homeImageRecyclerview.setLayoutManager(linearLayoutManager);
        imageAdapter = new HomeImageAdapter(homeImageList, getContext());
        homeImageRecyclerview.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();
    }
}
