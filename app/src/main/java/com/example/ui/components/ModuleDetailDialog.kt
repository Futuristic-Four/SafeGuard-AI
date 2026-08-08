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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.DemographicGroup
import com.example.data.model.LearningModule
import com.example.data.model.UserProfile
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CardSurfaceElevated
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ThreatRed

@Composable
fun ModuleDetailDialog(
    module: LearningModule,
    userProfile: UserProfile,
    onDismiss: () -> Unit,
    onCompleteModule: (points: Int) -> Unit
) {
    val group = userProfile.demographicGroup
    val scrollState = rememberScrollState()

    var selectedOptionIndex by remember { mutableIntStateOf(-1) }
    var isSubmitted by remember { mutableStateOf(false) }
    var showRewardAnim by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 24.dp)
                .testTag("dialog_module_detail"),
            shape = RoundedCornerShape(
                when (group) {
                    DemographicGroup.KIDS_5_10 -> 28.dp
                    DemographicGroup.TEENS_11_15 -> 14.dp
                    else -> 20.dp
                }
            ),
            color = when (group) {
                DemographicGroup.KIDS_5_10 -> Color(0xFF1E2638)
                DemographicGroup.TEENS_11_15 -> Color(0xFF1A221E) // Minecraft blocky dark green tone
                DemographicGroup.YOUNG_ADULTS_16_22 -> Color(0xFF181528) // Gen Z Violet dark
                DemographicGroup.PROFESSIONALS_23_40 -> CardSurface
                DemographicGroup.MID_ADULTS_41_60 -> Color(0xFF13171F)
                DemographicGroup.SENIORS_61_PLUS -> Color(0xFF0F1218)
            },
            border = androidx.compose.foundation.BorderStroke(
                width = if (group == DemographicGroup.TEENS_11_15) 2.dp else 1.dp,
                color = when (group) {
                    DemographicGroup.KIDS_5_10 -> Color(0xFFFFCC00)
                    DemographicGroup.TEENS_11_15 -> Color(0xFF55FF55) // Minecraft neon green border
                    DemographicGroup.YOUNG_ADULTS_16_22 -> Color(0xFFC084FC)
                    DemographicGroup.PROFESSIONALS_23_40 -> CardBorder
                    DemographicGroup.MID_ADULTS_41_60 -> CardBorder
                    DemographicGroup.SENIORS_61_PLUS -> CyberCyan
                }
            )
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(20.dp)
            ) {
                // Top Header Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Style Header Badge based on demographic
                    Surface(
                        color = when (group) {
                            DemographicGroup.KIDS_5_10 -> Color(0xFFFF9900)
                            DemographicGroup.TEENS_11_15 -> Color(0xFF55FF55)
                            DemographicGroup.YOUNG_ADULTS_16_22 -> Color(0xFFA855F7)
                            else -> CyberCyan
                        }.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = when (group) {
                                DemographicGroup.KIDS_5_10 -> "🐻 KIDS SHIELD MODULE"
                                DemographicGroup.TEENS_11_15 -> "🎮 GAMIFIED XP QUEST"
                                DemographicGroup.YOUNG_ADULTS_16_22 -> "⚡ GEN-Z DEFENSE"
                                DemographicGroup.PROFESSIONALS_23_40 -> "🏛️ CORPORATE / FINTECH BRIEFING"
                                DemographicGroup.MID_ADULTS_41_60 -> "🛡️ CLEAR RISK GUIDE"
                                DemographicGroup.SENIORS_61_PLUS -> "🔍 HIGH CONTRAST EASY GUIDE"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (group) {
                                DemographicGroup.KIDS_5_10 -> Color(0xFFFFCC00)
                                DemographicGroup.TEENS_11_15 -> Color(0xFF55FF55)
                                DemographicGroup.YOUNG_ADULTS_16_22 -> Color(0xFFE9D5FF)
                                else -> CyberCyan
                            },
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("btn_close_module_dialog")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title Presentation
                Text(
                    text = module.title,
                    fontSize = when (group) {
                        DemographicGroup.KIDS_5_10 -> 22.sp
                        DemographicGroup.SENIORS_61_PLUS -> 24.sp
                        else -> 20.sp
                    },
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Demographically Tailored Mascot / Banner Card
                when (group) {
                    DemographicGroup.KIDS_5_10 -> {
                        Surface(
                            color = Color(0xFF2C3852),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFFCC00)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(14.dp)
                            ) {
                                Text("🐻", fontSize = 42.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Shieldy Bear Says:", fontWeight = FontWeight.Bold, color = Color(0xFFFFCC00), fontSize = 16.sp)
                                    Text("Let's spot secret traps and earn super star shields together!", fontSize = 13.sp, color = TextPrimary)
                                }
                            }
                        }
                    }
                    DemographicGroup.TEENS_11_15 -> {
                        Surface(
                            color = Color(0xFF243028),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF55FF55)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(14.dp)
                            ) {
                                Icon(Icons.Default.MilitaryTech, contentDescription = null, tint = Color(0xFF55FF55), modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("QUEST REWARD: +500 XP XP-BOOST", fontWeight = FontWeight.Bold, color = Color(0xFF55FF55), fontSize = 14.sp)
                                    Text("Complete the scenario challenge to unlock the Fraudmaster Badge!", fontSize = 12.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                    DemographicGroup.SENIORS_61_PLUS -> {
                        Surface(
                            color = Color(0xFF1E2838),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(2.dp, CyberCyan),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Step-by-step guidance. Read carefully below.",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                    else -> {}
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Story / Scenario Content Body
                Surface(
                    color = CardSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val storyHeadline = if (module.storyHeadline.isNotBlank()) module.storyHeadline else module.title
                        val storyBody = if (module.storyBody.isNotBlank()) module.storyBody else module.description

                        Text(
                            text = storyHeadline,
                            fontSize = when (group) {
                                DemographicGroup.SENIORS_61_PLUS -> 18.sp
                                else -> 16.sp
                            },
                            fontWeight = FontWeight.Bold,
                            color = when (group) {
                                DemographicGroup.KIDS_5_10 -> Color(0xFFFFCC00)
                                DemographicGroup.TEENS_11_15 -> Color(0xFF55FF55)
                                DemographicGroup.YOUNG_ADULTS_16_22 -> Color(0xFFC084FC)
                                else -> CyberCyan
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = storyBody,
                            fontSize = when (group) {
                                DemographicGroup.SENIORS_61_PLUS -> 17.sp
                                DemographicGroup.MID_ADULTS_41_60 -> 16.sp
                                else -> 14.sp
                            },
                            lineHeight = when (group) {
                                DemographicGroup.SENIORS_61_PLUS -> 24.sp
                                else -> 20.sp
                            },
                            color = TextPrimary
                        )

                        if (module.keyTakeaway.isNotBlank()) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MintEmerald.copy(alpha = 0.15f))
                                    .padding(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = MintEmerald,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("KEY DEFENSE RULE:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MintEmerald)
                                    Text(
                                        text = module.keyTakeaway,
                                        fontSize = when (group) {
                                            DemographicGroup.SENIORS_61_PLUS -> 15.sp
                                            else -> 13.sp
                                        },
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Interactive Quiz Section
                val question = if (module.quizQuestion.isNotBlank()) module.quizQuestion else "What is the safest action when encountering this scenario?"
                val options = listOf(
                    if (module.quizOption1.isNotBlank()) module.quizOption1 else "Report and verify independently through official channels.",
                    if (module.quizOption2.isNotBlank()) module.quizOption2 else "Follow instructions in message immediately.",
                    if (module.quizOption3.isNotBlank()) module.quizOption3 else "Forward message to friends."
                )

                Text(
                    text = "Quick Interactive Check 🎯",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = question,
                    fontSize = when (group) {
                        DemographicGroup.SENIORS_61_PLUS -> 17.sp
                        else -> 14.sp
                    },
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                options.forEachIndexed { index, optionText ->
                    val isSelected = selectedOptionIndex == index
                    val isCorrect = index == module.correctOptionIndex

                    Surface(
                        color = when {
                            isSubmitted && isSelected && isCorrect -> MintEmerald.copy(alpha = 0.25f)
                            isSubmitted && isSelected && !isCorrect -> ThreatRed.copy(alpha = 0.25f)
                            isSelected -> CyberCyan.copy(alpha = 0.2f)
                            else -> CardSurface
                        },
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = when {
                                isSubmitted && isSelected && isCorrect -> MintEmerald
                                isSubmitted && isSelected && !isCorrect -> ThreatRed
                                isSelected -> CyberCyan
                                else -> CardBorder
                            }
                        ),
                        shape = RoundedCornerShape(
                            when (group) {
                                DemographicGroup.KIDS_5_10 -> 16.dp
                                else -> 10.dp
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable(enabled = !isSubmitted) { selectedOptionIndex = index }
                            .testTag("quiz_option_$index")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) CyberCyan else CardBorder)
                            ) {
                                Text(
                                    text = ('A' + index).toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (isSelected) Color.Black else TextPrimary
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = optionText,
                                fontSize = when (group) {
                                    DemographicGroup.SENIORS_61_PLUS -> 16.sp
                                    else -> 13.sp
                                },
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Submit / Complete Button
                if (!isSubmitted) {
                    Button(
                        onClick = {
                            if (selectedOptionIndex != -1) {
                                isSubmitted = true
                                if (selectedOptionIndex == module.correctOptionIndex) {
                                    showRewardAnim = true
                                    onCompleteModule(module.pointsReward)
                                }
                            }
                        },
                        enabled = selectedOptionIndex != -1,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (group) {
                                DemographicGroup.KIDS_5_10 -> Color(0xFFFF9900)
                                DemographicGroup.TEENS_11_15 -> Color(0xFF55FF55)
                                else -> CyberCyan
                            }
                        ),
                        shape = RoundedCornerShape(
                            when (group) {
                                DemographicGroup.KIDS_5_10 -> 24.dp
                                DemographicGroup.SENIORS_61_PLUS -> 14.dp
                                else -> 12.dp
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                when (group) {
                                    DemographicGroup.KIDS_5_10 -> 60.dp
                                    DemographicGroup.SENIORS_61_PLUS -> 58.dp
                                    else -> 50.dp
                                }
                            )
                            .testTag("btn_submit_quiz")
                    ) {
                        Text(
                            text = when (group) {
                                DemographicGroup.KIDS_5_10 -> "⭐ SUBMIT ANSWER! ⭐"
                                DemographicGroup.TEENS_11_15 -> "CLAIM XP REWARD"
                                else -> "Check Answer & Earn +${module.pointsReward} FVI Points"
                            },
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = when (group) {
                                DemographicGroup.SENIORS_61_PLUS -> 17.sp
                                else -> 15.sp
                            }
                        )
                    }
                } else {
                    // Feedback Result Card
                    val isCorrect = selectedOptionIndex == module.correctOptionIndex
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCorrect) MintEmerald.copy(alpha = 0.2f) else ThreatRed.copy(alpha = 0.2f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isCorrect) MintEmerald else ThreatRed
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(
                                imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (isCorrect) MintEmerald else ThreatRed,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isCorrect) "EXCELLENT! +${module.pointsReward} FVI Points Earned! 🎉" else "Not quite! Review the key defense rule above.",
                                fontWeight = FontWeight.Bold,
                                color = if (isCorrect) MintEmerald else ThreatRed,
                                textAlign = TextAlign.Center,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = onDismiss,
                                colors = ButtonDefaults.buttonColors(containerColor = CardSurfaceElevated),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_done_module_dialog")
                            ) {
                                Text("Continue Learning", color = TextPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
