package com.example.data.repository

import android.content.Context
import com.example.data.db.SafeGuardDatabase
import com.example.data.model.DemographicGroup
import com.example.data.model.FviRecord
import com.example.data.model.LearningModule
import com.example.data.model.TechLiteracy
import com.example.data.model.ThreatAlert
import com.example.data.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class SafeGuardRepository(context: Context) {
    private val db = SafeGuardDatabase.getDatabase(context)
    private val dao = db.dao()

    val userProfile: Flow<UserProfile?> = dao.getUserProfile()
    val fviHistory: Flow<List<FviRecord>> = dao.getFviHistory()
    val modules: Flow<List<LearningModule>> = dao.getAllModules()
    val threatAlerts: Flow<List<ThreatAlert>> = dao.getThreatAlerts()

    suspend fun initializeSeedData() = withContext(Dispatchers.IO) {
        val existingProfile = dao.getUserProfile().firstOrNull()
        if (existingProfile == null) {
            val defaultProfile = UserProfile(
                id = 1,
                userName = "Alex Vance",
                occupation = "Software Engineer",
                demographicGroup = DemographicGroup.PROFESSIONALS_23_40,
                techLiteracy = TechLiteracy.AVERAGE,
                fviScore = 680,
                isLoggedIn = false
            )
            dao.insertOrUpdateUserProfile(defaultProfile)

            // Initial FVI history point
            dao.insertFviRecord(
                FviRecord(
                    deltaPoints = 0,
                    reason = "Initial FVI Baseline Assessment",
                    scoreAfter = 680
                )
            )
        }

        val existingModules = dao.getAllModules().firstOrNull()
        if (existingModules.isNullOrEmpty()) {
            dao.insertModules(InitialData.sampleModules)
        } else {
            // Re-seed modules to ensure updated age group modules are inserted
            dao.insertModules(InitialData.sampleModules)
        }

        val existingAlerts = dao.getThreatAlerts().firstOrNull()
        if (existingAlerts.isNullOrEmpty()) {
            dao.insertThreatAlerts(InitialData.sampleThreatAlerts)
        }
    }

    suspend fun saveUserProfile(
        identifier: String = "alex@safeguard.ai",
        loginType: String = "EMAIL",
        password: String = "password123",
        name: String,
        occupation: String,
        demographicGroup: DemographicGroup,
        techLiteracy: TechLiteracy,
        scamExperience: String,
        bankingHabits: String,
        familyDetails: String,
        financialGoals: String
    ) = withContext(Dispatchers.IO) {
        val baseScore = when (techLiteracy) {
            TechLiteracy.BEGINNER -> 520
            TechLiteracy.AVERAGE -> 660
            TechLiteracy.EXPERT -> 760
        }

        val current = dao.getUserProfile().firstOrNull()
        val updated = (current ?: UserProfile()).copy(
            userIdentifier = identifier.ifBlank { "alex@safeguard.ai" },
            loginType = loginType,
            passwordHash = password.ifBlank { "password123" },
            userName = if (name.isNotBlank()) name else "Alex Vance",
            occupation = if (occupation.isNotBlank()) occupation else "General User",
            demographicGroup = demographicGroup,
            techLiteracy = techLiteracy,
            scamExperience = scamExperience,
            bankingHabits = bankingHabits,
            familyDetails = familyDetails,
            financialGoals = financialGoals,
            fviScore = baseScore,
            isLoggedIn = true
        )
        dao.insertOrUpdateUserProfile(updated)

        dao.insertFviRecord(
            FviRecord(
                deltaPoints = 0,
                reason = "Sign Up Complete: Personalized for ${demographicGroup.displayName}",
                scoreAfter = baseScore
            )
        )
    }

    suspend fun loginUser(
        identifier: String,
        password: String,
        loginType: String
    ) = withContext(Dispatchers.IO) {
        val current = dao.getUserProfile().firstOrNull()
        val finalIdentifier = identifier.ifBlank { current?.userIdentifier ?: "alex@safeguard.ai" }
        val finalPassword = password.ifBlank { current?.passwordHash ?: "password123" }

        if (current != null) {
            // Restore previous profile and update session without overwriting or asking for user details!
            val updated = current.copy(
                userIdentifier = finalIdentifier,
                loginType = loginType,
                passwordHash = finalPassword,
                isLoggedIn = true
            )
            dao.updateUserProfile(updated)

            dao.insertFviRecord(
                FviRecord(
                    deltaPoints = 0,
                    reason = "Log In Successful: Restored session for ${current.userName}",
                    scoreAfter = current.fviScore
                )
            )
        } else {
            // If no profile exists, create a baseline user with default values and set logged in
            val defaultProfile = UserProfile(
                userIdentifier = finalIdentifier,
                loginType = loginType,
                passwordHash = finalPassword,
                userName = if (loginType == "EMAIL" && finalIdentifier.contains("@")) finalIdentifier.substringBefore("@") else "SafeGuard User",
                isLoggedIn = true
            )
            dao.insertOrUpdateUserProfile(defaultProfile)

            dao.insertFviRecord(
                FviRecord(
                    deltaPoints = 0,
                    reason = "Log In Successful: Baseline profile loaded",
                    scoreAfter = 680
                )
            )
        }
    }

    suspend fun logoutUser() = withContext(Dispatchers.IO) {
        val current = dao.getUserProfile().firstOrNull()
        if (current != null) {
            val updated = current.copy(isLoggedIn = false)
            dao.updateUserProfile(updated)
        }
    }

    suspend fun updateDemographicProfile(
        demographicGroup: DemographicGroup,
        techLiteracy: TechLiteracy
    ) = withContext(Dispatchers.IO) {
        val currentProfile = dao.getUserProfile().firstOrNull() ?: return@withContext
        val baseScore = when (techLiteracy) {
            TechLiteracy.BEGINNER -> 520
            TechLiteracy.AVERAGE -> 660
            TechLiteracy.EXPERT -> 760
        }

        val updated = currentProfile.copy(
            demographicGroup = demographicGroup,
            techLiteracy = techLiteracy,
            fviScore = baseScore
        )
        dao.updateUserProfile(updated)

        dao.insertFviRecord(
            FviRecord(
                deltaPoints = 0,
                reason = "Recalibrated FVI Profile to ${demographicGroup.displayName}",
                scoreAfter = baseScore
            )
        )
    }

    suspend fun updateFviScore(deltaPoints: Int, reason: String) = withContext(Dispatchers.IO) {
        val currentProfile = dao.getUserProfile().firstOrNull() ?: return@withContext
        val newScore = (currentProfile.fviScore + deltaPoints).coerceIn(300, 850)
        
        // Dynamic Scam Genome calculations: as score improves, vulnerabilities drop and immunity rises!
        val newImmunity = ((newScore - 300) * 100 / 550).coerceIn(10, 99)
        val reduction = if (deltaPoints > 0) 2 else -1
        
        val newProfile = currentProfile.copy(
            fviScore = newScore,
            scamImmunityScore = newImmunity,
            urgencyBias = (currentProfile.urgencyBias - reduction).coerceIn(5, 95),
            authorityBias = (currentProfile.authorityBias - reduction).coerceIn(5, 95),
            fearBias = (currentProfile.fearBias - reduction).coerceIn(5, 95),
            trustInTechBias = (currentProfile.trustInTechBias - reduction).coerceIn(5, 95),
            completedModulesCount = if (deltaPoints > 0) currentProfile.completedModulesCount + 1 else currentProfile.completedModulesCount
        )
        
        dao.updateUserProfile(newProfile)

        dao.insertFviRecord(
            FviRecord(
                deltaPoints = deltaPoints,
                reason = reason,
                scoreAfter = newScore
            )
        )
    }

    suspend fun recordSimulationResult(isSuccess: Boolean, scamCategory: String) = withContext(Dispatchers.IO) {
        val currentProfile = dao.getUserProfile().firstOrNull() ?: return@withContext
        if (isSuccess) {
            val delta = 25
            val updatedPassed = currentProfile.totalSimulationsPassed + 1
            val newImmunity = (currentProfile.scamImmunityScore + 3).coerceIn(10, 99)
            
            val updated = currentProfile.copy(
                fviScore = (currentProfile.fviScore + delta).coerceIn(300, 850),
                totalSimulationsPassed = updatedPassed,
                scamImmunityScore = newImmunity,
                urgencyBias = (currentProfile.urgencyBias - 4).coerceIn(5, 95),
                authorityBias = (currentProfile.authorityBias - 3).coerceIn(5, 95),
                trustInTechBias = (currentProfile.trustInTechBias - 3).coerceIn(5, 95)
            )
            dao.updateUserProfile(updated)
            dao.insertFviRecord(FviRecord(deltaPoints = delta, reason = "Passed Simulation: $scamCategory", scoreAfter = updated.fviScore))
        } else {
            val delta = -15
            val updated = currentProfile.copy(
                fviScore = (currentProfile.fviScore + delta).coerceIn(300, 850),
                urgencyBias = (currentProfile.urgencyBias + 3).coerceIn(5, 95),
                authorityBias = (currentProfile.authorityBias + 4).coerceIn(5, 95)
            )
            dao.updateUserProfile(updated)
            dao.insertFviRecord(FviRecord(deltaPoints = delta, reason = "Failed Simulation: $scamCategory", scoreAfter = updated.fviScore))
        }
    }

    suspend fun completeModule(moduleId: String, points: Int) = withContext(Dispatchers.IO) {
        dao.updateModuleCompletion(moduleId, true)
        val module = dao.getAllModules().firstOrNull()?.find { it.id == moduleId }
        val title = module?.title ?: "Learning Module"
        updateFviScore(points, "Completed: $title")
    }

    suspend fun completeSubTask(moduleId: String, taskId: String, points: Int) = withContext(Dispatchers.IO) {
        val modules = dao.getAllModules().firstOrNull() ?: return@withContext
        val module = modules.find { it.id == moduleId } ?: return@withContext

        var tasks = SubTaskHelper.parseJson(module.subTasksJson)
        if (tasks.isEmpty()) {
            tasks = if (module.id.contains("reddit")) SubTaskHelper.getRedditScamPath()
            else if (module.id.contains("teens") || module.id.contains("discord")) SubTaskHelper.getDiscordGamersPath()
            else SubTaskHelper.getGenericPathForModule(module.title, module.category)
        }

        val updatedTasks = tasks.mapIndexed { index, task ->
            if (task.id == taskId) {
                task.copy(isCompleted = true)
            } else if (index > 0 && tasks[index - 1].id == taskId) {
                // Unlock the next node in the learning path
                task.copy(isUnlocked = true)
            } else {
                task
            }
        }

        val completedCount = updatedTasks.count { it.isCompleted }
        val isAllCompleted = completedCount == updatedTasks.size
        val newJson = SubTaskHelper.toJson(updatedTasks)

        val updatedModule = module.copy(
            completedSubTasksCount = completedCount,
            totalSubTasksCount = updatedTasks.size,
            subTasksJson = newJson,
            isCompleted = isAllCompleted
        )

        dao.insertModules(listOf(updatedModule))
        val taskTitle = tasks.find { it.id == taskId }?.title ?: "Path Task"
        updateFviScore(points, "Completed Path Task: $taskTitle")
    }

    suspend fun markAlertRead(alertId: String) = withContext(Dispatchers.IO) {
        dao.markAlertAsRead(alertId)
    }

    // AI Threat Scanner analysis (Uses intelligent multi-vector Scam Genome security engine)
    fun analyzeSuspiciousContent(input: String): ScannerResult {
        val text = input.lowercase()
        var riskScore = 15
        val flags = mutableListOf<String>()
        var threatCategory = "General Text Inspection"

        // 1. URL, Typosquat & Domain Spoofing Detection
        if (text.contains("http:") || text.contains("bit.ly") || text.contains("tinyurl") ||
            text.contains("is.gd") || text.contains("t.co") || text.contains("-verify") ||
            text.contains("login-") || text.contains(".xyz") || text.contains(".top") ||
            text.contains(".click") || text.contains(".site") || text.contains("paypa1") ||
            text.contains("bank-sec") || text.contains("01-update") || text.matches(Regex(".*\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}.*"))) {
            riskScore += 35
            threatCategory = "Malicious URL / Domain Spoofing"
            flags.add("🌐 Domain Spoofing / Typosquatting / Unencrypted URL Detected")
        }

        // 2. High Pressure Coercion & Law Enforcement Threats
        if (text.contains("urgent") || text.contains("immediately") || text.contains("15 minutes") ||
            text.contains("suspended") || text.contains("arrest") || text.contains("police") ||
            text.contains("court") || text.contains("customs") || text.contains("electricity disconnect") ||
            text.contains("sim blocked") || text.contains("warrant") || text.contains("legal action")) {
            riskScore += 30
            if (threatCategory == "General Text Inspection") threatCategory = "Coercive Social Engineering / Authority Fraud"
            flags.add("⚠️ High-Pressure Urgency & Threat Coercion Language")
        }

        // 3. Payment Traps (UPI, QR Code, P2P, Gift Cards)
        if (text.contains("zelle") || text.contains("venmo") || text.contains("gift card") ||
            text.contains("upi pin") || text.contains("scan qr") || text.contains("receive money enter pin") ||
            text.contains("crypto") || text.contains("wire transfer") || text.contains("overpayment refund")) {
            riskScore += 35
            threatCategory = "UPI / P2P Payment Fraud Trap"
            flags.add("💳 Payment Trap Detected (Demanding PIN/QR scan or Non-refundable P2P transfer)")
        }

        // 4. Deepfake Voice / Emergency Impersonation Indicators
        if (text.contains("voice clone") || text.contains("accident") || text.contains("kidnapped") ||
            text.contains("hospital emergency") || text.contains("crying voice") || text.contains("bail money")) {
            riskScore += 35
            threatCategory = "Deepfake Voice / Emergency Family Scam"
            flags.add("🎙️ Deepfake Voice / Emergency Impersonation Fraud Pattern")
        }

        // 5. Credential Harvesting Traps
        if (text.contains("ssn") || text.contains("password") || text.contains("otp") ||
            text.contains("pin") || text.contains("cvv") || text.contains("aadhaar") || text.contains("bank login")) {
            riskScore += 25
            flags.add("🔑 Credential & OTP Harvesting Attempt")
        }

        riskScore = riskScore.coerceIn(10, 99)

        val verdict = when {
            riskScore >= 70 -> "HIGH RISK SCAM DETECTED"
            riskScore >= 40 -> "SUSPICIOUS THREAT VECTOR"
            else -> "LOW RISK / LIKELY SAFE CONTENT"
        }

        val recommendations = when {
            riskScore >= 70 -> listOf(
                "❌ Do NOT click any links, scan QR codes, or reply to this sender.",
                "📞 Independently contact the official institution using a verified phone number.",
                "🛡️ Block sender immediately and report to cyber crime portal or bank fraud desk."
            )
            riskScore >= 40 -> listOf(
                "🔎 Cross-check sender email address domain or phone number carefully.",
                "🔑 Remember: Legitimate banks NEVER ask for UPI PINs or OTPs to credit money.",
                "⚠️ Verify via official banking app directly rather than provided links."
            )
            else -> listOf(
                "✅ No immediate malicious scam signatures detected.",
                "ℹ️ Always remain vigilant before opening unknown email attachments."
            )
        }

        return ScannerResult(
            riskScore = riskScore,
            verdict = verdict,
            threatCategory = threatCategory,
            detectedFlags = if (flags.isEmpty()) listOf("No immediate malicious heuristics flagged.") else flags,
            recommendations = recommendations
        )
    }

    suspend fun redeemReward(reward: com.example.data.model.RewardItem, generatedCode: String): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        val current = dao.getUserProfile().firstOrNull() ?: return@withContext Pair(false, "User profile not found")
        
        if (!reward.isTop3Exclusive && current.availableXp < reward.xpCost) {
            return@withContext Pair(false, "Insufficient XP. You need ${reward.xpCost} XP to redeem this reward.")
        }

        val existingList = parseClaimedRewards(current.claimedRewardsJson).toMutableList()
        val newClaimedReward = com.example.data.model.ClaimedReward(
            rewardId = reward.id,
            title = reward.title,
            providerName = reward.providerName,
            activationCode = generatedCode,
            xpSpent = reward.xpCost,
            redemptionUrl = reward.redemptionUrl,
            instructions = "Copy your license code below and visit ${reward.providerName}'s official activation page."
        )

        existingList.add(0, newClaimedReward)
        val updatedJson = encodeClaimedRewards(existingList)
        val newAvailableXp = if (reward.isTop3Exclusive) current.availableXp else (current.availableXp - reward.xpCost).coerceAtLeast(0)

        val updatedProfile = current.copy(
            availableXp = newAvailableXp,
            claimedRewardsJson = updatedJson
        )

        dao.updateUserProfile(updatedProfile)
        Pair(true, "Successfully redeemed ${reward.title}!")
    }

    private fun parseClaimedRewards(json: String): List<com.example.data.model.ClaimedReward> {
        if (json.isBlank() || json == "[]") return emptyList()
        return try {
            val array = org.json.JSONArray(json)
            val list = mutableListOf<com.example.data.model.ClaimedReward>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    com.example.data.model.ClaimedReward(
                        id = obj.optString("id", java.util.UUID.randomUUID().toString()),
                        rewardId = obj.optString("rewardId", ""),
                        title = obj.optString("title", ""),
                        providerName = obj.optString("providerName", ""),
                        activationCode = obj.optString("activationCode", ""),
                        xpSpent = obj.optInt("xpSpent", 0),
                        claimedTimestampMs = obj.optLong("claimedTimestampMs", System.currentTimeMillis()),
                        redemptionUrl = obj.optString("redemptionUrl", "https://safeguard.ai/activate"),
                        instructions = obj.optString("instructions", "Copy license code to redeem.")
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun encodeClaimedRewards(list: List<com.example.data.model.ClaimedReward>): String {
        val array = org.json.JSONArray()
        for (item in list) {
            val obj = org.json.JSONObject()
            obj.put("id", item.id)
            obj.put("rewardId", item.rewardId)
            obj.put("title", item.title)
            obj.put("providerName", item.providerName)
            obj.put("activationCode", item.activationCode)
            obj.put("xpSpent", item.xpSpent)
            obj.put("claimedTimestampMs", item.claimedTimestampMs)
            obj.put("redemptionUrl", item.redemptionUrl)
            obj.put("instructions", item.instructions)
            array.put(obj)
        }
        return array.toString()
    }
}

data class ScannerResult(
    val riskScore: Int,
    val verdict: String,
    val threatCategory: String = "General Threat Inspection",
    val detectedFlags: List<String>,
    val recommendations: List<String>
)
