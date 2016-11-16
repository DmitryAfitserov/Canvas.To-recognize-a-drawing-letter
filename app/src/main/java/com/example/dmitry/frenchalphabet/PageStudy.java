package com.example.dmitry.frenchalphabet;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class PageStudy extends AppCompatActivity implements View.OnClickListener, AnswerAsyncTask {

    ArrayList<ModelforAlphabet> list;
    ContollerDataBase contoller;
    int position;
    ImageButton playButton;


    TextView letterTextView;
    TextView answerTextView;
    Button okButton;
    EditText transEditText;
    TextToSpeech tts;
    boolean isHelp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_page_study);
        contoller = new ContollerDataBase(getApplicationContext());
        contoller.open();
        playButton = (ImageButton)findViewById(R.id.speak_image);
        letterTextView = (TextView)findViewById(R.id.letter);
        answerTextView = (TextView)findViewById(R.id.answer);
        okButton = (Button)findViewById(R.id.button_ok);
        transEditText = (EditText)findViewById(R.id.edit_text_trans);
        playButton.setOnClickListener(this);
        answerTextView.setOnClickListener(this);
        okButton.setOnClickListener(this);

        list = (ArrayList<ModelforAlphabet>) contoller.getList();
        createTTS();
        getRandomPosition();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);



    }

    private void createTask(int position){

        int coef = list.get(position).getCoef();
        if(coef == 0|| coef == 2|| coef > 4 && coef%3 == 2){
            letterTextView.setText(list.get(position).getLetter());
            isHelp = false;
            answerTextView.setText("ответ");
            Log.d("EEE", " da " + coef + " for " + list.get(position).getLetter());
        } else {
            Log.d("EEE", " net " + coef + " for " + list.get(position).getLetter());
            list.get(position).setCoef(list.get(position).getCoef() + 1);
            getRandomPosition();
            return;
        }

        showList();

    }
    public void showList(){
//        int i = 0;
        for(ModelforAlphabet model: list){
//            i++;
            Log.d("EEE", "letter " + model.getLetter() +
                    " trans " + model.getTranscription() +
                    " coef " + model.getCoef() );
//            if(i>5){
//                break;
//            }
        }
        Log.d("EEE", "-----------------------------------");
    }

    private void getRandomPosition(){
        Random r = new Random();
       position = r.nextInt(26);

        createTask(position);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.speak_image:{
                tts.speak(list.get(position).getTranscription(), TextToSpeech.QUEUE_FLUSH, null, null);
                break;
            }
            case R.id.answer:{
                answerTextView.setText("ответ: " + list.get(position).getTranscription());
                isHelp = true;
                break;
            }
            case R.id.button_ok:{
                if(!transEditText.getText().toString().equals("")){


                    if(transEditText.getText().toString().equals(list.get(position).getTranscription())){
                        if(!isHelp){
                            list.get(position).setCoef(list.get(position).getCoef() + 1);
                        }
                        Toast toast = Toast.makeText(getApplicationContext(), "Правильно", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 500);
                        toast.show();
                        tts.speak(list.get(position).getTranscription(), TextToSpeech.QUEUE_FLUSH, null, null);

                        Waiting w = new Waiting(this);
                        w.execute();


                    } else {
                        tts.speak("nou", TextToSpeech.QUEUE_FLUSH, null, null);
                        list.get(position).setCoef(0);
                        Toast toast = Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 500);
                        toast.show();

                    }
                }
                break;
            }



        }
    }


    @Override
    public void callBack() {
        transEditText.setText("");
        getRandomPosition();
    }

    private void createTTS(){
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.d("EEE", "This Language is not supported");
                    }
                } else {
                    Log.e("EEE", "Initilization Failed!");
                }
            }
        });
    }




    @Override
    protected void onStop() {
        contoller.setList(list);
        if(tts != null){
            tts.stop();
            // tts.shutdown();
        }
        super.onStop();

    }

}
