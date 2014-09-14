package info.marcbernstein.ticketsearch.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.common.base.Preconditions;

import info.marcbernstein.ticketsearch.R;
import info.marcbernstein.ticketsearch.StadiumsMapActivity;
import info.marcbernstein.ticketsearch.data.geojson.model.FeatureCollection;
import info.marcbernstein.ticketsearch.data.stubhub.StubHubClient;
import info.marcbernstein.ticketsearch.data.stubhub.model.StubHubResponse;
import info.marcbernstein.ticketsearch.util.FileUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A dialog fragment representing the list of teams.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class TeamFragment extends DialogFragment implements AdapterView.OnItemClickListener {

  public static final String TAG = TeamFragment.class.getSimpleName();

  private static final String KEY_FEATURE_COLLECTION = "KEY_FEATURE_COLLECTION";

  private OnFragmentInteractionListener mListener;

  private FeatureCollection mFeatureCollection;
  private String[] mTeams;

  public static TeamFragment newInstance(FeatureCollection featureCollection) {
    TeamFragment fragment = new TeamFragment();
    Bundle args = new Bundle();
    args.putSerializable(KEY_FEATURE_COLLECTION, featureCollection);
    fragment.setArguments(args);
    return fragment;
  }

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public TeamFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String geojson = FileUtils.getAssetAsString(getActivity(), StadiumsMapActivity.GEOJSON_ASSET_FILENAME);
    Preconditions.checkNotNull(geojson, "GeoJSON is null, cannot add stadium markers.");

    mFeatureCollection = FeatureCollection.fromJson(geojson);
    Preconditions.checkNotNull(mFeatureCollection, "Error parsing the GeoJSON, cannot add stadium markers.");

    if (getArguments() != null) {
      mFeatureCollection = (FeatureCollection) getArguments().getSerializable(KEY_FEATURE_COLLECTION);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    ListView listView = new ListView(getActivity());
    mTeams = new String[mFeatureCollection.getFeatures().size()];
    for (int i = 0; i < mTeams.length; i++) {
      mTeams[i] = mFeatureCollection.getFeatures().get(i).getTitle();
    }
    ArrayAdapter<String> adapter =
        new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, mTeams);
    listView.setAdapter(adapter);
    listView.setOnItemClickListener(this);
    return listView;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    dialog.setTitle(R.string.select_a_team);
    return dialog;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

    if (activity instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) activity;
    } else {
      throw new IllegalStateException(activity.toString() + " must implement OnFragmentInteractionListener.");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
    // TODO move to map activity
    StubHubClient.searchEvents("stubhubDocumentType:event AND description:san diego chargers AND -description:PARKING AND -description:Season Tickets AND ancestorGenreDescriptions:NFL", new Callback<StubHubResponse>() {
      @Override
      public void success(StubHubResponse stubHubResponse, Response response) {
        // TODO Show results to user
        Log.d(TAG, "# of events found: " + stubHubResponse.getNumFound());
      }

      @Override
      public void failure(RetrofitError error) {
        // TODO Show error to user
        Log.e(TAG, "Error: ", error);
      }
    });
    if (mListener != null) {
      mListener.onFragmentInteraction(mTeams[position]);
    }
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p/>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    public void onFragmentInteraction(String id);
  }

}
