package com.example.mywrotes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AboutMyWrotesActivity extends AppCompatActivity {

    ImageButton menuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_mywrotes);

        menuBtn = findViewById(R.id.menu_btn);
        menuBtn.setOnClickListener(v -> showMenu());
    }

    void showMenu() {
        PopupMenu popupMenu = new PopupMenu(AboutMyWrotesActivity.this, menuBtn);
        popupMenu.getMenu().add("Home");
        popupMenu.getMenu().add("Ausloggen");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getTitle().equals("Home")) {

                startActivity(new Intent(AboutMyWrotesActivity.this, MainActivity.class));
                return true;
            } else if (menuItem.getTitle().equals("Ausloggen")) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AboutMyWrotesActivity.this, LoginActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}
