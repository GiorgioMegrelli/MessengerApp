package ge.gmegrelishvili.messengerapp.view

import android.content.Context
import android.widget.Toast

class ToastWrapper(private val context: Context) {

    private fun show(value: String, duration: Int) {
        Toast.makeText(context, value, duration).show()
    }

    private fun showIf(value: String, duration: Int, condition: Boolean) {
        if (condition) {
            show(value, duration)
        }
    }

    fun short(value: String) {
        show(value, Toast.LENGTH_SHORT)
    }

    fun long(value: String) {
        show(value, Toast.LENGTH_LONG)
    }

    fun shortIf(value: String, condition: Boolean): Boolean {
        showIf(value, Toast.LENGTH_SHORT, condition)
        return condition
    }

    fun longIf(value: String, condition: Boolean): Boolean {
        showIf(value, Toast.LENGTH_LONG, condition)
        return condition
    }

}