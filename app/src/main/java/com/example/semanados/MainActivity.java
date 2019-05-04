package com.example.semanados;

import android.content.ComponentName;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mButtonDownload = (Button) findViewById(R.id.button_download);
        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mButtonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getURL();
                String result = "";
                if (hasInternetAccess()) {
                    Toast.makeText(getApplicationContext(), R.string.access_internet, Toast.LENGTH_SHORT).show();
                    result = downloadContent(url);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.no_access, Toast.LENGTH_SHORT).show();
                }
                setEditTextCont(result);
            }
        });
    }

    public boolean hasInternetAccess() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private String downloadContent(String url) {
        String response = "";
        try {
            URL urlToFetch = new URL(url);
            HttpURLConnection ulrConnection = (HttpURLConnection) urlToFetch.openConnection();
            InputStream stream = ulrConnection.getInputStream();
            response = readStream(stream);
            ulrConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String readStream(InputStream stream) throws IOException {
        Reader reader = new InputStreamReader(stream);
        BufferedReader buffer = new BufferedReader(reader);
        String response = "";
        String chunkJustRead = "";
        while ((chunkJustRead = buffer.readLine()) != null) {
            response += chunkJustRead;
        }
        return response;
    }

    private String getURL() {
        EditText editText = (EditText) findViewById(R.id.editText_url);
        return editText.getText().toString();
    }

    private void setEditTextCont(String value) {
        EditText editText = (EditText) findViewById(R.id.editText_content);
        editText.setText(value);
    }
}
