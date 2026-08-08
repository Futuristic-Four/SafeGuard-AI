package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.DemographicGroup
import com.example.data.model.FviRecord
import com.example.data.model.TechLiteracy
import com.example.data.model.UserProfile
import com.example.ui.components.DemographicSelectorCard
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileAnalyticsScreen(
    userProfile: UserProfile?,
    fviHistory: List<FviRecord>,
    onProfileChanged: (group: DemographicGroup, literacy: TechLiteracy) -> Unit,
    onOpenEditProfile: () -> Unit,
    onLogout: () -> Unit = {},
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
        // User Profile Summary Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("user_profile_summary_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(RoyalBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = profile.userName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${profile.occupation} • ${profile.demographicGroup.displayName}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = CircleShape,
                            color = MintEmerald.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "FVI Score: ${profile.fviScore} / 850",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MintEmerald,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                            )
                        }
                    }

                    IconButtonWithTag(
                        onClick = onOpenEditProfile,
                        tag = "btn_edit_profile"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = CardBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // Profile Details Grid
                Text(text = "SIGN IN & PERSONALIZATION DETAILS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberCyan, letterSpacing = 0.8.sp)
                Spacer(modifier = Modifier.height(10.dp))

                DetailItem(
                    icon = if (profile.loginType == "MOBILE") Icons.Default.Person else Icons.Default.Person,
                    label = "Sign In ID (${profile.loginType})",
                    value = profile.userIdentifier
                )
                DetailItem(icon = Icons.Default.Psychology, label = "Scam History", value = profile.scamExperience)
                DetailItem(icon = Icons.Default.AccountBalance, label = "Banking Habits", value = profile.bankingHabits)
                DetailItem(icon = Icons.Default.FamilyRestroom, label = "Family Setup", value = profile.familyDetails)
                DetailItem(icon = Icons.Default.Security, label = "Financial Goal", value = profile.financialGoals)
                DetailItem(icon = Icons.Default.Badge, label = "Tech Comfort", value = profile.techLiteracy.displayName)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onOpenEditProfile,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("btn_reconfigure_signup")
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Edit Details", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    Button(
                        onClick = onLogout,
                        colors = ButtonDefaults.buttonColors(containerColor = ThreatRed.copy(alpha = 0.2f), contentColor = ThreatRed),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("btn_logout")
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Log Out", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // FVI History Timeline Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("fvi_history_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(CyberCyan.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "History",
                                tint = CyberCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "FVI Score Audit Trail",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Text(
                        text = "${fviHistory.size} Records",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                fviHistory.take(5).forEachIndexed { index, record ->
                    val isPositive = record.deltaPoints >= 0
                    val deltaColor = if (isPositive) MintEmerald else ThreatRed

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(deltaColor.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isPositive) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                                    contentDescription = null,
                                    tint = deltaColor,
                                    modifier = Modifier.size(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = record.reason,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(record.timestampMs)),
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (record.deltaPoints != 0) "${if (isPositive) "+" else ""}${record.deltaPoints} Pts" else "Baseline",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = deltaColor
                            )
                            Text(
                                text = "Score: ${record.scoreAfter}",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                    }

                    if (index < fviHistory.size - 1) {
                        HorizontalDivider(color = CardBorder, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun IconButtonWithTag(onClick: () -> Unit, tag: String) {
    Surface(
        color = CardSurfaceElevated,
        shape = CircleShape,
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
        modifier = Modifier.testTag(tag)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(CardSurfaceElevated)
                .padding(6.dp)
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = CyberCyan, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun DetailItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(icon, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "$label: ", fontSize = 12.sp, color = TextMuted)
        Text(text = if (value.isNotBlank()) value else "Not specified", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
    }
}
