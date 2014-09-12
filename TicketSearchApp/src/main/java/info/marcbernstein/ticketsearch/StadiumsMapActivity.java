package info.marcbernstein.ticketsearch;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;

import info.marcbernstein.ticketsearch.util.FileUtils;

public class StadiumsMapActivity extends FragmentActivity {

  private static final String TAG = StadiumsMapActivity.class.getSimpleName();

  private GoogleMap mMap; // Might be null if Google Play services APK is not available.

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stadiums_map);
    setUpMapIfNeeded();
  }

  @Override
  protected void onResume() {
    super.onResume();
    setUpMapIfNeeded();
  }

  /**
   * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
   * installed) and the map has not already been instantiated.. This will ensure that we only ever
   * call {@link #setUpMap()} once when {@link #mMap} is not null.
   * <p/>
   * If it isn't installed {@link SupportMapFragment} (and
   * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
   * install/update the Google Play services APK on their device.
   * <p/>
   * A user can return to this FragmentActivity after following the prompt and correctly
   * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
   * have been completely destroyed during this process (it is likely that it would only be
   * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
   * method in {@link #onResume()} to guarantee that it will be called.
   */
  private void setUpMapIfNeeded() {
    // Do a null check to confirm that we have not already instantiated the map.
    if (mMap == null) {
      // Try to obtain the map from the SupportMapFragment.
      mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
      // Check if we were successful in obtaining the map.
      if (mMap != null) {
        setUpMap();
      }
    }
  }

  /**
   * This should only be called once and when we are sure that {@link #mMap} is not null.
   */
  private void setUpMap() {
    mMap.setMyLocationEnabled(true);
    setupStadiumMarkers();
  }

  private void setupStadiumMarkers() {
    mMap.setOnMarkerClickListener(new StadiumMarkerClickListener(this));

    String geojson = FileUtils.getAssetAsString(this, "nfl_stadiums.geojson");
  }
}
