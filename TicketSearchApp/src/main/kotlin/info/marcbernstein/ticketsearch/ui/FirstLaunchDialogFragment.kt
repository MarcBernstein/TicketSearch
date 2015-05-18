package info.marcbernstein.ticketsearch.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater

import info.marcbernstein.ticketsearch.R

/**
 * A dialog shown to the user upon the first launch of the app. The dialog shows information about the app,
 * including what it is meant to be used for and how it works.
 */
public class FirstLaunchDialogFragment : DialogFragment() {

    SuppressLint("InflateParams") // No root view to be the parent
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(getActivity())
        builder.setView(LayoutInflater.from(getActivity()).inflate(R.layout.first_launch_dialog, null))
        builder.setCancelable(true)
        builder.setTitle(R.string.app_name)
        builder.setPositiveButton(android.R.string.ok, null)

        return builder.create()
    }

    companion object {

        public val TAG: String = javaClass<FirstLaunchDialogFragment>().getSimpleName()

        public fun newInstance(): DialogFragment {
            return FirstLaunchDialogFragment()
        }
    }
}
