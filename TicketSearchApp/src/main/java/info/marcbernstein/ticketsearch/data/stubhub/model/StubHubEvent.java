package info.marcbernstein.ticketsearch.data.stubhub.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;

import info.marcbernstein.ticketsearch.util.UiUtils;

/**
 * Class used to represent an Event from the StubHub API. Implements a comparator based on oldest to newest date/time
 * of the event.
 */
@SuppressWarnings("unused") // Gson does in fact use these
public class StubHubEvent implements Serializable, Comparable<StubHubEvent> {

  private static final String STUBHUB_BASE_URL = "http://www.stubhub.com/";

  private static final String[] DESCRIPTION_SCRUB_LIST = {"Tickets"};

  private String event_id;

  private String description;

  private String event_date_time_local;

  private int totalTickets;

  private String venue_name;

  private String urlpath;

  private long utcSeconds;

  // Private ctor to disable direct instantiation.
  private StubHubEvent() {
  }

  /**
   * Returns this event's desription
   *
   * @return this event's desription
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns this event's date as a displayable String
   *
   * @return this event's date as a displayable String
   */
  public String getEventDateAsFormattedString() {
    return event_date_time_local;
  }

  /**
   * Returns this event's date as seconds since epoch
   *
   * @return this event's date as seconds since epoch
   */
  public long getEventDateAsUtcSeconds() {
    return utcSeconds;
  }

  /**
   * Returns the number of tickets currently available for purchase for this event
   *
   * @return the number of tickets currently available for purchase for this event
   */
  public int getTotalTickets() {
    return totalTickets;
  }

  /**
   * Returns this event's venue name
   *
   * @return this event's venue name
   */
  public String getVenueName() {
    return venue_name;
  }

  /**
   * Returns this event's url part for opening on StubHub's site (doesn't include the base URL)
   *
   * @return this event's url part for opening on StubHub's site
   */
  public String getEventUrl() {
    return STUBHUB_BASE_URL + urlpath;
  }

  /**
   * Used to transform some event data into other formats. Currently used to transform the event's date/time as
   * seconds since epoch and to remove some contant strings from the description.
   */
  public void postProcess() {
    // Save the event date as epoch time
    utcSeconds = UiUtils.getDateTimeAsEpoch(this);

    // Remove constant strings from the description if found
    if (!TextUtils.isEmpty(description)) {
      for (String scrubStr : DESCRIPTION_SCRUB_LIST) {
        if (description.contains(scrubStr)) {
          description = description.replace(scrubStr, "");
        }
      }
    }
  }

  @Override
  public int compareTo(@NonNull StubHubEvent other) {
    return Long.valueOf(utcSeconds).compareTo(other.utcSeconds);
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    StubHubEvent that = (StubHubEvent) o;

    return Objects.equal(event_id, that.event_id) &&
        Objects.equal(description, that.description) &&
        Objects.equal(event_date_time_local, that.event_date_time_local) &&
        Objects.equal(totalTickets, that.totalTickets) &&
        Objects.equal(venue_name, that.venue_name) &&
        Objects.equal(urlpath, that.urlpath);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(event_id, description, event_date_time_local, totalTickets, venue_name, urlpath);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("event_id", event_id).add("description", description)
                      .add("event_date_time_local", event_date_time_local).add("totalTickets", totalTickets)
                      .add("venue_name", venue_name).add("urlpath", urlpath).toString();
  }
}
