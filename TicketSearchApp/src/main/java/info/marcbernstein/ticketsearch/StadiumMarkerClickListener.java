package info.marcbernstein.ticketsearch;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;
import com.google.common.base.Preconditions;

public class StadiumMarkerClickListener implements OnMarkerClickListener {

  private final Context mContext;

  public StadiumMarkerClickListener(Context context) {
    Preconditions.checkNotNull(context, "Context cannot be null.");
    mContext = context.getApplicationContext();
  }

  @Override
  public boolean onMarkerClick(Marker marker) {
    // TODO implement
    Toast.makeText(mContext, marker.getTitle(), Toast.LENGTH_SHORT).show();
    return true;
  }
}
