package com.xampy.namboo.ui.posts.postModelData;

import androidx.annotation.NonNull;

/**
 * Making a post
 * Model object of containing post data
 */
public class MakePostModel {

    private String mTypePost;
    private int mPrice;
    private String mRoomType;
    private int mSurface;
    private String mDescription;
    private String mCity;
    private String mDistrict;

    public MakePostModel(){

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
    public void setModelData(@NonNull String mTypePost, @NonNull int mPrice, @NonNull String mRoomType,
                                @NonNull int mSurface, @NonNull String mDescription, @NonNull String mCity,
                                @NonNull String mDistrict){

        this.mCity = mCity;
        this.mDistrict = mDistrict;
        this.mDescription = mDescription;
        this.mPrice = mPrice;
        this.mRoomType = mRoomType;
        this.mSurface = mSurface;
        this.mTypePost = mTypePost;
    }


    //#######################################################################################
    //Getters
    public int getmPrice() {
        return mPrice;
    }

    public int getmSurface() {
        return mSurface;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmDistrict() {
        return mDistrict;
    }

    public String getmRoomType() {
        return mRoomType;
    }

    public String getmTypePost() {
        return mTypePost;
    }
    //End Getters ##################################################################################
}
