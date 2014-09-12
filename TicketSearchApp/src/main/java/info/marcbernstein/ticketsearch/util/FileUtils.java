package info.marcbernstein.ticketsearch.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class FileUtils {
  private static final String TAG = FileUtils.class.getSimpleName();

  private FileUtils() {
  }

  public static String getAssetAsString(Context context, String assetFilename) {
    StringBuilder buf = new StringBuilder();

    try {
      InputStream json = context.getAssets().open(assetFilename);
      BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
      String str;

      while ((str = in.readLine()) != null) {
        buf.append(str);
      }

      in.close();
    } catch (IOException e) {
      Log.w(TAG, "Error while reading assets file.", e);
    }

    return buf.toString();
  }
}
