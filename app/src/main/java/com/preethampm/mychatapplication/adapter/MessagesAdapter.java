package com.preethampm.mychatapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.preethampm.mychatapplication.R;
import com.preethampm.mychatapplication.model_class.Messages;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.preethampm.mychatapplication.ChatActivity.receiverImage1;
import static com.preethampm.mychatapplication.ChatActivity.senderImage;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item, parent, false);
            return new SenderViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout_item, parent, false);
        return new ReceiverViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        Messages messages = messagesArrayList.get(position);
        if(holder.getClass() == SenderViewHolder.class){
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.messageText.setText(messages.getMessage());

            Picasso.get().load(senderImage).into(viewHolder.circleImageView);
        }else{
            ReceiverViewHoder viewHolder = (ReceiverViewHoder) holder;
            viewHolder.messageText.setText(messages.getMessage());
            Picasso.get().load(receiverImage1).into(viewHolder.circleImageView);

        }

    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())) {
            return ITEM_SEND;
        }
        return ITEM_RECEIVE;
    }


    class SenderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView messageText;

        public SenderViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.message_image_1);
            messageText = itemView.findViewById(R.id.message_1);
        }
    }


    class ReceiverViewHoder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView messageText;

        public ReceiverViewHoder(@NonNull @NotNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.message_image_2);
            messageText = itemView.findViewById(R.id.message_2);
        }
    }
}
