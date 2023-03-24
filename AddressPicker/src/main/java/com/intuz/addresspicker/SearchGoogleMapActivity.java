package com.intuz.addresspicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchGoogleMapActivity extends AppCompatActivity
{
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyDGAULcU7yMUPZYyIlpuk1VE8PrBtYOFMo";
    EditText search;
    ImageView mic, clear, back;
    ArrayList<GooglePlaceModels> googlePlaceModels;
    ListView listView;
    String searchValue;
    private boolean isFirst = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_google_map);

        search = (EditText) findViewById(R.id.search);
        listView = (ListView) findViewById(R.id.listView);
        mic = (ImageView) findViewById(R.id.mic);
        clear = (ImageView) findViewById(R.id.clear);
        back = (ImageView) findViewById(R.id.back);
        googlePlaceModels = new ArrayList<>();
        // get search value from the MapsActivity
        searchValue = getIntent().getStringExtra("searchValue");
        if(!searchValue.isEmpty()) {;
            search.setText(searchValue);
            search.setSelection(search.getText().toString().length());
            mic.setVisibility(View.GONE);
            clear.setVisibility(View.VISIBLE);
        }

        // speech to text
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        // clear value
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText(null);
                googlePlaceModels.clear();
                setAdapter();
            }
        });

        // go to another activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchGoogleMapActivity.this, LocationPickerActivity.class);
                intent.putExtra("place", search.getText().toString());
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(search.getText().toString().isEmpty()) {
                    mic.setVisibility(View.VISIBLE);
                    clear.setVisibility(View.GONE);
                } else {
                    mic.setVisibility(View.GONE);
                    clear.setVisibility(View.VISIBLE);
                }

                if(s.toString().isEmpty()) {
                    googlePlaceModels.clear();
                    setAdapter();
                } else {
                    new GooglePlaces().execute(String.valueOf(s));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!googlePlaceModels.get(position).getPlaceName().equalsIgnoreCase("Not Found")) {

                    Log.e("bbhg",googlePlaceModels.get(position).getPlaceName());
                    Intent intent = new Intent(SearchGoogleMapActivity.this, LocationPickerActivity.class);
                    intent.putExtra("place", googlePlaceModels.get(position).getPlaceName());
                    intent.putExtra("Latitude", googlePlaceModels.get(position).getLatitude());
                    intent.putExtra("Longitude", googlePlaceModels.get(position).getLongitude());
                    intent.putExtra("Longitude", googlePlaceModels.get(position).getLongitude());

                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            }
        });
    }

    public void promptSpeechInput() {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"speak here");
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode == RESULT_OK && null != data) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.setText(result.get(0));
            search.setSelection(search.getText().toString().length());
        }
    }

    public class GooglePlaces extends AsyncTask<String, String, String> {

        @Override
        public void onPreExecute() {
            super .onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                // Your API key
                String key="?key=" + API_KEY;
                // components type
                String components="&components=country:in";
               // String components="&components=country:za";
                // set input type
                String input="&input=" + URLEncoder.encode(params[0], "utf8");
                // Building the url to the web service
                String strURL = PLACES_API_BASE+TYPE_AUTOCOMPLETE+OUT_JSON+key+components+input;


                URL url = new URL(strURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String temp;

                    while ((temp = reader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }
                    result = stringBuilder.toString();
                }else  {
                    result = "error";
                }

            } catch (Exception  e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        public void onPostExecute(String s) {
            super .onPostExecute(s);
            googlePlaceModels.clear();
            try {
                JSONObject jsonObj = new JSONObject(s);
                JSONArray jsonArray = jsonObj.getJSONArray("predictions");
                Log.e("response", jsonObj.toString());

                if (jsonObj.getString("status").equalsIgnoreCase("OK")) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        GooglePlaceModels googlePlaceModel = new GooglePlaceModels();
                        googlePlaceModel.setPlaceName(jsonArray.getJSONObject(i).getString("description"));
                        getLocationFromAddress(SearchGoogleMapActivity.this,jsonArray.getJSONObject(i).getString("description"));
                        googlePlaceModels.add(googlePlaceModel);
                    }
                } else if   (jsonObj.getString("status").equalsIgnoreCase("OVER_QUERY_LIMIT")) {
                    Toast.makeText(getApplicationContext(), "You have exceeded your daily request quota for this API.", Toast.LENGTH_LONG).show();
                    GooglePlaceModels googlePlaceModel = new GooglePlaceModels();
                    googlePlaceModel.setPlaceName("Not Found");
                    googlePlaceModels.add(googlePlaceModel);
                } else {
                    GooglePlaceModels googlePlaceModel = new GooglePlaceModels();
                    googlePlaceModel.setPlaceName("Not Found");
                    googlePlaceModels.add(googlePlaceModel);
                }
                // set adapter
                setAdapter();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public LatLng getLocationFromAddress(Context context, String strAddress)
    {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            GooglePlaceModels googlePlaceModel = new GooglePlaceModels();

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
            googlePlaceModel.setLatitude(String.valueOf(location.getLatitude()));
            googlePlaceModel.setLongitude(String.valueOf(location.getLatitude()));


            Toast.makeText(context, "Latitute"+location.getLatitude()+"Longitute"+location.getLongitude(), Toast.LENGTH_SHORT).show();

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public void setAdapter() {
        GooglePlaceAdapters adapter = new GooglePlaceAdapters(SearchGoogleMapActivity.this, googlePlaceModels);
        listView.setAdapter(adapter);
    }
}
