package info.marcbernstein.ticketsearch.data.geojson.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("unused") // Gson object class
public class Feature implements Serializable {

  private static final String TITLE_PROPERTY = "TeamName";

  private Geometry geometry;

  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") // Gson updates this map
  private Map<String, Object> properties;

  public Geometry getGeometry() {
    return geometry;
  }

  public String getTitle() {
    Object value = properties.get(TITLE_PROPERTY);
    return value instanceof String ? value.toString() : "";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Feature that = (Feature) o;
    return Objects.equal(this.geometry, that.geometry) && Objects.equal(this.properties, that.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(geometry, properties);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("geometry", geometry).add("properties", properties).toString();
  }
}
