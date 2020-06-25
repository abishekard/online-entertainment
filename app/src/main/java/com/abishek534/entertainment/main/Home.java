package com.abishek534.entertainment.main;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.abishek534.entertainment.MainActivity;
import com.abishek534.entertainment.R;
import com.abishek534.entertainment.customDialog.permissionDialog;
import com.abishek534.entertainment.fragment.AboutUs;
import com.abishek534.entertainment.fragment.Audio;
import com.abishek534.entertainment.fragment.Images;
import com.abishek534.entertainment.fragment.Main;
import com.abishek534.entertainment.fragment.Settings;
import com.abishek534.entertainment.fragment.Video;
import com.abishek534.entertainment.user.EditProfile;
import com.abishek534.entertainment.user.UserInfo;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,permissionDialog.permissionSettingInterface {

    private FrameLayout frameLayout;
    private NavigationView navigationView;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
  //  private ImageView profileImage;
    public static UserInfo userInfo;
    private TextView navName;
    private LinearLayout editProfileImage;
    private CircleImageView profileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.nav_app_bar_open_drawer_description, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        writeSettingPermissoion();
    }

    private void init()
    {
      frameLayout=findViewById(R.id.frame_container);
      navigationView=findViewById(R.id.navigation_view);
      navigationView.setNavigationItemSelectedListener(this);
      mDrawer = findViewById(R.id.drawer_layout);
      toolbar = findViewById(R.id.toolbar_main);
      setSupportActionBar(toolbar);
      toolbar.setTitle("Home");
      loadFragment(new Main(),"Home");
      View headerView = navigationView.getHeaderView(0);
      profileImage = headerView.findViewById(R.id.nav_profile_image);
      editProfileImage=headerView.findViewById(R.id.nav_edit_profile);
      navName = headerView.findViewById(R.id.nav_profile_name);
      editProfileImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(userInfo.getUserName() != null) {
                  Intent intent = new Intent(Home.this, EditProfile.class);
                  startActivity(intent);
                  mDrawer.closeDrawer(navigationView);
              }
          }
      });
      userInfo = new UserInfo();
      getUserInfo();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.nav_home:loadFragment(new Main(),"Home");
                      mDrawer.closeDrawer(navigationView);
                      menuItem.setChecked(true);
                return true;
            case R.id.nav_video:loadFragment(new Video(),"Videos");
                mDrawer.closeDrawer(navigationView);
                menuItem.setChecked(true);
                return true;
            case R.id.nav_audio:loadFragment(new Audio(),"Audios");
                mDrawer.closeDrawer(navigationView);
                menuItem.setChecked(true);
                return true;
            case R.id.nav_images:loadFragment(new Images(),"Wallpapers");
                mDrawer.closeDrawer(navigationView);
                menuItem.setChecked(true);
                return true;
            case R.id.nav_settings:loadFragment(new Settings(),"Settings");
                mDrawer.closeDrawer(navigationView);
                menuItem.setChecked(true);
                return true;
            case R.id.nav_about_us:loadFragment(new AboutUs(),"About Us");
                mDrawer.closeDrawer(navigationView);
                menuItem.setChecked(true);
                return true;
            case R.id.nav_logout:
                FirebaseMessaging.getInstance().unsubscribeFromTopic("all");
                FirebaseAuth.getInstance().signOut();
                clearSharedPreference();
                finish();

        }
        return false;
    }

    public void loadFragment(Fragment fragment, String title)
    {
        toolbar.setTitle(title);
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,fragment);
        transaction.commit();

    }

    public void getUserInfo()
    {
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userInfo.setUserName(dataSnapshot.child("userName").getValue().toString());
                        userInfo.setUserImage(dataSnapshot.child("image").getValue().toString());
                        userInfo.setUserEmail(dataSnapshot.child("email").getValue().toString());
                        userInfo.setUserMob(dataSnapshot.child("mob").getValue().toString());
                        if(userInfo.getUserImage().equals("na") )
                        {
                        }
                        else{
                            Picasso.get().load(userInfo.getUserImage()).into(profileImage);
                            //Toast.makeText(getApplicationContext(),userInfo.getUserImage(),Toast.LENGTH_SHORT).show();
                        }

                        navName.setText(userInfo.getUserName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void writeSettingPermissoion()
    {
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (android.provider.Settings.System.canWrite(Home.this))
            {
                // perform your actions
            }
            else
            {

                permissionDialog dialog=new permissionDialog();
                dialog.setStyle(DialogFragment.STYLE_NO_FRAME,R.style.setting_dialog);
                dialog.show(getSupportFragmentManager(),"permissionRequired");

            }
        }
    }

    @Override
    public void permissionSettingCancelButton() {
        finish();
    }

    public void clearSharedPreference()
    {
        SharedPreferences preferences =getSharedPreferences("mob", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        try {
            FirebaseInstanceId.getInstance().deleteInstanceId();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
