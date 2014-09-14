package info.marcbernstein.ticketsearch;

import android.app.Application;
import android.content.Context;

public class TicketSearchApplication extends Application {
  private static TicketSearchApplication sInstance;

  @Override
  public void onCreate() {
    super.onCreate();

    sInstance = this;
  }

  /**
   * Convenient way to get global access to the ApplicationContext. You must be careful to use this properly however,
   * as this context is not suitable for certain actions. See <a href="http://www.doubleencore
   * .com/2013/06/context">http://www.doubleencore.com/2013/06/context</a>
   * for more info.
   */
  public static Context getContext() {
    return sInstance;
  }
}
