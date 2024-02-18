package com.fnmeinss.pochette;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.CardViewDesignHolder>{
    private StorageReference reference= FirebaseStorage.getInstance().getReference();
    public static DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("product");
    private Context mContext;
    private List<Product> productList;
    public  String mail,seller;

    public ProductAdapter(Context mContext, List<Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @NonNull
    @Override
    public CardViewDesignHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view,parent,false);


        return new CardViewDesignHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewDesignHolder holder, int position) {

        Product product= productList.get(position);
        holder.textViewPrice.setText(product.getProduct_price()+"");
        holder.textView2Description.setText(product.getProduct_description2());
        holder.textViewSeller.setText(product.getSeller());

        String url=product.getImageUrl()+".jpg";
        Picasso.get().load(url).into(holder.imageViewProduct);
        holder.buttonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProductPage addProductPage1=new AddProductPage();
                String description = holder.textView2Description.getText().toString();
                String price = holder.textViewPrice.getText().toString();
                String seller = holder.textViewSeller.getText().toString();
                addProductPage1.addProduct(url,seller,"",description,price,mail);

                Toast.makeText(mContext,product.getProduct_description2()+"product added to cart",Toast.LENGTH_LONG).show();

            }
        });
    }
    public void setUser(String mail){
        this.mail = mail;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class CardViewDesignHolder extends RecyclerView.ViewHolder{
        public ImageView imageViewProduct;
        public TextView textViewPrice;
        public TextView textView2Description;
        public Button buttonCard;
        public TextView textViewSeller;

        public CardViewDesignHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct=itemView.findViewById(R.id.imageViewProduct);
            textViewPrice=itemView.findViewById(R.id.textViewPrice);
            textView2Description=itemView.findViewById(R.id.textView2Description);
            buttonCard=itemView.findViewById(R.id.buttonCard);
            textViewSeller = itemView.findViewById(R.id.textViewSeller);

        }
    }
}
