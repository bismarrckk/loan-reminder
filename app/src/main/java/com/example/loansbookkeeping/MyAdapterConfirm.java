package com.example.loansbookkeeping;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import androidx.recyclerview.widget.RecyclerView;


public class MyAdapterConfirm extends RecyclerView.Adapter<MyHolder> {
    Context con;
    ArrayList<Loan> loans;
    DatabaseHelper myDb;
    public MyAdapterConfirm(Context ctx, ArrayList<Loan> loans)
    {
        //ASSIGN THEM LOCALLY
        this.con=ctx;
        this.loans=loans;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //VIEW OBJ FROM XML
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.model_confirm,null);

        //holder
        MyHolder holder=new MyHolder(v);
        return holder;
    }

    //BIND DATA TO VIEWS
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        myDb = new DatabaseHelper(con);
        final String messageBody = loans.get(position).getMsg_Body();
        final StringTokenizer tokenizer = new StringTokenizer(messageBody, " ");
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



        holder.nameTxt.setText(loans.get(position).getMsg_Body());

        final int loan_id = Integer.parseInt(loans.get(position).getMsg_Id());


        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete row from database
                String mpesacode = splitArr[0];
                String amount = splitArr[2];
                String fullname = splitArr[6] + " " + splitArr[7];
                int phone_number = Integer.parseInt(splitArr[8]);

                StringTokenizer tokenizer2 = new StringTokenizer(amount, ".");
                int numberOfTokens2 = tokenizer2.countTokens();

                String[] splitArray = new String[numberOfTokens2];
                splitArray[0] = tokenizer2.nextToken();
                splitArray[1] = tokenizer2.nextToken();
                String decimal_amount = splitArray[1];
                String[] part = amount.split("(?<=\\D)(?=\\d)");
                String actual_amount = (part[1]);
                String final_amount = actual_amount + decimal_amount;

                int period = 30;
                double rate = 0.1;

                Date date = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, period);
                Date currentDatePlusOne = c.getTime();

                long currentdate = date.getTime() / 1000;
                long duedate = currentDatePlusOne.getTime() / 1000;
                boolean check_customer= myDb.checkCustomerPhone(phone_number);
                int confirmed=1;
                if(!check_customer){
                    myDb.insertCustomerInfo(fullname, phone_number);
                    myDb.insertDeletedLoans(phone_number,confirmed,loan_id,final_amount, period, rate, currentdate, mpesacode, duedate);

                }
                else {
                    myDb.insertDeletedLoans(phone_number,confirmed,loan_id,final_amount, period, rate, currentdate, mpesacode, duedate);

                }
                ((Activity) con).finish();
                con.startActivity(((Activity) con).getIntent());
            }
        });
        holder.imageconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insert into database

                String mpesacode = splitArr[0];
                String amount = splitArr[2];
                String fullname = splitArr[6] + " " + splitArr[7];
                int phone_number = Integer.parseInt(splitArr[8]);


                String part = amount.replaceAll("[^0-9]","");
                double final_amount = Double.parseDouble(part)*0.01;

                int period = 30;
                double rate = 0.1;


                Date date = new Date();

                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, period);
                Date currentDatePlusOne = c.getTime();



                long currentdate = date.getTime() / 1000;
                long duedate = currentDatePlusOne.getTime() / 1000;
                boolean check_customer= myDb.checkCustomerPhone(phone_number);
                if(!check_customer){
                    myDb.insertCustomerInfo(fullname, phone_number);
                }
                myDb.insertLoans(phone_number,loan_id,final_amount, period, rate, currentdate, mpesacode, duedate);

                //refresh the activity page.
                ((Activity) con).finish();
                con.startActivity(((Activity) con).getIntent());

            }
        });


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
