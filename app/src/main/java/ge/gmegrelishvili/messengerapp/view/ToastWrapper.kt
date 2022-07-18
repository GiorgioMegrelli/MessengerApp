package ge.gmegrelishvili.messengerapp.view

import android.content.Context
import android.widget.Toast

class ToastWrapper(private val context: Context) {

    private fun show(value: String, duration: Int) {
        Toast.makeText(context, value, duration).show()
    }

    fun short(value: String) {
        show(value, Toast.LENGTH_SHORT)
    }

    fun long(value: String) {
        show(value, Toast.LENGTH_LONG)
    }

}