package com.fnmeinss.pochette;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.location.GnssAntennaInfo;
import android.media.metrics.Event;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fnmeinss.pochette.MyCartAdapter;
import com.fnmeinss.pochette.MyUpdateCartEvent;
import com.fnmeinss.pochette.CartModel;
import com.fnmeinss.pochette.ICartLoadListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.annotation.Native;
import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity implements ICartLoadListener {
    private RecyclerView recycler_card;
    private RelativeLayout mainLayout;
    private ImageView btnBack;
   private TextView txtPrice;
   private BottomNavigationView bottomNavigationView;
   private MainPage main_mail;
   private FirebaseAuth mAuth;
   private ICartLoadListener iCartLoadListener;


    @Override
    protected void onStop() {
        if(org.greenrobot.eventbus.EventBus.getDefault().hasSubscriberForEvent(MyUpdateCartEvent.class))
            org.greenrobot.eventbus.EventBus.getDefault().removeStickyEvent(MyUpdateCartEvent.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky=true)
    public void onUpdateCart(MyUpdateCartEvent event)
    {
        LoadCartFromFirebase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);
        mAuth= FirebaseAuth.getInstance();
         recycler_card=findViewById(R.id.recycler_card);
         mainLayout=findViewById(R.id.mainLayout);
         btnBack=findViewById(R.id.btnBack);
        txtPrice=findViewById(R.id.txtPrice);
        recycler_card.setHasFixedSize(true);
        recycler_card.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
     main_mail = new MainPage();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.cartFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeFragment:
                        startActivity(new Intent(getApplicationContext(), MainPage.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.donationFragment:
                        startActivity(new Intent(getApplicationContext(), Donation.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.cartFragment:

                        return true;
                    case R.id.profileFragment:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });
        LoadCartFromFirebase();
    }

    private void LoadCartFromFirebase() {
        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Cart")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot cartSnapshot:snapshot.getChildren()) {
                                CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                                cartModel.setKey(cartSnapshot.getKey());

                                if(cartModel.getProductType().equals(mAuth.getCurrentUser().getEmail()))
                                cartModels.add(cartModel);
                            }
                           onCartLoadSuccess(cartModels);
                        }
                        else
                            iCartLoadListener.onCartLoadFailed("Cart is empty.");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                      onCartLoadFailed(error.getMessage());
                    }
                });}


    public void onCartLoadSuccess(List<CartModel> CartModeList) {
        double sum = 0;
        for(CartModel cartModel : CartModeList) {
            sum +=cartModel.getTotalPrice();
        }

        MyCartAdapter adapter = new MyCartAdapter(this, CartModeList);
        recycler_card.setAdapter(adapter);
    }

    public void onCartLoadFailed(String message) {
        Snackbar.make(mainLayout,message, Snackbar.LENGTH_LONG).show();

    }
    @Override
    public void onBackPressed() {
        Intent List_Ekran_gecis = new Intent(Cart.this, MainPage.class);
        startActivity(List_Ekran_gecis);
        finish();
    }

}