package com.example.dmitry.frenchalphabet;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class DrawPage extends AppCompatActivity implements View.OnClickListener, AnswerAsyncTask{

    DrawingView dv;
    ImageButton playButton;
    ImageButton helpButton;
    ImageButton okButton;
    ImageButton nextButton;
    ImageButton cleanButton;
    TextToSpeech tts;
    ContollerDataBase contoller;
    ArrayList<ModelforAlphabet> list;
    int position;
    private static final String DATA_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath().toString();
    private static final String TESSDATA = "/tessdata";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_page);

        dv = (DrawingView)findViewById(R.id.vMain);

        playButton = (ImageButton)findViewById(R.id.play_button_image);
        helpButton = (ImageButton)findViewById(R.id.help_button);
        okButton = (ImageButton)findViewById(R.id.ok_button);
        nextButton = (ImageButton)findViewById(R.id.next_button);
        cleanButton = (ImageButton)findViewById(R.id.clean_button);
        playButton.setOnClickListener(this);
        helpButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        cleanButton.setOnClickListener(this);
        cleanButton.requestFocus();
        createTTS();
        contoller = new ContollerDataBase(getApplicationContext());
        contoller.open();
        list = (ArrayList<ModelforAlphabet>) contoller.getList();
        getRandomPosition();



    }




    private void getRandomPosition(){
        Random r = new Random();
        position = r.nextInt(26);

        Toast toast = Toast.makeText(getApplicationContext(), "Прослушайте букву", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 400);
        toast.show();
    }






    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.play_button_image:{
                tts.speak(list.get(position).getTranscription(), TextToSpeech.QUEUE_FLUSH, null, null);
                break;
            }
            case R.id.help_button:{
                Toast toast = Toast.makeText(getApplicationContext(), "Это буква " + list.get(position).getLetter(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 400);
                toast.show();
                break;
            }
            case R.id.ok_button:{
                Bitmap bitMap =  dv.getBitMap();
                String letter = detectText(bitMap);
                if(letter.equals(list.get(position).getLetter().substring(0,1)) ||
                        letter.equals(list.get(position).getLetter().substring(1,2))){
                    tts.speak(list.get(position).getTranscription(), TextToSpeech.QUEUE_FLUSH, null, null);
                    Toast toast = Toast.makeText(getApplicationContext(), "Правильно, это буква " +
                            list.get(position).getLetter(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 400);
                    toast.show();
                    Waiting waiting = new Waiting(this);
                    waiting.execute();

                } else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Неверно, вы нарисовали " +
                            letter, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 400);
                    toast.show();
                }

                break;
            }
            case R.id.next_button:{
                dv.clean();
                getRandomPosition();
                break;
            }
            case R.id.clean_button:{
                dv.clean();
                break;
            }
        }
    }

    @Override
    public void callBack() {
        getRandomPosition();
        dv.clean();
    }

    private void createTTS() {
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

    public String detectText(Bitmap bitmap) {
        TessBaseAPI tessBaseApi = new TessBaseAPI();
        int i = bitmap.getPixel(40, 40);
        Log.e("EEE", "picel " + i);
     //   prepareTesseract();

        tessBaseApi.init(DATA_PATH, "eng");
        tessBaseApi.setImage(bitmap);
        tessBaseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_CHAR);
        tessBaseApi.setVariable("tessedit_char_whitelist", "ABCDEFGHIJKLMNOPRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        String extractedText = tessBaseApi.getUTF8Text();

        Log.e("EEE", "text " + extractedText);
        String v = "\\/";
        String x = "><";
        if(extractedText.equals(v)){
            extractedText = "V";
        }
        if(extractedText.equals(x)){
            extractedText = "X";
        }

        return extractedText;
    }

    private void prepareTesseract() {
        try {
            prepareDirectory(DATA_PATH + TESSDATA);
        } catch (Exception e) {
            e.printStackTrace();
        }

      //  copyTessDataFiles(TESSDATA);
    }

    private void prepareDirectory(String path) {

        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d("EEE", "ERROR: Creation of directory " + path + " failed, check does Android Manifest have permission to write to external storage.");
            }
        } else {
            Log.d("EEE", "Created directory " + path);
        }
    }



    private void copyTessDataFiles(String path) {
        try {
            String fileList[] = getAssets().list(path);

            for (String fileName : fileList) {

                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                String pathToDataFile = DATA_PATH + path + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {

                    InputStream in = getAssets().open(path + "/" + fileName);

                    OutputStream out = new FileOutputStream(pathToDataFile);

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Log.d("EEE", "Copied " + fileName + "to tessdata");
                }
            }
        } catch (IOException e) {
            Log.e("EEE", "Unable to copy files to tessdata " + e.toString());
        }
    }
}
