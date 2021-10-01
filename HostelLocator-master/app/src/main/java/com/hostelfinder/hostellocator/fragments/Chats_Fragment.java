package com.hostelfinder.hostellocator.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.Api;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hostelfinder.hostellocator.HelperClasses.Adapter_ForListOfHostels;
import com.hostelfinder.hostellocator.HelperClasses.Chat;
import com.hostelfinder.hostellocator.HelperClasses.ChatList;
import com.hostelfinder.hostellocator.HelperClasses.HolderClass_Hostel_Info;
import com.hostelfinder.hostellocator.HelperClasses.User_Model;
import com.hostelfinder.hostellocator.HelperClasses.Users_chat_adpater;
import com.hostelfinder.hostellocator.R;
import com.hostelfinder.hostellocator.notification.Token;

import java.util.ArrayList;
import java.util.List;


public class Chats_Fragment extends Fragment {


    private RecyclerView recyclerView;
    private Users_chat_adpater userAdapter;
    private List<User_Model> mUsers;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<ChatList> userList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats_, container, false);


        recyclerView = view.findViewById(R.id.recycler_view_chatsFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        userList = new ArrayList<>();


        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatList chatList = snapshot.getValue(ChatList.class);
                    userList.add(chatList);
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ///////////// last updates ////// this line and method update token....
        updateToken(FirebaseInstanceId.getInstance().getToken());

        return view;
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }

    private void chatList() {

        mUsers = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AllUsers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User_Model user_model = snapshot.getValue(User_Model.class);
                    for (ChatList chatList : userList) {
                        if (user_model.getId().equals(chatList.getId())) {
                            mUsers.add(user_model);
                        }
                    }
                }
                userAdapter = new Users_chat_adpater(getContext(), mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



   /* private void readChats() {

        mUsers = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AllUsers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User_Model user = snapshot.getValue(User_Model.class);

                    if (user != null) {
                        ////////// displaying one user from the chats
                        for (String id : userList) {
                            if (user.getId().equals(id)) {
                                if (mUsers.size() != 0) {
                                    for (User_Model user_model1 : mUsers) {
                                        if (!user.getId().equals(user_model1.getId())) {
                                            mUsers.add(user);
                                            break;
                                        }
                                    }
                                } else {
                                    mUsers.add(user);
                                }
                            }
                        }
                    }
                }
                userAdapter = new Users_chat_adpater(getContext(), mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        *//*reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                ////////// display one user from chats
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HolderClass_Hostel_Info user = snapshot.getValue(HolderClass_Hostel_Info.class);
                    if (user != null) {
                        for (String id : wardenList) {
                            if (user.getWardenUid().
                                    equals(id)) {
                                if (mUsers.size() != 0) {

                                    for (HolderClass_Hostel_Info
                                            user1 :

                                            mUsers) {
                                        if (!user.getWardenUid().equals(user1.getWardenUid())) {
                                            mUsers.add(user);
                                        }
                                    }
                                } else {
                                    mUsers.add(user);
                                }
                            }
                        }
                    }
                }
                userAdapter = new Users_chat_adpater(getContext(), mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*//*

    }*/
}