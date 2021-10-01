package com.hostelfinder.hostellocator.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hostelfinder.hostellocator.HelperClasses.HolderClass_Hostel_Info;
import com.hostelfinder.hostellocator.HelperClasses.User_Model;
import com.hostelfinder.hostellocator.HelperClasses.Users_chat_adpater;
import com.hostelfinder.hostellocator.R;
import com.hostelfinder.hostellocator.User.UserMapsActivity_Second;

import java.util.ArrayList;
import java.util.List;


public class Users_Fragment extends Fragment {
    private RecyclerView recyclerView;
    private Users_chat_adpater users_chat_adpater;
    private List<User_Model> mUsers;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    EditText searchUsers;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_, container, false);


        searchUsers = view.findViewById(R.id.searchusers);
        searchUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchUser(charSequence);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        recyclerView = view.findViewById(R.id.users_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsers = new ArrayList<>();
        readUsers();
        return view;
    }

    private void searchUser(CharSequence charSequence) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("AllUsers").orderByChild("fullname")
                .startAt(charSequence.toString())
                .endAt(charSequence + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User_Model user = snapshot.getValue(User_Model.class);
                    if (user != null) {
                        if (!user.getId().equals(firebaseUser.getUid())) {
                            mUsers.add(user);
                        }
                    }
                }
                users_chat_adpater = new Users_chat_adpater(getContext(), mUsers);
                recyclerView.setAdapter(users_chat_adpater);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void readUsers() {
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AllUsers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (searchUsers.getText().toString().equals("")) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User_Model user_model = snapshot.getValue(User_Model.class);
                        if (user_model != null) {
                            if (!user_model.getId().equals(firebaseUser.getUid())) {
                                mUsers.add(user_model);

                            }
                        }
                    }

                    users_chat_adpater = new Users_chat_adpater(getContext(), mUsers);
                    recyclerView.setAdapter(users_chat_adpater);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}