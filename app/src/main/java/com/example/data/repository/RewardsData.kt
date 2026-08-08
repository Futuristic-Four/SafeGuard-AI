package com.example.data.repository

import com.example.data.model.RewardItem

object RewardsData {
    val sampleRewards = listOf(
        RewardItem(
            id = "reward_mcafee_75",
            title = "McAfee Total Protection (1-Year Pass)",
            providerName = "McAfee",
            category = "ANTIVIRUS",
            xpCost = 500,
            originalPriceText = "$89.99/yr",
            rewardValueText = "75% OFF for XP",
            description = "Complete multi-device protection against malware, ransomware, phishing links, and keyloggers with safe web browsing.",
            highlightTag = "Antivirus Deal",
            codePrefix = "MCAFEE-75OFF-SAFEGUARD",
            iconEmoji = "🛡️",
            redemptionUrl = "https://www.mcafee.com/activate"
        ),
        RewardItem(
            id = "reward_norton_60",
            title = "Norton 360 Deluxe + Secure VPN",
            providerName = "Norton",
            category = "ANTIVIRUS",
            xpCost = 600,
            originalPriceText = "$99.99/yr",
            rewardValueText = "60% OFF for XP",
            description = "1-Year Deluxe protection for 5 devices, including unlimited Secure VPN, Dark Web Monitoring, and Smart Firewall.",
            highlightTag = "Antivirus + VPN",
            codePrefix = "NORTON360-60OFF-SAFEGUARD",
            iconEmoji = "💻",
            redemptionUrl = "https://norton.com/setup"
        ),
        RewardItem(
            id = "reward_top3_free_antivirus",
            title = "1-Year FREE Premium Antivirus Suite",
            providerName = "McAfee / Norton",
            category = "TOP3_PERK",
            xpCost = 0,
            originalPriceText = "$99.99 Value",
            rewardValueText = "100% FREE (Top 3 Perk)",
            description = "Exclusive monthly champion perk! Full 1-Year license key for McAfee Total Protection or Norton 360 Deluxe ($0 charge).",
            highlightTag = "👑 Monthly Top 3 Perk",
            codePrefix = "TOP3-CHAMPION-FREE-ANTIVIRUS",
            isTop3Exclusive = true,
            iconEmoji = "🏆",
            redemptionUrl = "https://safeguard.ai/claim-top3-antivirus"
        ),
        RewardItem(
            id = "reward_yubikey_30",
            title = "YubiKey 5 Series Security Key",
            providerName = "Yubico",
            category = "HARDWARE",
            xpCost = 750,
            originalPriceText = "$55.00",
            rewardValueText = "30% OFF Hardware Key",
            description = "Hardware FIDO2 NFC physical security key to unhackably lock bank, email, and crypto accounts against phishing traps.",
            highlightTag = "Anti-Phishing Hardware",
            codePrefix = "YUBICO-30OFF-SAFEGUARD",
            iconEmoji = "🔑",
            redemptionUrl = "https://www.yubico.com/store"
        ),
        RewardItem(
            id = "reward_proton_vpn",
            title = "ProtonVPN CyberShield Pass (3 Mo)",
            providerName = "Proton",
            category = "PRIVACY",
            xpCost = 450,
            originalPriceText = "$29.99",
            rewardValueText = "80% OFF Pass",
            description = "Swiss-hosted encrypted VPN with NetShield DNS malware filtering, blocking malicious phishing & scam sites automatically.",
            highlightTag = "DNS Malware Shield",
            codePrefix = "PROTON-CYBERSHIELD-80",
            iconEmoji = "🌐",
            redemptionUrl = "https://protonvpn.com/redeem"
        ),
        RewardItem(
            id = "reward_aura_identity",
            title = "Aura Identity & Credit Theft Protection",
            providerName = "Aura Security",
            category = "SCAM_TOOL",
            xpCost = 350,
            originalPriceText = "$30.00/mo",
            rewardValueText = "2 Months Free Key",
            description = "Real-time SSN, bank account, and credit monitoring backed by $1,000,000 stolen funds insurance reimbursement.",
            highlightTag = "$1M Fraud Coverage",
            codePrefix = "AURA-IDSHIELD-FREE2M",
            iconEmoji = "📑",
            redemptionUrl = "https://www.aura.com/redeem"
        ),
        RewardItem(
            id = "reward_robokiller",
            title = "Truecaller / Robokiller AI Call Blocker",
            providerName = "Truecaller Pro",
            category = "SCAM_TOOL",
            xpCost = 400,
            originalPriceText = "$23.99",
            rewardValueText = "50% OFF 6-Mo Pass",
            description = "AI voice clone detector & scam caller blocker. Automatically intercepts spoofed bank numbers & SMS phishing links.",
            highlightTag = "AI Voice Blocker",
            codePrefix = "TRUECALLER-VOICE-50OFF",
            iconEmoji = "📞",
            redemptionUrl = "https://www.truecaller.com/redeem"
        ),
        RewardItem(
            id = "reward_metal_vault",
            title = "SafeGuard Stainless Seed Storage Vault",
            providerName = "SafeGuard Hardware",
            category = "HARDWARE",
            xpCost = 800,
            originalPriceText = "$65.00",
            rewardValueText = "40% OFF Voucher",
            description = "Fireproof and waterproof physical stainless steel storage card to offline-shield master recovery keys from malware.",
            highlightTag = "Offline Key Shield",
            codePrefix = "SAFEGUARD-METALVAULT-40",
            iconEmoji = "🔐",
            redemptionUrl = "https://safeguard.ai/store"
        )
    )
}
