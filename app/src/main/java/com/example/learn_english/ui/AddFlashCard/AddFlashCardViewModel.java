package com.example.learn_english.ui.AddFlashCard;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.learn_english.model.FireBaseModel;
import com.example.learn_english.model.FlashCard;
import com.example.learn_english.model.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddFlashCardViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    FirebaseStorage storage;
    StorageReference storageReference;
    FragmentActivity  activityFragment;
    private FirebaseAuth firebaseAuth;

    public AddFlashCardViewModel( FragmentActivity  activityFragment) {

        this.activityFragment=activityFragment;
    }







    public LiveData<String> getText() {
        return mText;
    }
}