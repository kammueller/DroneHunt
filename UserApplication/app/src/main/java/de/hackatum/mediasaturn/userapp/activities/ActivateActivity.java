package de.hackatum.mediasaturn.userapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;

import de.hackatum.mediasaturn.userapp.R;
import de.hackatum.mediasaturn.userapp.utils.Contents;
import de.hackatum.mediasaturn.userapp.utils.QRCodeEncoder;

/**
 * In this activity the logic needed for the activation of an user key is written.
 * <p>
 * Created by Alexander Lannes on 12.11.2016.
 */

public class ActivateActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showQR(getIntent().getStringExtra("token"));

        setContentView(R.layout.activity_acvtivate);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startRequest(getIntent().getStringExtra("token"));
            }
        }, 10000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        }, 11000);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showQR(final String token) {
        AsyncTask at = new AsyncTask() {
            String url = "http://192.168.137.166:9000/resolveToken";

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(token,
                            null,
                            Contents.Type.TEXT,
                            BarcodeFormat.QR_CODE.toString(),
                            500);
                    Bitmap bitmap = null;
                    try {
                        bitmap = qrCodeEncoder.encodeAsBitmap();
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                    return bitmap;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Object bitmap) {
                if (!(bitmap == null)) {
                    ImageView myImage = (ImageView) findViewById(R.id.imageView3);
                    myImage.setImageBitmap((Bitmap) bitmap);
                }
            }
        };
        at.execute();
    }

    private void startRequest(final String token) {
        AsyncTask at = new AsyncTask() {
            String url = "http://192.168.137.166:9000/activate";

            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    JSONObject json = new JSONObject();
                    json.put("token", token);
                    JsonRepresentation rep = new JsonRepresentation(json);
                    new ClientResource(url).post(rep).getText();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return "";
            }
        };
        at.execute();
    }
}
