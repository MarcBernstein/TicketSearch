package info.marcbernstein.ticketsearch.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class FileUtils {
  private static final String TAG = FileUtils.class.getSimpleName();

  private FileUtils() {
  }

  public static String getAssetAsString(Context context, String assetFilename) {
    String result = null;

    try {
      InputStream stream = context.getAssets().open(assetFilename);
      result = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
      Closeables.closeQuietly(stream);
    } catch (IOException e) {
      Log.w(TAG, "Error while reading assets file.", e);
    }

    return result;
  }

  public static Bitmap bitmapFromBase64(String base64, boolean scaleUp) {
    byte[] imageAsBytes = Base64.decode(base64.getBytes(), Base64.DEFAULT);
    Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    if (scaleUp) {
      bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()*1.5), (int) (bitmap.getHeight()*1.5), true);
    }

    return bitmap;
  }
}
