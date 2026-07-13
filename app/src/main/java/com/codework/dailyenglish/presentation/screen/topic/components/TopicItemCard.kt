package com.codework.dailyenglish.presentation.screen.topic.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.codework.dailyenglish.domain.vocabulary.model.VocabTopic
import com.codework.dailyenglish.presentation.theme.AppColors

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TopicItemCard(
    topic: VocabTopic,
    showWordCount: Boolean,
    onClick: () -> Unit
) {

    val bgColor = when (topic.name.lowercase()) {
        "business"   -> Color(0xFFEEF2FF)
        "travel"     -> Color(0xFFE0F2FE)
        "food"       -> Color(0xFFFFF7D6)
        "animals"    -> Color(0xFFDCFCE7)
        "technology" -> Color(0xFFF3E8FF)
        "health"     -> Color(0xFFFFE4E6)
        "sports"     -> Color(0xFFFFEDD5)
        "education"  -> Color(0xFFCCFBF1)
        else         -> Color(0xFFF8FAFC)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.92f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(
                    color = AppColors.BluePrimary.copy(alpha = 0.08f)
                ),
                onClick = onClick
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {

                GlideImage(
                    model = topic.imageUrl,
                    contentDescription = topic.name,
                    modifier = Modifier.size(72.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.55f)
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = topic.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (showWordCount) {
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${topic.totalWords} words",
                        fontSize = 14.sp,
                        color = Color(0xFF94A3B8),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}