package com.example.lab2_mobiledevelopment.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lab2_mobiledevelopment.R;
import com.example.lab2_mobiledevelopment.model.Contact;

import java.util.List;

public class AdapterContacts extends BaseAdapter {
    Context mContext;
    List<Contact> contactList;

    // Constructor
    public AdapterContacts(Context context, List<Contact> mContact){
        this.mContext = context;
        this.contactList = mContact;
    }

    //Getters and setters
    public int getCount(){
        return contactList.size();
    }

    public Object getItem(int position){
        return contactList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View view = View.inflate(mContext, R.layout.contactlist_items, null);

        TextView textView_contactName = (TextView) view.findViewById(R.id.textview_contact_name);
        TextView textView_contactTelephoneNumber = (TextView) view.findViewById(R.id.textview_contact_phoneNumber);
        TextView textView_contactStatus = (TextView) view.findViewById(R.id.textview_contact_status);

        textView_contactName.setText(contactList.get(position).contactName);
        textView_contactTelephoneNumber.setText(contactList.get(position).contactTelephoneNumber);
        textView_contactStatus.setText(contactList.get(position).contactStatus);

        view.setTag(contactList.get(position).contactName);
        return view;
    }
}
