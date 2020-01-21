package com.example.lab2_mobiledevelopment.Fragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lab2_mobiledevelopment.Adapter.UserAdapter;
import com.example.lab2_mobiledevelopment.R;
import com.example.lab2_mobiledevelopment.CloudNotifications.Token;
import com.example.lab2_mobiledevelopment.model.ChatUserId;
import com.example.lab2_mobiledevelopment.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment{
// This Fragment is only for displaying the user to whom you send messages
    public RecyclerView stu_recyclerView;
    private UserAdapter stu_userAdapter;
    private List<User> stu_mUsers;

    private FirebaseAuth stu_auth;

    FirebaseUser stu_fuser;
    DatabaseReference stu_reference;


    private List<ChatUserId> stu_usersList;
    public ValueEventListener listener;

    public ChatsFragment(){}


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View stu_view = inflater.inflate(R.layout.fragment_chats, container, false);
        //stu_auth = FirebaseAuth.getInstance();

        stu_recyclerView = stu_view.findViewById(R.id.recycler_view);
        stu_recyclerView.setHasFixedSize(true);
        stu_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        stu_recyclerView.addItemDecoration(new DividerItemDecoration(stu_recyclerView.getContext(), DividerItemDecoration.VERTICAL)); //line

        stu_fuser = FirebaseAuth.getInstance().getCurrentUser();

        stu_usersList = new ArrayList<>();

        //stu_reference = FirebaseDatabase.getInstance().getReference("Chats");

        stu_reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(stu_fuser.getUid());

        stu_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stu_usersList.clear();

                for (DataSnapshot stu_snapshot : dataSnapshot.getChildren()) {

                    ChatUserId chatUserIdList = stu_snapshot.getValue(ChatUserId.class);
                    stu_usersList.add(chatUserIdList);

//                    Chat stu_chat = stu_snapshot.getValue(Chat.class);
//
//                    if (stu_chat.getSender().equals(stu_fuser.getUid())) {
//                        stu_usersList.add(stu_chat.getReceiver());
//                    }
//
//                    if (stu_chat.getReceiver().equals(stu_fuser.getUid())) {
//                        stu_usersList.add(stu_chat.getSender());
//
//                    }

                    //stu_readChats();
                }

                stu_readChats();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        updateToken(FirebaseInstanceId.getInstance().getToken());
        return stu_view;
    }

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(stu_fuser.getUid()).setValue(token1);
    }
    private void stu_readChats() {

        stu_mUsers = new ArrayList<>();

        stu_reference = FirebaseDatabase.getInstance().getReference("Users");
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stu_mUsers.clear();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User stu_user =  snapshot.getValue(User.class);

                    for(ChatUserId userList : stu_usersList){
                        if(stu_user.getId().equals(userList.getId())){
                            stu_mUsers.add(stu_user);
                        }
                    }

                }


                stu_userAdapter = new UserAdapter(getContext(), stu_mUsers, true);
                stu_recyclerView.setAdapter(stu_userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        stu_reference.addValueEventListener(listener);


    }


}