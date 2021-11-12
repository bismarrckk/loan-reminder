package com.example.loansbookkeeping;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.StringTokenizer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Confirm extends AppCompatActivity {


    RecyclerView rv;
    MyAdapterConfirm adapter;
    final ArrayList<Loan> loans=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        //recycler
        rv= findViewById(R.id.myRecyclerConfirm);

        //SET ITS PROPS
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        //ADAPTER
        adapter=new MyAdapterConfirm(this,loans);

        retrieve();

    }

    //RETRIEVE
    private void retrieve()
    {
        //OPEN DB
        loans.clear();
        DatabaseHelper myDb = new DatabaseHelper(this);
        //SELECT
        String phoneNumber = "MPESA";
        String sent = "sent";
        String todaydate="1600201600000";
        String datesent="date > '"+todaydate+"'";
        String sentmoney="body LIKE '%"+sent+"%'";
        String phone_sms = "address='"+ phoneNumber + "'";
        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = getContentResolver().query(uriSMSURI, null,  sentmoney +" AND "+phone_sms  +" AND "+datesent, null,null);

        String msg_body;
        String msg_id;

        if (cur.moveToNext()) {
            do {
                msg_id = cur.getString(cur.getColumnIndexOrThrow("_id"));
                msg_body = cur.getString(cur.getColumnIndexOrThrow("body"));

                //CREATE Loan
                int msgid = Integer.parseInt(msg_id);
                final StringTokenizer tokenizer = new StringTokenizer(msg_body, " ");
                final int numberOfTokens = tokenizer.countTokens();
                final String[] splitArr = new String[numberOfTokens];

                splitArr[0] = tokenizer.nextToken();
                splitArr[1] = tokenizer.nextToken();
                splitArr[2] = tokenizer.nextToken();
                splitArr[3] = tokenizer.nextToken();
                splitArr[5] = tokenizer.nextToken();
                splitArr[6] = tokenizer.nextToken();
                splitArr[7] = tokenizer.nextToken();
                splitArr[8] = tokenizer.nextToken();
                splitArr[9] = tokenizer.nextToken();

                String onFor = splitArr[9];
                if (onFor.equals("on")) {
                    boolean check_message = myDb.checkMessageId(msgid);
                    if (!check_message) {
                        Loan l = new Loan(msg_id, msg_body);
                        //ADD TO loans
                        loans.add(l);
                    }
                }

            } while (cur.moveToNext());
        }
        cur.close();
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
