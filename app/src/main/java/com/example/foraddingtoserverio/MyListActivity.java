package com.example.foraddingtoserverio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyListActivity extends AppCompatActivity {
    CustomAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_adapter);

        // data to populate the RecyclerView with

        Intent intent = getIntent();
        JSONObject mJsonObject;
        String[] mothers = {};
        String[] hidden = {};
        try {
            mJsonObject = new JSONObject(getIntent().getStringExtra("json"));
            JSONArray ja = mJsonObject.getJSONArray("sentences");
            for (int i=0; i<ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
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

            } else {
                e.setText(e.getText().toString() + "!CORRECTION!" + t.getText().toString());
            }
        }

    }

    public void correctAll(View view) {
    }
}
