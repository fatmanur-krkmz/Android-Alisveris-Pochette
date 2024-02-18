package com.fnmeinss.pochette;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.content.Intent;
import android.widget.TextView;

public class FirstLogin extends AppCompatActivity {
    private EditText editTextName, editTextSurname, editTextNumber, editTextAdress;
    private Button saveButton;
    private FirebaseAuth mAuth;
    private String mail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        saveButton = findViewById(R.id.saveButton);
        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextAdress = findViewById(R.id.editTextadress);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Person").child("Users");
        mAuth = FirebaseAuth.getInstance();
        mail = mAuth.getCurrentUser().getEmail();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String surname = editTextSurname.getText().toString().trim();
                String number = editTextNumber.getText().toString().trim();
                String adress = editTextAdress.getText().toString().trim();

                if (name.equals(""))
                    editTextName.setError(" 'Name' cannot be left blank");
                else if (surname.equals(""))
                    editTextName.setError(" 'Surname' cannot be left blank");
                else if (number.equals(""))
                    editTextName.setError(" 'Number' cannot be left blank");
                else if (adress.equals(""))
                    editTextName.setError(" 'Adress' cannot be left blank");
                else {
                    Users user = new Users(mail, name, surname, number, adress);
                    myRef.push().setValue(user);
                    Toast.makeText(getApplicationContext(), "Registration completed.", Toast.LENGTH_SHORT).show();

                    SharedPreferences sp = getSharedPreferences("aktifkullanici", Context.MODE_PRIVATE);//local saves 01.03
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("nameandsurname",name+" "+surname);
                    editor.commit();

                    Intent intent = new Intent(FirstLogin.this, MainPage.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent List_Ekran_gecis = new Intent(FirstLogin.this, MainActivity.class);
        startActivity(List_Ekran_gecis);
        finish();
    }
}