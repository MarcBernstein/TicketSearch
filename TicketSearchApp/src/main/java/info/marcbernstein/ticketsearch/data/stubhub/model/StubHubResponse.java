package info.marcbernstein.ticketsearch.data.stubhub.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import info.marcbernstein.ticketsearch.data.geojson.model.Feature;

@SuppressWarnings("unused") // Gson does in fact use these
public class StubHubResponse implements Serializable {

  private Response response;

  private Feature team;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    StubHubResponse that = (StubHubResponse) o;

    return Objects.equal(this.response, that.response) && Objects.equal(this.team, that.team);
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
