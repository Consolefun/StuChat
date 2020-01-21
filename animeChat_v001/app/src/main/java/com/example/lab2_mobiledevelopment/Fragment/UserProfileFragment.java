package com.example.lab2_mobiledevelopment.Fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lab2_mobiledevelopment.LoginActivity;
import com.example.lab2_mobiledevelopment.R;
import com.example.lab2_mobiledevelopment.SignUpActivity;
import com.example.lab2_mobiledevelopment.model.Contact;
import com.example.lab2_mobiledevelopment.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment {
// This fragment is only to show the user profile who signed in
    CircleImageView stu_mImageProfile;

    int REQUEST_CAMERA = 0;
    int SELECT_FILE = 1;
    DatabaseReference stu_reference;
    FirebaseUser stu_fuser;
    private Uri stu_imageUri;
    private FirebaseAuth stu_auth;


    private TextView stu_mUsername, stu_Email, stu_PhoneNumber, stu_DateofBirth;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View stu_view = inflater.inflate(R.layout.fragment_userprofile, container, false);



        stu_mImageProfile = stu_view.findViewById(R.id.profile_image);
        stu_mUsername = stu_view.findViewById(R.id.username);
        stu_Email = stu_view.findViewById(R.id.emailProfile);
        stu_PhoneNumber = stu_view.findViewById(R.id.up_phonenumber);
        stu_DateofBirth = stu_view.findViewById(R.id.up_birthday);

        stu_fuser = FirebaseAuth.getInstance().getCurrentUser();

        stu_reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(stu_fuser.getUid());

        stu_reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User stu_user = dataSnapshot.getValue(User.class);
                stu_mUsername.setText(stu_user.getFirstname() + " " + stu_user.getLastname());
                stu_Email.setText(stu_fuser.getEmail());
                stu_DateofBirth.setText(stu_user.getDateofBirth());

                stu_PhoneNumber.setText("(" + stu_user.getPhonenumber().substring(0,3) + ")" + " " +
                        stu_user.getPhonenumber().substring(3,6) + " - " + stu_user.getPhonenumber().substring(6, stu_user.getPhonenumber().length()-0));

                if(getActivity() == null){
                    return;
                }
                if(stu_user.getImageURL().equals("Default")){
                    stu_mImageProfile.setImageResource(R.mipmap.avatar6);
                }
                else if(!stu_user.getImageURL().equals(null)){
                    Glide.with(getContext()).load(stu_user.getImageURL()).into(stu_mImageProfile);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        return stu_view;
    }


}