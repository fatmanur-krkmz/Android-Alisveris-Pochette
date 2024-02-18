package com.fnmeinss.pochette;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
//bus la ilgili şeyi sildim proje isminden hata veriyodu
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.MyProductViewHolder> {
    private Context context;
    private List<Product> productModelList;
    private ICartLoadListener iCartLoadListener;

    public MyProductAdapter(Context context, List<Product> productModelList, ICartLoadListener iCartLoadListener) {
        this.context = context;
        this.productModelList = productModelList;
        this.iCartLoadListener = iCartLoadListener;
    }

    @NonNull
    @Override
    public MyProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_product_item,parent,false);

        return new MyProductViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyProductViewHolder holder, int position) {
        Product product= productModelList.get(position);
        holder.txtPrice.setText(product.getProduct_price()+"TL");
        holder.txtName.setText(product.getProduct_description2());
        String url=product.getImageUrl()+".jpg";
        Picasso.get().load(url).into(holder.imageView);
        holder.setListener((view, adapterposition) -> {
            addToCart(getProductModelList().get(position));
        } );
    }

    private void addToCart(Product productModel) {
        DatabaseReference userCart = FirebaseDatabase.getInstance().getReference("Cart") ;//

        //CartModel productModel=new CartModel();

        userCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) { //If user already have item in cart
                    //Update quantity and totalprice
                    CartModel cartModel = snapshot.getValue(CartModel.class);
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("quantity", cartModel.getQuantity() + 1);
                    updateData.put("totalPrice", cartModel.getQuantity() * cartModel.getPrice());

                    userCart.updateChildren(updateData)
                            .addOnSuccessListener(aVoid -> {
                                iCartLoadListener.onCartLoadFailed("Add to Cart Success");
                            })
                            .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));
                }
                else //it's checking if the item is in the cart, add new
                {
                    CartModel cartModel = new CartModel();
                    cartModel.setName(productModel.getName());
                    cartModel.setImageUrl(productModel.getImageUrl());
                    cartModel.setKey(productModel.getKey());
                    cartModel.setPrice(productModel.getProduct_price());
                    cartModel.setQuantity(1);
                    cartModel.setTotalPrice(cartModel.getQuantity()*cartModel.getPrice());
                    cartModel.setTotalPrice(productModel.getProduct_price());

                    userCart.child(productModel.getKey())
                            .setValue(cartModel)
                            .addOnSuccessListener(aVoid -> {
                                iCartLoadListener.onCartLoadFailed("Add to Cart Success");
                            })
                            .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));
                }
                EventBus.getDefault().postSticky(new MyUpdateCartEvent());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iCartLoadListener.onCartLoadFailed(error.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return getProductModelList().size();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Product> getProductModelList() {
        return productModelList;
    }

    public void setProductModelList(List<Product> productModelList) {
        this.productModelList = productModelList;
    }

    public ICartLoadListener getiCartLoadListener() {
        return iCartLoadListener;
    }

    public void setiCartLoadListener(ICartLoadListener iCartLoadListener) {
        this.iCartLoadListener = iCartLoadListener;
    }

    public class MyProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public TextView txtName;
        public TextView txtPrice;

        private IRecyclerViewClickListener listener;
        public IRecyclerViewClickListener getListener() {
            return listener;
        }

        public void setListener(IRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        //unbinding
        public MyProductViewHolder(@NonNull View itemView){
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            txtName=itemView.findViewById(R.id.txtName);
            txtPrice=itemView.findViewById(R.id.txtPrice);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onRecyclerClick(v,getAdapterPosition());

        }
    }
}