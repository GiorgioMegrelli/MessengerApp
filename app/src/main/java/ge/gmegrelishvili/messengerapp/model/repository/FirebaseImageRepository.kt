package ge.gmegrelishvili.messengerapp.model.repository

import android.graphics.BitmapFactory
import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class FirebaseImageRepository : ImageRepository {

    private val storage = Firebase.storage

    override fun uploadImage(
        key: String,
        uri: Uri,
        uploadImageResult: ImageRepository.Companion.UploadImageResult
    ) {
        val reference = storage.getReference(ImagesReference)
        reference.child(key).putFile(uri).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                uploadImageResult.uploadImageFinished(null)
            } else {
                uploadImageResult.uploadImageFinished(task.exception)
            }
        }
    }

    override fun downloadImage(
        key: String,
        downloadImageResult: ImageRepository.Companion.DownloadImageResult
    ) {
        val reference = storage.getReference(ImagesReference)
        reference.child(key).getBytes(ImageMaxSize).addOnCompleteListener { task ->
            val result = task.result
            val bitmap = BitmapFactory.decodeByteArray(result, 0, result.size)
            downloadImageResult.downloadImageFinished(bitmap, null)
        }
    }

    companion object {
        private const val ImagesReference = "profileImages"

        const val ImageMaxSize = 10 * 1024L * 1024L
    }

}