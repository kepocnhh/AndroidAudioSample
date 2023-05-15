package test.android.audio

import android.content.Context
import android.widget.Toast

internal fun Context.showToast(message: CharSequence, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}
