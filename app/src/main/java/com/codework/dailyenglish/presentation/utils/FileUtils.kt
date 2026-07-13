package com.codework.dailyenglish.presentation.utils


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
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
        // 1. Đọc Uri thành Bitmap
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close() ?: return null

        var quality = 100
        val byteArrayOutputStream = ByteArrayOutputStream()

        // 2. Vòng lặp nén chất lượng ảnh giảm dần cho đến khi đạt dung lượng mong muốn (VD: dưới 500KB)
        do {
            byteArrayOutputStream.reset()
            // Nén ảnh theo định dạng JPEG với chất lượng `quality`
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
            quality -= 10 // Mỗi lần giảm 10% chất lượng nếu file vẫn quá lớn
        } while (byteArrayOutputStream.toByteArray().size / 1024 > maxFileSizeKB && quality > 10)

        // 3. Ghi dữ liệu đã nén vào một file tạm (Cache) để chuẩn bị gửi lên Server
        val compressedFile = File(context.cacheDir, "avatar_compressed.jpg")
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