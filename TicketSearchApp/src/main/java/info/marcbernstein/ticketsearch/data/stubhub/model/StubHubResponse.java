package info.marcbernstein.ticketsearch.data.stubhub.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import info.marcbernstein.ticketsearch.data.geojson.model.Feature;

/**
 * Class used to represent the response from the StubHub API call. Used as a container to hold the reponse and
 * events, and to proxy function calls to the internal objects.
 */
@SuppressWarnings("unused") // Gson does in fact use these
public class StubHubResponse implements Serializable {

  private Response response;

  private Feature team;

  // Private ctor to disable direct instantiation.
  private StubHubResponse() {
  }

  public static StubHubResponse createEmptyResponse() {
    return new StubHubResponse();
  }

  /**
   * Returns the number of events found in the response
   *
   * @return the number of events found in the response
   */
  public int getNumFound() {
    return response != null ? response.numFound : 0;
  }

  /**
   * Returns the list of all events found in the response
   *
   * @return the list of all events found in the response
   */
  public List<Event> getEvents() {
    return response != null ? response.events : Collections.<Event>emptyList();
  }

  /**
   * Sets the event passed in as the next event.
   *
   * @param event the event to set as the next event
   */
  public void setNextEvent(Event event) {
    if (response != null) {
      response.nextEvent = event;
    }
  }

  /**
   * Returns the next event
   *
   * @return the next event
   */
  public Event getNextEvent() {
    return response != null ? response.nextEvent : null;
  }

  /**
   * Sets the team used for the StubHub API query
   *
   * @param team the team used for the StubHub API query
   */
  public void setTeam(Feature team) {
    this.team = team;
  }

  /**
   * Returns the team used in the StubHub API query
   *
   * @return the team used in the StubHub API query
   */
  public Feature getTeam() {
    return team;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    StubHubResponse that = (StubHubResponse) o;

    return Objects.equal(response, that.response) && Objects.equal(team, that.team);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(response, team);
  }


  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("response", response).add("team", team).toString();
  }
}
