package com.fnmeinss.pochette;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DonationsAdmin extends AppCompatActivity {
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
        reference= FirebaseDatabase.getInstance().getReference();
        rvDonation =findViewById(R.id.rvDonation);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
        rvDonation.setHasFixedSize(true);
        rvDonation.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        reference= FirebaseDatabase.getInstance().getReference("Donations");
        List<Product> productArrayList = new ArrayList<>();

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

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu_donationsadmin,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.donation_request){
            Intent intent=new Intent(DonationsAdmin.this, Admin.class);
            startActivity(intent);
            finish();
        }else if(item.getItemId()==R.id.logout){
            mAuth.signOut();
            Intent intentToMain=new Intent(DonationsAdmin.this,MainActivity.class);
            startActivity(intentToMain);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent List_Ekran_gecis = new Intent(DonationsAdmin.this, Admin.class);
        startActivity(List_Ekran_gecis);
        finish();
    }

}
