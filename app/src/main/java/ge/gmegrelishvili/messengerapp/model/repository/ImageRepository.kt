package ge.gmegrelishvili.messengerapp.model.repository

import android.graphics.Bitmap
import android.net.Uri

interface ImageRepository {

    fun uploadImage(key: String, uri: Uri, uploadImageResult: UploadImageResult)

    fun downloadImage(key: String, downloadImageResult: DownloadImageResult)

    companion object {
        interface UploadImageResult {
            fun <T> uploadImageFinished(error: T?)
        }

        interface DownloadImageResult {
            fun <T> downloadImageFinished(bitmap: Bitmap?, error: T?)
        }
    }

}