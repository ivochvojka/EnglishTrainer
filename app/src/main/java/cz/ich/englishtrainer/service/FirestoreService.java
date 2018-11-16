package cz.ich.englishtrainer.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.ich.englishtrainer.model.Word;
import io.reactivex.Observable;
import io.reactivex.subjects.ReplaySubject;

/**
 * Implementation of Firebase Firestore database.
 * TODO Singleton Dagger
 *
 * @author Ivo Chvojka
 */
public class FirestoreService {

    private static String TAG = FirestoreService.class.getSimpleName();

    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseUser firebaseUser;
    private DocumentReference mRatingRef;

    private static FirestoreService instance;

    public static synchronized FirestoreService getInstance(FirebaseUser firebaseUser) {
        if (instance == null) {
            instance = new FirestoreService(firebaseUser, FirebaseFirestore.getInstance());
        }
        return instance;
    }

    //    @Inject
    public FirestoreService(FirebaseUser firebaseUser, FirebaseFirestore firebaseFirestore) {
        this.firebaseUser = firebaseUser;
        this.mFirebaseFirestore = firebaseFirestore;
    }

    public void readDb() {
//        mFirebaseFirestore.collection("users").document("Contacts").
    }

    public Observable<List<String>> getAlbums() {
        final ReplaySubject subject = ReplaySubject.create();
        mFirebaseFirestore.collection("users")
                .document(firebaseUser.getUid())
                .collection("albums")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        final List<String> albums = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                albums.add(document.getId());
                            }
                        } else {
                            Log.w(TAG, "Error getting document.", task.getException());
                        }

                        subject.onNext(albums);
                        subject.onComplete();
                    }

                });
        return subject;
    }

    public Observable<List<Word>> getWords(String album) {
        final ReplaySubject subject = ReplaySubject.create();
        mFirebaseFirestore.collection("users")
                .document(firebaseUser.getUid())
                .collection("albums")
                .document(album)
                .collection("words")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        final List<Word> words = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                final Map<String, Object> data = document.getData();
                                words.add(new Word((String) data.get("lan1"), (String) data.get("lan2")));
                            }
                        } else {
                            Log.w(TAG, "Error getting document.", task.getException());
                        }

                        subject.onNext(words);
                        subject.onComplete();
                    }

                });
        return subject;
    }

    public void createUser() {
// Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

// Add a new document with a generated ID
        mFirebaseFirestore.collection("users")
                .document(firebaseUser.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public Task<QuerySnapshot> getUser() {

        mFirebaseFirestore.collection("users")
                .document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Object lastName = task.getResult().get("last");
                            Log.d(TAG, "Last name=" + lastName);
                        } else {
                            Log.w(TAG, "Error getting document.", task.getException());
                        }
                    }
                });

        return null;
    }

//    public void addRating(String email, AppRating appRating) {
//        Log.d(TAG, "Add rating for email=" + email + ", rating=" + appRating.toString());
//
//        if (email != null) {
//            // because of firebase paths problem
//            email = email.replace("/", "_");
//        }
//
//        DocumentReference emailRef = null;
//        try {
//            emailRef = mFirebaseFirestore.collection("feedbacks").document(email);
//        } catch (Exception e) {
//            // when refecence to email could not be obtained
//            emailRef = mFirebaseFirestore.collection("feedbacks").document("unknown");
//        }
//
//        final DocumentReference documentReference = emailRef;
//        documentReference.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) createNode(task, documentReference, appRating);
//        });
//    }
//
//    @Override
//    public void appendFeedback(String feedback) {
//        Log.d(TAG, "Append feedback with value: " + feedback);
//        if (mRatingRef != null && !TextUtils.isEmpty(feedback)) {
//            final Map<String, Object> map = new HashMap<>(1);
//            map.put("feedback", feedback);
//            final Task<Void> task = mRatingRef.update(map);
//            addLogs(task);
//        }
//    }
//
//    private void createNode(
//            Task<DocumentSnapshot> task,
//            DocumentReference reference,
//            AppRating appRating) {
//
//        updateAppFeedback(appRating);
//
//        final boolean exists = task.getResult().exists();
//        if (exists) {
//            Log.d(TAG, "Adding rating to collection with values: " + appRating.toString());
//            final CollectionReference collectionRef = reference.collection("newfeedbacks");
//            createNewCollectionItem(collectionRef, getTimeKey(), appRating);
//        } else {
//            Log.d(TAG, "Creating rating with values: " + appRating.toString());
//            mRatingRef = reference;
//            Task<Void> result = reference.set(appRating);
//            addLogs(result);
//        }
//    }
//
//    private void createNewCollectionItem(
//            CollectionReference collectionReference,
//            String key,
//            Object value) {
//        final DocumentReference newDocumentReference = collectionReference.document(key);
//        newDocumentReference.get().addOnCompleteListener(taskNewNode -> {
//            if (taskNewNode.isSuccessful()) {
//                newDocumentReference.set(value)
//                        .addOnCompleteListener(setTask -> {
//                            if (setTask.isSuccessful()) {
//                                mRatingRef = newDocumentReference;
//                            }
//                        });
//            }
//        });
//    }
//
//    private void addLogs(Task<?> task) {
//        task
//                .addOnSuccessListener(aVoid -> Log.d(TAG, "Rating successfully created."))
//                .addOnFailureListener(e -> Log.e(TAG, "Creation of rating failed.", e));
//    }
//
//    private void updateAppFeedback(AppRating appFeedback) {
//        appFeedback.setTime(new Date());
//        appFeedback.setVersion(BuildConfig.VERSION_NAME);
//    }
//
//    private String getTimeKey() {
//        final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
//        return dataFormat.format(new Date());
//    }

}