package com.codework.daily_english.core.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object PermissionManager {

    // --- NHÓM 1: QUYỀN VỊ TRÍ (Dùng cho Map & Đo đạc) ---
    fun getLocationPermissions(): Array<String> {
        return arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    // --- NHÓM 2: QUYỀN BLUETOOTH (Chỉ dùng khi đo sensor) ---
    fun getBluetoothPermissions(): Array<String> {
        val permissions = mutableListOf<String>()
        // Chỉ Android 12+ mới cần quyền này riêng biệt
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        return permissions.toTypedArray()
    }

    // --- NHÓM 3: QUYỀN THÔNG BÁO (Dùng khi chạy Service ngầm) ---
    fun getNotificationPermission(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            emptyArray()
        }
    }

    // ============================================================
    // CÁC HÀM TỔNG HỢP (Dùng cho từng tính năng cụ thể)
    // ============================================================

    // 1. Dùng cho nút "Bắt đầu đo" (Cần TẤT CẢ: GPS + BLE + Noti)
    fun getMeasurementPermissions(): Array<String> {
        val allPerms = mutableListOf<String>()
        allPerms.addAll(getLocationPermissions())
        allPerms.addAll(getBluetoothPermissions())
        allPerms.addAll(getNotificationPermission())
        return allPerms.toTypedArray()
    }

    // 2. Dùng cho màn hình "Xem bản đồ" (Chỉ cần GPS)
    fun getMapOnlyPermissions(): Array<String> {
        return getLocationPermissions()
    }

    // Hàm check tổng quát: Bạn truyền danh sách quyền nào vào thì nó check danh sách đó
    fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        return permissions.all { per ->
            ContextCompat.checkSelfPermission(context, per) == PackageManager.PERMISSION_GRANTED
        }
    }
}