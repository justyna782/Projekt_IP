package com.example.learn_english.model;

import java.util.ArrayList;
import java.util.Random;

public class UserProfile {


    private static UserProfile instance;
    private int learned;
    private int toDay = 0;
    private int left;
    private String userEmail;
    private String userName;
    private ArrayList<FlashCard> userFlashCards;

    private UserProfile() {
        if (userFlashCards == null)
            userFlashCards = new ArrayList<FlashCard>();
    }

    private UserProfile(String userEmail, String userName) {

        this.userEmail = userEmail;
        this.userName = userName;
        if (userFlashCards == null)
            userFlashCards = new ArrayList<FlashCard>();
    }

    public static UserProfile getInstance(String userEmail, String userName) {
        return instance = new UserProfile(userEmail, userName);
    }

    public static UserProfile getInstance() {
        if (instance == null)
            instance = new UserProfile();
        return instance;
    }

    public void setFromDataBase(UserProfile userProfile) {
        if (userProfile != null) {
            this.toDay = userProfile.toDay;
            this.userName = userProfile.userName;
            this.userEmail = userProfile.userEmail;
            this.userFlashCards = userProfile.userFlashCards;
            this.learned = userProfile.learned;
            this.left = userProfile.left;
            FireBaseModel.getInstanceOfFireBase().getDataOfFishCard(this);
        }
    }

    public void takeFish() {
        FireBaseModel.getInstanceOfFireBase().getDataOfFishCard(this);
    }

    public void AddFlashCard(FlashCard flashCard) {
        Random r = new Random();
        int indexMax = r.nextInt(80 - 65) + 65;
        flashCard.setRepeat(0);
        flashCard.setIndex(indexMax + 1);
        userFlashCards.add(flashCard);
        FireBaseModel.getInstanceOfFireBase().AddFishCardToDataBase(flashCard);
    }


    public ArrayList<FlashCard> getUserFlashCards() {
        return userFlashCards;
    }

    public void setUserFlashCards(ArrayList<FlashCard> userFlashCards) {
        this.userFlashCards = userFlashCards;
    }

    public int getLearned() {
        return learned;
    }

    public void setLearned(int learned) {
        this.learned = learned;
    }

    public int getToDay() {
        return toDay;
    }

    public void setToDay(int toDay) {

        if (toDay == 0)
            this.toDay = toDay;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void AddUserFishCard(FlashCard card) {
        for (FlashCard f : userFlashCards) {
            if (f.getRepeat() > 20) {
                learned++;
            }
        }


        userFlashCards.add(card);
    }

    public void RestCard() {
        userFlashCards = new ArrayList<FlashCard>();
    }

    public void DecreaseLeft() {
        if (left != 0)
            left--;

        toDay++;
        FireBaseModel.getInstanceOfFireBase().UpdateToday();
    }

    public void repatOfCard(FlashCard card) {


        for (FlashCard fla : userFlashCards) {
            if (fla.getIndexImage() == card.getIndexImage()) {
                fla.setRepeat(fla.getRepeat() + 1);
            }
        }
    }
}
