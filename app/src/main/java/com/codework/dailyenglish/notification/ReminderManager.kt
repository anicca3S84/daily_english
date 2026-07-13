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
        cancelAllReminders()

        for (i in 1..7) {
            val calendar = Calendar.getInstance().apply {
                add(Calendar.SECOND, 5 * i)
            }

            val intent = Intent(context, ReminderReceiver::class.java).apply {
                // Đưa ID của thông báo vào intent để Receiver có thể nhận được
                putExtra("notification_id", i)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                i, // RequestCode vẫn giữ nguyên để không bị ghi đè báo thức
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val canSchedule = alarmManager.canScheduleExactAlarms()
                    // Ghi log kiểm tra xem quyền thực sự đang là True hay False
                    android.util.Log.d("DailyEnglishDebug", "Quyền canScheduleExactAlarms: $canSchedule")

                    if (canSchedule) {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                        )
                        android.util.Log.d("DailyEnglishDebug", "Đã lên lịch báo thức chính xác cho mốc ${5 * i} giây")
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
    }

    // Hủy bỏ toàn bộ báo thức của 7 ngày đã lên lịch trước đó
    private fun cancelAllReminders() {
        for (i in 1..7) {
            val intent = Intent(context, ReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                i,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }
    }
}