package com.codework.dailyenglish.presentation.screen.subscription.components

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.screen.subscription.mvi.SubscriptionEvent
import com.codework.dailyenglish.presentation.screen.subscription.mvi.SubscriptionState
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun IntroAndPacksSectionContent(
    state: SubscriptionState,
    onEvent: (SubscriptionEvent) -> Unit,
    textDark: Color,
    textLight: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_app_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Unlock DailyEnglish PRO",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Black,
                color = textDark,
                fontSize = 24.sp
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Experience an unlimited",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = textLight,
                fontSize = 15.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8FAFC), shape = RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Your Active Benefits",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textLight,
                    letterSpacing = 1.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Remove all ads",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )
                    )
                    Text(
                        text = "Ad-free & seamless learning experience.",
                        style = MaterialTheme.typography.bodySmall.copy(color = textLight)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Advanced VIP courses",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )
                    )
                    Text(
                        text = "Full unlimited access to all VIP lessons.",
                        style = MaterialTheme.typography.bodySmall.copy(color = textLight)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Choose your membership",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textLight,
                letterSpacing = 1.sp
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            val subscriptionPacks = state.packsUiState.data.orEmpty()

            subscriptionPacks.forEach { pack ->
                val isSelected = state.selectedPackId == pack.id

                val isBestValuePack = pack.name.contains("PRO", ignoreCase = true) || pack.durationDays == 90

                val icon = if (isBestValuePack) Icons.Rounded.Star else Icons.Rounded.Diamond
                val iconTint = if (isBestValuePack) Color(0xFFD97706) else Color(0xFF2563EB)
                val iconBgColor = if (isBestValuePack) Color(0xFFFEF3C7) else Color(0xFFEFF6FF)
                val priceColor = if (isBestValuePack) Color(0xFFDC2626) else Color(0xFF0F172A)

                val animatedBorderWidth by animateDpAsState(
                    targetValue = if (isSelected) 2.dp else 1.dp,
                    label = "borderWidth"
                )

                val borderColor by animateColorAsState(
                    targetValue = when {
                        isSelected && isBestValuePack -> Color(0xFFD97706)
                        isSelected -> AppColors.BluePrimary
                        isBestValuePack -> Color(0xFFF59E0B).copy(alpha = 0.5f)
                        else -> Color(0xFFE2E8F0)
                    },
                    label = "borderColor"
                )

                val backgroundColor by animateColorAsState(
                    targetValue = when {
                        isSelected && isBestValuePack -> Color(0xFFFFFDF5)
                        isSelected -> AppColors.BluePrimary.copy(alpha = 0.04f)
                        else -> Color.White
                    },
                    label = "backgroundColor"
                )

                val cardShape = RoundedCornerShape(20.dp)

                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .clip(cardShape)
                            .background(backgroundColor)
                            .border(animatedBorderWidth, borderColor, cardShape)
                            .clickable { onEvent(SubscriptionEvent.OnSelectPack(pack.id)) }
                            .padding(vertical = 18.dp, horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val nameParts = pack.name.split("-")
                        val displayName = nameParts.firstOrNull()?.trim() ?: pack.name
                        val displayDuration = if (nameParts.size > 1) {
                            "/ ${nameParts.subList(1, nameParts.size).joinToString("-").trim()}"
                        } else {
                            "/ ${pack.durationDays} days"
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(iconBgColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = pack.name,
                                    tint = iconTint,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = displayName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A),
                                        fontSize = 13.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = displayDuration,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = textLight,
                                        fontSize = 14.sp
                                    )
                                )
                            }
                        }

                        val rawPriceText = formatPrice(pack.price)
                            .replace("đ", "")
                            .replace("VND", "")
                            .trim()

                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                                    append("đ")
                                }
                                append(rawPriceText)
                            },
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.W500,
                                color = priceColor,
                                fontSize = 15.sp
                            )
                        )
                    }

                    if (isBestValuePack) {
                        Box(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .background(
                                    color = Color(0xFFF59E0B),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "BEST VALUE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val isInitiatingPayment = state.paymentUiState.isLoading

        Button(
            onClick = { onEvent(SubscriptionEvent.OnBuyClick) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.BluePrimary,
                disabledContainerColor = AppColors.BluePrimary.copy(alpha = 0.4f)
            ),
            shape = RoundedCornerShape(16.dp),
            enabled = !isInitiatingPayment && state.selectedPackId.isNotEmpty()
        ) {
            if (isInitiatingPayment) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.5.dp
                )
            } else {
                Text(
                    text = "Activate pro benefits",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                )
            }
        }
    }
}

private fun formatPrice(price: Any): String {
    val number: Double? = when (price) {
        is Number -> price.toDouble()
        else -> {
            val cleaned = price.toString()
                .replace("đ", "")
                .replace("VND", "")
                .trim()
            cleaned.toDoubleOrNull()
        }
    }

    return if (number != null) {
        val symbols = java.text.DecimalFormatSymbols(java.util.Locale.US).apply {
            groupingSeparator = '.'
        }
        val formatter = java.text.DecimalFormat("#,###", symbols)
        formatter.format(number)
    } else {
        price.toString()
    }
}