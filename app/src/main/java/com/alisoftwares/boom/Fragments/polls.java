package com.alisoftwares.boom.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alisoftwares.boom.Activities.AddNewPoll;
import com.alisoftwares.boom.Activities.ContactsToChooseFriendsFrom;
import com.alisoftwares.boom.Activities.MainActivity;
import com.alisoftwares.boom.Adapters.MyPollsAdapter;
import com.alisoftwares.boom.Adapters.PollsFriendsApapter;
import com.alisoftwares.boom.DB.ContactDao;
import com.alisoftwares.boom.DB.ContactDatabase;
import com.alisoftwares.boom.DB.ContactEnteties;
import com.alisoftwares.boom.Modals.Contact;
import com.alisoftwares.boom.Modals.FriendsPollsModal;
import com.alisoftwares.boom.Modals.Poll;
import com.alisoftwares.boom.R;
import com.alisoftwares.boom.databinding.FragmentPollsBinding;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class polls extends Fragment {

    private ArrayList<Contact> contactList;

    private ArrayList<FriendsPollsModal> myFriendsPoll;

    private ContactDao contactDao;
    ProgressBar progressBarMyPolls;
    ArrayList<Poll> MyPolls;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    String uid;
    PollsFriendsApapter pollsFriends;
    MyPollsAdapter myPollsAdapter;
    FragmentPollsBinding binding;
    public polls() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        getMyPolls();

        getTheFriendsPolls();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         binding = FragmentPollsBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment

         myPollsAdapter = new MyPollsAdapter(getContext());
        binding.myPolls.setAdapter(myPollsAdapter);

        pollsFriends = new PollsFriendsApapter(getContext());
        binding.pollsFriendsRecycler.setAdapter(pollsFriends);


        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

              //  if(binding.swipeRefresh.isRefreshing()) {
                    getMyPolls();
                    getTheFriendsPolls();

               // }
                binding.swipeRefresh.setRefreshing(false);
            }
        });


        ContactDatabase contactDb = Room.databaseBuilder(getContext(),
                                 ContactDatabase.class, "contact-db").build();
        contactDao = contactDb.contactDao();


        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

         uid = firebaseAuth.getUid();
         MyPolls = new ArrayList<>();
         contactList = new ArrayList<>();
        myFriendsPoll = new ArrayList<>();


     /*   FriendsPollsModal f = new FriendsPollsModal();

        f.setName("ali");
        myFriendsPoll.add(f);


        f.setName("ali");
        myFriendsPoll.add(f);

        f.setName("ali");
        myFriendsPoll.add(f);

        f.setName("ali");
        myFriendsPoll.add(f);

        f.setName("ali");
        myFriendsPoll.add(f);
        f.setName("ali");
        myFriendsPoll.add(f);
        f.setName("ali");
        myFriendsPoll.add(f);
        f.setName("ali");
        myFriendsPoll.add(f);
        f.setName("ali");
        myFriendsPoll.add(f);

        f.setName("ali");
        myFriendsPoll.add(f);

        f.setName("ali");
        myFriendsPoll.add(f);

        f.setName("ali");
        myFriendsPoll.add(f);

        f.setName("ali");
        myFriendsPoll.add(f);

        f.setName("ali");
        myFriendsPoll.add(f);*/

        pollsFriends.setDataIntoRecycler(myFriendsPoll);
        pollsFriends.notifyDataSetChanged();




        binding.addNewPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                open_add_new_poll();
            }
        });

        binding.chooseWhoToShowOnPolls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), ContactsToChooseFriendsFrom.class));
            }
        });

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                open_add_new_poll();
            }
        });

    return  binding.getRoot();
    }

    private void open_add_new_poll() {

        startActivity(new Intent(getActivity(), AddNewPoll.class));

    }


    private  void getMyPolls(){

        MyPolls.clear();
        binding.progressBarMyPolls.setVisibility(View.VISIBLE);


        firebaseDatabase.getReference().child("polls").
                child(uid).orderByChild("time").limitToLast(6).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        MyPolls.clear();
                        Poll poll;

                        for(DataSnapshot snapshot1: snapshot.getChildren()){
                            poll =  snapshot1.getValue(Poll.class);
                            MyPolls.add(poll);
                        }
                        Collections.reverse(MyPolls);
                        myPollsAdapter.setDataIntoRecycler(MyPolls);
                        myPollsAdapter.notifyDataSetChanged();
                        binding.progressBarMyPolls.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getTheFriendsPolls() {
        myFriendsPoll.clear();
        retrieveSavedContacts();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("polls");


        //GET UID
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String uid = userSnapshot.getKey();
                        String phoneNumber = userSnapshot.child("phoneNumber").getValue(String.class);

                        if (phoneNumber != null && contactList != null) {

                            final FriendsPollsModal[] friendsPollsModal = new FriendsPollsModal[1];

                            for (Contact contact : contactList) {
                                if (contact.getPhoneNumber().equals(phoneNumber)) {

                                    friendsPollsModal[0] = new FriendsPollsModal();
                                    friendsPollsModal[0].setName(contact.getName());
                                    friendsPollsModal[0].setImageUrl(userSnapshot.child("profileImage").getValue(String.class));


                                    final int[] polls = {0};
                                     ArrayList<Poll> FriendPolls = new ArrayList<Poll>();


                                    dataRef.child(uid).orderByChild("time").addValueEventListener(new ValueEventListener() {


                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (dataSnapshot.exists()) {



                                                Poll poll;
                                                for(DataSnapshot snapshot1: snapshot.getChildren()){
                                                    poll =  snapshot1.getValue(Poll.class);
                                                    FriendPolls.add(poll);
                                                }

                                                //Poll poll = dataSnapshot.getValue(Poll.class);
                                                // myFriendsPoll.add(poll);

                                                // if(isWithinLast24Hours(poll.getTime())) {

                                                //polls[0] = poll;
                                                //   }
                                                friendsPollsModal[0].setNumberOfPolls(String.valueOf(FriendPolls.size()));

                                                friendsPollsModal[0].setLastUpdateDate(getTimePassed(Long.parseLong(FriendPolls.get(FriendPolls.size() - 1).getTime())));

                                                myFriendsPoll.add(friendsPollsModal[0]);
                                                pollsFriends.setDataIntoRecycler(myFriendsPoll);
                                                pollsFriends.notifyDataSetChanged();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });





                                   /* dataRef.child(uid).orderByChild("time").addListenerForSingleValueEvent(new ValueEventListener() {


                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {


                                                Poll poll = dataSnapshot.getValue(Poll.class);
                                                // myFriendsPoll.add(poll);

                                               // if(isWithinLast24Hours(poll.getTime())) {

                                                    polls[0]++;
                                             //   }
                                                time[0] = poll.getTime();

                                                Log.d("SHAKIR", "Poll added: " + polls[0]+" "+time[0]);


                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Handle any errors that occur during the data retrieval process
                                        }
                                    });


                                    friendsPollsModal[0].setNumberOfPolls(String.valueOf(polls[0]));

                                   friendsPollsModal[0].setLastUpdateDate(time[0]);
                                    myFriendsPoll.add(friendsPollsModal[0]);
*/

                                }
                            }
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the data retrieval process
            }
        });





         }

    private void retrieveSavedContacts() {
        new AsyncTask<Void, Void, List<ContactEnteties>>() {
            @Override
            protected List<ContactEnteties> doInBackground(Void... voids) {
                return contactDao.getAllContacts();
            }

            @Override
            protected void onPostExecute(List<ContactEnteties> allContacts) {
                contactList.clear();
                if (allContacts != null && !allContacts.isEmpty()) {
                    Contact contact;
                    for (ContactEnteties c : allContacts) {

                        if(c.getChecked()) {
                             contact = new Contact(c.getName(), c.getPhoneNumber(), c.getChecked());
                            contactList.add(contact);

                        }
                    }
                }


            }
        }.execute();
    }

        public boolean isWithinLast24Hours(String givenTimeString) {
            // Parse the given time string into a Date object
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date givenTime = null;
            try {
                givenTime = format.parse(givenTimeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Check if the givenTime is not null and call the existing isWithinLast24Hours function
            if (givenTime != null) {
                return isWithinLast24Hours(String.valueOf(givenTime));
            } else {
                // Invalid given time
                return false;
            }
    }

    public static String getTimePassed(long givenTimeMillis) {
        long currentTimeMillis = System.currentTimeMillis();
        long timeDifferenceMillis = currentTimeMillis - givenTimeMillis;

        long minutesPassed = TimeUnit.MILLISECONDS.toMinutes(timeDifferenceMillis);
        if (minutesPassed < 1) {
            return  " now";
        }
        else if (minutesPassed<60)
            return minutesPassed+" min";


        long hoursPassed = TimeUnit.MILLISECONDS.toHours(timeDifferenceMillis);
        if (hoursPassed < 24) {
            return hoursPassed + " Hr";
        }

        long daysPassed = TimeUnit.MILLISECONDS.toDays(timeDifferenceMillis);
        if (daysPassed > 1 && daysPassed<2) {
            return " Yesterday";
        }
        else return  daysPassed+" Days";
    }


}