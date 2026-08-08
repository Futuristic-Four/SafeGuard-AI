package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.data.model.DemographicGroup
import com.example.data.model.LearningModule
import com.example.data.model.PathSubTask
import com.example.data.model.SubTaskType
import com.example.data.model.UserProfile
import com.example.data.repository.SubTaskHelper
import com.example.ui.components.ModuleDetailDialog
import com.example.ui.components.PathTaskDialog
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

import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.ui.res.painterResource
import com.example.R

@Composable
fun LearningHubScreen(
    userProfile: UserProfile?,
    modules: List<LearningModule>,
    onCompleteModule: (moduleId: String, points: Int) -> Unit,
    onCompleteSubTask: (moduleId: String, taskId: String, points: Int) -> Unit = { _, _, _ -> },
    onOpenChatbot: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val activeProfile = userProfile ?: UserProfile()
    val activeGroup = activeProfile.demographicGroup

    val primaryModules = modules.filter { it.targetDemographic == activeGroup }
    val secondaryModules = modules.filter { it.targetDemographic != activeGroup }

    var showAdditionalCourses by remember { mutableStateOf(false) }
    var selectedModuleForDialog by remember { mutableStateOf<LearningModule?>(null) }
    var activePathTaskSelection by remember { mutableStateOf<Triple<LearningModule, PathSubTask, Int>?>(null) }

    // Avatar Companion Tip Index State
    var avatarTipIndex by remember { mutableStateOf(0) }
    val avatarTips = remember {
        listOf(
            "Hey ${activeProfile.userName.substringBefore(" ")}! 🛡️ Complete 2 more scenario nodes to unlock the Master Immunity Badge and boost your FVI Score!",
            "Did you know? 84% of financial fraud starts with high-pressure SMS links! Practice the Email Red Flag Inspector module to train your brain.",
            "Awesome job keeping your family safe! Ask me any doubts anytime using the floating AI Educator Chatbot at the bottom right!"
        )
    }

    // Overall Progress Calculation
    val totalSubTasks = remember(modules) {
        val count = modules.sumOf { module ->
            val parsed = SubTaskHelper.parseJson(module.subTasksJson)
            if (parsed.isNotEmpty()) parsed.size else 4
        }
        if (count > 0) count else 12
    }
    val completedSubTasks = remember(modules) {
        modules.sumOf { module ->
            val parsed = SubTaskHelper.parseJson(module.subTasksJson)
            if (parsed.isNotEmpty()) parsed.count { it.isCompleted } else if (module.isCompleted) 4 else 1
        }
    }
    val overallProgressPercent = if (totalSubTasks > 0) ((completedSubTasks * 100) / totalSubTasks).coerceIn(5, 100) else 25
    val progressFraction = overallProgressPercent / 100f

    // Dialog for sub-task player
    activePathTaskSelection?.let { (module, subTask, stepNum) ->
        PathTaskDialog(
            module = module,
            subTask = subTask,
            stepNumber = stepNum,
            totalSteps = module.totalSubTasksCount,
            onDismiss = { activePathTaskSelection = null },
            onCompleteSubTask = { taskId, points ->
                onCompleteSubTask(module.id, taskId, points)
                activePathTaskSelection = null
            }
        )
    }

    // Fallback single-view dialog
    selectedModuleForDialog?.let { module ->
        ModuleDetailDialog(
            module = module,
            userProfile = activeProfile,
            onDismiss = { selectedModuleForDialog = null },
            onCompleteModule = { points ->
                onCompleteModule(module.id, points)
                selectedModuleForDialog = null
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117))
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Overall Progress Dashboard Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("overall_learning_progress_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(CyberCyan.copy(alpha = 0.2f))
                        ) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(24.dp))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "PATHWAY MASTERY PROGRESS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CyberCyan,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "$overallProgressPercent% Cyber Immunity Achieved",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = WarningGold.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarningGold)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = WarningGold, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${activeProfile.fviScore} XP",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = WarningGold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Bar with Percentage and Metrics
                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = MintEmerald,
                    trackColor = CardSurfaceElevated
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Nodes Passed: $completedSubTasks / $totalSubTasks",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                    Text(
                        text = "Shield Level: ${if (overallProgressPercent >= 70) "Master Defense 🛡️" else "Active Guard ⚔️"}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MintEmerald
                    )
                }
            }
        }

        // 2. Interactive Pop-out Avatar Assistant Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("avatar_companion_popout_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurfaceElevated),
            border = androidx.compose.foundation.BorderStroke(1.dp, MintEmerald.copy(alpha = 0.6f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Avatar Image Character
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(2.dp, MintEmerald, CircleShape)
                            .background(CardSurface)
                            .padding(4.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = "SafeGuard AI Avatar",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    // Speech Bubble Area
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(CardSurface)
                            .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SafeGuard AI Buddy",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MintEmerald
                            )
                            Surface(
                                shape = CircleShape,
                                color = MintEmerald.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "MOTIVATOR",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MintEmerald,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = avatarTips[avatarTipIndex % avatarTips.size],
                            fontSize = 12.sp,
                            color = TextPrimary,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Companion Interactive Action Buttons
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                color = MintEmerald.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MintEmerald),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onOpenChatbot() }
                                    .testTag("btn_ask_avatar_chatbot")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp)
                                ) {
                                    Icon(Icons.Default.ChatBubble, contentDescription = null, tint = MintEmerald, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Ask Doubts", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MintEmerald)
                                }
                            }

                            Surface(
                                color = CyberCyan.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { avatarTipIndex++ }
                                    .testTag("btn_next_avatar_tip")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp)
                                ) {
                                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Next Tip", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberCyan)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Primary Demographic Paths (${activeGroup.displayName})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        // Display Duolingo-style Continuous Path Cards
        primaryModules.forEach { module ->
            DuolingoModulePathCard(
                module = module,
                onNodeClick = { subTask, stepNum ->
                    activePathTaskSelection = Triple(module, subTask, stepNum)
                },
                onFullOpen = { selectedModuleForDialog = module }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Option to explore additional learning paths
        Surface(
            color = CardSurfaceElevated,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showAdditionalCourses = !showAdditionalCourses }
                .testTag("btn_toggle_additional_courses")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Explore, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (showAdditionalCourses) "Hide Additional Learning Paths" else "Explore Additional Paths (${secondaryModules.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Cross-demographic scenarios: Reddit traps, Discord Nitro, Crypto, Seniors",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                Icon(
                    imageVector = if (showAdditionalCourses) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = CyberCyan
                )
            }
        }

        // Additional Modules Path List
        AnimatedVisibility(
            visible = showAdditionalCourses,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                secondaryModules.forEach { module ->
                    DuolingoModulePathCard(
                        module = module,
                        onNodeClick = { subTask, stepNum ->
                            activePathTaskSelection = Triple(module, subTask, stepNum)
                        },
                        onFullOpen = { selectedModuleForDialog = module }
                    )
                }
            }
        }
    }
}

