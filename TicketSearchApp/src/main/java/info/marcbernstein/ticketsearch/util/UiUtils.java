package info.marcbernstein.ticketsearch.util;

import android.content.Context;

import com.google.common.base.Preconditions;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import info.marcbernstein.ticketsearch.R;
import info.marcbernstein.ticketsearch.data.stubhub.model.StubHubResponse;

public final class UiUtils {

  private static final DateTimeFormatter STUB_HUB_DATE_TIME_FORMAT =
      DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
  public static final DateTimeFormatter SANE_DATE_TIME_FORMAT = DateTimeFormat.forPattern("MMM dd, yyyy h:mma");

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

  public static String getDateTimeString(StubHubResponse.Event event) {
    Preconditions.checkNotNull(event, "Event cannot be null.");
    DateTime dateTime = STUB_HUB_DATE_TIME_FORMAT.parseDateTime(event.getEventDateAsFormattedString());
    return SANE_DATE_TIME_FORMAT.print(dateTime);
  }

  public static long getDateTimeAsEpoch(StubHubResponse.Event event) {
    Preconditions.checkNotNull(event, "Event cannot be null.");
    DateTime dateTime = STUB_HUB_DATE_TIME_FORMAT.parseDateTime(event.getEventDateAsFormattedString());
    return dateTime.getMillis();
  }
}
