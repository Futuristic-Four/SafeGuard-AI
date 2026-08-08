package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.DemographicGroup
import com.example.data.model.FviRecord
import com.example.data.model.LearningModule
import com.example.data.model.TechLiteracy
import com.example.data.model.ThreatAlert
import com.example.data.model.UserProfile
import com.example.data.repository.SafeGuardRepository
import com.example.data.repository.ScannerResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SafeGuardViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SafeGuardRepository(application)

    val userProfile: StateFlow<UserProfile?> = repository.userProfile.toStateFlow(null)
    val fviHistory: StateFlow<List<FviRecord>> = repository.fviHistory.toStateFlow(emptyList())
    val modules: StateFlow<List<LearningModule>> = repository.modules.toStateFlow(emptyList())
    val threatAlerts: StateFlow<List<ThreatAlert>> = repository.threatAlerts.toStateFlow(emptyList())

    private val _isScannerDialogOpen = MutableStateFlow(false)
    val isScannerDialogOpen: StateFlow<Boolean> = _isScannerDialogOpen.asStateFlow()

    private val _isThreatSheetOpen = MutableStateFlow(false)
    val isThreatSheetOpen: StateFlow<Boolean> = _isThreatSheetOpen.asStateFlow()

    private val _activeScamReplay = MutableStateFlow<com.example.data.model.ScamReplayData?>(null)
    val activeScamReplay: StateFlow<com.example.data.model.ScamReplayData?> = _activeScamReplay.asStateFlow()

    private val _selectedTab = MutableStateFlow(0) // 0: Dashboard, 1: Learning Hub, 2: Simulation Hub, 3: Profile/Analytics
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _simulationCategory = MutableStateFlow(0) // 0: Phishing Email, 1: AI Deepfake, 2: Live Scammer Chat
    val simulationCategory: StateFlow<Int> = _simulationCategory.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeSeedData()
        }
    }

    fun setSelectedTab(tabIndex: Int) {
        _selectedTab.value = tabIndex
    }

    fun setSimulationCategory(catIndex: Int) {
        _simulationCategory.value = catIndex
    }

    fun toggleScannerDialog(isOpen: Boolean) {
        _isScannerDialogOpen.value = isOpen
    }

    fun toggleThreatSheet(isOpen: Boolean) {
        _isThreatSheetOpen.value = isOpen
    }

    fun updateDemographicProfile(group: DemographicGroup, literacy: TechLiteracy) {
        viewModelScope.launch {
            repository.updateDemographicProfile(group, literacy)
        }
    }

    fun saveUserProfile(
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
    ) {
        viewModelScope.launch {
            repository.saveUserProfile(
                identifier = identifier,
                loginType = loginType,
                password = password,
                name = name,
                occupation = occupation,
                demographicGroup = demographicGroup,
                techLiteracy = techLiteracy,
                scamExperience = scamExperience,
                bankingHabits = bankingHabits,
                familyDetails = familyDetails,
                financialGoals = financialGoals
            )
        }
    }

    fun loginUser(
        identifier: String,
        password: String,
        loginType: String
    ) {
        viewModelScope.launch {
            repository.loginUser(
                identifier = identifier,
                password = password,
                loginType = loginType
            )
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            repository.logoutUser()
        }
    }

    fun completeModule(moduleId: String, points: Int) {
        viewModelScope.launch {
            repository.completeModule(moduleId, points)
        }
    }

    fun completeSubTask(moduleId: String, taskId: String, points: Int) {
        viewModelScope.launch {
            repository.completeSubTask(moduleId, taskId, points)
        }
    }

    fun recordSimulationScore(delta: Int, reason: String) {
        viewModelScope.launch {
            repository.updateFviScore(delta, reason)
        }
    }

    fun recordSimulationResult(isSuccess: Boolean, scamCategory: String, replayData: com.example.data.model.ScamReplayData? = null) {
        viewModelScope.launch {
            repository.recordSimulationResult(isSuccess, scamCategory)
            if (!isSuccess && replayData != null) {
                _activeScamReplay.value = replayData
            }
        }
    }

    fun triggerScamReplay(replayData: com.example.data.model.ScamReplayData) {
        _activeScamReplay.value = replayData
    }

    fun dismissScamReplay() {
        _activeScamReplay.value = null
    }

    fun analyzeContent(text: String): ScannerResult {
        return repository.analyzeSuspiciousContent(text)
    }

    fun redeemReward(
        reward: com.example.data.model.RewardItem,
        onResult: (Boolean, String, String) -> Unit
    ) {
        viewModelScope.launch {
            val randomSuffix1 = (1000..9999).random()
            val randomSuffix2 = (1000..9999).random()
            val generatedCode = "${reward.codePrefix}-$randomSuffix1-$randomSuffix2"
            
            val (success, message) = repository.redeemReward(reward, generatedCode)
            onResult(success, message, if (success) generatedCode else "")
        }
    }

    private fun <T> kotlinx.coroutines.flow.Flow<T>.toStateFlow(initialValue: T): StateFlow<T> {
        val state = MutableStateFlow(initialValue)
        viewModelScope.launch {
            this@toStateFlow.collect { state.value = it }
        }
        return state.asStateFlow()
    }
}
