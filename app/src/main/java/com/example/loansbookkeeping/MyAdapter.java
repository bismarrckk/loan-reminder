package com.example.loansbookkeeping;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;


public class MyAdapter extends RecyclerView.Adapter<MyHolder> {
    Context c;
    ArrayList<Loan> loans;
    DatabaseHelper myDb;
    public MyAdapter(Context ctx, ArrayList<Loan> loans)
    {
        //ASSIGN THEM LOCALLY
        this.c=ctx;
        this.loans=loans;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //VIEW OBJ FROM XML
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.model,null);

        //holder
        MyHolder holder=new MyHolder(v);
        return holder;
    }

    //BIND DATA TO VIEWS
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        myDb=new DatabaseHelper(c);
           holder.posTxt.setText(String.format("Loan taken is Ksh.%s", loans.get(position).getAmount_disbursed()));
           holder.nameTxt.setText(loans.get(position).getFullname());


        //HANDLE ITEM CLICKS
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                //OPEN DETAIL ACTIVITY
                //PASS DATA

                //CREATE INTENT
                Intent i=new Intent(c,DetailActivity.class);

                //LOAD DATA
                i.putExtra("ID",loans.get(pos).getLoanId());
                i.putExtra("NAME",loans.get(pos).getFullname());
                i.putExtra("PHONE",loans.get(pos).getPhoneNumber());
                i.putExtra("AMOUNT",loans.get(pos).getLoanAmount());
                i.putExtra("MPESA",loans.get(pos).getMpesaCode());
                i.putExtra("DISBURSED",loans.get(pos).getAmount_disbursed());
                i.putExtra("DUEDATE",loans.get(pos).getDue_date());
                i.putExtra("ISSUEDATE",loans.get(pos).getIssue_date());
                i.putExtra("STATUS",loans.get(pos).getLoanStatus());
                i.putExtra("RATE",loans.get(pos).getRate());


                //START ACTIVITY
                c.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return loans.size();
    }


}
