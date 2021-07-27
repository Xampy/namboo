package com.xampy.namboo.api.dataModel;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import static com.xampy.namboo.MainActivity.mCurrentUser;

public class PostNambooFirestore implements Serializable {

    private String mTypePost;
    private int mPrice;
    private String mRoomType;
    private int mSurface;
    private String mDescription;
    private String mCity;
    private int mChambre;
    private int mSalon;
    private String mDistrict;
    private String mUid;

    private boolean mBoosted;

    private Date dateCreated;

    @NonNull  private String mUserSenderUid;
    private String imagesUrl;

    public PostNambooFirestore(){

    }

    /**
     * Set the data to the model
     * @param mTypePost
     * @param mPrice
     * @param mRoomType
     * @param mSurface
     * @param mDescription
     * @param mCity
     * @param mDistrict
     */
    public  PostNambooFirestore(
            String mTypePost,
            int mPrice,
            String mRoomType,
            int mSurface,
            int salon,
            int chambre,
            String mDescription,
            String mCity,
            String mDistrict,
            String imagesUrl,
            String post_uid){

        this.mCity = mCity;
        this.mDistrict = mDistrict;
        this.mDescription = mDescription;
        this.mPrice = mPrice;
        this.mRoomType = mRoomType;
        this.mSurface = mSurface;
        this.mTypePost = mTypePost;
        this.mChambre = chambre;
        this.mSalon = salon;
        this.imagesUrl = imagesUrl;

        this.mBoosted = false;
        this.mUid = post_uid;
    }
    //###########################


    public void setmPrice(int mPrice) {this.mPrice = mPrice;}
    public void setmRoomType(String mRoomType) {this.mRoomType = mRoomType;}
    public void setmTypePost(String mTypePost) {this.mTypePost = mTypePost;}
    public void setDateCreated(Date dateCreated) {this.dateCreated = dateCreated;}
    public void setmSurface(int mSurface) {this.mSurface = mSurface;}
    public void setmCity(String mCity) {this.mCity = mCity;}
    public void setmDescription(String mDescription) {this.mDescription = mDescription;}
    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }
    public void setmDistrict(String mDistrict) {this.mDistrict = mDistrict;}
    public void setmUserSenderUid(@NonNull String mUserSenderUid) {this.mUserSenderUid = mUserSenderUid;}
    public void setmChambre(int mChambre) {
        this.mChambre = mChambre;
    }

    public boolean getmBoosted() {
        return mBoosted;
    }

    public void setmSalon(int mSalon) {
        this.mSalon = mSalon;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }

    public void setmBoosted(boolean mBoosted) {
        this.mBoosted = mBoosted;
    }

    //#######################################################################################
    //Getters
    public int getmPrice() {return mPrice;}
    public int getmSurface() {return mSurface;}
    public String getmCity() {return mCity;}
    public String getmDescription() {return mDescription;}
    public String getmDistrict() {return mDistrict;}
    public String getmRoomType() {return mRoomType;}
    public int getmChambre() {return mChambre;}
    public int getmSalon() {return mSalon;}
    public String getmTypePost() {return mTypePost;}
    public String getImagesUrl() {
        return imagesUrl;
    }
    @ServerTimestamp public Date getDateCreated() {return dateCreated;}
    @NonNull public String getmUserSenderUid() { return mUserSenderUid;}

    public String getmUid() {
        return mUid;
    }

    //End Getters ##################################################################################
}
