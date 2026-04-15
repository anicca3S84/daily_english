package com.codework.dailyenglish.presentation.theme.provider

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.material.icons.rounded.LocalFlorist
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.codework.dailyenglish.core.utils.Constants
import com.codework.dailyenglish.presentation.theme.*

/**
 * Builder tạo CourseTheme
 */
object CourseThemeProvider {
    private fun createCourseTheme(
        hex: String,
        primary: Color,
        title: Color,
        background: Color,
        gradientStart: Color,
        gradientEnd: Color,
        icon: ImageVector
    ): CourseTheme {
        return CourseTheme(
            themeColorHex = hex,
            primaryColor = primary,
            moduleTitleColor = title,
            moduleHeaderBackgroundColor = background,
            headerColor = Brush.horizontalGradient(
                colors = listOf(gradientStart, gradientEnd)
            ),
            icon = icon
        )
    }

    /**
     * Map theme theo level (data-driven, dễ scale)
     */
    private val themeMap: Map<String, CourseTheme> = mapOf(
        Constants.LEVEL_ROOKIE to createCourseTheme(
            "#4CAF50",
            RookiePrimary,
            RookieTitle,
            RookieBackground,
            RookieGradientStart,
            RookieGradientEnd,
            Icons.Rounded.LocalFlorist
        ),
        Constants.LEVEL_EXPLORER to createCourseTheme(
            "#2196F3",
            ExplorerPrimary,
            ExplorerTitle,
            ExplorerBackground,
            ExplorerGradientStart,
            ExplorerGradientEnd,
            Icons.Rounded.Explore
        ),
        Constants.LEVEL_COMMUNICATOR to createCourseTheme(
            "#FF9800",
            CommunicatorPrimary,
            CommunicatorTitle,
            CommunicatorBackground,
            CommunicatorGradientStart,
            CommunicatorGradientEnd,
            Icons.Rounded.Forum
        ),
        Constants.LEVEL_FLUENT to createCourseTheme(
            "#E91E63",
            FluentPrimary,
            FluentTitle,
            FluentBackground,
            FluentGradientStart,
            FluentGradientEnd,
            Icons.Rounded.AutoGraph
        ),
        Constants.LEVEL_MASTER to createCourseTheme(
            "#9C27B0",
            MasterPrimary,
            MasterTitle,
            MasterBackground,
            MasterGradientStart,
            MasterGradientEnd,
            Icons.Rounded.WorkspacePremium
        ),
        Constants.LEVEL_LEGENDARY to createCourseTheme(
            "#FFC107",
            LegendaryPrimary,
            LegendaryTitle,
            LegendaryBackground,
            LegendaryGradientStart,
            LegendaryGradientEnd,
            Icons.Rounded.WorkspacePremium
        )
    )

    private val DefaultTheme = themeMap[Constants.LEVEL_ROOKIE]!!


    fun getThemeByCode(code: String?): CourseTheme {
        return themeMap[code?.trim()?.uppercase()] ?: DefaultTheme
    }
}