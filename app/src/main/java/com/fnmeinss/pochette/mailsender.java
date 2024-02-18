package com.fnmeinss.pochette;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class mailsender extends AppCompatActivity {

    public Intent mailsend(String mail, String productname, String quaintity){

        Intent intent;
        intent = new Intent( Intent.ACTION_SEND );
        intent.setType("text/html");
        String[] to = {mail};
        intent.putExtra( Intent.EXTRA_EMAIL, to);
        intent.putExtra( Intent.EXTRA_SUBJECT, "Your product in Pochette" );
        intent.putExtra( Intent.EXTRA_TEXT, "I want to get \""+quaintity+"\" of your \""+productname+ "\" product in the Pochette app. Is the product available?" );
        Intent chooser=Intent.createChooser( intent, "Send mail" );
        return chooser;
    }
}
