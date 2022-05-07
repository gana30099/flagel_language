package com.example.foraddingtoserverio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentAdd extends AppCompatActivity {
    {
        
    }
    JSONArray jsonArray = new JSONArray();

    public FragmentAdd() {
        super(R.layout.add_fragment);
    }

    /*
    maybe called by the activity and then failed
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_fragment);

        setTitle("Expandable List View Example.");

        Button b = (Button) findViewById(R.id.add);

        b.setOnClickListener(new View.OnClickListener() {

            EditText mother = (EditText) findViewById(R.id.mother);
            EditText other = (EditText) findViewById(R.id.foreign);

            @Override
            public void onClick(View v) {


                JSONObject jo = new JSONObject();

                try {
                    jo.put("mother", mother.getText().toString());
                    jo.put("other", other.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonArray.put(jo);



                //((MainActivity) v.getContext()).makeText(mother.getText().toString());
                // Write data to the output stream of the Client Socket.
            }


        });
    }

    public void goBack(View view) {
        Intent intent = new Intent();

        intent.putExtra("json", jsonArray.toString());
        setResult(2, intent);
        finish();
    }
}