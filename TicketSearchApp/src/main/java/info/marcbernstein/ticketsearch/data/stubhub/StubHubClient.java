package info.marcbernstein.ticketsearch.data.stubhub;

import android.os.Looper;

import com.google.common.base.Preconditions;

import org.joda.time.Instant;

import java.util.Collections;
import java.util.List;

import info.marcbernstein.ticketsearch.BuildConfig;
import info.marcbernstein.ticketsearch.data.geojson.model.Feature;
import info.marcbernstein.ticketsearch.data.stubhub.model.Event;
import info.marcbernstein.ticketsearch.data.stubhub.model.StubHubResponse;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

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

  // Path and constant param portion of the StubHIB API call to the events listing catalog
  private static final String LIST_CATALOG_PATH =
      "/listingCatalog/select?wt=json&fl=event_id,description,event_date_time_local,venue_name,totalTickets,urlpath";

  public interface StubHubService {
    @GET(LIST_CATALOG_PATH)
    void searchEvents(@Query("q") String query, Callback<StubHubResponse> callback);

    @GET(LIST_CATALOG_PATH)
    StubHubResponse searchEvents(@Query("q") String query);
  }

  // Private ctor to disable direct instantiation.
  private StubHubClient() {
  }

  /**
   * Add our StubHub API app token to every request. *
   */
  private static final RequestInterceptor sRequestInterceptor = new RequestInterceptor() {
    @Override
    public void intercept(RequestInterceptor.RequestFacade request) {
      request.addHeader("Authorization", String.format("Bearer %s", APP_TOKEN));
    }
  };

  private static final RestAdapter sRestAdapter =
      new RestAdapter.Builder().setEndpoint(API_URL).setRequestInterceptor(sRequestInterceptor)
          // If this is a debug build, show the full Retrofit response. Otherwise no logging.
          .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE).build();

  private static final StubHubService sService = sRestAdapter.create(StubHubService.class);

  /**
   * Query (asynchronously) the StubHub API to search for events matching the query. See <a href="http://stubhubapi
   * .stubhub.com/index .php/Main_Page#How-to_Use_StubHub.E2.80.99s_Listing_Catalog_Service">How-to Use StubHub’s
   * Listing Catalog Service</a> for more info.
   *
   * @param query    The query to pass to the listingCatalog API
   * @param team     The team being searched for
   * @param callback The callback to get the results from
   */
  public static void searchEvents(String query, final Feature team, final Callback<StubHubResponse> callback) {
    sService.searchEvents(query, new Callback<StubHubResponse>() {
      @Override
      public void success(StubHubResponse stubHubResponse, Response response) {
        // First, post process the response before handin it off to the caller
        postProcess(stubHubResponse, team);

        if (callback != null) {
          callback.success(stubHubResponse, response);
        }
      }

      @Override
      public void failure(RetrofitError error) {
        if (callback != null) {
          callback.failure(error);
        }
      }
    });
  }

  /**
   * Sync version of {@link #searchEvents(String, Feature, retrofit.Callback)}. Must not be called on the main (UI)
   * thread.
   *
   * @param query The query to pass to the listingCatalog API
   * @param team  The team being searched for
   * @return The response from the API call
   */
  public static StubHubResponse searchEvents(String query, Feature team) {
    Preconditions
        .checkState(!Looper.myLooper().equals(Looper.getMainLooper()), "This method cannot be run on the UI thread.");

    StubHubResponse stubHubResponse = sService.searchEvents(query);

    // First, post process the response before handin it off to the caller
    postProcess(stubHubResponse, team);

    return stubHubResponse;
  }

  /**
   * Processes all events, creating an entry in the object for the next upcoming event.
   *
   * @param stubHubResponse The Response to get the Events from
   * @param team            The team that was searched for
   */
  private static void postProcess(StubHubResponse stubHubResponse, Feature team) {
    if (stubHubResponse == null || stubHubResponse.getEvents() == null || stubHubResponse.getEvents().isEmpty() ||
        team == null) {
      return;
    }

    // Sets the team directly in the response for use by the calling code
    stubHubResponse.setTeam(team);
    String stadiumName = team.getStadiumName();

    List<Event> events = stubHubResponse.getEvents();

    // Add the UTC epoch time to the Event
    for (Event event : events) {
      event.postProcess();
    }

    // Sort by the comparator implemented in the Event class. Sorts by oldest to newest events by date/time.
    Collections.sort(events);

    // Try to find the next home game to set it as the 'next event' memeber in the response
    Instant now = Instant.now();
    for (Event event : events) {
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
