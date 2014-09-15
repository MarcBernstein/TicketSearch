package info.marcbernstein.ticketsearch.data.stubhub.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

import info.marcbernstein.ticketsearch.data.geojson.model.Feature;
import info.marcbernstein.ticketsearch.util.UiUtils;

@SuppressWarnings("unused") // Gson does in fact use these
public class StubHubResponse {

  private Response response;

  private Feature team;

  private static class Response {
    private int numFound = -1;

    @SerializedName("docs")
    private List<Event> events;

    private Event nextEvent;

    private int getNumFound() {
      return numFound;
    }

    private List<Event> getEvents() {
      return events;
    }
  }

  public static class Event implements Comparable<Event> {
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
    public int compareTo(Event other) {
      return Long.valueOf(this.utcSeconds).compareTo(other.utcSeconds);
    }
  }

  public int getNumFound() {
    return response != null ? response.numFound : 0;
  }

  public List<Event> getEvents() {
    return response != null ? response.events : Collections.<Event>emptyList();
  }

  public void setNextEvent(Event event) {
    if (response != null) {
      response.nextEvent = event;
    }
  }

  public Event getNextEvent() {
    return response != null ? response.nextEvent : null;
  }

  public void setTeam(Feature feature) {
    team = feature;
  }

  public Feature getTeam() {
    return team;
  }
}
