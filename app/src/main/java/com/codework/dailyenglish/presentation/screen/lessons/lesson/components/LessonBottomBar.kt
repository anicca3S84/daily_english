package com.codework.dailyenglish.presentation.screen.lessons.lesson.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.domain.lesson.model.LessonStep
import com.codework.dailyenglish.domain.lesson.model.StepData

import com.codework.dailyenglish.presentation.components.AppCircleButton

@Composable
fun LessonBottomBar(
    currentStep: LessonStep?,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val isNextEnabled = when (val data = currentStep?.data) {
        is StepData.Question -> data.selectedOptionId != null
        else -> true
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppCircleButton(icon = Icons.AutoMirrored.Filled.ArrowBack, onClick = onBackClick)

        Button(
            onClick = onNextClick,
            enabled = isNextEnabled,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Tiếp tục", fontWeight = FontWeight.Bold)
        }
    }
}

