package com.example.lab2_mobiledevelopment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.Tag;

import java.time.Instant;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 0;
    private FirebaseAuth stu_auth;
    ProgressBar progressBar;
    // UI references
    //private AutoCompleteTextView stu_emailView;
    private EditText stu_emailView;
    private EditText stu_passwordView;
    FirebaseUser stu_firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUI(findViewById(R.id.parent));
        stu_auth = FirebaseAuth.getInstance();

        stu_emailView = (EditText) findViewById(R.id.email);
        stu_passwordView = (EditText) findViewById(R.id.login_password);
        progressBar = (ProgressBar) findViewById(R.id.loginProgress);

        stu_firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // if the user signed in already it will go to the mainactivity.
        if(stu_firebaseUser != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


    }
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(LoginActivity.this);
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


    public void reset(View view) {
        final EditText reset = new EditText(this);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Reset Password");
        // set dialog message
        alertDialogBuilder
                .setMessage("Enter your email?")
                .setCancelable(false)
                .setView(reset)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (reset.getText().toString().equals("")){
                            Toast.makeText(LoginActivity.this, "Please type your email!", Toast.LENGTH_SHORT).show();
                        } else {
                            stu_auth.sendPasswordResetEmail(reset.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(LoginActivity.this, "Please check your E-mail box", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String stu_error = task.getException().getMessage();
                                        Toast.makeText(LoginActivity.this, stu_error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing

                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void Login(View view){
        //Reset errors
        stu_emailView.setError(null);
        stu_passwordView.setError(null);

        progressBar.setVisibility(View.VISIBLE);

        // Store values at the time when user try to login

        String stu_email = stu_emailView.getText().toString();
        String stu_password = stu_passwordView.getText().toString();

        // check for a valid password
        if(stu_email.matches("")){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "You need to enter an email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(stu_password.matches("")){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "You need to enter a password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!TextUtils.isEmpty(stu_password) && !checkValidPassword(stu_password)){
            progressBar.setVisibility(View.GONE);
            stu_passwordView.setError("Password must be at least 5 characters");
        }


        if(!checkValidEmail(stu_email) && !TextUtils.isEmpty(stu_email)){
            progressBar.setVisibility(View.GONE);
            stu_emailView.setError("Your email is not valid");

        }


        stu_auth.signInWithEmailAndPassword(stu_email, stu_password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to user
                        // If signin is successful, the auth state listener will be notified and logic
                        // to handle signed in

                        if(!task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Authentication failed, This user is not in the database", Toast.LENGTH_SHORT).show();
                        }else{
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    public boolean checkValidPassword(String stu_password){
        return  stu_password.length() >= 5;
    }

    public boolean checkValidEmail(String stu_email){
        return Patterns.EMAIL_ADDRESS.matcher(stu_email).matches();
    }

    public void redirectToSignUpPage(View view){
        Intent redirect = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(redirect);
    }

}
