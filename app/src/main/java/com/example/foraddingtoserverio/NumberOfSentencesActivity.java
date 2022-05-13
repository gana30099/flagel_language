package com.example.foraddingtoserverio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Random;

public class NumberOfSentencesActivity extends AppCompatActivity {

    String languageChoosen;
    String ip;
    int port;
    JSONArray mJsonObject = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_of_sentences);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        JSONArray ja = null;
        int l = 0;
        try {
            String s = getIntent().getStringExtra("json");
             mJsonObject = new JSONArray(s);
             mJsonObject = shuffleJsonArray(mJsonObject);
             l = mJsonObject.length();
        } catch (JSONException e) {

            e.printStackTrace();
        }


       languageChoosen = intent.getStringExtra("language");
        /* String message2 = intent.getStringExtra("number_of_sentences");
        ip = intent.getStringExtra("ip");
        port = intent.getIntExtra("port", 3000);*/


        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.choosen_language);
        textView.setText(languageChoosen);

        TextView textView2 = findViewById(R.id.number_of_sentences_draw);
        //textView2.setText(l);

    }

    public void goToNextActivity(View view) {

        EditText ev = (EditText)findViewById(R.id.number_of_sentences);
        String numberOfSentencesChoosen = ev.getText().toString();
        int choosen = Integer.parseInt(numberOfSentencesChoosen);

        if (choosen > mJsonObject.length()) {
            TextView tv2 = (TextView)findViewById(R.id.max_to_fill);
            tv2.setText("number is to big (max : " + mJsonObject.length() + ")");
            return;
        }

        /* get number_of_sentences first json */
        JSONArray jsonArray = new JSONArray();
        for (int i=0; i<choosen; i++) {
            try {
                jsonArray.put(i, mJsonObject.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(this, MyListActivity.class);



        System.out.println("choosen : " + numberOfSentencesChoosen + " " + choosen);

        intent.putExtra("json", mJsonObject.toString());
        intent.putExtra("number_of_sentences_choosen", choosen);
        intent.putExtra("language", languageChoosen);

        // TODO : change value below 8 to...
        intent.putExtra("number_of_sentences", 8);


        startActivityForResult(intent, 5);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //int note = data.getIntExtra("note", 0);
        if (data != null) {
            double note = data.getDoubleExtra("note", 0);
            Intent intent = new Intent();
            System.out.println("note number of sentences " + note);
            intent.putExtra("note", note);

            setResult(2, intent);

        }

        setResult(3);

        finish();

    }

    public static JSONArray shuffleJsonArray (JSONArray array) throws JSONException {
        // Implementing Fisher–Yates shuffle
        Random rnd = new Random();
        for (int i = array.length() - 1; i >= 0; i--)
        {
            int j = rnd.nextInt(i + 1);
            // Simple swap
            Object object = array.get(j);
            array.put(j, array.get(i));
            array.put(i, object);
        }
        return array;
    }
}