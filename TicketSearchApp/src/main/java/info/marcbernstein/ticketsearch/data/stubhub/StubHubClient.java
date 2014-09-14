package info.marcbernstein.ticketsearch.data.stubhub;

import info.marcbernstein.ticketsearch.BuildConfig;
import info.marcbernstein.ticketsearch.R;
import info.marcbernstein.ticketsearch.TicketSearchApplication;
import info.marcbernstein.ticketsearch.data.stubhub.model.StubHubResponse;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;

public class StubHubClient {

  private static final String API_URL = TicketSearchApplication.getContext().getString(R.string.stubhub_api_url);

  private static final String APP_TOKEN = TicketSearchApplication.getContext().getString(R.string.stubhub_api_token);

  public interface StubHubService {
    @GET("/listingCatalog/select?wt=json&fl=event_id,description,event_date,venue_name,totalTickets")
    void searchEvents(@Query("q") String query, Callback<StubHubResponse> callback);
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
   * Query the StubHub API to search for events matching the query. See <a href="http://stubhubapi.stubhub.com/index
   * .php/Main_Page#How-to_Use_StubHub.E2.80.99s_Listing_Catalog_Service">How-to Use StubHub’s Listing Catalog
   * Service</a> for more info.
   *
   * @param query    The query to pass to the listingCatalog API
   * @param callback The callback to get the results from
   */
  public static void searchEvents(String query, Callback<StubHubResponse> callback) {
    sService.searchEvents(query, callback);
  }
}
