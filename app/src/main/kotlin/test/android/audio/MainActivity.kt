package test.android.audio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

internal class MainActivity : AppCompatActivity() {
    private var recordButton: TextView? = null
    private var playButton: TextView? = null
    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null

    private fun startRecord() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 1)
            return
        }
        if (recorder != null) TODO()
        val audioSource = MediaRecorder.AudioSource.MIC
        val outputFormat = MediaRecorder.OutputFormat.DEFAULT
        val audioEncoder = MediaRecorder.AudioEncoder.DEFAULT
        val outputFile = checkNotNull(outputFile)
        outputFile.delete()
        checkNotNull(recordButton).also {
            it.text = "stop record"
            it.setOnClickListener {
                stopRecord()
            }
        }
        recorder = newMediaRecorder().also {
            it.setAudioSource(audioSource)
            it.setOutputFormat(outputFormat)
            it.setAudioEncoder(audioEncoder)
            it.setOutputFile(outputFile.absolutePath)
            it.prepare()
            it.start()
        }
    }

    private fun stopRecord() {
        checkNotNull(recorder).also {
            it.stop()
            it.release()
        }
        recorder = null
        checkNotNull(recordButton).also {
            it.text = "start record"
            it.setOnClickListener {
                startRecord()
            }
        }
    }

    private fun playRecord() {
        val outputFile = outputFile ?: return
        if (!outputFile.exists()) return
        if (!outputFile.isFile) return
        if (outputFile.length() == 0L) return
        TODO()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context: Context = this
        outputFile = cacheDir.resolve("foo")
        val root = FrameLayout(context)
        LinearLayout(context).also { rows ->
            rows.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_VERTICAL
            )
            rows.orientation = LinearLayout.VERTICAL
            Button(context).also {
                it.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                it.text = "start record"
                it.setOnClickListener {
                    startRecord()
                }
                recordButton = it
                rows.addView(it)
            }
            Button(context).also {
                it.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                it.text = "play"
                it.setOnClickListener {
                    playRecord()
                }
                playButton = it
                rows.addView(it)
            }
            root.addView(rows)
        }
        setContentView(root)
    }
}
