
package com.fnmeinss.pochette;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    private DatabaseReference mRef;
    private RecyclerView rvProfile;
    private ArrayList<Users> usersArrayList;
    private ProfilAdapter profilAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        rvProfile = findViewById(R.id.rvProfile);
        rvProfile.setHasFixedSize(true);
        rvProfile.setLayoutManager(new LinearLayoutManager(this));

        mRef = FirebaseDatabase.getInstance().getReference("Person").child("Users");
        List<Users> usersList = new ArrayList<>();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot d : snapshot.getChildren()) {
                    Users users = d.getValue(Users.class);
                    String name= (String) users.getuserName();
                    String surname = (String)users.getuserSurname();
                    String address= (String) users.getuserAdress();
                    String email=(String)users.getuserEmail();
                    String number=(String)users.getuserNumber();

                    SharedPreferences sp = getSharedPreferences("aktifkullanici", Context.MODE_PRIVATE);
                    String users2 = sp.getString("nameandsurname", "");

                    if (users2.equals(name+" "+surname)) {
                        Users users1 = new Users(email,name,surname,number,address);
                        usersList.add(users1);
                    }

                }
                profilAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        profilAdapter = new ProfilAdapter(this,usersList);
        rvProfile.setAdapter(profilAdapter);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profileFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeFragment:
                        startActivity(new Intent(getApplicationContext(),MainPage.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.donationFragment:
                        startActivity(new Intent(getApplicationContext(),Donation.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.cartFragment:
                        startActivity(new Intent(getApplicationContext(),Cart.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profileFragment:

                        return true;
                }

                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent List_Ekran_gecis = new Intent(Profile.this, MainPage.class);
        startActivity(List_Ekran_gecis);
        finish();
    }
}