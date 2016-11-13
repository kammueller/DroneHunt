package de.hackatum.mediasaturn.userapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hackatum.mediasaturn.userapp.R;
import de.hackatum.mediasaturn.userapp.logic.UserLogic;

/**
 * In this activity is the logic written used for the registration of the user to the server
 * <p>
 * Created by Alexander Lannes on 11.11.2016.
 */

public class RegisterActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(de.hackatum.mediasaturn.userapp.R.layout.activity_register);

        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendUsername();
            }
        });

    }

    public void sendUsername() {
        EditText editText = (EditText) findViewById(R.id.userNameInput);
        final String username = editText.getText().toString();
        AsyncTask at = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                JSONObject token;
                try {
                    String url = "http://192.168.137.166:9000/register";

                    JSONObject json = new JSONObject();
	        	    json.put("username", username);
		            JsonRepresentation rep = new JsonRepresentation(json);
	    	        token = new JSONObject(new ClientResource(url).post(rep).getText());
                    writeTokenFile(token.getString("token"));

                } catch (Exception ex) {
                    token = new JSONObject();
                }
                return token;
            }

        };
        at.execute();
    }

    public void writeTokenFile(String token) {
        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(sdcard, "token.txt");

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.print(token);
            pw.flush();
            pw.close();
            f.close();
            switchToMain();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
