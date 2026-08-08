package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CardSurfaceElevated
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ThreatRed

@Composable
fun FviGaugeCard(
    userProfile: UserProfile,
    modifier: Modifier = Modifier
) {
    val score = userProfile.fviScore.coerceIn(300, 850)
    // Scale 300..850 to 0.0..1.0
    val targetRatio = ((score - 300) / 550f).coerceIn(0f, 1f)

    val animatedRatio = remember { Animatable(0f) }
    LaunchedEffect(score) {
        animatedRatio.animateTo(
            targetValue = targetRatio,
            animationSpec = tween(durationMillis = 1200)
        )
    }

    val (gaugeColor, statusLabel) = when {
        score < 550 -> ThreatRed to "HIGH RISK"
        score in 550..690 -> AmberWarning to "MODERATE PREPAREDNESS"
        else -> MintEmerald to "IMMUNIZED"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("fvi_gauge_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Title with Shield Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(gaugeColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "FVI Shield",
                            tint = gaugeColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "FINANCIAL VULNERABILITY INDEX",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Real-Time Security Credit Score",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                }

                // Trend Pill
                Surface(
                    shape = CircleShape,
                    color = MintEmerald.copy(alpha = 0.12f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MintEmerald.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "Trend",
                            tint = MintEmerald,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "+45 pts this mo",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MintEmerald
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Semi-Circular Gauge Drawing
            Box(
                modifier = Modifier.size(210.dp, 125.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Canvas(modifier = Modifier.size(200.dp, 200.dp)) {
                    val strokeWidth = 18.dp.toPx()
                    val arcRadius = size.width / 2 - strokeWidth / 2

                    // Background Track Arc (180 degrees from 180° to 360°)
                    drawArc(
                        color = Color(0xFF21262D),
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Active Sweep Arc
                    val activeSweep = animatedRatio.value * 180f
                    val gradient = Brush.horizontalGradient(
                        colors = listOf(ThreatRed, AmberWarning, MintEmerald, CyberCyan)
                    )

                    drawArc(
                        brush = gradient,
                        startAngle = 180f,
                        sweepAngle = activeSweep,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                // Score text display inside gauge
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Text(
                        text = "$score",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = gaugeColor.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, gaugeColor.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = statusLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = gaugeColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = "Score Range: 300 - 850",
                        fontSize = 10.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Defense Breakdown Factors
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardSurfaceElevated)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Security Resilience Factors",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = TextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                }

                ResilienceFactorRow(
                    label = "AI Voice Clone Defense",
                    percent = userProfile.voiceCloneDefensePercent,
                    color = CyberCyan
                )
                ResilienceFactorRow(
                    label = "Phishing & Email Spotting",
                    percent = userProfile.phishingSpottingPercent,
                    color = MintEmerald
                )
                ResilienceFactorRow(
                    label = "Social Engineering Trap Resistance",
                    percent = userProfile.socialEngResiliencePercent,
                    color = AmberWarning
                )
            }
        }
    }
}

@Composable
private fun ResilienceFactorRow(
    label: String,
    percent: Int,
    color: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
            Text(text = "$percent%", fontSize = 12.sp, color = color, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { percent / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape),
            color = color,
            trackColor = CardSurface
        )
    }
}
