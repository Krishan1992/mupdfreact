
package com.mupdfviewer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static android.app.Activity.RESULT_OK;

public class PdfDialogModule extends ReactContextBaseJavaModule implements ActivityEventListener {
  private final ReactApplicationContext reactContext;
  private Callback successCallback;
  private Callback cancelCallback;
  public PdfDialogModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    reactContext.addActivityEventListener(this);
  }

  @Override
  public String getName() {
    return "PdfDialog";
  }

  @ReactMethod
  public void openPdfDialog(Callback successCallback, Callback cancelCallback) {
    this.successCallback=successCallback;
    this.cancelCallback=cancelCallback;
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_GET_CONTENT);
    intent.setType("application/pdf");
    getCurrentActivity().startActivityForResult(intent,1212);
  }


  @Override
  public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case 1212:
        if (resultCode == RESULT_OK) {
          // Get the Uri of the selected file
          Uri uri = data.getData();
          String uriString = uri.toString();
          File myFile = new File(uriString);
          String path = myFile.getAbsolutePath();
          String displayName = null;

          if (uriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
              cursor = getCurrentActivity().getContentResolver().query(uri, null, null, null, null);
              if (cursor != null && cursor.moveToFirst()) {
                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
              }
            } finally {
              cursor.close();
            }
          } else if (uriString.startsWith("file://")) {
            displayName = myFile.getName();
          }
          String fullPath=null;
          if(uriString.startsWith("file://")) {
             fullPath = uriString.replace("file://","");
            try {
              fullPath= URLDecoder.decode(fullPath, "UTF-8");

            }
            catch (UnsupportedEncodingException e) {
              e.printStackTrace();
            }
          }
          if(fullPath!=null)
          successCallback.invoke(fullPath);
          else cancelCallback.invoke("Error to pick pdf file");

        }else{
          cancelCallback.invoke("Error to pick pdf file");
        }
        break;
    }
  }

  @Override
  public void onNewIntent(Intent intent) {

  }
}