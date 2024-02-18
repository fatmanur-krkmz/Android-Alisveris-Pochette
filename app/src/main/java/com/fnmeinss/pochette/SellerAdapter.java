package com.fnmeinss.pochette;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerAdapter  extends RecyclerView.Adapter<SellerAdapter.CardViewDesignHolder>{
    private Context mContext;
    private List<Product> productList;
    DatabaseReference databaseReferenceRead;

    public SellerAdapter(Context mContext, List<Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @NonNull
    @Override
    public SellerAdapter.CardViewDesignHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_viewsellermain,parent,false);

        return new SellerAdapter.CardViewDesignHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerAdapter.CardViewDesignHolder holder, int position) {

        Product product= productList.get(position);
        holder.textViewPrice.setText(product.getProduct_price()+"₺");
        holder.textView2Description.setText(product.getProduct_description2());
        holder.textViewSeller.setText(product.getSeller());
        String url=product.getImageUrl();
        Picasso.get().load(url).into(holder.imageViewProduct);
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(url);
            }
        });
        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit(url);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class CardViewDesignHolder extends RecyclerView.ViewHolder{
        public ImageView imageViewProduct;
        public TextView textViewPrice;
        public TextView textView2Description;
        public Button buttonDelete,buttonEdit;
        public TextView textViewSeller;
        public int data = 0;
        public CardViewDesignHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct=itemView.findViewById(R.id.imageViewProduct);
            textViewPrice=itemView.findViewById(R.id.textViewPrice);
            textView2Description=itemView.findViewById(R.id.textView2Description);
            buttonDelete=itemView.findViewById(R.id.buttonDelete);
            textViewSeller = itemView.findViewById(R.id.textViewSeller);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
        }

    }

    public void delete(String url){

        if (!url.equals("")){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef1 = database.getReference("product").child("clothes");
            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database1.getReference("product").child("toys");

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot d:snapshot.getChildren()){
                        Product product = d.getValue(Product.class);
                        String url2 = product.getImageUrl();
                        if (url.equals(url2)){
                            myRef.child(d.getKey()).removeValue();
                            Toast.makeText(mContext,product.getProduct_description2()+" Sepetten silindi.",Toast.LENGTH_LONG).show();
                            Intent i = new  Intent(mContext,SellerMainPage.class);
                            mContext.startActivity(i);

                        }}}

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            myRef1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    for(DataSnapshot d:snapshot.getChildren()){
                        Product product = d.getValue(Product.class);
                        String url2 = product.getImageUrl();
                        if (url.equals(url2)){
                            myRef1.child(d.getKey()).removeValue();
                            Intent i = new  Intent(mContext,SellerMainPage.class);
                            Toast.makeText(mContext,product.getProduct_description2()+" Sepetten silindi.",Toast.LENGTH_LONG).show();
                            mContext.startActivity(i);

                        }}}

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    public void edit(String url){




        if (!url.equals("")){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef1 = database.getReference("product").child("clothes");
            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database1.getReference("product").child("toys");
            final int[] data = {0};
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    data[0] += 1;
                    if(data[0] <2){
                        for(DataSnapshot d:snapshot.getChildren()){

                            Product product = d.getValue(Product.class);
                            String url2 = product.getImageUrl();
                            if (url.equals(url2)){
                                Intent i = new  Intent(mContext,AddProductPage.class);
                                i.putExtra("edittengeldi",true);
                                i.putExtra("gelenid",d.getKey());
                                i.putExtra("istoys",true);
                                mContext.startActivity(i);
                            }}}
                    else {
                        Intent i = new  Intent(mContext,SellerMainPage.class);
                        mContext.startActivity(i);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            myRef1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    for(DataSnapshot d:snapshot.getChildren()){
                        Product product = d.getValue(Product.class);
                        String url2 = product.getImageUrl();
                        if (url.equals(url2)){
                            Intent i = new  Intent(mContext,AddProductPage.class);
                            i.putExtra("edittengeldi",true);
                            i.putExtra("gelenid",d.getKey());
                            i.putExtra("istoys",false);
                            mContext.startActivity(i);


                        }}}

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



    }
}
