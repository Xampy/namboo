package com.xampy.namboo.api.firestoreNamboo;

import com.xampy.namboo.api.dataModel.UserNambooFirestore;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;

public class FirestoreNambooUserHelper {

    private static final String COLLECTION_NAME = "users";

    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static Task<Void> createUser(
            String uid,
            //String userName, Not necessary for the first time creation
            String phoneNUmber
            //String urlPicture, Not necessary for the first time creation
            //String amount Not necessary for the first time creation
            ){



        UserNambooFirestore userNambooFirestore = new UserNambooFirestore(
                uid,
                "@none",
                "@none", //Passwor is default at cration
                phoneNUmber,
                "@none",
                "0",
                "",
                "",
                "");

        //By default all users are particular
        return FirestoreNambooUserHelper.getUsersCollection().document(uid).set(userNambooFirestore);
    }


    /**
     * get activated user by service name
     * @param service
     * @return
     */
    public  static Query getUsersByServices(String service){
        return getUsersCollection()
                .limit(50)
                .whereEqualTo("activated", true)
                .whereEqualTo("serviceType", service);
    }

    /**
     *  Get users who ectivated thier service  with parameters
     * @param service
     * @param city
     * @param district
     * @return
     */
    public  static Query getSearchUsersByServicesParams(String service, String city, String district){
        return getUsersCollection()
                .limit(50)
                .whereEqualTo("activated", true)
                .whereEqualTo("serviceType", service)
                .whereEqualTo("city", city)
                .whereEqualTo("district", district);

    }






















    public static Task<DocumentSnapshot> getUser(String uid){
        return FirestoreNambooUserHelper.getUsersCollection().document(uid).get();
    }

    public static Task<Void> updateUsername(String username, String uid){
        return FirestoreNambooUserHelper.getUsersCollection()
                .document(uid).update("username", username);
    }
    public static Task<Void> updateUserPassword(String pass, String uid){
        return FirestoreNambooUserHelper.getUsersCollection()
                .document(uid).update("password", pass);
    }
    public static Task<Void> updateUserServiceType(String username, String uid){
        return FirestoreNambooUserHelper.getUsersCollection()
                .document(uid).update("serviceType", username);
    }

    public static Task<Void> updateUserImageURl(String url, String uid){
        return FirestoreNambooUserHelper.getUsersCollection()
                .document(uid).update("urlPicture", url);
    }

    public static Task<Void> updateUserCity(String city, String uid){
        return FirestoreNambooUserHelper.getUsersCollection()
                .document(uid).update("city", city);
    }

    public static Task<Void> updateUserDistrict(String district, String uid){
        return FirestoreNambooUserHelper.getUsersCollection()
                .document(uid).update("district", district);
    }

    public static Task<Void> updateUserPosition(String pos, String uid){
        return FirestoreNambooUserHelper.getUsersCollection()
                .document(uid).update("position", pos);
    }

    public static Task<Void> updateUserActivationState(boolean state, String uid){
        return FirestoreNambooUserHelper.getUsersCollection()
                .document(uid).update("activated", state);
    }


    public static Task<Void> updateUserAmount(int new_amount, String uid){
        return FirestoreNambooUserHelper.getUsersCollection()
                .document(uid).update("accountAmount", new_amount);
    }

    public static Task<Void> deleteUser(String uid) {
        return FirestoreNambooUserHelper.getUsersCollection().document(uid).delete();
    }


    //Implementing others here;
}
