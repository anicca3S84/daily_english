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

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val openAppPendingIntent = PendingIntent.getActivity(
            context,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Dùng cờ IMMUTABLE theo chuẩn bảo mật
        )

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