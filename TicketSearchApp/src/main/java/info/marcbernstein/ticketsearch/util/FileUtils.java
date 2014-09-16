package info.marcbernstein.ticketsearch.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Util class for anything file related.
 */
public final class FileUtils {
  private static final String TAG = FileUtils.class.getSimpleName();

  // Private ctor so the util class can't be instantiated
  private FileUtils() {
  }

  /**
   * Convenience method to open a file from the assets directory, read it, and return the contents as a String.
   *
   * @param context       The context to use to open the file from the assets directory
   * @param assetFilename The name of the file in the assets directory
   * @return a String containing the contents of the asset file
   */
  public static String getAssetAsString(Context context, String assetFilename) {
    Preconditions.checkNotNull(context, "Context cannot be null.");
    Preconditions.checkArgument(!TextUtils.isEmpty(assetFilename), "Asset filename cannot be null or empty.");

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
}
