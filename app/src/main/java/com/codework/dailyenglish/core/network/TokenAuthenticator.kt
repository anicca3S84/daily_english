package com.codework.dailyenglish.core.network

import com.codework.dailyenglish.data.auth.local.AuthPreferences
import com.codework.dailyenglish.data.auth.remote.AuthApi
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val prefs: AuthPreferences,
    private val authApi: Provider<AuthApi>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // 1. Lấy Refresh Token từ máy. Nếu null thì dừng (buộc login lại)
        val refreshToken = prefs.getRefreshToken() ?: return null

        // 2. Gọi API đổi Token đồng bộ (.execute())
        // Vì AuthApi trả về Call<AuthDto>, nên res.body() chính là AuthDto
        val res = authApi.get().refreshToken("Bearer $refreshToken").execute()
        val authDto = res.body()

        // 3. Kiểm tra: Phải thành công VÀ các token trong AuthDto không được null
        return if (res.isSuccessful && authDto != null &&
            authDto.accessToken != null && authDto.refreshToken != null) {

            // Lưu token mới vào SharedPreferences
            // Kotlin tự Smart Cast từ String? sang String nhờ check null ở trên
            prefs.saveAccessToken(authDto.accessToken)
            prefs.saveRefreshToken(authDto.refreshToken)

            // 4. Gửi lại request bị lỗi 401 trước đó với Access Token mới
            response.request.newBuilder()
                .header("Authorization", "Bearer ${authDto.accessToken}")
                .build()
        } else {
            // Nếu đổi thất bại (Refresh Token hết hạn) -> Xóa sạch để về màn Login
            prefs.clearAll()
            null
        }
    }
}