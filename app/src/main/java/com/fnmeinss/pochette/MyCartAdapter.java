package com.fnmeinss.pochette;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.myapplication.eventbus.MyUpdateCartEvent;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder> {
    private Context context;
    private List<CartModel> cartModelList;

    public MyCartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @Override
    public MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyCartViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartViewHolder holder, int position) {

        CartModel cartModel= cartModelList.get(position);
        holder.txtPrice.setText(cartModel.getPrice()*cartModel.getQuantity()+"₺");
        holder.txtName.setText(cartModel.getName());
        holder.txtQuantity.setText(cartModel.getQuantity()+"");
        Picasso.get().load(cartModel.getImageUrl()).into(holder.imageView);

        holder.btnMinus.setOnClickListener(v -> {
            minusCartItem(holder,cartModelList.get(position));
            holder.txtPrice.setText(cartModel.getPrice()*cartModel.getQuantity()+"TL");
        });
        holder.btnPlus.setOnClickListener(v -> {
            plusCartItem(holder,cartModelList.get(position));
            holder.txtPrice.setText(cartModel.getPrice()*cartModel.getQuantity()+"TL");

        });
        holder.connectButton.setOnClickListener(v -> {
            mailsender mails = new mailsender();
           context.startActivity(mails.mailsend("fnur.korkmaz13@gmail.com",cartModel.getName(),cartModel.getQuantity()+""));

        });
        holder.btnDelete.setOnClickListener(v-> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Delete item")
                    .setMessage("Do you want to delete item?")
                    .setNegativeButton("CANCEL",(dialog1, which) -> dialog1.dismiss())
                    .setPositiveButton("OK", (dialog2,which) -> {
                        //temp remove
                        notifyItemRemoved(position);
                        deleteFromFirebase(cartModelList.get(position));
                        dialog2.dismiss();
                    }).create();
            dialog.show();
        });
    }

    private void deleteFromFirebase(CartModel cartModel) {
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(cartModel.getKey())
                .removeValue()
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
    }

    private void plusCartItem(MyCartViewHolder holder, CartModel cartModel) {
        cartModel.setQuantity(cartModel.getQuantity()+1);
        cartModel.setTotalPrice(cartModel.getQuantity()*cartModel.getPrice());

        holder.txtQuantity.setText(new StringBuilder().append(cartModel.getQuantity()));
        updateFirebase(cartModel);
    }

    private void minusCartItem(MyCartViewHolder holder, CartModel cartModel) {
        if(cartModel.getQuantity() > 1)
        {
            cartModel.setQuantity((cartModel.getQuantity()-1));
            cartModel.setTotalPrice((cartModel.getQuantity()*cartModel.getPrice()));

            //updating Quantity
            holder.txtQuantity.setText(new StringBuilder().append(cartModel.getQuantity()));
            updateFirebase(cartModel);

        } ;
    }

    private void updateFirebase(CartModel cartModel) {
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(cartModel.getKey())
                .setValue(cartModel)
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));


    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class MyCartViewHolder extends RecyclerView.ViewHolder {
        public ImageView btnMinus,btnPlus,imageView,btnDelete;
        public TextView txtName,txtPrice,txtQuantity;
        public Button connectButton;
        public MyCartViewHolder(@NonNull View itemView) {
            super(itemView);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            imageView = itemView.findViewById(R.id.imageViewcart);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            connectButton = itemView.findViewById(R.id.Connectbutton);
        }
    }
}