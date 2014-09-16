package info.marcbernstein.ticketsearch.data.geojson.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Object that represents a GeoJSON geometry.
 */
@SuppressWarnings("unused") // Gson object class
public class Geometry implements Serializable {

  private static final int LATITUDE = 0;

  private static final int LONGITUDE = 1;

  @SuppressWarnings("MismatchedReadAndWriteOfArray") //Gson does write to this
  private double[] coordinates;

  // Private ctor to disable direct instantiation.
  private Geometry() {
  }

  /**
   * Returns the longitude component of this geometry
   *
   * @return the longitude component of this geometry
   */
  public double getLongitude() {
    double longitude = Double.NaN;

    if (coordinates != null && coordinates.length >= LONGITUDE) {
      longitude = coordinates[LONGITUDE];
    }

    return longitude;
  }

  /**
   * Returns the latitude component of this geometry
   *
   * @return the latitude component of this geometry
   */
  public double getLatitude() {
    double longitude = Double.NaN;

    if (coordinates != null && coordinates.length >= LATITUDE) {
      longitude = coordinates[LATITUDE];
    }

    return longitude;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Geometry that = (Geometry) o;
    return Objects.equal(getLatitude(), that.getLatitude()) &&
        Objects.equal(getLongitude(), that.getLongitude());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getLatitude(), getLongitude());
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("latitude", getLatitude()).add("longitude", getLongitude()).toString();
  }
}
