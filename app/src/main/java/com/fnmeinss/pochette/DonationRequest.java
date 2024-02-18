package com.fnmeinss.pochette;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DonationRequest extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button createDemand_button;
    EditText address_MultiAutoCompleteTextView;
    EditText informationRequest_MultiAutoCompleteTextView;
    EditText institutionName_MultiAutoCompleteTextView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("DonationRequest");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth= FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_request);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        createDemand_button=findViewById(R.id.createDemand_button);
        address_MultiAutoCompleteTextView=findViewById(R.id.address_MultiAutoCompleteTextView) ;

        informationRequest_MultiAutoCompleteTextView=findViewById(R.id.informationRequest_MultiAutoCompleteTextView);

        institutionName_MultiAutoCompleteTextView=findViewById(R.id.institutionName_MultiAutoCompleteTextView);
        createDemand_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = ref.push().getKey();

                String addresss = address_MultiAutoCompleteTextView.getText().toString();
                String informationnn=informationRequest_MultiAutoCompleteTextView.getText().toString();
                String institutionnn=institutionName_MultiAutoCompleteTextView.getText().toString();
                ref.child(ID).child("address").setValue(addresss);
                ref.child(ID).child("info").setValue(informationnn);
                ref.child(ID).child("institutionName").setValue(institutionnn);
                Toast.makeText(DonationRequest.this,"Request is send.",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DonationRequest.this, MainPage.class);
                startActivity(intent);



            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.donation_request,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.main_page){
            Intent sellerpageIntent=new Intent(DonationRequest.this, MainPage.class);
            startActivity(sellerpageIntent);
            finish();
        }else if(item.getItemId()==R.id.logout){
            mAuth.signOut();
            Intent intentToMain=new Intent(DonationRequest.this,MainActivity.class);
            startActivity(intentToMain);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent List_Ekran_gecis = new Intent(DonationRequest.this,  MainPage.class);
        startActivity(List_Ekran_gecis);
        finish();
    }

}