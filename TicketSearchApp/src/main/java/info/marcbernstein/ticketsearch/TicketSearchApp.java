package info.marcbernstein.ticketsearch;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

public class TicketSearchApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    JodaTimeAndroid.init(this);
  }
}
