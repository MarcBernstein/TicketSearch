package info.marcbernstein.ticketsearch.util;

import android.content.Context;

import com.google.common.base.Preconditions;

import info.marcbernstein.ticketsearch.data.geojson.model.FeatureCollection;

/**
 * Util class for dealing with GeoJSON data.
 */
public final class GeoJsonUtils {

  private static final String GEOJSON_ASSET_FILENAME = "nfl_stadiums.geojson";

  // Private ctor so the util class can't be instantiated
  private GeoJsonUtils() {
  }

  /**
   * Opens the stadium features GeoJSON file, reads and parses it into a {@link info.marcbernstein.ticketsearch.data
   * .geojson.model.FeatureCollection.}
   *
   * @param context The context to use to read the files from the assets directory
   * @return a FeatureCollection representing all the team's stadiums
   */
  public static FeatureCollection getStadiumFeatures(Context context) {
    Preconditions.checkNotNull(context, "Context cannot be null.");

    String geojson = FileUtils.getAssetAsString(context, GEOJSON_ASSET_FILENAME);
    Preconditions.checkNotNull(geojson, "GeoJSON is null.");

    FeatureCollection featureCollection = FeatureCollection.fromJson(geojson);
    Preconditions.checkNotNull(featureCollection, "Error parsing the GeoJSON.");

    return featureCollection;
  }
}
