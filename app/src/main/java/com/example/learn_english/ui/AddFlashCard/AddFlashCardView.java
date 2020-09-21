
package com.example.learn_english.ui.AddFlashCard;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.learn_english.R;
import com.example.learn_english.model.FlashCard;
import com.example.learn_english.model.UserProfile;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

public class AddFlashCardView extends FragmentActivity {

    private String finalFilePath, text;
    private AddFlashCardViewModel flashCardViewModel;
    private ImageView imageView;
    private EditText englishText;
    private EditText polishText;
    private Uri imageUrl;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flash_card);
        flashCardViewModel = new AddFlashCardViewModel(this);


        imageUrl = Uri.parse(getIntent().getStringExtra("image"));
        text = getIntent().getStringExtra("text");


        imageView = this.findViewById(R.id.imageView1);
        englishText = this.findViewById(R.id.TextEnglish);
        polishText = this.findViewById(R.id.TextPolish);
        button = this.findViewById(R.id.bottom_button);
        button.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                UserProfile user = UserProfile.getInstance();
                user.AddFlashCard(new FlashCard(englishText.getText().toString(), polishText.getText().toString(), imageUrl.toString()));
                finish();
            }
        });

        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(imageUrl, filePath, null, null, null);
        cursor.moveToFirst();

        int index = cursor.getColumnIndex(filePath[0]);
        finalFilePath = cursor.getString(index);
        imageView.setImageBitmap(BitmapFactory.decodeFile(finalFilePath));
        englishText.setText(text);


    }
}