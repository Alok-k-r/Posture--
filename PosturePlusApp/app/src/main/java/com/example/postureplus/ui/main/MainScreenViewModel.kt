package com.example.postureplus.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postureplus.data.PostureCategory
import com.example.postureplus.data.PostureTab
import com.example.postureplus.data.PostureUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(calculateStateForAngle(71))
    val uiState: StateFlow<PostureUiState> = _uiState.asStateFlow()

    fun updateAngle(angle: Int) {
        val clamped = angle.coerceIn(45, 90)
        _uiState.update { current ->
            calculateStateForAngle(clamped, current)
        }
    }

    fun togglePodConnection() {
        _uiState.update { current ->
            val nextConnected = !current.isPodConnected
            current.copy(
                isPodConnected = nextConnected,
                batteryPercent = if (nextConnected) 84 else 0
            )
        }
    }

    fun selectDay(index: Int) {
        _uiState.update { current ->
            val isToday = index == 3
            current.copy(
                selectedDayIndex = index,
                slouchAlerts = if (isToday) 2 else (index * 3) % 6
            )
        }
    }

    fun selectTab(tab: PostureTab) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun syncHardware() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true) }
            delay(1200)
            _uiState.update { it.copy(isSyncing = false) }
        }
    }

    companion object {
        fun calculateStateForAngle(angle: Int, prev: PostureUiState = PostureUiState()): PostureUiState {
            val tiltDeg = 90 - angle

            // Integrity Score calculation (calibrated so 71° = 75%, 90° = 99%, 45° = 38%)
            val score = when {
                angle == 71 -> 75
                angle > 71 -> (75 + ((angle - 71).toFloat() / 19f) * 24f).roundToInt()
                else -> (38 + ((angle - 45).toFloat() / 26f) * 36f).roundToInt()
            }.coerceIn(0, 100)

            // Kapandji cervical spine strain formula
            val loadLbs = if (tiltDeg > 0) {
                ((10f + tiltDeg * 0.95f) * 10f).roundToInt() / 10f
            } else {
                10.0f
            }

            val category = when {
                angle >= 80 -> PostureCategory.UPRIGHT
                angle >= 68 -> PostureCategory.MILD_SLOUCH
                else -> PostureCategory.SLOUCH_ALERT
            }

            val (headline, subheadline) = when (category) {
                PostureCategory.UPRIGHT -> "UPRIGHT OPTIMAL" to "ALIGNMENT OPTIMAL"
                PostureCategory.MILD_SLOUCH -> "MILD SLOUCH" to "CLINICAL PRECISION"
                PostureCategory.SLOUCH_ALERT -> "SLOUCH ALERT" to "HIGH CERVICAL STRAIN"
            }

            return prev.copy(
                angle = angle,
                integrityScore = score,
                spinalLoadLbs = loadLbs,
                torsoTiltDeg = tiltDeg,
                postureCategory = category,
                statusHeadline = headline,
                statusSubheadline = subheadline
            )
        }
    }
}
