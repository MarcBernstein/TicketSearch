package info.marcbernstein.ticketsearch;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.base.Preconditions;

import info.marcbernstein.ticketsearch.geojson.Feature;
import info.marcbernstein.ticketsearch.geojson.FeatureCollection;
import info.marcbernstein.ticketsearch.util.FileUtils;
import info.marcbernstein.ticketsearch.utils.UiUtils;

public class StadiumsMapActivity extends FragmentActivity
    implements GoogleMap.OnMarkerClickListener, TeamFragment.OnFragmentInteractionListener {

  private static final String TAG = StadiumsMapActivity.class.getSimpleName();
  public static final String GEOJSON_ASSET_FILENAME = "nfl_stadiums.geojson";

  private GoogleMap mMap; // Might be null if Google Play services APK is not available.
  private FeatureCollection mFeatureCollection;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stadiums_map);

    String geojson = FileUtils.getAssetAsString(this, GEOJSON_ASSET_FILENAME);
    Preconditions.checkNotNull(geojson, "GeoJSON is null, cannot add stadium markers.");

    mFeatureCollection = FeatureCollection.fromJson(geojson);
    Preconditions.checkNotNull(mFeatureCollection, "Error parsing the GeoJSON, cannot add stadium markers.");

    setUpMapIfNeeded();
  }

  @Override
  protected void onResume() {
    super.onResume();
    setUpMapIfNeeded();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (!UiUtils.isMultiPanel(this)) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu_stadiums_map, menu);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_show_teams:
        showTeamsFragment();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void showTeamsFragment() {
    if (!UiUtils.isMultiPanel(this)) {
      DialogFragment newFragment = TeamFragment.newInstance(mFeatureCollection);
      newFragment.show(getFragmentManager(), TeamFragment.TAG);
    }

    //    Fragment teamFragment = TeamFragment.newInstance();
    //    getFragmentManager().beginTransaction().add(R.id.side_panel, teamFragment, TeamFragment.TAG)
    //                        .addToBackStack(TeamFragment.TAG).commit();
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
      // Try to obtain the map from the MapFragment.
      mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragment_map)).getMap();
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
    mMap.getUiSettings().setZoomControlsEnabled(false);
    mMap.setMyLocationEnabled(true);
    mMap.setOnMarkerClickListener(this);
    setupStadiumMarkers();
  }

  private void setupStadiumMarkers() {
    BitmapDescriptor symbol = BitmapDescriptorFactory.fromResource(R.drawable.football_marker);
    LatLng location;
    String title;
    for (Feature feature : mFeatureCollection.getFeatures()) {
      location = new LatLng(feature.getGeometry().getLatitude(), feature.getGeometry().getLongitude());
      title = feature.getTitle();
      mMap.addMarker(new MarkerOptions().position(location).title(title).icon(symbol));
    }
  }

  @Override
  public boolean onMarkerClick(Marker marker) {
    // TODO implement
    Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();
    return false;
  }

  @Override
  public void onFragmentInteraction(String id) {
    Log.d(TAG, "[onFragmentInteraction] " + id);
  }
}
