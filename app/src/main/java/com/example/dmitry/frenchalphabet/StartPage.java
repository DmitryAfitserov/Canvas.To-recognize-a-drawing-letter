package com.example.dmitry.frenchalphabet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartPage extends AppCompatActivity implements View.OnClickListener{

    Button studyPage;
    Button viewingAlphabet;
    Button speachPage;
    Button drawPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        studyPage =  (Button)findViewById(R.id.start_study);
        viewingAlphabet = (Button)findViewById(R.id.viewing_alphabet);
        speachPage = (Button)findViewById(R.id.speach);
        drawPage = (Button)findViewById(R.id.draw_alphabet);

        studyPage.setOnClickListener(this);
        viewingAlphabet.setOnClickListener(this);
        speachPage.setOnClickListener(this);
        drawPage.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_study:{
                Intent intent = new Intent(StartPage.this, PageStudy.class);
                startActivity(intent);
                break;
            }
            case R.id.viewing_alphabet:{
                Intent intent = new Intent(StartPage.this, ListwithAlphbet.class);
                startActivity(intent);

                break;
            }

            case R.id.draw_alphabet:{
                Intent intent = new Intent(StartPage.this, DrawPage.class);
                startActivity(intent);
                break;
            }
            case R.id.speach:{
                Intent intent = new Intent(StartPage.this, SpeechPage.class);
                startActivity(intent);

                break;
            }
        }
    }
}
