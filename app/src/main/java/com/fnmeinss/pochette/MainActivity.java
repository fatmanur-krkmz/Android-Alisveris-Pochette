package com.fnmeinss.pochette;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView textLinkPassword;
    private TextView signupTextView;
    private Button loginButton;
    private Button sellerLoginButton;
    private EditText eMailText, passwordText;
    private FirebaseAuth mAuth;
    private String testmail, kayıtlimail, name, surname;
    private int databaseusedtimes = 0;
    private boolean isaccountexist = false,signupdangeldi;
    private String gelenbuton;
    private ProgressBar progressBar;
    private LinearLayout biglinear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//Full-Screen
        textLinkPassword = findViewById(R.id.textLinkPassword);
        signupTextView = findViewById(R.id.signupTextView);
        loginButton = findViewById(R.id.loginButton);
        sellerLoginButton = findViewById(R.id.sellerLoginButton);
        eMailText = findViewById(R.id.eMailText);
        passwordText = findViewById(R.id.passwordText);
        progressBar = findViewById(R.id.progressBar);
        biglinear = findViewById(R.id.big_linear);
        mAuth = FirebaseAuth.getInstance();

        SharedPreferences sp = getSharedPreferences("aktifkullanici", Context.MODE_PRIVATE);
        String currentUser = sp.getString("nameandsurname","");

        if(!currentUser.equals("")){
            progressBar.setVisibility(View.VISIBLE);
            biglinear.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this,"Giriş yapılıyor",Toast.LENGTH_SHORT).show();
        }


        signupdangeldi = getIntent().getBooleanExtra("signupdangeldi", false);//yeni

        if(!signupdangeldi){
            FirebaseUser user =mAuth.getCurrentUser(); //bir defa giriş yaptıktan sonra direkt ana sayfaya girer
            if(user!=null){
                kayıtlimail = user.getEmail();//yeni
                if(kayıtlimail.equals("pochettetemporary@gmail.com")){
                    Intent intent=new Intent(this,Admin.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    String loginType = sp.getString("loginType", "");
                    gelenbuton = loginType;
                    afterLogIn(kayıtlimail);//yeni
                }
            }
        }
        sellerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("s",eMailText.getText().toString().toLowerCase().trim());
                if("a".equals(eMailText.getText().toString().toLowerCase().trim())){
                    Intent intent=new Intent(MainActivity.this,Admin.class);
                    startActivity(intent);
                    finish();
                }
                else {
                String email = eMailText.getText().toString().toLowerCase();
                String password = passwordText.getText().toString();
                signIn(true, email, password);
                gelenbuton="Seller";}
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = eMailText.getText().toString().toLowerCase();
                String password = passwordText.getText().toString();
                if (email.equals(""))
                    eMailText.setError(" 'Email' cannot be left blank");
                else if (password.equals(""))
                    passwordText.setError(" 'Password' cannot be left blank");
                else{
                signIn(true, email, password);
                gelenbuton="Customer";}
            }

        });

        textLinkPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PasswordResett.class);
                startActivity(intent);

            }
        });
        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);

            }
        });


    }

    private void signIn(final boolean LogIn, final String mail, final String password) {

        mAuth.fetchSignInMethodsForEmail(mail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                if (task.isSuccessful()) {

                    if (task.getResult() != null && task.getResult().getSignInMethods() != null) {

                        boolean email_exist;

                        if (task.getResult().getSignInMethods().isEmpty()) {

                            Log.e("Error", "Email can not found.");
                            email_exist = false;

                        } else {

                            email_exist = true;

                        }



                        if (LogIn) {

                            if (email_exist) {

                                logIn(mail, password);

                            } else {

                                Toast.makeText(MainActivity.this, "Mail Adress can not be found!", Toast.LENGTH_SHORT).show();

                            }


                        }

                    }

                }

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

            }

        });
    }


    private void logIn(String mail, String password) {

        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful() && task.getResult() != null) {

                    if (task.getResult().getUser() != null && task.getResult().getUser().isEmailVerified()) {
                        Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_LONG).show();
                        afterLogIn(mail);

                    } else {
                        Toast.makeText(MainActivity.this, "You should check your mailbox", Toast.LENGTH_LONG).show();
                    }

                } else {
                    passwordText.setText("");
                    Toast.makeText(MainActivity.this, "Password incorrect", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void afterLogIn(String mail) {


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Person").child("Users");
            myRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    databaseusedtimes++;
                    if(mail.equals("pochettetemporary@gmail.com")){
                        isaccountexist = true;
                    }
                    for (DataSnapshot d : snapshot.getChildren()) {
                        Users user = d.getValue(Users.class);
                        testmail = user.getuserEmail();
                        //
                        if (mail.equals(testmail)) {
                            name = user.getuserName();
                            surname = user.getuserSurname();
                            isaccountexist = true;
                        }
                    }

                    isAccountExist();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("error", "database başlatılamadı");
                }

            });
        }


    public void isAccountExist() {
        if (databaseusedtimes > 0) {
            if (isaccountexist) {
                if("Seller".equals(gelenbuton)){

                    if(eMailText.getText().toString().trim().toLowerCase(Locale.ROOT).equals("pochettetemporary@gmail.com")){
                        Intent intent=new Intent(MainActivity.this,Admin.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                    Log.e("durum", "hesap bulundu");
                    SharedPreferences sp = getSharedPreferences("aktifkullanici", Context.MODE_PRIVATE);//local saves 01.03
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("nameandsurname",name+" "+surname);
                    editor.putString("loginType","Seller");
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this, SellerMainPage.class);
                    startActivity(intent);
                    finish();
                    }

                }
                else {

                    if (eMailText.getText().toString().trim().toLowerCase(Locale.ROOT).equals("pochettetemporary@gmail.com"))
                    {
                        Intent intent=new Intent(MainActivity.this,Admin.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                Log.e("durum", "hesap bulundu");
                SharedPreferences sp = getSharedPreferences("aktifkullanici", Context.MODE_PRIVATE);//local saves 01.03
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("nameandsurname",name+" "+surname);
                editor.putString("loginType","Customer");
                editor.commit();
                Intent intent = new Intent(MainActivity.this, MainPage.class);
                startActivity(intent);
                finish();
                    }
                }
            } else {
                Log.e("durum", "hesap bulunamadı");
                Intent intent = new Intent(MainActivity.this, FirstLogin.class);  //Database'de kullanıcı maili kayıtlı değilse,
                startActivity(intent);                                                                              //bilgilerini ekletmek için ilk giriş sayfasına gidilir
                finish();
            }
        }
    }
    @Override
    public void onBackPressed() {
        System.exit(0);
    }
}
