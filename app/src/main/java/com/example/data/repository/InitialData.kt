package com.example.data.repository

import com.example.data.model.DeepfakeScenario
import com.example.data.model.DemographicGroup
import com.example.data.model.EmailScenario
import com.example.data.model.LearningModule
import com.example.data.model.RedFlag
import com.example.data.model.ScamReplayData
import com.example.data.model.ScamReplayStep
import com.example.data.model.ScammerChatMessage
import com.example.data.model.ScammerChatScenario
import com.example.data.model.ScammerChoice
import com.example.data.model.ThreatAlert
import com.example.data.model.WebsiteHotspot
import com.example.data.model.WebsiteScenario

object InitialData {
    val sampleModules = listOf(
        // 5-10 YEARS: KIDS
        LearningModule(
            id = "mod_kids_1",
            title = "Free Robux & In-Game Gem Traps!",
            category = "Game Safety",
            targetDemographic = DemographicGroup.KIDS_5_10,
            description = "Learn how Shieldy Bear spots fake pop-ups promising 10,000 free gems or Robux if you type your secret password!",
            pointsReward = 30,
            isCompleted = false,
            isRecommended = true,
            trendingThreat = true,
            estMinutes = 3,
            storyHeadline = "Shieldy Bear's Secret Roblox Mission 🐻🎮",
            storyBody = "Shieldy Bear was playing his favorite game when a bright yellow popup appeared: 'CONGRATS! You won 10,000 Free Robux! Just type your account password and your mom's credit card!'. Shieldy Bear stopped! Real game companies NEVER ask for your password or credit card for free gifts.",
            keyTakeaway = "Rule #1: Never share passwords or adult credit cards for free game coins!",
            quizQuestion = "A popup promises 10,000 free game gems if you give your password. What should you do?",
            quizOption1 = "Ask an adult first and close the popup!",
            quizOption2 = "Type your secret password quickly!",
            quizOption3 = "Give your friend's password instead!",
            correctOptionIndex = 0
        ),
        LearningModule(
            id = "mod_kids_2",
            title = "Secret Password Power Shield 🛡️",
            category = "Password Security",
            targetDemographic = DemographicGroup.KIDS_5_10,
            description = "Build a super-strong pet password with Cyber Pup and learn why sharing with schoolmates loses your game items!",
            pointsReward = 25,
            isCompleted = true,
            isRecommended = true,
            trendingThreat = false,
            estMinutes = 4,
            storyHeadline = "Cyber Pup's Golden Password Lock 🐶🔐",
            storyBody = "Cyber Pup created a password using his favorite food and lucky number: 'SuperPizza#99!'. His friend asked to log in to see his pet skins. Cyber Pup said 'My password is my secret shield! If I give it away, my pets might disappear!'.",
            keyTakeaway = "Your password is like your house key. Only you and your parents should know it!",
            quizQuestion = "Who is allowed to know your game password?",
            quizOption1 = "Only you and your parents/guardians!",
            quizOption2 = "All your school classmates!",
            quizOption3 = "Anyone in the game chat!",
            correctOptionIndex = 0
        ),

        // 11-15 YEARS: TEENS
        LearningModule(
            id = "mod_teens_1",
            title = "Discord Nitro & Roblox Skin Trading Hijacks",
            category = "Social Media & Gaming",
            targetDemographic = DemographicGroup.TEENS_11_15,
            description = "Uncover fake Steam bot messages, malicious OAuth login links, and hijacked friend accounts offering free Nitro.",
            pointsReward = 30,
            isCompleted = false,
            isRecommended = true,
            trendingThreat = true,
            estMinutes = 5,
            storyHeadline = "Level Up: Spotting Discord Nitro Scams 🎮⚡",
            storyBody = "Your friend 'GamerPro_99' sends a Discord DM: 'Dude! Discord is giving away 3 months of free Nitro! Click discord-gift-nitro-claim.xyz to verify!'. But GamerPro_99's account was hijacked by a phishing bot! Official Discord links ALWAYS end in '.discord.com' or '.discord.gg'.",
            keyTakeaway = "Check the domain link carefully! Fake giveaway links steal your session tokens.",
            quizQuestion = "Your friend DMs a link 'discord-free-nitro-claim.xyz'. How do you verify?",
            quizOption1 = "Check official Discord app notices; domain is fake!",
            quizOption2 = "Click and enter your login immediately!",
            quizOption3 = "Forward it to 5 other servers!",
            correctOptionIndex = 0
        ),
        LearningModule(
            id = "mod_teens_2",
            title = "Instagram & TikTok Account Takeover Traps",
            category = "Account Defense",
            targetDemographic = DemographicGroup.TEENS_11_15,
            description = "Identify fake Copyright Violation DMs and 'Verify your Blue Badge' phishing forms targeting teenage creators.",
            pointsReward = 25,
            isCompleted = false,
            isRecommended = true,
            trendingThreat = false,
            estMinutes = 5,
            storyHeadline = "The Fake Copyright Strike Trap 📱⚠️",
            storyBody = "You post a short video and receive a DM from 'InstaSupport_Official': 'Your post violated trademark #4891. Verify account ownership in 24 hours at instacopyright-appeal.com or account will be permanently deleted!'. Instagram NEVER sends copyright notices via direct chat DMs!",
            keyTakeaway = "Real platforms communicate via official app notification settings, never DMs.",
            quizQuestion = "A DM claims your account will be deleted in 24 hours unless you click a link. What is this?",
            quizOption1 = "A classic account takeover phishing panic trap!",
            quizOption2 = "A real message from official staff!",
            quizOption3 = "An automatic system update!",
            correctOptionIndex = 0
        ),

        // 16-22 YEARS: YOUNG ADULTS
        LearningModule(
            id = "mod_young_1",
            title = "Internship, Remote Job & Scholarship Scams",
            category = "Career & Financial",
            targetDemographic = DemographicGroup.YOUNG_ADULTS_16_22,
            description = "Detect fake check-cashing equipment traps, Telegram interviews, and upfront scholarship processing fee extortion.",
            pointsReward = 35,
            isCompleted = false,
            isRecommended = true,
            trendingThreat = true,
            estMinutes = 6,
            storyHeadline = "The 'Work From Home' Check Cashing Trap 💼💸",
            storyBody = "Maya applied for a remote data entry internship. After a brief interview on Telegram, they emailed her a $2,500 check: 'Deposit this check and wire $2,000 to our supplier for laptop setup equipment'. 3 days later, the check bounced, and Maya lost $2,000 from her own checking account!",
            keyTakeaway = "Legitimate employers NEVER ask you to deposit a check and wire back money for equipment.",
            quizQuestion = "An employer sends you a check and asks you to wire money back for equipment. Is this safe?",
            quizOption1 = "NO! Fake check scam — you will lose all wired money!",
            quizOption2 = "YES! Standard remote onboarding procedure.",
            quizOption3 = "YES! As long as the check prints clearly.",
            correctOptionIndex = 0
        ),
        LearningModule(
            id = "mod_young_2",
            title = "P2P Payment Traps: Zelle, Venmo & UPI Scams",
            category = "Peer-to-Peer Scams",
            targetDemographic = DemographicGroup.YOUNG_ADULTS_16_22,
            description = "Master defense against buyer overpayments, accidental transfer chargeback extortion, and QR code pay traps.",
            pointsReward = 25,
            isCompleted = true,
            isRecommended = true,
            trendingThreat = true,
            estMinutes = 4,
            storyHeadline = "The 'Accidental Zelle Transfer' Extortion 📱💸",
            storyBody = "Sam received $300 on Venmo from a stranger, followed by a message: 'OMG so sorry! I meant to send money to my sister! Please send $300 back!'. If Sam sends $300 back, the scammer cancels the original transfer using a stolen credit card, leaving Sam down $300!",
            keyTakeaway = "Never send money back directly. Tell the sender to request a refund through official app support.",
            quizQuestion = "A stranger accidentally sends you money on a payment app and asks you to send it back. What should you do?",
            quizOption1 = "Tell them to contact customer support; do NOT send money back!",
            quizOption2 = "Send the money back immediately out of kindness!",
            quizOption3 = "Send half the money back!",
            correctOptionIndex = 0
        ),

        // 23-40 YEARS: PROFESSIONALS
        LearningModule(
            id = "mod_prof_1",
            title = "Salary Account Scams & Credit Card Fraud",
            category = "Corporate Wire & Card Safety",
            targetDemographic = DemographicGroup.PROFESSIONALS_23_40,
            description = "Identify fake salary account audit notifications, credit card limit enhancement traps, and unauthorized OTP requests.",
            pointsReward = 30,
            isCompleted = true,
            isRecommended = true,
            trendingThreat = true,
            estMinutes = 6,
            storyHeadline = "Salary Account Audit Phishing & Card Traps 💳🏛️",
            storyBody = "An email appearing to come from your corporate HR asks you to update salary account direct deposit details due to a bank merger. The link redirects to a cloned banking portal that captures your online banking credentials and 2FA SMS tokens.",
            keyTakeaway = "Always verify payroll routing changes via internal corporate intranet or face-to-face HR confirmation.",
            quizQuestion = "How should you handle an email asking to change direct deposit routing before payday?",
            quizOption1 = "Independently contact HR/Payroll via official internal directory!",
            quizOption2 = "Click the link and re-enter your banking login!",
            quizOption3 = "Reply with your credit card details instead!",
            correctOptionIndex = 0
        ),
        LearningModule(
            id = "mod_prof_2",
            title = "Fake High-Yield Crypto & Investment App Traps",
            category = "Wealth Protection",
            targetDemographic = DemographicGroup.PROFESSIONALS_23_40,
            description = "Detect fake trading platforms promising 3% daily guaranteed returns and pig butchering messaging lures.",
            pointsReward = 35,
            isCompleted = false,
            isRecommended = true,
            trendingThreat = true,
            estMinutes = 6,
            storyHeadline = "The 3% Daily Yield Trading Platform Trap 📈⚠️",
            storyBody = "Alex met a connection on LinkedIn who shared screenshots of massive guaranteed profits on an exclusive AI investment platform 'ApexVault.io'. Alex deposited $1,000, saw fake profits grow to $5,000 on screen, but when trying to withdraw, was asked to pay a $1,200 'tax release fee'!",
            keyTakeaway = "Guaranteed daily returns + withdrawal fees = 100% Investment Fraud.",
            quizQuestion = "An investment app shows huge gains but demands a 'tax release fee' to withdraw funds. What is this?",
            quizOption1 = "A classic Pig Butchering investment scam!",
            quizOption2 = "A standard government tax requirement!",
            quizOption3 = "An app processing delay!",
            correctOptionIndex = 0
        ),

        // 41-60 YEARS: MID-CAREER ADULTS
        LearningModule(
            id = "mod_mid_1",
            title = "Insurance Claim & Health Reimbursement Fraud",
            category = "Insurance & Healthcare",
            targetDemographic = DemographicGroup.MID_ADULTS_41_60,
            description = "Learn to spot spoofed SMS messages claiming unpaid medical bills, fake health policy updates, and insurance refund scams.",
            pointsReward = 30,
            isCompleted = false,
            isRecommended = true,
            trendingThreat = true,
            estMinutes = 5,
            storyHeadline = "Urgent Medical Bill & Insurance Claim SMS Traps 🏥📜",
            storyBody = "David received an SMS: 'HEALTH ALERT: Your medical claim #9920 was denied due to missing verification. Update policy info within 12 hours at health-claims-portal.org to prevent $1,200 penalty'. Official health insurers never send unencrypted SMS penalty links.",
            keyTakeaway = "Verify health claims by calling the toll-free number on your physical insurance card.",
            quizQuestion = "An SMS demands immediate insurance policy verification to avoid a fine. What should you do?",
            quizOption1 = "Call the number on your insurance card directly to verify!",
            quizOption2 = "Click the SMS link and fill in your SSN!",
            quizOption3 = "Forward the text to your doctor!",
            correctOptionIndex = 0
        ),
        LearningModule(
            id = "mod_mid_2",
            title = "Pension, Retirement & Mortgage Fraud Defense",
            category = "Retirement Security",
            targetDemographic = DemographicGroup.MID_ADULTS_41_60,
            description = "Protect retirement funds from fraudulent mortgage refinancing offers, pension transfer traps, and fake tax notices.",
            pointsReward = 35,
            isCompleted = false,
            isRecommended = true,
            trendingThreat = false,
            estMinutes = 6,
            storyHeadline = "Pension Fund Rollover & Mortgage Refi Traps 🏛️🔒",
            storyBody = "A smooth-talking advisor calls offering to roll over 401(k) retirement funds into 'tax-exempt gold certificates with guaranteed 12% yield'. Fraudulent advisers use high-pressure tactics to drain lifetime retirement savings into unregulated entities.",
            keyTakeaway = "Consult a licensed, fiduciary financial advisor before initiating any 401(k) or pension transfer.",
            quizQuestion = "A caller pressures you to roll over your 401(k) into guaranteed high-yield gold certificates. Is this safe?",
            quizOption1 = "UNSAFE! High risk pension drain scam; verify with a licensed fiduciary!",
            quizOption2 = "SAFE! Gold is always 100% guaranteed.",
            quizOption3 = "SAFE! As long as they send a brochure.",
            correctOptionIndex = 0
        ),

        // 61+ YEARS: SENIORS
        LearningModule(
            id = "mod_senior_1",
            title = "Grandparent AI Voice-Clone Emergency Defence",
            category = "AI Voice Spoofing",
            targetDemographic = DemographicGroup.SENIORS_61_PLUS,
            description = "Learn how scammers clone a family member's voice using 3 seconds of social media audio to demand bail money.",
            pointsReward = 35,
            isCompleted = false,
            isRecommended = true,
            trendingThreat = true,
            estMinutes = 7,
            storyHeadline = "AI Voice Clone Bail Money Phone Traps 📞👵",
            storyBody = "At 2:00 AM, Eleanor received a frantic phone call sounding exactly like her grandson Tommy: 'Grandma! I got in a car accident in Mexico! I'm in jail and need $4,000 cash bail right now!'. Scammers used AI voice synthesis. Eleanor remembered her family secret password and asked for it — the caller hung up!",
            keyTakeaway = "Always establish a Secret Family Code Word for emergency money requests!",
            quizQuestion = "A relative calls in panic asking for immediate wire money or gift cards. How do you protect yourself?",
            quizOption1 = "Hang up and call the relative or parent on their saved personal phone number!",
            quizOption2 = "Buy gift cards immediately to help!",
            quizOption3 = "Wire cash to the caller's wire address!",
            correctOptionIndex = 0
        ),
        LearningModule(
            id = "mod_senior_2",
            title = "Tech Support Pop-Up & Medicare Plastic Card Scams",
            category = "Tech & Government Impersonation",
            targetDemographic = DemographicGroup.SENIORS_61_PLUS,
            description = "Defeat pop-ups claiming 'Windows Security Locked' and callers asking for bank numbers for new Medicare plastic cards.",
            pointsReward = 30,
            isCompleted = false,
            isRecommended = true,
            trendingThreat = true,
            estMinutes = 5,
            storyHeadline = "The 'Windows Security Virus Warning' Screen Lock 🖥️⚠️",
            storyBody = "Arthur was browsing news when his screen turned red with loud beeping: 'CRITICAL VIRUS DETECTED! Call Microsoft Support at 1-800-555-9000 immediately to unlock computer'. Microsoft NEVER puts phone numbers on virus popups! Turning off the computer unlocks the browser safely.",
            keyTakeaway = "Never call phone numbers on pop-up warnings or allow strangers remote access to your computer.",
            quizQuestion = "A loud pop-up says your computer has a virus and gives a phone number to call. What should you do?",
            quizOption1 = "Restart your computer and ignore the number; Microsoft never posts phone alerts!",
            quizOption2 = "Call the number and give remote access!",
            quizOption3 = "Pay the agent $200 in gift cards!",
            correctOptionIndex = 0
        )
    )

