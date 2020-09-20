package com.example.learn_english.model;

import android.net.Uri;

public class FlashCard {

    private String nameOfCard;
    private String nameInFirebase;

    public FlashCard(String indexCard, int repeat, String englishText, String polishText, String nameOfImage, String image) {
        this.index = Integer.parseInt(indexCard);
                this.repeat = repeat;
        this.englishText=englishText;
        this.polishText=polishText;
        this.nameInFirebase = nameOfImage;
        this.imageUri = image;
    }

    public void SetNameInFirebase(String name){
        nameInFirebase = name;
    }
    private String englishText;
    private String polishText;
    private String imageUri;
    private int repeat;
    private int index;

    public FlashCard(String englishText, String polishText, String ImagePath){
        setQuesitonAndAnswer(englishText, polishText, ImagePath);
    }
    public FlashCard(String nameOfCard, int repeat, int index){
        this.nameOfCard=nameOfCard;
        this.repeat = repeat;
        this.index = index;
    }
    public void setQuesitonAndAnswer(String englishText, String polishText,  String imageUri){
        this.englishText = englishText;
        this.polishText = polishText;
        this.imageUri = imageUri;

    }
    public void setRepeat(int repeat)
    {
        this.repeat = repeat;
    }
    public void setIndex(int index)
    {
        this.index = index;
    }



    public int getRepeat(){
        return repeat;
    }
    public String getNameOfImage(){return nameInFirebase;}
    public int getIndexImage(){
        return index;
    }

    public String getImage(){return imageUri;}

    public String getEnglishText(){
        return englishText;
    }

    public String getPolishText(){
        return polishText;
    }
}
