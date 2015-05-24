package info.marcbernstein.ticketsearch.data.stubhub;

import info.marcbernstein.ticketsearch.data.stubhub.model.StubHubResponseContainer;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface StubHubService {
  // Path and constant param portion of the StubHIB API call to the events listing catalog
  String LIST_CATALOG_PATH =
      "/listingCatalog/select?wt=json&fl=event_id,description,event_date_time_local,venue_name,totalTickets,urlpath";

  @GET(LIST_CATALOG_PATH)
  Observable<StubHubResponseContainer> searchEvents(@Query("q") String query);
}
