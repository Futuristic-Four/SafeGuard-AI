package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ScamReplayData
import com.example.data.model.UserProfile
import com.example.data.repository.InitialData
import com.example.ui.components.ChatScammerInterface
import com.example.ui.components.EmailInspectorCard
import com.example.ui.components.ScamGenomeCard
import com.example.ui.components.WaveformVisualizer
import com.example.ui.components.WebsiteSpotterCard
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.TextMuted

@Composable
fun SimulationHubScreen(
    userProfile: UserProfile?,
    selectedCategoryIndex: Int,
    onCategorySelected: (catIndex: Int) -> Unit,
    onRecordScore: (delta: Int, reason: String) -> Unit,
    onRecordSimulationResult: (isSuccess: Boolean, category: String, replayData: ScamReplayData?) -> Unit,
    onViewReplayRequested: (replayData: ScamReplayData) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val profile = userProfile ?: UserProfile()

    val tabs = listOf(
        "Phishing Email" to Icons.Default.AlternateEmail,
        "AI Deepfake Voice" to Icons.Default.RecordVoiceOver,
        "Live Scammer Chat" to Icons.Default.Forum,
        "Website Inspector" to Icons.Default.Language
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117))
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Tab Row Switcher (4 Categories)
        TabRow(
            selectedTabIndex = selectedCategoryIndex,
            containerColor = CardSurface,
            contentColor = CyberCyan,
            divider = {},
            indicator = { tabPositions ->
                if (selectedCategoryIndex < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedCategoryIndex]),
                        height = 3.dp,
                        color = CyberCyan
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("simulation_hub_tabs")
        ) {
            tabs.forEachIndexed { index, (label, icon) ->
                Tab(
                    selected = selectedCategoryIndex == index,
                    onClick = { onCategorySelected(index) },
                    text = {
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = if (selectedCategoryIndex == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedCategoryIndex == index) CyberCyan else TextMuted
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (selectedCategoryIndex == index) CyberCyan else TextMuted
                        )
                    },
                    modifier = Modifier.testTag("simulation_tab_$index")
                )
            }
        }

        // Active Simulation Display
        when (selectedCategoryIndex) {
            0 -> {
                // Email Phishing Inspector
                val emailScenario = InitialData.emailScenarios.first()
                EmailInspectorCard(
                    scenario = emailScenario,
                    onAssessmentSubmitted = { flagsSpotted, totalFlags ->
                        val isSuccess = flagsSpotted >= totalFlags
                        val replay = InitialData.sampleScamReplays["PHISHING"]
                        onRecordSimulationResult(isSuccess, "Phishing Email", replay)
                    }
                )
            }
            1 -> {
                // AI Deepfake Voice Challenge
                val deepfakeScenario = InitialData.deepfakeScenarios.first()
                WaveformVisualizer(
                    scenario = deepfakeScenario,
                    onAnswerSubmitted = { isAiVerdict, correct ->
                        val replay = InitialData.sampleScamReplays["DEEPFAKE"]
                        onRecordSimulationResult(correct, "AI Deepfake Voice", replay)
                        if (!correct && replay != null) {
                            onViewReplayRequested(replay)
                        }
                    }
                )
            }
            2 -> {
                // Live Scammer Red Teaming Chat
                val chatScenario = InitialData.chatScenarios.first()
                val replay = InitialData.sampleScamReplays["LIVE_CHAT"]
                ChatScammerInterface(
                    scenario = chatScenario,
                    onChoiceMade = { choice ->
                        onRecordSimulationResult(choice.isSafeChoice, "Live Scammer Chat", replay)
                        if (!choice.isSafeChoice && replay != null) {
                            onViewReplayRequested(replay)
                        }
                    },
                    onViewReplayRequested = {
                        replay?.let { onViewReplayRequested(it) }
                    }
                )
            }
            3 -> {
                // Fake Website Spotter Simulation
                val websiteScenario = InitialData.websiteScenarios.first()
                val replay = InitialData.sampleScamReplays["WEBSITE"]
                WebsiteSpotterCard(
                    scenario = websiteScenario,
                    onAssessmentSubmitted = { isSuccess, spottedCount, totalRedFlags ->
                        onRecordSimulationResult(isSuccess, "Website Inspection", replay)
                        if (!isSuccess && replay != null) {
                            onViewReplayRequested(replay)
                        }
                    },
                    onViewReplayRequested = {
                        replay?.let { onViewReplayRequested(it) }
                    }
                )
            }
        }
    }
}
