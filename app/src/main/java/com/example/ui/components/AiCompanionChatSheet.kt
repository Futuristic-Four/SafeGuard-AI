package com.example.ui.components

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.model.UserProfile
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
import com.example.ui.theme.WarningGold
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: String = "Just now"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiCompanionChatSheet(
    userProfile: UserProfile? = null,
    onDismiss: () -> Unit,
    initialPrompt: String? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var inputText by remember { mutableStateOf("") }
    var isRecordingVoice by remember { mutableStateOf(false) }
    var isThinking by remember { mutableStateOf(false) }
    var isTtsSpeaking by remember { mutableStateOf(false) }
    var voiceMuted by remember { mutableStateOf(false) }

    // TextToSpeech Engine initialization
    var ttsEngine by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(context) {
        val tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Initialize TTS with English
            }
        }
        tts.setLanguage(Locale.US)
        ttsEngine = tts

        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    fun speakText(text: String) {
        if (!voiceMuted && ttsEngine != null) {
            ttsEngine?.stop()
            ttsEngine?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "companion_voice")
            isTtsSpeaking = true
        }
    }

    val greetingName = userProfile?.userName?.split(" ")?.firstOrNull() ?: "there"
    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                text = "Hello $greetingName! I'm your SafeGuard AI Educator & Cybersecurity Companion. 🛡️ Ask me anything about suspicious emails, UPI scams, McAfee/Norton antivirus perks, deepfakes, or how to keep your family safe!",
                isUser = false
            )
        )
    }

    fun sendMessage(query: String) {
        if (query.isBlank()) return
        val userMsg = ChatMessage(text = query, isUser = true)
        messages.add(userMsg)
        inputText = ""
        isThinking = true

        coroutineScope.launch {
            listState.animateScrollToItem(messages.size - 1)
            delay(700) // Simulated realistic AI reasoning time

            val aiResponse = generateEducationalAnswer(query, userProfile)
            isThinking = false
            val aiMsg = ChatMessage(text = aiResponse, isUser = false)
            messages.add(aiMsg)

            listState.animateScrollToItem(messages.size - 1)
            speakText(aiResponse)
        }
    }

    LaunchedEffect(initialPrompt) {
        if (!initialPrompt.isNullOrBlank()) {
            sendMessage(initialPrompt!!)
        }
    }

    // Voice Recording Speech-to-Text Simulation
    LaunchedEffect(isRecordingVoice) {
        if (isRecordingVoice) {
            delay(2500) // Simulate listening to speech
            isRecordingVoice = false
            val simulatedSpeech = "How do I recognize a fake UPI request on my phone?"
            inputText = simulatedSpeech
            sendMessage(simulatedSpeech)
        }
    }

    Dialog(
        onDismissRequest = {
            ttsEngine?.stop()
            onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f)
                .testTag("ai_companion_chat_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ObsidianBackground),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, CyberCyan)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Header Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CardSurface)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, CyberCyan, RoundedCornerShape(12.dp))
                                .background(CardSurfaceElevated)
                                .padding(2.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.app_logo),
                                contentDescription = "SafeGuard AI Logo",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "SafeGuard AI Educator",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = CircleShape,
                                    color = MintEmerald.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "LIVE AI",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MintEmerald,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Text & Voice Educational Assistant",
                                fontSize = 11.sp,
                                color = CyberCyan
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                voiceMuted = !voiceMuted
                                if (voiceMuted) ttsEngine?.stop()
                            },
                            modifier = Modifier.testTag("toggle_voice_output_button")
                        ) {
                            Icon(
                                imageVector = if (voiceMuted) Icons.Default.VolumeUp else Icons.Default.RecordVoiceOver,
                                contentDescription = "Toggle Voice",
                                tint = if (voiceMuted) TextMuted else MintEmerald
                            )
                        }

                        IconButton(
                            onClick = {
                                ttsEngine?.stop()
                                onDismiss()
                            },
                            modifier = Modifier.testTag("close_chat_button")
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                        }
                    }
                }

                // Quick Topic Suggestion Pills
                Surface(
                    color = CardSurfaceElevated,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "💡 Tap to ask AI Educator:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(
                                "How do I get free McAfee/Norton?",
                                "How do I spot fake UPI QR codes?",
                                "What are Deepfake Voice Scams?",
                                "What is a Fake Digital Arrest?",
                                "Is it safe to share my OTP?",
                                "How do Telegram Part-time Job Scams work?"
                            ).forEach { topic ->
                                Surface(
                                    color = CardSurface,
                                    shape = RoundedCornerShape(20.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                                    modifier = Modifier.clickable { sendMessage(topic) }
                                ) {
                                    Text(
                                        text = topic,
                                        fontSize = 11.sp,
                                        color = CyberCyan,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Message Stream Area
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(messages) { msg ->
                        ChatMessageBubble(
                            message = msg,
                            onPlayVoice = { speakText(msg.text) }
                        )
                    }

                    if (isThinking) {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(CardSurface)
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = CyberCyan,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Analyzing security threat vector...",
                                    fontSize = 12.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }

                // Voice Recording Live Listening Overlay Indicator
                AnimatedVisibility(visible = isRecordingVoice) {
                    Surface(
                        color = WarningGold.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarningGold),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            VoiceWaveformBars()
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "🎙️ Listening to your voice query... Speak now!",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = WarningGold
                            )
                        }
                    }
                }

                // Bottom Input Control Bar
                Surface(
                    color = CardSurface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Voice Mic Button
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(if (isRecordingVoice) WarningGold else CardSurfaceElevated)
                                .border(1.dp, if (isRecordingVoice) WarningGold else CardBorder, CircleShape)
                                .clickable { isRecordingVoice = !isRecordingVoice }
                                .testTag("btn_voice_input_mic")
                        ) {
                            Icon(
                                imageVector = if (isRecordingVoice) Icons.Default.MicOff else Icons.Default.Mic,
                                contentDescription = "Voice Search",
                                tint = if (isRecordingVoice) Color.Black else CyberCyan
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Text Field
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text("Ask doubt or type scenario...", fontSize = 13.sp, color = TextMuted) },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(onSend = { sendMessage(inputText) }),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CardSurfaceElevated,
                                unfocusedContainerColor = CardSurfaceElevated,
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = CardBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("chatbot_text_input")
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        // Send Button
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(if (inputText.isNotBlank()) CyberCyan else CardSurfaceElevated)
                                .clickable(enabled = inputText.isNotBlank()) {
                                    sendMessage(inputText)
                                }
                                .testTag("btn_send_chat_message")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = if (inputText.isNotBlank()) Color.Black else TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageBubble(
    message: ChatMessage,
    onPlayVoice: () -> Unit
) {
    val isUser = message.isUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(CyberCyan.copy(alpha = 0.2f))
                    .border(1.dp, CyberCyan, CircleShape)
            ) {
                Icon(Icons.Default.SmartToy, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            color = if (isUser) RoyalBlue.copy(alpha = 0.35f) else CardSurface,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isUser) RoyalBlue else CardBorder
            ),
            modifier = Modifier.fillMaxWidth(0.82f)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.text,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    lineHeight = 19.sp
                )

                if (!isUser) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(CyberCyan.copy(alpha = 0.15f))
                                .clickable { onPlayVoice() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.VolumeUp, contentDescription = "Play voice", tint = CyberCyan, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "LISTEN TO AI", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberCyan)
                        }

                        Text(text = message.timestamp, fontSize = 10.sp, color = TextMuted)
                    }
                }
            }
        }
    }
}

