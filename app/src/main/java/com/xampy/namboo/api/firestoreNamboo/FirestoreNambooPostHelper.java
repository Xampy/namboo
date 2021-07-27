package com.xampy.namboo.api.firestoreNamboo;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;
import com.xampy.namboo.api.dataModel.PostNambooFirestore;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.xampy.namboo.api.dataModel.PostNambooFirestore;

import static com.xampy.namboo.MainActivity.mCurrentUser;

public class FirestoreNambooPostHelper {

    private static final String COLLECTION_NAME = "posts";

    public static CollectionReference getPostsCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public  static Query getPostByParameters(String post_type, String room_type,
                                             int price_min, int price_max,
                                             String city, String district){

        return getPostsCollection()
                .orderBy(
                        "mPrice",
                        Query.Direction.DESCENDING)
                .limit(20)
                .whereEqualTo("mTypePost", post_type)
                .whereEqualTo("mRoomType", room_type)
                .whereEqualTo("mCity", city)
                .whereEqualTo("mDistrict", district)
                .whereLessThan("mPrice", price_max);
    }

    public static Task<Void> updatePostBoostedState(boolean state, String post_uid){
         return FirestoreNambooPostHelper.getPostsCollection().document(post_uid).update("mBoosted", state);
    }


    public static Task<Void> deletePost(String uid) {
        return FirestoreNambooPostHelper.getPostsCollection()
                .document(uid).delete();
    }

    public  static Query getAllRenting(){
        return getPostsCollection()
                .orderBy(
                        "dateCreated",
                        Query.Direction.DESCENDING)
                .limit(20)
                .whereEqualTo("mTypePost", "Location");
    }

    public  static Query getLatestRenting(){
        return getPostsCollection()
                .orderBy(
                        "dateCreated",
                        Query.Direction.DESCENDING)
                .limit(4)
                .whereEqualTo("mTypePost", "Location");
    }


    /**
     * Get the last ten boosted post
     * @return
     */
    public  static Query getLatestBoostedRenting(){
        return getPostsCollection()
                .orderBy(
                        "dateCreated",
                        Query.Direction.DESCENDING)
                .limit(10)
                .whereEqualTo("mBoosted", true)
                .whereEqualTo("mTypePost", "Location");
    }




    public  static Query getAllSelling(){
        return getPostsCollection()
                .orderBy(
                        "dateCreated",
                        Query.Direction.DESCENDING)
                .limit(20)
                .whereEqualTo("mTypePost", "Vente");
    }

    public  static Query getLatestSelling(){
        return getPostsCollection()
                .orderBy(
                        "dateCreated",
                        Query.Direction.DESCENDING)
                .limit(4)
                .whereEqualTo("mTypePost", "Vente");
    }


    /**
     * Get the latest ten selling boosted post
     * @return
     */
    public  static Query getLatestBoostedSelling(){
        return getPostsCollection()
                .orderBy(
                        "dateCreated",
                        Query.Direction.DESCENDING)
                .limit(10)
                .whereEqualTo("mBoosted", true)
                .whereEqualTo("mTypePost", "Vente");
    }


    public  static Query getFourChambresForHome(){
        return getPostsCollection()
                .orderBy   (
                        "dateCreated",
                        Query.Direction.DESCENDING)
                .limit(6)
                .whereEqualTo("mRoomType", "Chambre");
    }

    public  static Query getSomeChambres(){
        return getPostsCollection()
                .orderBy   (
                        "dateCreated",
                        Query.Direction.DESCENDING)
                .limit(20)
                .whereEqualTo("mRoomType", "Chambre");
    }

    public  static Query getSomeMaisonsForHome(){
        return getPostsCollection()
                .orderBy(
                        "dateCreated",
                        Query.Direction.DESCENDING)
                .limit(6)
                .whereEqualTo("mRoomType", "Maison");
    }

    public  static Query getSomeMaisons(){
        return getPostsCollection()
                .orderBy(
                        "dateCreated",
                        Query.Direction.DESCENDING)
                .limit(20)
                .whereEqualTo("mRoomType", "Maison");
    }

    public  static Query getSomeTerrainsForHome(){
        return getPostsCollection()
                .orderBy(
                        "dateCreated",
                        Query.Direction.DESCENDING)
                .limit(6)
                .whereEqualTo("mRoomType", "Terrain");
    }

    public  static Query getSomeTerrains(){
        return getPostsCollection()
                .orderBy(
                        "dateCreated",
                        Query.Direction.DESCENDING)
                .limit(20)
                .whereEqualTo("mRoomType", "Terrain");
    }

    public  static Query getCurrentUserPosts(String uid){
        return getPostsCollection()
                .orderBy(
                        "dateCreated",
                        Query.Direction.DESCENDING)
                .limit(6)
                .whereEqualTo("mUserSenderUid", uid);
    }



    public static Task<Void> createPost(
            String mTypePost,
            int mPrice,
            String mRoomType,
            int mSurface,
            int salon,
            int chambre,
            String mDescription,
            String mCity,
            String mDistrict,
            String uid,
            String img_utls
    ){



        String post_uid = mCurrentUser.getTel() + "_" + System.currentTimeMillis();

        PostNambooFirestore postNambooFirestore = new PostNambooFirestore(
                mTypePost,
                mPrice,
                mRoomType,
                mSurface,
                salon,
                chambre,
                mDescription,
                mCity,
                mDistrict,
                img_utls,
                post_uid);
        postNambooFirestore.setmUserSenderUid(uid);
        String s = img_utls;
        Log.d("Firestore database set", "createPost: " + s);
        //postNambooFirestore.setImagesUrl(img_utls);

        //By default all users are particular
        return FirestoreNambooPostHelper.getPostsCollection().document(post_uid).set(postNambooFirestore);
    }
}
