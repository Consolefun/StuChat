package com.example.lab2_mobiledevelopment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lab2_mobiledevelopment.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileUpdateAcitivity extends AppCompatActivity {

    int REQUEST_CAMERA = 0;

    int SELECT_FILE = 1;
    private CircleImageView userImage;
    private TextView userName;
    private Button userUpdatebtn;
    private EditText firstName, lastName, phoneNumber, dateofBirth;
    private ProgressBar progressBar;
    FirebaseAuth auth;
    private Uri stu_imageUri;
    private StorageTask uploadTask;

    User stu_user; //
    DatabaseReference databaseReference;
    StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_update_acitivity);
        setupUI(findViewById(R.id.parent));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        progressBar = findViewById(R.id.progress_bar);
        userImage = (CircleImageView) findViewById(R.id.profile_image);
        userName = (TextView) findViewById(R.id.username);
        firstName = (EditText) findViewById(R.id.up_firstname);
        lastName = (EditText) findViewById(R.id.up_lastname);
        phoneNumber = (EditText) findViewById(R.id.up_phonenumber);
        dateofBirth = (EditText) findViewById(R.id.up_dateofbirth);
        userUpdatebtn = (Button) findViewById(R.id.stu_btnUpdate);

        auth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid());

        userUpdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(UserProfileUpdateAcitivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                else{
                    onClickUserUpdate(firstName.getText().toString(), lastName.getText().toString(), phoneNumber.getText().toString(), dateofBirth.getText().toString());
                }

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stu_user = dataSnapshot.getValue(User.class);
                userName.setText(stu_user.getFirstname() + " " + stu_user.getLastname());
                firstName.setText(stu_user.getFirstname());
                lastName.setText(stu_user.getLastname());
                phoneNumber.setText(stu_user.getPhonenumber());
                dateofBirth.setText(stu_user.getDateofBirth());


                if(stu_user.getImageURL().equals("Default")){

                    userImage.setImageResource(R.mipmap.avatar6);
                }
                else{
                    Glide.with(getApplicationContext()).load(stu_user.getImageURL()).into(userImage);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        storageReference = FirebaseStorage.getInstance().getReference("images");

    }



    public void onClickUserPhoto(View view){
        selectImage();
    }

    private void selectImage(){
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileUpdateAcitivity.this);
        builder.setTitle("Add Photo!");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if(items[item].equals("Take Photo")){

                    cameraIntent();


                }
                else if(items[item].equals("Choose from Gallery")){
                    galleryItent();
                }else if(items[item].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    // Check if both permissions are granted to allow access to camera
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CAMERA){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                cameraIntent();
            }
            else{
                Toast.makeText(this, "Must allow permissions of both camera and storage", Toast.LENGTH_LONG).show();
            }
        }
    }

    // cameraIntent will only met if both permissions are granted
    private void cameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(UserProfileUpdateAcitivity.this, new String[] {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA);

        }
        else if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

            ActivityCompat.requestPermissions(UserProfileUpdateAcitivity.this, new String[] {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA);
        }
        else{
            startActivityForResult(intent, REQUEST_CAMERA);

        }

    }

    private void galleryItent(){
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == SELECT_FILE){
//                stu_imageUri = data.getData();
//                filePath = getPathFromUri(stu_imageUri);
                onSelecFromGalleryResult(data);
                if(uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(getApplicationContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
                }
            }else if(requestCode == REQUEST_CAMERA){

                onCaptureImageResult(data);
            }

        }
    }

    private void onSelecFromGalleryResult(Intent data){

        Bitmap Mybitmap = null;

        if(data != null){
            try{

                Mybitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), stu_imageUri = data.getData());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        //stu_imageUri = data.getData(); // added


        userImage.setImageBitmap(Mybitmap);
    }

    private String getFileExtensions(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void onCaptureImageResult(Intent data){
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        stu_imageUri = data.getData();
        String saveImageUrl = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                thumbnail,
                "Image",
                "Image of image"
        );
        stu_imageUri = Uri.parse(saveImageUrl);


        userImage.setImageURI(stu_imageUri);
    }

    public void onClickUserUpdate(final String stu_firstName, final String stu_lastName, final String stu_phoneNumber, final String stu_dateofBirth){

        // these are the codes that work without particular images for users

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid());
        FirebaseUser firebaseUser = auth.getCurrentUser();
        final String stu_userid = firebaseUser.getUid();


        //These works
//        User user = new User(stu_userid, userName.getText().toString(), stu_firstName, stu_lastName, "Default", stu_phoneNumber, "Online");
//        databaseReference.setValue(user);
        ///Toast.makeText(UserProfileUpdateAcitivity.this, "User Updated", Toast.LENGTH_LONG).show();



        if(stu_imageUri != null){

            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtensions(stu_imageUri));

            uploadTask = fileReference.putFile(stu_imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setProgress(0);
                                        }
                                    }, 500);
                                    String url = uri.toString();
                                    Toast.makeText(UserProfileUpdateAcitivity.this, "User updated", Toast.LENGTH_LONG).show();

                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("imageURL", url);
                                    map.put("id", stu_userid);
                                    map.put("firstname", stu_firstName);
                                    map.put("lastname", stu_lastName);
                                    map.put("phonenumber", stu_phoneNumber);
                                    map.put("dateofBirth", stu_dateofBirth);
                                    databaseReference.updateChildren(map);

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserProfileUpdateAcitivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });

        }
        else{
            Toast.makeText(UserProfileUpdateAcitivity.this, "User updated", Toast.LENGTH_LONG).show();
            User user = new User(stu_userid, userName.getText().toString(), stu_firstName, stu_lastName, stu_user.getImageURL(), stu_phoneNumber, "Online", stu_dateofBirth);
            databaseReference.setValue(user);

        }

        // The code end here

    }
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(UserProfileUpdateAcitivity.this);
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


}
