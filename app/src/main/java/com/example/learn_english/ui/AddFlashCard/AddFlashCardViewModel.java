package com.example.learn_english.ui.AddFlashCard;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddFlashCardViewModel extends ViewModel {

    FragmentActivity activityFragment;
    private MutableLiveData<String> mText;

    public AddFlashCardViewModel(FragmentActivity activityFragment) {

        this.activityFragment = activityFragment;
    }


    public LiveData<String> getText() {
        return mText;
    }
}