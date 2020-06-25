package com.abishek534.entertainment.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abishek534.entertainment.R;
import com.abishek534.entertainment.image.ImageAdapter;
import com.abishek534.entertainment.image.ImageInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Images#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Images extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static ArrayList<ImageInfo> imageList;
    private RecyclerView imageRecyclerview;
    private ImageAdapter adapter;
    private ProgressDialog progressDialog;

    public Images() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Images.
     */
    // TODO: Rename and change types and number of parameters
    public static Images newInstance(String param1, String param2) {
        Images fragment = new Images();
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
        View view = inflater.inflate(R.layout.fragment_images, container, false);

        initUi(view);
        getImagesFromDatabase();
        return view;
    }

    public void initUi(View view)
    {

        imageList = new ArrayList<>();
        imageRecyclerview = view.findViewById(R.id.image_recyclerview);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


      //  LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
     //   linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);


        imageRecyclerview.setLayoutManager(gridLayoutManager);
        adapter = new ImageAdapter(imageList,getContext());
        imageRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void getImagesFromDatabase(){
        FirebaseDatabase.getInstance().getReference().child("wallpapers").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String name = dataSnapshot.child("image_title").getValue().toString();
                String thumbUrl = dataSnapshot.child("image_url").getValue().toString();
                String imageLike =dataSnapshot.child("likes").getValue().toString();
                String imageViews = dataSnapshot.child("views").getValue().toString();
                String imageId = dataSnapshot.getKey().toString();
                String date = dataSnapshot.child("date").getValue().toString();
                date = date.substring(0,8);


                ImageInfo imageItem = new ImageInfo(name,imageLike,imageViews,date,imageId,thumbUrl);
                imageList.add(imageItem);
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
