package test.android.audio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

internal class MainActivity : AppCompatActivity() {
    private var recordButton: TextView? = null
    private var playButton: TextView? = null
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var file: File? = null
    private var outputFormat = MediaRecorder.OutputFormat.DEFAULT
    private var audioEncoder = MediaRecorder.AudioEncoder.DEFAULT

    private enum class OutputFormat {
        DEFAULT,
        THREE_GPP,
        MPEG_4,
        RAW_AMR,
        AMR_NB,
        AMR_WB,
//        AAC_ADIF,
        AAC_ADTS,
//        OUTPUT_FORMAT_RTP_AVP,
//        MPEG_2_TS,
        WEBM,
//        HEIF,
//        OGG,
    }

    private enum class AudioEncoder {
        DEFAULT,
        AMR_NB,
        AMR_WB,
        AAC,
        HE_AAC,
        AAC_ELD,
        VORBIS,
//        OPUS,
    }

    private fun startRecord() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 1)
            return
        }
        if (recorder != null) TODO()
        val audioSource = MediaRecorder.AudioSource.MIC
        val outputFile = checkNotNull(file)
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

    private fun playAudio() {
        val file = file ?: return
        if (!file.exists()) {
            showToast("File does not exist!")
            return
        }
        if (!file.isFile) TODO()
        if (file.length() == 0L) {
            showToast("File is empty!")
            return
        }
        if (player != null) TODO()
        checkNotNull(playButton).also {
            it.text = "stop audio"
            it.setOnClickListener {
                stopAudio()
            }
        }
        player = MediaPlayer().also {
            it.setDataSource(file.absolutePath)
            it.setOnCompletionListener {
                stopAudio()
            }
            it.prepare()
            it.start()
        }
    }

    private fun stopAudio() {
        checkNotNull(playButton).also {
            it.text = "play audio"
            it.setOnClickListener {
                playAudio()
            }
        }
        checkNotNull(player).also {
            it.stop()
            it.release()
        }
        player = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context: Context = this
        file = cacheDir.resolve("foo")
        val root = FrameLayout(context)
        LinearLayout(context).also { rows ->
            rows.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_VERTICAL
            )
            rows.orientation = LinearLayout.VERTICAL
            TextView(context).also {
                it.text = "Output format:"
                rows.addView(it)
            }
            Spinner(context).also {
                it.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    128,
                )
                val values = OutputFormat.values().map { format -> format.name }
                it.adapter = SpinnerAdapterGeneric(
                    context = context,
                    values = values
                )
                it.setOnItemSelectedListener { position ->
                    outputFormat = when (OutputFormat.valueOf(values[position])) {
                        OutputFormat.DEFAULT -> MediaRecorder.OutputFormat.DEFAULT
                        OutputFormat.THREE_GPP -> MediaRecorder.OutputFormat.THREE_GPP
                        OutputFormat.MPEG_4 -> MediaRecorder.OutputFormat.MPEG_4
                        OutputFormat.RAW_AMR -> MediaRecorder.OutputFormat.RAW_AMR
                        OutputFormat.AMR_NB -> MediaRecorder.OutputFormat.AMR_NB
                        OutputFormat.AMR_WB -> MediaRecorder.OutputFormat.AMR_WB
//                        OutputFormat.AAC_ADIF -> MediaRecorder.OutputFormat.AAC_ADIF
                        OutputFormat.AAC_ADTS -> MediaRecorder.OutputFormat.AAC_ADTS
//                        OutputFormat.OUTPUT_FORMAT_RTP_AVP -> MediaRecorder.OutputFormat.OUTPUT_FORMAT_RTP_AVP
//                        OutputFormat.MPEG_2_TS -> MediaRecorder.OutputFormat.MPEG_2_TS
                        OutputFormat.WEBM -> MediaRecorder.OutputFormat.WEBM
//                        OutputFormat.HEIF -> MediaRecorder.OutputFormat.HEIF
//                        OutputFormat.OGG -> MediaRecorder.OutputFormat.OGG
                    }
                }
                rows.addView(it)
            }
            TextView(context).also {
                it.text = "Audio encoder:"
                rows.addView(it)
            }
            Spinner(context).also {
                it.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    128,
                )
                val values = AudioEncoder.values().map { format -> format.name }
                it.adapter = SpinnerAdapterGeneric(
                    context = context,
                    values = values
                )
                it.setOnItemSelectedListener { position ->
                    audioEncoder = when (AudioEncoder.valueOf(values[position])) {
                        AudioEncoder.DEFAULT -> MediaRecorder.AudioEncoder.DEFAULT
                        AudioEncoder.AMR_NB -> MediaRecorder.AudioEncoder.AMR_NB
                        AudioEncoder.AMR_WB -> MediaRecorder.AudioEncoder.AMR_WB
                        AudioEncoder.AAC -> MediaRecorder.AudioEncoder.AAC
                        AudioEncoder.HE_AAC -> MediaRecorder.AudioEncoder.HE_AAC
                        AudioEncoder.AAC_ELD -> MediaRecorder.AudioEncoder.AAC_ELD
                        AudioEncoder.VORBIS -> MediaRecorder.AudioEncoder.VORBIS
//                        AudioEncoder.OPUS -> MediaRecorder.AudioEncoder.OPUS
                    }
                }
                rows.addView(it)
            }
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
                it.text = "play audio"
                it.setOnClickListener {
                    playAudio()
                }
                playButton = it
                rows.addView(it)
            }
            root.addView(rows)
        }
        setContentView(root)
    }
}
