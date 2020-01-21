package com.example.lab2_mobiledevelopment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lab2_mobiledevelopment.Fragment.UserProfileFragment;
import com.example.lab2_mobiledevelopment.Helper.BottomNavigationBehavior;
import com.google.android.gms.common.Feature;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView stu_bottomNavigationView = findViewById(R.id.bottom_navigation);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) stu_bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
        AppBarConfiguration stu_appBarConfiguration = new AppBarConfiguration.Builder( R.id.nav_chats, R.id.nav_Public,
                R.id.nav_profile, R.id.nav_Contacts).build();
        NavController stu_navController = Navigation.findNavController(this, R.id.fragment_container);
        NavigationUI.setupActionBarWithNavController(this, stu_navController, stu_appBarConfiguration);
        NavigationUI.setupWithNavController(stu_bottomNavigationView, stu_navController);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater stu_inflater = getMenuInflater();
        stu_inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FirebaseAuth stu_a = FirebaseAuth.getInstance();
        switch(item.getItemId()){
//            case R.id.my_profile:
//
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserProfileFragment()).commit();
//
//                break;
            case R.id.Sign_out:
                //user_status("offline");
                stu_a.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickUserProfile(View view){
        switch (view.getId()){
            case R.id.stu_NavUserProfile:
                Intent intent = new Intent();
                intent.setClassName("com.example.lab2_mobiledevelopment", "com.example.lab2_mobiledevelopment.UserProfileUpdateAcitivity");

                startActivity(intent);
                break;
        }

    }




//    private void user_status (String stu_status){
//        FirebaseUser firebaseUser = stu_auth.getCurrentUser();
//        final String stu_userid = firebaseUser.getUid();
//        stu_reference = FirebaseDatabase.getInstance().getReference("Users").child(stu_userid);
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("stu_status", stu_status);
//        stu_reference.updateChildren(hashMap);
//    }


}