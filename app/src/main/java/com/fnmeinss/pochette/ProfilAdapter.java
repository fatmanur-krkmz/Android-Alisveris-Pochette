package com.fnmeinss.pochette;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfilAdapter extends RecyclerView.Adapter<ProfilAdapter.CardViewDesignHolder>{
    private Context mContext;
    private List<Users> usersList;//list isimleri karışırsa bunu değiştir.

    public ProfilAdapter(Context mContext, List<Users> usersList) {
        this.mContext = mContext;
        this.usersList=usersList;
    }

    @NonNull
    @Override
    public ProfilAdapter.CardViewDesignHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_userprofile,parent,false);


        return new ProfilAdapter.CardViewDesignHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilAdapter.CardViewDesignHolder holder, int position) {

        Users users= usersList.get(position);
        holder.editTextUserName.setText(users.getuserName());
        holder.editTextNumberr.setText(users.getuserNumber());
        holder.editTextUserAddress.setText(users.getuserAdress());
        holder.editTextSurname.setText(users.getuserSurname());
        holder.editTextEmaill.setText(users.getuserEmail());

        //  AddProductPage addProductPage=new AddProductPage();
        // Picasso.get().load(url).into(holder.imageViewProduct);
        //holder.imageViewProduct.setImageResource(mContext.getResources().getIdentifier());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class CardViewDesignHolder extends RecyclerView.ViewHolder{
        public EditText editTextUserName,editTextSurname,editTextNumberr,editTextEmaill,editTextUserAddress;
        //public TextView textView2;


        public CardViewDesignHolder(@NonNull View itemView) {
            super(itemView);
            editTextUserName=itemView.findViewById(R.id.editTextUserName);
            editTextSurname=itemView.findViewById(R.id.editTextUserSurname);
            editTextNumberr=itemView.findViewById(R.id.editTextNumberr);
            editTextEmaill=itemView.findViewById(R.id.editTextEmaill);
            editTextUserAddress=itemView.findViewById(R.id.editTextUserAddress);

        }
    }
}