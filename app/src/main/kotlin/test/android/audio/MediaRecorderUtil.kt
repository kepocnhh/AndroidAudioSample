package test.android.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File

internal fun Context.newMediaRecorder(): MediaRecorder {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        MediaRecorder(this)
    } else {
        MediaRecorder()
    }
}

internal fun MediaRecorder.prepare(
    audioSource: Int,
    outputFormat: Int,
    audioEncoder: Int,
    outputFile: File,
) {
    setAudioSource(audioSource)
    setOutputFormat(outputFormat)
    setAudioEncoder(audioEncoder)
    setOutputFile(outputFile.absolutePath)
    prepare()
}
