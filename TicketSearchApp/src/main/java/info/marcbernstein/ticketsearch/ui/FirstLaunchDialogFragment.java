package info.marcbernstein.ticketsearch.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;

import info.marcbernstein.ticketsearch.R;

public class FirstLaunchDialogFragment extends DialogFragment {
  public static final String TAG = FirstLaunchDialogFragment.class.getSimpleName();

  public static DialogFragment newInstance() {
    return new FirstLaunchDialogFragment();
  }

  public FirstLaunchDialogFragment() {
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setView(LayoutInflater.from(getActivity()).inflate(R.layout.first_launch_dialog, null));
    builder.setCancelable(true);
    builder.setTitle(R.string.app_name);
    builder.setPositiveButton(android.R.string.ok, null);

    return builder.create();
  }
}
