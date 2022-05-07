package com.example.foraddingtoserverio;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyListActivity extends AppCompatActivity {
    CustomAdapter adapter;
    RecyclerView recyclerView;
    double note = 0;
    double total = 0;
    double noteFinal = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_adapter_test);

        // data to populate the RecyclerView with

        Intent intent = getIntent();
        JSONArray mJsonObject;
        String[] mothers = null;
        String[] hidden = null;
        try {
            mJsonObject = new JSONArray(getIntent().getStringExtra("json"));
            total = getIntent().getIntExtra("number_of_sentences_choosen", 0);

            System.out.println("total : " + total);
            //JSONArray ja = mJsonObject.getJSONArray("sentences");
            mothers = new String[mJsonObject.length()];
            hidden = new String[mJsonObject.length()];
            for (int i=0; i<mJsonObject.length(); i++) {

                JSONObject jo = mJsonObject.getJSONObject(i);
                System.out.println("jsonobj : " + jo);
                String mother = jo.getString("mother");
                String other = jo.getString("other");
                mothers[i] = mother;
                hidden[i] = other;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        // set up the RecyclerView
        recyclerView = findViewById(R.id.simpleListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter(this, mothers, hidden);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    public void myMethod(View view)  {

        ((Button)view).setEnabled(false);

       /* I/System.out: ---------- v.lenght : {correct=Apple, name=Apple}
        I/System.out: ---------- v.lenght : {correct=Banana, name=Banana}
        I/System.out: ---------- v.lenght : {correct=Litchi, name=Litchi}
        I/System.out: ---------- v.lenght : {correct=Mango, name=Mango}
        I/System.out: ---------- v.lenght : {correct=PineApple, name=PineApple}*/
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (recyclerView.getChildAt(i) == null) {
                return;
            }
            EditText e = (EditText)recyclerView.getChildAt(i).findViewById(R.id.other);
            TextView t = (TextView)recyclerView.getChildAt(i).findViewById(R.id.invisible);

            //simpleAdapter.setViewText();
            System.out.println("---------- .getText().toString(): " + t.getText().toString());
            System.out.println(e.getText().toString() + "!VS!" + t.getText().toString());

            if (e.getEditableText().toString().equals(t.getText().toString())) {
                note++;
                System.out.println("note/total : " + note + "/" + total);
            } else {
                String sourceString = "<p style=\"color:red\">" + e.getText().toString() + " | " + t.getText().toString() + "</p>";
                e.setText(Html.fromHtml(sourceString));
            }
        }
        noteFinal = Math.round((note/total)*10);
        System.out.println(noteFinal);

    }

    public void correctAll(View view) {

    }

    public void goBack(View view) {
        Intent intent = new Intent();

        intent.putExtra("note", noteFinal);
        setResult(2, intent);

        finish();
    }
}
