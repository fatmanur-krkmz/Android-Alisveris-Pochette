package com.fnmeinss.pochette;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.CardViewDesignHolder>{
    private Context mContext;
    private List<Product> productList;

    public DonationAdapter(Context mContext, List<Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @NonNull
    @Override
    public DonationAdapter.CardViewDesignHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_donation,parent,false);
        return new DonationAdapter.CardViewDesignHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationAdapter.CardViewDesignHolder holder, int position) {

        Product product= productList.get(position);
        holder.textView2Description.setText(product.getProduct_description2());
        String url=product.getImageUrl()+".jpg";
        Picasso.get().load(url).into(holder.imageViewProduct);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class CardViewDesignHolder extends RecyclerView.ViewHolder{
        public ImageView imageViewProduct;
        public TextView textView2Description;


        public CardViewDesignHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct=itemView.findViewById(R.id.imageViewProduct);
            textView2Description=itemView.findViewById(R.id.textView2Description);

        }
    }
}
