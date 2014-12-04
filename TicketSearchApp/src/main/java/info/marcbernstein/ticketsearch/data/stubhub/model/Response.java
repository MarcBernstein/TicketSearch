package info.marcbernstein.ticketsearch.data.stubhub.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Class used to represent a Response from the StubHub API. The response holds the events returned from the request.
 */
@SuppressWarnings("unused") // Gson does in fact use these
public class Response implements Serializable {

  @SuppressWarnings("FieldCanBeLocal") // Inspections get confused by Gson
  int numFound = -1;

  @SerializedName("docs")
  List<Event> events;

  Event nextEvent;

  // Private ctor to disable direct instantiation.
  private Response() {
  }

  /**
   * Returns the number of events found in the response
   *
   * @return the number of events found in the response
   */
  int getNumFound() {
    return numFound;
  }

  /**
   * Returns the list of all events found in the response
   *
   * @return the list of all events found in the response
   */
  List<Event> getEvents() {
    return events;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Response that = (Response) o;

    return Objects.equal(numFound, that.numFound) &&
        Objects.equal(events, that.events) &&
        Objects.equal(nextEvent, that.nextEvent);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(numFound, events, nextEvent);
  }


  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("numFound", numFound).add("events", events).add("nextEvent", nextEvent)
                      .toString();
  }
}
