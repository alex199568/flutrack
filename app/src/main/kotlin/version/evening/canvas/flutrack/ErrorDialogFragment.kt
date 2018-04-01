package version.evening.canvas.flutrack

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class ErrorDialogFragment : DialogFragment() {
    private val retrySubject = BehaviorSubject.create<Unit>()

    val retryObservable: Observable<Unit> = retrySubject

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = context ?: throw IllegalStateException("attempting to show dialog with null context")

        isCancelable = false

        return AlertDialog.Builder(context).apply {
            setPositiveButton(R.string.errorDialogButton, { _, _ -> retrySubject.onNext(Unit) })
            setTitle(R.string.errorDialogTitle)
            setMessage(R.string.errorDialogMessage)
            setIcon(android.R.drawable.ic_dialog_alert)
        }.create()
    }
}
