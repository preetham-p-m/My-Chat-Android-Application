package com.preethampm.mychatapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.preethampm.mychatapplication.ChatActivity;
import com.preethampm.mychatapplication.HomeActivity;
import com.preethampm.mychatapplication.R;
import com.preethampm.mychatapplication.model_class.Users;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context homeActivity;
    ArrayList<Users> userArrayList;

    public UserAdapter(HomeActivity homeActivity, ArrayList<Users> userArrayList) {
        this.homeActivity = homeActivity;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(homeActivity).inflate(R.layout.item_user_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdapter.ViewHolder holder, int position) {
        Users users = userArrayList.get(position);

        holder.userName.setText(users.getName());
        holder.userStatus.setText(users.getStatus());
        Picasso.get().load(users.getImageUri()).into(holder.userProfile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeActivity, ChatActivity.class);
                intent.putExtra("name", users.getName());
                intent.putExtra("ReceiverImage", users.getImageUri());
                intent.putExtra("uid", users.getUid());
                homeActivity.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userProfile;
        TextView userName, userStatus;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            userProfile = itemView.findViewById(R.id.user_image_of_home);
            userName = itemView.findViewById(R.id.user_name_of_home);
            userStatus = itemView.findViewById(R.id.user_status_of_home);
        }
    }
}
