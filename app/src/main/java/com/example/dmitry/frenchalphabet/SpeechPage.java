package com.example.dmitry.frenchalphabet;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class SpeechPage extends AppCompatActivity implements View.OnClickListener {


    ArrayList<ModelforAlphabet> list;
    ContollerDataBase contoller;

    ImageButton microphoneButton;
    ImageButton nextButton;
    ImageButton playButton;
    TextView letterTextView;
    TextView answerletterTextView;
    private final int SPEECH_RECOGNITION_CODE = 1;

    int position;
    boolean isAnswer = false;
    TextToSpeech tts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_page);

        microphoneButton = (ImageButton)findViewById(R.id.microphone_image_speech_page);
        nextButton = (ImageButton)findViewById(R.id.next_image_button);
        playButton = (ImageButton)findViewById(R.id.play_button_speech_page);
        letterTextView = (TextView)findViewById(R.id.letter_for_speech_text_view);
        answerletterTextView = (TextView)findViewById(R.id.letter_of_speech_text_view);

        microphoneButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        playButton.setOnClickListener(this);

        createTTS();
        contoller = new ContollerDataBase(getApplicationContext());
        contoller.open();
        list = (ArrayList<ModelforAlphabet>) contoller.getList();
        contoller.close();
        getRandomPosition();

    }

    private void getRandomPosition(){
        Random r = new Random();
        position = r.nextInt(26);

        createTask(position);

    }

    private void createTask(int position){
        letterTextView.setText(list.get(position).getLetter());
        answerletterTextView.setText("");


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.microphone_image_speech_page:{
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR");
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "Произнесите букву...");
                try {
                    startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry! Speech recognition is not supported in this device.",
                            Toast.LENGTH_SHORT).show();
                }

                break;
            }
            case R.id.next_image_button:{
                if(isAnswer){
                    isAnswer = false;
                    getRandomPosition();
                }
                break;
            }

            case R.id.play_button_speech_page:{
                tts.speak(list.get(position).getTranscription(), TextToSpeech.QUEUE_FLUSH, null, null);
                break;
            }

        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {

                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    Log.d("EEE", "true " + text);
                    String letter = text.substring(0, 1);
                    if(letter.equals("à")){
                        letter = "a";
                        text = "a";
                    }
                    isAnswer = true;
                    if(letter.equals(list.get(position).getLetter().substring(0, 1)) ||
                            letter.equals(list.get(position).getLetter().substring(1, 2))){
                        answerletterTextView.setText("Правильно, вы произнесли " +
                                list.get(position).getLetter().substring(0, 1));
                        Log.d("EEE", "true");
                    } else answerletterTextView.setText("Неверно, вы сказали " + text);

                }
                break;
            }
        }
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
        if(tts != null){
            tts.stop();
            // tts.shutdown();
        }
        super.onStop();

    }
}
