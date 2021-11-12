package com.example.loansbookkeeping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    EditText login_phonenumber;
    EditText login_password;
    Button btnLogin;
    TextView btnSignup;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_phonenumber = findViewById(R.id.phonenumber);
        login_password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnRegister);
        btnSignup = findViewById(R.id.textViewsignup);
        myDb = new DatabaseHelper(this);

        viewregisterForm();
        login();

    }

    public void viewregisterForm() {
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }
    public void login() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((login_phonenumber.getText().toString().equals("")) || (login_password.getText().toString().equals(""))) {
                    Toast.makeText(Login.this, "Incorrect username/password", Toast.LENGTH_SHORT).show();
                }
                else {
                  boolean check= myDb.getCredentials(login_phonenumber.getText().toString(),login_password.getText().toString());
                    if(check){
                        login_phonenumber.setText(null);
                        login_password.setText(null);
                       startActivity(new Intent(Login.this, MainActivity.class));
                   }
                   else{

                       login_password.setText(null);

                        Toast.makeText(Login.this, "Incorrect username/password", Toast.LENGTH_SHORT).show();
                   }
                }
            }

        });

    }

}