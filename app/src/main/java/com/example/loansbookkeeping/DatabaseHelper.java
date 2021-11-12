package com.example.loansbookkeeping;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Loans.db";

    public static final String user_info = "user_info";
    public static final String user_fullname = "user_fullname";
    public static final String user_phone_number = "user_phone_number";
    public static final String user_password = "user_password";
    public static final String user_log = "user_log";

    public static final String customer_info = "customer_info";
    public static final String customer_info_id = "customer_info_id";
    public static final String customer_name = "customer_name";
    public static final String customer_phone_number = "customer_phone_number";

    public static final String loan = "loan";
    public static final String loan_id = "loan_id";
    public static final String loan_customer_phone_number = "loan_customer_phone_number";
    public static final String loan_amount = "loan_amount";
    public static final String loan_period = "loan_period";
    public static final String loan_interest = "loan_interest";
    public static final String loan_issue_date = "loan_issue_date";
    public static final String loan_mpesa_code = "loan_mpesa_code";
    public static final String loan_amount_due = "loan_amount_due";
    public static final String loan_due_date = "loan_due_date";
    public static final String loan_status = "loan_status";
    public static final String loan_confirmed = "loan_confirmed";
    public static final String loan_message_id="loan_message_id";

    public static final String payments = "payments";
    public static final String payment_id = "payment_id";
    public static final String payment_loan_id = "payment_loan_id";
    public static final String payment_amount = "payment_amount";
    public static final String payment_reference_code = "payment_reference_code";
    public static final String payment_date = "payment_date";

    public static final String penalties = "penalties";
    public static final String penalty_id = "penalty_id";
    public static final String penalty_loan_id = "penalty_loan_id";
    public static final String penalty_amount = "penalty_amount";
    public static final String penalty_reg_date = "penalty_reg_date";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table  " + user_info + " " +
                "(user_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "user_fullname TEXT NOT NULL,\n" +
                "user_phone_number INTEGER NOT NULL,\n" +
                "user_password TEXT NOT NULL,\n" +
                "user_log INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE " + customer_info + " (\n" +
                "  customer_info_id INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  customer_name TEXT NOT NULL,\n" +
                "  customer_phone_number INTEGER NOT NULL\n" +
                "  )");

        db.execSQL("CREATE TABLE " + loan + " (\n" +
                "  loan_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  loan_customer_phone_number INTEGER NOT NULL,\n" +
                "  loan_amount decimal(10,2) NOT NULL,\n" +
                "  loan_period INTEGER NOT NULL,\n" +
                "  loan_interest decimal(4,2) NOT NULL,\n" +
                "  loan_issue_date INTEGER NOT NULL,\n" +
                "  loan_mpesa_code TEXT NOT NULL,\n" +
                "  loan_amount_due decimal(10,2) ,\n" +
                "  loan_due_date TEXT,\n" +
                "  loan_message_id INTEGER NOT NULL,\n" +
                "  loan_confirmed INTEGER NOT NULL DEFAULT '0',\n" +
                "  loan_status TEXT NOT NULL DEFAULT 'pending'\n" +

                ")");

        db.execSQL("CREATE TABLE " + payments + " (\n" +
                "  payment_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  payment_loan_id INTEGER NOT NULL,\n" +
                "  payment_amount decimal(10,2) NOT NULL,\n" +
                "  payment_reference_code TEXT NOT NULL,\n" +
                "  payment_date datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  CONSTRAINT payments_ibfk_1 FOREIGN KEY (payment_loan_id) REFERENCES loan (loan_id) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ")");

        db.execSQL("CREATE TABLE  " + penalties + " (\n" +
                "  penalty_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  penalty_loan_id INTEGER NOT NULL,\n" +
                "  penalty_amount decimal(10,2) NOT NULL,\n" +
                "  penalty_reg_date datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  CONSTRAINT penalties_ibfk_1 FOREIGN KEY (penalty_loan_id) REFERENCES loan(loan_id) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ")");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS update_loans AFTER UPDATE ON user_info " +
                "BEGIN UPDATE loan set loan_status='overdue',loan_amount_due=loan_amount_due + loan_amount_due * 0.15 ,loan_due_date=loan_due_date + '2592000' " +
                "WHERE loan_amount_due > 0 AND loan_due_date < new.user_log; END;");

        db.execSQL("CREATE TRIGGER IF NOT EXISTS insert_penalties AFTER UPDATE ON loan " +
                "WHEN old.loan_amount_due <> new.loan_amount_due AND old.loan_due_date <> new.loan_due_date AND new.loan_amount_due > 0" +
                " BEGIN INSERT into penalties(penalty_loan_id,penalty_amount) values (old.loan_id,(new.loan_amount_due * 0.1)); END;");

        db.execSQL("CREATE TRIGGER IF NOT EXISTS update_loan_amount_due AFTER INSERT ON payments" +
                " BEGIN UPDATE loan SET loan_amount_due=(loan_amount_due - new.payment_amount) where loan_id=new.payment_loan_id; END;");

        db.execSQL("CREATE TRIGGER update_loan_status AFTER UPDATE ON loan" +
                " WHEN old.loan_amount_due <> new.loan_amount_due " +
                "BEGIN UPDATE loan SET loan_status='settled' where new.loan_amount_due <= 0; END;");

        db.execSQL("CREATE TRIGGER calculate_amount_due AFTER UPDATE ON loan" +
                " WHEN old.loan_interest <> new.loan_interest " +
                " BEGIN UPDATE loan SET loan_amount_due= loan_amount * (1 + new.loan_interest * new.loan_period/30); END;");

        db.execSQL("CREATE TRIGGER cal_loan_amount_due AFTER INSERT ON loan" +
                   " BEGIN UPDATE loan SET loan_amount_due= loan_amount * (1 +  loan_interest *  loan_period/30); END;");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ user_info);
        db.execSQL("DROP TABLE IF EXISTS "+ customer_info);
        db.execSQL("DROP TABLE IF EXISTS "+ loan);
        db.execSQL("DROP TABLE IF EXISTS "+ penalties);
        db.execSQL("DROP TABLE IF EXISTS "+ payment_amount);
        onCreate(db);
    }

    public boolean insertCustomerInfo(String name, int number){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();

        contentValues.put(customer_name,name);
        contentValues.put(customer_phone_number,number);


        long result=db.insert(customer_info,null,contentValues);
        db.close();
        if(result == -1)
            return false;
        else
            return true;

    }

    public long insertLoans(int cust_id, int message_id, double amount, int payment_period, double rate, long issue, String code, long due_date)
    {
        SQLiteDatabase db =this.getReadableDatabase();
        ContentValues contentValues= new ContentValues();
        try
        {

        contentValues.put(loan_customer_phone_number ,cust_id);
        contentValues.put(loan_amount,amount);
        contentValues.put(loan_period,payment_period);
        contentValues.put(loan_interest,rate);
        contentValues.put(loan_issue_date, String.valueOf(issue));
        contentValues.put(loan_mpesa_code,code);
        contentValues.put(loan_message_id,message_id);
        contentValues.put(loan_due_date,due_date);
        return db.insert(loan,null,contentValues);

    }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        db.close();
        return 0;
    }

    public long insertDeletedLoans(int cust_id, int confirmed, int message_id, String amount, int payment_period, double rate, long issue, String code, long due_date)
    {
        SQLiteDatabase db =this.getReadableDatabase();
        ContentValues contentValues= new ContentValues();
        try
        {

            contentValues.put(loan_customer_phone_number ,cust_id);
            contentValues.put(loan_amount,amount);
            contentValues.put(loan_period,payment_period);
            contentValues.put(loan_interest,rate);
            contentValues.put(loan_issue_date, String.valueOf(issue));
            contentValues.put(loan_mpesa_code,code);
            contentValues.put(loan_confirmed,confirmed);
            contentValues.put(loan_message_id,message_id);
            contentValues.put(loan_due_date,due_date);
            return db.insert(loan,null,contentValues);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        db.close();
        return 0;
    }

    public boolean insertuserData(String fname,String lname,String phone,String password,long datetoday){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();

        contentValues.put(user_fullname,fname+' '+lname);
        contentValues.put(user_phone_number,phone);
        contentValues.put(user_password,password);
        contentValues.put(user_log,datetoday);

        long result=db.insert(user_info,null,contentValues);

        db.close();
        if(result == -1)
            return false;
        else
            return true;
    }



    public boolean getCredentials(String phone,String userpass){
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor cursor= db.rawQuery("select * from user_info WHERE user_phone_number=?  AND user_password= ? ",new String[]{phone,userpass});
        int count = cursor.getCount();

        db.close();
        if(count > 0){
            return true;
        } else {
            return false;
        }
    }
    public boolean checkUserPhone(String phone){
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor cursor= db.rawQuery("select * from user_info WHERE user_phone_number=? ",new String[]{phone});
        int count = cursor.getCount();

        db.close();
        if(count > 0){
            return true;
        } else {
            return false;
        }
    }
    public boolean checkCustomerPhone(int phone){
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor cursor= db.rawQuery("select * from customer_info WHERE customer_phone_number= ?",new String[]{String.valueOf(phone)});
        int count = cursor.getCount();
        db.close();
        if(count > 0){
            return true;
        } else {
            return false;
        }
    }
    public boolean checkMessageId(int msgId){
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor cursor= db.rawQuery("select * from loan WHERE loan_message_id = ?",new String[]{String.valueOf(msgId)});
        int count = cursor.getCount();

        db.close();
        if(count > 0){
            return true;
        } else {
            return false;
        }
    }

    public Cursor getConfirmedLoans()
    {
        SQLiteDatabase db =this.getWritableDatabase();
        return db.rawQuery("SELECT loan_id,loan_mpesa_code,loan_amount_due,loan_status,customer_name,loan_customer_phone_number,loan_amount,loan_due_date,loan_issue_date,loan_interest FROM loan ,customer_info where loan_customer_phone_number = customer_phone_number AND loan_confirmed ='0' AND loan_amount_due>0 GROUP BY loan_id",null);

    }
    public Cursor getUnConfirmedLoans()
    {
        SQLiteDatabase db =this.getWritableDatabase();
        String[] columns={loan_id,loan_mpesa_code,loan_amount_due,loan_status,customer_name};
        return db.rawQuery("SELECT loan_id,loan_mpesa_code,loan_amount_due,loan_status,customer_name,loan_customer_phone_number,loan_amount,loan_due_date,loan_issue_date FROM loan ,customer_info where loan_customer_phone_number = customer_phone_number AND loan_confirmed = '0' GROUP BY loan_id",null);

    }

    public long updateLoanPayment(int id, String amount , String mpesaCode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try{

        values.put(payment_loan_id,id);
        values.put(payment_amount,amount);
        values.put(payment_reference_code,mpesaCode);

        db.insert(payments,null,values);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        db.close();
        return 0;

    }
    public long deleteLoan(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(loan, loan_id	+ "	= ?", new String[] { String.valueOf(id)});

        db.close();
        return 0;
    }
    public long confirmLoans(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(loan_confirmed,1);
        db.update(loan,values, loan_id	+ "	= ?", new String[] { String.valueOf(id)});
        db.close();
        return 0;
    }
    public int countLoans(){
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor cursor= db.rawQuery("select COUNT(*) from loan WHERE loan_confirmed= '2'",null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        db.close();
        return count;
    }
    public int setReminder(long today){
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor cursor= db.rawQuery("select COUNT(*) from loan WHERE (loan_due_date-"+today+")/86400 < 7 AND loan_amount_due > 0 ",null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        db.close();
        return count;
    }
    public Cursor getReminderLoans(long today)
    {
        SQLiteDatabase db =this.getWritableDatabase();
        return db.rawQuery("SELECT loan_id,loan_mpesa_code,loan_amount_due,loan_status,customer_name,loan_customer_phone_number,loan_amount,loan_due_date,loan_issue_date,loan_interest FROM loan ,customer_info where loan_customer_phone_number = customer_phone_number AND loan_confirmed ='0' AND (loan_due_date-"+today+")/86400 < 7 AND loan_amount_due > 0 GROUP BY loan_id",null);

    }
    public long updateLoanTerms(int id, double interest , double days,long seconds){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try{

            values.put(loan_interest,interest);
            values.put(loan_period,days);
            values.put(loan_due_date,seconds);

            db.update(loan,values,loan_id	+ "	= ?",new String[] { String.valueOf(id)});

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        db.close();
        return 0;

    }

}

