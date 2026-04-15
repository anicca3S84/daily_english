package com.codework.dailyenglish.presentation.screen.courses.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.theme.Spaces

@Composable
fun EmptyCourseState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Spaces.Space100),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(Spaces.Space80),
            tint = Color(0xFFCFD8DC)
        )
        Text(text = stringResource(R.string.msg_no_lesson_available), color = Color.Gray)
    }
}