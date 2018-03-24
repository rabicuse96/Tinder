package com.example.ahmad_zakir.tinder;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private cards cards_data[];

    private arrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;

    ListView listView;
    List<cards> rowitem;

    private DatabaseReference userdb;
    private String currentuid;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        userdb = FirebaseDatabase.getInstance().getReference().child("Users");

        currentuid = mAuth.getCurrentUser().getUid();

        checkusersex();


        rowitem = new ArrayList<cards>();

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowitem );

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView)findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowitem.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject

                cards obj =(cards) dataObject;
                String userid = obj.getUserid();

                userdb.child(oppositeusersex).child(userid).child("connection").child("nope").child(currentuid).setValue(true);
                Toast.makeText(MainActivity.this, "Left!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj =(cards) dataObject;
                String userid = obj.getUserid();
                isconnectionmatches(userid);

                userdb.child(oppositeusersex).child(userid).child("connection").child("yaps").child(currentuid).setValue(true);
                Toast.makeText(MainActivity.this, "Right!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
//                al.add("XML ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Clicked",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void isconnectionmatches(final String userid) {
        DatabaseReference currentuserconnectiondb = userdb.child(usersex).child(currentuid).child("connection").child("yaps").child(userid);
        currentuserconnectiondb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(MainActivity.this, "New Connection", Toast.LENGTH_LONG).show();
                    userdb.child(oppositeusersex).child(dataSnapshot.getKey()).child("connection").child("Matchers").child(currentuid).setValue(true);
                    userdb.child(usersex).child(currentuid).child("connection").child("Matchers").child(dataSnapshot.getKey()).setValue(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String usersex;
    private String oppositeusersex;

    public void checkusersex(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Male");
        maleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(user.getUid())){
                    usersex = "Male";
                    oppositeusersex ="Female";
                    getoppositesexuser();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        DatabaseReference femaleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Female");
        femaleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(user.getUid())){
                    usersex = "Female";
                    oppositeusersex ="Male";
                    getoppositesexuser();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void  getoppositesexuser(){
        DatabaseReference oppositeDb = FirebaseDatabase.getInstance().getReference().child("Users").child(oppositeusersex);
        oppositeDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
              if (dataSnapshot.exists() && !dataSnapshot.child("connection").child("nope").hasChild(currentuid) && !dataSnapshot.child("connection").child("yaps").hasChild(currentuid) ){
                  cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("Name").getValue().toString());
                  rowitem.add(Item);
                      arrayAdapter.notifyDataSetChanged();
              }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void logout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, ChooseLoginRegistrationActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}
