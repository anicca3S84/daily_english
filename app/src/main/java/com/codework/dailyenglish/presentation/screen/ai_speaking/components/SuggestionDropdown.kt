package com.codework.dailyenglish.presentation.screen.ai_speaking.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SuggestionDropdown(onDismiss: () -> Unit, onSelect: (String) -> Unit) {
    val suggestions = listOf("Can I order a coffee?", "How's your day?", "What do you think?")

    DropdownMenu(expanded = true, onDismissRequest = onDismiss) {
        suggestions.forEach { text ->
            DropdownMenuItem(
                text = { Text(text) },
                onClick = { onSelect(text) }
            )
        }
    }
}