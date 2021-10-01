package com.hostelfinder.hostellocator.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hostelfinder.hostellocator.Common.Message_Activity;
import com.hostelfinder.hostellocator.Common.The_Dashboard;
import com.hostelfinder.hostellocator.R;

import java.util.List;

public class Users_chat_adpater extends RecyclerView.Adapter<Users_chat_adpater.ViewHolder> {

    private Context context;
    private List<User_Model> mUsers;
    String TheLastMessage;

    public Users_chat_adpater(Context context, List<User_Model> mUsers) {
        this.mUsers = mUsers;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item_in_chats_recycleriveiw, parent, false);
        return new Users_chat_adpater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User_Model user_model = mUsers.get(position);
        holder.username.setText(user_model.getFullname());
        holder.profileimage.setImageResource(R.drawable.profile_nav_icon);
        lastMessage(user_model.getId(), holder.lastmsg);

        /*  Glide.with(context).load(user_model.getHostelimage()).into(holder.profileimage);*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gomsgact = new Intent(context, Message_Activity.class);
                gomsgact.putExtra("userid", user_model.getId());
                /* gomsgact.putExtra("", user_model.getFullname());*/
                /*gomsgact.putExtra("imageee", user_model.getHostelimage());*/
                context.startActivity(gomsgact);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView profileimage;
        public TextView lastmsg;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.nameoftheuser);
            profileimage = itemView.findViewById(R.id.user_image);
            lastmsg = itemView.findViewById(R.id.lastmessage);
        }
    }

    /////// check for last message
    private void lastMessage(String userId, TextView last_msg) {
        TheLastMessage = "default";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId) ||
                                chat.getReceiver().equals(userId) && chat.getSender().equals(firebaseUser.getUid())) {
                            TheLastMessage = chat.getMessage();
                        }
                    }
                }

                switch (TheLastMessage) {
                    case "default":
                        last_msg.setText("No Messages");
                        break;
                    default:
                        last_msg.setText(TheLastMessage);
                        break;
                }
                TheLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
