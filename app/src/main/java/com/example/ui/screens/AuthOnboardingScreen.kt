package com.example.ui.screens

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.DemographicGroup
import com.example.data.model.TechLiteracy
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AuthOnboardingScreen(
    onLogin: (identifier: String, password: String, loginType: String) -> Unit,
    onSignUp: (
        identifier: String,
        loginType: String,
        password: String,
        name: String,
        occupation: String,
        group: DemographicGroup,
        literacy: TechLiteracy,
        scamExp: String,
        banking: String,
        family: String,
        goals: String
    ) -> Unit
) {
    // Mode Switcher: false = Log In, true = Sign Up
    var isSignUpMode by remember { mutableStateOf(false) }

    // Auth Type: "EMAIL" or "MOBILE"
    var loginType by remember { mutableStateOf("EMAIL") }

    // Auth Inputs
    var identifierInput by remember { mutableStateOf("alex@safeguard.ai") }
    var passwordInput by remember { mutableStateOf("password123") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Sign Up Profile Details Inputs (asked ONLY during Sign Up)
    var nameInput by remember { mutableStateOf("Alex Vance") }
    var occupationInput by remember { mutableStateOf("Software Engineer") }
    var selectedGroup by remember { mutableStateOf(DemographicGroup.PROFESSIONALS_23_40) }
    var selectedLiteracy by remember { mutableStateOf(TechLiteracy.AVERAGE) }

    var selectedScamExp by remember { mutableStateOf("Narrowly Avoided") }
    var selectedBankingHabits by remember { mutableStateOf(setOf("UPI", "Credit Card", "Internet Banking", "Mobile Banking")) }
    var familyInput by remember { mutableStateOf("Living with family") }
    var financialGoalInput by remember { mutableStateOf("Wealth Growth & Security") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBackground)
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Hero Header with Logo Image
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, CyberCyan, RoundedCornerShape(14.dp))
                    .background(CardSurface)
                    .padding(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "SafeGuard AI Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "SafeGuard AI",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
                Text(
                    text = "Financial Immunity & Scam Defense",
                    fontSize = 12.sp,
                    color = CyberCyan,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Auth Mode Switcher (Log In vs Sign Up)
        Surface(
            color = CardSurface,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(4.dp)) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (!isSignUpMode) MintEmerald.copy(alpha = 0.2f) else Color.Transparent)
                        .border(
                            width = if (!isSignUpMode) 1.dp else 0.dp,
                            color = if (!isSignUpMode) MintEmerald else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            isSignUpMode = false
                        }
                        .padding(vertical = 12.dp)
                        .testTag("tab_login")
                ) {
                    Text(
                        text = "Log In",
                        fontWeight = FontWeight.Bold,
                        color = if (!isSignUpMode) MintEmerald else TextMuted,
                        fontSize = 15.sp
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSignUpMode) CyberCyan.copy(alpha = 0.2f) else Color.Transparent)
                        .border(
                            width = if (isSignUpMode) 1.dp else 0.dp,
                            color = if (isSignUpMode) CyberCyan else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            isSignUpMode = true
                        }
                        .padding(vertical = 12.dp)
                        .testTag("tab_signup")
                ) {
                    Text(
                        text = "Sign Up",
                        fontWeight = FontWeight.Bold,
                        color = if (isSignUpMode) CyberCyan else TextMuted,
                        fontSize = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Identification Channel Selector (Email ID vs Mobile Number)
        Card(
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Sign In Method",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(
                        color = if (loginType == "EMAIL") CyberCyan.copy(alpha = 0.18f) else CardSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (loginType == "EMAIL") CyberCyan else CardBorder
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                loginType = "EMAIL"
                                if (identifierInput.contains("+") || identifierInput.all { it.isDigit() }) {
                                    identifierInput = "alex@safeguard.ai"
                                }
                            }
                            .testTag("method_email")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = if (loginType == "EMAIL") CyberCyan else TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Email ID",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (loginType == "EMAIL") CyberCyan else TextPrimary
                            )
                        }
                    }

                    Surface(
                        color = if (loginType == "MOBILE") MintEmerald.copy(alpha = 0.18f) else CardSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (loginType == "MOBILE") MintEmerald else CardBorder
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                loginType = "MOBILE"
                                if (identifierInput.contains("@")) {
                                    identifierInput = "9876543210"
                                }
                            }
                            .testTag("method_mobile")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null,
                                tint = if (loginType == "MOBILE") MintEmerald else TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mobile Number",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (loginType == "MOBILE") MintEmerald else TextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Credentials Input Fields
                OutlinedTextField(
                    value = identifierInput,
                    onValueChange = { identifierInput = it },
                    label = { Text(if (loginType == "EMAIL") "Email Address" else "Mobile Number") },
                    placeholder = { Text(if (loginType == "EMAIL") "e.g. alex@safeguard.ai" else "e.g. +1 555-019-2834") },
                    leadingIcon = {
                        Icon(
                            imageVector = if (loginType == "EMAIL") Icons.Default.Email else Icons.Default.Phone,
                            contentDescription = null,
                            tint = CyberCyan
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (loginType == "EMAIL") KeyboardType.Email else KeyboardType.Phone
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_identifier_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = { passwordInput = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = CyberCyan) },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password",
                                tint = TextMuted
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_password_input")
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!isSignUpMode) {
            // ================= LOG IN MODE =================
            // Directly logs in and restores user data WITHOUT asking demographic details!
            Button(
                onClick = {
                    onLogin(
                        identifierInput.ifBlank { if (loginType == "EMAIL") "alex@safeguard.ai" else "9876543210" },
                        passwordInput.ifBlank { "password123" },
                        loginType
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = MintEmerald),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("btn_login_submit")
            ) {
                Text(
                    text = "Log In & Access SafeGuard AI",
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.Black)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = CardSurface,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "ℹ️ Logging in restores your existing FVI baseline, badges, and learning history directly without re-asking profile questions.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(12.dp)
                )
            }
        } else {
            // ================= SIGN UP MODE =================
            // Asks user details ONLY during Sign Up!
            Card(
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Badge, contentDescription = null, tint = CyberCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "New User Details (Personalization)",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Full Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = CyberCyan) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("signup_name_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = occupationInput,
                        onValueChange = { occupationInput = it },
                        label = { Text("Occupation / Role") },
                        leadingIcon = { Icon(Icons.Default.Work, contentDescription = null, tint = CyberCyan) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("signup_occupation_input")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Select Your Demographic / Age Group:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Age Group Options
                    DemographicGroup.entries.forEach { group ->
                        val isSelected = selectedGroup == group
                        Surface(
                            color = if (isSelected) CyberCyan.copy(alpha = 0.15f) else CardSurfaceElevated,
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) CyberCyan else CardBorder
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { selectedGroup = group }
                                .testTag("age_group_${group.name}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) CyberCyan else CardBorder)
                                ) {
                                    Text(
                                        text = group.ageRange,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.Black else TextPrimary
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = group.displayName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isSelected) CyberCyan else TextPrimary
                                    )
                                    Text(
                                        text = "Focus: ${group.primaryFocus}",
                                        fontSize = 11.sp,
                                        color = TextMuted,
                                        maxLines = 1
                                    )
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = CyberCyan,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 2: Technology & Scam History
            Card(
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = MintEmerald)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Scam History & Tech Comfort",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Have you encountered scam attempts before?",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Never", "Lost money before", "Narrowly avoided", "Frequent target").forEach { exp ->
                            val isSelected = selectedScamExp == exp
                            Surface(
                                color = if (isSelected) MintEmerald.copy(alpha = 0.2f) else CardSurfaceElevated,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) MintEmerald else CardBorder
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .clickable { selectedScamExp = exp }
                                    .testTag("scam_exp_$exp")
                            ) {
                                Text(
                                    text = exp,
                                    fontSize = 12.sp,
                                    color = if (isSelected) MintEmerald else TextPrimary,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Technology Comfort Level:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TechLiteracy.entries.forEach { literacy ->
                            val isSelected = selectedLiteracy == literacy
                            Surface(
                                color = if (isSelected) RoyalBlue.copy(alpha = 0.25f) else CardSurfaceElevated,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) RoyalBlue else CardBorder
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedLiteracy = literacy }
                                    .testTag("tech_lit_${literacy.name}")
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(10.dp)
                                ) {
                                    Text(
                                        text = literacy.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (isSelected) TextPrimary else TextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 3: Banking & Financial Goals
            Card(
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountBalance, contentDescription = null, tint = CyberCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Banking Channels & Goals",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Select active financial payment channels:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("UPI", "Credit Card", "Internet Banking", "Investment Apps", "Crypto", "Mobile Banking").forEach { habit ->
                            val isChecked = selectedBankingHabits.contains(habit)
                            Surface(
                                color = if (isChecked) CyberCyan.copy(alpha = 0.2f) else CardSurfaceElevated,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isChecked) CyberCyan else CardBorder
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .clickable {
                                        selectedBankingHabits = if (isChecked) {
                                            selectedBankingHabits - habit
                                        } else {
                                            selectedBankingHabits + habit
                                        }
                                    }
                                    .testTag("habit_$habit")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    if (isChecked) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    Text(
                                        text = habit,
                                        fontSize = 12.sp,
                                        color = if (isChecked) CyberCyan else TextMuted
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = familyInput,
                        onValueChange = { familyInput = it },
                        label = { Text("Family Setup") },
                        leadingIcon = { Icon(Icons.Default.FamilyRestroom, contentDescription = null, tint = CyberCyan) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = financialGoalInput,
                        onValueChange = { financialGoalInput = it },
                        label = { Text("Primary Financial Goal") },
                        leadingIcon = { Icon(Icons.Default.Security, contentDescription = null, tint = CyberCyan) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Button for Sign Up
            Button(
                onClick = {
                    onSignUp(
                        identifierInput.ifBlank { if (loginType == "EMAIL") "alex@safeguard.ai" else "9876543210" },
                        loginType,
                        passwordInput.ifBlank { "password123" },
                        nameInput.ifBlank { "Alex Vance" },
                        occupationInput.ifBlank { "General User" },
                        selectedGroup,
                        selectedLiteracy,
                        selectedScamExp,
                        selectedBankingHabits.joinToString(", "),
                        familyInput,
                        financialGoalInput
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("btn_signup_submit")
            ) {
                Text(
                    text = "Complete Sign Up & Generate Shield",
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
