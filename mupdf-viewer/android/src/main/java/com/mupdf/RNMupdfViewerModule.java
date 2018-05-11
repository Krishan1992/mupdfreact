
package com.mupdf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RNMupdfViewerModule extends ReactContextBaseJavaModule {
  private static final String TEST_FILE_NAME = "sample.pdf";
  private final ReactApplicationContext reactContext;

  public RNMupdfViewerModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNMupdfViewer";
  }

  @ReactMethod
  public void openPdf(String filePath) {
    File foi = getFilesDirectory(getReactApplicationContext());
    File fii = new File(foi, TEST_FILE_NAME);


    //if (!fii.exists()) {
      copyTestDocToSdCard(fii,filePath);
   // }

    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getReactApplicationContext());
    sharedPrefs.edit().putString("prefKeyLanguage", "en").commit();

    File fo = getFilesDirectory(getReactApplicationContext());
    File fi = new File(fo, TEST_FILE_NAME);
    Uri uri = Uri.parse(fi.getAbsolutePath());

    //Uri uri = Uri.parse("file:///android_asset/" + TEST_FILE_NAME);
    Intent intent = new Intent(getCurrentActivity(), com.artifex.mupdflib.MuPDFActivity.class);
    intent.setAction(Intent.ACTION_VIEW);
    intent.setData(uri);

    //if document protected with password
    intent.putExtra("password", "encrypted PDF password");

    //if you need highlight link boxes
    intent.putExtra("linkhighlight", true);

    //if you don't need device sleep on reading document
    intent.putExtra("idleenabled", false);

    //set true value for horizontal page scrolling, false value for vertical page scrolling
    intent.putExtra("horizontalscrolling", true);

    //document name
    intent.putExtra("docname", "PDF document name");

    getCurrentActivity().startActivity(intent);
  }

  public static File getFilesDirectory(Context context) {
    File appFilesDir = null;
    if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
      appFilesDir = getExternalFilesDir(context);
    }
    if (appFilesDir == null) {
      appFilesDir = context.getFilesDir();
    }
    return appFilesDir;
  }

  private void copyTestDocToSdCard(final File testImageOnSdCard, final String filePath) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
         // File filePdf=new File(filePath);
          InputStream is =getCurrentActivity().getAssets().open(filePath);
          //InputStream is = new FileInputStream(filePdf);
          FileOutputStream fos = new FileOutputStream(testImageOnSdCard);
          byte[] buffer = new byte[8192];
          int read;
          try {
            while ((read = is.read(buffer)) != -1) {
              fos.write(buffer, 0, read);
            }
          } finally {
            fos.flush();
            fos.close();
            is.close();
          }
        } catch (IOException e) {

        }
      }
    }).start();
  }

  private static File getExternalFilesDir(Context context) {
    File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
    File appFilesDir = new File(new File(dataDir, context.getPackageName()), "files");
    if (!appFilesDir.exists()) {
      if (!appFilesDir.mkdirs()) {
        //L.w("Unable to create external cache directory");
        return null;
      }
      try {
        new File(appFilesDir, ".nomedia").createNewFile();
      } catch (IOException e) {
        //L.i("Can't create \".nomedia\" file in application external cache directory");
        return null;
      }
    }
    return appFilesDir;
  }
}