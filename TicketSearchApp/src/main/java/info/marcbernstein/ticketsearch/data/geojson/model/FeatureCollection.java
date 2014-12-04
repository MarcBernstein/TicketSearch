package info.marcbernstein.ticketsearch.data.geojson.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Object that represents a GeoJSON feature collection.
 */
@SuppressWarnings("unused") // Gson does in fact use these
public class FeatureCollection implements Serializable {

  private List<Feature> features;

  // Private ctor to disable direct instantiation. Use the static fromJson method instead.
  private FeatureCollection() {
  }

  /**
   * Factory method to turn a JSON string into a GeoJSON feature collection.
   *
   * @param json The GeoJSON as a String
   * @return a FeatureCollection parsed from the provided JSON
   */
  public static FeatureCollection fromJson(String json) {
    return new Gson().fromJson(json, FeatureCollection.class);
  }

  /**
   * Returns a list of all the features found in the GeoJSON
   *
   * @return list of all features
   */
  public List<Feature> getFeatures() {
    return features;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    FeatureCollection that = (FeatureCollection) o;
    return Objects.equal(features, that.features);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(features);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("features", features).toString();
  }
}
