package info.marcbernstein.ticketsearch.util;

import android.content.Context;

import com.google.common.base.Preconditions;

import info.marcbernstein.ticketsearch.R;

public final class UiUtils {
  private UiUtils() {
  }

  /**
   * Returns whether or not the current device configuration is showing multiple panels.
   *
   * @param context The {@link android.content.Context} used to look up the resource
   * @return whether or not the current configuration shows multiple panels
   */
  public static boolean isMultiPanel(Context context) {
    Preconditions.checkNotNull(context, "Context cannot be null.");
    return context.getResources().getBoolean(R.bool.two_panels);
  }
}
