package com.sam.gasapplication.utility;



import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sam.gasapplication.R;
import com.sam.gasapplication.adapter.GooglePlaceAdapter;
import com.sam.gasapplication.model.GooglePlaceModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

public class SearchMapActivity extends AppCompatActivity {
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyDGAULcU7yMUPZYyIlpuk1VE8PrBtYOFMo";
    EditText search;
    ImageView mic, clear, back;
    ArrayList<GooglePlaceModel> googlePlaceModels;
    RecyclerView listView;
    String searchValue, strGoogleApiKey = "";
    SharedPreferences sharedPreferences;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);

        sharedPreferences = SearchMapActivity.this.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
        strGoogleApiKey = sharedPreferences.getString("strGoogleApiKey", "");

        search = findViewById(R.id.search);
        listView = findViewById(R.id.listView);
        mic = findViewById(R.id.mic);
        clear = findViewById(R.id.clear);
        back = findViewById(R.id.back);
        googlePlaceModels = new ArrayList<>();
        //get search value from the MapsActivity

        RecyclerView.LayoutManager addItemManager = new LinearLayoutManager(SearchMapActivity.this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(addItemManager);
        listView.setItemAnimator(new DefaultItemAnimator());

        /*searchValue = getIntent().getStringExtra("searchValue");
        if (!searchValue.isEmpty()) {
            search.setText(searchValue);
            search.setSelection(search.getText().toString().length());
            mic.setVisibility(View.GONE);
            clear.setVisibility(View.VISIBLE);
        }*/
        //speech to text
        mic.setOnClickListener(v -> promptSpeechInput());

        //clear value
        clear.setOnClickListener(v -> {
            search.setText(null);
            googlePlaceModels.clear();
            setAdapter();
        });

        back.setOnClickListener(v -> finish());

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (search.getText().toString().isEmpty()) {
                    mic.setVisibility(View.VISIBLE);
                    clear.setVisibility(View.GONE);
                } else {
                    mic.setVisibility(View.GONE);
                    clear.setVisibility(View.VISIBLE);
                }

                if (s.toString().isEmpty()) {
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

        listView.addOnItemTouchListener(new RecyclerTouchListener(SearchMapActivity.this, listView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                try {
                    if (!googlePlaceModels.get(position).getPlaceName().equalsIgnoreCase("Not Found")) {

                        Log.e("bbhg", googlePlaceModels.get(position).getPlaceName());
                       /* Intent intent = new Intent(SearchMapActivity.this, LocationActivity.class);
                        intent.putExtra("place", googlePlaceModels.get(position).getPlaceName());
                        intent.putExtra("Latitude", googlePlaceModels.get(position).getLatitude());
                        intent.putExtra("Longitude", googlePlaceModels.get(position).getLongitude());

                        startActivity(intent);*/

                        BookingActivity.place = googlePlaceModels.get(position).getPlaceName();
                        finish();
                    }
                } catch (IndexOutOfBoundsException e) {
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    public void promptSpeechInput() {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.setText(result.get(0));
            search.setSelection(search.getText().toString().length());
        }
    }

    public class GooglePlaces extends AsyncTask<String, String, String> {

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                // Your API key
                String key = "?key=" + API_KEY;
                // components type
                //String components = "&components=country:us";
                //String components = "&components=country:in";
                String components = "&components=country:za";

                //set input type
                String input = "&input=" + URLEncoder.encode(params[0], "utf8");
                // Building the url to the web service
                String strURL = PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON + key + components + input;

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
                } else {
                    result = "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);
            googlePlaceModels.clear();

            if (s != null) {
                try {
                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("predictions");
                    Log.e("response", jsonObj.toString());

                    if (jsonObj.getString("status").equalsIgnoreCase("OK")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            GooglePlaceModel googlePlaceModel = new GooglePlaceModel();
                            googlePlaceModel.setPlaceName(jsonArray.getJSONObject(i).getString("description"));
                            googlePlaceModels.add(googlePlaceModel);
                        }
                    } else if (jsonObj.getString("status").equalsIgnoreCase("OVER_QUERY_LIMIT")) {
                        Toast.makeText(getApplicationContext(), "You have exceeded your daily request quota for this API.", Toast.LENGTH_LONG).show();
                        GooglePlaceModel googlePlaceModel = new GooglePlaceModel();
                        googlePlaceModel.setPlaceName("Not Found");
                        googlePlaceModels.add(googlePlaceModel);
                    } else {
                        GooglePlaceModel googlePlaceModel = new GooglePlaceModel();
                        googlePlaceModel.setPlaceName("Not Found");
                        googlePlaceModels.add(googlePlaceModel);
                    }
                    // set adapter
                    setAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
            }
        }
    }

    public void setAdapter() {
        GooglePlaceAdapter adapters = new GooglePlaceAdapter(SearchMapActivity.this, googlePlaceModels);
        listView.setAdapter(adapters);
    }
}
