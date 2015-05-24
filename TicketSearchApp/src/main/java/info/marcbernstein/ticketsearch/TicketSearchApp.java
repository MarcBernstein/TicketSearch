package info.marcbernstein.ticketsearch;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

public class TicketSearchApp extends Application {

  private static TicketSearchApp sInstance;

  @Override
  public void onCreate() {
    super.onCreate();
    sInstance = this;
    JodaTimeAndroid.init(this);
  }

  public static TicketSearchApp getInstance() {
    return sInstance;
  }
}
