package info.marcbernstein.ticketsearch;

import android.test.AndroidTestCase;

import info.marcbernstein.ticketsearch.data.stubhub.StubHubClient;
import info.marcbernstein.ticketsearch.data.stubhub.model.StubHubResponseContainer;

/**
 * Tests the remote connection to the StubHub API, and that we get a valid, parsed response back.
 */

public class StubHubTest extends AndroidTestCase {

  public void testStubHubClient() {
    String query = mContext.getString(R.string.stubhub_query, "San Diego Chargers");

    StubHubResponseContainer stubHubResponse = StubHubClient.searchEventsRx(query, null).toBlocking().single();
    assertNotNull("Null StubHubResponse returned.", stubHubResponse);
    assertNotNull("Null events list returned.", stubHubResponse.getEvents());
    assertFalse("Empty events list returned.", stubHubResponse.getEvents().isEmpty());
    assertFalse("StubHubResponse may not have been parsed properly.", stubHubResponse.getNumFound() == -1);
  }
}