    val sampleThreatAlerts = listOf(
        ThreatAlert(
            id = "alert_101",
            title = "CRITICAL: Urgent IRS Tax Refund Email Campaign Active",
            severity = "HIGH",
            category = "Phishing Campaign",
            dateText = "2 hours ago",
            description = "Widespread email spoofing 'irs-refund-portal-sec.com' sending fraudulent tax recalculation links with password harvesting.",
            actionText = "Do not open external attachments. Official IRS notices arrive via physical US postal mail."
        ),
        ThreatAlert(
            id = "alert_102",
            title = "WARNING: AI Voice Clone Bail Money Phone Attacks",
            severity = "HIGH",
            category = "Deepfake Voice Scam",
            dateText = "Today",
            description = "Scammers using AI voice clones extracted from Instagram Reels to call family members claiming hostage or car accident emergencies.",
            actionText = "Set up a secret family safe word and hang up to call the relative directly."
        ),
        ThreatAlert(
            id = "alert_103",
            title = "NOTICE: Zelle 'Safety Vault' SMS Fraud Spike",
            severity = "MEDIUM",
            category = "SMS Phishing",
            dateText = "Yesterday",
            description = "Texts impersonating Chase and Bank of America requesting users transfer money to a 'temporary fraud protection account'.",
            actionText = "Banks NEVER ask you to transfer money to protect yourself from fraud."
        )
    )

