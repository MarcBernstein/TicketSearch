package info.marcbernstein.ticketsearch.data.stubhub;

import info.marcbernstein.ticketsearch.data.stubhub.model.StubHubResponse;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;

public class StubHubClient {

  private static final String API_URL = "http://www.stubhubsandbox.com";

  public interface StubHubService {
    @GET("/listingCatalog/select?wt=json&fl=event_id,description,event_date,venue_name,totalTickets")
    void searchEvents(@Query("q") String query, Callback<StubHubResponse> callback);
  }

  private static RequestInterceptor sRequestInterceptor = new RequestInterceptor() {
    @Override
    public void intercept(RequestInterceptor.RequestFacade request) {
      request.addHeader("Authorization", "Bearer JQACLL4wpU7S2q2hrfSAExrJL7wa");
    }
  };

  private static RestAdapter sRestAdapter = new RestAdapter.Builder()
      .setEndpoint(API_URL)
      .setRequestInterceptor(sRequestInterceptor)
      .setLogLevel(RestAdapter.LogLevel.FULL)
      .build();

  private static StubHubService sService = sRestAdapter.create(StubHubService.class);

  /**
   * TODO
   * @param query
   * @param callback
   * @return
   */
  public static void searchEvents(String query, Callback<StubHubResponse> callback) {
    sService.searchEvents(query, callback);
  }
}
