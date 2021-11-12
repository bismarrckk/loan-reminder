package com.example.loansbookkeeping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.recyclerview.widget.RecyclerView;

public class MyAdapterReminder extends RecyclerView.Adapter<MyHolder> {
    Context con;
    ArrayList<Loan> loans;
    DatabaseHelper myDb;
    public MyAdapterReminder(Context ctx, ArrayList<Loan> loans)
    {
        //ASSIGN THEM LOCALLY
        this.con=ctx;
        this.loans=loans;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //VIEW OBJ FROM XML
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.model_reminder,null);

        //holder
        MyHolder holder=new MyHolder(v);
        return holder;
    }

    //BIND DATA TO VIEWS
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        myDb = new DatabaseHelper(con);
        String fullname = loans.get(position).getFullname();
        String loan_balance = loans.get(position).getLoanAmount();
        long due_date = Long.parseLong(loans.get(position).getDue_date());

        Date date = new Date(due_date * 1000L);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String due_date_format = formatter.format(date);


            holder.nameTxt.setText(fullname);
        holder.posTxt.setText(String.format("Ksh.%s  due on %s", loan_balance, due_date_format));



            //HANDLE ITEM CLICKS
            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    //OPEN DETAIL ACTIVITY
                    //PASS DATA

                    //CREATE INTENT


                }
            });
        }

    @Override
    public int getItemCount () {
        return loans.size();
    }


}
