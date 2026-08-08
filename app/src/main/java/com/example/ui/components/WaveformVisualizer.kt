package com.example.ui.components

import android.media.AudioManager
import android.media.ToneGenerator
import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeepfakeScenario
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CardSurfaceElevated
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ThreatRed
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun WaveformVisualizer(
    scenario: DeepfakeScenario,
    onAnswerSubmitted: (isAiVerdict: Boolean, correct: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var playbackProgress by remember { mutableStateOf(0f) }
    var showTelemetry by remember { mutableStateOf(true) }
    var userVerdict by remember { mutableStateOf<Boolean?>(null) }
    var hasSubmitted by remember { mutableStateOf(false) }

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var toneGenerator by remember { mutableStateOf<ToneGenerator?>(null) }

    DisposableEffect(context) {
        var speechEngine: TextToSpeech? = null
        speechEngine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                speechEngine?.language = Locale.US
                speechEngine?.setPitch(if (scenario.isAiGenerated) 0.85f else 1.05f)
                speechEngine?.setSpeechRate(0.95f)
            }
        }
        tts = speechEngine
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        onDispose {
            try {
                speechEngine.stop()
                speechEngine.shutdown()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                toneGenerator?.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            val audioText = "Grandpa, it's me! Urgent emergency call: ${scenario.scenarioContext}"
            tts?.speak(audioText, TextToSpeech.QUEUE_FLUSH, null, "deepfake_demo_voice")
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            while (playbackProgress < 1f) {
                delay(100)
                playbackProgress += 0.04f
            }
            isPlaying = false
            playbackProgress = 0f
            tts?.stop()
        } else {
            tts?.stop()
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("deepfake_waveform_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header with Speaker identity
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(RoyalBlue.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = "Voice Speaker",
                            tint = RoyalBlue,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = scenario.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = scenario.callerIdentity,
                            fontSize = 12.sp,
                            color = CyberCyan
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = CardSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Text(
                        text = "${scenario.audioDurationSeconds}s Clip",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Context Story Card
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CardSurfaceElevated,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Context: ${scenario.scenarioContext}",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interactive Waveform & Player Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0D1117))
                    .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { isPlaying = !isPlaying },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(CyberCyan)
                        .testTag("play_pause_audio_button")
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Waveform Canvas Drawing
                Canvas(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    val points = scenario.waveformPoints
                    val barWidth = size.width / (points.size * 2)
                    val centerY = size.height / 2

                    points.forEachIndexed { index, amp ->
                        val x = index * barWidth * 2 + barWidth
                        val barHeight = amp * size.height * 0.8f
                        val isPlayed = (index.toFloat() / points.size) <= playbackProgress

                        val barColor = when {
                            isPlayed -> CyberCyan
                            showTelemetry && scenario.isAiGenerated && amp > 0.8f -> ThreatRed
                            else -> Color(0xFF30363D)
                        }

                        drawLine(
                            color = barColor,
                            start = Offset(x, centerY - barHeight / 2),
                            end = Offset(x, centerY + barHeight / 2),
                            strokeWidth = barWidth * 1.2f
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Diagnostic Telemetry Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = "Telemetry",
                        tint = CyberCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Forensic Spectral Diagnostics",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Diagnostics",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Switch(
                        checked = showTelemetry,
                        onCheckedChange = { showTelemetry = it },
                        modifier = Modifier.testTag("toggle_telemetry_switch"),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CyberCyan,
                            checkedTrackColor = CyberCyan.copy(alpha = 0.3f)
                        )
                    )
                }
            }

            // Diagnostic Metrics Overlay
            AnimatedVisibility(visible = showTelemetry) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardSurfaceElevated)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricBar(
                        label = "Spectral Phase Artifacts (Synthetic Glitches)",
                        score = scenario.spectralArtifactsScore,
                        isHighRisk = scenario.spectralArtifactsScore > 60
                    )
                    MetricBar(
                        label = "Pitch Monotony & Neural Smoothing",
                        score = scenario.pitchMonotonyScore,
                        isHighRisk = scenario.pitchMonotonyScore > 60
                    )
                    MetricBar(
                        label = "TTS Generation Latency Discrepancy",
                        score = scenario.latencyScore,
                        isHighRisk = scenario.latencyScore > 60
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Verdict Buttons
            if (!hasSubmitted) {
                Text(
                    text = "Is this audio sample Authentic or an AI Deepfake?",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            userVerdict = false
                            hasSubmitted = true
                            val correct = (scenario.isAiGenerated == false)
                            onAnswerSubmitted(false, correct)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("verdict_authentic_button"),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MintEmerald),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MintEmerald)
                    ) {
                        Text("AUTHENTIC HUMAN", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            userVerdict = true
                            hasSubmitted = true
                            val correct = (scenario.isAiGenerated == true)
                            onAnswerSubmitted(true, correct)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("verdict_ai_deepfake_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = ThreatRed)
                    ) {
                        Text("AI DEEPFAKE", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                    }
                }
            } else {
                // Post-Answer Feedback Card
                val isUserCorrect = (userVerdict == scenario.isAiGenerated)
                val feedbackBg = if (isUserCorrect) MintEmerald.copy(alpha = 0.15f) else ThreatRed.copy(alpha = 0.15f)
                val feedbackBorder = if (isUserCorrect) MintEmerald else ThreatRed

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = feedbackBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, feedbackBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isUserCorrect) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = "Result",
                                tint = if (isUserCorrect) MintEmerald else ThreatRed,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isUserCorrect) "CORRECT DETECTION! (+30 FVI Pts)" else "DETECTION MISSED (-15 FVI Pts)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isUserCorrect) MintEmerald else ThreatRed
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = scenario.forensicExplanation,
                            fontSize = 12.sp,
                            color = TextPrimary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricBar(
    label: String,
    score: Int,
    isHighRisk: Boolean
) {
    val barColor = if (isHighRisk) ThreatRed else MintEmerald

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 11.sp, color = TextSecondary)
            Text(text = "$score%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = barColor)
        }
        Spacer(modifier = Modifier.height(3.dp))
        LinearProgressIndicator(
            progress = { score / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(CircleShape),
            color = barColor,
            trackColor = CardSurface
        )
    }
}
