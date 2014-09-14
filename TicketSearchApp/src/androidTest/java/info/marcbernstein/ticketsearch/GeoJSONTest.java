package info.marcbernstein.ticketsearch;

import android.test.AndroidTestCase;
import android.text.TextUtils;

import java.util.List;

import info.marcbernstein.ticketsearch.data.geojson.model.Feature;
import info.marcbernstein.ticketsearch.data.geojson.model.FeatureCollection;
import info.marcbernstein.ticketsearch.util.FileUtils;

public class GeoJSONTest extends AndroidTestCase {

  private String mGeoJson;

  private FeatureCollection mFeatureCollection;

  @Override
  protected void setUp() throws Exception {
    mGeoJson = FileUtils.getAssetAsString(mContext, StadiumsMapActivity.GEOJSON_ASSET_FILENAME);
    mFeatureCollection = FeatureCollection.fromJson(mGeoJson);
  }

  public void testPreconditions() {
    assertTrue("GeoJson is empty.", !TextUtils.isEmpty(mGeoJson));
    assertNotNull("A FeatureCollection could not be parsed from the GeoJSON.", mFeatureCollection);
  }

  public void testFeatures() {
    List<Feature> features = mFeatureCollection.getFeatures();
    assertNotNull("No Features found in FeatureCollection.", features);
    assertTrue("Features list is empty.", !features.isEmpty());

    Feature firstFeature = features.get(0);
    assertNotNull("First feature is null.", firstFeature);
    assertTrue("First feature latitude is NaN.", !Double.isNaN(firstFeature.getLatitude()));
    assertTrue("First feature longitude is NaN.", !Double.isNaN(firstFeature.getLongitude()));
    assertTrue("First feature title is empty.", !TextUtils.isEmpty(firstFeature.getTitle()));
  }

}
