package com.preethampm.mychatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.preethampm.mychatapplication.adapter.MessagesAdapter;
import com.preethampm.mychatapplication.model_class.Messages;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String receiverImage, receiverUid, receiverName, senderUid;
    CircleImageView profileImage;
    TextView receiverNameText;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    public static String senderImage;
    public static String receiverImage1;

    CardView sendBtn;
    EditText editMessage;

    String senderRoom, receiverRoom;

    RecyclerView messageAdapter;
    ArrayList<Messages> messagesArrayList;

    MessagesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        receiverImage = getIntent().getStringExtra("ReceiverImage");
        receiverName = getIntent().getStringExtra("name");
        receiverUid = getIntent().getStringExtra("uid");
        senderUid = firebaseAuth.getUid();

        messagesArrayList = new ArrayList<>();

        profileImage = findViewById(R.id.profile_image_of_particular_chat);
        receiverNameText = findViewById(R.id.receiver_name_of_particular_chat);

        sendBtn = findViewById(R.id.send_btn);
        editMessage = findViewById(R.id.edit_message);

        messageAdapter = findViewById(R.id.message_adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);
        adapter = new MessagesAdapter(ChatActivity.this, messagesArrayList);
        messageAdapter.setAdapter(adapter);


        Picasso.get().load(receiverImage).into(profileImage);
        receiverNameText.setText(receiverName);

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;


        DatabaseReference databaseReference = firebaseDatabase.getReference().child("User").child(senderUid);
        DatabaseReference chatReference = firebaseDatabase.getReference().child("chats")
                .child(senderRoom).child("messages");


        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                senderImage = snapshot.child("imageUri").getValue().toString();
                receiverImage1 = receiverImage;
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println(receiverUid);
                System.out.println(senderUid);
                System.out.println(receiverRoom);
                System.out.println(senderRoom);
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");

                String messageText = editMessage.getText().toString();
                if (messageText.isEmpty()) {
                    return;
                } else {
                    editMessage.setText("");
                    Date date = new Date();

                    Messages messages = new Messages(messageText, senderUid, date.getTime());

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages").push()
                            .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Done in one", Toast.LENGTH_SHORT).show();
                            firebaseDatabase.getReference().child("chats").child(receiverRoom).child("messages").push()
                                    .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                }
                            });
                            Toast.makeText(getApplicationContext(), "Why not", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

    }
}