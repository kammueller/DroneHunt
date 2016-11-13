package de.hackatum.mediasaturn.userapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import de.hackatum.mediasaturn.userapp.R;

/**
 * Created by Alexander Lannes on 13.11.2016.
 */
public class ScanActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan);
        super.onCreate(savedInstanceState);
    }



//    /* http://javapapers.com/core-java/java-qr-code/ */
//    public static String readQRCode(String filePath, String charset, Map hintMap)
//            throws FileNotFoundException, IOException, NotFoundException {
//        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
//                new BufferedImageLuminanceSource(
//                        ImageIO.read(new FileInputStream(filePath)))));
//        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
//                hintMap);
//        return qrCodeResult.getText();
//    }
}
