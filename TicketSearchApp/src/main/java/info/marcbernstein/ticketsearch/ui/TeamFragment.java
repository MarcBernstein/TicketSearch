package info.marcbernstein.ticketsearch.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.List;

import info.marcbernstein.ticketsearch.R;
import info.marcbernstein.ticketsearch.data.geojson.model.Feature;
import info.marcbernstein.ticketsearch.data.geojson.model.FeatureCollection;

/**
 * A dialog fragment representing the list of teams.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class TeamFragment extends DialogFragment implements AdapterView.OnItemClickListener {

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   */
  public interface OnFragmentInteractionListener {
    public void onFragmentInteraction(Feature feature);
  }

  public static final String TAG = TeamFragment.class.getSimpleName();

  private static final String KEY_FEATURE_COLLECTION = "KEY_FEATURE_COLLECTION";

  private OnFragmentInteractionListener mListener;

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
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    ListView listView = new ListView(getActivity());

    FeatureCollection featureCollection = null;
    if (getArguments() != null) {
      featureCollection = (FeatureCollection) getArguments().getSerializable(KEY_FEATURE_COLLECTION);
    }

    List<Feature> stadiums =
        featureCollection != null ? featureCollection.getFeatures() : Collections.<Feature>emptyList();

    TeamAdapter adapter = new TeamAdapter(stadiums);
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
    Feature stadium = null;
    if (view.getTag() instanceof Feature) {
      stadium = (Feature) view.getTag();
    }

    if (mListener != null) {
      mListener.onFragmentInteraction(stadium);
    }

    if (getShowsDialog()) {
      dismiss();
    }
  }

  class TeamAdapter extends BaseAdapter {

    private final List<Feature> mTeams;

    TeamAdapter(List<Feature> teams) {
      mTeams = MoreObjects.firstNonNull(teams, Collections.<Feature>emptyList());
    }

    @Override
    public int getCount() {
      return mTeams.size();
    }

    @Override
    public Feature getItem(int position) {
      return mTeams.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
      View view = convertView;
      Feature team = getItem(position);

      Preconditions.checkNotNull(team, "Could not get team feature.");

      if (view == null) {
        view = LayoutInflater.from(getActivity()).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
      }

      // For a more complex view, normally I'd use a ViewHolder here.
      if (view instanceof TextView) {
        TextView textView = (TextView) view;
        textView.setText(team.getTeamName());
        textView.setTag(team);
      }

      return view;
    }
  }
}
