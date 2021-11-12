package com.example.loansbookkeeping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    EditText firstname;
    EditText lastname;
    EditText phonenumber;
    EditText password;
    EditText password2;
    Button btnRegister;
    TextView btnalreadyHave;
    DatabaseHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstname=findViewById(R.id.firstname);
        lastname=findViewById(R.id.lastname);
        phonenumber=findViewById(R.id.phonenumber);
        password=findViewById(R.id.password);
        password2=findViewById(R.id.password2);
        btnRegister=findViewById(R.id.btnRegister);
        btnalreadyHave=findViewById(R.id.alreadyhaveaccount_view);
        myDb = new DatabaseHelper(this);

        viewloginForm();
        adduserData();

    }
    public void viewloginForm(){
        btnalreadyHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
            }

        });
    }
    public void adduserData(){
        btnRegister.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Date date = new Date();
                        long currentdate = date.getTime() / 1000;
                        boolean checkphone=myDb.checkUserPhone(phonenumber.getText().toString());
                        if ((firstname.getText().toString().equals("")) || (lastname.getText().toString().equals("")) || (phonenumber.getText().toString().equals("")) || (password.getText().toString().equals(""))) {
                            Toast.makeText(Register.this, "All fields must be filled", Toast.LENGTH_LONG).show();
                        }
                        else if(!(password.getText().toString().equals(password2.getText().toString()))){
                            password.setText("");
                            password2.setText("");
                            Toast.makeText(Register.this, "Passwords do not match,Please try again", Toast.LENGTH_LONG).show();
                        }
                        else if(checkphone){
                            Toast.makeText(Register.this, "The phone number provided already exists", Toast.LENGTH_LONG).show();
                        }
                        else {
                            boolean isInserted = myDb.insertuserData(firstname.getText().toString(), lastname.getText().toString(), phonenumber.getText().toString(), password.getText().toString(), currentdate);
                            if (isInserted) {
                                firstname.setText(null);
                                lastname.setText(null);
                                phonenumber.setText(null);
                                password.setText(null);
                                Toast.makeText(Register.this, "Registration Successful:", Toast.LENGTH_LONG).show();
                            }
                            else
                                Toast.makeText(Register.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
}