
package com.example.learn_english.ui.AddFlashCard;


        import android.graphics.BitmapFactory;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;

        import androidx.fragment.app.FragmentActivity;

        import com.example.learn_english.R;
        import com.example.learn_english.model.DataBase;
        import com.example.learn_english.model.FlashCard;

public class AddFlashCardView extends FragmentActivity {

    private AddFlashCardViewModel flashCardViewModel;
    private ImageView imageView;
    private EditText englishText;
    private EditText polishText;
    private String imageUrl;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUrl = getIntent().getStringExtra("image");
        String text = getIntent().getStringExtra("text");
        setContentView(R.layout.activity_add_flash_card);
        imageView = this.findViewById(R.id.imageView1);
        englishText = this.findViewById(R.id.TextEnglish);
        polishText = this.findViewById(R.id.TextPolish);
        button = this.findViewById(R.id.bottom_button);
        button.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveFlaszCard();
            }
        });
        imageView.setImageBitmap(BitmapFactory.decodeFile(imageUrl));
        englishText.setText(text);


    }



    private void saveFlaszCard()
        {
            DataBase dataBase = DataBase.getInstance(this);
            dataBase.addQuestionAndAnswer(englishText.getText().toString(),polishText.getText().toString(),imageUrl);
            this.finish();
    }

}