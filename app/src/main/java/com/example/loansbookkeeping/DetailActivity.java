package com.example.loansbookkeeping;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {
    TextView customer_name,phoneNumber,outStanding,disbursed,mpesa_code,date_due,loan_status,date_issued,rate;
    EditText amountTxt,mpesaTxt,editInterest,editPeriod;

    Button updateBtn,updateTerms;
    DatabaseHelper myDb;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        myDb=new DatabaseHelper(this);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //RECEIVE DATA FROM MAIN ACTIVITY
        Intent i=getIntent();

        final String name=i.getExtras().getString("NAME");
        final String interest_rate=i.getExtras().getString("RATE");
        final String phone=i.getExtras().getString("PHONE");
        final String amount=i.getExtras().getString("AMOUNT");
        final String status=i.getExtras().getString("STATUS");
        final String amount_disbursed=i.getExtras().getString("DISBURSED");
        final long loan_due_date= Long.parseLong(i.getExtras().getString("DUEDATE"));
        final long loan_issue_date= Long.parseLong(i.getExtras().getString("ISSUEDATE"));
        final String reference_code=i.getExtras().getString("MPESA");

        final int id=i.getExtras().getInt("ID");

        updateBtn= findViewById(R.id.updateBtn);
        updateTerms= findViewById(R.id.updateTerms);

        amountTxt= findViewById(R.id.amountTxt);
        mpesaTxt= findViewById(R.id.mpesaTxt);
        editInterest =  findViewById(R.id.editInterest);
        editPeriod=  findViewById(R.id.editPeriod);

        customer_name=findViewById(R.id.customer_name);
        phoneNumber=findViewById(R.id.phoneNumber);
        outStanding=findViewById(R.id.outStanding);
        disbursed=findViewById(R.id.disbursed);
        mpesa_code=findViewById(R.id.reference_code);
        date_due=findViewById(R.id.due_date);
        date_issued=findViewById(R.id.issue_date);
        loan_status=findViewById(R.id.status);
        rate=findViewById(R.id.interest);

        Date date = new Date(loan_due_date * 1000L);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String due_date_format = formatter.format(date);

        Date dateissued = new Date(loan_issue_date * 1000L);

        String issue_date_format = formatter.format(dateissued);

        //ASSIGN DATA TO THOSE VIEWS
        customer_name.setText(name);
        phoneNumber.setText("0"+phone);
        outStanding.setText("Ksh."+amount);
        disbursed.setText("Ksh."+amount_disbursed);
        mpesa_code.setText(reference_code);
        date_due.setText(due_date_format);
        date_issued.setText(issue_date_format);
        loan_status.setText(status);
        rate.setText(interest_rate );

        //update loan payments
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((amountTxt.getText().toString().equals("")) || (mpesaTxt.getText().toString().equals(""))) {
                    Snackbar.make(amountTxt, "All fields must be filled", Snackbar.LENGTH_SHORT).show();
                } else {
                    long result = myDb.updateLoanPayment(id, amountTxt.getText().toString(), mpesaTxt.getText().toString());
                    if (result < 0) {

                        Snackbar.make(amountTxt, "Unable to Add Payment", Snackbar.LENGTH_SHORT).show();
                    } else {
                        amountTxt.setText("");
                        mpesaTxt.setText("");
                        Toast.makeText(DetailActivity.this, "Payment updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DetailActivity.this, MainActivity.class));
                    }
                }
            }
        });

        //update loan terms
        updateTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((editInterest.getText().toString().equals("")) || (editPeriod.getText().toString().equals(""))) {
                    Snackbar.make(amountTxt, "All fields must be filled", Snackbar.LENGTH_SHORT).show();
                } else {
                    double interest= Double.parseDouble(editInterest.getText().toString())/100;
                    int period = Integer.parseInt((editPeriod.getText().toString()));
                    long seconds= period * 86400;
                    long final_due_date=seconds + loan_issue_date;
                    long result = myDb.updateLoanTerms(id, interest,period, final_due_date);
                    if (result < 0) {

                        Snackbar.make(amountTxt, "Unable to Update Loan Terms", Snackbar.LENGTH_SHORT).show();
                    } else {
                        editPeriod.setText("");
                        editInterest.setText("");
                        Toast.makeText(DetailActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DetailActivity.this, MainActivity.class));


                    }
                }
            }
        });

        //DELETE
        //update
        /*deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              delete(id);
            }
        });*/


    }

}
