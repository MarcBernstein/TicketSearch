package info.marcbernstein.ticketsearch;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import info.marcbernstein.ticketsearch.data.stubhub.StubHubClient;
import info.marcbernstein.ticketsearch.data.stubhub.model.StubHubResponse;
import retrofit.RetrofitError;

/**
 * Tests the remote connection to the StubHub API, and that we get a valid, parsed response back.
 */w
public class StubHubTest extends AndroidTestCase {

  @LargeTest
  public void testStubHubClient() {
    String query = mContext.getString(R.string.stubhub_query, "San Diego Chargers");

    try {
      StubHubResponse stubHubResponse = StubHubClient.searchEvents(query, null);
      assertNotNull("Null StubHubResponse returned.", stubHubResponse);
      assertNotNull("Null events list returned.", stubHubResponse.getEvents());
      assertFalse("Empty events list returned.", stubHubResponse.getEvents().isEmpty());
      assertFalse("StubHubResponse may not have been parsed properly.", stubHubResponse.getNumFound() == -1);
    } catch (RetrofitError e) {
      // We never want to get here
      fail("StubHubClient.searchEvents callback called its failure() method. Error: " + e.getMessage());
    }
  }
}
