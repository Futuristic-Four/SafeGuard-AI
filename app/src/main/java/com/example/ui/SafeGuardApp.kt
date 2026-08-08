package com.example.ui

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.R
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.AuthOnboardingScreen
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AiScannerDialog
import com.example.ui.components.ThreatAlertsBottomSheet
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LearningHubScreen
import com.example.ui.screens.ProfileAnalyticsScreen
import androidx.compose.material.icons.filled.EmojiEvents
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.theme.WarningGold
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ThreatRed
import com.example.viewmodel.SafeGuardViewModel

import com.example.ui.components.AiCompanionChatSheet
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material.icons.filled.SmartToy

@Composable
fun SafeGuardApp(
    viewModel: SafeGuardViewModel = viewModel()
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val modules by viewModel.modules.collectAsState()
    val threatAlerts by viewModel.threatAlerts.collectAsState()
    val fviHistory by viewModel.fviHistory.collectAsState()

    val selectedTab by viewModel.selectedTab.collectAsState()
    val simulationCategory by viewModel.simulationCategory.collectAsState()
    val isScannerOpen by viewModel.isScannerDialogOpen.collectAsState()
    val isThreatSheetOpen by viewModel.isThreatSheetOpen.collectAsState()
    val activeScamReplay by viewModel.activeScamReplay.collectAsState()

    var showSplash by remember { mutableStateOf(true) }
    var isEditingProfile by remember { mutableStateOf(false) }
    var showAiCompanionSheet by remember { mutableStateOf(false) }

    val currentScore = userProfile?.fviScore ?: 680

    // 1. Show Splash Screen first on app start
    if (showSplash) {
        SplashScreen(
            userProfile = userProfile,
            onProceed = { showSplash = false }
        )
        return
    }

    // 2. Show Auth/Signup screen if user is not logged in or requested profile edit
    if (userProfile?.isLoggedIn != true || isEditingProfile) {
        AuthOnboardingScreen(
            onLogin = { identifier, password, loginType ->
                viewModel.loginUser(
                    identifier = identifier,
                    password = password,
                    loginType = loginType
                )
                isEditingProfile = false
            },
            onSignUp = { identifier, loginType, password, name, occupation, group, literacy, scamExp, banking, family, goals ->
                viewModel.saveUserProfile(
                    identifier = identifier,
                    loginType = loginType,
                    password = password,
                    name = name,
                    occupation = occupation,
                    demographicGroup = group,
                    techLiteracy = literacy,
                    scamExperience = scamExp,
                    bankingHabits = banking,
                    familyDetails = family,
                    financialGoals = goals
                )
                isEditingProfile = false
            }
        )
        return
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBackground),
        topBar = {
            // Sleek FinTech Top Navigation Bar
            Surface(
                color = CardSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // SafeGuard AI Brand Logo & Name
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { viewModel.setSelectedTab(0) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, CyberCyan, RoundedCornerShape(10.dp))
                                .background(CardSurface)
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.app_logo),
                                contentDescription = "SafeGuard AI Logo",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "SafeGuard AI",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Financial Safety Training",
                                fontSize = 10.sp,
                                color = MintEmerald,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Action Icons: AI Scanner, Notification Bell, FVI Quick Badge Avatar
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // AI Threat Scanner Button
                        IconButton(
                            onClick = { viewModel.toggleScannerDialog(true) },
                            modifier = Modifier.testTag("top_bar_ai_scanner_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI Scanner",
                                tint = CyberCyan,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Notification Bell with Badge
                        IconButton(
                            onClick = { viewModel.toggleThreatSheet(true) },
                            modifier = Modifier.testTag("top_bar_notifications_button")
                        ) {
                            BadgedBox(
                                badge = {
                                    if (threatAlerts.isNotEmpty()) {
                                        Badge(containerColor = ThreatRed) {
                                            Text(
                                                text = "${threatAlerts.size}",
                                                color = Color.White,
                                                fontSize = 9.sp
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Threat Alerts",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Quick FVI Score Pill Badge
                        Surface(
                            shape = CircleShape,
                            color = MintEmerald.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MintEmerald.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .clickable { viewModel.setSelectedTab(3) }
                                .testTag("top_bar_fvi_badge")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = "FVI",
                                    tint = MintEmerald,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$currentScore",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MintEmerald
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            // FinTech Bottom Navigation Bar
            NavigationBar(
                containerColor = CardSurface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .navigationBarsPadding()
                    .testTag("bottom_navigation_bar")
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { viewModel.setSelectedTab(0) },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Dashboard", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MintEmerald,
                        selectedTextColor = MintEmerald,
                        indicatorColor = MintEmerald.copy(alpha = 0.15f),
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_item_dashboard")
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { viewModel.setSelectedTab(1) },
                    icon = { Icon(Icons.Default.School, contentDescription = "Learning") },
                    label = { Text("Learning", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CyberCyan,
                        selectedTextColor = CyberCyan,
                        indicatorColor = CyberCyan.copy(alpha = 0.15f),
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_item_learning")
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { viewModel.setSelectedTab(2) },
                    icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Leaderboard") },
                    label = { Text("Leaderboard", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = WarningGold,
                        selectedTextColor = WarningGold,
                        indicatorColor = WarningGold.copy(alpha = 0.15f),
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_item_leaderboard")
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { viewModel.setSelectedTab(3) },
                    icon = { Icon(Icons.Default.Security, contentDescription = "Simulations") },
                    label = { Text("Simulations", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ThreatRed,
                        selectedTextColor = ThreatRed,
                        indicatorColor = ThreatRed.copy(alpha = 0.15f),
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_item_simulations")
                )

                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { viewModel.setSelectedTab(4) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RoyalBlue,
                        selectedTextColor = RoyalBlue,
                        indicatorColor = RoyalBlue.copy(alpha = 0.15f),
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_item_profile")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> DashboardScreen(
                    userProfile = userProfile,
                    alerts = threatAlerts,
                    recommendedModules = modules,
                    onOpenAlerts = { viewModel.toggleThreatSheet(true) },
                    onOpenScanner = { viewModel.toggleScannerDialog(true) },
                    onNavigateSimulations = { category ->
                        viewModel.setSimulationCategory(category)
                        viewModel.setSelectedTab(3)
                    },
                    onNavigateLearning = { viewModel.setSelectedTab(1) }
                )
                1 -> LearningHubScreen(
                    userProfile = userProfile,
                    modules = modules,
                    onCompleteModule = { moduleId, points ->
                        viewModel.completeModule(moduleId, points)
                    },
                    onCompleteSubTask = { moduleId, taskId, points ->
                        viewModel.completeSubTask(moduleId, taskId, points)
                    },
                    onOpenChatbot = { showAiCompanionSheet = true }
                )
                2 -> LeaderboardScreen(
                    userProfile = userProfile,
                    onRedeemReward = { reward, callback ->
                        viewModel.redeemReward(reward, callback)
                    }
                )
                3 -> com.example.ui.screens.SimulationHubScreen(
                    userProfile = userProfile,
                    selectedCategoryIndex = simulationCategory,
                    onCategorySelected = { viewModel.setSimulationCategory(it) },
                    onRecordScore = { delta, reason ->
                        viewModel.recordSimulationScore(delta, reason)
                    },
                    onRecordSimulationResult = { isSuccess, cat, replay ->
                        viewModel.recordSimulationResult(isSuccess, cat, replay)
                    },
                    onViewReplayRequested = { replayData ->
                        viewModel.triggerScamReplay(replayData)
                    }
                )
                4 -> ProfileAnalyticsScreen(
                    userProfile = userProfile,
                    fviHistory = fviHistory,
                    onProfileChanged = { group, literacy ->
                        viewModel.updateDemographicProfile(group, literacy)
                    },
                    onOpenEditProfile = { isEditingProfile = true },
                    onLogout = { viewModel.logoutUser() }
                )
            }

            // Floating Circular AI Educator Chatbot Button (Bottom Right of Screen)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 18.dp, bottom = 18.dp)
            ) {
                FloatingActionButton(
                    onClick = { showAiCompanionSheet = true },
                    containerColor = CyberCyan,
                    contentColor = Color.Black,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .size(56.dp)
                        .border(2.dp, MintEmerald, CircleShape)
                        .testTag("floating_ai_chatbot_fab")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = "AI Companion Chatbot",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                }
            }

            // AI Educator Companion Chatbot Dialog Sheet
            if (showAiCompanionSheet) {
                AiCompanionChatSheet(
                    userProfile = userProfile,
                    onDismiss = { showAiCompanionSheet = false }
                )
            }

            // Explainable Scam Replay Dialog
            activeScamReplay?.let { replay ->
                com.example.ui.components.ScamReplayDialog(
                    replayData = replay,
                    onDismiss = { viewModel.dismissScamReplay() },
                    onRetryTask = { viewModel.dismissScamReplay() }
                )
            }

            // AI Scanner Dialog
            if (isScannerOpen) {
                AiScannerDialog(
                    onDismiss = { viewModel.toggleScannerDialog(false) },
                    onAnalyze = { input -> viewModel.analyzeContent(input) }
                )
            }

            // Threat Alerts Bottom Sheet
            if (isThreatSheetOpen) {
                ThreatAlertsBottomSheet(
                    alerts = threatAlerts,
                    onDismiss = { viewModel.toggleThreatSheet(false) }
                )
            }
        }
    }
}
