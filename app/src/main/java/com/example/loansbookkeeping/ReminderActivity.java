package com.example.loansbookkeeping;


import android.database.Cursor;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReminderActivity extends AppCompatActivity {

    RecyclerView rv;
    MyAdapterReminder adapter;
    ArrayList<Loan> loans=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        //recycler
        rv= findViewById(R.id.myRecyclerReminder);
        //SET ITS PROPS
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        //ADAPTER
        adapter=new MyAdapterReminder(this,loans);

        retrieve();

    }

    //RETRIEVE
    private void retrieve()
    {
        Date date = new Date();
        long todaysdate = date.getTime() / 1000;
        DatabaseHelper myDb=new DatabaseHelper(this);
        //OPEN DB
        loans.clear();
        //SELECT
        Cursor c=myDb.getReminderLoans(todaysdate);

        //LOOP THROUGH THE DATA ADDING TO ARRAY LIST
        while (c.moveToNext())
        {
            int loan_id = Integer.parseInt(c.getString(0));
            String mpesa = c.getString(1);
            String outstanding_amount = c.getString(2);
            String status = c.getString(3);
            String customername = c.getString(4);
            String phone = c.getString(5);
            String amount_disbursed = c.getString(6);
            String due_date = c.getString(7);
            String issue_date = c.getString(8);
            String rate =c.getString(9);

            //CREATE Loan
            Loan l=new Loan(loan_id,mpesa,outstanding_amount,status,customername,phone,amount_disbursed,due_date,issue_date,rate);

            //ADD TO loans
            loans.add(l);
        }
        //SET ADAPTER TO RV
        if(!(loans.size()<1))
        {
            rv.setAdapter(adapter);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieve();
    }


}
