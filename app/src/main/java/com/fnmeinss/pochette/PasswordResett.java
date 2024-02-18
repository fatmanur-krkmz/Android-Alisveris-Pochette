package com.fnmeinss.pochette;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResett extends AppCompatActivity {
    private Button resetbutton;
    private EditText eMailText;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_resett);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//Tam ekran

        resetbutton = findViewById(R.id.resetMailButton);
        eMailText = findViewById(R.id.eMailText);
        mAuth = FirebaseAuth.getInstance();


        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(eMailText.getText().toString().isEmpty()){

                    Toast.makeText(PasswordResett.this,"Bir Mail Adresi Giriniz!",Toast.LENGTH_SHORT).show();

                }else{
                    String email = eMailText.getText().toString();
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                Toast.makeText(PasswordResett.this,"Sıfırlama Maili Gönderildi!",Toast.LENGTH_SHORT).show();

                            }else{
                                Log.e("hata",task.getException().toString());

                            }

                        }
                    });

                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent List_Ekran_gecis = new Intent(PasswordResett.this, MainActivity.class);
        startActivity(List_Ekran_gecis);
        finish();
    }
}