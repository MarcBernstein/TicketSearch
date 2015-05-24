package info.marcbernstein.ticketsearch.data.stubhub;

import org.joda.time.Instant;

import java.util.Collections;
import java.util.List;

import info.marcbernstein.ticketsearch.BuildConfig;
import info.marcbernstein.ticketsearch.R;
import info.marcbernstein.ticketsearch.TicketSearchApp;
import info.marcbernstein.ticketsearch.data.geojson.model.Feature;
import info.marcbernstein.ticketsearch.data.stubhub.model.StubHubEvent;
import info.marcbernstein.ticketsearch.data.stubhub.model.StubHubResponseContainer;
import retrofit.RestAdapter;
import rx.Observable;
import rx.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Class used to initiate requests and receive responses from the StubHub API.
 */
public final class StubHubClient {

  private static final String API_URL = "http://www.stubhub.com";

  /**
   * Normally I'd keep this somewhere secret, like a secrets.xml that doesn't get checked in to a VCS.
   * Embedding here for now so it can be built after cloning with no additional steps. *
   */
  private static final String APP_TOKEN = "ggmgN2D2lglFKwkYwCRZM7CSSHca";

  /**
   * Add our StubHub API app token to every request. *
   */
  private static final RestAdapter sRestAdapter =
      new RestAdapter.Builder()
          .setEndpoint(API_URL)
          .setRequestInterceptor(request -> request.addHeader("Authorization", String.format("Bearer %s", APP_TOKEN)))
              // If this is a debug build, show the full Retrofit response. Otherwise no logging.
          .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE).build();
  private static final StubHubService sService = sRestAdapter.create(StubHubService.class);

  // Private ctor to disable direct instantiation.
  private StubHubClient() {}

  /**
   * Query (asynchronously) the StubHub API to search for events matching the query. See <a href="http://stubhubapi
   * .stubhub.com/index .php/Main_Page#How-to_Use_StubHub.E2.80.99s_Listing_Catalog_Service">How-to Use StubHub’s
   * Listing Catalog Service</a> for more info.
   *
   * @param team The team being searched for
   */
  public static Observable<StubHubResponseContainer> searchEvents(Feature team) {
    checkNotNull(team);

    return Observable
        .just(team)
        .map(Feature::getTeamName)
            // StubHub API needs to have any . chars removed before making the search event request.
        .map(teamName -> teamName.replace(".", ""))
        .map(teamName -> TicketSearchApp.getInstance().getString(R.string.stubhub_query, teamName))
        .flatMap(sService::searchEvents)
        .doOnNext(stubHubResponse -> postProcess(stubHubResponse, team))
        .subscribeOn(Schedulers.io());
  }

  /**
   * Processes all events, creating an entry in the object for the next upcoming event.
   *
   * @param stubHubResponse The Response to get the Events from
   * @param team            The team that was searched for
   */
  private static void postProcess(StubHubResponseContainer stubHubResponse, Feature team) {
    if (stubHubResponse == null || stubHubResponse.getEvents() == null || stubHubResponse.getEvents().isEmpty() ||
        team == null) {
      return;
    }

    // Sets the team directly in the response for use by the calling code
    stubHubResponse.setTeam(team);
    String stadiumName = team.getStadiumName();

    List<StubHubEvent> events = stubHubResponse.getEvents();

    // Add the UTC epoch time to the Event
    for (StubHubEvent event : events) {
      event.postProcess();
    }

    // Sort by the comparator implemented in the Event class. Sorts by oldest to newest events by date/time.
    Collections.sort(events);

    // Try to find the next home game to set it as the 'next event' member in the response
    Instant now = Instant.now();
    for (StubHubEvent event : events) {
      // Next, we want to filter out any events in the past
      if (now.isAfter(event.getEventDateAsUtcSeconds())) {
        continue;
      }

      // First time we get here in our sorted list, it should be the next event. However,
      // we also need to check if the venue name matches the team's home stadium,
      // as the StubHub API doesn't seem to have a way to only request home games.
      if (stadiumName != null && !stadiumName.equals(event.getVenueName())) {
        continue;
      }

      // If we get here, this should be the next home event.
      stubHubResponse.setNextEvent(event);
      break;
    }
  }
}
