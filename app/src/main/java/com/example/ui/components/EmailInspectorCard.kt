package com.example.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.example.data.model.EmailScenario
import com.example.data.model.RedFlag
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
fun EmailInspectorCard(
    scenario: EmailScenario,
    onAssessmentSubmitted: (flagsSpottedCount: Int, totalFlags: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedFlagIds = remember { mutableStateListOf<String>() }
    var isSubmitted by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("email_inspector_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Top Bar
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
                            .background(RoyalBlue.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AlternateEmail,
                            contentDescription = "Email",
                            tint = RoyalBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Phishing & Email Red Flag Inspector",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = AmberWarning.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AmberWarning.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "${scenario.redFlags.size} Red Flags",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberWarning,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Simulated Email Client Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0D1117))
                    .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "From: ", fontSize = 12.sp, color = TextMuted)
                    Text(
                        text = "${scenario.senderName} <${scenario.senderEmail}>",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Date: ", fontSize = 12.sp, color = TextMuted)
                    Text(text = scenario.date, fontSize = 12.sp, color = TextSecondary)
                }

                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider(color = CardBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Subject: ${scenario.subject}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = ThreatRed
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = scenario.body,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interactive Red Flags Selection Prompt
            Text(
                text = "Tap suspicious elements below to highlight Red Flags:",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            scenario.redFlags.forEach { flag ->
                val isSelected = selectedFlagIds.contains(flag.id)
                val cardBorderColor = if (isSelected) ThreatRed else CardBorder
                val cardBg = if (isSelected) ThreatRed.copy(alpha = 0.12f) else CardSurfaceElevated

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = cardBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            if (!isSubmitted) {
                                if (isSelected) selectedFlagIds.remove(flag.id) else selectedFlagIds.add(
                                    flag.id
                                )
                            }
                        }
                        .testTag("red_flag_item_${flag.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) ThreatRed else CardBorder),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isSelected) Icons.Default.Flag else Icons.Default.Info,
                                contentDescription = "Flag",
                                tint = if (isSelected) Color.White else TextMuted,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "\"${flag.snippetText}\"",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) ThreatRed else TextPrimary
                            )
                            if (isSubmitted || isSelected) {
                                Text(
                                    text = flag.explanation,
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!isSubmitted) {
                Button(
                    onClick = {
                        isSubmitted = true
                        onAssessmentSubmitted(selectedFlagIds.size, scenario.redFlags.size)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_email_assessment_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue)
                ) {
                    Text("SUBMIT PHISHING ANALYSIS", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            } else {
                val scoreGained = selectedFlagIds.size * 10
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MintEmerald.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MintEmerald),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Passed",
                            tint = MintEmerald,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Analysis Complete! (+$scoreGained FVI Pts)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MintEmerald
                            )
                            Text(
                                text = "Spotted ${selectedFlagIds.size} of ${scenario.redFlags.size} critical red flags.",
                                fontSize = 11.sp,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
