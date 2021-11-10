package com.example.foraddingtoserverio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NumberOfSentencesActivity extends AppCompatActivity {

    String languageChoosen;
    String ip;
    int port;
    JSONObject mJsonObject = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_of_sentences);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        JSONArray ja = null;
        try {
             mJsonObject = new JSONObject(getIntent().getStringExtra("json"));
             ja = mJsonObject.getJSONArray("sentences");
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
        textView2.setText(ja.length());

    }
/*
todo add port and ip
 */
    public void goToNextActivity(View view) {

        EditText ev = (EditText)findViewById(R.id.number_of_sentences);

        Intent intent = new Intent(this, MyListActivity.class);

        String numberOfSentencesChoosen = ev.getText().toString();

        intent.putExtra("json", mJsonObject.toString());
        intent.putExtra("number_of_sentences_choosen", numberOfSentencesChoosen);
        intent.putExtra("language", languageChoosen);

        // TODO : change value below 8 to...
        intent.putExtra("number_of_sentences", 8);


        startActivity(intent);

    }
}