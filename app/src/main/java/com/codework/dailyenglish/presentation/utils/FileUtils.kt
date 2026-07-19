package com.codework.dailyenglish.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Uri.toFile(context: Context): File? {
    val contentResolver = context.contentResolver ?: return null
    // Tạo một file tạm trong thư mục cache của ứng dụng
    val tempFile = File(context.cacheDir, "temp_avatar_${System.currentTimeMillis()}.jpg")

    return try {
        val inputStream: InputStream = contentResolver.openInputStream(this) ?: return null
        val outputStream = FileOutputStream(tempFile)
        val buffer = ByteArray(4 * 1024) // 4KB buffer
        var read: Int
        while (inputStream.read(buffer).also { read = it } != -1) {
            outputStream.write(buffer, 0, read)
        }
        outputStream.flush()
        outputStream.close()
        inputStream.close()
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Context.createTempPictureUri(): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    // Tạo file tạm trong thư mục cache
    val tempFile = File.createTempFile(
        "JPEG_${timeStamp}_",
        ".jpg",
        cacheDir
    )

    return FileProvider.getUriForFile(
        this,
        "${packageName}.fileprovider", // Phải khớp với authorities ở AndroidManifest
        tempFile
    )
}

fun compressImage(context: Context, imageUri: Uri, maxFileSizeKB: Int = 500): File? {
    try {
        // 1. Xác định ma trận xoay từ thông tin EXIF của ảnh gốc
        val matrix = Matrix()
        context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
            val exifInterface = ExifInterface(inputStream)
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
            }
        }

        // 2. Đọc Uri thành Bitmap gốc
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close() ?: return null

        // 3. Áp dụng xoay Bitmap nếu cần thiết dựa vào thông tin EXIF
        val rotatedBitmap = if (!matrix.isIdentity) {
            val processed = Bitmap.createBitmap(
                originalBitmap,
                0, 0,
                originalBitmap.width,
                originalBitmap.height,
                matrix,
                true
            )
            if (processed != originalBitmap) {
                originalBitmap.recycle() // Giải phóng ảnh gốc để tránh rò rỉ bộ nhớ
            }
            processed
        } else {
            originalBitmap
        }

        var quality = 100
        val byteArrayOutputStream = ByteArrayOutputStream()

        // 4. Vòng lặp nén chất lượng ảnh giảm dần
        do {
            byteArrayOutputStream.reset()
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
            quality -= 10
        } while (byteArrayOutputStream.toByteArray().size / 1024 > maxFileSizeKB && quality > 10)

        // Giải phóng Bitmap đã nén khỏi bộ nhớ sau khi hoàn tất nén
        rotatedBitmap.recycle()

        // 5. Ghi dữ liệu đã nén vào file tạm mới trong bộ nhớ đệm (Cache)
        // Thay đổi tên file theo thời gian để tránh bị đè hoặc bị dính cơ chế cache của thư viện tải ảnh
        val compressedFile = File(context.cacheDir, "avatar_compressed_${System.currentTimeMillis()}.jpg")
        val fileOutputStream = FileOutputStream(compressedFile)
        fileOutputStream.write(byteArrayOutputStream.toByteArray())
        fileOutputStream.flush()
        fileOutputStream.close()

        return compressedFile
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}