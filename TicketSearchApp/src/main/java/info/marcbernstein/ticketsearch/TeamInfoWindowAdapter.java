package info.marcbernstein.ticketsearch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.common.collect.BiMap;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.marcbernstein.ticketsearch.data.geojson.model.Feature;
import info.marcbernstein.ticketsearch.data.stubhub.StubHubClient;
import info.marcbernstein.ticketsearch.data.stubhub.model.StubHubResponse;
import info.marcbernstein.ticketsearch.data.stubhub.model.StubHubResponse.Event;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TeamInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

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
      bindStubHubResponseToView(stubHubResponse, view.findViewById(R.id.ticket_info_container));
    } else {
      startFetchTicketInfo(marker, team, fetchTicketInfoContainer);
    }

    return view;
  }

  private void bindDataToView(View view, Feature team) {
    View teamNameView = view.findViewById(R.id.team_name_text_view);
    if (teamNameView instanceof TextView) {
      ((TextView) teamNameView).setText(team.getTitle());
    }
  }

  private void bindStubHubResponseToView(StubHubResponse stubHubResponse, View ticketInfoContainer) {
    if (stubHubResponse == null || stubHubResponse.getEvents() == null) {
      Log.w(TAG, "Null response from StubHub API call.");
      return;
    }

    List<Event> events = stubHubResponse.getEvents();

    // TODO implement comparator in Event
    Collections.sort(events, new Comparator<Event>() {
      @Override
      public int compare(Event event, Event event2) {
        return 0;
      }
    });

    Event nextEvent = events.get(0);
    View ticketsAvailableTextView = ticketInfoContainer.findViewById(R.id.tickets_available_text_view);
    if (ticketsAvailableTextView instanceof TextView) {
      ((TextView) ticketsAvailableTextView)
          .setText(mContext.getString(R.string.tickets_available, nextEvent.getTotalTickets()));
    }

    View eventDescriptionTextView = ticketInfoContainer.findViewById(R.id.event_description_text_view);
    if (ticketsAvailableTextView instanceof TextView) {
      ((TextView) eventDescriptionTextView)
          .setText(mContext.getString(R.string.event_description, nextEvent.getDescription()));
    }

    View eventDateTextView = ticketInfoContainer.findViewById(R.id.event_date_text_view);
    if (ticketsAvailableTextView instanceof TextView) {
      ((TextView) eventDateTextView).setText(mContext.getString(R.string.event_date, nextEvent.getEventDate()));
    }

    View venueNameTextView = ticketInfoContainer.findViewById(R.id.venue_name_text_view);
    if (ticketsAvailableTextView instanceof TextView) {
      ((TextView) venueNameTextView).setText(mContext.getString(R.string.event_venue, nextEvent.getVenueName()));
    }

    ticketInfoContainer.setVisibility(View.VISIBLE);
  }

  private void startFetchTicketInfo(final Marker marker, final Feature team, final View fetchTicketInfoContainer) {
    StubHubClient
        .searchEvents(mContext.getString(R.string.stubhub_query, team.getTitle()), new Callback<StubHubResponse>() {
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


}
