package com.fnmeinss.pochette;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Donation extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private RecyclerView rvDonation;
    private ArrayList<Product> productArrayList;
    private DonationAdapter adapterDonation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth=FirebaseAuth.getInstance();
        reference=FirebaseDatabase.getInstance().getReference();
        rvDonation =findViewById(R.id.rvDonation);
        rvDonation.setHasFixedSize(true);
        rvDonation.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        reference= FirebaseDatabase.getInstance().getReference("Donations");
        List<Product> productArrayList = new ArrayList<>();
       // SharedPreferences sp = getSharedPreferences("aktifkullanici", Context.MODE_PRIVATE);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productArrayList.clear();

                for(DataSnapshot d:snapshot.getChildren()){

                    Product product = d.getValue(Product.class);
                    String description=product.getProduct_description2();
                    String url = product.getImageUrl();
                    Product product2=new Product(url,description);
                    Log.e("Product",description+url);
                    productArrayList.add(product2);

               }
                adapterDonation.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapterDonation= new DonationAdapter(this,productArrayList);
        rvDonation.setAdapter(adapterDonation);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.donationFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeFragment:
                        startActivity(new Intent(getApplicationContext(),MainPage.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.donationFragment:

                        return true;
                    case R.id.cartFragment:
                        startActivity(new Intent(getApplicationContext(),Cart.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profileFragment:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.donation_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.add_donation){
            Intent intent=new Intent(Donation.this, AddDonation.class);
            startActivity(intent);
            finish();
        }else if(item.getItemId()==R.id.logout){
            mAuth.signOut();
            SharedPreferences sp = getSharedPreferences("aktifkullanici", Context.MODE_PRIVATE);//local saves 01.03
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("nameandsurname","");
            editor.commit();
            Intent intentToMain=new Intent(Donation.this,MainActivity.class);
            startActivity(intentToMain);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent List_Ekran_gecis = new Intent(Donation.this, MainPage.class);
        startActivity(List_Ekran_gecis);
        finish();
    }
}