@Composable
fun DuolingoModulePathCard(
    module: LearningModule,
    onNodeClick: (subTask: PathSubTask, stepNumber: Int) -> Unit,
    onFullOpen: () -> Unit
) {
    // Parse or generate tasks for this module
    val tasks = remember(module.subTasksJson, module.id) {
        var list = SubTaskHelper.parseJson(module.subTasksJson)
        if (list.isEmpty()) {
            list = if (module.id.contains("reddit")) SubTaskHelper.getRedditScamPath()
            else if (module.id.contains("teens") || module.id.contains("discord")) SubTaskHelper.getDiscordGamersPath()
            else SubTaskHelper.getGenericPathForModule(module.title, module.category)
        }
        list
    }

    val completedCount = tasks.count { it.isCompleted }
    val progressFraction = if (tasks.isNotEmpty()) completedCount.toFloat() / tasks.size else 0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("path_module_card_${module.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (completedCount == tasks.size && tasks.isNotEmpty()) MintEmerald.copy(alpha = 0.6f) else CardBorder
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = CyberCyan.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = module.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberCyan,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }

                Text(
                    text = "$completedCount/${tasks.size} Nodes Completed",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (completedCount == tasks.size) MintEmerald else TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = module.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = module.description,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (progressFraction == 1f) MintEmerald else CyberCyan,
                trackColor = CardSurfaceElevated
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Duolingo-style Sequential Node Map Visualizer
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardSurfaceElevated)
                    .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "PATH SCENARIO NODES",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )

                tasks.forEachIndexed { index, subTask ->
                    val isCompleted = subTask.isCompleted
                    val isUnlocked = subTask.isUnlocked || index == 0 || (index > 0 && tasks[index - 1].isCompleted)

                    val nodeColor = when {
                        isCompleted -> MintEmerald
                        isUnlocked -> CyberCyan
                        else -> TextMuted
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isUnlocked) nodeColor.copy(alpha = 0.12f) else Color.Transparent)
                            .border(1.dp, if (isUnlocked) nodeColor else CardBorder, RoundedCornerShape(12.dp))
                            .clickable(enabled = isUnlocked) {
                                onNodeClick(subTask, index + 1)
                            }
                            .padding(12.dp)
                            .testTag("node_task_${subTask.id}")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(if (isCompleted) MintEmerald else if (isUnlocked) CyberCyan else CardBorder),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isCompleted) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = ObsidianBackground,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    } else if (isUnlocked) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            tint = ObsidianBackground,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = null,
                                            tint = TextMuted,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Node ${index + 1}: ${subTask.title}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isUnlocked) TextPrimary else TextMuted
                                        )
                                    }
                                    Text(
                                        text = when (subTask.taskType) {
                                            SubTaskType.LESSON -> "Educational Concept • +${subTask.pointsReward} XP"
                                            SubTaskType.SCENARIO -> "Interactive Scenario • +${subTask.pointsReward} XP"
                                            SubTaskType.QUIZ -> "Threat Quiz • +${subTask.pointsReward} XP"
                                            SubTaskType.BOSS_CHALLENGE -> "Boss Challenge • +${subTask.pointsReward} XP"
                                        },
                                        fontSize = 11.sp,
                                        color = if (isUnlocked) nodeColor else TextMuted
                                    )
                                }
                            }

                            // Node Action Tag
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isCompleted) MintEmerald.copy(alpha = 0.2f) else if (isUnlocked) CyberCyan.copy(alpha = 0.2f) else Color.Transparent
                            ) {
                                Text(
                                    text = if (isCompleted) "PASSED 🌟" else if (isUnlocked) "PLAY ▶" else "LOCKED 🔒",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCompleted) MintEmerald else if (isUnlocked) CyberCyan else TextMuted,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
