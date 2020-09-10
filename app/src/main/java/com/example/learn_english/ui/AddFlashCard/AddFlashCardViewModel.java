package com.example.learn_english.ui.AddFlashCard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddFlashCardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddFlashCardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Jest za gorąco dla tego widoku!");
    }



    public LiveData<String> getText() {
        return mText;
    }
}