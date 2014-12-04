package info.marcbernstein.ticketsearch;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.text.TextUtils;

import java.util.List;

import info.marcbernstein.ticketsearch.data.geojson.model.Feature;
import info.marcbernstein.ticketsearch.data.geojson.model.FeatureCollection;
import info.marcbernstein.ticketsearch.util.GeoJsonUtils;

/**
 * Tests that the geojson file can be read and parsed properly.
 */
public class GeoJSONTest extends AndroidTestCase {

  private FeatureCollection mFeatureCollection;

  @Override
  protected void setUp() throws Exception {
    mFeatureCollection = GeoJsonUtils.getStadiumFeatures(mContext);
  }

  public void testPreconditions() {
    assertNotNull("A FeatureCollection could not be parsed from the GeoJSON.", mFeatureCollection);
  }

  @SmallTest
  public void testFeatures() {
    List<Feature> features = mFeatureCollection.getFeatures();
    assertNotNull("No Features found in FeatureCollection.", features);
    assertTrue("Features list is empty.", !features.isEmpty());

    Feature firstFeature = features.get(0);
    assertNotNull("First feature is null.", firstFeature);
    assertNotNull("First feature geometry is null.", firstFeature.getGeometry());
    assertTrue("First feature latitude is NaN.", !Double.isNaN(firstFeature.getGeometry().getLatitude()));
    assertTrue("First feature longitude is NaN.", !Double.isNaN(firstFeature.getGeometry().getLongitude()));
    assertTrue("First feature title is empty.", !TextUtils.isEmpty(firstFeature.getTeamName()));
  }

}
