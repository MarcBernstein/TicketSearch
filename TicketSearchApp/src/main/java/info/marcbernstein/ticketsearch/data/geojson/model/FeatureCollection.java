package info.marcbernstein.ticketsearch.data.geojson.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused") // Gson does in fact use these
public class FeatureCollection implements Serializable {

  public static FeatureCollection fromJson(String json) {
    return new Gson().fromJson(json, FeatureCollection.class);
  }

  private List<Feature> features;

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
    return Objects.equal(this.features, that.features);
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