    val emailScenarios = listOf(
        EmailScenario(
            id = "email_1",
            senderName = "Chase Fraud Prevention Dept",
            senderEmail = "security-alerts@chase-bank-verify-auth.com",
            subject = "URGENT: Suspicious Debit of $1,840.00 Authorized - Lock Account Now",
            date = "10:42 AM Today",
            body = """
                Dear Valued Customer,
                
                We detected an unauthorized debit request of $1,840.00 from your checking account to 'CRYPTO_EXCHANGE_GLOBAL'.
                
                If you DID NOT authorize this payment, you must verify your identity immediately within 15 minutes to freeze the transfer:
                
                http://chase-bank-verify-auth.com/secure/login?session_id=98341
                
                Failure to respond immediately will result in irreversible fund transfer and permanent account suspension.
                
                Thank you,
                Chase Fraud Incident Team
            """.trimIndent(),
            targetDemographic = DemographicGroup.PROFESSIONALS_23_40,
            difficulty = "Medium",
            redFlags = listOf(
                RedFlag(
                    id = "rf_1",
                    snippetText = "security-alerts@chase-bank-verify-auth.com",
                    explanation = "Domain Mismatch: The domain is 'chase-bank-verify-auth.com', NOT official 'chase.com'."
                ),
                RedFlag(
                    id = "rf_2",
                    snippetText = "within 15 minutes to freeze the transfer",
                    explanation = "High-Pressure Urgency: Creates artificial panic to bypass critical thinking."
                ),
                RedFlag(
                    id = "rf_3",
                    snippetText = "http://chase-bank-verify-auth.com/secure/login?session_id=98341",
                    explanation = "Unsecure Phishing URL: Non-HTTPS link leading to an external credential harvester."
                ),
                RedFlag(
                    id = "rf_4",
                    snippetText = "irreversible fund transfer and permanent account suspension",
                    explanation = "Coercive Threat: Threatens legal/account lockout to force compliance."
                )
            )
        ),
        EmailScenario(
            id = "email_2",
            senderName = "University Payroll Office",
            senderEmail = "payroll-admin@campus-hr-portal.org",
            subject = "Mandatory Direct Deposit Routing Verification Required Before Payday",
            date = "Yesterday",
            body = """
                All Employees & Student Workers,
                
                Due to our annual payroll system migration, all direct deposit account details must be updated on our temporary portal before 5:00 PM today.
                
                Please click below to re-enter your SSN, Banking Routing Number, and Account Password:
                
                http://campus-hr-portal.org/update-payroll
                
                Unverified accounts will experience a 30-day salary delay.
                
                Sincerely,
                HR Payroll Department
            """.trimIndent(),
            targetDemographic = DemographicGroup.YOUNG_ADULTS_16_22,
            difficulty = "Easy",
            redFlags = listOf(
                RedFlag(
                    id = "rf_2_1",
                    snippetText = "payroll-admin@campus-hr-portal.org",
                    explanation = "Unofficial Domain: HR payroll uses official university email suffix, not a public .org."
                ),
                RedFlag(
                    id = "rf_2_2",
                    snippetText = "re-enter your SSN, Banking Routing Number, and Account Password",
                    explanation = "Sensitive Data Request: Payroll offices never request passwords or SSNs via an unverified link."
                ),
                RedFlag(
                    id = "rf_2_3",
                    snippetText = "30-day salary delay",
                    explanation = "Extreme Threat Penalty: Scammers use extreme penalties to trigger panic."
                )
            )
        )
    )

