package info.marcbernstein.ticketsearch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.common.collect.BiMap;

import java.util.HashMap;
import java.util.Map;

import info.marcbernstein.ticketsearch.data.geojson.model.Feature;
import info.marcbernstein.ticketsearch.data.stubhub.StubHubClient;
import info.marcbernstein.ticketsearch.data.stubhub.model.Event;
import info.marcbernstein.ticketsearch.data.stubhub.model.StubHubResponse;
import info.marcbernstein.ticketsearch.util.UiUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TeamInfoWindowAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

  private static final String TAG = TeamInfoWindowAdapter.class.getSimpleName();

  private final Context mContext;

  private final BiMap<Feature, Marker> mMapMarkers;

  private Map<Feature, StubHubResponse> mResponseMap = new HashMap<>();

  public TeamInfoWindowAdapter(Context context, BiMap<Feature, Marker> mapMarkers) {
    mContext = context;
    mMapMarkers = mapMarkers;
  }

  @Override
  public View getInfoWindow(Marker marker) {
    return null;
  }

  @SuppressLint("InflateParams") // No parent view to attach to
  @Override
  public View getInfoContents(Marker marker) {
    Feature team = mMapMarkers.inverse().get(marker);

    View view = LayoutInflater.from(mContext).inflate(R.layout.team_info_window, null, false);
    bindDataToView(view, team);

    // Google Maps info window gives very limited access to the underlying view. In order to refresh the view with
    // our fetched ticket info, we need to call showInfoWindow on the Marker again. So here we either use an already
    // fetched response or kick off a new fetch.
    View fetchTicketInfoContainer = view.findViewById(R.id.fetching_ticket_info_container);
    StubHubResponse stubHubResponse = mResponseMap.get(team);
    if (stubHubResponse != null) {
      fetchTicketInfoContainer.setVisibility(View.GONE);

      Event nextEvent = stubHubResponse.getNextEvent();
      if (nextEvent != null) {
        bindEventDataToView(nextEvent, view.findViewById(R.id.ticket_info_container));
      } else {
        // Handle the situation where there are no more home games, or the API failed on us
        view.findViewById(R.id.no_events_found_text_view).setVisibility(View.VISIBLE);
      }
    } else {
      startFetchTicketInfo(marker, team, fetchTicketInfoContainer);
    }

    return view;
  }

  private void bindDataToView(View view, Feature team) {
    View teamNameView = view.findViewById(R.id.team_name_text_view);
    if (teamNameView instanceof TextView) {
      ((TextView) teamNameView).setText(team.getTeamName());
    }
  }

  private void bindEventDataToView(Event nextEvent, View ticketInfoContainer) {
    if (nextEvent == null) {
      Log.w(TAG, "No future event was returned from the StubHub API call.");
      return;
    }

    View ticketsAvailableTextView = ticketInfoContainer.findViewById(R.id.tickets_available_text_view);
    if (ticketsAvailableTextView instanceof TextView) {
      ((TextView) ticketsAvailableTextView)
          .setText(mContext.getString(R.string.tickets_available, nextEvent.getTotalTickets()));
    }

    View eventDescriptionTextView = ticketInfoContainer.findViewById(R.id.event_description_text_view);
    if (eventDescriptionTextView instanceof TextView) {
      ((TextView) eventDescriptionTextView)
          .setText(mContext.getString(R.string.event_description, nextEvent.getDescription()));
    }

    View eventDateTextView = ticketInfoContainer.findViewById(R.id.event_date_text_view);
    if (eventDateTextView instanceof TextView) {
      ((TextView) eventDateTextView)
          .setText(mContext.getString(R.string.event_date, UiUtils.getDateTimeString(nextEvent)));
    }

    View venueNameTextView = ticketInfoContainer.findViewById(R.id.venue_name_text_view);
    if (venueNameTextView instanceof TextView) {
      ((TextView) venueNameTextView).setText(mContext.getString(R.string.event_venue, nextEvent.getVenueName()));
    }

    ticketInfoContainer.setVisibility(View.VISIBLE);
  }

  private void startFetchTicketInfo(final Marker marker, final Feature team, final View fetchTicketInfoContainer) {
    String teamName = team.getTeamName();

    // StubHub API needs to have any . chars removed. Le sigh...
    if (teamName != null && teamName.contains(".")) {
      teamName = teamName.replace(".", "");
    }

    StubHubClient
        .searchEvents(mContext.getString(R.string.stubhub_query, teamName), team, new Callback<StubHubResponse>() {
          @Override
          public void success(StubHubResponse stubHubResponse, Response response) {
            Log.d(TAG, "# of events found: " + stubHubResponse.getNumFound());
            mResponseMap.put(team, stubHubResponse);
            marker.showInfoWindow();
          }

          @Override
          public void failure(RetrofitError error) {
            hideFetchingTicketsContainer();
            Toast.makeText(mContext, R.string.error_fetching_ticket_info, Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error: ", error);
            marker.showInfoWindow();
          }

          private void hideFetchingTicketsContainer() {
            fetchTicketInfoContainer.setVisibility(View.GONE);
          }
        });
  }


  @Override
  public void onInfoWindowClick(Marker marker) {
    Feature team = mMapMarkers.inverse().get(marker);
    StubHubResponse stubHubResponse = mResponseMap.get(team);
    Event nextEvent = stubHubResponse != null ? stubHubResponse.getNextEvent() : null;
    if (nextEvent == null) {
      return;
    }

    String url = nextEvent.getEventUrl();
    Intent openBrowserIntent = new Intent(Intent.ACTION_VIEW);
    openBrowserIntent.setData(Uri.parse(url));
    mContext.startActivity(openBrowserIntent);
  }
}
