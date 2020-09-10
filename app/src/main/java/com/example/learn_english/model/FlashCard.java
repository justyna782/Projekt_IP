package com.example.learn_english.model;

public class FlashCard {

    private String englishText;
    private String polishText;
    private boolean solved;
    private int index;
    private String ImagePath;

    public FlashCard(String englishText, String polishText, int index, boolean solved , String ImagePath){
        setQuesitonAndAnswer(englishText, polishText, index,  solved, ImagePath);
    }

    public void setQuesitonAndAnswer(String englishText, String polishText, int index,  boolean solved, String ImagePath){
        this.englishText = englishText;
        this.polishText = polishText;
        this.index = index;
        this.ImagePath = ImagePath;
        this.solved = solved;
    }

    public boolean getSolved(){return solved;}

    public String getImage(){
        return ImagePath;
    }

    public int getIndex(){return index;}

    public String getEnglishText(){
        return englishText;
    }

    public String getPolishText(){
        return polishText;
    }
}
