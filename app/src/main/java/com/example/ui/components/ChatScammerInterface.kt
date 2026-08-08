package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.model.ScammerChatMessage
import com.example.data.model.ScammerChatScenario
import com.example.data.model.ScammerChoice
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CardSurfaceElevated
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ThreatRed

@Composable
fun ChatScammerInterface(
    scenario: ScammerChatScenario,
    onChoiceMade: (choice: ScammerChoice) -> Unit,
    onViewReplayRequested: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val chatMessages = remember { mutableStateListOf<ScammerChatMessage>().apply { addAll(scenario.initialMessages) } }
    var selectedChoice by remember { mutableStateOf<ScammerChoice?>(null) }
    var isFinished by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("chat_scammer_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header Bar
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
                            .background(ThreatRed.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forum,
                            contentDescription = "Chat",
                            tint = ThreatRed,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = scenario.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Channel: ${scenario.channelType} • Persona: ${scenario.scammerPersona}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = ThreatRed.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ThreatRed.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Threat",
                            tint = ThreatRed,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "LIVE RED TEAM",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ThreatRed
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chat Messages Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0D1117))
                    .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                chatMessages.forEach { msg ->
                    val isScammer = msg.sender == "Scammer"
                    val bubbleBg = when {
                        msg.isWarning -> ThreatRed.copy(alpha = 0.2f)
                        isScammer -> CardSurfaceElevated
                        else -> RoyalBlue.copy(alpha = 0.25f)
                    }
                    val align = if (isScammer) Alignment.Start else Alignment.End

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = align
                    ) {
                        Text(
                            text = if (isScammer) scenario.scammerPersona else "You",
                            fontSize = 10.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(bottom = 2.dp, start = 4.dp, end = 4.dp)
                        )
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = bubbleBg,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (msg.isWarning) ThreatRed else CardBorder
                            )
                        ) {
                            Text(
                                text = msg.text,
                                fontSize = 12.sp,
                                color = TextPrimary,
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Decision Prompt / Response Choice Buttons
            if (!isFinished) {
                Text(
                    text = "How do you respond to this high-pressure scam attack?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                scenario.choices.forEachIndexed { index, choice ->
                    OutlinedButton(
                        onClick = {
                            selectedChoice = choice
                            isFinished = true
                            chatMessages.add(
                                ScammerChatMessage(
                                    id = System.currentTimeMillis().toString(),
                                    sender = "User",
                                    text = choice.text,
                                    timestamp = "Just now"
                                )
                            )
                            onChoiceMade(choice)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("scammer_choice_button_$index"),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = choice.text,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )
                        }
                    }
                }
            } else {
                // Feedback Card
                selectedChoice?.let { choice ->
                    val isSuccess = choice.isSafeChoice
                    val color = if (isSuccess) MintEmerald else ThreatRed

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = color.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, color),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isSuccess) Icons.Default.Security else Icons.Default.Block,
                                    contentDescription = "Status",
                                    tint = color,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isSuccess) "ATTACK DEFENDED! (+${choice.scoreDelta} FVI Pts)" else "TRAP EXPOSED (${choice.scoreDelta} FVI Pts)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = color
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = choice.feedback,
                                fontSize = 12.sp,
                                color = TextPrimary,
                                lineHeight = 18.sp
                            )

                            if (!isSuccess && onViewReplayRequested != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedButton(
                                    onClick = onViewReplayRequested,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("view_scam_replay_from_chat_button"),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, ThreatRed),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ThreatRed)
                                ) {
                                    Icon(imageVector = Icons.Default.Security, contentDescription = "Replay", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("WATCH EXPLAINABLE SCAM REPLAY", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
