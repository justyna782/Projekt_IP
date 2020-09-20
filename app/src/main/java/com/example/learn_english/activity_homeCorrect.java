package com.example.learn_english;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learn_english.model.FireBaseModel;
import com.example.learn_english.model.UserProfile;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class activity_homeCorrect extends AppCompatActivity {


    private TextView profileName, profileEmail;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_correct);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        profileName = (TextView) headerView.findViewById(R.id.tvProfileName);
        profileEmail = (TextView) headerView.findViewById(R.id.tvEmail);


//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(activity_homeCorrect.this, FlashCardView.class));
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
              R.id.nav_profile,  R.id.nav_find, R.id.nav_detectTextId, R.id.nav_flashcard, R.id.nav_translatorId)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseDatabase = FirebaseDatabase.getInstance();

        if(!FireBaseModel.isLogin)
            FireBaseModel.getInstanceOfFireBase().getDataAboutLastProfile(this);

//        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                UserProfile.getInstance().setFromDataBase(dataSnapshot.getValue(UserProfile.class));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(activity_homeCorrect.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_home_correct, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.log_out) {

            Intent startIntent = new Intent(getApplicationContext(), Logout.class);
            startActivity(startIntent);

        } else if (id == R.id.edit_profile) {

            Intent startIntent = new Intent(getApplicationContext(), UpdateProfile.class);
            startActivity(startIntent);
        } else if (id == R.id.change_password) {

        Intent startIntent = new Intent(getApplicationContext(), UpdatePassword.class);
        startActivity(startIntent);
    }

        return super.onOptionsItemSelected(item);
    }
}