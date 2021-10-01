package com.hostelfinder.hostellocator.Common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hostelfinder.hostellocator.HelperClasses.Adapter_ForListOfHostels;
import com.hostelfinder.hostellocator.HelperClasses.Chat;
import com.hostelfinder.hostellocator.HelperClasses.HolderClass_Hostel_Info;
import com.hostelfinder.hostellocator.HelperClasses.MessageAdapter;
import com.hostelfinder.hostellocator.HelperClasses.User_Model;
import com.hostelfinder.hostellocator.R;
import com.hostelfinder.hostellocator.User.UserMapsActivity_Second;
import com.hostelfinder.hostellocator.fragments.APIService;
import com.hostelfinder.hostellocator.notification.Client;
import com.hostelfinder.hostellocator.notification.Data;
import com.hostelfinder.hostellocator.notification.MyResponse;
import com.hostelfinder.hostellocator.notification.Sender;
import com.hostelfinder.hostellocator.notification.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Message_Activity extends AppCompatActivity {

    FirebaseUser fuser;
    FirebaseAuth firebaseAuth;
    DatabaseReference dReference;
    String WardenUid, WardenName, profileimage;
    TextView username, hostelnameunderusern;
    CircleImageView userimage;
    ImageButton btnsend;
    String imageurlll;
    EditText writemessage;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    List<Chat> mChats;

    String userid;
    String hostelname, wardenname;

    APIService apiService; /////////// extra
    boolean notify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_message);

        recyclerView = findViewById(R.id.therecyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        /////// extra lines
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        btnsend = findViewById(R.id.sendbuton);
        writemessage = findViewById(R.id.writemessage);
        hostelnameunderusern = findViewById(R.id.hostelnameunderusername);
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.image);
        firebaseAuth = FirebaseAuth.getInstance();


        /* imageurlll = getIntent().getStringExtra("imageee");*/
        userid = getIntent().getStringExtra("userid");
        wardenname = getIntent().getStringExtra("wardenname");
        hostelname = getIntent().getStringExtra("Hostelnameee");

        dReference = FirebaseDatabase.getInstance().getReference("AllUsers").child(userid);

        dReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User_Model user_model = snapshot.getValue(User_Model.class);
                if (user_model != null) {
                    username.setText(user_model.getFullname());
                    userimage.setImageResource(R.drawable.profile_nav_icon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        /*hostelnameunderusern.setText(getIntent().getStringExtra("Hostelnameee"));
        WardenUid = getIntent().getStringExtra("wardenid");
        WardenName = getIntent().getStringExtra("wardenname");
        profileimage = getIntent().getStringExtra("userimage");
        Glide.with(Message_Activity.this).load(profileimage).into(userimage);
        imageurlll = getIntent().getStringExtra("userimage");
        username.setText(WardenName);*/

        ///////////////////////////////////////////
        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
            readMessage(currentuser.getUid(), userid);
        } else {
            Toast.makeText(this, "Login Required", Toast.LENGTH_SHORT).show();
        }
        //////////////////////////////////////////

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String message = writemessage.getText().toString();
                if (!message.equals("")) {
                    SenDmessage(fuser.getUid(), userid, message);
                } else {
                    Toast.makeText(Message_Activity.this, "Empty message cannot be send", Toast.LENGTH_SHORT).show();
                }
                writemessage.setText("");
            }
        });

    }

    private void SenDmessage(String sender, String reciever, String message) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", reciever);
        hashMap.put("message", message);

        reference.child("chats").push().setValue(hashMap);
        /// add user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid())
                .child(userid);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatRef.child("id").setValue(userid);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //////////// extra lines

        final String msg = message;

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("AllUsers").child(fuser.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User_Model user = snapshot.getValue(User_Model.class);
                if (notify) {
                    if (user != null) {
                        sendNotification(reciever, user.getUsername(), msg);
                    }
                    notify = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //////////////////////////////////
    }

    //////////// extra method
    private void sendNotification(String receiver, String username, String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fuser.getUid(), R.mipmap.ic_launcher, username + ": " + message, "New Message",
                            userid);

                    assert token != null;
                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        assert response.body() != null;
                                        if (response.body().success != 1) {
                                            Toast.makeText(Message_Activity.this, "Failed!", Toast.LENGTH_SHORT).show();
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
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //////////////////////////

    private void readMessage(final String myid, String userid) {
        mChats = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChats.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat != null) {
                        if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                            mChats.add(chat);

                        }
                        messageAdapter = new MessageAdapter(Message_Activity.this, mChats);
                        recyclerView.setAdapter(messageAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void status(String status) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AllUsers").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("Offline");
    }
}