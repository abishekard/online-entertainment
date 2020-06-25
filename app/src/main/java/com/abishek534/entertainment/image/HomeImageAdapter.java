package com.abishek534.entertainment.image;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abishek534.entertainment.R;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeImageAdapter extends RecyclerView.Adapter<HomeImageAdapter.ImageViewHolder> {
    private ArrayList<ImageInfo> imageList;
   private Context context;

    public HomeImageAdapter(ArrayList<ImageInfo> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_image_list_design,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageTitle = imageList.get(position).getImageTitle();
        String imageDate = imageList.get(position).getDate();
        String imageLikes = imageList.get(position).getLike();
        String imageViews = imageList.get(position).getViews();
        String imageThumb = imageList.get(position).getImageUrl();
        String id = imageList.get(position).getImageId();

        holder.setDataToUi(imageTitle,imageDate,imageLikes,imageViews,imageThumb,id,position);

    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        private TextView imageTitleView;
        private TextView imageViewView;
        private TextView imageLikeView;
        private ImageView imageView;


        public ImageViewHolder(@NonNull View v) {
            super(v);
            imageTitleView = v.findViewById(R.id.image_title);
            imageLikeView = v.findViewById(R.id.image_likes);
            imageView = v.findViewById(R.id.image_thumb);

            imageViewView = v.findViewById(R.id.image_views);
        }

        public void setDataToUi(String title, String date, String like, final String view, String thumb, final String id, final int position)
        {
            imageTitleView.setText(title);
            imageViewView.setText(view);
            imageLikeView.setText(like);
            Picasso.get().load(thumb).into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int vew = Integer.parseInt(view)+1;
                    FirebaseDatabase.getInstance().getReference().child("wallpapers").child(id).child("views").setValue(vew+"");
                    Intent intent = new Intent(context,DisplayImage.class);
                    intent.putExtra("position",position);
                    context.startActivity(intent);
                }
            });
        }
    }
}
