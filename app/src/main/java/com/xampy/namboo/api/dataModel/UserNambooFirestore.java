package com.xampy.namboo.api.dataModel;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.GeoPoint;

public class UserNambooFirestore {

    public static final String SERVICE_PARTICULAR = "particular";

    private String uid;
    private String username;
    private String password;
    private String phoneNumber;
    private int accountAmount; //Value on the amount
    private String serviceType; //Particular, Agent immobilier, Maçon, etc
    private String urlPicture;

    private String city; //The city of work
    private String district; //The district of work
    private String position; //The work GPS position

    private boolean activated;

    public UserNambooFirestore(){

    }

    public UserNambooFirestore(String uid, String username, String password, String phoneNumber, String urlPicture, String amount,
                               String city, String district, String position) {
        this.uid = uid;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password =password;
        this.accountAmount = Integer.parseInt( amount );
        this.urlPicture = urlPicture;
        this.serviceType = "Particulier";

        this.city = city;
        this.district = district;
        this.position = position;

        this.activated = false;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setAccountAmount(int accountAmount) {
        this.accountAmount = accountAmount;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setCity(String city) {this.city = city;}
    public void setDistrict(String district) {this.district = district;}
    public void setPosition(String position) {this.position = position;}

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    //Getters
    public String getPhoneNumber() { return phoneNumber; }
    public String getUid() {return uid;}
    public String getUsername() { return username; }
    public int getAccountAmount() { return accountAmount;}
    public String getServiceType() {return serviceType;}
    public String getPassword() {return password;}
    public String getPosition() {return position;}
    public String getCity() { return city;}
    public String getDistrict() { return district;}

    public boolean getActivated() {
        return activated;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

}
