package com.example.foraddingtoserverio;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Intent intent = getIntent();

        String notes = intent.getStringExtra("json");
        System.out.println(notes);

        XYPlot plot = (XYPlot) findViewById(R.id.chart);
        //plot.getLayoutParams().height = 100;
        //plot.getLayoutParams().width = 100;
        plot.measure(150,150);
        plot.layout(0,0,150,150);
        plot.setDrawingCacheEnabled(true);


        JSONObject jo;
        LinearLayout buttons = (LinearLayout) findViewById(R.id.layout_notes);
        Number[][] series1Numbers1 = new Number[2][10];
        try {
            jo = new JSONObject(notes);
            JSONArray jo2 = jo.getJSONArray("languages");

            for (int y = 0; y<jo2.length(); y++) {
                JSONObject jo3 = jo2.getJSONObject(y);
                String s = jo3.getString("language");
                JSONArray jsa = jo3.getJSONArray("notes");
                for (int i = 0; i < jsa.length(); i++) {
                    series1Numbers1[y][i] = jsa.getInt(i);
                    System.out.println(series1Numbers1[y][i] + ", ");




                }

                final XYSeries serie1 = new SimpleXYSeries(
                        Arrays.asList(series1Numbers1[y]),          // SimpleXYSeries takes a List so turn our array into a List
                        SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                        "Series1");                             // Set the display title of the series

                // same as above
                //XYSeries series2 = new SimpleXYSeries(Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2");

                // Create a formatter to use for drawing a series using LineAndPointRenderer:
                LineAndPointFormatter series1Format = new LineAndPointFormatter();

                LineAndPointFormatter lineAndPointFormatter = new LineAndPointFormatter();// fill color (none)

                // add a new series' to the xyplot:
                plot.addSeries(serie1, series1Format);


                Switch sw = new Switch(getBaseContext());
                sw.setChecked(true);

                sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!sw.isChecked())
                            plot.removeSeries(serie1);
                        else
                            plot.addSeries(serie1, series1Format);
                        plot.redraw();
                    }
                });

                TextView tv = new TextView(getBaseContext());
                tv.setText(s);
                buttons.addView(tv);
                buttons.addView(sw);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        // Turn the above arrays into XYSeries':


        // same as above:
        //plot.addSeries(series2, new LineAndPointFormatter());


        // reduce the number of range labels
//            plot.setTicksPerRangeLabel(3);

        // by default, AndroidPlot displays developer guides to aid in laying out your plot.
        // To get rid of them call disableAllMarkup():
        //plot.disableAllMarkup();
//
//        Bitmap bmp = plot.getDrawingCache();
//
//        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
//        //rv.setBitmap(R.id.imgView, "setImageBitmap", bmp);
//        appWidgetManager.updateAppWidget(widgetId, rv);

    }
}