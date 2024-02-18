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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SellerMainPage extends AppCompatActivity {
private FirebaseAuth mAuth;
private DatabaseReference reference;
    private DatabaseReference reference1;
private FirebaseDatabase data;
private RecyclerView rvSellerMain;
private ArrayList<Product> productArrayList;
private SellerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main_page);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();

        rvSellerMain = findViewById(R.id.rvSellerMain);
        rvSellerMain.setHasFixedSize(true);
        rvSellerMain.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        reference = FirebaseDatabase.getInstance().getReference("product").child("toys");
        reference1 = FirebaseDatabase.getInstance().getReference("product").child("clothes");
        List<Product> productArrayList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productArrayList.clear();

                for (DataSnapshot d : snapshot.getChildren()) {

                    Product product = d.getValue(Product.class);
                    float price = product.getProduct_price();
                    String description = product.getProduct_description2();
                    String url = product.getImageUrl();
                    String seller = product.getSeller();
                    SharedPreferences sp = getSharedPreferences("aktifkullanici", Context.MODE_PRIVATE);
                    String seller2 = sp.getString("nameandsurname", "");

                    if (seller2.equals(seller)) {
                       Product product1 = new Product(seller, "", url, description, price);
                        productArrayList.add(product1);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot d : snapshot.getChildren()) {


                    Product product = d.getValue(Product.class);
                    float price = product.getProduct_price();
                    String description = product.getProduct_description2();
                    String url = product.getImageUrl();
                    String seller = product.getSeller();

                    SharedPreferences sp = getSharedPreferences("aktifkullanici", Context.MODE_PRIVATE);
                    String seller2 = sp.getString("nameandsurname", "");
                    Log.e("seller",seller2+seller);

                    if (seller2.equals(seller)) {
                        Product product1 = new Product(seller, "", url, description, price);
                        productArrayList.add(product1);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new SellerAdapter(this, productArrayList);
        rvSellerMain.setAdapter(adapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu_seller,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences sp = getSharedPreferences("aktifkullanici", Context.MODE_PRIVATE);//local saves 01.03
        SharedPreferences.Editor editor = sp.edit();
        if(item.getItemId()==R.id.addproduct_page) {
            Intent customerpageIntent = new Intent(SellerMainPage.this, AddProductPage.class);
            startActivity(customerpageIntent);
            finish();
        }else if(item.getItemId()==R.id.customer_page){
            editor.putString("loginType","Customer");
            editor.commit();
            Intent customerpageIntent=new Intent(SellerMainPage.this,MainPage.class);
            startActivity(customerpageIntent);
            finish();
        }else if(item.getItemId()==R.id.logout){
            mAuth.signOut();
            editor.putString("nameandsurname","");
            editor.commit();
            Intent intentToMain=new Intent(SellerMainPage.this,MainActivity.class);
            startActivity(intentToMain);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        Intent List_Ekran_gecis = new Intent(SellerMainPage.this, MainPage.class);
        startActivity(List_Ekran_gecis);
        finish();
    }
}