package com.abishek534.entertainment.image;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.abishek534.entertainment.R;
import com.abishek534.entertainment.fragment.Images;
import com.abishek534.entertainment.fragment.Main;
import com.abishek534.entertainment.main.Home;

import java.util.ArrayList;

public class DisplayImage extends AppCompatActivity {

    private ViewPager viewPager;
    private ArrayList<ImageInfo> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        imageList = Main.homeImageList;
        viewPager = findViewById(R.id.viewpager);

        int imagePosition = getIntent().getIntExtra("position",0);

        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(imageList,DisplayImage.this);
        viewPager.setAdapter(imagePagerAdapter);
        viewPager.setPageMargin(20);
        viewPager.setCurrentItem(imagePosition);


    }
}