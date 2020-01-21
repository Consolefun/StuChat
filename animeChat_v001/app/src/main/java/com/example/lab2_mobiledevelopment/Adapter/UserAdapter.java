package com.example.lab2_mobiledevelopment.Adapter;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget. *;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab2_mobiledevelopment.MessageActivity;
import com.example.lab2_mobiledevelopment.R;
import com.example.lab2_mobiledevelopment.model.User;

import java.util.ArrayList;
import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;

    private boolean stu_ischat;

    //Constructor
    public UserAdapter(Context mContext, List<User> mUsers, boolean stu_ischat){
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.stu_ischat = stu_ischat;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    // Set user's image and their last and first name and set their status to be online and offline
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final User user = mUsers.get(position);
        holder.username.setText((user.getFirstname() + " " + user.getLastname()));


        if(user.getImageURL().equals("Default")){
            holder.profile_image.setImageResource(R.mipmap.avatar6);
        }
        else if(!user.getImageURL().equals(null)){
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }
        else if (stu_ischat){
            if (user.getStu_status().equals("online")){
                holder.stu_on.setVisibility(View.VISIBLE);
                holder.stu_off.setVisibility(View.GONE);
            } else {
                holder.stu_on.setVisibility(View.GONE);
                holder.stu_off.setVisibility(View.VISIBLE);
            }
        } else {
            holder.stu_on.setVisibility(View.GONE);
            holder.stu_off.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    // Set the recycler view for user in public and chat
    public class ViewHolder extends RecyclerView.ViewHolder{
        public  TextView username;
        public ImageView profile_image;
        private ImageView stu_on;
        private ImageView stu_off;
        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            stu_on = itemView.findViewById(R.id.stu_on);
            stu_off = itemView.findViewById(R.id.stu_off);

        }

    }
}
