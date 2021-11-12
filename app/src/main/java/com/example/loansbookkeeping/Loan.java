package com.example.loansbookkeeping;

public class Loan {

        private String loan_amount;
        private int loan_id;
        private String loan_customer_phone_number;
        private String customer_name;
        private String loan_status;
        private String loan_mpesa_code;
        private String amount_disbursed;
        private String due_date;
        private String issue_date;
        private String rate;

    private String msg_id;
    private String msg_body;

        public  Loan(String msg_id,String msg_body){
            this.msg_id=msg_id;
            this.msg_body=msg_body;
        }

        public Loan(int loan_id, String loan_mpesa_code,String loan_amount,String loan_status,String customer_name,String loan_customer_phone_number,String amount_disbursed,String due_date,String issue_date,String rate ) {
        this.loan_id=loan_id;
        this.rate=rate;
        this.customer_name=customer_name;
        this.loan_amount=loan_amount;
        this.loan_status=loan_status;
        this.loan_mpesa_code=loan_mpesa_code;
        this.loan_customer_phone_number=loan_customer_phone_number;
        this.amount_disbursed=amount_disbursed;
        this.due_date=due_date;
        this.issue_date=issue_date;
        }
        public String getMsg_Body() {
        return msg_body;
    }
        public String getMsg_Id() {
        return msg_id;
    }
        public String getLoanAmount() {
            return loan_amount;
        }
        public String getRate() {
        return rate;
    }
        public String getMpesaCode() {
        return loan_mpesa_code;
    }
        public int getLoanId() {
            return loan_id;
        }
        public String getPhoneNumber() {
            return loan_customer_phone_number;
        }
        public String getLoanStatus() {
        return loan_status;
        }
        public String getFullname() {
            return customer_name;
        }
        public String getAmount_disbursed(){return amount_disbursed;}
        public String getDue_date(){return due_date;}
        public String getIssue_date(){return issue_date;}


    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }
    public void setMsg_body(String msg_body) {
        this.msg_body = msg_body;
    }
    public void setRate(String rate){this.rate=rate;}
        public void setPhoneNumber(String loan_customer_phone_number) {
            this.loan_customer_phone_number = loan_customer_phone_number;
        }
        public void setAmount_disbursed(String amount_disbursed){
            this.amount_disbursed=amount_disbursed;
        }
        public void setDue_date(String due_date){
            this.due_date=due_date;}
        public void setIssue_date(String issue_date){
            this.issue_date=issue_date;}
        public void setLoanAmount(String loan_amount){
            this.loan_amount=loan_amount;
        }
        public void setLoanId(int loan_id){
            this.loan_id=loan_id;
        }
        public void setFullname(String customer_name) {

            this.customer_name = customer_name;
        }
    public void setMpesaCode(String loan_mpesa_code) {

        this.loan_mpesa_code = loan_mpesa_code;
    }
        public void setLoanStatus(){
            this.loan_status=loan_status;
        }

}
