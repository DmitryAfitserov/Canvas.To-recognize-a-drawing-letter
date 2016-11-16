package com.example.dmitry.frenchalphabet;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.Locale;

/**
 * Created by Dmitry on 27.10.2016.
 */

public class ListwithAlphbet extends ListActivity {

    ContollerDataBase contollerDataBase;
    Cursor cursor;
    ListView listView;
    AdapterforAlphabet adapterforAlphabet;
    TextToSpeech tts;
    Toolbar mActionBarToolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        contollerDataBase = new ContollerDataBase(getApplicationContext());
        contollerDataBase.open();
        cursor = contollerDataBase.query();

        adapterforAlphabet = new AdapterforAlphabet(getApplicationContext(), cursor, 0);


        Log.d("EEE" , "lf  " + adapterforAlphabet.getCount());

        setListAdapter(adapterforAlphabet);

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
        super.onCreate(savedInstanceState);

    }


    @Override
    protected void onDestroy() {
        if(tts != null){
            tts.stop();
           // tts.shutdown();
        }
        super.onDestroy();

    }

    class AdapterforAlphabet extends CursorAdapter {

        private LayoutInflater cursorInflater;


        public AdapterforAlphabet(Context context, Cursor c, int flags) {
            super(context, c, flags);

            cursorInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            return cursorInflater.inflate(R.layout.item, viewGroup, false);
        }

        @Override
        public void bindView(View view, Context context,Cursor cursor) {

            TextView letter = (TextView)view.findViewById(R.id.letter);
            letter.setText("Letter: " + cursor.getString(cursor.getColumnIndex("letter")));
            final String a = cursor.getString(cursor.getColumnIndex("transcription"));
            TextView transcription = (TextView)view.findViewById(R.id.transcription);
            transcription.setText("[" + cursor.getString(cursor.getColumnIndex("transcription")) + "]");

            ImageButton imageButton = (ImageButton)view.findViewById(R.id.image);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {

                    tts.speak(a, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            });
        }


    }


}
