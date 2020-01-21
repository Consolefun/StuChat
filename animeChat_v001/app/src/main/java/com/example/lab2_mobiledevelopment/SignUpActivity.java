package com.example.lab2_mobiledevelopment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lab2_mobiledevelopment.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    int REQUEST_CAMERA = 0;
    int SELECT_FILE = 1;
    ProgressBar progressBar;
    Bitmap stu_userphoto = null;

    private FirebaseAuth stu_auth;
    private DatabaseReference stu_databaseReference;
    private StorageReference stu_storageReference;
    private StorageTask uploadTask;
    private Uri stu_imageUri;
    User stu_user;


    private EditText stu_userEmail, stu_userPassword, stu_FirstName, stu_LastName, stu_PhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setupUI(findViewById(R.id.parent));
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        stu_userEmail = (EditText) findViewById(R.id.signup_email);
        stu_userPassword = (EditText) findViewById(R.id.signup_password);
        stu_FirstName = (EditText) findViewById(R.id.signup_firstname);
        stu_LastName = (EditText) findViewById(R.id.signup_lastname);
        stu_PhoneNumber = (EditText) findViewById(R.id.signup_phonenumber);
        stu_auth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(SignUpActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    public void onSignUp(View v){
        progressBar.setVisibility(View.VISIBLE);
        // add the sign up details to firebase
        String stu_email = stu_userEmail.getText().toString().trim();
        String stu_password = stu_userPassword.getText().toString().trim();
        final String stu_Firstname = stu_FirstName.getText().toString().trim();
        final String stu_Lastname = stu_LastName.getText().toString().trim();
        final String stu_Phonenumber = stu_PhoneNumber.getText().toString().trim();
        //final String Uri_image = imageUri.toString().trim();
        if(TextUtils.isEmpty(stu_Phonenumber)){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "please enter your phone number in digits!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(stu_Phonenumber.length() != 10){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Mobile's phone numbers must have 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }
        if(stu_Phonenumber.contains(" ")){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Phone number must has no spaces", Toast.LENGTH_SHORT).show();
            return;
        }
        if(stu_Phonenumber.contains("-") || stu_Phonenumber.contains("(") || stu_Phonenumber.contains(")") || stu_Phonenumber.contains("*")
        || stu_Phonenumber.contains("#")){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Phone number must has no special characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(stu_email)){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "please enter your email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(stu_password)){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(stu_password.length() < 6 ){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Password is too short, enter minimum 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if(true){

            Toast.makeText(getApplicationContext(), "Account is being create...", Toast.LENGTH_SHORT).show();
        }

        //create user

        stu_auth.createUserWithEmailAndPassword(stu_email, stu_password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.


                        if(task.isSuccessful()){

                            FirebaseUser firebaseUser = stu_auth.getCurrentUser();
                            final String stu_userid = firebaseUser.getUid();
                            final String stu_search = stu_Firstname.toLowerCase() + stu_Lastname.toLowerCase();
                            final String stu_status = "offline";
//                            Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    progressBar.setProgress(0);
//                                }
//                            }, 500);
                            progressBar.setVisibility(View.GONE);
                            stu_databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(stu_userid);
                            stu_user = new User(stu_userid, stu_search, stu_Firstname, stu_Lastname, "Default", stu_Phonenumber, stu_status, "MM/DD/YYYY");

                            stu_databaseReference.setValue(stu_user);


                            stu_databaseReference.setValue(stu_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        Toast.makeText(SignUpActivity.this, "Create user complete" + task.isSuccessful(),Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                }
                            });

                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(SignUpActivity.this, "Failed to create account" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