    val websiteScenarios = listOf(
        WebsiteScenario(
            id = "web_1",
            title = "PayPai Secure Billing Gateway Mockup",
            siteName = "PayPal Identity Verification",
            displayUrl = "http://paypai-secure-auth-update.com/login?ref=urgency",
            isLegitimate = false,
            headerSubtitle = "Security Lock: Your account requires immediate verification to prevent permanent lockout.",
            targetDemographic = DemographicGroup.YOUNG_ADULTS_16_22,
            hotspots = listOf(
                WebsiteHotspot("web_1_url", "URL Address Bar", "Domain Bar", true, "Typosquatting & Unsecure Protocol: Domain uses 'paypai' with an 'i' instead of 'l', and runs on http:// instead of https://."),
                WebsiteHotspot("web_1_badge", "SSL Padlock Icon", "Security Badge", true, "Fake Security Badge: The padlock graphic is rendered inside the web page canvas rather than standard browser chrome."),
                WebsiteHotspot("web_1_banner", "Urgent Account Suspension Banner", "Urgent Warning Banner", true, "Urgency & Panic Coercion: Uses artificial timer 'Account will be closed in 10 minutes'."),
                WebsiteHotspot("web_1_form", "Full SSN & Credit Card Input", "Form Input", true, "Excessive Data Harvesting: Real PayPal never asks for your full SSN, PIN, and Card CVV together on a login page."),
                WebsiteHotspot("web_1_footer", "Copyright & Terms Links", "Footer", false, "Standard Footer: Non-functional links copying PayPal footer text.")
            )
        ),
        WebsiteScenario(
            id = "web_2",
            title = "IRS Official Federal Tax Refund Portal",
            siteName = "Internal Revenue Service",
            displayUrl = "https://www.irs.gov/refunds/status",
            isLegitimate = true,
            headerSubtitle = "Check your official tax refund status securely.",
            targetDemographic = DemographicGroup.MID_ADULTS_41_60,
            hotspots = listOf(
                WebsiteHotspot("web_2_url", "Official URL Bar", "Domain Bar", false, "Authentic Domain: Ends strictly with '.gov' and uses legitimate IRS SSL certificates."),
                WebsiteHotspot("web_2_badge", "Browser SSL Lock", "Security Badge", false, "Valid HTTPS Encryption provided by US Government Treasury CAs."),
                WebsiteHotspot("web_2_form", "SSN & Filing Status Input", "Form Input", false, "Standard IRS Refund Query parameters (SSN, Filing Status, Expected Amount).")
            )
        ),
        WebsiteScenario(
            id = "web_3",
            title = "Amazon Prime Order & Package Reschedule Traps",
            siteName = "Amazon Delivery Reschedule",
            displayUrl = "http://amazon-package-redelivery-notice.info/track",
            isLegitimate = false,
            headerSubtitle = "Package #4902 could not be delivered due to $1.99 unpaid redelivery fee.",
            targetDemographic = DemographicGroup.SENIORS_61_PLUS,
            hotspots = listOf(
                WebsiteHotspot("web_3_url", "Domain URL Bar", "Domain Bar", true, "Fake Domain: Ends with '.info' instead of official 'amazon.com'."),
                WebsiteHotspot("web_3_fee", "Redelivery Fee Button", "Form Input", true, "Fee Bait: Scammers ask for $1.99 to harvest credit card numbers and subscription enrollments."),
                WebsiteHotspot("web_3_timer", "Countdown Clock", "Urgent Warning Banner", true, "Artificial Urgency: Claims package will be returned to sender in 2 hours.")
            )
        )
    )

