package version.evening.canvas.flutrack

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.fragment_data_error.view.retryButton

class DataErrorFragment : Fragment() {
    private val retrySubject = BehaviorSubject.create<Unit>()
    val retryObservable: Observable<Unit> = retrySubject

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_data_error, container, false)

        view.retryButton.setOnClickListener { retrySubject.onNext(Unit) }

        return view
    }
}

fun createDataErrorFragment(): DataErrorFragment = DataErrorFragment()
