package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LearningModule
import com.example.data.model.ThreatAlert
import com.example.data.model.UserProfile
import com.example.ui.components.FviGaugeCard
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
fun DashboardScreen(
    userProfile: UserProfile?,
    alerts: List<ThreatAlert>,
    recommendedModules: List<LearningModule>,
    onOpenAlerts: () -> Unit,
    onOpenScanner: () -> Unit,
    onNavigateSimulations: (categoryIndex: Int) -> Unit,
    onNavigateLearning: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val profile = userProfile ?: UserProfile()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117))
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Active Threat Alert Banner
        alerts.firstOrNull()?.let { topAlert ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = ThreatRed.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.dp, ThreatRed.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenAlerts() }
                    .testTag("top_threat_banner")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(ThreatRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Alert",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = ThreatRed
                            ) {
                                Text(
                                    text = "URGENT THREAT RADAR",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = topAlert.dateText, fontSize = 10.sp, color = TextMuted)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = topAlert.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            maxLines = 1
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open",
                        tint = TextMuted
                    )
                }
            }
        }

        // FVI Gauge Card
        FviGaugeCard(userProfile = profile)

        // Scam Genome Fingerprint (Vulnerability & Defense Profile)
        com.example.ui.components.ScamGenomeCard(userProfile = profile)

        // Quick AI Threat Scanner Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOpenScanner() }
                .testTag("quick_scanner_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(CyberCyan.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Scanner",
                        tint = CyberCyan,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "AI Threat & Scam Scanner",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Paste suspicious texts, emails, or links for instant Gemini safety analysis",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }

                Button(
                    onClick = onOpenScanner,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    shape = CircleShape,
                    modifier = Modifier.testTag("open_scanner_action_button")
                ) {
                    Text("SCAN", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }

        // Interactive Simulation Hub Launchers
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "INTERACTIVE ATTACK SIMULATORS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SimulatorLauncherBox(
                    title = "Phishing Email",
                    subtitle = "Spot Red Flags",
                    icon = Icons.Default.AlternateEmail,
                    accentColor = RoyalBlue,
                    onClick = { onNavigateSimulations(0) },
                    modifier = Modifier.weight(1f)
                )

                SimulatorLauncherBox(
                    title = "AI Voice Clone",
                    subtitle = "Deepfake Challenge",
                    icon = Icons.Default.RecordVoiceOver,
                    accentColor = ThreatRed,
                    onClick = { onNavigateSimulations(1) },
                    modifier = Modifier.weight(1f)
                )

                SimulatorLauncherBox(
                    title = "Live Red Team",
                    subtitle = "SMS/WhatsApp Scam",
                    icon = Icons.Default.Forum,
                    accentColor = MintEmerald,
                    onClick = { onNavigateSimulations(2) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Personalized Recommended Modules Preview
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RECOMMENDED FOR YOUR PROFILE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "View All",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberCyan,
                    modifier = Modifier.clickable { onNavigateLearning() }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            recommendedModules.take(2).forEach { module ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onNavigateLearning() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MintEmerald.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = MintEmerald,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = RoyalBlue.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = module.category,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyberCyan,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${module.estMinutes} mins",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = module.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = MintEmerald.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "+${module.pointsReward} Pts",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MintEmerald,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SimulatorLauncherBox(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = CardSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
        modifier = modifier
            .clickable { onClick() }
            .testTag("simulator_launcher_$title")
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = TextSecondary
            )
        }
    }
}
