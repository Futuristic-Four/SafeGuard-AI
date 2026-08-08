package com.example.data.repository

import com.example.data.model.DemographicGroup
import com.example.data.model.LeaderboardEntry

enum class LeaderboardTimeframe(val label: String) {
    WEEK("This Week"),
    MONTH("This Month"),
    YEAR("This Year")
}

object LeaderboardData {
    fun getLeaderboard(
        userDemographic: DemographicGroup,
        timeframe: LeaderboardTimeframe,
        filterByAgeGroup: Boolean,
        currentUserFvi: Int,
        currentUserName: String = "Alex Vance"
    ): List<LeaderboardEntry> {
        val rawUsers = listOf(
            LeaderboardEntry(
                id = "user_1",
                rank = 0,
                userName = "Sophia_Tech",
                occupation = "Cybersecurity Analyst",
                avatarEmoji = "👩‍💻",
                demographicGroup = DemographicGroup.PROFESSIONALS_23_40,
                fviScore = 820,
                weeklyPoints = 540,
                monthlyPoints = 1850,
                yearlyPoints = 5200,
                badgeName = "Phishing Master 🛡️",
                streakDays = 14
            ),
            LeaderboardEntry(
                id = "user_2",
                rank = 0,
                userName = "Rohan_Dev",
                occupation = "Software Engineer",
                avatarEmoji = "👨‍💻",
                demographicGroup = DemographicGroup.YOUNG_ADULTS_16_22,
                fviScore = 790,
                weeklyPoints = 480,
                monthlyPoints = 1620,
                yearlyPoints = 4750,
                badgeName = "Reddit Traps Guardian 🚀",
                streakDays = 9
            ),
            LeaderboardEntry(
                id = "user_cur",
                rank = 0,
                userName = currentUserName,
                occupation = "Product Developer",
                avatarEmoji = "⚡",
                demographicGroup = userDemographic,
                fviScore = currentUserFvi,
                weeklyPoints = (currentUserFvi * 0.65).toInt(),
                monthlyPoints = (currentUserFvi * 2.1).toInt(),
                yearlyPoints = (currentUserFvi * 5.8).toInt(),
                badgeName = "Defense Strategist 🛡️",
                isCurrentUser = true,
                streakDays = 5
            ),
            LeaderboardEntry(
                id = "user_3",
                rank = 0,
                userName = "Marcus_Fintech",
                occupation = "Financial Advisor",
                avatarEmoji = "🏦",
                demographicGroup = DemographicGroup.PROFESSIONALS_23_40,
                fviScore = 765,
                weeklyPoints = 410,
                monthlyPoints = 1420,
                yearlyPoints = 4100,
                badgeName = "Crypto Shield 🪙",
                streakDays = 7
            ),
            LeaderboardEntry(
                id = "user_4",
                rank = 0,
                userName = "Grandma_Martha",
                occupation = "Retired Educator",
                avatarEmoji = "👵",
                demographicGroup = DemographicGroup.SENIORS_61_PLUS,
                fviScore = 750,
                weeklyPoints = 390,
                monthlyPoints = 1380,
                yearlyPoints = 3950,
                badgeName = "Voice Clone Sentinel 📞",
                streakDays = 12
            ),
            LeaderboardEntry(
                id = "user_5",
                rank = 0,
                userName = "GamerNinja_99",
                occupation = "High School Student",
                avatarEmoji = "🎮",
                demographicGroup = DemographicGroup.TEENS_11_15,
                fviScore = 730,
                weeklyPoints = 370,
                monthlyPoints = 1290,
                yearlyPoints = 3600,
                badgeName = "Discord Nitro Defender ⚡",
                streakDays = 4
            ),
            LeaderboardEntry(
                id = "user_6",
                rank = 0,
                userName = "David_Invest",
                occupation = "Project Director",
                avatarEmoji = "👔",
                demographicGroup = DemographicGroup.MID_ADULTS_41_60,
                fviScore = 710,
                weeklyPoints = 340,
                monthlyPoints = 1180,
                yearlyPoints = 3400,
                badgeName = "Insurance Fraud Sentinel 🏥",
                streakDays = 6
            ),
            LeaderboardEntry(
                id = "user_7",
                rank = 0,
                userName = "Shieldy_Kid_Leo",
                occupation = "Elementary Student",
                avatarEmoji = "🐻",
                demographicGroup = DemographicGroup.KIDS_5_10,
                fviScore = 690,
                weeklyPoints = 310,
                monthlyPoints = 1050,
                yearlyPoints = 2900,
                badgeName = "Robux Shield Hero 🎮",
                streakDays = 8
            ),
            LeaderboardEntry(
                id = "user_8",
                rank = 0,
                userName = "Elena_Design",
                occupation = "UI/UX Designer",
                avatarEmoji = "🎨",
                demographicGroup = DemographicGroup.PROFESSIONALS_23_40,
                fviScore = 675,
                weeklyPoints = 280,
                monthlyPoints = 990,
                yearlyPoints = 2800,
                badgeName = "Social Eng Defender 🔒",
                streakDays = 3
            )
        )

        // Filter by age demographic if selected
        val filtered = if (filterByAgeGroup) {
            rawUsers.filter { it.demographicGroup == userDemographic || it.isCurrentUser }
        } else {
            rawUsers
        }

        // Sort by selected timeframe points
        val sorted = when (timeframe) {
            LeaderboardTimeframe.WEEK -> filtered.sortedByDescending { it.weeklyPoints }
            LeaderboardTimeframe.MONTH -> filtered.sortedByDescending { it.monthlyPoints }
            LeaderboardTimeframe.YEAR -> filtered.sortedByDescending { it.yearlyPoints }
        }

        // Assign rank numbers
        return sorted.mapIndexed { index, entry ->
            entry.copy(rank = index + 1)
        }
    }
}
