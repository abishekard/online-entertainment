package com.abishek534.entertainment.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abishek534.entertainment.R;
import com.abishek534.entertainment.audio.AudioAdapter;
import com.abishek534.entertainment.audio.AudioInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Audio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Audio extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static ArrayList<AudioInfo> audioList;
    private RecyclerView audioRecyclerview;
    private AudioAdapter adapter;
    private ProgressDialog progressDialog;

    public Audio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Audio.
     */
    // TODO: Rename and change types and number of parameters
    public static Audio newInstance(String param1, String param2) {
        Audio fragment = new Audio();
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
        View view = inflater.inflate(R.layout.fragment_audio, container, false);

        initUi(view);
        getAudiosFromDatabase();
        return view;
    }
    public void initUi(View view)
    {

        audioList = new ArrayList<>();
        audioRecyclerview = view.findViewById(R.id.audio_recyclerview);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        audioRecyclerview.setLayoutManager(linearLayoutManager);
        adapter = new AudioAdapter(audioList,getContext());
        audioRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void getAudiosFromDatabase(){
        FirebaseDatabase.getInstance().getReference().child("audios").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String name = dataSnapshot.child("audio_title").getValue().toString();
                String duration =dataSnapshot.child("audio_duration").getValue().toString();
                String thumbUrl = dataSnapshot.child("thumb_url").getValue().toString();
                String audioUrl = dataSnapshot.child("audio_url").getValue().toString();
                String audioLike =dataSnapshot.child("likes").getValue().toString();
                String audioViews = dataSnapshot.child("views").getValue().toString();
                String audioId = dataSnapshot.getKey().toString();
                String date = dataSnapshot.child("date").getValue().toString();
                date = date.substring(0,8);


                AudioInfo audioItem = new AudioInfo(name,duration,audioUrl,date,audioLike,audioViews,thumbUrl,audioId);
                audioList.add(audioItem);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
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
}
