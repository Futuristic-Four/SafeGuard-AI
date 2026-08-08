package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class DemographicGroup(val displayName: String, val ageRange: String, val primaryFocus: String) {
    KIDS_5_10("Kids & Young Learners", "5–10", "Game currency scams, Fake Roblox Robux popups, In-game trading traps"),
    TEENS_11_15("Teens & Gamers", "11–15", "Discord nitro scams, Social media account takeovers, Gaming skin trading traps"),
    YOUNG_ADULTS_16_22("Young Adults & Students", "16–22", "Internship & Remote job scams, Scholarship traps, Student loan fraud, P2P payment scams"),
    PROFESSIONALS_23_40("Working Professionals", "23–40", "Salary account scams, Credit card fraud, Crypto rug pulls, Corporate wire transfer"),
    MID_ADULTS_41_60("Mid-Career Adults", "41–60", "Insurance claims scams, Health & medical fraud, Pension & retirement traps, Tax refund fraud"),
    SENIORS_61_PLUS("Seniors & Retirees", "61+", "Tech support impersonation, Grandparent voice clones, IRS/Medicare scams, Sweepstakes")
}

enum class TechLiteracy(val displayName: String) {
    BEGINNER("Beginner (Needs step-by-step guidance)"),
    AVERAGE("Average (Familiar with daily apps)"),
    EXPERT("Expert (Tech-savvy user)")
}

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val userIdentifier: String = "alex@safeguard.ai",
    val loginType: String = "EMAIL", // "EMAIL" or "MOBILE"
    val passwordHash: String = "password123",
    val userName: String = "Alex Vance",
    val occupation: String = "Software Developer",
    val demographicGroup: DemographicGroup = DemographicGroup.PROFESSIONALS_23_40,
    val techLiteracy: TechLiteracy = TechLiteracy.AVERAGE,
    val fviScore: Int = 680, // FVI Range 300..850
    val voiceCloneDefensePercent: Int = 85,
    val phishingSpottingPercent: Int = 78,
    val socialEngResiliencePercent: Int = 72,
    val completedModulesCount: Int = 6,
    val totalSimulationsPassed: Int = 12,
    val scamExperience: String = "Narrowly Avoided",
    val bankingHabits: String = "UPI, Credit Card, Internet Banking, Mobile Banking",
    val familyDetails: String = "Living with family",
    val financialGoals: String = "Wealth Growth & Security",
    val isLoggedIn: Boolean = false,
    // Scam Genome Psychological Vulnerability Traits
    val authorityBias: Int = 88,
    val urgencyBias: Int = 76,
    val greedBias: Int = 15,
    val curiosityBias: Int = 30,
    val trustInTechBias: Int = 82,
    val fearBias: Int = 42,
    val scamImmunityScore: Int = 74, // 0..100% Immunity Level
    val streakDays: Int = 5, // Daily Immunity Streak 🔥
    val availableXp: Int = 1450, // XP Balance for redeeming rewards
    val claimedRewardsJson: String = "[]" // JSON array of claimed rewards
)

data class RewardItem(
    val id: String,
    val title: String,
    val providerName: String,
    val category: String, // "ANTIVIRUS", "SCAM_TOOL", "HARDWARE", "PRIVACY", "TOP3_PERK"
    val xpCost: Int,
    val originalPriceText: String,
    val rewardValueText: String,
    val description: String,
    val highlightTag: String,
    val codePrefix: String,
    val isTop3Exclusive: Boolean = false,
    val iconEmoji: String = "🛡️",
    val redemptionUrl: String = "https://safeguard.ai/activate"
)

data class ClaimedReward(
    val id: String = java.util.UUID.randomUUID().toString(),
    val rewardId: String,
    val title: String,
    val providerName: String,
    val activationCode: String,
    val xpSpent: Int,
    val claimedTimestampMs: Long = System.currentTimeMillis(),
    val redemptionUrl: String = "https://safeguard.ai/activate",
    val instructions: String = "Copy your activation key and visit the partner portal to claim your antivirus or security offer."
)

@Entity(tableName = "fvi_history")
data class FviRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestampMs: Long = System.currentTimeMillis(),
    val deltaPoints: Int,
    val reason: String,
    val scoreAfter: Int
)

enum class SubTaskType {
    LESSON,          // Educational story / concept card
    SCENARIO,        // Spot the red flag / post analysis
    QUIZ,            // Interactive scenario test
    BOSS_CHALLENGE   // Live multi-step challenge
}

