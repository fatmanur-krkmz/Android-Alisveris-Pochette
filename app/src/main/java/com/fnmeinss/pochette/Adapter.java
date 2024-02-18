package com.fnmeinss.pochette;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.CardViewHolder>{
    private Context context;
    private List<Information> informationList;

    public Adapter(Context context, List<Information> informationList) {
        this.context = context;
        this.informationList = informationList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_admin ,parent,false);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Information information=informationList.get(position);
        Log.e("inof", information.getAddress()+information.getAddress()+" \n"+information.getInstitutionName()+" \n"+information.getInfo()+"");
        holder.editTextInformation.setText("Address: "+information.getAddress()+" \n\nInstitution Name: "+information.getInstitutionName()+" \n\nInformation About Request: "+information.getInfo());
    }//burda hata veriyo amin açılmıyo bunun yüzünden
//ne hoş
    @Override
    public int getItemCount() {
        return informationList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder{
        public EditText editTextInformation;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextInformation=itemView.findViewById(R.id.editTextInformation);
        }
    }
}
