package info.marcbernstein.ticketsearch.data.stubhub.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused") // Gson does in fact use these
public class StubHubResponse {

  private Response response;

  private static class Response {
    private int numFound = -1;

    @SerializedName("docs")
    private List<Event> events;

    public int getNumFound() {
      return numFound;
    }

    public List<Event> getEvents() {
      return events;
    }
  }

  public static class Event {
    private String event_id;
    private String description;
    private String event_date;
    private int totalTickets;
    private String venue_name;

    public String getDescription() {
      return description;
    }

    public String getEventDate() {
      return event_date;
    }

    public int getTotalTickets() {
      return totalTickets;
    }

    public String getVenueName() {
      return venue_name;
    }
  }

  public int getNumFound() {
    return response != null ? response.numFound : 0;
  }

  public List<Event> getEvents() {
    return response != null ? response.events : Collections.<Event>emptyList();
  }
}