data class PathSubTask(
    val id: String,
    val title: String,
    val taskType: SubTaskType = SubTaskType.LESSON,
    val description: String,
    val storyBody: String = "",
    val question: String = "",
    val options: List<String> = emptyList(),
    val correctOptionIndex: Int = 0,
    val keyTakeaway: String = "",
    val pointsReward: Int = 15,
    val isCompleted: Boolean = false,
    val isUnlocked: Boolean = false
)

@Entity(tableName = "learning_modules")
data class LearningModule(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val targetDemographic: DemographicGroup,
    val description: String,
    val pointsReward: Int = 60,
    val isCompleted: Boolean = false,
    val isRecommended: Boolean = true,
    val trendingThreat: Boolean = false,
    val estMinutes: Int = 8,
    val completedSubTasksCount: Int = 0,
    val totalSubTasksCount: Int = 4,
    val subTasksJson: String = "",
    val storyHeadline: String = "",
    val storyBody: String = "",
    val keyTakeaway: String = "",
    val quizQuestion: String = "",
    val quizOption1: String = "",
    val quizOption2: String = "",
    val quizOption3: String = "",
    val correctOptionIndex: Int = 0
)

data class LeaderboardEntry(
    val id: String,
    val rank: Int,
    val userName: String,
    val occupation: String,
    val avatarEmoji: String,
    val demographicGroup: DemographicGroup,
    val fviScore: Int,
    val weeklyPoints: Int,
    val monthlyPoints: Int,
    val yearlyPoints: Int,
    val badgeName: String,
    val isCurrentUser: Boolean = false,
    val streakDays: Int = 3
)

@Entity(tableName = "threat_alerts")
data class ThreatAlert(
    @PrimaryKey val id: String,
    val title: String,
    val severity: String, // HIGH, MEDIUM, LOW
    val category: String,
    val dateText: String,
    val description: String,
    val actionText: String,
    val isRead: Boolean = false
)

data class RedFlag(
    val id: String,
    val snippetText: String,
    val explanation: String,
    val severity: String = "HIGH"
)

data class EmailScenario(
    val id: String,
    val senderName: String,
    val senderEmail: String,
    val subject: String,
    val date: String,
    val body: String,
    val redFlags: List<RedFlag>,
    val targetDemographic: DemographicGroup,
    val difficulty: String = "Medium"
)

data class DeepfakeScenario(
    val id: String,
    val title: String,
    val callerIdentity: String,
    val scenarioContext: String,
    val audioDurationSeconds: Int = 18,
    val waveformPoints: List<Float>,
    val spectralArtifactsScore: Int, // 0..100 (high = AI glitch detected)
    val pitchMonotonyScore: Int, // 0..100
    val latencyScore: Int, // 0..100
    val isAiGenerated: Boolean,
    val forensicExplanation: String,
    val targetDemographic: DemographicGroup
)

data class ScammerChoice(
    val text: String,
    val isSafeChoice: Boolean,
    val scoreDelta: Int,
    val feedback: String
)

data class ScammerChatMessage(
    val id: String,
    val sender: String, // "Scammer", "User", "System"
    val text: String,
    val timestamp: String,
    val isWarning: Boolean = false
)

data class ScammerChatScenario(
    val id: String,
    val title: String,
    val channelType: String, // "WhatsApp", "SMS", "Telegram", "Discord"
    val scammerPersona: String,
    val initialMessages: List<ScammerChatMessage>,
    val choices: List<ScammerChoice>,
    val targetDemographic: DemographicGroup
)

// Explainable Scam Replay Data Models
data class ScamReplayStep(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val biasTriggered: String, // e.g. "Urgency Bias (+15%)", "Authority Bias (+22%)"
    val iconType: String // "URGENCY", "AUTHORITY", "WARNING", "PAYLOAD", "LOSS"
)

data class ScamReplayData(
    val scenarioTitle: String,
    val scamType: String,
    val summary: String,
    val steps: List<ScamReplayStep>,
    val keyLesson: String
)

// Interactive Website Spotter Data Models
data class WebsiteHotspot(
    val id: String,
    val name: String,
    val sectionName: String, // e.g. "Domain Bar", "Security Badge", "Urgent Warning Banner", "Form Input", "Footer"
    val isRedFlag: Boolean,
    val explanation: String
)

data class WebsiteScenario(
    val id: String,
    val title: String,
    val siteName: String,
    val displayUrl: String,
    val isLegitimate: Boolean,
    val headerSubtitle: String,
    val targetDemographic: DemographicGroup,
    val hotspots: List<WebsiteHotspot>
)

