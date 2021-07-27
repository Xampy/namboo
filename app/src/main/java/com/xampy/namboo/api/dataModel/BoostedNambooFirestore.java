package com.xampy.namboo.api.dataModel;

public class BoostedNambooFirestore {
    private String mPostUid;
    private String mSenderUid;


    public BoostedNambooFirestore(String p_uid, String s_uid){
        mPostUid = p_uid;
        mSenderUid = s_uid;
    }


    public String getmPostUid() {
        return mPostUid;
    }

    public String getmSenderUid() {
        return mSenderUid;
    }

    public void setmPostUid(String mPostUid) {
        this.mPostUid = mPostUid;
    }

    public void setmSenderUid(String mSenderUid) {
        this.mSenderUid = mSenderUid;
    }
}
