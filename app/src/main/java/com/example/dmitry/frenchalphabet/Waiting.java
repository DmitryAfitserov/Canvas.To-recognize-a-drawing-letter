package com.example.dmitry.frenchalphabet;

import android.os.AsyncTask;
import android.os.SystemClock;

/**
 * Created by Dmitry on 29.10.2016.
 */

public class Waiting extends AsyncTask<Void,Void,Void> {

    AnswerAsyncTask answerAsyncTask;

    public Waiting(AnswerAsyncTask answerAsyncTask) {
        this.answerAsyncTask = answerAsyncTask;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        SystemClock.sleep(2000);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        answerAsyncTask.callBack();
        super.onPostExecute(aVoid);
    }
}
