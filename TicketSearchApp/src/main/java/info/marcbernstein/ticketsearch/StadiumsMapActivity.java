package info.marcbernstein.ticketsearch;

import android.app.DialogFragment;
import android.app.Fragment;
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
import info.marcbernstein.ticketsearch.ui.FirstLaunchDialogFragment;
import info.marcbernstein.ticketsearch.ui.TeamFragment;
import info.marcbernstein.ticketsearch.ui.TeamInfoWindowAdapter;
import info.marcbernstein.ticketsearch.util.GeoJsonUtils;
import info.marcbernstein.ticketsearch.util.UiUtils;

/**
 * Main Activity for the Ticket Search app. Responsible for hosting the map view and also the team list when on a
 * tablet UI.
 */
public class StadiumsMapActivity extends FragmentActivity implements TeamFragment.OnFragmentInteractionListener {

  private static final String TAG = StadiumsMapActivity.class.getSimpleName();

  private static final int ANIMATION_DURATION = 500;

  private static final int ZOOM_LEVEL = 12;

  private GoogleMap mMap; // Might be null if Google Play services APK is not available.

  private FeatureCollection mFeatureCollection;

  private BiMap<Feature, Marker> mMapMarkers;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stadiums_map);

    // Read and parse the local json for the stadium locations
    mFeatureCollection = GeoJsonUtils.getStadiumFeatures(this);

    setUpMapIfNeeded();

    if (UiUtils.isMultiPanel(this)) {
      showTeamsFragment();
    }

    if (UiUtils.isFirstLaunch(this)) {
      DialogFragment firstLaunchDialogFragment = FirstLaunchDialogFragment.newInstance();
      firstLaunchDialogFragment.show(getFragmentManager(), FirstLaunchDialogFragment.TAG);
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

  /**
   * Shows the team list fragment either as a dialog or embedded in a side panel, depending on if this is a phone or a
   * tablet UI. Reuses the same fragment code and layout.
   */
  private void showTeamsFragment() {
    DialogFragment teamFragment = TeamFragment.newInstance(mFeatureCollection);
    if (!UiUtils.isMultiPanel(this)) {
      teamFragment.show(getFragmentManager(), TeamFragment.TAG);
    } else {
      Fragment fragmentByTag = getFragmentManager().findFragmentByTag(TeamFragment.TAG);
      if (fragmentByTag == null || !fragmentByTag.isAdded()) {
        getFragmentManager().beginTransaction().add(R.id.side_panel, teamFragment, TeamFragment.TAG).commit();
      }
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

    // Order is important here, must setup the markers before crating the adapter with them.
    setupStadiumMarkers();
    TeamInfoWindowAdapter adapter = new TeamInfoWindowAdapter(this, mMapMarkers);
    mMap.setInfoWindowAdapter(adapter);
    mMap.setOnInfoWindowClickListener(adapter);
  }

  /**
   * Creates and adds Marker symbol for each team's stadium to the map.
   */
  private void setupStadiumMarkers() {
    if (mFeatureCollection == null || mFeatureCollection.getFeatures() == null) {
      Log.w(TAG, "Feature Collection is empty, cannot add stadium markers.");
      return;
    }

    // Use of a BiMap lets us treat either the team or it's marker as a key.
    mMapMarkers = HashBiMap.create(mFeatureCollection.getFeatures().size());

    BitmapDescriptor symbol = BitmapDescriptorFactory.fromResource(R.drawable.football_marker);
    LatLng location;
    String title;
    for (Feature feature : mFeatureCollection.getFeatures()) {
      location = new LatLng(feature.getGeometry().getLatitude(), feature.getGeometry().getLongitude());
      title = feature.getTeamName();
      Marker stadiumMarker = mMap.addMarker(new MarkerOptions().position(location).title(title).icon(symbol));
      mMapMarkers.put(feature, stadiumMarker);
    }
  }

  /**
   * Activity implements this interface so that we can get notified when the user selects a team in the TeamFragment
   * list view, and take an approriate action.
   *
   * @param team The team the user selected
   */
  @Override
  public void onFragmentInteraction(Feature team) {
    if (team == null) {
      Log.w(TAG, "No Feature to zoom to.");
      return;
    }

    final Marker stadiumMarker = mMapMarkers.get(team);
    if (stadiumMarker == null) {
      Log.w(TAG, "Could not find stadium marker to zoom to.");
      return;
    }

    // Animate a map extent change + zoom in to the team's stadium location
    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(stadiumMarker.getPosition(), ZOOM_LEVEL);
    mMap.animateCamera(update, ANIMATION_DURATION, new GoogleMap.CancelableCallback() {
      @Override
      public void onFinish() {
        // Once we've panned and zoomed, show the info window as if the user had clicked on the marker symbol
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
