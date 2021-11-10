package com.example.foraddingtoserverio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyMockup {

    private static volatile MyMockup instance = null;

    //private JSONArray myMockup = new JSONArray();
    private JSONObject mySentencesMockup = new JSONObject();
    private JSONObject myLanguagesMockup = new JSONObject();

    public final static MyMockup getInstance() {
        //Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet
        //d'éviter un appel coûteux à synchronized,
        //une fois que l'instanciation est faite.
        if (MyMockup.instance == null) {
            // Le mot-clé synchronized sur ce bloc empêche toute instanciation
            // multiple même par différents "threads".
            // Il est TRES important.
            synchronized(MyMockup.class) {
                if (MyMockup.instance == null) {
                    MyMockup.instance = new MyMockup();
                }
            }
        }
        return MyMockup.instance;
    }

    private MyMockup() {


        /*
        Set Sentences mockup
         */
        JSONObject rootObject = new JSONObject();

        JSONObject myObject = new JSONObject();
        JSONObject myObject2 = new JSONObject();
        JSONArray ja = new JSONArray();
        try {

            rootObject.put("sentences", ja);
            //ja = rootObject.getJSONArray("sentencces");

            myObject.put("mother", "bonjour le monde");
            myObject.put("other", "hellow world");

            myObject.put("mother", "salut");
            myObject.put("other", "hi");

            ja.put(myObject);
            ja.put(myObject2);

            mySentencesMockup.put("sentences", ja);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        Set languaeg mockup
         */
        JSONObject rootObject2 = new JSONObject();

        JSONObject myObject21 = new JSONObject();
        JSONObject myObject22 = new JSONObject();
        JSONArray ja2 = new JSONArray();

        JSONObject infosExtended = new JSONObject();
        JSONObject infosExtended2 = new JSONObject();
        JSONObject infosExtended3 = new JSONObject();

        JSONObject infosExtended21 = new JSONObject();
        JSONObject infosExtended22 = new JSONObject();
        JSONObject infosExtended33 = new JSONObject();

        try {

            rootObject2.put("languages", ja2);

            infosExtended.put("number", 2);
            infosExtended.put("note", 2);
            infosExtended.put("language", "English");

            infosExtended2.put("number", 0);
            infosExtended2.put("note", 0);
            infosExtended2.put("language", "Russian");

            ja2.put(infosExtended);
            ja2.put(infosExtended2);

            myLanguagesMockup.put("languages", ja2);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public JSONObject getMySentencesMockup() {
        return mySentencesMockup;
    }

    public JSONObject getMyLanguagesMockup() {
        return mySentencesMockup;
    }
}
