package com.example.data.repository

import com.example.data.model.PathSubTask
import com.example.data.model.SubTaskType
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object SubTaskHelper {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val listType = Types.newParameterizedType(List::class.java, PathSubTask::class.java)
    private val adapter = moshi.adapter<List<PathSubTask>>(listType)

    fun toJson(tasks: List<PathSubTask>): String {
        return try {
            adapter.toJson(tasks)
        } catch (e: Exception) {
            ""
        }
    }

    fun parseJson(json: String): List<PathSubTask> {
        if (json.isBlank()) return emptyList()
        return try {
            adapter.fromJson(json) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Default sub-task generator for Reddit Scam Path
    fun getRedditScamPath(): List<PathSubTask> {
        return listOf(
            PathSubTask(
                id = "reddit_task_1",
                title = "Task 1: Reddit 'Mod' DMs & Verification Traps",
                taskType = SubTaskType.LESSON,
                description = "Learn how Reddit scammers impersonate subreddit moderators via DMs offering special flair or warning about fake bans.",
                storyBody = "You receive a Reddit chat message from 'u/Mod_Security_Bot_Official': 'ALERT: Your recent post on r/CryptoCurrency violated Rule 4. Click reddit-mod-appeal.org to verify your account within 12 hours or face permanent subreddit ban!'.\n\nReal Reddit moderators NEVER ask for login credentials or redirect you to third-party domains outside reddit.com!",
                keyTakeaway = "Always check the domain! Official Reddit moderator communications occur inside the subreddit's ModMail interface, never via private external links.",
                pointsReward = 15,
                isCompleted = false,
                isUnlocked = true
            ),
            PathSubTask(
                id = "reddit_task_2",
                title = "Task 2: The 'Free Token Airdrop' Comment Section Trap",
                taskType = SubTaskType.SCENARIO,
                description = "Analyze a trending Reddit post comment section filled with fake upvotes and bot accounts promoting a fraudulent wallet drainer link.",
                storyBody = "In a top Reddit post, a stickied-looking comment says: '🚀 Reddit is distributing 5,000 MOON tokens to all users active today! Connect wallet at reddit-moon-claim.app to claim!'. Below it, 20 accounts comment 'Worked for me! Got $300 instantly!'.\n\nAll 20 accounts were created on the same day with 1 karma! These are bot farms created to trick you into connecting your Web3 wallet to a drainer smart contract.",
                question = "What is the biggest red flag in this Reddit comment section?",
                options = listOf(
                    "Low account age and 1 karma on all praising bot comments.",
                    "The post was posted during evening hours.",
                    "The subreddit has over 100,000 members."
                ),
                correctOptionIndex = 0,
                keyTakeaway = "High upvotes and positive comments can be easily faked using bot networks. Check account age and karma before trusting any link!",
                pointsReward = 20,
                isCompleted = false,
                isUnlocked = false
            ),
            PathSubTask(
                id = "reddit_task_3",
                title = "Task 3: Reddit Karma & Account Age Signals Quiz",
                taskType = SubTaskType.QUIZ,
                description = "Test your skills on evaluating Reddit user profiles, karma metrics, and suspicious post histories.",
                storyBody = "A Reddit user 'u/Trusted_Trader_2026' messages you offering a 50% discount on Steam gift cards or tickets. You view their profile:\n• Account Age: 3 days\n• Total Karma: 4,120 (all from posting spam memes in karma-farming subs)\n• Post History: No organic community interactions.",
                question = "Should you proceed with buying gift cards from this Reddit user?",
                options = listOf(
                    "No! High karma from karma-farm subreddits on a 3-day old account is a classic scammer setup.",
                    "Yes! 4,000 karma means they are completely trustworthy.",
                    "Yes, provided they send a screenshot of the gift card."
                ),
                correctOptionIndex = 0,
                keyTakeaway = "Scammers buy or farm accounts in 'FreeKarma4You' subreddits to look legitimate. Look at post history quality, not just total karma!",
                pointsReward = 20,
                isCompleted = false,
                isUnlocked = false
            ),
            PathSubTask(
                id = "reddit_task_4",
                title = "Task 4: Boss Challenge - Live Reddit Phishing Bot Defense",
                taskType = SubTaskType.BOSS_CHALLENGE,
                description = "Defend yourself against a multi-step simulated Reddit phishing attack attempting to steal your credentials and session cookies.",
                storyBody = "BOSS CHALLENGE: A Reddit user posts an urgent notice: 'Reddit API error causing user data leak! Download the patch tool from r-patch-reddit.com now!'. Shortly after, you get a chat notification confirming the 'patch'.\n\nChoose your defense countermeasure:",
                question = "What is your immediate response to neutralize this Reddit threat?",
                options = listOf(
                    "Report the user and post to Reddit Security & Admins, and warn the subreddit community.",
                    "Download and test the patch tool in a web browser.",
                    "Reply to the user asking for their official Reddit employee ID."
                ),
                correctOptionIndex = 0,
                keyTakeaway = "Reporting phishing posts protects thousands of fellow redditors. Reddit admins communicate exclusively via official blog.reddit.com or in-app announcements.",
                pointsReward = 30,
                isCompleted = false,
                isUnlocked = false
            )
        )
    }

    fun getDiscordGamersPath(): List<PathSubTask> {
        return listOf(
            PathSubTask(
                id = "discord_task_1",
                title = "Task 1: Discord Nitro & Steam Gift Links",
                taskType = SubTaskType.LESSON,
                description = "Identify fake Discord Nitro giveaway DMs sent from compromised friend accounts.",
                storyBody = "Your friend 'PixelKnight' sends you a DM: 'Hey! Discord is giving 3 free months of Nitro! Grab it here: discord-gift-nitro.xyz!'.\n\nPixelKnight's account was hijacked because they scanned a QR code on a malicious website. Official Discord links use '.discord.com' or '.discord.gg'.",
                keyTakeaway = "Never scan Discord QR codes on third-party sites or click unusual gift links from friends without verifying over phone or voice chat.",
                pointsReward = 15,
                isCompleted = true,
                isUnlocked = true
            ),
            PathSubTask(
                id = "discord_task_2",
                title = "Task 2: Steam & Roblox Skin Trading OAuth Traps",
                taskType = SubTaskType.SCENARIO,
                description = "Analyze a fake trading site demanding 'Log in with Steam/Discord' OAuth permission.",
                storyBody = "A trader on Discord asks you to trade a rare CS2 skin or Roblox item on 'skins-trade-zone.com'. When you click 'Sign in with Steam', a popup window opens asking for your 2FA Guard code.",
                question = "How can you tell if the Steam login popup is fake?",
                options = listOf(
                    "Try dragging the popup window outside the main browser window—if it stays inside, it's a fake HTML canvas trap!",
                    "If the background color is dark gray, it's real.",
                    "If it asks for 2FA, it's always legitimate."
                ),
                correctOptionIndex = 0,
                keyTakeaway = "Browser-in-the-browser (BitB) attacks create fake popups inside the web page. Real browser popups can be dragged outside the main window!",
                pointsReward = 20,
                isCompleted = false,
                isUnlocked = true
            ),
            PathSubTask(
                id = "discord_task_3",
                title = "Task 3: Spotting Discord Token Stealer Malware",
                taskType = SubTaskType.QUIZ,
                description = "Learn how malicious game testing '.exe' or '.zip' files steal Discord authorization tokens.",
                storyBody = "A user on Discord asks you: 'Hey, I'm developing an indie game in Unity! Can you test my beta build? Here is the zip file: PlayGame_Beta.zip'.",
                question = "What should you do before running game executables from strangers?",
                options = listOf(
                    "Never run unverified .exe files from strangers; they often contain Discord token grabbers and keyloggers!",
                    "Run it immediately to help the developer.",
                    "Disable your antivirus so the game runs faster."
                ),
                correctOptionIndex = 0,
                keyTakeaway = "Indie game beta test files are a top vehicle for token grabbers. Always use sandbox environments or decline unverified executables.",
                pointsReward = 20,
                isCompleted = false,
                isUnlocked = false
            ),
            PathSubTask(
                id = "discord_task_4",
                title = "Task 4: Boss Challenge - Account Recovery & Token Reset",
                taskType = SubTaskType.BOSS_CHALLENGE,
                description = "Navigate an active account recovery scenario if an account is compromised.",
                storyBody = "BOSS CHALLENGE: You accidentally entered your credentials on a fake site. Your account starts auto-DMing all your servers!",
                question = "What is the single most effective action to immediately invalidate the thief's token?",
                options = listOf(
                    "Change your Discord password immediately and enable 2FA—this resets your secret authorization token!",
                    "Delete the Discord app and reinstall it.",
                    "Message all your friends individually telling them to ignore the link."
                ),
                correctOptionIndex = 0,
                keyTakeaway = "Changing your account password immediately revokes all active session tokens across all devices!",
                pointsReward = 30,
                isCompleted = false,
                isUnlocked = false
            )
        )
    }

    fun getGenericPathForModule(title: String, category: String): List<PathSubTask> {
        return listOf(
            PathSubTask(
                id = "${title.hashCode()}_task_1",
                title = "Task 1: Core $category Defense Principles",
                taskType = SubTaskType.LESSON,
                description = "Master the primary psychological triggers used by scammers in $category scenarios.",
                storyBody = "Scammers rely on urgency, panic, or unexpected rewards to bypass your rational evaluation. When dealing with $title, always take a 30-second pause to inspect official verification channels.",
                keyTakeaway = "Urgency is the scammer's primary tool. Slow down and verify independently.",
                pointsReward = 15,
                isCompleted = false,
                isUnlocked = true
            ),
            PathSubTask(
                id = "${title.hashCode()}_task_2",
                title = "Task 2: Interactive Scenario - Spotting Red Flags",
                taskType = SubTaskType.SCENARIO,
                description = "Analyze a real-world attempt and isolate suspicious communication markers.",
                storyBody = "You receive an unexpected message regarding $title requesting immediate payment or verification via an unfamiliar link.",
                question = "What is the safest protocol when receiving an unverified request?",
                options = listOf(
                    "Contact the official institution directly via their verified website or phone number.",
                    "Click the link provided in the message to inspect it.",
                    "Reply asking if the message is authentic."
                ),
                correctOptionIndex = 0,
                keyTakeaway = "Never use contact information provided inside suspicious messages. Look up official numbers independently.",
                pointsReward = 20,
                isCompleted = false,
                isUnlocked = false
            ),
            PathSubTask(
                id = "${title.hashCode()}_task_3",
                title = "Task 3: Threat Verification Quiz",
                taskType = SubTaskType.QUIZ,
                description = "Test your knowledge on domain security, payment methods, and privacy rules.",
                storyBody = "A sender claims to represent a legitimate organization but demands payment via gift cards, wire transfer, or cryptocurrency.",
                question = "Are legitimate organizations permitted to require payment via gift cards?",
                options = listOf(
                    "No! Gift cards are non-refundable and untraceable—demand for gift card payment is 100% a scam.",
                    "Yes, many government agencies use gift cards for convenience.",
                    "Only during official holiday seasons."
                ),
                correctOptionIndex = 0,
                keyTakeaway = "No official government or financial agency ever requests payment in gift cards or crypto.",
                pointsReward = 20,
                isCompleted = false,
                isUnlocked = false
            ),
            PathSubTask(
                id = "${title.hashCode()}_task_4",
                title = "Task 4: Boss Challenge - Counter-defense",
                taskType = SubTaskType.BOSS_CHALLENGE,
                description = "Execute a full defense sequence to secure your accounts and report the perpetrator.",
                storyBody = "You have identified an active threat targeting your account or financial credentials.",
                question = "Select the optimal 3-step defense action:",
                options = listOf(
                    "1. Do not engage/click 2. Report threat to platform 3. Update security credentials & 2FA",
                    "1. Reply angrily 2. Delete the app 3. Wait 24 hours",
                    "1. Send money to unlock account 2. File a complaint later"
                ),
                correctOptionIndex = 0,
                keyTakeaway = "Reporting and locking down credentials stops perpetrators from spreading attacks to others.",
                pointsReward = 30,
                isCompleted = false,
                isUnlocked = false
            )
        )
    }
}
