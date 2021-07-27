package com.xampy.namboo.api.firestoreNamboo;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.xampy.namboo.api.dataModel.BoostedNambooFirestore;

public class FirestoreNambooBoostedHelper {

    private static final String COLLECTION_NAME = "boosted";

    public static CollectionReference getBoostedCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static Task<Void> createBoostedPost(String p_uid, String s_uid){

        BoostedNambooFirestore boot = new BoostedNambooFirestore(p_uid, s_uid);
        return FirestoreNambooBoostedHelper.getBoostedCollection().document().set(boot );
    }
}
