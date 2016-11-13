package hackatum.drohnenhandy;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;

import org.json.JSONObject;
import org.restlet.resource.ClientResource;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AsyncTask at = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    JSONObject json = new JSONObject(new ClientResource("http://192.168.137.166:9000/createChallenge").post("").getText());
                    String QRCodeDataString = json.getString("secret");

                    // QRCodeEncoder --------------------

                    QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(QRCodeDataString,
                            null,
                            Contents.Type.TEXT,
                            BarcodeFormat.QR_CODE.toString(),
                            500);
                    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();

                    return bitmap;
                } catch(Exception ex) {
                    Log.e("jhgfd", ex.getMessage(), ex);
                    //
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object bitmap) {
                if (!(bitmap == null)){
                    ImageView myImage = (ImageView) findViewById(R.id.imageView1);
                    myImage.setImageBitmap((Bitmap) bitmap);
                }
            }
        };
        at.execute();

        AsyncTask at2 = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                boolean somebodyWon = false;
                while(!somebodyWon){
                    try  {
                        Thread.sleep(1000);

                        JSONObject json = new JSONObject(new ClientResource("http://192.168.137.166:9000/winnerExists").get().getText());
                        somebodyWon = json.getBoolean("winnerExists");
                        Log.e("Returnvalue:", Boolean.toString(somebodyWon));
                    } catch(Exception ex) {
                        Log.e("Failed GET winnerexists", ex.getMessage());

                        return null;}
                    //  Get User won

                   /* try {
                        JSONObject json = new JSONObject(new ClientResource("http://192.168.137.166:9000/winnerExists").get().getText());
                        somebodyWon = json.getBoolean("winnerExists");
                        Log.e("Returnvalue:", Boolean.toString(somebodyWon));
                    }
                    catch (Exception e){
                        Log.e("Failed GET winnerexists", e.getMessage());
                    }*/
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                setContentView(R.layout.congrats);
                // show congratulation
            }
        };
        at2.execute();
    }
}
