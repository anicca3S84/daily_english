package com.codework.daily_english.data.translate.remote

import com.codework.daily_english.core.network.safeApiCall
import com.codework.daily_english.core.network.unwrap
import com.codework.daily_english.core.result.AppError
import com.codework.daily_english.core.result.AppResult
import com.codework.daily_english.data.translate.remote.dto.TranslateResponseDto
import jakarta.inject.Inject
import kotlinx.coroutines.delay

interface TranslateRemoteDataSource {
    suspend fun translate(
        text: String,
        source: String,
        target: String
    ): AppResult<TranslateResponseDto>
}

class TranslateRemoteDataSourceImpl @Inject constructor(
    private val api: TranslateApi
) : TranslateRemoteDataSource {

//    override suspend fun translate(text: String, source: String, target: String): AppResult<TranslateResponseDto> {
//        return safeApiCall { api.translate(text, source, target) }.unwrap()
//    }

    //fake
    override suspend fun translate(
        text: String,
        source: String,
        target: String
    ): AppResult<TranslateResponseDto> {
        delay(1500)
        if (text.lowercase() == "error") {
            return AppResult.Error(
                error = AppError.Network,
                message = "Đây là lỗi mạng giả lập để test UI!"
            )
        }
        // 3. Trả về kết quả thành công ảo
        // LƯU Ý: Sửa lại các tham số bên trong TranslateResponseDto cho đúng với file của bạn nhé
        val fakeResult = TranslateResponseDto(
            translatedText = "Bản dịch ảo của: '$text' (Từ $source sang $target)"
            // Nếu DTO có historyId hay gì đó, bạn cứ điền bừa vào đây
        )
        return AppResult.Success(fakeResult)
    }


}