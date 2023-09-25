package com.alisoftwares.boom.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alisoftwares.boom.Activities.ViewPollFullScreen;
import com.alisoftwares.boom.Modals.FriendsPollsModal;
import com.alisoftwares.boom.Modals.Poll;
import com.alisoftwares.boom.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PollsFriendsApapter extends RecyclerView.Adapter<PollsFriendsApapter.ViewHolder> {

    private ArrayList<FriendsPollsModal> FirendsPolls;
    private Context context;


    public PollsFriendsApapter(Context context) {
        this.context = context;
        FirendsPolls = new ArrayList<>();
    }

    @NonNull
    @Override
    public PollsFriendsApapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_polls, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PollsFriendsApapter.ViewHolder holder, int position) {

        holder.friendName.setText(FirendsPolls.get(position).getName());
        holder.numberOfPolls.setText(FirendsPolls.get(position).getNumberOfPolls()+" recent polls");
        Glide.with(context).load(FirendsPolls.get(position).getImageUrl()).into(holder.friendsProfile);
        holder.lastUpdate.setText(FirendsPolls.get(position).getLastUpdateDate());

        holder.friendPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent it = new Intent(context, ViewPollFullScreen.class);
                context.startActivity(it);

            }
        });

    }

    @Override
    public int getItemCount() {
        return FirendsPolls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout friendPoll;
        TextView friendName, numberOfPolls, lastUpdate;
        ImageView friendsProfile;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            friendName = itemView.findViewById(R.id.friendName);
            numberOfPolls = itemView.findViewById(R.id.numberOfPolls);
            lastUpdate = itemView.findViewById(R.id.lastUpdate);
            friendsProfile = itemView.findViewById(R.id.friendsProfile);
            friendPoll = itemView.findViewById(R.id.friendPoll);
        }
    }

    public void setDataIntoRecycler(ArrayList<FriendsPollsModal> myPolls) {
        this.FirendsPolls = myPolls;
        notifyDataSetChanged();
    }
}
