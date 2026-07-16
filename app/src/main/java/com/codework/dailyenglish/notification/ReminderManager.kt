package com.codework.dailyenglish.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

class ReminderManager(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "daily_reminder_channel"
            val channelName = "Daily Reminders"
            val descriptionText = "Nhắc nhở học tập hàng ngày"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = descriptionText
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun rescheduleDailyReminders() {
        // 1. Hủy lịch hẹn cũ trước để đảm bảo không bị trùng lặp
        cancelAllReminders()

        // 2. Chỉ đặt lịch duy nhất 1 lần sau 5 giây
        val calendar = Calendar.getInstance().apply {
            add(Calendar.SECOND, 5)
        }

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            // Gửi kèm notification_id là 1
            putExtra("notification_id", 1)
        }

        // Sử dụng ID là 1 và giữ lại cờ FLAG_MUTABLE để tránh bị lỗi hệ thống
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val canSchedule = alarmManager.canScheduleExactAlarms()
                android.util.Log.d("DailyEnglishDebug", "Quyền canScheduleExactAlarms: $canSchedule")

                if (canSchedule) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                    android.util.Log.d("DailyEnglishDebug", "Đã lên lịch báo thức chính xác sau 5 giây")
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                    android.util.Log.d("DailyEnglishDebug", "Quyền bị từ chối, hạ cấp xuống báo thức thông thường")
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            android.util.Log.e("DailyEnglishDebug", "Lỗi bảo mật khi đặt báo thức: ${e.message}")
        }
    }

    // Hàm hủy lịch cũng thu gọn lại để dọn duy nhất ID số 1
    private fun cancelAllReminders() {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1, // Khớp với ID 1 lúc tạo
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            android.util.Log.d("DailyEnglishDebug", "Đã hủy lịch cũ thành công")
        }
    }
}