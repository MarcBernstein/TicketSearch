package info.marcbernstein.ticketsearch.data.stubhub;

import android.os.Looper;

import com.google.common.base.Preconditions;

import info.marcbernstein.ticketsearch.BuildConfig;
import info.marcbernstein.ticketsearch.data.stubhub.model.StubHubResponse;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;

public class StubHubClient {

  private static final String API_URL = "http://www.stubhub.com";

  /**
   * Normally I'd keep this somewhere secret, like a secrets.xml that doesn't get checked in to a VCS.
   * Embedding here for now so it can be built after cloning with no additional steps. *
   */
  private static final String APP_TOKEN = "ggmgN2D2lglFKwkYwCRZM7CSSHca";

  public interface StubHubService {
    @GET("/listingCatalog/select?wt=json&fl=event_id,description,event_date,venue_name,totalTickets")
    void searchEvents(@Query("q") String query, Callback<StubHubResponse> callback);

    @GET("/listingCatalog/select?wt=json&fl=event_id,description,event_date,venue_name,totalTickets")
    StubHubResponse searchEvents(@Query("q") String query);
  }

  /**
   * Add our StubHub API app token to every request. *
   */
  private static RequestInterceptor sRequestInterceptor = new RequestInterceptor() {
    @Override
    public void intercept(RequestInterceptor.RequestFacade request) {
      request.addHeader("Authorization", String.format("Bearer %s", APP_TOKEN));
    }
  };

  private static RestAdapter sRestAdapter =
      new RestAdapter.Builder().setEndpoint(API_URL).setRequestInterceptor(sRequestInterceptor)
          // If this is a debug build, show the full Retrofit response. Otherwise no logging.
          .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE).build();

  private static StubHubService sService = sRestAdapter.create(StubHubService.class);

  /**
   * Query (asynchronously) the StubHub API to search for events matching the query. See <a href="http://stubhubapi
   * .stubhub.com/index .php/Main_Page#How-to_Use_StubHub.E2.80.99s_Listing_Catalog_Service">How-to Use StubHub’s
   * Listing Catalog Service</a> for more info.
   *
   * @param query    The query to pass to the listingCatalog API
   * @param callback The callback to get the results from
   */
  public static void searchEvents(String query, Callback<StubHubResponse> callback) {
    sService.searchEvents(query, callback);
  }

  /**
   * Sync version of {@link #searchEvents(String, retrofit.Callback)}. Must not be called on the main (UI) thread.
   *
   * @param query The query to pass to the listingCatalog API
   * @return The response from the API call
   */
  public static StubHubResponse searchEvents(String query) {
    Preconditions
        .checkState(Looper.myLooper() != Looper.getMainLooper(), "This method cannot be run on the UI thread.");
    return sService.searchEvents(query);
  }
}
