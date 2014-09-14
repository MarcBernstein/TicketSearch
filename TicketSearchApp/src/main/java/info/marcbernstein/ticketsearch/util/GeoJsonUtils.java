package info.marcbernstein.ticketsearch.util;

import android.content.Context;

import com.google.common.base.Preconditions;

import info.marcbernstein.ticketsearch.data.geojson.model.FeatureCollection;

public final class GeoJsonUtils {

  private GeoJsonUtils() {
  }

  private static final String GEOJSON_ASSET_FILENAME = "nfl_stadiums.geojson";

  public static FeatureCollection getStadiumFeatures(Context context) {
    Preconditions.checkNotNull(context, "Context cannot be null.");

    String geojson = FileUtils.getAssetAsString(context, GEOJSON_ASSET_FILENAME);
    Preconditions.checkNotNull(geojson, "GeoJSON is null.");

    FeatureCollection featureCollection = FeatureCollection.fromJson(geojson);
    Preconditions.checkNotNull(featureCollection, "Error parsing the GeoJSON.");

    return featureCollection;
  }
}
