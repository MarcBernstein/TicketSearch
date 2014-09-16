package info.marcbernstein.ticketsearch.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;

import info.marcbernstein.ticketsearch.R;

/**
 * A dialog shown to the user upon the first launch of the app. The dialog shows information about the app,
 * including what it is meant to be used for and how it works.
 */
public class FirstLaunchDialogFragment extends DialogFragment {

  public static final String TAG = FirstLaunchDialogFragment.class.getSimpleName();

  public static DialogFragment newInstance() {
    return new FirstLaunchDialogFragment();
  }

  public FirstLaunchDialogFragment() {
  }

  @SuppressLint("InflateParams") // No root view to be the parent
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
