package com.example.learn_english;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        textView=findViewById(R.id.textView);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new
                ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
 //       navigationView.setCheckedItem(R.id.nav_tasks);
        menu = navigationView.getMenu();

    }

//    public void goToFind(View view) {
//        if(view.getId() == R.id.findId)
//        {
//            //Do something Like starting an activity
//            Intent intent = new Intent(HomeActivity.this, Lablieng.class);
//            startActivity(intent);
//        }
//        else if(view.getId()==R.id.profile_image)
//        {
//            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
//            startActivity(intent);
//        }
//        else if(view.getId()==R.id.translatorId)
//        {
//            Intent intent = new Intent(HomeActivity.this, Translator.class);
//            startActivity(intent);
//        }
//        else if(view.getId()==R.id.detectTextId)
//        {
//            Intent intent = new Intent(HomeActivity.this, DetectText.class);
//            startActivity(intent);
//        }
//    }
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.nav_find:
                intent = new Intent(HomeActivity.this, Labeling.class);
                startActivity(intent);
                break;
            case R.id.nav_detectTextId:
                intent = new Intent(HomeActivity.this, DetectText.class);
                startActivity(intent);
                break;
            case R.id.nav_translatorId:
                intent = new Intent(HomeActivity.this, Translator.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START); return true;
    }


}
