package info.marcbernstein.ticketsearch.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.base.Preconditions;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import info.marcbernstein.ticketsearch.R;
import info.marcbernstein.ticketsearch.data.stubhub.model.Event;

/**
 * Util class for various UI related functions.
 */
public final class UiUtils {

  // Cache the formatter for the date strings as they come back from StubHub
  private static final DateTimeFormatter STUB_HUB_DATE_TIME_FORMAT =
      DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

  // Cache the formatter for how we want to display the date and time in the info window
  private static final DateTimeFormatter SANE_DATE_TIME_FORMAT = DateTimeFormat.forPattern("MMM dd, yyyy h:mma");

  // Private ctor so the util class can't be instantiated
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

  /**
   * Returns a transformed formatted date string from the event.
   *
   * @param event the {@link info.marcbernstein.ticketsearch.data.stubhub.model.Event} to get the date from
   * @return a transformed formatted date string
   */
  public static String getDateTimeString(Event event) {
    Preconditions.checkNotNull(event, "Event cannot be null.");

    DateTime dateTime = STUB_HUB_DATE_TIME_FORMAT.parseDateTime(event.getEventDateAsFormattedString());
    return SANE_DATE_TIME_FORMAT.print(dateTime);
  }

  /**
   * Returns a transformed date from the event as seconds since epoch.
   *
   * @param event the {@link info.marcbernstein.ticketsearch.data.stubhub.model.Event} to get the date from
   * @return a transformed date as seconds since epoch
   */
  public static long getDateTimeAsEpoch(Event event) {
    Preconditions.checkNotNull(event, "Event cannot be null.");

    DateTime dateTime = STUB_HUB_DATE_TIME_FORMAT.parseDateTime(event.getEventDateAsFormattedString());
    return dateTime.getMillis();
  }

  /**
   * Convenience method to check if this is the app's first launch. If so, it writes out the flag to the
   * SharedPreferences.
   *
   * @param context The Context to use to access the SharedPreferences
   * @return whether or not this is the app's first launch
   */
  public static boolean isFirstLaunch(Context context) {
    Preconditions.checkNotNull(context, "Context cannot be null.");

    SharedPreferences settings =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    boolean firstLaunch = settings.getBoolean("isFirstRun", true);

    if (firstLaunch) {
      SharedPreferences.Editor editor = settings.edit();
      editor.putBoolean("isFirstRun", false);
      editor.apply();
    }

    return firstLaunch;
  }
}