    val deepfakeScenarios = listOf(
        DeepfakeScenario(
            id = "df_1",
            title = "Emergency Bail Call from 'Grandson Ethan'",
            callerIdentity = "Voice Clone impersonating Ethan (22 yrs)",
            scenarioContext = "A frantic call received at 2:15 AM claiming Ethan was arrested after a car accident and urgently needs $4,500 wire bail before court.",
            audioDurationSeconds = 24,
            waveformPoints = listOf(0.2f, 0.85f, 0.95f, 0.3f, 0.8f, 0.15f, 0.9f, 0.88f, 0.2f, 0.75f, 0.92f, 0.1f, 0.8f, 0.84f, 0.22f, 0.96f),
            spectralArtifactsScore = 88, // High synthetic artifacts
            pitchMonotonyScore = 82, // Unnatural robotic pitch smoothing
            latencyScore = 91, // Latency gap typical of AI LLM speech synthesis models
            isAiGenerated = true,
            forensicExplanation = "AI Spectral Analysis detected unnatural pitch smoothing at 1.2kHz, metallic reverberation artifacts in the vocal tract model, and a 420ms response delay consistent with real-time neural TTS generators.",
            targetDemographic = DemographicGroup.SENIORS_61_PLUS
        ),
        DeepfakeScenario(
            id = "df_2",
            title = "Corporate Urgent Wire Authorization Call",
            callerIdentity = "Authentic Voice of CEO Marcus Vance",
            scenarioContext = "Call received from CEO Marcus requesting routine confirmation for vendor payment voucher #8839.",
            audioDurationSeconds = 15,
            waveformPoints = listOf(0.3f, 0.4f, 0.5f, 0.35f, 0.6f, 0.45f, 0.38f, 0.52f, 0.41f, 0.3f, 0.48f, 0.36f),
            spectralArtifactsScore = 12, // Low artifacts
            pitchMonotonyScore = 18, // Natural vocal inflection
            latencyScore = 15, // Normal human response time
            isAiGenerated = false,
            forensicExplanation = "Natural vocal harmonics, organic micro-tremors, continuous breathing acoustic noise, and zero neural synthesis phase anomalies confirm this audio is AUTHENTIC human speech.",
            targetDemographic = DemographicGroup.PROFESSIONALS_23_40
        ),
        DeepfakeScenario(
            id = "df_3",
            title = "IRS Agent Penalty Assessment Hotline Call",
            callerIdentity = "AI Voice Bot 'Agent Robert Thomas'",
            scenarioContext = "An automated phone call claiming an arrest warrant will be issued in 30 minutes for tax fraud unless $2,000 Apple Gift Cards are provided.",
            audioDurationSeconds = 20,
            waveformPoints = listOf(0.1f, 0.9f, 0.85f, 0.88f, 0.2f, 0.8f, 0.82f, 0.79f, 0.15f, 0.86f, 0.9f, 0.1f),
            spectralArtifactsScore = 94,
            pitchMonotonyScore = 90,
            latencyScore = 85,
            isAiGenerated = true,
            forensicExplanation = "Extreme pitch monotony, artificial room acoustic synthesis, and voice print matching known voice clones confirm this is a SCAM AI GENERATION.",
            targetDemographic = DemographicGroup.MID_ADULTS_41_60
        ),
        DeepfakeScenario(
            id = "df_4",
            title = "Microsoft Tech Support Remote Lock Call",
            callerIdentity = "Authentic Technical Support Specialist",
            scenarioContext = "A routine scheduled callback from your enterprise IT helpdesk verifying ticket #4920.",
            audioDurationSeconds = 18,
            waveformPoints = listOf(0.2f, 0.5f, 0.4f, 0.6f, 0.3f, 0.55f, 0.45f, 0.5f, 0.35f, 0.48f),
            spectralArtifactsScore = 15,
            pitchMonotonyScore = 22,
            latencyScore = 18,
            isAiGenerated = false,
            forensicExplanation = "Acoustic room acoustics match physical headset mic characteristics with genuine background ambient chatter and zero robotic formants.",
            targetDemographic = DemographicGroup.PROFESSIONALS_23_40
        )
    )

