package com.example.nishanth.mymessengerapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    EditText UsernameET , PasswordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        UsernameET = (EditText) findViewById(R.id.editUsername);
        PasswordET = (EditText) findViewById(R.id.editpassword);
    }
    public void onLogin(View view) {
        String username = UsernameET.getText().toString();
        String password = PasswordET.getText().toString();
        String type = "login";
        UserAuthentication userAuthentication = new UserAuthentication(this);
        userAuthentication.execute("login", username, password);
    }
    public void OpenReg(View view)
    {
        //startActivity(new Intent(this, RegisterActivity.class));

    }
}