package com.example.lab2_mobiledevelopment.Fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2_mobiledevelopment.Adapter.AdapterContacts;
import com.example.lab2_mobiledevelopment.Adapter.UserAdapter;
import com.example.lab2_mobiledevelopment.MainActivity;
import com.example.lab2_mobiledevelopment.MessageActivity;
import com.example.lab2_mobiledevelopment.R;
import com.example.lab2_mobiledevelopment.UserProfileUpdateAcitivity;
import com.example.lab2_mobiledevelopment.model.Contact;
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
import java.util.HashMap;
import java.util.List;

public class ContactsFragment extends Fragment {

    private ListView stu_listView;
    private int REQUEST_CONTACT_CODE = 0;
    Contact stu_contact;
    User stu_user;
    RecyclerView stu_recyclerView;
    Context context;
    private UserAdapter stu_userAdapter;
    private List<User> mUsers;;
    ArrayList<Contact> stu_arrayListContacts;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View stu_View = inflater.inflate(R.layout.fragment_contacts, container, false);
        stu_listView = stu_View.findViewById(R.id.listview_Contacts);
        stu_recyclerView = stu_View.findViewById(R.id.recycler_view);
        context = getActivity().getApplicationContext();

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT_CODE);

        }
        else{
            get();
        }


        return stu_View;

    }

    // This get User's phone number from address book
    private void get(){
        mUsers = new ArrayList<>();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                stu_arrayListContacts = new ArrayList<>();

                //Get all contacts
                Cursor cursorContacts = null;
                ContentResolver contentResolver = context.getContentResolver();
                try{
                    cursorContacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

                }catch (Exception ex){
                    Log.e("Error on contact", ex.getMessage());
                }



                if(cursorContacts.getCount() > 0){
                    while (cursorContacts.moveToNext()){
                        stu_contact = new Contact();
                        String stu_contactId = cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts._ID));
                        String stu_contactDisplayName = cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        stu_contact.contactName = stu_contactDisplayName;

                        int stu_hasPhoneNumber = Integer.parseInt(cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                        if(stu_hasPhoneNumber > 0){

                            Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                                    , null
                                    , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?"
                                    , new String[]{stu_contactId}
                                    , null);

                            while (phoneCursor.moveToNext()){
                                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                stu_contact.contactTelephoneNumber = phoneNumber;
                            }
                            phoneCursor.close();
                        }

                        for(DataSnapshot stu_snapshot : dataSnapshot.getChildren()){

                            stu_user = stu_snapshot.getValue(User.class);
                            //FirebaseAuth stu_auth;
                           // stu_auth = FirebaseAuth.getInstance();
                            FirebaseUser stu_fuser = FirebaseAuth.getInstance().getCurrentUser();

                            //System.out.println(stu_contact.contactTelephoneNumber.replaceAll("[-() ]+", ""));
                            //System.out.println(stu_user.getPhonenumber().split(" "));
//                            if(stu_user.getPhonenumber().substring(0,9) == )
                            HashMap<String, Object> map = new HashMap<>();
                            List<String> list = new ArrayList<>();

                            if (stu_fuser.getUid().equals(stu_user.getId())) {
                                list.add(stu_user.getPhonenumber());

                            }

                            System.out.println(list.size());

                            //System.out.println(reference.child("phonenumber"));
                            //List<String> list = new ArrayList<>();
                            //list.add(stu_user.getPhonenumber());

                            //System.out.println(list.size());
                            //System.out.println(stu_user.getPhonenumber().matches(stu_contact.contactTelephoneNumber.replaceAll("[-() ]+", "")));
                            for(int i = 0; i < list.size(); i++) {


                                if (list.get(i).equals(stu_contact.contactTelephoneNumber.replaceAll("[-() ]+", ""))) {

                                    stu_contact.contactStatus = "Register";

//                                mUsers.add(stu_user);
//                                Intent intent = new Intent(getContext(), MessageActivity.class);
//                                intent.putExtra("userid", stu_user.getId());
//                                startActivity(intent);

                                } else if (!stu_user.getPhonenumber().equals(stu_contact.contactTelephoneNumber.replaceAll("[-() ]+", ""))) {
                                    stu_contact.contactStatus = "Not Register";
                                }
                            }


                        }


                        stu_arrayListContacts.add(stu_contact);


                    }


            AdapterContacts stu_adapterContacts = new AdapterContacts(getContext(), stu_arrayListContacts);
            stu_listView.setAdapter(stu_adapterContacts);

                }
//                stu_userAdapter = new UserAdapter(getContext(), mUsers, true);
//                stu_recyclerView.setAdapter(stu_userAdapter);
//            AdapterContacts stu_adapterContacts = new AdapterContacts(getContext(), stu_arrayListContacts);
//            stu_listView.setAdapter(stu_adapterContacts);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
