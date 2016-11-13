package de.hackatum.mediasaturn.userapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;

import java.io.BufferedInputStream;
import java.io.InputStream;

import de.hackatum.mediasaturn.userapp.R;

/**
 * Activity for recieving hints
 * <p>
 * Created by Alexander Lannes on 13.11.2016.
 */
public class HintActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AsyncTask at = getAsyncTask();
        at.execute();
        setContentView(R.layout.activity_hint);
        final Button refreshButton = (Button) findViewById(R.id.button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getAsyncTask().execute();
                refreshButton.setActivated(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshButton.setActivated(true);
                    }
                }, 5000);
            }
        });
        Button scanButton = (Button) findViewById(R.id.button2);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HintActivity.super.getApplicationContext(), ScanActivity.class);
                startActivity(intent);
            }
        });
    }

    private AsyncTask getAsyncTask() { return new AsyncTask() {
        String url = "http://192.168.137.166:9000/currentHint";

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                InputStream input = new ClientResource(url).get(MediaType.IMAGE_JPEG).getStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(input);

                Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                return bitmap;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object bitmap) {
            if (!(bitmap == null)) {
                ImageView myImage = (ImageView) findViewById(R.id.imageView2);
                myImage.setImageBitmap((Bitmap) bitmap);
            }
        }

    };
    }
}