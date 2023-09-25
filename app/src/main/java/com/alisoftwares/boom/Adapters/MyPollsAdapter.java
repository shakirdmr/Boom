package com.alisoftwares.boom.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.alisoftwares.boom.Modals.Poll;
import com.alisoftwares.boom.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;



public class MyPollsAdapter extends RecyclerView.Adapter<MyPollsAdapter.ViewHolder> {
    private ArrayList<Poll> myPolls;
    private Context context;

    public MyPollsAdapter(Context context) {
        this.context = context;
        myPolls  = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyPollsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_polls_modal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPollsAdapter.ViewHolder holder, int position) {

        if(myPolls.get(position).getPollImage().contains("null"))
        {
            holder.textPollBox.setVisibility(View.VISIBLE);
        holder.textStatus.setText(myPolls.get(position).getTitle());


        }
        else {
            holder.imagePollBox.setVisibility(View.VISIBLE);
            Glide.with(context).load(myPolls.get(position).getPollImage()).into(holder.imageStatus);


        }

    }

    @Override
    public int getItemCount() {
        return myPolls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout textPollBox;
        CardView imagePollBox;
        TextView textStatus,numberOfReplies1,numberOfReplies2;
        ImageView imageStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textStatus = itemView.findViewById(R.id.textPoll);
            numberOfReplies1 = itemView.findViewById(R.id.numberOfReplies1);
            numberOfReplies2 = itemView.findViewById(R.id.numberOfReplies2);
            imageStatus = itemView.findViewById(R.id.imagePoll);
            textPollBox = itemView.findViewById(R.id.textPollBox);
            imagePollBox = itemView.findViewById(R.id.imagePollBox);

        }
    }



    public void setDataIntoRecycler(ArrayList<Poll> myPolls) {
        this.myPolls = myPolls;
        notifyDataSetChanged();
    }
}
