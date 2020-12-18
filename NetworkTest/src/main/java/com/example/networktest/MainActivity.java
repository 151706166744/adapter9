package com.example.networktest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView responseText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequest = (Button) findViewById(R.id.send_request);
        responseText = (TextView) findViewById(R.id.response_text);
        sendRequest.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.send_request) {
            /*sendRequestWithHttpURLConnection();*/
            sendRequestWithOkhttp();
        }
    }

    private void sendRequestWithOkhttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://10.0.2.2/get_data.json")
                            .build();
                    Response response = client.newCall(request).execute();
                    String reponseData = response.body().string();
                    praseJSONWithGSON(reponseData);
                    /*praseJSONWithJSONObject(reponseData);*/
                    /*showResponse(reponseData);*/
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void praseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        List<App> appList= gson.fromJson(jsonData, new TypeToken<List<App>>(){}.getType());
        for(App app:appList){
            Log.d("MainActivity","name is"+ app.getName());
            Log.d("MainActivity","age is"+ app.getAge());
            Log.d("MainActivity","sex is"+ app.getSex());
        }
    }

    private void praseJSONWithJSONObject(String jsonData) {
        Log.d("MainActivity","sex is"+"sex");
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i <jsonArray.length() ; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String age = jsonObject.getString("age");
                String sex = jsonObject.getString("sex");
                Log.d("MainActivity","name is"+ name);
                Log.d("MainActivity","age is"+ age);
                Log.d("MainActivity","sex is"+ sex);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void sendRequestWithHttpURLConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    URL url = new URL("http://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
;                    }
                    showResponse(response.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader !=null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if ((connection!=null)) {
                        connection.disconnect();
                    }
                }

            }
        }).start();
    }
    private void showResponse(String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }
}