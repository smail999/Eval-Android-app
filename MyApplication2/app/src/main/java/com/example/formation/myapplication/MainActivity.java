package com.example.formation.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import com.example.formation.myapplication.quackeapi.Feature;
import com.example.formation.myapplication.quackeapi.Properties;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;



    // URL to get contacts JSON
    private static String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-02";


    ArrayList<Feature> seismeList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seismeList = new  ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new GetContacts().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }


        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray seismee = jsonObj.getJSONArray("features");

                    // looping through All Seismes
                    for (int i = 0; i < seismee.length(); i++) {
                        JSONObject eq = seismee.getJSONObject(i);

                        JSONObject properties = eq.getJSONObject("properties");

                        String mag = properties.getString("mag");
                        String place = properties.getString("place");

                        String time = properties.getString("time");
                        String url = properties.getString("url");


                        // Get place
                        String distanceAndPlace[] = place.split("\\s");
                        if (distanceAndPlace.length > 3) {
                            place = "To: \n" + distanceAndPlace[0] + " \n\nFrom: \n" + distanceAndPlace[1];
                            for(int j = 3; j < distanceAndPlace.length; j++) {
                                place = place + " " + distanceAndPlace[j];
                            }
                        }


                        // tmp hash map for single contact
                        Properties mproperties = new Properties();

                        // adding each child node to HashMap key => value

                        mproperties.setmMag(Double.parseDouble(mag));
                        mproperties.setmPlace(place);

                        mproperties.setmTime(Long.parseLong(time));
                        mproperties.setmUrl(url);

                        Feature feature = new Feature();

                        feature.setmProperties(mproperties);
                        // adding seism to seism list
                        seismeList.add(feature);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

            final QuakeAdapter adapter = new QuakeAdapter(MainActivity.this,seismeList);
            lv.setAdapter(adapter);
        }

    }
}