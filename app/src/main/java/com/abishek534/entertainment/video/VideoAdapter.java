package com.abishek534.entertainment.video;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.abishek534.entertainment.video.*;

import com.abishek534.entertainment.R;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private ArrayList<VideoInfo> videoList;
    private Context context;

    public VideoAdapter(ArrayList<VideoInfo> videoList, Context context) {
        this.videoList = videoList;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_design,parent,false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        String videoTitle = videoList.get(position).getVideoTitle();
        String videoDuration = videoList.get(position).getVideoDuration();
        String videoDate = videoList.get(position).getVideoDate();
        String videoLikes = videoList.get(position).getLikes();
        String videoViews = videoList.get(position).getViews();
        String videoThumb = videoList.get(position).getThumbUrl();
        String id = videoList.get(position).getVideoId();


        holder.setDataToUi(videoTitle,videoDuration,videoDate,videoLikes,videoViews,videoThumb,context,position,id);

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder{

        private TextView videoTitleView;
        private TextView videoViewView;
        private TextView videoLikeView;
        private ImageView thumbView;
        private TextView durationView;
        private TextView videoDate;
        private RelativeLayout mainLayout;

        public VideoViewHolder(@NonNull View v) {
            super(v);

            videoTitleView = v.findViewById(R.id.video_title);
            videoViewView = v.findViewById(R.id.video_views);
            videoLikeView = v.findViewById(R.id.video_likes);
            thumbView = v.findViewById(R.id.video_thumb);
            durationView = v.findViewById(R.id.video_duration);
            videoDate = v.findViewById(R.id.video_date);
            mainLayout = v.findViewById(R.id.main_layout);


        }

        public void setDataToUi(String title, String duration, String date, String like, final String view, String thumb,
                                final Context context, final int position, final String id)
        {
             videoTitleView.setText(title);
             videoViewView.setText(view);
             videoLikeView.setText(like);
             durationView.setText(duration);
             videoDate.setText(date);
            Picasso.get().load(thumb).into(thumbView);

            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, hybridVideoList.class);
                    intent.putExtra("position",position);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    int aa = Integer.parseInt(view)+1;
                    FirebaseDatabase.getInstance().getReference().child("videos").child(id).child("views").setValue(aa+"");
                }
            });
        }
    }
}
