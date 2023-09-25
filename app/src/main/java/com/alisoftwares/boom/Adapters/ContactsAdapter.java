package com.alisoftwares.boom.Adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.alisoftwares.boom.DB.ContactDao;
import com.alisoftwares.boom.DB.ContactDatabase;
import com.alisoftwares.boom.DB.ContactEnteties;

import com.alisoftwares.boom.Modals.Contact;
import com.alisoftwares.boom.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private ArrayList<Contact> contactList = new ArrayList<>();
    private Context context;
    private List<Contact> friendList_contacts;

    private ContactDao contactDao;
    ContactDatabase contactDb;

    public ContactsAdapter(Context context) {
        this.context = context;
        friendList_contacts = new ArrayList<>();

        contactDb = Room.databaseBuilder(context,
                ContactDatabase.class, "contact-db").build();
        contactDao = contactDb.contactDao();


    }

    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_show_on_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder holder, int position) {
        holder.ContactName.setText(friendList_contacts.get(position).getName());
        holder.ContactNumber.setText(friendList_contacts.get(position).getPhoneNumber());

          holder.checkBox.setOnCheckedChangeListener(null);

          if(friendList_contacts.get(position).getChecked() !=null) {
              holder.checkBox.setChecked(friendList_contacts.get(position).getChecked());
          }
           /*   int colorResourceId = R.color.lightWhite;
              int colorValue = context.getResources().getColor(colorResourceId);
              holder.background_contact.setBackgroundColor(colorValue);
          }
          else {
              // Reset the background color when the CheckBox is not checked
              int colorResourceId = R.color.white;
              int colorValue = context.getResources().getColor(colorResourceId);
              holder.background_contact.setBackgroundColor(colorValue);
          }*/



        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                friendList_contacts.get(position).setChecked(isChecked);


                        UpdateToDatabase_preferenceContacts(
                                friendList_contacts.get(position).getName(),
                                friendList_contacts.get(position).getPhoneNumber(),
                                isChecked);

                        Toast.makeText(context, "" + friendList_contacts.get(position).getName(), Toast.LENGTH_SHORT).show();



            }
        });
    }

    private void UpdateToDatabase_preferenceContacts(String name, String phoneNumber, boolean isChecked) {

        ContactEnteties contact = new ContactEnteties();
        contact.setName(name);
        contact.setPhoneNumber(phoneNumber);
        contact.setChecked(isChecked);



        new InsertContactAsyncTask(contactDao).execute(contact);

    }



    @Override
    public int getItemCount() {
        return friendList_contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout background_contact;
        TextView ContactName, ContactNumber;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ContactName = itemView.findViewById(R.id.ContactName);
            ContactNumber = itemView.findViewById(R.id.ContactNumber);
            checkBox = itemView.findViewById(R.id.checkBox);
            background_contact = itemView.findViewById(R.id.background_contact);
        }
    }

    public void setDataIntoRecycler(List<Contact> friendList_contacts) {
        this.friendList_contacts = friendList_contacts;
        notifyDataSetChanged();
    }

  /*  private void retrieveSavedContacts() {
        new AsyncTask<Void, Void, List<PreferenceContactsEnteties>>() {
            @Override
            protected List<PreferenceContactsEnteties> doInBackground(Void... voids) {
                return prefernceContactsDao.getAllPrefernceContacts();
            }

            @Override
            protected void onPostExecute(List<PreferenceContactsEnteties> allContacts) {
                if (allContacts != null && !allContacts.isEmpty()) {
                    for (PreferenceContactsEnteties c : allContacts) {
                        Contact contact = new Contact(c.getName(), c.getPhoneNumber());
                       // contact.setChecked(true);
                        friendList_contacts.add(contact);
                    }
                }
                notifyDataSetChanged();
            }
        }.execute();
    }
*/
    private static class InsertContactAsyncTask extends AsyncTask<ContactEnteties, Void, Void> {
      private ContactDao contactDao;

      InsertContactAsyncTask(ContactDao contactDao) {
          this.contactDao = contactDao;
      }

        @Override
        protected Void doInBackground(ContactEnteties... contacts) {

            contactDao.update(contacts[0].getChecked(),contacts[0].getPhoneNumber(),contacts[0].getName());

            return null;
        }
    }

}
