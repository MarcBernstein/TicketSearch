package info.marcbernstein.ticketsearch.data.stubhub;

import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import info.marcbernstein.ticketsearch.data.stubhub.model.StubHubResponseContainer;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class StubHubClientTest {

  @Test
  public void testSearchEventsRx() throws Exception {
    String response = "{\"responseHeader\":{\"status\":0,\"QTime\":13," +
        "\"params\":{\"fl\":\"event_id,description,event_date_time_local,venue_name,totalTickets," +
        "urlpath\",\"q\":\"stubhubDocumentType:event AND description:San Diego Chargers AND " +
        "-description:PARKING AND -description:Season Tickets AND -description:VIP AND -description:Tailgate AND " +
        "ancestorGenreDescriptions:NFL +allowedViewingDomain:stubhub.com\",\"wt\":\"json\"," +
        "\"rows\":\"5500\"}},\"response\":{\"numFound\":20,\"start\":0," +
        "\"docs\":[{\"event_id\":\"9298645\",\"description\":\"Pittsburgh Steelers at San Diego " +
        "Chargers Tickets\",\"event_date_time_local\":\"2015-10-12T17:30:00Z\",\"totalTickets\":7497.0," +
        "\"urlpath\":\"san-diego-chargers-san-diego-qualcomm-stadium-10-12-2015-9298645\"," +
        "\"venue_name\":\"Qualcomm Stadium\"},{\"event_id\":\"9298658\"," +
        "\"description\":\"Denver Broncos at San Diego Chargers Tickets\"," +
        "\"event_date_time_local\":\"2015-12-06T13:05:00Z\",\"totalTickets\":10471.0," +
        "\"urlpath\":\"san-diego-chargers-san-diego-qualcomm-stadium-12-6-2015-9298658\"," +
        "\"venue_name\":\"Qualcomm Stadium\"},{\"event_id\":\"9298641\"," +
        "\"description\":\"Cleveland Browns at San Diego Chargers Tickets\"," +
        "\"event_date_time_local\":\"2015-10-04T13:05:00Z\",\"totalTickets\":10740.0," +
        "\"urlpath\":\"san-diego-chargers-san-diego-qualcomm-stadium-10-4-2015-9298641\"," +
        "\"venue_name\":\"Qualcomm Stadium\"},{\"event_id\":\"9298661\",\"description\":\"Miami" +
        " Dolphins at San Diego Chargers Tickets\",\"event_date_time_local\":\"2015-12-20T13:25:00Z\"," +
        "\"totalTickets\":9904.0,\"urlpath\":\"san-diego-chargers-san-diego-qualcomm-stadium-12-20-2015" +
        "-9298661\",\"venue_name\":\"Qualcomm Stadium\"},{\"event_id\":\"9298653\"," +
        "\"description\":\"Chicago Bears at San Diego Chargers Tickets\"," +
        "\"event_date_time_local\":\"2015-11-09T17:30:00Z\",\"totalTickets\":9710.0," +
        "\"urlpath\":\"san-diego-chargers-san-diego-qualcomm-stadium-11-9-2015-9298653\"," +
        "\"venue_name\":\"Qualcomm Stadium\"},{\"event_id\":\"9298650\"," +
        "\"description\":\"Oakland Raiders at San Diego Chargers Tickets\"," +
        "\"event_date_time_local\":\"2015-10-25T13:05:00Z\",\"totalTickets\":10062.0," +
        "\"urlpath\":\"san-diego-chargers-san-diego-qualcomm-stadium-10-25-2015-9298650\"," +
        "\"venue_name\":\"Qualcomm Stadium\"},{\"event_id\":\"9298632\"," +
        "\"description\":\"Detroit Lions at San Diego Chargers Tickets\"," +
        "\"event_date_time_local\":\"2015-09-13T13:05:00Z\",\"totalTickets\":10310.0," +
        "\"urlpath\":\"san-diego-chargers-san-diego-qualcomm-stadium-9-13-2015-9298632\"," +
        "\"venue_name\":\"Qualcomm Stadium\"},{\"event_id\":\"9298656\"," +
        "\"description\":\"Kansas City Chiefs at San Diego Chargers Tickets\"," +
        "\"event_date_time_local\":\"2015-11-22T17:30:00Z\",\"totalTickets\":10560.0," +
        "\"urlpath\":\"san-diego-chargers-san-diego-qualcomm-stadium-11-22-2015-9298656\"," +
        "\"venue_name\":\"Qualcomm Stadium\"},{\"event_id\":\"9289647\"," +
        "\"description\":\"Dallas Cowboys at San Diego Chargers Preseason Tickets\"," +
        "\"event_date_time_local\":\"2015-08-13T19:00:00Z\",\"totalTickets\":10540.0," +
        "\"urlpath\":\"san-diego-chargers-san-diego-qualcomm-stadium-8-13-2015-9289647\"," +
        "\"venue_name\":\"Qualcomm Stadium\"},{\"event_id\":\"9289646\"," +
        "\"description\":\"Seattle Seahawks at San Diego Chargers Preseason Tickets\"," +
        "\"event_date_time_local\":\"2015-08-29T17:00:00Z\",\"totalTickets\":10817.0," +
        "\"urlpath\":\"san-diego-chargers-san-diego-qualcomm-stadium-8-29-2015-9289646\"," +
        "\"venue_name\":\"Qualcomm Stadium\"},{\"event_id\":\"9298620\",\"description\":\"San " +
        "Diego Chargers at Cincinnati Bengals Tickets\",\"event_date_time_local\":\"2015-09-20T13:00:00Z\"," +
        "\"totalTickets\":3092.0,\"urlpath\":\"cincinnati-bengals-cincinnati-paul-brown-stadium-9-20-2015" +
        "-9298620\",\"venue_name\":\"Paul Brown Stadium\"},{\"event_id\":\"9289677\"," +
        "\"description\":\"San Diego Chargers at San Francisco 49ers Preseason Tickets\"," +
        "\"event_date_time_local\":\"2015-09-03T19:00:00Z\",\"totalTickets\":10751.0," +
        "\"urlpath\":\"san-francisco-49ers-santa-clara-levi-s-stadium-9-3-2015-9289677\"," +
        "\"venue_name\":\"Levi's Stadium\"},{\"event_id\":\"9298759\",\"description\":\"San " +
        "Diego Chargers at Jacksonville Jaguars Tickets\"," +
        "\"event_date_time_local\":\"2015-11-29T13:00:00Z\",\"totalTickets\":1913.0," +
        "\"urlpath\":\"jacksonville-jaguars-jacksonville-everbank-field-11-29-2015-9298759\"," +
        "\"venue_name\":\"EverBank Field\"},{\"event_id\":\"9298767\",\"description\":\"San " +
        "Diego Chargers at Baltimore Ravens Tickets\",\"eve05-22 23:37:47.235  23150-23355/info.marcbernstein" +
        ".ticketsearch D/Retrofit\\uFE55 nt_date_time_local\":\"2015-11-01T13:00:00Z\"," +
        "\"totalTickets\":2563.0,\"urlpath\":\"baltimore-ravens-baltimore-m-t-bank-stadium-11-1-2015" +
        "-9298767\",\"venue_name\":\"M&T Bank Stadium\"},{\"event_id\":\"9298799\"," +
        "\"description\":\"San Diego Chargers at Minnesota Vikings Tickets\"," +
        "\"event_date_time_local\":\"2015-09-27T12:00:00Z\",\"totalTickets\":5863.0," +
        "\"urlpath\":\"minnesota-vikings-minneapolis-tcf-bank-stadium-9-27-2015-9298799\"," +
        "\"venue_name\":\"TCF Bank Stadium\"},{\"event_id\":\"9298537\",\"description\":\"San " +
        "Diego Chargers at Denver Broncos Tickets\",\"event_date_time_local\":\"2016-01-03T14:25:00Z\"," +
        "\"totalTickets\":1678.0,\"urlpath\":\"denver-broncos-denver-sports-authority-field-at-mile-high-1" +
        "-3-2016-9298537\",\"venue_name\":\"Sports Authority Field at Mile High\"}," +
        "{\"event_id\":\"9298744\",\"description\":\"San Diego Chargers at Oakland Raiders Tickets\"," +
        "\"event_date_time_local\":\"2015-12-24T17:25:00Z\",\"totalTickets\":5640.0," +
        "\"urlpath\":\"oakland-raiders-oakland-o-co-coliseum-12-24-2015-9298744\",\"venue_name\":\"O.co" +
        " Coliseum\"},{\"event_id\":\"9298697\",\"description\":\"San Diego Chargers at Kansas City " +
        "Chiefs Tickets\",\"event_date_time_local\":\"2015-12-13T12:00:00Z\",\"totalTickets\":12514.0," +
        "\"urlpath\":\"kansas-city-chiefs-kansas-city-arrowhead-stadium-12-13-2015-9298697\"," +
        "\"venue_name\":\"Arrowhead Stadium\"},{\"event_id\":\"9298747\",\"description\":\"San " +
        "Diego Chargers at Green Bay Packers Tickets\",\"event_date_time_local\":\"2015-10-18T15:25:00Z\"," +
        "\"totalTickets\":1777.0,\"urlpath\":\"green-bay-packers-green-bay-lambeau-field-10-18-2015-9298747" +
        "\",\"venue_name\":\"Lambeau Field\"},{\"event_id\":\"9289655\",\"description\":\"San" +
        " Diego Chargers at Arizona Cardinals Preseason Tickets\"," +
        "\"event_date_time_local\":\"2015-08-22T19:00:00Z\",\"totalTickets\":10472.0," +
        "\"urlpath\":\"arizona-cardinals-glendale-university-of-phoenix-stadium-8-22-2015-9289655\"," +
        "\"venue_name\":\"University of Phoenix Stadium\"}]}}";

    StubHubResponseContainer stubHubResponse = new Gson().fromJson(response, StubHubResponseContainer.class);
    assertThat(stubHubResponse).isNotNull();
    assertThat(stubHubResponse.getEvents()).isNotEmpty();
    assertThat(stubHubResponse.getNumFound()).isEqualTo(20);
  }
}
