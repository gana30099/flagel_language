package com.example.foraddingtoserverio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static io.socket.client.Socket.EVENT_CONNECT;

/* todo deprecated changed to fragment */
public class MainActivity extends AppCompatActivity {

    /*
    Will wgit
    ontains the languages available
     */

    private List<String> groupList = null;
    private Map<String, List<String>> childListMap = null;
    String selectedLanguage = "English";
    JSONArray jObject = null;
    //MyMockup myMockup = new MyMockup();

    public void setExpendable() {
        ExpandableListAdapter expandableListAdapter = new MyExpandableListAdapter(groupList, childListMap, this);

        final ExpandableListView expandableListView = (ExpandableListView)findViewById(R.id.ex_languages);
        expandableListView.setAdapter(expandableListAdapter);
        //expandableListView.

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupIndex) {
                // Get total group size.
                int groupListSize = groupList.size();
                Log.v("group index","" + groupIndex);
                if (groupIndex < 0) {
                    return;
                }
                selectedLanguage = (String)expandableListAdapter.getChild(groupIndex, 0);

                switch (selectedLanguage) {
                    case "Language : English" :
                        selectedLanguage = "English";
                        break;
                    case "Language : Russian" :
                        selectedLanguage = "Russian";
                        break;
                }


                /*int index = selectedLanguage.indexOf("English");
                if (index != -1) {
                    selectedLanguage.substring(index, 7);
                }*/
                //selectedLanguage = tv.getText().toString();
                // Close other expanded group.
                for(int i=0;i < groupListSize; i++) {
                    if(i!=groupIndex) {
                        expandableListView.collapseGroup(i);
                    }
                }
            }
        });
    }

    public void getPropertyAndSetEditText(EditText e1, EditText e2) {

        Properties prop = new Properties();
        AssetManager assetManager = this.getBaseContext().getAssets();
        try (InputStream input = assetManager.open("server.properties")) {

            // load a properties file
            prop.load(input);

            EditText et1 = findViewById(R.id.ip_field);
            EditText et2 = findViewById(R.id.port_field);

            et1.setText(prop.getProperty("ip"));
            et2.setText(prop.getProperty("port"));



        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_plus_port_main);
        setTitle("dev2qa.com --- Expandable List View Example.");

        MyMockup mm = MyMockup.getInstance();
        JSONObject jo = mm.getMyLanguagesMockup();

        JSONObject extended;

        JSONArray ja;
        try {

             ja = jo.getJSONArray("languages");

            for (int i = 0; i < ja.length(); i++) {
                extended = ja.getJSONObject(i);
                String lan = (String)extended.get("language");
                int not = (int)extended.get("note");
                int num = (int)extended.get("number");
                this.addLanguageInfo(lan, not, num);
            }
        } catch(JSONException e) {

        }

        // Create an ExpandableListAdapter object, this object will be used to provide data to ExpandableListView.



    }

    /*
    todo : go to activity "select number of sentences"
     */
    public void goToNextActivity(View view) {
        Intent intent = new Intent(this, NumberOfSentencesActivity.class);

        // todo replace the mokup
        intent.putExtra("language", selectedLanguage);
        intent.putExtra("json", MyMockup.getInstance().getMySentencesMockup().toString());

        //intent.putExtra("json", jObject.toString());

        // TODO : change value below 8 to... (not used in NumberOfSentencesActivity for now)
        intent.putExtra("number_of_sentences", 8);


        startActivity(intent);

    }

    /*
    todo : add subElement of the languages as well as the element
     */
    private void addLanguageInfo(String language, int note, int number)
    {
        if(this.groupList == null)
        {
            this.groupList = new ArrayList<String>();
        }

        if(this.childListMap == null)
        {
            this.childListMap = new HashMap<String, List<String>>();
        }

        if(!this.groupList.contains(language)) {
            this.groupList.add(language);
        }

        // Create child list.
        List<String> childList = new ArrayList<String>();
        childList.add("Language : " + language);
        childList.add("Number of sentences : " + number);
        childList.add("Note : " + note);
        // Add child data list in the map, key is group name.
        this.childListMap.put(language, childList);

    }

   public void goToFragment(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, FragmentAdd.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name") // name can be null
                .commit();

    }

    private void enableButton() {
        Button b1 = (Button)findViewById(R.id.next);
        Button b2 = (Button)findViewById(R.id.add2);
        b1.setEnabled(true);
        b2.setEnabled(true);
        Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
    }

    public void connect(View view) {

        final Socket mySocket;

        EditText editText = (EditText) findViewById(R.id.ip_field);
        EditText editText2 = (EditText) findViewById(R.id.port_field);

        String ip = editText.getText().toString();
        String port = editText2.getText().toString();

        try {
            mySocket = IO.socket("http://" + ip + ":" + port);
            mySocket.connect();
            MainActivity t = this;
            mySocket.once(EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    t.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            t.enableButton();
                        }
                    });
                }
            });

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        // return false cause of multi threading : so not used
        System.out.println("connected ? " + mySocket.connected());
        /*if (mySocket.connected()){

        }*/

        mySocket.emit("Language", selectedLanguage);

        mySocket.on("json_all_included", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.v("args", args.toString());

                    jObject = (JSONArray)args[0];
                    //JSONArray jsonArray = jObject.getJSONArray();
                    for (int i=0; i < jObject.length(); i++) {
                        try {
                            JSONObject oneObject = jObject.getJSONObject(i);
                            // Pulling items from the array
                            String oneObjectsItem = oneObject.getString("mother");
                            String oneObjectsItem2 = oneObject.getString("other");

                           /* System.out.println("mother : " + oneObjectsItem);
                            System.out.println("other : " + oneObjectsItem2);*/

                        } catch (JSONException e) {
                            // Oops
                        }
                    }

            }
        });

        //mySocket.emit("new message", mother.getText().toString() + ", " + other.getText().toString());
    }

    public void add_sentence(EditText mother, EditText other) {
    }
}