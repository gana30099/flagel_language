package com.example.foraddingtoserverio;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
public class MainActivity2 extends AppCompatActivity  {

    /*
    Will wgit
    ontains the languages available
     */

    private List<String> groupList = null;
    private Map<String, List<String>> childListMap = null;
    String selectedLanguage = "English";
    JSONArray jObject = null;
    Socket mySocket;
    //MyMockup myMockup = new MyMockup();

    public void setExpendable() {
        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter() {
            @Override
            public void registerDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public int getGroupCount() {
                return groupList.size();
            }

            @Override
            public int getChildrenCount(int groupIndex) {
                String group = groupList.get(groupIndex);
                List<String> childInfoList = childListMap.get(group);
                return childInfoList.size();
            }

            @Override
            public Object getGroup(int groupIndex) {
                return groupList.get(groupIndex);
            }

            @Override
            public Object getChild(int groupIndex, int childIndex) {
                String group = groupList.get(groupIndex);
                List<String> childInfoList = childListMap.get(group);
                return childInfoList.get(childIndex);
            }

            @Override
            public long getGroupId(int groupIndex) {
                return groupIndex;
            }

            @Override
            public long getChildId(int groupIndex, int childIndex) {
                return childIndex;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            // This method will return a View object displayed in group list item.
            @Override
            public View getGroupView(int groupIndex, boolean isExpanded, View view, ViewGroup viewGroup) {
                // Create the group view object.
                LinearLayout groupLayoutView = new LinearLayout(MainActivity2.this);
                groupLayoutView.setOrientation(LinearLayout.HORIZONTAL);

                // Create and add an imageview in returned group view.
                //ImageView groupImageView = new ImageView(ExpandableListViewActivity.this);
                /*if(isExpanded) {
                    groupImageView.setImageResource(R.mipmap.ic_launcher_round);
                }else
                {
                    groupImageView.setImageResource(R.mipmap.ic_launcher);
                }
                groupLayoutView.addView(groupImageView);*/

                // Create and add a textview in returned group view.
                String groupText = groupList.get(groupIndex);
                TextView groupTextView = new TextView(MainActivity2.this);
                groupTextView.setText(groupText);
                groupTextView.setTextSize(30);
                groupLayoutView.addView(groupTextView);

                return groupLayoutView;
            }

            // This method will return a View object displayed in child list item.
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public View getChildView(int groupIndex, int childIndex, boolean isLastChild, View view, ViewGroup viewGroup) {
                // First get child text/
                Object childObj = this.getChild(groupIndex, childIndex);
                String childText = (String)childObj;

                // Create a TextView to display child text.
                TextView childTextView = new TextView(MainActivity2.this);
                childTextView.setText(childText);
                childTextView.setTextSize(20);
                //childTextView.setBackgroundColor(Color.GREEN);

                // Get group image width.
                Drawable groupImage = getDrawable(R.mipmap.ic_launcher);
                int groupImageWidth = groupImage.getIntrinsicWidth();

                // Set child textview offset left. Then it will align to the right of the group image.
                childTextView.setPadding(groupImageWidth,0,0,0);

                return childTextView;
            }

            @Override
            public boolean isChildSelectable(int groupIndex, int childIndex) {
                return false;
            }

            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public void onGroupExpanded(int groupIndex) {

            }

            @Override
            public void onGroupCollapsed(int groupIndex) {


            }

            @Override
            public long getCombinedChildId(long groupIndex, long childIndex) {
                return 0;
            }

            @Override
            public long getCombinedGroupId(long groupIndex) {
                return 0;
            }
        };

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

                json(selectedLanguage);


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

    public String getProperty() {

        Properties prop = new Properties();
        AssetManager assetManager = this.getBaseContext().getAssets();
        String ip = null;
        String port = null;
        try (InputStream input = assetManager.open("server.properties")) {

            // load a properties file
            prop.load(input);



            ip = prop.getProperty("ip");
            port = prop.getProperty("port");



        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return "http://" + ip + ":" + port;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_plus_port_main);

        setTitle("Languages");
/*        AirshipConfigOptions config = AirshipConfigOptions.newBuilder();

// Set any custom values
        config.developmentAppKey = "DEV APP KEY"
        config.developmentAppSecret = "DEV APP SECRET"
        UAirship.takeOff();
        */

        /*

         */
        Notification.Builder builder = new Notification.Builder(this, NotificationChannel.EDIT_CONVERSATION)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("hi")
                .setContentText("how are you?")
                .setPriority(Notification.PRIORITY_HIGH);

        Notification not = builder.build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NotificationChannel.EDIT_CONVERSATION, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            boolean boolean1 = notificationManager.areNotificationsEnabled();
            notificationManager.notify(1, not);

        }

            try {
            String prop = getProperty();
            System.out.println(prop);
            mySocket = IO.socket(prop);
            mySocket.connect();
            MainActivity2 t = this;

            mySocket.emit("lobby", "");
            
            //json();

            t.addLanguageInfo("Spanish", 5, 10);

            mySocket.on("lobby_return", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
//                ma.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
                    Log.v("args ? ", args.toString());
                    JSONObject jo = (JSONObject) args[0];
                    JSONObject extended;



                    JSONArray ja = null;
                    try {

                        ja = jo.getJSONArray("the_languages");

                        for (int i = 0; i < ja.length(); i++) {
                            extended = ja.getJSONObject(i);
                            String lan = (String) extended.get("the_language");
                            int not = (int) extended.get("note");
                            int num = (int) extended.get("number");
                            t.addLanguageInfo(lan, not, num);
                        }

                        t.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("set expendable");
                                t.setExpendable();
                                //t.json();


                            }
                        });


                    } catch (JSONException e) {
                        Log.e("json exeption", ja.toString());
                    }
                }
                //});

            });



            mySocket.once(EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    t.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("connected");
                            t.enableButton();
                            //t.json();


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







        MainActivity2 ma = this;



        MyMockup mm = MyMockup.getInstance();
        //JSONObject jo = mm.getMyLanguagesMockup();

       /* JSONObject extended;

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

        }*/


        // Create an ExpandableListAdapter object, this object will be used to provide data to ExpandableListView.



    }

    /*
    todo : go to activity "select number of sentences"
     */
    public void goToNextActivity(View view) {
        Intent intent = new Intent(this, NumberOfSentencesActivity.class);

        // todo replace the mokup
        intent.putExtra("language", selectedLanguage);
        Log.v("go to next act -> json", MyMockup.getInstance().getMySentencesMockup().toString());
        System.out.println("array : " + jObject.toString());
        intent.putExtra("json", jObject.toString());

        //intent.putExtra("json", jObject.toString());

        // TODO : change value below 8 to... (not used in NumberOfSentencesActivity for now)
        intent.putExtra("number_of_sentences", 8);


        //startActivity(intent);
        startActivityForResult(intent, 4);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String s1 = data.getStringExtra("json");
            try {
                JSONArray ja = new JSONArray(s1);
                JSONObject jo = new JSONObject();
                jo.put("language", selectedLanguage);
                jo.put("sentences", ja);
                //mySocket.emit("add_sentences", selectedLanguage + " - " + ja);
                mySocket.emit("add_sentences",jo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //String s2 = data.getStringExtra("other");

            Log.v("onActivity result", s1);



        } else if (requestCode == 4) {  // todo return the note
            if (resultCode == 2) {
                // data => => <=
                double note;
                System.out.println(note = data.getDoubleExtra("note", 0));
                System.out.println("--------note---------" + note);
                mySocket.emit("put_note", selectedLanguage + " " + note);
            } else {
                // no data => <=
                System.out.println("no data => <=");
            }
        }


    }

    /*
        todo : add subElement of the languages as well as the element
         */
    private void addLanguageInfo(String language, int note, int number)
    {
        if(groupList == null)
        {
            groupList = new ArrayList<String>();
        }

        if(this.childListMap == null)
        {
            childListMap = new HashMap<String, List<String>>();
        }

        if(!groupList.contains(language)) {
            groupList.add(language);
        }

        // Create child list.
        List<String> childList = new ArrayList<String>();
        childList.add("Language : " + language);
        childList.add("Number of sentences : " + number);
        childList.add("Note : " + note);
        // Add child data list in the map, key is group name.
        childListMap.put(language, childList);

    }

    public void goToFragment(View view) {
        Intent intent = new Intent(this, FragmentAdd.class);

        // todo replace the mokup
        intent.putExtra("language", selectedLanguage);
        intent.putExtra("json", MyMockup.getInstance().getMyLanguagesMockup().toString());

        intent.putExtra("json", jObject.toString());

        startActivityForResult(intent, 1);

    }

    private void enableButton() {
        Button b1 = (Button)findViewById(R.id.next);
        Button b2 = (Button)findViewById(R.id.add2);
        Button b3 = (Button)findViewById(R.id.chart);
        b1.setEnabled(true);
        b2.setEnabled(true);
        b3.setEnabled(true);
        Toast.makeText(MainActivity2.this, "Connected", Toast.LENGTH_SHORT).show();
    }

    public void add_sentence(EditText mother, EditText other) {
    }

    public void goToChartActivity(View view) {
        // i have added a parameter else i have an error
        mySocket.emit("notes", selectedLanguage);

        mySocket.on("notes", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                goToChartActivity2(args[0]);
            }
        });



    }

    private void goToChartActivity2(Object... args) {
        Intent intent = new Intent(this, ChartActivity.class);

        System.out.println("args[0] " + args[0]);
        System.out.println("args[0].toString " + args[0].toString());


        // todo replace the mokup
        intent.putExtra("json", args[0].toString());

        //intent.putExtra("json", jObject.toString());

        startActivityForResult(intent, 3);
    }

    public void delete(View view) {
    }

    public void addALanguageYouWantLearn(View view) {
    }

    public void pull(View view) {
    }

    private final Object monitor = new Object();
    private boolean ready = false;

    /* contient emit */
    private void json(String selected) {

//        Object syntocken = new Object();
//        DataCreator dataCreator = new DataCreator(syntocken, this);
//        dataCreator.start();
        MainActivity2 ma = this;






        mySocket.emit("Language", selected);

        mySocket.on("json_all_included", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.v("args", args.toString());

                jObject = (JSONArray) args[0];
                //JSONArray jsonArray = jObject.getJSONArray();
                for (int i = 0; i < jObject.length(); i++) {
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
    }

}