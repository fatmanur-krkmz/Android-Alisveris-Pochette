package com.fnmeinss.pochette;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private StorageReference reference= FirebaseStorage.getInstance().getReference();
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;
    BottomNavigationView bottomNavigationView;
    BottomNavigationView topNavigationView;
    private RecyclerView rv;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth=FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        rv=findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        databaseReference= FirebaseDatabase.getInstance().getReference("product").child("toys");
        databaseReference1= FirebaseDatabase.getInstance().getReference("product").child("clothes");
        List<Product> productArrayList = new ArrayList<>();
        SharedPreferences sp = getSharedPreferences("aktifkullanici", Context.MODE_PRIVATE);//local saves 01.03



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productArrayList.clear();

                for(DataSnapshot d:snapshot.getChildren()){

                    Product product = d.getValue(Product.class);
                    float price = product.getProduct_price();
                    String description=product.getProduct_description2();
                    String url = product.getImageUrl();
                    String seller = product.getSeller();
                    Product product1=new Product(seller,"",url,description,price);
                    productArrayList.add(product1);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot d:snapshot.getChildren()){

                    Product product = d.getValue(Product.class);
                    float price =product.getProduct_price();
                    String description=product.getProduct_description2();
                    String url = product.getImageUrl();
                    String seller = product.getSeller();
                    Product product1=new Product(seller,"",url,description,price);
                    Log.e("Product",price+description+url);
                    productArrayList.add(product1);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter=new ProductAdapter(this,productArrayList);
        rv.setAdapter(adapter);
        adapter.setUser(mAuth.getCurrentUser().getEmail());


        bottomNavigationView=findViewById(R.id.bottom_navigation);
        topNavigationView=findViewById(R.id.top_navigation);
        bottomNavigationView.setSelectedItemId(R.id.homeFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeFragment:
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
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
        topNavigationView.setSelectedItemId(R.id.all_product);
        topNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.all_product:
                        databaseReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for(DataSnapshot d:snapshot.getChildren()){

                                    Product product = d.getValue(Product.class);
                                    float price = product.getProduct_price();
                                    String description=product.getProduct_description2();
                                    String url = product.getImageUrl();
                                    String seller = product.getSeller();
                                    Product product1=new Product(seller,"",url,description,price);
                                    Log.e("Product",price+description+url);
                                    // productArrayList.add(product);
                                    productArrayList.add(product1);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        return true;
                    case R.id.clothes:
                        databaseReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                 productArrayList.clear();

                                for(DataSnapshot d:snapshot.getChildren()){

                                    Product product = d.getValue(Product.class);
                                    float price = product.getProduct_price();
                                    String description=product.getProduct_description2();
                                    String url = product.getImageUrl();
                                    String seller = product.getSeller();
                                    Product product1=new Product(seller,"",url,description,price);
                                    Log.e("Product",price+description+url);
                                    productArrayList.add(product1);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        return true;
                    case  R.id.toys:
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                 productArrayList.clear();

                                for(DataSnapshot d:snapshot.getChildren()){

                                    Product product = d.getValue(Product.class);
                                    float price = product.getProduct_price();
                                    String description=product.getProduct_description2();
                                    String url = product.getImageUrl();
                                    String seller = product.getSeller();
                                    Product product1=new Product(seller,"",url,description,price);
                                    Log.e("Product",price+description+url);
                                    productArrayList.add(product1);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        return true;
                    case R.id.donation_request:
                        startActivity(new Intent(getApplicationContext(),DonationRequest.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences sp = getSharedPreferences("aktifkullanici", Context.MODE_PRIVATE);//local saves 01.03
        SharedPreferences.Editor editor = sp.edit();
        if(item.getItemId()==R.id.seller_page){
            editor.putString("loginType","Seller");
            editor.commit();
            Intent sellerpageIntent=new Intent(MainPage.this, SellerMainPage.class);
            startActivity(sellerpageIntent);
            finish();
        }else if(item.getItemId()==R.id.logout){
            mAuth.signOut();
            editor.putString("nameandsurname","");
            editor.commit();
            Intent intentToMain=new Intent(MainPage.this,MainActivity.class);
            startActivity(intentToMain);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }
}
