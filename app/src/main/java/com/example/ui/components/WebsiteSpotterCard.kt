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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Replay
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
import com.example.data.model.WebsiteHotspot
import com.example.data.model.WebsiteScenario
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
fun WebsiteSpotterCard(
    scenario: WebsiteScenario,
    onAssessmentSubmitted: (isSuccess: Boolean, spottedCount: Int, totalRedFlags: Int) -> Unit,
    onViewReplayRequested: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedHotspotIds = remember { mutableStateListOf<String>() }
    var isSubmitted by remember { mutableStateOf(false) }

    val redFlagHotspots = scenario.hotspots.filter { it.isRedFlag }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("website_spotter_card"),
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
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CyberCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Website",
                            tint = CyberCyan,
                            modifier = Modifier.size(20.dp)
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
                            text = scenario.siteName,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = if (scenario.isLegitimate) MintEmerald.copy(alpha = 0.15f) else ThreatRed.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (scenario.isLegitimate) MintEmerald else ThreatRed
                    )
                ) {
                    Text(
                        text = if (scenario.isLegitimate) "AUTHENTIC SITE" else "${redFlagHotspots.size} RED FLAGS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (scenario.isLegitimate) MintEmerald else ThreatRed,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Browser Frame Mockup
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0B0E14))
                    .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
            ) {
                // Browser URL Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF161B22))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(ThreatRed))
                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(AmberWarning))
                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(MintEmerald))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Address Bar
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0D1117))
                            .border(1.dp, CardBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (scenario.displayUrl.startsWith("https")) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = "SSL Status",
                            tint = if (scenario.displayUrl.startsWith("https")) MintEmerald else ThreatRed,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = scenario.displayUrl,
                            fontSize = 11.sp,
                            color = if (scenario.displayUrl.contains("paypai") || scenario.displayUrl.contains("http:")) ThreatRed else TextPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Rendered Web Page Content Canvas
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    // Website Screenshot Capture Preview Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .testTag("website_screenshot_preview_card"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF30363D))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = RoyalBlue
                                    ) {
                                        Text(
                                            text = if (scenario.siteName.contains("PayP")) "P" else "IRS",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = scenario.siteName.uppercase(),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF21262D)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = "Screenshot",
                                            tint = CyberCyan,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "SCREENSHOT PREVIEW",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CyberCyan
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Fake Rendered Page Content Mockup
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF0D1117))
                                    .border(1.dp, Color(0xFF21262D), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = "⚠️ URGENT: ACCOUNT HOLD NOTICE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ThreatRed
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = scenario.headerSubtitle,
                                    fontSize = 11.sp,
                                    color = TextPrimary
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Simulated Form Fields
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF161B22),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "SSN / Full Identity Number: [ Input Field ]",
                                        fontSize = 9.sp,
                                        color = TextMuted,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF161B22),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Card PIN / Account Password: [ Input Field ]",
                                        fontSize = 9.sp,
                                        color = TextMuted,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = RoyalBlue,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "SUBMIT IDENTITY & RESTORE ACCOUNT",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(vertical = 6.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    Text(
                        text = "Tap on suspicious page regions below to flag threats:",
                        fontSize = 11.sp,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Hotspot Items
                    scenario.hotspots.forEach { hotspot ->
                        val isSelected = selectedHotspotIds.contains(hotspot.id)
                        val itemBorder = if (isSelected) ThreatRed else CardBorder
                        val itemBg = if (isSelected) ThreatRed.copy(alpha = 0.15f) else CardSurfaceElevated

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = itemBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, itemBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    if (!isSubmitted) {
                                        if (isSelected) selectedHotspotIds.remove(hotspot.id)
                                        else selectedHotspotIds.add(hotspot.id)
                                    }
                                }
                                .testTag("website_hotspot_${hotspot.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isSelected) Icons.Default.Flag else Icons.Default.Info,
                                    contentDescription = "Hotspot",
                                    tint = if (isSelected) ThreatRed else TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = hotspot.name,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) ThreatRed else TextPrimary
                                    )
                                    if (isSubmitted || isSelected) {
                                        Text(
                                            text = hotspot.explanation,
                                            fontSize = 11.sp,
                                            color = TextSecondary,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }
                                }
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
                        val correctSpotted = selectedHotspotIds.count { id ->
                            scenario.hotspots.find { it.id == id }?.isRedFlag == true
                        }
                        val totalFlags = redFlagHotspots.size
                        val isSuccess = if (totalFlags == 0) selectedHotspotIds.isEmpty() else (correctSpotted >= totalFlags)
                        onAssessmentSubmitted(isSuccess, correctSpotted, totalFlags)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_website_assessment_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
                ) {
                    Text("SUBMIT WEBSITE INSPECTION", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black)
                }
            } else {
                val correctSpotted = selectedHotspotIds.count { id ->
                    scenario.hotspots.find { it.id == id }?.isRedFlag == true
                }
                val isSuccess = if (redFlagHotspots.isEmpty()) selectedHotspotIds.isEmpty() else (correctSpotted >= redFlagHotspots.size)

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSuccess) MintEmerald.copy(alpha = 0.15f) else ThreatRed.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSuccess) MintEmerald else ThreatRed),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isSuccess) Icons.Default.Check else Icons.Default.Warning,
                                contentDescription = "Result",
                                tint = if (isSuccess) MintEmerald else ThreatRed,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isSuccess) "PASSED INSPECTION! (+25 FVI Pts)" else "INSPECTION FAILED (-15 FVI Pts)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSuccess) MintEmerald else ThreatRed
                            )
                        }

                        if (!isSuccess) {
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedButton(
                                onClick = onViewReplayRequested,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("view_scam_replay_from_website_button"),
                                border = androidx.compose.foundation.BorderStroke(1.dp, ThreatRed),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ThreatRed)
                            ) {
                                Icon(imageVector = Icons.Default.Replay, contentDescription = "Replay", modifier = Modifier.size(16.dp))
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
