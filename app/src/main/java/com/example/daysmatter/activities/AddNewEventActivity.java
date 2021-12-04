package com.example.daysmatter.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.daysmatter.R;

import java.util.Objects;

public class AddNewEventActivity extends AppCompatActivity {

    private ImageView addEventBack_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_add_new_event);

        baseInit();
    }

    public void baseInit(){
        addEventBack_imageView = findViewById(R.id.addEventBack_imageView);

        addEventBack_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEventBack_imageView.setAlpha(0.5f);
                onSupportNavigateUp();
            }
        });
    }

    /**
     * rewrite the back button on the action bar to make its functionality works better
     * @return false to return to the activity before
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}