package com.example.learn_english.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBase extends SQLiteOpenHelper {

    private static DataBase dInstance;

    private static final String DATABASE_NAME = "FishCardBase";
    private static final int DATABASE_VERSION = 1;
    private final String QuestionPrimary = "QuestionID";
    private final String EnglishText =  "EnglishText";
    private final String PolishText =  "PolishText";
    private final String FlashCardTable = "Question";
    private final String FlashCardFolder = "FlashCardFolder";
    private final String ImagePath = "ImagePath";
    private final String QuestionSolved = "Solved";
    private final String QuestionElo = "Elo";
    private final String FolderTable = "Folder";
    private final String FolderPrimary = "FolderID";
    private final String FolderName = "FolderName";
    private final String UserEloTable = "UserEloTable";
    private final String UserElo = "UserElo";

    public static synchronized DataBase getInstance(Context context){
        if(dInstance == null){
            dInstance = new DataBase(context.getApplicationContext());
        }
        return dInstance;
    }

    private DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public DataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuestion = "CREATE TABLE " + FlashCardTable + " ("
                + QuestionPrimary + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EnglishText + " TEXT, "
                + PolishText  + " TEXT, "
                + QuestionElo + " INTEGER, "
                + QuestionSolved + " INTEGER, "
                + ImagePath + " VARCHAR(255), "
                + FlashCardFolder + " INTEGER);";
        db.beginTransaction();
        try{
            db.execSQL(createQuestion);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String dropAnswer = "DROP TABLE IF EXISTS " + FlashCardTable;
        db.beginTransaction();
        try{
            db.execSQL(dropAnswer);
        }finally {
            db.endTransaction();
        }
        onCreate(db);
    }

    public void addQuestionAndAnswer(String englishText, String polishText, String imagePath){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EnglishText, englishText);
        values.put(PolishText, polishText);
        values.put(QuestionSolved, 0);
        values.put(ImagePath,imagePath);
        db.insert(FlashCardTable, null, values);
        db.close();
    }
    public boolean deleteFlashcard(int PrimatyKey){
        SQLiteDatabase db = this.getWritableDatabase();

        boolean bQuestion = db.delete(FlashCardTable, QuestionPrimary + " = " + Integer.toString(PrimatyKey), null) > 0;

        db.close();
        if(!bQuestion){
            return false;
        }
        return true;
    }

    public FlashCard getFlashcard(int PrimaryKey){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursorQuestion = db.rawQuery("SELECT * FROM " + FlashCardTable + " WHERE " + QuestionPrimary + " = " + PrimaryKey, null);

        String englishText = null;
        String polishText = null;
        String ImagePath = null;
        String answer = null;
        boolean solved = false;



        while(cursorQuestion.moveToNext()){
            englishText = cursorQuestion.getString(1);
            polishText = cursorQuestion.getString(2);
            solved = (0 != cursorQuestion.getInt(3));
            ImagePath = cursorQuestion.getString(5);
        }

        cursorQuestion.close();
        db.close();
        return new FlashCard(englishText, polishText, PrimaryKey, solved, ImagePath);
    }

}
