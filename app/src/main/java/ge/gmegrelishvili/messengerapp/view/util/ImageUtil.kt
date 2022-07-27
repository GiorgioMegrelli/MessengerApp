package ge.gmegrelishvili.messengerapp.view.util

import android.content.ContentResolver
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import kotlin.math.max
import kotlin.math.min

class ImageUtil {

    companion object {
        @JvmStatic
        fun cropToSquare(bitmap: Bitmap): Bitmap {
            if (bitmap.width == bitmap.height) {
                return bitmap
            }
            val sizeMin = min(bitmap.width, bitmap.height)
            val sizeMax = max(bitmap.width, bitmap.height)
            val offset = (sizeMax - sizeMin) / 2
            val x = if (sizeMin == bitmap.width) 0 else offset
            val y = if (sizeMin == bitmap.height) 0 else offset
            return Bitmap.createBitmap(bitmap, x, y, sizeMin, sizeMin)
        }

        @JvmStatic
        fun cropImageRounded(bitmapArg: Bitmap, fitSquare: Boolean = true): Bitmap {
            val bitmap = if (fitSquare) {
                cropToSquare(bitmapArg)
            } else {
                bitmapArg
            }
            val width = bitmap.width
            val height = bitmap.height
            val output = Bitmap.createBitmap(
                bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888
            )
            val paintColor = Paint().apply {
                flags = Paint.ANTI_ALIAS_FLAG
            }
            val rectF = RectF(Rect(0, 0, width, height))
            val paintImage = Paint().apply {
                xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
            }
            Canvas(output).apply {
                drawRoundRect(rectF, width / 2f, height / 2f, paintColor)
                drawBitmap(bitmap, 0f, 0f, paintImage)
            }
            return output
        }

        @JvmStatic
        fun loadBitmap(uri: Uri, contentResolver: ContentResolver): Bitmap? {
            return try {
                if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(
                        contentResolver,
                        uri
                    )
                } else {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(contentResolver, uri)
                    )
                }
            } catch (e: Exception) {
                null
            }
        }
    }

}