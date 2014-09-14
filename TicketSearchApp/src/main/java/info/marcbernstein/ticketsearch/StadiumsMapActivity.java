package info.marcbernstein.ticketsearch;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import info.marcbernstein.ticketsearch.data.geojson.model.Feature;
import info.marcbernstein.ticketsearch.data.geojson.model.FeatureCollection;
import info.marcbernstein.ticketsearch.data.stubhub.StubHubClient;
import info.marcbernstein.ticketsearch.data.stubhub.model.StubHubResponse;
import info.marcbernstein.ticketsearch.ui.TeamFragment;
import info.marcbernstein.ticketsearch.util.GeoJsonUtils;
import info.marcbernstein.ticketsearch.util.UiUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class StadiumsMapActivity extends FragmentActivity
    implements GoogleMap.OnMarkerClickListener, TeamFragment.OnFragmentInteractionListener {

  private static final String TAG = StadiumsMapActivity.class.getSimpleName();
  public static final int ANIM_DURATION = 500;


  private GoogleMap mMap; // Might be null if Google Play services APK is not available.

  private FeatureCollection mFeatureCollection;

  private BiMap<Feature, Marker> mMapMarkers;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stadiums_map);

    mFeatureCollection = GeoJsonUtils.getStadiumFeatures(this);

    setUpMapIfNeeded();

    if (UiUtils.isMultiPanel(this)) {
      showTeamsFragment();
    }
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
    DialogFragment teamFragment = TeamFragment.newInstance(mFeatureCollection);
    if (!UiUtils.isMultiPanel(this)) {
      teamFragment.show(getFragmentManager(), TeamFragment.TAG);
    } else {
      getFragmentManager().beginTransaction().add(R.id.side_panel, teamFragment, TeamFragment.TAG).commit();
    }
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

    // Order is important here, must setup the markers before crating the adapter with them.
    setupStadiumMarkers();
    TeamInfoWindowAdapter adapter = new TeamInfoWindowAdapter(this, mMapMarkers);
    mMap.setInfoWindowAdapter(adapter);
    mMap.setOnInfoWindowClickListener(adapter);
  }

  private void setupStadiumMarkers() {
    if (mFeatureCollection == null || mFeatureCollection.getFeatures() == null) {
      Log.w(TAG, "Feature Collection is empty, cannot add stadium markers.");
      return;
    }

    mMapMarkers = HashBiMap.create(mFeatureCollection.getFeatures().size());

    BitmapDescriptor symbol = BitmapDescriptorFactory.fromResource(R.drawable.football_marker);
    LatLng location;
    String title;
    for (Feature feature : mFeatureCollection.getFeatures()) {
      location = new LatLng(feature.getGeometry().getLatitude(), feature.getGeometry().getLongitude());
      title = feature.getTitle();
      Marker stadiumMarker = mMap.addMarker(new MarkerOptions().position(location).title(title).icon(symbol));
      mMapMarkers.put(feature, stadiumMarker);
    }
  }

  @Override
  public boolean onMarkerClick(Marker marker) {
    // TODO implement
    // Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();
    // marker.showInfoWindow();
    // mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
    return false;
  }

  @Override
  public void onFragmentInteraction(Feature feature) {
    if (feature == null) {
      Log.w(TAG, "No Feature to zoom to.");
      return;
    }

    final Marker stadiumMarker = mMapMarkers.get(feature);
    if (stadiumMarker == null) {
      Log.w(TAG, "Could not find stadium marker to zoom to.");
      return;
    }

    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(stadiumMarker.getPosition(), 10);
    mMap.animateCamera(update, ANIM_DURATION, new GoogleMap.CancelableCallback() {
      @Override
      public void onFinish() {
        if (!stadiumMarker.isInfoWindowShown()) {
          stadiumMarker.showInfoWindow();
        }
      }

      @Override
      public void onCancel() {
        // Empty, we don't provide ability to cancel.
      }
    });
  }
}
