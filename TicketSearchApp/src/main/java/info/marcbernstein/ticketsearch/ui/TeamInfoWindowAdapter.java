package info.marcbernstein.ticketsearch.ui;

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

import java.util.HashMap;
import java.util.Map;

import info.marcbernstein.ticketsearch.R;
import info.marcbernstein.ticketsearch.data.geojson.model.Feature;
import info.marcbernstein.ticketsearch.data.stubhub.StubHubClient;
import info.marcbernstein.ticketsearch.data.stubhub.model.StubHubEvent;
import info.marcbernstein.ticketsearch.data.stubhub.model.StubHubResponseContainer;
import info.marcbernstein.ticketsearch.util.UiUtils;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Adapter class used to show an info window on the Google Map. Transforms a team feature's properties into an info
 * window suitable for display to the user.
 */
public class TeamInfoWindowAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

  private static final String TAG = TeamInfoWindowAdapter.class.getSimpleName();

  private final Context mContext;

  private final Map<Marker, Feature> mMapMarkers;

  private final Map<Feature, StubHubResponseContainer> mResponseMap = new HashMap<>();

  public TeamInfoWindowAdapter(Context context, Map<Marker, Feature> mapMarkers) {
    mContext = context;
    mMapMarkers = mapMarkers;
  }

  // We don't want to replace the whole window, just the contents.
  @Override
  public View getInfoWindow(Marker marker) {
    return null;
  }

  // Replace the content view of the default InfoWindow, retaining the frame.
  @SuppressLint("InflateParams") // No parent view to attach to
  @Override
  public View getInfoContents(Marker marker) {
    Feature team = mMapMarkers.get(marker);

    View view = LayoutInflater.from(mContext).inflate(R.layout.team_info_window, null, false);
    bindDataToView(view, team);

    // Google Maps info window gives very limited access to the underlying view. In order to refresh the view with
    // our fetched ticket info, we need to call showInfoWindow on the Marker again. So here we either use an already
    // fetched response or kick off a new fetch.
    View fetchTicketInfoContainer = view.findViewById(R.id.fetching_ticket_info_container);
    StubHubResponseContainer stubHubResponse = mResponseMap.get(team);
    if (stubHubResponse != null) {
      fetchTicketInfoContainer.setVisibility(View.GONE);

      StubHubEvent nextEvent = stubHubResponse.getNextEvent();
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

  /**
   * Responsible for taking data from the feature and putting it into the header view.
   *
   * @param view the info window content view
   * @param team the team to get the data from
   */
  private void bindDataToView(View view, Feature team) {
    View teamNameView = view.findViewById(R.id.team_name_text_view);
    if (teamNameView instanceof TextView) {
      ((TextView) teamNameView).setText(team.getTeamName());
    }
  }

  /**
   * Responsible for taking data from the event and putting it into the header view.
   *
   * @param event               the event to get the data from
   * @param ticketInfoContainer the container view that holds the sub-views
   */
  private void bindEventDataToView(StubHubEvent event, View ticketInfoContainer) {
    if (event == null) {
      Log.w(TAG, "No future event was returned from the StubHub API call.");
      return;
    }

    // Shows number of currently available tickets for the event
    View ticketsAvailableTextView = ticketInfoContainer.findViewById(R.id.tickets_available_text_view);
    if (ticketsAvailableTextView instanceof TextView) {
      ((TextView) ticketsAvailableTextView)
          .setText(mContext.getString(R.string.tickets_available, event.getTotalTickets()));
    }

    // Shows the event's description - e.g., "San Diego Chargers at Buffalo Bills"
    View eventDescriptionTextView = ticketInfoContainer.findViewById(R.id.event_description_text_view);
    if (eventDescriptionTextView instanceof TextView) {
      ((TextView) eventDescriptionTextView)
          .setText(mContext.getString(R.string.event_description, event.getDescription()));
    }

    // Shows the event's date formatted nicely in device's timezone - e.g., "Sep 21, 2014 1:00PM"
    View eventDateTextView = ticketInfoContainer.findViewById(R.id.event_date_text_view);
    if (eventDateTextView instanceof TextView) {
      ((TextView) eventDateTextView).setText(mContext.getString(R.string.event_date, UiUtils.getDateTimeString(event)));
    }

    // Show's the event's venue name - e.g., "Ralph Wilson Stadium"
    View venueNameTextView = ticketInfoContainer.findViewById(R.id.venue_name_text_view);
    if (venueNameTextView instanceof TextView) {
      ((TextView) venueNameTextView).setText(mContext.getString(R.string.event_venue, event.getVenueName()));
    }

    // Finally, set the previously gone ViewGroup to be visible
    ticketInfoContainer.setVisibility(View.VISIBLE);
  }

  /**
   * Used to compose a query to the StubHub API and receive it's reposnse asynchronously via a callback.
   *
   * @param marker                   the Marker symbol that will eventually be used to show the info window
   * @param team                     the team to run the query upon
   * @param fetchTicketInfoContainer the ViewGroup to hide once the request is complete
   */
  private void startFetchTicketInfo(final Marker marker, final Feature team, final View fetchTicketInfoContainer) {
    String teamName = team.getTeamName();

    // StubHub API evidently needs to have any . chars removed before making the search event request. Le sigh...
    if (teamName != null && teamName.contains(".")) {
      teamName = teamName.replace(".", "");
    }

    StubHubClient
        .searchEventsRx(mContext.getString(R.string.stubhub_query, teamName), team)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<StubHubResponseContainer>() {
          @Override
          public void onNext(StubHubResponseContainer stubHubResponse) {
            // Put the response in the mapping and call show info window again. This team the cached
            // response will be retrieved from the map and used instead of calling to searchEvents
            // again. This is not really optimal but necessary given the limited access from the
            // Google Maps API to refresh the info window.
            mResponseMap.put(team, stubHubResponse);
            marker.showInfoWindow();
          }

          @Override
          public void onError(Throwable e) {
            Log.e(TAG, "Error: ", e);

            hideFetchingTicketsContainer();

            // If the request failed, notify the user via a Toast message and store an empty response in the
            // mapping.
            Toast.makeText(mContext, R.string.error_fetching_ticket_info, Toast.LENGTH_LONG).show();
            mResponseMap.put(team, StubHubResponseContainer.createEmptyResponse());
            marker.showInfoWindow();
          }

          @Override
          public void onCompleted() {
            // Nothing
          }

          private void hideFetchingTicketsContainer() {
            fetchTicketInfoContainer.setVisibility(View.GONE);
          }
        });
  }


  // When the info window is clicked, get the event page URL on sstubhub.com and open a web browser to that page on
  // the device.
  @Override
  public void onInfoWindowClick(Marker marker) {
    Feature team = mMapMarkers.get(marker);
    StubHubResponseContainer stubHubResponse = mResponseMap.get(team);
    StubHubEvent nextEvent = stubHubResponse != null ? stubHubResponse.getNextEvent() : null;

    // If there's no event (we show that message in the info window) don't do anything on click.
    if (nextEvent == null) {
      return;
    }

    String url = nextEvent.getEventUrl();
    Intent openBrowserIntent = new Intent(Intent.ACTION_VIEW);
    openBrowserIntent.setData(Uri.parse(url));
    mContext.startActivity(openBrowserIntent);
  }
}
