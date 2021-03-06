package version.evening.canvas.flutrack

import io.reactivex.Observable
import java.text.SimpleDateFormat
import java.util.Calendar

const val MILLIS_IN_SECOND = 1000L

fun String.millisToReadableDate(): String {
    val formatter = SimpleDateFormat.getDateInstance()
    val calendar = Calendar.getInstance().apply {
        timeInMillis = toLong() * MILLIS_IN_SECOND
    }
    return formatter.format(calendar.time)
}

fun Calendar.defaultDateFormat(): String = SimpleDateFormat.getInstance().format(time)

/**
 * false when "", "false", "0"
 */
fun String.toBooleanExtra(): Boolean =
        !(isEmpty() || equals("false", true) || equals("0"))

fun <T> Observable<T>.doOnFirst(action: () -> Unit): Observable<T> = take(1).doOnNext { action() }.concatWith(this)
