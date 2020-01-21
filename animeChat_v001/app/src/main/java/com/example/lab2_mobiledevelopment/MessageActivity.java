package com.example.lab2_mobiledevelopment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lab2_mobiledevelopment.Adapter.MessageAdapter;
import com.example.lab2_mobiledevelopment.CloudNotifications.Client;
import com.example.lab2_mobiledevelopment.CloudNotifications.Data;
import com.example.lab2_mobiledevelopment.CloudNotifications.MyResponse;
import com.example.lab2_mobiledevelopment.CloudNotifications.Sender;
import com.example.lab2_mobiledevelopment.CloudNotifications.Token;
import com.example.lab2_mobiledevelopment.Fragment.UserProfileFragment;
import com.example.lab2_mobiledevelopment.model.Chat;
import com.example.lab2_mobiledevelopment.model.User;
import com.google.android.gms.common.api.Api;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    CircleImageView stu_profile_image;
    TextView stu_username;
    FirebaseUser stu_fuser;
    DatabaseReference stu_reference;
    Intent intent;
    String userid;

    ImageButton stu_btn_send, stu_btn_record;
    EditText stu_textSend;

    MessageAdapter stu_messageAdapter;
    List<Chat> stu_mchat;
    String stu_phoneNumber = "7377800393";
    RecyclerView stu_recyclerView;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private FirebaseAuth stu_auth;

    boolean notify = false;
    APINotification apiNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        apiNotification = Client.getClient("https://fcm.googleapis.com/").create(APINotification.class);



        stu_recyclerView = findViewById(R.id.recycler_view);
        stu_recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);

        stu_recyclerView.setLayoutManager(linearLayoutManager);

        stu_profile_image = findViewById(R.id.profile_image);
        stu_username = findViewById(R.id.username);
        stu_btn_send = findViewById(R.id.btn_send);
        stu_btn_record = findViewById(R.id.btn_record);
        stu_textSend = findViewById(R.id.text_send);


        intent = getIntent();
        userid = intent.getStringExtra("userid");
        stu_fuser = FirebaseAuth.getInstance().getCurrentUser();
        stu_btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVoiceInput();            }
        });

        stu_btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String msg = stu_textSend.getText().toString();
                if(!msg.equals("")){
                    sendMessage(stu_fuser.getUid(), userid, msg);
                }else {
                    Toast.makeText(MessageActivity.this, "You can't send an empty message", Toast.LENGTH_LONG).show();
                }
                //This method hide keyboard when user click send

                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(stu_textSend.getWindowToken(), 0);
                stu_textSend.setText("");
            }
        });



        //stu_fuser = FirebaseAuth.getInstance().getCurrentUser();
        stu_reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        stu_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                stu_username.setText(user.getFirstname() + " " + user.getLastname());


                if (user.getImageURL().equals("Default")){
                    stu_profile_image.setImageResource(R.mipmap.avatar6);
                }

                else if (!user.getImageURL().equals(null)){
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(stu_profile_image);
                }

                readMessages(stu_fuser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    stu_textSend.setText(result.get(0));
                }
                break;
            }
        }
    }
    private void sendMessage(String sender, final String receiver, final String message){
        DatabaseReference stu_reference = FirebaseDatabase.getInstance().getReference().child("Chats");

        Chat chat = new Chat(sender, receiver, message);

        stu_reference.push().setValue(chat);

        // add user to chat fragment

        final DatabaseReference stu_ChatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
        .child(stu_fuser.getUid())
        .child(userid);

        stu_ChatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    stu_ChatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference stu_chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(userid)
                .child(stu_fuser.getUid());
        stu_chatRefReceiver.child("id").setValue(stu_fuser.getUid());

        final String stu_msg = message;

        stu_reference = FirebaseDatabase.getInstance().getReference("Users").child(stu_fuser.getUid());

        stu_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if(notify){
                    // Send notification to the user
                    sendNotification(receiver, user.getFirstname(), message);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendNotification(String receiver, final String username, final String message){

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");

        Query query = tokens.orderByKey().equalTo(receiver);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);

                    Data data = new Data(stu_fuser.getUid(),username+ ": "+ message, "New Message", userid);

                    Sender sender = new Sender(data, token.getToken());

                    apiNotification.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                            if (response.code() == 200){
                                if (response.body().success != 1){
                                    Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages(final String myid, final String userid, final String imageurl) {
        stu_mchat = new ArrayList<>();

        stu_reference = FirebaseDatabase.getInstance().getReference("Chats");
        stu_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stu_mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {

                        stu_mchat.add(chat);
                    }

                    stu_messageAdapter = new MessageAdapter(MessageActivity.this, stu_mchat, imageurl);
                    stu_recyclerView.setAdapter(stu_messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void user_status (String stu_status){
        FirebaseUser firebaseUser = stu_auth.getCurrentUser();
        final String stu_userid = firebaseUser.getUid();
        stu_reference = FirebaseDatabase.getInstance().getReference("Users").child(stu_userid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("stu_status", stu_status);
        stu_reference.updateChildren(hashMap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater stu_inflater = getMenuInflater();
        stu_inflater.inflate(R.menu.users, menu);
        return true;
    }

    private void currentUser(String userid){
        SharedPreferences.Editor editor = getSharedPreferences("CurrentUser", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //status("online");
        currentUser(userid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //reference.removeEventListener(seenListener);
        //status("offline");
        currentUser("none");
    }

    //@Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        FirebaseAuth stu_a = FirebaseAuth.getInstance();
//        switch(item.getItemId()){
//            case R.id.stu_call:
//                Intent stu_call = new Intent(Intent.ACTION_CALL);
//                stu_call.setData(Uri.parse("tel:"+stu_phoneNumber));
//                startActivity(stu_call); // add check permission to get rid the error
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}