private fun String?.isNullByBlank(): Boolean = this == null || this.isBlank()

private fun generateEducationalAnswer(query: String, userProfile: UserProfile? = null): String {
    val q = query.trim().lowercase()
    val userName = userProfile?.userName?.split(" ")?.firstOrNull() ?: "there"
    val availableXp = userProfile?.availableXp ?: 1450

    // 1. Greetings / Small Talk / Introductions
    if (q == "hi" || q == "hello" || q == "hey" || q.startsWith("hi ") || q.startsWith("hello ") || q.contains("good morning") || q.contains("good evening")) {
        return "👋 Hello $userName! I'm your SafeGuard AI Educator & Cybersecurity Companion.\n\n" +
                "How can I assist you today? You can ask me about:\n" +
                "• 🛡️ **McAfee & Norton Antivirus** (and how to get free 1-year licenses with your $availableXp XP)\n" +
                "• 📲 **UPI & QR Code Scams**\n" +
                "• 🎙️ **Deepfake Voice & AI Fraud**\n" +
                "• 🚔 **Fake Digital Arrest & Police Video Calls**\n" +
                "• 💼 **Telegram Part-time Job Scams**\n" +
                "• 🔑 **Hardware Security Keys (YubiKey)**"
    }

    if (q.contains("who are you") || q.contains("what can you do") || q.contains("your name")) {
        return "🤖 I am **SafeGuard AI**, your 24/7 personal financial security and scam prevention assistant.\n\n" +
                "My core superpowers include:\n" +
                "1. **Real-time Threat Analysis**: I break down suspicious links, calls, SMS, and emails.\n" +
                "2. **Interactive Defense Coaching**: I teach you step-by-step how to protect your bank accounts and identity.\n" +
                "3. **Rewards & Immunity Guidance**: I show you how to leverage your earned XP ($availableXp XP) to claim free antivirus software and security hardware!"
    }

    if (q.contains("thank") || q == "thanks" || q.contains("awesome") || q.contains("good job") || q.contains("great")) {
        return "😊 You're very welcome, $userName! Staying vigilant is the best defense against digital financial fraud.\n\n" +
                "Whenever you receive an unexpected link, QR code, or urgent phone call, come back and ask me before taking action!"
    }

    // 2. Antivirus / McAfee / Norton / Rewards / XP / Software
    if (q.contains("mcafee") || q.contains("norton") || q.contains("antivirus") || q.contains("software") || q.contains("reward") || q.contains("xp") || q.contains("leaderboard") || q.contains("yubikey") || q.contains("vpn")) {
        if (q.contains("free") || q.contains("get") || q.contains("claim") || q.contains("how") || q.contains("redeem")) {
            return "🎁 **HOW TO GET FREE ANTIVIRUS & DISCOUNTS**\n\n" +
                    "1. **Monthly Top 3 Rank (100% FREE)**: Finish in the Top 3 on the Monthly Leaderboard to instantly unlock a **1-Year FREE License Key** for **McAfee Total Protection** or **Norton 360 Deluxe** ($99 value)!\n\n" +
                    "2. **Spend XP in Rewards Store**: You currently have **$availableXp XP**! Head over to the **Leaderboard tab**, scroll down to the **Scam Prevention Rewards** section, and redeem your XP for:\n" +
                    "   • 75% OFF McAfee 1-Year Subscription (1,000 XP)\n" +
                    "   • 60% OFF Norton 360 Deluxe (1,200 XP)\n" +
                    "   • $25 Off YubiKey 5C NFC Hardware Key (1,800 XP)\n" +
                    "   • 1-Year ProtonVPN Plus (1,500 XP)\n\n" +
                    "💡 *Pro Tip: Complete daily simulations in the Simulation Hub to boost your XP fast!*"
        } else {
            return "🛡️ **WHY PREMIUM ANTIVIRUS IS ESSENTIAL FOR FINANCIAL SAFETY**\n\n" +
                    "Modern scammers don't just use fake calls — they send hidden malware inside fake utility bill APKs, bank phishing sites, and keyloggers.\n\n" +
                    "• **Real-Time Web Protection**: McAfee and Norton block phishing links before you enter passwords or credit card details.\n" +
                    "• **Malware & Trojan Scanner**: Detects dangerous screen-recording apps (like fake AnyDesk or fake banking APKs) in milliseconds.\n" +
                    "• **Wi-Fi & VPN Encryption**: Protects public Wi-Fi transactions from man-in-the-middle attacks.\n\n" +
                    "👉 *Check out the **Leaderboard Rewards Store** to redeem your $availableXp XP for McAfee or Norton activation keys!*"
        }
    }

    // 3. UPI / QR Code / PIN / Payment Scams
    if (q.contains("upi") || q.contains("qr") || q.contains("pin") || q.contains("gpay") || q.contains("phonepe") || q.contains("paytm") || q.contains("collect")) {
        return "⚠️ **GOLDEN RULE OF UPI PAYMENT SAFETY**\n\n" +
                "🛑 **Entering your UPI PIN ALWAYS DEDUCTS money from your bank account!** You NEVER enter a PIN or scan a QR code to RECEIVE money.\n\n" +
                "**Common UPI Scam Traps:**\n" +
                "1. **The 'Buyer' QR Code**: Scammer claims they sent money for your item on OLX, but sends a QR code asking you to 'Scan & enter PIN to receive $500'. That's a trap!\n" +
                "2. **Fake Payment Screenshots**: Scammer shows a fake GPay/PhonePe payment confirmation screenshot and demands cash refund.\n" +
                "3. **UPI Collect Request**: Scammer sends a notification titled 'Refund of $100' — clicking 'Pay' sends your money to them.\n\n" +
                "💡 **Action Step**: If you accidentally fell for a UPI scam, call national cybercrime helpline **1930** immediately and lodge a dispute at cybercrime.gov.in within 2 hours!"
    }

    // 4. Deepfake Voice & AI Cloning
    if (q.contains("deepfake") || q.contains("voice") || q.contains("clone") || q.contains("kidnap") || q.contains("accident") || q.contains("face swap")) {
        return "🎙️ **DEEPFAKE VOICE CLONING SCAMS EXPLAINED**\n\n" +
                "Scammers use generative AI tools to clone a person's exact voice using just **3 seconds** of audio extracted from Instagram, YouTube, or TikTok reels!\n\n" +
                "**How the Scam Works:**\n" +
                "You receive an frantic call from a number you don't recognize. The voice sounds EXACTLY like your child, parent, or relative crying that they got into a terrible car crash or were arrested, and need urgent bail money via UPI.\n\n" +
                "🛡️ **How to Protect Yourself:**\n" +
                "1. **Establish a Family Emergency Secret Code**: Create a private password that only immediate family members know.\n" +
                "2. **Hang Up & Call Direct**: Immediately disconnect and dial your relative's saved phone number directly.\n" +
                "3. **Ask Personal Questions**: Ask something only your real relative would know (e.g., 'What was our dog's name?')."
    }

    // 5. Digital Arrest / Cyber Police / CBI / ED / Skype Video Call
    if (q.contains("police") || q.contains("digital arrest") || q.contains("cbi") || q.contains("narcotics") || q.contains("ncb") || q.contains("ed") || q.contains("warrant") || q.contains("skype")) {
        return "🚔 **FAKE 'DIGITAL ARREST' CYBER POLICE EXTORTION**\n\n" +
                "🚨 **FACT**: There is NO SUCH THING as a 'Digital Arrest' in legitimate law enforcement in India or worldwide!\n\n" +
                "**How the Scammers Manipulate You:**\n" +
                "1. They call claiming a courier package in your name contains illegal drugs, fake passports, or stolen credit cards.\n" +
                "2. They transfer the call to a fake 'CBI Officer' or 'Customs Inspector' on WhatsApp/Skype video call sitting in front of a staged police logo backdrop.\n" +
                "3. They threaten immediate arrest and force you to keep your camera on ('Digital Arrest') while demanding money to clear your name.\n\n" +
                "🛡️ **What to Do Immediately:**\n" +
                "• **Disconnect the call right away!** Police do NOT conduct investigations via Skype or ask for money via UPI/RTGS.\n" +
                "• Report the caller's number to **Chakshu portal** or **1930 Cybercrime Helpline**."
    }

    // 6. OTP / Bank SMS / Account Freeze / KYC / Credit Cards
    if (q.contains("otp") || q.contains("sms") || q.contains("kyc") || q.contains("bank") || q.contains("freeze") || q.contains("sbi") || q.contains("hdfc") || q.contains("icici") || q.contains("pan") || q.contains("cvv")) {
        return "🔐 **BANK SMS & OTP SAFETY CHECK**\n\n" +
                "1. **No Bank Demands OTPs**: Bank executives, RBI officers, and tech support will NEVER call asking for your OTP, CVV, or net banking password.\n" +
                "2. **Fake Account Suspension SMS**: If you receive an SMS saying 'Your SBI account is blocked! Update PAN immediately at [fake-link]', check the sender ID. Official bank SMS headers use 6-character short codes (like *AD-SBIINB*), never 10-digit mobile numbers.\n" +
                "3. **SIM Swap Protection**: If your mobile network suddenly loses signal completely for hours, call your telecom provider immediately from another phone — scammers may have issued a duplicate SIM."
    }

    // 7. Electricity Bill / Power Disconnection / Meter Scams
    if (q.contains("electricity") || q.contains("power") || q.contains("bijli") || q.contains("meter") || q.contains("disconnection")) {
        return "⚡ **ELECTRICITY BILL DISCONNECTION SCAM ALERT**\n\n" +
                "**The Fake Message:**\n" +
                "'Dear customer, your electricity power supply will be disconnected tonight at 9:30 PM because your previous month's bill was not updated. Immediately call electricity officer at 98xxxxxx.'\n\n" +
                "**The Real Danger:**\n" +
                "When you call the number, the scammer asks you to pay a nominal Rs 10 fee via a link, or asks you to download a 'quick support' app like AnyDesk or RustDesk. They then mirror your screen and steal your bank credentials!\n\n" +
                "🛡️ **Safe Guard Rule**: Always pay electricity bills only via official state power utility portals or trusted apps like Bharat BillPay."
    }

    // 8. Courier / Customs / FedEx / Parcel Scams
    if (q.contains("courier") || q.contains("fedex") || q.contains("bluedart") || q.contains("customs") || q.contains("parcel") || q.contains("package") || q.contains("drugs")) {
        return "📦 **FAKE COURIER & CUSTOMS PARCEL FRAUD**\n\n" +
                "Scammers send automated IVR robocalls: *'Press 1 to speak to FedEx agent regarding seized parcel #84920.'*\n\n" +
                "When you press 1, a fake agent claims a parcel sent from Mumbai to Taiwan containing 5 illegal passports and 140g MDMA drugs was seized with your Aadhaar ID.\n\n" +
                "🛑 **Remember**: Official courier companies do not call you demanding money or threatening police action over the phone. Hang up immediately!"
    }

    // 9. Fake Jobs / Telegram / YouTube Like Tasks
    if (q.contains("job") || q.contains("work from home") || q.contains("telegram") || q.contains("youtube") || q.contains("like") || q.contains("subscribe") || q.contains("part time") || q.contains("commission")) {
        return "💼 **THE TELEGRAM 'PART-TIME TASK' SCAM TRAP**\n\n" +
                "**How the Trap Springs:**\n" +
                "1. You get a WhatsApp offer for a simple online job: 'Earn $50 to $200 daily by liking YouTube videos or rating Google Maps locations.'\n" +
                "2. First 2 tasks pay you real money ($5 - $10) into your UPI to earn your trust.\n" +
                "3. You are added to a Telegram group and asked to do 'Prepaid Crypto Investment Tasks' where you must deposit $500 to earn $1,000.\n" +
                "4. When you try to withdraw your profits, they demand additional 'tax' fees until you lose thousands!\n\n" +
                "🛑 **Rule**: Real employers pay you for work; they NEVER ask you to deposit money to unlock your earnings!"
    }

    // 10. Investment / Crypto / Stock Tips / Guaranteed Returns
    if (q.contains("invest") || q.contains("stock") || q.contains("trading") || q.contains("crypto") || q.contains("guarantee") || q.contains("return") || q.contains("profit") || q.contains("whatsapp group")) {
        return "📈 **FAKE INVESTMENT & HIGH-YIELD STOCK GROUP SCAMS**\n\n" +
                "Scammers add victims to WhatsApp or Telegram groups titled 'VIP Institutional Stock Trading Tips' or 'Crypto AI Trading Bot'.\n\n" +
                "• Fake group members post screenshots showing fake daily gains of 300% to 500%.\n" +
                "• They instruct you to register on a custom trading website or APK app controlled by the scammers.\n" +
                "• Your invested money goes directly into scammer bank accounts, while the app shows fake rising balances.\n\n" +
                "💡 **SEBI / SEC Warning**: Registered financial advisors NEVER promise guaranteed returns. Always verify SEBI/SEC registration numbers!"
    }

    // 11. OLX / Army Officer / Advance Token Scams
    if (q.contains("olx") || q.contains("quikr") || q.contains("army") || q.contains("soldier") || q.contains("token") || q.contains("furniture") || q.contains("car") || q.contains("second hand")) {
        return "🪖 **OLX & ARMY OFFICER ADVANCE FRAUD**\n\n" +
                "Scammers post photos of clean cars, iPhones, or furniture at ridiculously low prices, posing as an Army Officer being urgently transferred.\n\n" +
                "They ask for an 'advance token payment' or 'army gate pass fee' via UPI before delivery. Once paid, they disappear or block your number!\n\n" +
                "🛡️ **Rule**: Never pay advance money to strangers for second-hand items. Inspect items in person before transferring money."
    }

    // 12. E-Commerce / Amazon / Flipkart / Screen Share
    if (q.contains("amazon") || q.contains("flipkart") || q.contains("customer care") || q.contains("anydesk") || q.contains("teamviewer") || q.contains("screen share")) {
        return "🛒 **FAKE CUSTOMER CARE & SCREEN-SHARING TRAP**\n\n" +
                "When people search 'Amazon customer care number' on Google, scammers publish fake helpline numbers in paid ads.\n\n" +
                "When you call, they pretend to process your refund but ask you to install **AnyDesk, TeamViewer QuickSupport, or RustDesk**.\n\n" +
                "🛑 **Crucial Safety Rule**: Screen-sharing apps allow scammers to view your banking OTPs as they arrive on your screen! NEVER install screen-sharing software at the request of an incoming caller."
    }

    // 13. Passwords, MFA, Hardware Security Keys
    if (q.contains("password") || q.contains("mfa") || q.contains("2fa") || q.contains("authenticator") || q.contains("hack") || q.contains("breach") || q.contains("passkey")) {
        return "🔑 **ULTIMATE PASSWORD & IDENTITY HARDWARE SECURITY**\n\n" +
                "1. **Ditch SMS 2FA for Authenticator Apps**: SMS 2FA can be intercepted via SIM swapping. Use Google Authenticator, Microsoft Authenticator, or Bitwarden.\n" +
                "2. **Hardware Security Keys (YubiKey)**: Physical FIDO2 keys provide 100% phishing-proof protection. Even if a scammer tricks you into giving your password, they cannot log in without pressing the physical button on your YubiKey!\n" +
                "3. **Passphrase Rule**: Instead of `P@ssw0rd123`, use 4 random words like `PurpleElephantCoffeeDancing!` which are virtually impossible to brute-force."
    }

    // 14. SafeGuard App Features / FVI Score / How to level up
    if (q.contains("fvi") || q.contains("score") || q.contains("simulation") || q.contains("hub") || q.contains("streak") || q.contains("safeguard")) {
        return "📊 **YOUR SAFEGUARD AI FINANCIAL VULNERABILITY INDEX (FVI)**\n\n" +
                "Your **FVI Score** (currently around 650-850) measures your real-world immunity against financial fraud.\n\n" +
                "**How to Maximize Your Immunity & XP:**\n" +
                "1. **Simulation Hub**: Practice interactive scenarios (Phishing Emails, AI Deepfakes, Scammer Voice Calls).\n" +
                "2. **Learning Modules**: Complete bite-sized lessons to earn +50 to +100 XP per module.\n" +
                "3. **Daily Streaks**: Maintain your streak to earn bonus multiplier XP and top the Leaderboard for free antivirus rewards!"
    }

    // 15. Smart Dynamic Contextual Generator (for any other query)
    val queryKeywords = q.replace(Regex("[^a-zA-Z0-9 ]"), "").split(" ").filter { it.length > 3 }
    val topicContext = if (queryKeywords.isNotEmpty()) queryKeywords.take(3).joinToString(" & ") else "digital safety"

    return "🛡️ **SAFEGUARD AI CUSTOM ANALYSIS**: Regarding *$topicContext*\n\n" +
            "To keep your bank accounts, personal identity, and devices 100% secure, keep these 3 core defense principles in mind:\n\n" +
            "1. **Never Act Under Artificial Urgency**: Scammers create panic ('Account frozen in 10 mins', 'Court warrant issued'). Take a deep breath and verify independently.\n" +
            "2. **Verify Through Official Channels**: Never tap links in unsolicited SMS or emails. Open your official banking app directly or call the number printed on your physical debit/credit card.\n" +
            "3. **Protective Shield**: Ensure you have an active antivirus like McAfee or Norton enabled on your device. You can use your **$availableXp XP** in our **Leaderboard Rewards** section to get free licenses!\n\n" +
            "💬 *Do you have a specific message, link, or scenario you want me to analyze for threat risks?*"
}

@Composable
fun VoiceWaveformBars() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(20.dp)
    ) {
        listOf(12.dp, 18.dp, 8.dp, 20.dp, 14.dp, 10.dp, 16.dp).forEach { height ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(height)
                    .clip(CircleShape)
                    .background(WarningGold)
            )
        }
    }
}
