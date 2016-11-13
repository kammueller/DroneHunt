package de.hackatum.mediasaturn.userapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import de.hackatum.mediasaturn.userapp.R;

/**
 * Although this activity isn't the start activity, it is the main activity. Here gets checked if there is a valid token for the user.
 * If so the actual logic with the hints is started. If not the needed activity gets started
 * <p>
 * Created by Alexander Lannes on 11.11.2016.
 */

public class MainActivity extends Activity {

    private static final String FILENAME = "token.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String token = readTokenFile();
        //if token is still empty the user has to register
        if (token.isEmpty()) {
            Intent registerIntent = new Intent(MainActivity.super.getApplicationContext(), RegisterActivity.class);
            startActivity(registerIntent);
            return;
        }
        isActivated(token);
        //activity for main part with hints and so on
        setContentView(R.layout.activity_main);
    }

    private void isActivated(final String token) {
        AsyncTask at = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    String url = "http://192.168.137.166:9000/isActive";
                    JSONObject json = new JSONObject();
		            json.put("token", params[0]);
		            JsonRepresentation rep = new JsonRepresentation(json);
                    JSONObject seconds = new JSONObject(new ClientResource(url).post(rep).getText());
                    int secondsValid = Integer.parseInt(seconds.getString("secondsValid"));
                    if(secondsValid <= 0) {
                        Intent intent = new Intent(MainActivity.super.getApplicationContext(), ActivateActivity.class);
                        intent.putExtra("token", token);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MainActivity.super.getApplicationContext(), HintActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        };
        at.execute(token);
    }

    public String readTokenFile() {
        String token = "";
        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(sdcard, "token.txt");

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();

            while (line != null) {
                text.append(line);
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
        if (!text.toString().isEmpty()) {
            token = text.toString();
        }
        return token;
    }
}
