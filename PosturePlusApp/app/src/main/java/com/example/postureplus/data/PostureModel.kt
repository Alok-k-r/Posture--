package com.example.postureplus.data

enum class PostureCategory {
    UPRIGHT,
    MILD_SLOUCH,
    SLOUCH_ALERT
}

enum class PostureTab {
    HOME,
    HISTORY,
    POD,
    PROFILE
}

data class DayItem(
    val dayName: String,
    val dayNum: String,
    val isToday: Boolean = false,
    val uprightPct: Int = 80
)

data class PostureUiState(
    val angle: Int = 71,
    val integrityScore: Int = 75,
    val spinalLoadLbs: Float = 28.1f,
    val torsoTiltDeg: Int = 19,
    val postureCategory: PostureCategory = PostureCategory.MILD_SLOUCH,
    val isPodConnected: Boolean = true,
    val batteryPercent: Int = 84,
    val activeTab: PostureTab = PostureTab.HOME,
    val selectedDayIndex: Int = 3, // Thu 22 Oct
    val uprightMinutes: Int = 42,
    val uprightTargetMinutes: Int = 45,
    val slouchAlerts: Int = 2,
    val streakDays: Int = 4,
    val isSyncing: Boolean = false,
    val statusHeadline: String = "MILD SLOUCH",
    val statusSubheadline: String = "CLINICAL PRECISION"
)
