package ru.vi_tour.data_video.domain.model

import android.graphics.Bitmap
import android.net.Uri

data class Video(
    val uri: Uri,
    val name: String,
    val duration: Int,
    val length: Long,
    val thumbnail: Bitmap
) {
    val sizeMB: Double
        get() = length.toDouble() / 1024 / 1024
    val durationMinSec: String
        get() {
            val allSeconds = duration / 1000
            val minutes = allSeconds / 60
            val seconds = allSeconds % 60
            val minutesStr = if(minutes < 10) "0$minutes" else minutes.toString()
            val secondsStr = if(seconds < 10) "0$seconds" else seconds.toString()
            return "$minutesStr:$secondsStr"
        }
}
