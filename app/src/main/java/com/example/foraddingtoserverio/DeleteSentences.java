package com.example.foraddingtoserverio;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static io.socket.client.Socket.EVENT_CONNECT;
public class DeleteSentences extends Activity {

    private Socket mySocket;
    CustomAdapterDel adapter;
    RecyclerView recyclerView;
    int[] deleted;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_activity);

        int num = getIntent().getIntExtra("number_to_delete", 0);

        try {
            mySocket = IO.socket("http://10.0.2.2:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mySocket.connect();



        mySocket.once(EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                mySocket.emit("number_to_delete", num);
                System.out.println("connected on service");



            }
        });

        String[] mothers = new String[num];
        String[] others = new String[num];
        deleted = new int[num];
        DeleteSentences del = this;

        mySocket.on("json_added", new Emitter.Listener() {


            @Override
            public void call(Object... args) {

                    JSONArray jsonO = (JSONArray)args[0];
                    System.out.println("------json_added------ " + jsonO.toString());
                    for (int i=0; i<jsonO.length(); i++) {
                        try {
                            mothers[i] = jsonO.getJSONObject(i).getString("mother");
                            others[i] = jsonO.getJSONObject(i).getString("other");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                del.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView = findViewById(R.id.simpleListView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(del));

                        // todo delete following line and do with Custom adapter
                        recyclerView.setItemViewCacheSize(mothers.length * 3);
                        adapter = new CustomAdapterDel(del, mothers, others);
                        //adapter.setClickListener(this);
                        recyclerView.setAdapter(adapter);
                        //final AtomicInteger i = new AtomicInteger(1);
                        recyclerView.addOnItemTouchListener(
                                new RecyclerItemClickListener(del, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override public void onItemClick(View view, int position) {
                                        // do whatever
                                    }

                                    @Override public void onLongItemClick(View view, int position) {
                                        String j = null;
                                        try {
                                            j = jsonO.getJSONObject(position).getString("id");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        int jj = Integer.parseInt(j);
                                        if (deleted[position] == 0) {
                                            view.setBackgroundColor(Color.RED);
                                            deleted[position] = jj;
                                            System.out.println("deleted : " + deleted.toString());

                                        } else {
                                            view.setBackgroundColor(Color.WHITE);
                                            deleted[position] = 0;

                                        }
                                    }
                                })
                        );
                    }
                });

            }
        });


    }

    public void done(View v) {
        String line = "";
        for(int i=0; i<deleted.length; i++) {
            line += deleted[i] + " ";
        }
        mySocket.emit("delete_several", line);
    }


}
