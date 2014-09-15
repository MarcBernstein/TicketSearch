package info.marcbernstein.ticketsearch.data.stubhub.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused") // Gson does in fact use these
public class Response implements Serializable {

  @SuppressWarnings("FieldCanBeLocal") // Inspections get confused by Gson
      int numFound = -1;

  @SerializedName("docs")
  List<Event> events;

  Event nextEvent;

  int getNumFound() {
    return numFound;
  }

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

    return Objects.equal(this.numFound, that.numFound) &&
        Objects.equal(this.events, that.events) &&
        Objects.equal(this.nextEvent, that.nextEvent);
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
