package version.evening.canvas.flutrack

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment

class AboutFragment : DialogFragment() {
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        activity?.layoutInflater?.let {
            builder.setView(it.inflate(R.layout.fragment_about, null))
        }

        return builder.apply {
            setPositiveButton(R.string.aboutDialogPositiveButton, null)
            setTitle(R.string.aboutDialogTitle)
        }.create()
    }
}

fun createAboutFragment(): AboutFragment = AboutFragment()
