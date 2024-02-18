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
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;


public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button signupButton;
    private EditText editTextMail;
    private EditText editTextPassword;
    private EditText editTextPasswordAgain;
    private ProgressBar progressBar;
    private TableLayout bigtable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();
        signupButton = findViewById(R.id.signupButton);
        editTextMail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordAgain = findViewById(R.id.editTextPasswordAgain);
        bigtable =findViewById(R.id.big_tableLayout);
        progressBar = findViewById(R.id.progressBar);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mailAdress = editTextMail.getText().toString().toLowerCase();
                String password = editTextPassword.getText().toString();
                String passwordAgain = editTextPasswordAgain.getText().toString();


               if (password.length() < 6) {
                   editTextPassword.setError("Passwords size must be greater than or equal to 6");
                   editTextPassword.setText("");
               }
               else{
                if (password.equals(passwordAgain)) {
                    signUp(false, mailAdress, password);
                } else{
                    editTextPasswordAgain.setError("Passwords are different");
                    editTextPasswordAgain.setText("");
            }}}
        });


    }


    private void signUp(final boolean LogIn, final String mail, final String password) {
        //LogIn == True, to Log in methods
        //LogIn == false, to Sign in methods
        mAuth.fetchSignInMethodsForEmail(mail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {


                if (task.isSuccessful()) {

                    if (task.getResult() != null && task.getResult().getSignInMethods() != null) {

                        boolean email_exist;

                        if (task.getResult().getSignInMethods().isEmpty()) {

                            Log.e("Error", "Email can not be found!");

                            email_exist = false;

                        } else {

                            Log.e("Error", "Email found.");

                            email_exist = true;

                        }



                        if (!LogIn) {

                            if (email_exist) {
                                Toast.makeText(SignUpActivity.this, "This mail already using", Toast.LENGTH_LONG).show();

                            } else {
                                bigtable.setVisibility(View.INVISIBLE);
                                signupButton.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.VISIBLE);
                                doMembership(mail, password);

                            }

                        }

                    }

                }

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

               /* if (e.getMessage() != null && e.getMessage().equals("A network error (such as timeout, interrupted connection or unreachable host) has occurred.")) {

                    Log.e("Error", "Connection failed, check your internet connection!");

                }*/
                Toast.makeText(SignUpActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

            }

        });
    }

    private void doMembership(String mail, String password) {

        mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful() && task.getResult() != null) {

                    // Aktivasyon linki yollama
                    if (task.getResult().getUser() != null)
                        task.getResult().getUser().sendEmailVerification();

                    Toast.makeText(SignUpActivity.this, "Done! You are one of us now!", Toast.LENGTH_LONG).show();

                    afterLogin();

                }
            }
        });


    }


    public void afterLogin() {
        mAuth.signOut();
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.putExtra("signupdangeldi",true);
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        Intent List_Ekran_gecis = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(List_Ekran_gecis);
        finish();
    }

}
