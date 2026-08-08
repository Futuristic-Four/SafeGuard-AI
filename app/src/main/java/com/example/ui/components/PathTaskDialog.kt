package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.LearningModule
import com.example.data.model.PathSubTask
import com.example.data.model.SubTaskType
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CardSurfaceElevated
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ThreatRed
import com.example.ui.theme.WarningGold

@Composable
fun PathTaskDialog(
    module: LearningModule,
    subTask: PathSubTask,
    stepNumber: Int,
    totalSteps: Int,
    onDismiss: () -> Unit,
    onCompleteSubTask: (taskId: String, points: Int) -> Unit
) {
    var selectedOptionIndex by remember { mutableIntStateOf(-1) }
    var hasAnswered by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    val taskIcon = when (subTask.taskType) {
        SubTaskType.LESSON -> Icons.Default.AutoAwesome
        SubTaskType.SCENARIO -> Icons.Default.Psychology
        SubTaskType.QUIZ -> Icons.Default.Quiz
        SubTaskType.BOSS_CHALLENGE -> Icons.Default.MilitaryTech
    }

    val taskColor = when (subTask.taskType) {
        SubTaskType.LESSON -> CyberCyan
        SubTaskType.SCENARIO -> WarningGold
        SubTaskType.QUIZ -> MintEmerald
        SubTaskType.BOSS_CHALLENGE -> ThreatRed
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 24.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
                .testTag("path_task_dialog"),
            color = CardSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Top Navigation Header
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
                                .background(taskColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = taskIcon,
                                contentDescription = null,
                                tint = taskColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "PATH STEP $stepNumber OF $totalSteps",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = taskColor,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = module.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_task_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Task Title
                Text(
                    text = subTask.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Reward XP Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(WarningGold.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = WarningGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "+${subTask.pointsReward} XP & FVI Points",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarningGold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Content / Story Body
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardSurfaceElevated)
                        .border(1.dp, taskColor.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = subTask.description,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = taskColor
                        )
                        if (subTask.storyBody.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = subTask.storyBody,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = TextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // If Question Options Exist (Quiz / Scenario / Boss)
                if (subTask.options.isNotEmpty()) {
                    Text(
                        text = subTask.question.ifBlank { "What is the correct security response?" },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    subTask.options.forEachIndexed { index, option ->
                        val isSelected = selectedOptionIndex == index
                        val optionBorderColor = when {
                            hasAnswered && index == subTask.correctOptionIndex -> MintEmerald
                            hasAnswered && isSelected && !isCorrect -> ThreatRed
                            isSelected -> CyberCyan
                            else -> CardBorder
                        }

                        val optionBgColor = when {
                            hasAnswered && index == subTask.correctOptionIndex -> MintEmerald.copy(alpha = 0.15f)
                            hasAnswered && isSelected && !isCorrect -> ThreatRed.copy(alpha = 0.15f)
                            isSelected -> CyberCyan.copy(alpha = 0.12f)
                            else -> CardSurfaceElevated
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(optionBgColor)
                                .border(1.dp, optionBorderColor, RoundedCornerShape(12.dp))
                                .clickable(enabled = !hasAnswered) {
                                    selectedOptionIndex = index
                                }
                                .padding(14.dp)
                                .testTag("task_option_$index")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) CyberCyan else CardBorder),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = ('A' + index).toString(),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) ObsidianBackground else TextSecondary
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = option,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextPrimary,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Answer Verification Button or Result Feedback
                    if (!hasAnswered) {
                        Button(
                            onClick = {
                                if (selectedOptionIndex != -1) {
                                    hasAnswered = true
                                    isCorrect = selectedOptionIndex == subTask.correctOptionIndex
                                }
                            },
                            enabled = selectedOptionIndex != -1,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("verify_answer_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("CHECK ANSWER", fontWeight = FontWeight.Bold, color = ObsidianBackground)
                        }
                    } else {
                        // Answer Feedback Box
                        AnimatedVisibility(visible = true, enter = fadeIn() + slideInVertically()) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(if (isCorrect) MintEmerald.copy(alpha = 0.15f) else ThreatRed.copy(alpha = 0.15f))
                                        .border(1.dp, if (isCorrect) MintEmerald else ThreatRed, RoundedCornerShape(14.dp))
                                        .padding(14.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.Top) {
                                        Icon(
                                            imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Warning,
                                            contentDescription = null,
                                            tint = if (isCorrect) MintEmerald else ThreatRed,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = if (isCorrect) "EXCELLENT DEFENSE! 🎉" else "SECURITY THREAT MISSED ⚠️",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isCorrect) MintEmerald else ThreatRed
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = subTask.keyTakeaway,
                                                fontSize = 12.sp,
                                                lineHeight = 17.sp,
                                                color = TextPrimary
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        val earnedPoints = if (isCorrect) subTask.pointsReward else (subTask.pointsReward / 2).coerceAtLeast(5)
                                        onCompleteSubTask(subTask.id, earnedPoints)
                                        onDismiss()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("complete_step_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = if (isCorrect) MintEmerald else taskColor),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = if (isCorrect) "CONTINUE PATH (+${subTask.pointsReward} XP)" else "RETRY / CONTINUE (+${subTask.pointsReward / 2} XP)",
                                        fontWeight = FontWeight.Bold,
                                        color = ObsidianBackground
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // LESSON Task (No questions required, read & complete)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MintEmerald.copy(alpha = 0.1f))
                            .border(1.dp, MintEmerald.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MintEmerald,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Key Rule: ${subTask.keyTakeaway}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            onCompleteSubTask(subTask.id, subTask.pointsReward)
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("complete_lesson_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "MARK LESSON COMPLETE (+${subTask.pointsReward} XP)",
                            fontWeight = FontWeight.Bold,
                            color = ObsidianBackground
                        )
                    }
                }
            }
        }
    }
}
