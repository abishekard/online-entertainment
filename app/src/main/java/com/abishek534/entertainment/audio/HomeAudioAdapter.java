package com.abishek534.entertainment.audio;

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

import com.abishek534.entertainment.R;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class HomeAudioAdapter extends RecyclerView.Adapter<HomeAudioAdapter.AudioViewHolder> {
    private ArrayList<AudioInfo> audioList;
    private Context context;

    public HomeAudioAdapter(ArrayList<AudioInfo> videoList, Context context) {
        this.audioList = videoList;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeAudioAdapter.AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_audio_list_design,parent,false);
        return new HomeAudioAdapter.AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAudioAdapter.AudioViewHolder holder, int position) {
        String audioTitle = audioList.get(position).getAudioTitle();
        String audioDuration = audioList.get(position).getAudioDuration();
        String audioDate = audioList.get(position).getAudioDate();
        String audioLikes = audioList.get(position).getLikes();
        String audioViews = audioList.get(position).getViews();
        String audioThumb = audioList.get(position).getThumbUrl();
        String id = audioList.get(position).getAudioId();

        holder.setDataToUi(audioTitle,audioDuration,audioDate,audioLikes,audioViews,audioThumb,context,position,id);

    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    public static class AudioViewHolder extends RecyclerView.ViewHolder{

        private TextView audioTitleView;
        private TextView audioViewView;
        private TextView audioLikeView;
        private ImageView thumbView;
        private TextView durationView;
        private RelativeLayout mainLayout;

        public AudioViewHolder(@NonNull View v) {
            super(v);

            audioTitleView = v.findViewById(R.id.audio_title);
            audioViewView = v.findViewById(R.id.audio_views);
            audioLikeView = v.findViewById(R.id.audio_likes);
            thumbView = v.findViewById(R.id.audio_thumb);
            durationView = v.findViewById(R.id.audio_duration);
            mainLayout = v.findViewById(R.id.main_layout);
        }

        public void setDataToUi(String title, String duration, String date, String like,final String view, String thumb
        , final Context context, final int position,final String id)
        {
            audioTitleView.setText(title);
            audioViewView.setText(view);
            audioLikeView.setText(like);
            durationView.setText(duration);
            Picasso.get().load(thumb).into(thumbView);
            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,hybridAudioList.class);
                    intent.putExtra("position",position);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);

                    int aa = Integer.parseInt(view)+1;
                    FirebaseDatabase.getInstance().getReference().child("audios").child(id).child("views").setValue(aa+"");
                }
            });
        }
    }
}
