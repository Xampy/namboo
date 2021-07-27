package com.xampy.namboo.api.dataModel;

public class DataBaseUser {
    private String username;
    private String password;
    private String status;
    private long id;
    private String tel;
    private String uid;

    private String last_transaction_reference;
    private boolean account_activated;
    private int credit_amount;

    public DataBaseUser(){
        this.username = "Username";
    }

    public DataBaseUser(long id, String uid, String username, String pass, String status, String tel) {

        this.username = username;
        this.password = pass;
        this.status = status;
        this.tel = tel;
        this.uid = uid;

        this.credit_amount = 0; //At start amount is equal to zero
        this.last_transaction_reference = "@none";
        this.account_activated = false;
    }


    public DataBaseUser(
            long id, String uid, String username,
            String pass, String status, String tel,
            String last_transaction_reference, int credit_amount, boolean account_activated) {

        this.username = username;
        this.password = pass;
        this.status = "Particulier"; //As default
        this.tel = tel;
        this.uid = uid;

        this.credit_amount = credit_amount; //At start amount is equal to zero
        this.last_transaction_reference = last_transaction_reference;
        this.account_activated = account_activated;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public void setTel(String tel) {this.tel = tel;}
    public void setUid(String uid) {this.uid = uid;}
    public void setPassword(String password) {this.password = password;}
    public void setAccount_activated(boolean account_activated) {
        this.account_activated = account_activated;
    }
    public void setCredit_amount(int credit_amount) {this.credit_amount = credit_amount;}
    public void setLast_transaction_reference(String last_transaction_reference) {
        this.last_transaction_reference = last_transaction_reference;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //[START getters]
    public String getUsername(){
        return this.username;
    }
    public String getTel() {
        return tel;
    }
    public String getPassword() {  return password;}
    public String getStatus() {
        return status;
    }
    public String getUid() {
        return uid;
    }
    public int getCredit_amount() { return credit_amount;}
    public String getLast_transaction_reference() {return last_transaction_reference;}
    public boolean isAccount_activated() {
        return account_activated;
    }
    //[END getters]
}
