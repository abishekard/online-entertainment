package com.abishek534.entertainment.video;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abishek534.entertainment.R;
import com.abishek534.entertainment.fragment.Main;
import com.abishek534.entertainment.setting.bottomCardSetting;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;

import java.util.concurrent.TimeUnit;

public class hybridVideoList extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnVideoSizeChangedListener, bottomCardSetting.BottomSheetListener,
        View.OnTouchListener, AudioManager.OnAudioFocusChangeListener, HybridVideoListClickPlay {

    public static final String Broadcast_PLAY_NEW_AUDIO = "com.abishek534.musicPlayer";

    private ImageButton hbtnPlay;
    private ImageButton hbtnPause;
    private ImageButton hbtnNext;
    private ImageButton hbtnPrev;
    private ConstraintLayout htxtFwd;
    private ConstraintLayout htxtBkd;
    private SeekBar hSeekBar;
    private ImageButton fullScreen;
    private String videoPath;
    private LinearLayout root;
    private MediaPlayer hPlayer;
    private TextView currentDuration;
    private TextView fullDuration;
    private Handler mainHandler;
    private SurfaceView hVideoSurface;
    private SurfaceHolder surfaceHolder;
    private ProgressBar hLoading;
    private RelativeLayout showHide;
    private LinearLayout controls;
    private boolean fScreen;
    private Guideline guideline;
    private LinearLayout list;
    private ImageButton halfScreen;
    private boolean isLandscape = true;
    private LinearLayout seekbarHolder;
    private RecyclerView hRecyclerView;
    private int videoPosition;
    private ImageButton disabledPrev;
    private ImageButton disabledNext;
    private SharedPreferences prefs;
    private int in;
    private int mvduration;
    private ConstraintLayout parent;
    private int height;
    private int width;
    private boolean isPortrait;
    private ImageButton btnBack;
    private ImageButton dotMenu;
    private TextView videoTopName;
    private SharedPreferences sets;
    private String loopState;
    private boolean looping;
    private boolean back;
    private ArrayList<VideoInfo> videoList;
    private VideoAdapter adapter;
    private LinearLayout rightScreen, leftScreen;
    private boolean singleClick, doubleClick;
    private LinearLayout volumeContainer, brightnessContainer;
    private ProgressBar volumeBar, brightnessBar;
    private boolean backgroundPlay;
    private TextView bottomVideoTitle;
    private TextView videoViews;
    private TextView videoLikes;
    private ImageButton videoLiked;
    // private static final String PREF_IS_LANDSCAPE = "is_landscape";


    private Runnable updatePlayer; //updates player time
    private boolean wasPlaying;

    {
        updatePlayer = new Runnable() {
            @Override
            public void run() {

                String toDurr = String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(hPlayer.getDuration()),
                        TimeUnit.MILLISECONDS.toMinutes(hPlayer.getDuration()) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hPlayer.getDuration())),
                        TimeUnit.MILLISECONDS.toSeconds(hPlayer.getDuration()) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(hPlayer.getDuration()))
                );
                String currDur = String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(hPlayer.getCurrentPosition()),
                        TimeUnit.MILLISECONDS.toMinutes(hPlayer.getCurrentPosition()) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hPlayer.getCurrentPosition())),
                        TimeUnit.MILLISECONDS.toSeconds(hPlayer.getCurrentPosition()) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(hPlayer.getCurrentPosition()))
                );
                fullDuration.setText(toDurr);
                currentDuration.setText(currDur);
                hSeekBar.setMax(hPlayer.getDuration());
                hSeekBar.setProgress(hPlayer.getCurrentPosition());
                mainHandler.postDelayed(updatePlayer, 200);    // calling update player after every 200 milli seconds

            }
        };
    }

    private final Runnable hideControls;   // runnable to hide play controls

    {
        hideControls = new Runnable() {

            @Override
            public void run() {
                hideAllControlls();
            }
        };
    }


    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hybrid_video_list);



        hbtnPlay = findViewById(R.id.h_btn_play);
        hbtnPause = findViewById(R.id.h_btn_pause);
        hbtnNext = findViewById(R.id.h_btn_next);
        hbtnPrev = findViewById(R.id.h_btn_prev);
        htxtFwd = findViewById(R.id.h_txt_fwd);
        htxtBkd = findViewById(R.id.h_txt_bkd);
        hSeekBar = findViewById(R.id.h_seek_bar);
        fullScreen = findViewById(R.id.h_full_screen);
        currentDuration = findViewById(R.id.h_current_time);
        fullDuration = findViewById(R.id.h_full_duration);
        hLoading = findViewById(R.id.hLoading);
        root = findViewById(R.id.root);
        hVideoSurface = findViewById(R.id.videoSurface);
        controls = findViewById(R.id.controls);
        showHide = findViewById(R.id.show_hide);
        videoPath = getIntent().getStringExtra("path");
        videoPosition = getIntent().getIntExtra("position", 0);
        disabledNext = findViewById(R.id.h_btn_disabled_next);
        disabledPrev = findViewById(R.id.h_btn_disabled_prev);
        parent = findViewById(R.id.parent);
        dotMenu = findViewById(R.id.hybrid_dot_menu);
        btnBack = findViewById(R.id.hybrid_back_btn);
        videoTopName = findViewById(R.id.hybrid_video_name_top);
        rightScreen = findViewById(R.id.hRight);
        leftScreen = findViewById(R.id.hLeft);
        volumeContainer = findViewById(R.id.h_volume_slider_container);
        brightnessContainer = findViewById(R.id.h_brightness_slider_container);
        volumeBar = findViewById(R.id.h_volume_bar);
        brightnessBar = findViewById(R.id.h_brightness_bar);
        bottomVideoTitle = findViewById(R.id.hybrid_video_title);
        videoLikes = findViewById(R.id.hybrid_video_likes);
        videoViews =findViewById(R.id.hybrid_video_views);
        videoLiked = findViewById(R.id.hybrid_video_liked);


        leftScreen.setOnTouchListener(this);
        rightScreen.setOnTouchListener(this);
        mainHandler = new Handler();
        list = findViewById(R.id.list);
        guideline = findViewById(R.id.guideline);
        halfScreen = findViewById(R.id.h_half_screen);
        hRecyclerView = findViewById(R.id.hybrid_recycler_view);
        fScreen = false;
        seekbarHolder = findViewById(R.id.seek_bar_holder);




        isLandscape = checkIfLandscape();
        executeSettingAtStartup();
        showHide.setOnTouchListener(this);


        btnBack.setOnClickListener(this);
        dotMenu.setOnClickListener(this);
        hbtnPlay.setOnClickListener(this);
        hbtnPause.setOnClickListener(this);
        hbtnNext.setOnClickListener(this);
        hbtnPrev.setOnClickListener(this);
        fullScreen.setOnClickListener(this);
        halfScreen.setOnClickListener(this);
        hSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                hPlayer.seekTo(hSeekBar.getProgress());
            }
        });


        in = 0;


        root = findViewById(R.id.root);
        showAllControls();
        hLoading.setVisibility(View.VISIBLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        surfaceHolder = hVideoSurface.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        hRecyclerView.setOnClickListener(this);


        Handler handler = new Handler();



            videoList = Main.homeVideoList;

            populatVideoList();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Play(videoList.get(videoPosition).getVideoUrl());
                }
            }, 2000);




        sets = PreferenceManager.getDefaultSharedPreferences(this);
        loopState = sets.getString("loopSetting", "one");
        back = false;

        backgroundPlay = sets.getBoolean("backPlay", true);
        wasPlaying = true;



    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (!looping && videoPosition >= 0) {
            next();
        } else {
            back = true;
            mainHandler.removeCallbacks(updatePlayer);
            mp.release();
            finish();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch(what){
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                hLoading.setVisibility(View.VISIBLE);
                controls.setVisibility(View.GONE);
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                hLoading.setVisibility(View.GONE);
                controls.setVisibility(View.VISIBLE);
                break;

        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {


        if (hLoading.getVisibility() == View.VISIBLE) {
            hLoading.setVisibility(View.GONE);
            controls.setVisibility(View.VISIBLE);
        }

        hPlayer.start();
        mainHandler.postDelayed(updatePlayer, 100);
        if (in != 0) {
            if (!backgroundPlay) {
                hPlayer.pause();
            } else {
                if (wasPlaying) {
                    hbtnPlay.setVisibility(View.GONE);
                    hbtnPause.setVisibility(View.VISIBLE);
                } else {
                    pause();
                }
            }
            hPlayer.seekTo(mvduration);
            in = 0;
        }

        if (videoPosition == 0) {
            //hide prev
            hbtnPrev.setVisibility(View.GONE);
            disabledPrev.setVisibility(View.VISIBLE);
        }
        if (videoPosition == videoList.size() - 1) {
            //hide next
            hbtnNext.setVisibility(View.GONE);
            disabledNext.setVisibility(View.VISIBLE);
        }


        if (isPortrait && isLandscape) {
            portraitFullScreenMode();
            toggleOrientation();
        } else {
            if (isPortrait) {
                portraitHalfScreenMode();
                showSystemUI();
            } else {
                if (!isLandscape) {
                    portraitHalfScreenMode();
                    showSystemUI();
                    guideline.setGuidelinePercent(0.35f);
                }
            }

        }

    }


    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        isPortrait = height > width;

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onClick(View v) {
        if(hPlayer !=null) {
            switch (v.getId()) {
                case R.id.h_btn_play:
                    play();
                    break;
                case R.id.h_btn_pause:
                    pause();
                    break;
                case R.id.h_btn_next:
                    next();
                    break;
                case R.id.h_btn_prev:
                    prev();
                    break;

                case R.id.h_full_screen:
                    if (isPortrait) {
                        portraitFullScreenMode();
                        hideSystemUI();
                    } else {
                        fullScreenMode();
                        hideSystemUI();
                    }


                    break;
                case R.id.h_half_screen:

                    if (isPortrait) {
                        portraitHalfScreenMode();
                        showSystemUI();
                    } else {
                        halfScreenMode();
                        showSystemUI();
                    }

                    break;
                case R.id.hybrid_back_btn:
                    backArrowButton();
                    break;
                case R.id.hybrid_dot_menu:
                    bottomCardMeu();
                    break;


            }
        }

    }

    public void Play(String vPath) {
        try {

            setCurrentVideoInfoToUi(vPath);
            hPlayer = new MediaPlayer();

            hPlayer.setOnCompletionListener(this);
            hPlayer.setOnErrorListener(this);
            hPlayer.setOnInfoListener(this);
            hPlayer.setOnPreparedListener(this);
            hPlayer.setOnSeekCompleteListener(this);
            hPlayer.setOnVideoSizeChangedListener(this);


            hPlayer.setDataSource(vPath);
            hPlayer.setDisplay(surfaceHolder);
            hPlayer.prepareAsync();
            hPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            if (loopState.equals("one"))
                hPlayer.setLooping(true);


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void play() {


        hPlayer.start();
        hbtnPlay.setVisibility(View.GONE);
        hbtnPause.setVisibility(View.VISIBLE);


    }

    public void pause() {
        hPlayer.pause();
        hbtnPause.setVisibility(View.GONE);
        hbtnPlay.setVisibility(View.VISIBLE);

    }

    private void hideAllControlls() {

        if (root.getVisibility() == View.VISIBLE) {
            root.setVisibility(View.GONE);
        }
        if (isLandscape) {
            hideSystemUI();
        }


    }

    private void showAllControls() {


        if (root.getVisibility() == View.GONE) {
            root.setVisibility(View.VISIBLE);
        }


        mainHandler.removeCallbacks(hideControls);
        mainHandler.postDelayed(hideControls, 3000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back = true;
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);

        if (hPlayer != null) {
            mainHandler.removeCallbacks(updatePlayer);
            hPlayer.release();
        }
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void fullScreenMode() {
        list.setVisibility(View.GONE);
        guideline.setGuidelinePercent(1);
        fullScreen.setVisibility(View.GONE);
        halfScreen.setVisibility(View.VISIBLE);
        changeSeekBarPosition(30, 50, 30, 30);
        toggleOrientation();
        statusBarInfullScreen();


    }

    private void halfScreenMode() {
        list.setVisibility(View.VISIBLE);

        halfScreen.setVisibility(View.GONE);
        fullScreen.setVisibility(View.VISIBLE);
        changeSeekBarPosition(0, 0, 0, 0);
        statusBarInnHalfscreen();
        guideline.setGuidelinePercent(0.35f);
        toggleOrientation();
        // parent.setFitsSystemWindows(true);

    }

    private boolean checkIfLandscape() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels < displayMetrics.widthPixels;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void toggleOrientation() {
        if (isLandscape) {
            isLandscape = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            isLandscape = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }

    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }

    private void changeSeekBarPosition(int left, int bottom, int right, int top) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) seekbarHolder.getLayoutParams();
        //  LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(left, top, right, bottom);
        seekbarHolder.setLayoutParams(params);
    }

    public void populatVideoList() {
        if (videoList.size() > 0) {
            linearLayoutManager = new LinearLayoutManager(hybridVideoList.this);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            // linearLayoutManager.setReverseLayout(true);
            // linearLayoutManager.setStackFromEnd(true);
            adapter = new VideoAdapter(videoList,hybridVideoList.this);
            hRecyclerView.setLayoutManager(linearLayoutManager);
            hRecyclerView.setAdapter(adapter);
            hRecyclerView.setItemAnimator(null);
            adapter.notifyDataSetChanged();
            //  hRecyclerView.setNestedScrollingEnabled(false);
            hRecyclerView.setHasFixedSize(true);

        } else {
            Toast.makeText(hybridVideoList.this, "no video", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_HEADSETHOOK) {
            if (hPlayer.isPlaying())
                pause();
            else
                play();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void statusBarInfullScreen() {
        Window window = hybridVideoList.this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(hybridVideoList.this.getResources().getColor(R.color.transparent));
            window.setNavigationBarColor(hybridVideoList.this.getResources().getColor(R.color.transparent));

        }

    }

    public void statusBarInnHalfscreen() {
        Window window = hybridVideoList.this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(hybridVideoList.this.getResources().getColor(R.color.black));
            window.setNavigationBarColor(hybridVideoList.this.getResources().getColor(R.color.black));
        }

    }

    public void next() {

        if (videoPosition < videoList.size() - 1) {

            hLoading.setVisibility(View.VISIBLE);
            controls.setVisibility(View.GONE);
            videoPosition = videoPosition + 1;
          //  PublicUse.hCurrentTrackIndex = videoPosition;

            mainHandler.removeCallbacks(updatePlayer);
            hPlayer.release();
            Play(videoList.get(videoPosition).getVideoUrl());

            setCurrentVideoInfoToUi(videoList.get(videoPosition).getVideoUrl());

            if ((videoPosition == videoList.size() - 1)) {
                //disable next
                hbtnNext.setVisibility(View.GONE);
                disabledNext.setVisibility(View.VISIBLE);
            }
            if (videoPosition > 0) {
                //enable prev
                hbtnPrev.setVisibility(View.VISIBLE);
                disabledPrev.setVisibility(View.GONE);

            }

            if (hbtnPlay.getVisibility() == View.VISIBLE) {
                hbtnPlay.setVisibility(View.GONE);
                hbtnPause.setVisibility(View.VISIBLE);
            }


            prefs = getSharedPreferences("lastPlayed", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("lastPlayed", videoList.get(videoPosition).getVideoUrl());
            editor.apply();

        } else {
            mainHandler.removeCallbacks(updatePlayer);
            hPlayer.release();
            back = true;
            finish();
        }

    }

    public void prev() {

        if (videoPosition > 0) {


            hLoading.setVisibility(View.VISIBLE);
            controls.setVisibility(View.GONE);
            videoPosition = videoPosition - 1;
         //   PublicUse.hCurrentTrackIndex = videoPosition;
            mainHandler.removeCallbacks(updatePlayer);
            hPlayer.release();
            Play(videoList.get(videoPosition).getVideoUrl());

            setCurrentVideoInfoToUi(videoList.get(videoPosition).getVideoUrl());


            if (videoPosition == 0) {
                //disable prev
                hbtnPrev.setVisibility(View.GONE);
                disabledPrev.setVisibility(View.VISIBLE);
            }
            if (videoPosition < videoList.size() - 1) {
                //enable next
                hbtnNext.setVisibility(View.VISIBLE);
                disabledNext.setVisibility(View.GONE);
            }

            if (hbtnPlay.getVisibility() == View.VISIBLE) {
                hbtnPlay.setVisibility(View.GONE);
                hbtnPause.setVisibility(View.VISIBLE);
            }

            prefs = getSharedPreferences("lastPlayed", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("lastPlayed", videoList.get(videoPosition).getVideoUrl());
            editor.apply();
        }

    }

    public void setCurrentVideoInfoToUi(String nextVideoPath) {
     //   File file = new File(nextVideoPath);
        videoTopName.setText(videoList.get(videoPosition).getVideoTitle());
        bottomVideoTitle.setText(videoList.get(videoPosition).getVideoTitle());
        videoViews.setText(videoList.get(videoPosition).getViews());
        videoLikes.setText(videoList.get(videoPosition).getLikes());
        videoLiked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lke = Integer.parseInt(videoList.get(videoPosition).getLikes())+1;
                FirebaseDatabase.getInstance().getReference().child("videos").child(videoList.get(videoPosition).getVideoId()).
                        child("likes").setValue(lke+"");
                Toast.makeText(hybridVideoList.this,"you liked this video",Toast.LENGTH_SHORT).show();
            }
        });

        videoPath = nextVideoPath;

    }


    @Override
    protected void onPause() {
        super.onPause();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        if (!back && hPlayer != null) {
            wasPlaying = hPlayer.isPlaying();
        //    PublicUse.hCurrentTrackIndex = videoPosition;
        //    PublicUse.hCurrentTrackTime = hPlayer.getCurrentPosition();
            editor.putInt("videoSeekTime", hPlayer.getCurrentPosition());
            editor.putInt("videoPosition", videoPosition);
            editor.apply();
            pause();
            mainHandler.removeCallbacks(updatePlayer);
            hPlayer.release();
            hPlayer=null;
            in = 1;
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);

        } else {
            editor.putInt("videoSeekTime", 0);
            editor.putInt("videoPosition", videoPosition);
            editor.apply();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (in != 0) {
            prefs = PreferenceManager.getDefaultSharedPreferences(this);  // retrieving video position and video seek time to play again
            mvduration = prefs.getInt("videoSeekTime", 0) + 3000;   // from same position after resuming activity
            int vPosition = prefs.getInt("videoPosition", 0);
            final String mPath = videoList.get(vPosition).getVideoUrl();
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Play(mPath);
                    controls.setVisibility(View.GONE);
                    hLoading.setVisibility(View.VISIBLE);
                }
            }, 200);


        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    public void portraitFullScreenMode() {

        list.setVisibility(View.GONE);
        guideline.setGuidelinePercent(1);
        fullScreen.setVisibility(View.GONE);
        halfScreen.setVisibility(View.VISIBLE);
        changeSeekBarPosition(30, 50, 30, 30);
        // parent.setFitsSystemWindows(false);
        statusBarInfullScreen();
    }

    public void portraitHalfScreenMode() {

        list.setVisibility(View.VISIBLE);

        halfScreen.setVisibility(View.GONE);
        fullScreen.setVisibility(View.VISIBLE);
        changeSeekBarPosition(0, 0, 0, 0);
        statusBarInnHalfscreen();
        guideline.setGuidelinePercent(0.6f);

    }

    public void backArrowButton() {
        if (isLandscape) {
            halfScreenMode();
            showSystemUI();
        } else {
            finish();
        }
    }

    public void bottomCardMeu() {
        bottomCardSetting cardSetting = new bottomCardSetting(videoPath);
        cardSetting.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.setting_dialog);
        cardSetting.show(getSupportFragmentManager(), "BottomCardSetting");
    }

    @Override
    public void loopStateChanged(String mode) {
        if (mode.equals("one")) {
            hPlayer.setLooping(true);
            looping = true;

        } else {
            hPlayer.setLooping(false);
            looping = false;

        }

    }

    void executeSettingAtStartup() {
        SharedPreferences sets;
        sets = PreferenceManager.getDefaultSharedPreferences(hybridVideoList.this);
        String videoRepeat = sets.getString("loopSetting", "one");
        assert videoRepeat != null;
        looping = videoRepeat.equals("one");

    }

    private int baseX, baseY;
    int diffX, diffY;
    int brightness;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                baseX = (int) event.getX();
                baseY = (int) event.getY();
                diffX = 0;
                diffY = 0;

                break;
            case MotionEvent.ACTION_MOVE:
                diffX = (int) event.getX() - baseX;
                diffY = (int) event.getY() - baseY;

                if (Math.abs(diffY) > 60 && isLandscape) {


                    switch (v.getId()) {
                        case R.id.hRight:
                            AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                            int mediaVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                            int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                            double cal = (double) diffY * 0.045;
                            int newMediaVolume = mediaVolume - (int) cal;
                            if (newMediaVolume > maxVol)
                                newMediaVolume = maxVol;
                            else if (newMediaVolume < 1)
                                newMediaVolume = 0;
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newMediaVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                            double volume = Math.ceil(((double) newMediaVolume / (double) maxVol) * (double) 10);
                            volumeContainer.setVisibility(View.VISIBLE);
                            volumeBar.setProgress((int) volume);
                            break;
                        case R.id.hLeft:

                            ContentResolver cResolver = getContentResolver();
                            Window window = getWindow();
                            try {
                                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                                brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                            int newBrightness = (int) (brightness - (diffY * 0.6));
                            if (newBrightness > 250) {
                                newBrightness = 250;
                            } else if (newBrightness < 1) {
                                newBrightness = 1;
                            }
                            double brightPerc = Math.ceil((((double) newBrightness / (double) 250)) * (double) 10);
                            brightnessContainer.setVisibility(View.VISIBLE);
                            brightnessBar.setProgress((int) brightPerc);
                            Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, (newBrightness));

                            WindowManager.LayoutParams layoutParams = window.getAttributes();
                            layoutParams.screenBrightness = brightness / (float) 255;
                            window.setAttributes(layoutParams);
                            break;
                    }

                    baseY = baseY + diffY;
                    diffY = 0;
                }


                break;
            case MotionEvent.ACTION_UP:
                switch (v.getId()) {
                    case R.id.hLeft:
                        if (singleClick) {
                            doubleClick = true;
                            hPlayer.seekTo(hPlayer.getCurrentPosition() - 10000);
                            htxtBkd.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    htxtBkd.setVisibility(View.GONE);
                                }
                            }, 300);
                        }
                        break;
                    case R.id.hRight:
                        if (singleClick) {
                            doubleClick = true;
                            hPlayer.seekTo(hPlayer.getCurrentPosition() + 10000);
                            htxtFwd.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    htxtFwd.setVisibility(View.GONE);
                                }
                            }, 300);
                        }
                        break;
                }
                if (!singleClick)
                    doubleTapDetector();


                brightnessContainer.setVisibility(View.GONE);
                volumeContainer.setVisibility(View.GONE);


                break;

        }

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                // showAllControls();
                break;

        }

        return super.onTouchEvent(event);
    }

    public void doubleTapDetector() {
        singleClick = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                singleClick = false;
                if (!doubleClick) {
                    showAllControls();

                }
                doubleClick = false;


            }
        }, 250);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange <= 0) {
            //LOSS -> PAUSE
            pause();
        } else {
            //GAIN -> PLAY
            play();
        }
    }



    @Override
    public void videoClickListener(int position) {

        if(hPlayer != null) {
            videoPosition = position;
            mainHandler.removeCallbacks(updatePlayer);
            if (!hPlayer.isPlaying()) {
                hbtnPlay.setVisibility(View.INVISIBLE);
                hbtnPause.setVisibility(View.VISIBLE);
            }

            hPlayer.release();

            Play(videoList.get(position).getVideoUrl());
        }
    }


}
