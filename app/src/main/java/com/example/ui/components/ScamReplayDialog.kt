package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ScamReplayData
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CardSurfaceElevated
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ThreatRed
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScamReplayDialog(
    replayData: ScamReplayData,
    onDismiss: () -> Unit,
    onRetryTask: (() -> Unit)? = null
) {
    var currentStepIndex by remember { mutableStateOf(0) }
    var isAutoPlaying by remember { mutableStateOf(false) }

    LaunchedEffect(isAutoPlaying, currentStepIndex) {
        if (isAutoPlaying) {
            delay(2500)
            if (currentStepIndex < replayData.steps.size - 1) {
                currentStepIndex++
            } else {
                isAutoPlaying = false
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(16.dp)
                .testTag("scam_replay_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
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
                                .background(ThreatRed.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Replay,
                                contentDescription = "Replay",
                                tint = ThreatRed,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Explainable Scam Replay",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = replayData.scamType,
                                fontSize = 11.sp,
                                color = CyberCyan
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_scam_replay_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Stepper Indicators
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    replayData.steps.forEachIndexed { index, _ ->
                        val isActive = index == currentStepIndex
                        val isPassed = index < currentStepIndex
                        val barColor = when {
                            isActive -> ThreatRed
                            isPassed -> AmberWarning
                            else -> CardSurfaceElevated
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(CircleShape)
                                .background(barColor)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Step Content Card
                val currentStep = replayData.steps.getOrNull(currentStepIndex) ?: replayData.steps.first()

                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = { fadeIn() with fadeOut() },
                    label = "step_transition"
                ) { step ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(CardSurfaceElevated)
                            .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = ThreatRed.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, ThreatRed)
                            ) {
                                Text(
                                    text = "Step ${step.stepNumber} of ${replayData.steps.size}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ThreatRed,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                color = AmberWarning.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = "Trait Impact",
                                        tint = AmberWarning,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = step.biasTriggered,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AmberWarning
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.Top) {
                            val stepIcon = when (step.iconType) {
                                "URGENCY" -> Icons.Default.Timer
                                "AUTHORITY" -> Icons.Default.Gavel
                                "WARNING" -> Icons.Default.Warning
                                "PAYLOAD" -> Icons.Default.Key
                                else -> Icons.Default.Replay
                            }

                            Icon(
                                imageVector = stepIcon,
                                contentDescription = "Step Icon",
                                tint = ThreatRed,
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .size(24.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = step.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = step.description,
                                    fontSize = 13.sp,
                                    color = TextSecondary,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Key Takeaway
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = RoyalBlue.copy(alpha = 0.12f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RoyalBlue.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "💡 Takeaway: ${replayData.keyLesson}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CyberCyan,
                        modifier = Modifier.padding(12.dp),
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Navigation Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        IconButton(
                            onClick = {
                                if (currentStepIndex > 0) currentStepIndex--
                            },
                            enabled = currentStepIndex > 0,
                            modifier = Modifier.testTag("prev_step_replay_button")
                        ) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous")
                        }

                        IconButton(
                            onClick = {
                                isAutoPlaying = !isAutoPlaying
                            },
                            modifier = Modifier.testTag("autoplay_replay_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.FastForward,
                                contentDescription = "AutoPlay",
                                tint = if (isAutoPlaying) CyberCyan else TextSecondary
                            )
                        }

                        IconButton(
                            onClick = {
                                if (currentStepIndex < replayData.steps.size - 1) currentStepIndex++
                            },
                            enabled = currentStepIndex < replayData.steps.size - 1,
                            modifier = Modifier.testTag("next_step_replay_button")
                        ) {
                            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next")
                        }
                    }

                    if (onRetryTask != null) {
                        Button(
                            onClick = {
                                onDismiss()
                                onRetryTask()
                            },
                            modifier = Modifier.testTag("retry_task_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = MintEmerald)
                        ) {
                            Text("RETRY SIMULATION", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Black)
                        }
                    } else {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("dismiss_replay_button")
                        ) {
                            Text("GOT IT", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
