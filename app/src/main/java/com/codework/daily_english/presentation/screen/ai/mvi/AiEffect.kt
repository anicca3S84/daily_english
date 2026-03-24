package com.codework.daily_english.presentation.screen.ai.mvi

import com.codework.daily_english.core.base.BaseEffect

sealed interface AiEffect : BaseEffect{
    data class ShowToast(val message: String) : AiEffect
    // Màn AI này chủ yếu xử lý tại chỗ, ít khi cần điều hướng (trừ khi bạn muốn mở camera/thư viện ảnh)
    data object OpenGallery : AiEffect
    data object OpenMicrophone : AiEffect
}