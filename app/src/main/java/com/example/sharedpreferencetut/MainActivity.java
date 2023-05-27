package com.example.sharedpreferencetut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharedpreferencetut.adapter.ContactAdapter;
import com.example.sharedpreferencetut.database.DatabaseHelper;
import com.example.sharedpreferencetut.entity.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ContactAdapter.EditContact {

    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;

    ContactAdapter contactAdapter;

    DatabaseHelper databaseHelper;

    private final ArrayList<Contact> contactArrayList  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Contact Manager");
        initView();

        contactArrayList.addAll(databaseHelper.getAllContact());

        contactAdapter = new ContactAdapter(this);
        contactAdapter.setContacts(contactArrayList);
        recyclerView.setAdapter(contactAdapter);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

        floatingActionButton.setOnClickListener(v -> addAndEditContacts(false , null , -1));


    }




    private void initView() {
        floatingActionButton = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu , menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void addAndEditContacts(boolean updating, final Contact contact, final int position) {

           View view =   LayoutInflater.from(this).inflate(R.layout.add_contact ,null , false);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(view);

        final  TextView contactTitle = view.findViewById(R.id.contactTitle);
        final EditText contactName = view.findViewById(R.id.name);
        final EditText contactEmail = view.findViewById(R.id.email);

        contactTitle.setText(!updating ? "Add New Contact" : " Edit Contact");
        if(updating && contact != null){
            contactName.setText(contact.getName());
            contactEmail.setText(contact.getEmail());
        }

        alertBuilder.setCancelable(false)
                .setPositiveButton(updating ? "Update" : "Save", (dialog, which) -> {



                }).setNegativeButton("Delete", (dialog, which) -> {

                    if(updating){
                        DeleteContact(contact , position);
                    }else{
                        dialog.cancel();
                    }
                });

        final AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = contactName.getText().toString();
            String email = contactEmail.getText().toString();
            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email)){
                Toast.makeText(MainActivity.this, "Please Enter a name", Toast.LENGTH_SHORT).show();
                return;
            }else {
                alertDialog.dismiss();

            }
            if(updating && contact != null){


                UpdateContact(new Contact(name, email, contact.getId()) , position);
            }else{

                CreateContact(name ,email);
            }

        });



    }

    private void UpdateContact(Contact contact, int position) {
        contactArrayList.remove(position);
        contactArrayList.add(contact);
        databaseHelper.updateContact(contact);
        contactAdapter.notifyDataSetChanged();

    }
    private void CreateContact(String name , String email){

        long id = databaseHelper.insertContact(name , email);
        Contact newContact = new Contact(name , email , (int) id);
        contactArrayList.add(newContact);
        contactAdapter.notifyDataSetChanged();
    }

    private void DeleteContact(Contact contact, int position) {
        contactArrayList.remove(position);
        databaseHelper.deleteContact(contact);
        contactAdapter.notifyDataSetChanged();



    }


}