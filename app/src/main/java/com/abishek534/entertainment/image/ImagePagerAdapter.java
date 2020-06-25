package com.abishek534.entertainment.image;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.abishek534.entertainment.R;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImagePagerAdapter extends PagerAdapter {

    private ArrayList<ImageInfo> imageList;
    private Context context;

    public ImagePagerAdapter(ArrayList<ImageInfo> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {


        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_pager_item,container,false);
        ImageView imageView = view.findViewById(R.id.image);
        Picasso.get().load(imageList.get(position).getImageUrl()).into(imageView);
        TextView views = view.findViewById(R.id.image_pager_views);
        TextView likes = view.findViewById(R.id.image_pager_likes);
        final ImageButton imageLiked =view.findViewById(R.id.image_liked);
        views.setText(imageList.get(position).getViews());
        likes.setText(imageList.get(position).getLike());
        imageLiked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lke = Integer.parseInt(imageList.get(position).getLike())+1;
                FirebaseDatabase.getInstance().getReference().child("wallpapers").child(imageList.get(position)
                        .getImageId()).child("likes").setValue(lke+"");
                Toast.makeText(context,"you liked the image",Toast.LENGTH_SHORT).show();
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
