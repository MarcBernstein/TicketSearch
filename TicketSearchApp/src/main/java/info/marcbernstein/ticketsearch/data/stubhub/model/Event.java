package info.marcbernstein.ticketsearch.data.stubhub.model;

import android.text.TextUtils;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import info.marcbernstein.ticketsearch.util.UiUtils;

@SuppressWarnings("unused") // Gson does in fact use these
public class Event implements Serializable, Comparable<Event> {
  private static final String STUBHUB_BASE_URL = "http://www.stubhub.com/";

  private static final String[] DESCRIPTION_SCRUB_LIST = {"Tickets"};

  private String event_id;
  private String description;
  private String event_date_time_local;
  private int totalTickets;
  private String venue_name;
  private String urlpath;
  private long utcSeconds;

  public String getDescription() {
    return description;
  }

  public String getEventDateAsFormattedString() {
    return event_date_time_local;
  }

  public long getEventDateAsUtcSeconds() {
    return utcSeconds;
  }

  public int getTotalTickets() {
    return totalTickets;
  }

  public String getVenueName() {
    return venue_name;
  }

  public String getEventUrl() {
    return STUBHUB_BASE_URL + urlpath;
  }

  public void postProcess() {
    // Save the event date as epocj time
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
  public int compareTo(@NotNull Event other) {
    return Long.valueOf(this.utcSeconds).compareTo(other.utcSeconds);
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Event that = (Event) o;

    return Objects.equal(this.event_id, that.event_id) &&
        Objects.equal(this.description, that.description) &&
        Objects.equal(this.event_date_time_local, that.event_date_time_local) &&
        Objects.equal(this.totalTickets, that.totalTickets) &&
        Objects.equal(this.venue_name, that.venue_name) &&
        Objects.equal(this.urlpath, that.urlpath);
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