    val chatScenarios = listOf(
        ScammerChatScenario(
            id = "chat_1",
            title = "Zelle / Venmo 'Fraud Protection Vault' Threat",
            channelType = "SMS Message",
            scammerPersona = "Bank Fraud Intervention Bot",
            targetDemographic = DemographicGroup.PROFESSIONALS_23_40,
            initialMessages = listOf(
                ScammerChatMessage("1", "Scammer", "ALERT: Chase Fraud Dept detected $2,400 Zelle transfer to 'K. Miller'. If unauthorized, reply 'NO' immediately.", "10:01 AM"),
                ScammerChatMessage("2", "User", "NO", "10:01 AM"),
                ScammerChatMessage("3", "Scammer", "[SYSTEM]: Fraud Intervention Activated. A Senior Fraud Agent is securing your account. Call our toll-free line now at 1-800-555-0199 or reply AGENT to initiate transfer of funds to Safe Vault Account #9921.", "10:02 AM", isWarning = true)
            ),
            choices = listOf(
                ScammerChoice(
                    text = "Halt communication immediately. Open official Chase App independently or call number on back of card.",
                    isSafeChoice = true,
                    scoreDelta = 25,
                    feedback = "EXCELLENT! You recognized the trap. Banks NEVER ask you to transfer funds to a 'safe vault' or provide numbers from incoming SMS texts."
                ),
                ScammerChoice(
                    text = "Reply AGENT and follow instructions to transfer $2,400 to the Safe Vault account.",
                    isSafeChoice = false,
                    scoreDelta = -20,
                    feedback = "TRAP FALLEN! Scammers use the 'Safe Vault' trick to trick you into voluntarily sending them money."
                ),
                ScammerChoice(
                    text = "Call 1-800-555-0199 from the text message to speak with the agent.",
                    isSafeChoice = false,
                    scoreDelta = -15,
                    feedback = "DANGER! Phone numbers sent in phishing texts connect straight to scam call centers trained to impersonate bank staff."
                )
            )
        ),
        ScammerChatScenario(
            id = "chat_2",
            title = "WhatsApp Crypto Investment VIP Group Hook",
            channelType = "WhatsApp",
            scammerPersona = "Crypto Advisor 'Elena'",
            targetDemographic = DemographicGroup.YOUNG_ADULTS_16_22,
            initialMessages = listOf(
                ScammerChatMessage("201", "Scammer", "Hi Alex! You were selected for our VIP Trading Group! We achieved 420% profit yesterday using our AI Arbitrage Bot 🚀", "2:14 PM"),
                ScammerChatMessage("202", "Scammer", "Just deposit $100 in USDT to test it out. You can withdraw profits anytime! Click custom-trade-vault.io/join", "2:15 PM", isWarning = true)
            ),
            choices = listOf(
                ScammerChoice(
                    text = "Block contact, exit group, and report as spam to WhatsApp.",
                    isSafeChoice = true,
                    scoreDelta = 25,
                    feedback = "SAFE DECISION! Guaranteed crypto returns in messaging groups are 100% Pig Butchering lures."
                ),
                ScammerChoice(
                    text = "Deposit $100 to test if the profits are real.",
                    isSafeChoice = false,
                    scoreDelta = -25,
                    feedback = "TRAP FALLEN! The platform shows fake earnings but will lock your money and demand 'tax release fees'."
                )
            )
        ),
        ScammerChatScenario(
            id = "chat_3",
            title = "Discord Free Nitro & Steam Skin Gift Bot",
            channelType = "Discord",
            scammerPersona = "Gamer Friend Hijacked Bot",
            targetDemographic = DemographicGroup.TEENS_11_15,
            initialMessages = listOf(
                ScammerChatMessage("301", "Scammer", "Yo bro! Discord is giving away 3 months free Nitro to celebrate their anniversary!", "4:30 PM"),
                ScammerChatMessage("302", "Scammer", "Claim yours here before the 500 spots run out: http://dlscord-gift-nitro-claim.xyz/verify", "4:30 PM", isWarning = true)
            ),
            choices = listOf(
                ScammerChoice(
                    text = "Ignore link, check official Discord app settings, and warn your friend that their account is hijacked.",
                    isSafeChoice = true,
                    scoreDelta = 20,
                    feedback = "GENIUS! Official Discord links always end in .discord.com or .discord.gg. 'dlscord' was a token harvester!"
                ),
                ScammerChoice(
                    text = "Click link and authorize Discord account login.",
                    isSafeChoice = false,
                    scoreDelta = -20,
                    feedback = "TAKEN OVER! Authorizing OAuth on fake domains steals your Discord authorization token."
                )
            )
        )
    )

