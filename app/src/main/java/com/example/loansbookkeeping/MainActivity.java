package com.example.loansbookkeeping;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    Context ctx;
    TextView textItemCount,textItemCount2;
    int mCartItemCount,mCartItemCount2;
    RecyclerView rv;
    MyAdapter adapter;
    ArrayList<Loan> loans=new ArrayList<>();
    ArrayList<Loan> loans2=new ArrayList<>();
    private static  final  int MY_PERMISSIONS_REQUEST_READ_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, MY_PERMISSIONS_REQUEST_READ_SMS);
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //recycler
        rv= findViewById(R.id.myRecycler);

        //SET ITS PROPS
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        //ADAPTER
        adapter=new MyAdapter(this,loans);

        retrieve();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        final MenuItem menuItem2 = menu.findItem(R.id.action_cart2);

        View actionView = menuItem.getActionView();
        textItemCount =  actionView.findViewById(R.id.message_count);

        setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReminderActivity.class));
            }
        });

        View actionView2 = menuItem2.getActionView();
        textItemCount2 = actionView2.findViewById(R.id.message_count2);

        setupBadge2();
        actionView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Confirm.class));
            }
        });

        return true;
    }

    private void setupBadge() {
        Date date = new Date();
        long todaysdate = date.getTime() / 1000;
        DatabaseHelper myDb=new DatabaseHelper(this);
        mCartItemCount = myDb.setReminder(todaysdate);
        if (textItemCount != null) {
            if (mCartItemCount == 0) {
                if (textItemCount.getVisibility() != View.GONE) {
                    textItemCount.setVisibility(View.GONE);

                }
            } else {
                textItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textItemCount.getVisibility() != View.VISIBLE) {
                    textItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    private void setupBadge2() {
        mCartItemCount2 = countMessages();
        if (textItemCount2 != null) {
            if (mCartItemCount2 == 0) {
                if (textItemCount2.getVisibility() != View.GONE) {
                    textItemCount2.setVisibility(View.GONE);

                }
            } else {
                textItemCount2.setText(String.valueOf(Math.min(mCartItemCount2, 99)));
                if (textItemCount2.getVisibility() != View.VISIBLE) {
                    textItemCount2.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    //sms permission
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults){
        switch(requestCode){
            case MY_PERMISSIONS_REQUEST_READ_SMS:
            {
                if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Thanks for permitting", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this,"Go to settings to grant sms permission ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    //RETRIEVE
    private void retrieve()
    {
        DatabaseHelper myDb=new DatabaseHelper(this);

        //OPEN DB
        loans.clear();
        //SELECT
        Cursor c=myDb.getConfirmedLoans();

        //LOOP THROUGH THE DATA ADDING TO ARRAY LIST
        if (c.moveToNext()) {
            do {
                int loan_id = Integer.parseInt(c.getString(0));
                String mpesa = c.getString(1);
                String outstanding_amount = c.getString(2);
                String status = c.getString(3);
                String customername = c.getString(4);
                String phone = c.getString(5);
                String amount_disbursed = c.getString(6);
                String due_date = c.getString(7);
                String issue_date = c.getString(8);
                String rate = c.getString(9);

                //CREATE Loan
                Loan l = new Loan(loan_id, mpesa, outstanding_amount, status, customername, phone, amount_disbursed, due_date, issue_date, rate);

                //ADD TO loans
                loans.add(l);
            } while (c.moveToNext());
        }
        c.close();
        //SET ADAPTER TO RV
        if(!(loans.size()<1))
        {
            rv.setAdapter(adapter);
        }

    }

    private int countMessages()
    {
        //OPEN DB
        loans2.clear();
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
                        loans2.add(l);
                    }
                }

            } while (cur.moveToNext());
        }
        cur.close();
        //SET ADAPTER TO RV
        return loans2.size();
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieve();
    }


}
