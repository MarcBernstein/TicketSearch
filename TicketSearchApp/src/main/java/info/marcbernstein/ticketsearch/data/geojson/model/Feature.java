package info.marcbernstein.ticketsearch.data.geojson.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Map;

/**
 * Object that represents a GeoJSON feature.
 */
@SuppressWarnings("unused") // Gson object class
public class Feature implements Serializable {

  // Key to retrieve the team name from the properties map
  private static final String TEAM_NAME_PROPERTY = "TeamName";

  // Key to retrieve the team's stadium name from the properties map
  private static final String STADIUM_NAME_PROPERTY = "StadiumName";

  private Geometry geometry;

  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") // Gson updates this map
  private Map<String, Object> properties;

  // Private ctor to disable direct instantiation.
  private Feature() {
  }

  /**
   * Returns the geometry that this feature represents
   *
   * @return the geometry that this feature represents
   */
  public Geometry getGeometry() {
    return geometry;
  }

  /**
   * Returns the team's name as a display friendly String
   *
   * @return the team's name as a display friendly String
   */
  public String getTeamName() {
    Object value = properties.get(TEAM_NAME_PROPERTY);
    return value instanceof String ? value.toString() : "";
  }

  /**
   * Returns the team's stadium name as a display friendly String
   *
   * @return the team's stadium name as a display friendly String
   */
  public String getStadiumName() {
    Object value = properties.get(STADIUM_NAME_PROPERTY);
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
    return Objects.equal(geometry, that.geometry) && Objects.equal(properties, that.properties);
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