    val sampleScamReplays: Map<String, ScamReplayData> = mapOf(
        "LIVE_CHAT" to ScamReplayData(
            scenarioTitle = "Zelle 'Safe Vault' Bank Impersonation",
            scamType = "SMS Social Engineering",
            summary = "How scammers leverage artificial urgency and authority to trick victims into voluntary bank transfers.",
            steps = listOf(
                ScamReplayStep(1, "Artificial Urgency Triggered", "The scammer sends a fake $2,400 debit alert to induce immediate panic.", "Urgency Bias (+25%)", "URGENCY"),
                ScamReplayStep(2, "Simulated Defense System", "Replying 'NO' triggers a fake system response offering a 'Senior Agent' and 'Safe Vault'.", "Trust in Tech (+18%)", "WARNING"),
                ScamReplayStep(3, "Authority Bias Exploitation", "The scammer pretends to be Chase Fraud Department protecting your money.", "Authority Bias (+30%)", "AUTHORITY"),
                ScamReplayStep(4, "Voluntary Transfer Lure", "Victim is instructed to Zelle funds to a 'Safe Protection Account'.", "Information Shared", "PAYLOAD"),
                ScamReplayStep(5, "Irreversible Financial Loss", "Funds are immediately wired out to non-recoverable mule accounts.", "Fraud Completed", "LOSS")
            ),
            keyLesson = "Rule: Banks will NEVER ask you to move your own money into a 'safe account' to prevent fraud!"
        ),
        "PHISHING" to ScamReplayData(
            scenarioTitle = "Phishing Email Credential Harvester",
            scamType = "Email Phishing",
            summary = "Dissecting a fake Chase Fraud email leading to credential theft.",
            steps = listOf(
                ScamReplayStep(1, "Spoofed Authority Sender", "Email header uses 'chase-bank-verify-auth.com' to masquerade as official bank.", "Authority Bias (+22%)", "AUTHORITY"),
                ScamReplayStep(2, "Panic Deadline (15 Mins)", "Claims account will be locked forever if not clicked in 15 mins.", "Urgency Bias (+35%)", "URGENCY"),
                ScamReplayStep(3, "Unverified Link Clicked", "Victim clicks non-HTTPS external link instead of navigating to bank app.", "Curiosity & Fear (+15%)", "WARNING"),
                ScamReplayStep(4, "Login Credentials Stolen", "Victim types online banking username, password, and SMS OTP.", "Credential Harvest", "PAYLOAD"),
                ScamReplayStep(5, "Account Drain Completed", "Scammers log into real bank portal and transfer account balance.", "Fraud Completed", "LOSS")
            ),
            keyLesson = "Rule: Always check the full sender email domain and navigate directly to official apps/sites!"
        ),
        "DEEPFAKE" to ScamReplayData(
            scenarioTitle = "Grandson Bail AI Voice Clone Trap",
            scamType = "AI Deepfake Voice Cloning",
            summary = "How scammers extract 3 seconds of vocal audio to spoof panic emergency bail requests.",
            steps = listOf(
                ScamReplayStep(1, "Social Media Voice Scrape", "Scammers scraped public TikTok/Reels audio to train a real-time neural TTS voice clone.", "Tech Exploitation (+30%)", "WARNING"),
                ScamReplayStep(2, "Late Night Panic Call", "Phone call at 2:00 AM claiming a car crash and jail lockup in Mexico.", "Urgency & Fear (+40%)", "URGENCY"),
                ScamReplayStep(3, "Bypass Verification", "Caller begs 'Don't tell mom and dad!' to prevent independent verification.", "Isolation Tactics", "AUTHORITY"),
                ScamReplayStep(4, "Wire/Gift Card Demand", "Demands $4,000 cash wire bail immediately.", "Financial Demand", "PAYLOAD"),
                ScamReplayStep(5, "Defense Action", "Establish a secret family codeword and hang up to call relative directly on saved phone number.", "Immunity Rule", "LOSS")
            ),
            keyLesson = "Rule: Always hang up and call your relative back directly on their known phone number!"
        ),
        "WEBSITE" to ScamReplayData(
            scenarioTitle = "Fake PayPai Spoofed Login Gateway",
            scamType = "Phishing Website",
            summary = "How typosquatting domains and copied visual assets trick users into giving away full SSNs and card credentials.",
            steps = listOf(
                ScamReplayStep(1, "Typosquatting Domain Lure", "URL uses 'paypai' with an 'i' instead of an 'l' on non-HTTPS connection.", "Urgency & Curiosity (+20%)", "WARNING"),
                ScamReplayStep(2, "Fake Security Badge", "Page renders a fake padlock graphic inside page HTML canvas.", "Trust in Tech (+25%)", "AUTHORITY"),
                ScamReplayStep(3, "Extortion Form Inputs", "Form requests full SSN, Credit Card number, and PIN code on one screen.", "Over-sharing Info", "PAYLOAD"),
                ScamReplayStep(4, "Identity Theft & Unauthorized Debits", "Credentials sent to illegal dark web database.", "Fraud Completed", "LOSS")
            ),
            keyLesson = "Rule: Inspect browser URL bar strictly! Real financial portals use official domain names and HTTPS."
        )
    )
}

