package com.codework.dailyenglish.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import android.app.PendingIntent
import com.codework.dailyenglish.MainActivity

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        android.util.Log.d("DailyEnglishDebug", "ReminderReceiver.onReceive đã được kích hoạt!")
        val notificationId = intent.getIntExtra("notification_id", 0)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "daily_reminder_channel"

        // 1. Tạo Intent trỏ tới MainActivity để mở ứng dụng
        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            // Cờ này giúp mở lại app nếu app đang chạy ngầm, không tạo mới một Activity chồng lên
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        // 2. Bọc Intent vào PendingIntent để hệ thống Android có quyền thực thi khi click
        val openAppPendingIntent = PendingIntent.getActivity(
            context,
            0, // Request code cho việc mở app, có thể để là 0
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Dùng cờ IMMUTABLE theo chuẩn bảo mật
        )

        // 3. Gắn PendingIntent vào Notification Builder thông qua setContentIntent
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("🔥 Protect Your Learning Streak!")
            .setContentText("Don't let your hard-earned English learning streak end. Keep learning today and stay on track!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(openAppPendingIntent) // <-- THÊM DÒNG NÀY ĐỂ CLICK VÀO MỞ APP
            .setAutoCancel(true) // Tự động xóa thông báo sau khi click
            .build()

        notificationManager.notify(notificationId, notification)
        android.util.Log.d("DailyEnglishDebug", "Đã gửi yêu cầu hiển thị notification lên hệ thống")
    }
}