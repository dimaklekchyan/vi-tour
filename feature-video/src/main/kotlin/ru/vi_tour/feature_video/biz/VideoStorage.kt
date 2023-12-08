package ru.vi_tour.feature_video.biz

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Size
import ru.vi_tour.data_video.domain.model.Video

object VideoStorage {

    val PERMISSION = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_VIDEO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    const val PERMISSIONS_KEY = "storage_permissions"

    private val videoCollections = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }

    private val videoProjection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.SIZE,
    )

    fun getVideoByUri(uri: Uri, context: Context): Video? {
        val query = getSpecificVideoCursor(context, uri)

        val video = query?.use { cursor ->
            cursor.moveToNext()
            getVideoFromCursor(cursor, context)
        }

        return video
    }

    fun getExternalVideos(context: Context, page: Int, pageSize: Int): List<Video> {
        val videos = mutableListOf<Video>()

        val query = getExternalVideoCursor(context, page, pageSize)

        query?.use { cursor ->
            while (cursor.moveToNext()) {
                val video = getVideoFromCursor(cursor, context)
                videos += video
            }
        }

        return videos
    }

    private fun getVideoFromCursor(cursor: Cursor, context: Context): Video {
        val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
        val duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
        val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))

        val contentUri: Uri = ContentUris.withAppendedId(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            id
        )

        val thumbnail: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.contentResolver
                .loadThumbnail(contentUri, Size(640, 480), null)
        } else {
            MediaStore.Video.Thumbnails.getThumbnail(context.contentResolver, id, MediaStore.Video.Thumbnails.MINI_KIND, null)
        }

        return Video(
            uri = contentUri,
            name = name,
            duration = duration,
            length = size,
            thumbnail = thumbnail
        )
    }

    private fun getExternalVideoCursor(
        context: Context,
        page: Int,
        pageSize: Int
    ) = getVideoCursor(context, videoCollections, page, pageSize)

    private fun getSpecificVideoCursor(
        context: Context,
        uri: Uri
    ) = getVideoCursor(context, uri, 0, 1)

    private fun getVideoCursor(
        context: Context,
        uri: Uri,
        page: Int,
        pageSize: Int
    ): Cursor? {
        val offset = page * pageSize
        val sort = MediaStore.Files.FileColumns.DATE_MODIFIED
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.contentResolver.query(
                uri,
                videoProjection,
                Bundle().apply {
                    // sorting by
                    putStringArray(
                        ContentResolver.QUERY_ARG_SORT_COLUMNS,
                        arrayOf(sort)
                    )
                    // sorting direction
                    putInt(
                        ContentResolver.QUERY_ARG_SORT_DIRECTION,
                        ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                    )
                    // limit and offset
                    putInt(ContentResolver.QUERY_ARG_LIMIT, pageSize)
                    putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
                },
                null
            )
        } else {
            val sortOrder = "$sort DESC LIMIT $pageSize OFFSET $offset"

            context.contentResolver.query(
                uri,
                videoProjection,
                null,
                null,
                sortOrder
            )
        }
    }
}