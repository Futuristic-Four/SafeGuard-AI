package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ClaimedReward
import com.example.data.model.DemographicGroup
import com.example.data.model.RewardItem
import com.example.data.model.UserProfile
import com.example.data.repository.LeaderboardData
import com.example.data.repository.LeaderboardTimeframe
import com.example.data.repository.RewardsData
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
import com.example.ui.theme.WarningGold
import org.json.JSONArray

@Composable
fun LeaderboardScreen(
    userProfile: UserProfile?,
    onRedeemReward: ((RewardItem, (Boolean, String, String) -> Unit) -> Unit)? = null
) {
    var selectedTimeframe by remember { mutableStateOf(LeaderboardTimeframe.WEEK) }
    var filterByAgeGroup by remember { mutableStateOf(true) }

    // Rewards tab state: 0 -> Store Offers, 1 -> My Claimed Vault
    var selectedRewardTab by remember { mutableStateOf(0) }
    var selectedCategoryFilter by remember { mutableStateOf("ALL") }

    // Modal state for confirmation and success
    var pendingRedeemReward by remember { mutableStateOf<RewardItem?>(null) }
    var successRedeemedCode by remember { mutableStateOf<Pair<RewardItem, String>?>(null) }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val currentUserGroup = userProfile?.demographicGroup ?: DemographicGroup.PROFESSIONALS_23_40
    val currentUserFvi = userProfile?.fviScore ?: 680
    val currentUserName = userProfile?.userName ?: "Alex Vance"
    val userAvailableXp = userProfile?.availableXp ?: 1450

    val leaderboardEntries = remember(selectedTimeframe, filterByAgeGroup, currentUserGroup, currentUserFvi, currentUserName) {
        LeaderboardData.getLeaderboard(
            userDemographic = currentUserGroup,
            timeframe = selectedTimeframe,
            filterByAgeGroup = filterByAgeGroup,
            currentUserFvi = currentUserFvi,
            currentUserName = currentUserName
        )
    }

    // Monthly Leaderboard check for Top 3 Free Antivirus Perk
    val monthlyEntries = remember(currentUserGroup, filterByAgeGroup, currentUserFvi, currentUserName) {
        LeaderboardData.getLeaderboard(
            userDemographic = currentUserGroup,
            timeframe = LeaderboardTimeframe.MONTH,
            filterByAgeGroup = filterByAgeGroup,
            currentUserFvi = currentUserFvi,
            currentUserName = currentUserName
        )
    }

    val userMonthlyRank = monthlyEntries.find { it.isCurrentUser }?.rank ?: 99
    val isUserInMonthlyTop3 = userMonthlyRank in 1..3

    val currentUserEntry = leaderboardEntries.find { it.isCurrentUser }
    val topThree = leaderboardEntries.take(3)
    val remainingEntries = if (leaderboardEntries.size > 3) leaderboardEntries.drop(3) else emptyList()

    // Claimed rewards parsed from JSON
    val claimedRewardsList = remember(userProfile?.claimedRewardsJson) {
        parseClaimedRewardsJson(userProfile?.claimedRewardsJson)
    }

    val allRewards = RewardsData.sampleRewards
    val filteredRewards = remember(selectedCategoryFilter) {
        when (selectedCategoryFilter) {
            "ANTIVIRUS" -> allRewards.filter { it.category == "ANTIVIRUS" }
            "SCAM_TOOL" -> allRewards.filter { it.category == "SCAM_TOOL" }
            "HARDWARE" -> allRewards.filter { it.category == "HARDWARE" }
            "TOP3_PERK" -> allRewards.filter { it.isTop3Exclusive }
            else -> allRewards
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("leaderboard_screen"),
        color = ObsidianBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header Banner
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = WarningGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CYBER SHIELD LEADERBOARD",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    Text(
                        text = if (filterByAgeGroup) "Competing in: ${currentUserGroup.displayName}" else "Global Cyber Defense Rankings",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                // Age Group Toggle Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (filterByAgeGroup) CyberCyan.copy(alpha = 0.2f) else CardSurfaceElevated)
                        .border(1.dp, if (filterByAgeGroup) CyberCyan else CardBorder, RoundedCornerShape(12.dp))
                        .clickable { filterByAgeGroup = !filterByAgeGroup }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("toggle_age_filter")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = if (filterByAgeGroup) CyberCyan else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (filterByAgeGroup) "My Age Group" else "All Ages",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (filterByAgeGroup) CyberCyan else TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Timeframe Selector Tabs (Week / Month / Year)
            val timeframes = LeaderboardTimeframe.values()
            TabRow(
                selectedTabIndex = selectedTimeframe.ordinal,
                containerColor = CardSurface,
                contentColor = CyberCyan,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTimeframe.ordinal]),
                        color = CyberCyan
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
            ) {
                timeframes.forEachIndexed { index, timeframe ->
                    Tab(
                        selected = selectedTimeframe.ordinal == index,
                        onClick = { selectedTimeframe = timeframe },
                        text = {
                            Text(
                                text = timeframe.label,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTimeframe.ordinal == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTimeframe.ordinal == index) CyberCyan else TextMuted
                            )
                        },
                        modifier = Modifier.testTag("timeframe_tab_$index")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User Current Rank Summary Card
            if (currentUserEntry != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CyberCyan.copy(alpha = 0.12f))
                        .border(1.dp, CyberCyan, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                        .testTag("user_rank_summary_card")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(CyberCyan),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "#${currentUserEntry.rank}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ObsidianBackground
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = currentUserEntry.userName,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "(YOU)",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyberCyan
                                    )
                                }
                                Text(
                                    text = "FVI Score: ${currentUserEntry.fviScore} • ${currentUserEntry.badgeName}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            val pts = when (selectedTimeframe) {
                                LeaderboardTimeframe.WEEK -> currentUserEntry.weeklyPoints
                                LeaderboardTimeframe.MONTH -> currentUserEntry.monthlyPoints
                                LeaderboardTimeframe.YEAR -> currentUserEntry.yearlyPoints
                            }
                            Text(
                                text = "$pts XP",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = WarningGold
                            )
                            Text(
                                text = selectedTimeframe.label,
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main List with Top 3 Podium, Ranked Rows, AND Scam Prevention Rewards Store
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Top 3 Podium View
                item {
                    if (topThree.isNotEmpty()) {
                        Text(
                            text = "TOP CYBER DEFENDERS 👑",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = WarningGold,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            // #2 Silver (Left)
                            if (topThree.size >= 2) {
                                PodiumCard(
                                    entry = topThree[1],
                                    rank = 2,
                                    timeframe = selectedTimeframe,
                                    color = Color(0xFFC0C0C0),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            // #1 Gold (Center, taller)
                            if (topThree.isNotEmpty()) {
                                PodiumCard(
                                    entry = topThree[0],
                                    rank = 1,
                                    timeframe = selectedTimeframe,
                                    color = WarningGold,
                                    modifier = Modifier.weight(1.1f)
                                )
                            }

                            // #3 Bronze (Right)
                            if (topThree.size >= 3) {
                                PodiumCard(
                                    entry = topThree[2],
                                    rank = 3,
                                    timeframe = selectedTimeframe,
                                    color = Color(0xFFCD7F32),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "ALL RANKED PARTICIPANTS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Remaining Ranked Rows
                itemsIndexed(remainingEntries) { index, entry ->
                    LeaderboardRow(
                        entry = entry,
                        timeframe = selectedTimeframe
                    )
                }

                // =========================================================================
                // REWARDS SECTION (BELOW ALL RANKED PARTICIPANTS LIST)
                // =========================================================================
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Divider(color = CardBorder, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(20.dp))

                    // Rewards Section Title
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(WarningGold.copy(alpha = 0.2f))
                                    .border(1.dp, WarningGold, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = null,
                                    tint = WarningGold,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "SCAM PREVENTION REWARDS 🎁",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Use earned XP for Antivirus, VPNs & Hardware Keys",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // User Available XP Banner & Toggle Pill (Offers vs Claimed Vault)
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = CardSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = WarningGold,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = "YOUR AVAILABLE BALANCE",
                                            fontSize = 10.sp,
                                            color = TextMuted,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "$userAvailableXp XP",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = WarningGold
                                        )
                                    }
                                }

                                // Toggle Pills: Store Offers vs My Vault
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(CardSurfaceElevated)
                                        .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                                        .padding(3.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (selectedRewardTab == 0) CyberCyan.copy(alpha = 0.25f) else Color.Transparent)
                                            .clickable { selectedRewardTab = 0 }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                            .testTag("reward_tab_offers")
                                    ) {
                                        Text(
                                            text = "Store Offers",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selectedRewardTab == 0) CyberCyan else TextMuted
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (selectedRewardTab == 1) WarningGold.copy(alpha = 0.25f) else Color.Transparent)
                                            .clickable { selectedRewardTab = 1 }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                            .testTag("reward_tab_vault")
                                    ) {
                                        Text(
                                            text = "My Vault (${claimedRewardsList.size})",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selectedRewardTab == 1) WarningGold else TextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // MONTHLY TOP 3 SPECIAL ANTIVIRUS FEATURED BANNER
                    if (selectedRewardTab == 0) {
                        MonthlyTop3BannerCard(
                            userMonthlyRank = userMonthlyRank,
                            isUserInMonthlyTop3 = isUserInMonthlyTop3,
                            onClaimTop3 = {
                                val top3RewardItem = allRewards.find { it.isTop3Exclusive }
                                if (top3RewardItem != null) {
                                    pendingRedeemReward = top3RewardItem
                                }
                            },
                            onSwitchToMonthly = { selectedTimeframe = LeaderboardTimeframe.MONTH }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Category Filter Chips
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            item {
                                RewardCategoryFilterChip(
                                    label = "All Rewards",
                                    isSelected = selectedCategoryFilter == "ALL",
                                    onClick = { selectedCategoryFilter = "ALL" }
                                )
                            }
                            item {
                                RewardCategoryFilterChip(
                                    label = "Antivirus (McAfee/Norton)",
                                    isSelected = selectedCategoryFilter == "ANTIVIRUS",
                                    onClick = { selectedCategoryFilter = "ANTIVIRUS" }
                                )
                            }
                            item {
                                RewardCategoryFilterChip(
                                    label = "Scam Prevention Tools",
                                    isSelected = selectedCategoryFilter == "SCAM_TOOL",
                                    onClick = { selectedCategoryFilter = "SCAM_TOOL" }
                                )
                            }
                            item {
                                RewardCategoryFilterChip(
                                    label = "Hardware Keys",
                                    isSelected = selectedCategoryFilter == "HARDWARE",
                                    onClick = { selectedCategoryFilter = "HARDWARE" }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // TAB 0: STORE OFFERS
                if (selectedRewardTab == 0) {
                    items(filteredRewards) { reward ->
                        val isAlreadyClaimed = claimedRewardsList.any { it.rewardId == reward.id }
                        RewardCard(
                            reward = reward,
                            userXp = userAvailableXp,
                            isAlreadyClaimed = isAlreadyClaimed,
                            isUserInMonthlyTop3 = isUserInMonthlyTop3,
                            onRedeemClick = {
                                pendingRedeemReward = reward
                            },
                            onViewClaimedClick = {
                                selectedRewardTab = 1
                            }
                        )
                    }
                }

                // TAB 1: MY CLAIMED REWARDS VAULT
                if (selectedRewardTab == 1) {
                    item {
                        if (claimedRewardsList.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = TextMuted,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "No Redeemed Rewards Yet",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Use your XP balance to get antivirus discounts, hardware keys, or finish top 3 monthly for 1-year free antivirus!",
                                        fontSize = 12.sp,
                                        color = TextSecondary,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(horizontal = 24.dp)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = { selectedRewardTab = 0 },
                                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Browse Store Offers", color = Color.Black, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    items(claimedRewardsList) { claimed ->
                        ClaimedRewardVaultCard(
                            claimedReward = claimed,
                            onCopyCode = { code ->
                                clipboardManager.setText(AnnotatedString(code))
                                Toast.makeText(context, "Activation code copied to clipboard!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }

    // =========================================================================
    // REDEMPTION CONFIRMATION DIALOG
    // =========================================================================
    pendingRedeemReward?.let { reward ->
        Dialog(onDismissRequest = { pendingRedeemReward = null }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = CardSurfaceElevated,
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("redemption_confirmation_dialog")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = reward.iconEmoji,
                        fontSize = 42.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "CONFIRM REWARD REDEMPTION",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CyberCyan,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = reward.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Partner: ${reward.providerName} • ${reward.rewardValueText}",
                        fontSize = 12.sp,
                        color = MintEmerald,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = reward.description,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(CardSurface)
                            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (reward.isTop3Exclusive) "Requirement" else "XP Deduction",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                                Text(
                                    text = if (reward.isTop3Exclusive) "Monthly Top 3 Rank" else "-${reward.xpCost} XP",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WarningGold
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "XP Balance After",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                                val remaining = if (reward.isTop3Exclusive) userAvailableXp else (userAvailableXp - reward.xpCost)
                                Text(
                                    text = "$remaining XP",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { pendingRedeemReward = null },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Cancel", color = TextSecondary)
                        }

                        Button(
                            onClick = {
                                val currentRewardToClaim = reward
                                pendingRedeemReward = null
                                onRedeemReward?.invoke(currentRewardToClaim) { success, msg, code ->
                                    if (success) {
                                        successRedeemedCode = Pair(currentRewardToClaim, code)
                                    } else {
                                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                    }
                                }
                            },
                            modifier = Modifier.weight(1.3f),
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Confirm & Get Code", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // =========================================================================
    // SUCCESSFUL REDEMPTION DIALOG WITH CODE & COPY BUTTON
    // =========================================================================
    successRedeemedCode?.let { (reward, code) ->
        Dialog(onDismissRequest = { successRedeemedCode = null }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = CardSurfaceElevated,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, MintEmerald),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("redemption_success_dialog")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MintEmerald,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "REWARD UNLOCKED! 🎉",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MintEmerald,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = reward.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Code Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(ObsidianBackground)
                            .border(1.dp, CyberCyan, RoundedCornerShape(14.dp))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "YOUR ACTIVATION KEY",
                                fontSize = 10.sp,
                                color = TextMuted,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = code,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CyberCyan,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(code))
                            Toast.makeText(context, "Copied activation code to clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Copy Code to Clipboard", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Visit ${reward.providerName}'s activation portal (${reward.redemptionUrl}) and enter this key to claim your offer.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            successRedeemedCode = null
                            selectedRewardTab = 1
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CardSurface),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View in My Vault", color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun MonthlyTop3BannerCard(
    userMonthlyRank: Int,
    isUserInMonthlyTop3: Boolean,
    onClaimTop3: () -> Unit,
    onSwitchToMonthly: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(1.5.dp, Brush.horizontalGradient(listOf(WarningGold, CyberCyan)), RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = CardSurfaceElevated)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("👑", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "MONTHLY TOP 3 CHAMPION PERK",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = WarningGold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "1-Year FREE McAfee or Norton Premium License",
                            fontSize = 11.sp,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(WarningGold.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$99 Value • FREE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = WarningGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Participants who finish in the Top 3 on the Monthly Leaderboard unlock a full 1-Year Premium License Key for McAfee Total Protection or Norton 360 Deluxe completely free of charge!",
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (isUserInMonthlyTop3) {
                Button(
                    onClick = onClaimTop3,
                    colors = ButtonDefaults.buttonColors(containerColor = WarningGold),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("claim_top3_free_antivirus_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = ObsidianBackground, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "🎉 YOU QUALIFY (#$userMonthlyRank)! CLAIM FREE 1-YEAR ANTIVIRUS",
                            color = ObsidianBackground,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardSurface)
                        .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Your Current Monthly Rank: #$userMonthlyRank",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberCyan
                        )
                        Text(
                            text = "Climb to #1, #2, or #3 on Monthly Leaderboard to unlock!",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(CyberCyan.copy(alpha = 0.2f))
                            .border(1.dp, CyberCyan, RoundedCornerShape(10.dp))
                            .clickable { onSwitchToMonthly() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("Check Rank", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberCyan)
                    }
                }
            }
        }
    }
}

@Composable
fun RewardCategoryFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) CyberCyan else CardSurface)
            .border(1.dp, if (isSelected) CyberCyan else CardBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
            color = if (isSelected) ObsidianBackground else TextSecondary
        )
    }
}

@Composable
fun RewardCard(
    reward: RewardItem,
    userXp: Int,
    isAlreadyClaimed: Boolean,
    isUserInMonthlyTop3: Boolean,
    onRedeemClick: () -> Unit,
    onViewClaimedClick: () -> Unit
) {
    val canAfford = reward.isTop3Exclusive || userXp >= reward.xpCost

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, if (reward.isTop3Exclusive) WarningGold.copy(alpha = 0.8f) else CardBorder, RoundedCornerShape(16.dp))
            .testTag("reward_card_${reward.id}"),
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Text(reward.iconEmoji, fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = reward.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "By ${reward.providerName}",
                                fontSize = 11.sp,
                                color = MintEmerald,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "• ${reward.originalPriceText}",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                }

                // Tag Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (reward.isTop3Exclusive) WarningGold.copy(alpha = 0.2f) else CyberCyan.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = reward.rewardValueText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (reward.isTop3Exclusive) WarningGold else CyberCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = reward.description,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // XP Cost Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = WarningGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (reward.isTop3Exclusive) "0 XP (Top 3 Perk)" else "${reward.xpCost} XP",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarningGold
                    )
                }

                // Action Button
                if (isAlreadyClaimed) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(MintEmerald.copy(alpha = 0.2f))
                            .border(1.dp, MintEmerald, RoundedCornerShape(10.dp))
                            .clickable { onViewClaimedClick() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MintEmerald, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("✓ Claimed (View Key)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MintEmerald)
                        }
                    }
                } else if (reward.isTop3Exclusive) {
                    if (isUserInMonthlyTop3) {
                        Button(
                            onClick = onRedeemClick,
                            colors = ButtonDefaults.buttonColors(containerColor = WarningGold),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Claim Free License", color = ObsidianBackground, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(CardSurfaceElevated)
                                .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Monthly Top 3 Exclusive", fontSize = 11.sp, color = TextMuted)
                        }
                    }
                } else if (canAfford) {
                    Button(
                        onClick = onRedeemClick,
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Redeem for ${reward.xpCost} XP", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(CardSurfaceElevated)
                            .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Need ${reward.xpCost} XP (Have $userXp)", fontSize = 11.sp, color = TextMuted)
                    }
                }
            }
        }
    }
}

@Composable
fun ClaimedRewardVaultCard(
    claimedReward: ClaimedReward,
    onCopyCode: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MintEmerald, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = CardSurfaceElevated)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = claimedReward.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Provider: ${claimedReward.providerName}",
                        fontSize = 11.sp,
                        color = MintEmerald
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MintEmerald.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "ACTIVATED",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MintEmerald
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Activation Key Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ObsidianBackground)
                    .border(1.dp, CyberCyan, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "LICENSE / VOUCHER CODE",
                            fontSize = 9.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = claimedReward.activationCode,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CyberCyan
                        )
                    }

                    Button(
                        onClick = { onCopyCode(claimedReward.activationCode) },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy", fontSize = 11.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = claimedReward.instructions,
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }
}

private fun parseClaimedRewardsJson(json: String?): List<ClaimedReward> {
    if (json.isNullOrBlank() || json == "[]") return emptyList()
    return try {
        val array = JSONArray(json)
        val list = mutableListOf<ClaimedReward>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(
                ClaimedReward(
                    id = obj.optString("id", java.util.UUID.randomUUID().toString()),
                    rewardId = obj.optString("rewardId", ""),
                    title = obj.optString("title", ""),
                    providerName = obj.optString("providerName", ""),
                    activationCode = obj.optString("activationCode", ""),
                    xpSpent = obj.optInt("xpSpent", 0),
                    claimedTimestampMs = obj.optLong("claimedTimestampMs", System.currentTimeMillis()),
                    redemptionUrl = obj.optString("redemptionUrl", "https://safeguard.ai/activate"),
                    instructions = obj.optString("instructions", "Copy license key to redeem.")
                )
            )
        }
        list
    } catch (e: Exception) {
        emptyList()
    }
}

@Composable
fun PodiumCard(
    entry: com.example.data.model.LeaderboardEntry,
    rank: Int,
    timeframe: LeaderboardTimeframe,
    color: Color,
    modifier: Modifier = Modifier
) {
    val pts = when (timeframe) {
        LeaderboardTimeframe.WEEK -> entry.weeklyPoints
        LeaderboardTimeframe.MONTH -> entry.monthlyPoints
        LeaderboardTimeframe.YEAR -> entry.yearlyPoints
    }

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, color.copy(alpha = 0.6f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = CardSurfaceElevated)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = when (rank) {
                    1 -> "🥇 #1"
                    2 -> "🥈 #2"
                    else -> "🥉 #3"
                },
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = entry.avatarEmoji,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = entry.userName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 1,
                textAlign = TextAlign.Center
            )

            Text(
                text = "$pts XP",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )

            Text(
                text = "🔥 ${entry.streakDays}d Streak • FVI ${entry.fviScore}",
                fontSize = 10.sp,
                color = TextMuted
            )
        }
    }
}

@Composable
fun LeaderboardRow(
    entry: com.example.data.model.LeaderboardEntry,
    timeframe: LeaderboardTimeframe
) {
    val pts = when (timeframe) {
        LeaderboardTimeframe.WEEK -> entry.weeklyPoints
        LeaderboardTimeframe.MONTH -> entry.monthlyPoints
        LeaderboardTimeframe.YEAR -> entry.yearlyPoints
    }

    val isUser = entry.isCurrentUser
    val rowBorder = if (isUser) CyberCyan else CardBorder
    val rowBg = if (isUser) CyberCyan.copy(alpha = 0.1f) else CardSurface

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(rowBg)
            .border(1.dp, rowBorder, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("leaderboard_row_${entry.rank}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "#${entry.rank}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isUser) CyberCyan else TextMuted,
                    modifier = Modifier.width(32.dp)
                )

                Text(
                    text = entry.avatarEmoji,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = entry.userName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        if (isUser) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "• YOU",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberCyan
                            )
                        }
                    }
                    Text(
                        text = "${entry.occupation} • ${entry.badgeName}",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        maxLines = 1
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$pts XP",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = WarningGold
                )
                Text(
                    text = "🔥 ${entry.streakDays}d • FVI ${entry.fviScore}",
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }
        }
    }
}
