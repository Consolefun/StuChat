package com.example.lab2_mobiledevelopment.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2_mobiledevelopment.Adapter.UserAdapter;
import com.example.lab2_mobiledevelopment.R;
import com.example.lab2_mobiledevelopment.SignUpActivity;
import com.example.lab2_mobiledevelopment.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PublicFragment extends Fragment {
    private RecyclerView stu_recyclerView;
    private UserAdapter stu_userAdapter;
    private List<User> stu_mUsers;
    EditText stu_SearchUsers;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View stu_view = inflater.inflate(R.layout.fragment_public, container, false);
        setupUI(stu_view.findViewById(R.id.relativeLayout));
        stu_recyclerView = stu_view.findViewById(R.id.recycler_view);
        stu_recyclerView.setHasFixedSize(true);
        stu_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        stu_recyclerView.addItemDecoration(new DividerItemDecoration(stu_recyclerView.getContext(), DividerItemDecoration.VERTICAL)); //line

        stu_mUsers = new ArrayList<>();

        stu_readUsers();
        stu_SearchUsers = stu_view.findViewById(R.id.stu_search_users);
        stu_SearchUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                StuSearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return stu_view;
    }


    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
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
                activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }


    private void StuSearch(String v) {
        final FirebaseUser stu_fuser = FirebaseAuth.getInstance().getCurrentUser();

        Query stu_query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("firstname")
                .startAt(v)
                .endAt(v+"\uf8ff");
        stu_query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stu_mUsers.clear();
                for (DataSnapshot stu_snapshot : dataSnapshot.getChildren()){
                    User stu_user = stu_snapshot.getValue(User.class);

                    assert stu_user != null;
                    assert stu_fuser != null;
                    if (!stu_user.getId().equals(stu_fuser.getUid())){
                        stu_mUsers.add(stu_user);
                    }
                }

                stu_userAdapter = new UserAdapter(getContext(), stu_mUsers, false);
                stu_recyclerView.setAdapter(stu_userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void stu_readUsers() {
        final FirebaseUser stu_firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference stu_reference = FirebaseDatabase.getInstance().getReference("Users");

        stu_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stu_mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User stu_user = snapshot.getValue(User.class);

//                    assert  stu_user != null;
//                    assert firebaseUser != null;
                    if (!stu_user.getId().equals(stu_firebaseUser.getUid())){
                        stu_mUsers.add(stu_user);
                    }
                }

                stu_userAdapter = new UserAdapter(getContext(), stu_mUsers, false);
                stu_recyclerView.setAdapter(stu_userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
