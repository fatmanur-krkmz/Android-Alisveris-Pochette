package com.fnmeinss.pochette;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class Login extends AppCompatActivity {
    private Button buttonGirisYap;
    private EditText editTextName,editTextSurname,editTextnumber;
    private TextView text_kayitol;
    private FirebaseAuth mAuth;
    private String testmail,key;
    private boolean hesapvarmi = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);
    }}
//        buttonGirisYap = findViewById(R.id.saveButton);
//        editTextEposta = findViewById(R.id.editTextEposta);
//        editTextSifre = findViewById(R.id.editTextSifre);
//        text_kayitol = findViewById(R.id.text_kayitol);
//
//
//        buttonGirisYap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String eposta = editTextEposta.getText().toString().toLowerCase();
//                String sifre = editTextSifre.getText().toString();
//                mAuth = FirebaseAuth.getInstance();
//                islemsecimi(true,eposta,sifre);
//
//            }
//        });
//
//        text_kayitol.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent kayitolgecis = new Intent(code_yemekver_giris.this, code_kayitol.class);
//                startActivity(kayitolgecis);
//            }
//        });
//
//    }
//    private void islemsecimi(final boolean girisYap, final String mail, final String sifre){
//
//        mAuth.fetchSignInMethodsForEmail(mail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
//            @Override
//            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
//
//                if(task.isSuccessful()){
//
//                    if(task.getResult() != null && task.getResult().getSignInMethods() != null){
//
//                        boolean email_var;
//
//                        if(task.getResult().getSignInMethods().isEmpty()){
//
//                            //Email bulunamadi
//                            Log.e("hata","Email Bulunamadi");
//
//                            email_var = false;
//
//                        }else{
//
//                            Log.e("hata","Email Bulundu");
//
//                            email_var = true;
//
//                        }
//
//
//                        // İşlemin türüne göre giriş veya üyelik yaptımak
//                        if(girisYap){ // Giriş işlemleri için
//
//                            // Email adresi varsa girişe devam eder
//                            if(email_var){
//
//                                // Mail adresi üyeyse girişe gidilir
//                                girisYaptir(mail, sifre);
//
//                            }else{
//
//                                Toast.makeText(code_yemekver_giris.this,"Mail Adresi Bulunamadi!",Toast.LENGTH_SHORT).show();
//
//                            }
//
//
//                        }
//
//                    }
//
//                }
//
//            }
//
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                if(e.getMessage() != null && e.getMessage().equals("A network error (such as timeout, interrupted connection or unreachable host) has occurred.")){
//
//                    Log.e("hata","Bağlantı sağlanamadı internet bağlantınızı kontrol ediniz!");
//
//                }
//
//            }
//
//        });
//    }
//
//
//    private void girisYaptir(String mail, String sifre){
//
//        mAuth.signInWithEmailAndPassword(mail, sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//
//                if(task.isSuccessful() && task.getResult() != null){
//
//                    if(task.getResult().getUser() != null && task.getResult().getUser().isEmailVerified()){
//
//                        Toast.makeText(code_yemekver_giris.this,"Giriş Başarılı!",Toast.LENGTH_LONG).show();
//                        girisSonrasiIslemler(mail);
//
//                    }else{
//                        Toast.makeText(code_yemekver_giris.this,"Mail adresinize gelen aktivasyonu kontrol ediniz",Toast.LENGTH_LONG).show();
//                    }
//
//                }
//                else {
//                    editTextSifre.setText("");
//                    Toast.makeText(code_yemekver_giris.this, "Şifre yanlış!", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//    }
//
//    public void girisSonrasiIslemler(String mail){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("Isletmeler"); //isletmelerin olduğu dataya bağlanır
//        myRef.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot d : snapshot.getChildren()) {
//                    isletmeler isletme = d.getValue(isletmeler.class);
//                    testmail = isletme.getIsletmemail();
//                    Log.e("s","ss");
//
//                    if (mail.equals(testmail)) {
//                        hesapvarmi = true;
//                    }
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("error","database başlatılamadı");
//            }
//
//        });
//        if (hesapvarmi){
//            Log.e("durum", "hesap bulundu");
//            Intent yemekbulgecis = new Intent(code_yemekver_giris.this, code_yemekver.class);
//            startActivity(yemekbulgecis);
//            finish();
//        }
//        else{
//            Log.e("durum", "hesap bulunamadı");
//            Intent yemekbulgecis = new Intent(code_yemekver_giris.this, code_yemekver_ilkgiris.class);  //Database'de kullanıcı maili kayıtlı değilse,
//            startActivity(yemekbulgecis);                                                                              //bilgilerini ekletmek için ilk giriş sayfasına gidilir
//            finish();
//        }
//
//    }
//
//
//
//
//}