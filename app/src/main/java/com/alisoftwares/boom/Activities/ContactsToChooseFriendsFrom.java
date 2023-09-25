package com.alisoftwares.boom.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.Manifest;
import android.provider.ContactsContract;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alisoftwares.boom.Adapters.ContactsAdapter;
import com.alisoftwares.boom.DB.ContactEnteties;
import com.alisoftwares.boom.DB.ContactDao;
import com.alisoftwares.boom.DB.ContactDatabase;
import com.alisoftwares.boom.Modals.Contact;
import com.alisoftwares.boom.databinding.ActivityContactsToChooseFriendsFromBinding;

import java.util.ArrayList;
import java.util.List;


public class ContactsToChooseFriendsFrom extends AppCompatActivity {



    private ArrayList<Contact> contactList_new = new ArrayList<>();
    ContactsAdapter friendsAdapter;
    private ContactDao contactDao;
    private ArrayList<Contact> contactList = new ArrayList<>();

    private SharedPreferences sharedPreferences;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    ActivityContactsToChooseFriendsFromBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactsToChooseFriendsFromBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences("BOOM_APP_SHARED_PREF", MODE_PRIVATE);


        ContactDatabase contactDb = Room.databaseBuilder(getApplicationContext(),
        ContactDatabase.class, "contact-db").build();
        contactDao = contactDb.contactDao();



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Check if contacts permission is granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Request contacts permission
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_CONTACTS},
                                PERMISSIONS_REQUEST_READ_CONTACTS);
                    } else {
                        // Permission already granted
                        retrieveSavedContacts();
                    }
        }
        else
        {
            //FIRST CHECK IF DB EXSISTS
                    getContacts();
        }


        friendsAdapter = new ContactsAdapter(getApplicationContext());
        binding.pollsFriendsRecycler.setAdapter(friendsAdapter);

          binding.closeActivity.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  onBackPressed();
              }
          });

          binding.AddNewContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openAddContactPage();
                }
            });


        binding.SearchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search or any desired action when the search icon is clicked
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);

                return true;
            }
        });

        binding.RefreshContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //FIRST DESTROY OLD DATABASE
                new AsyncTask<Void,Void,Void>(){

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {

                        ContactDatabase myDatabase =Room.databaseBuilder(getApplicationContext(),
                                ContactDatabase.class, "contact-db").build();
                        ContactDao contactDao = myDatabase.contactDao();
                        contactDao.deleteAllContacts();
                        return null;

                    }
                }.execute();

                getContacts();
            }
        });
    }

    private void filterList(String newText) {

        contactList_new = new ArrayList<>();
        for(Contact c : contactList){

             if(c.getName().toLowerCase().contains(newText.toLowerCase())
                    || c.getPhoneNumber().contains(newText) )
                 contactList_new.add(c);

        }
        friendsAdapter.setDataIntoRecycler(contactList_new);
        friendsAdapter.notifyDataSetChanged();

    }

    private void retrieveSavedContacts() {
            new AsyncTask<Void, Void, List<ContactEnteties>>() {
                @Override
                protected List<ContactEnteties> doInBackground(Void... voids) {
                    return contactDao.getAllContacts();

                }

                @Override
                protected void onProgressUpdate(Void... values) {
                    super.onProgressUpdate(values);
                }

                @Override
                protected void onPostExecute(List<ContactEnteties> allContacts) {
                    Log.d("SIMRAN","size------------- "+allContacts.size());

                    if (allContacts != null && !allContacts.isEmpty()) {
                        Contact contact;

                        for (ContactEnteties c : allContacts) {
                             contact = new Contact(c.getName(), c.getPhoneNumber(),c.getChecked());
                            contactList.add(contact);

                        }
                    }

                    if(contactList.isEmpty())
                        binding.noContactsFound.setVisibility(View.VISIBLE);
                    else
                        Toast.makeText(ContactsToChooseFriendsFrom.this, ""+contactList.size(), Toast.LENGTH_SHORT).show();

                    friendsAdapter.setDataIntoRecycler(contactList);
                    friendsAdapter.notifyDataSetChanged();
                }
            }.execute();
        }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                //performContactsOperation();
                getContacts();
            } else {

                Toast.makeText(this, "Please give contact permissions", Toast.LENGTH_SHORT).show();
                // Permission is denied
                // Handle the situation where the user denied the permission
            }
        }
    }

    private void getContacts() {
        new AsyncTask<Void, ArrayList<Contact>, Void>() {
            @Override
            protected void onPreExecute() {
                binding.RefreshContacts.setVisibility(View.INVISIBLE);
                binding.progressRefreshContacts.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {

                contactList.clear();
                Cursor cursor = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );

                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        saveContactToSharedPreferences(name, phoneNumber);
                        publishProgress(contactList);

                    }

                    cursor.close();
                }

                return null;
            }


            @Override
            protected void onProgressUpdate(ArrayList<Contact>... values) {
                super.onProgressUpdate(values);

                if(contactList.isEmpty())
                    binding.noContactsFound.setVisibility(View.VISIBLE);
                else binding.noContactsFound.setVisibility(View.INVISIBLE);

                friendsAdapter.setDataIntoRecycler(contactList);
                friendsAdapter.notifyDataSetChanged();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // This method will be called when the background task is completed
                // You can update the UI or perform any other post-processing here
                binding.RefreshContacts.setVisibility(View.VISIBLE);
                binding.progressRefreshContacts.setVisibility(View.INVISIBLE);


                // Example: notify the user that contacts retrieval is completed
                Toast.makeText(ContactsToChooseFriendsFrom.this, "Contact List Updated. "+contactList.size()+" new contacts.", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private void saveContactToSharedPreferences(String name, String phoneNumber) {


        // Save a contact
        ContactEnteties contact = new ContactEnteties();
        contact.setName(name);
        contact.setPhoneNumber(phoneNumber);
        contact.setChecked(false);

        contactDao.insert(contact);

        Contact cc = new Contact(contact.getName(),contact.getPhoneNumber(),false);
        contactList.add(cc);



    }

    public  void openAddContactPage() {
        Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
        startActivity(intent);
    }

}