package com.example.postureplus.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.postureplus.R
import com.example.postureplus.data.DayItem
import com.example.postureplus.data.PostureCategory
import com.example.postureplus.data.PostureTab
import com.example.postureplus.data.PostureUiState
import com.example.postureplus.theme.AccentAmber
import com.example.postureplus.theme.AccentAmberLight
import com.example.postureplus.theme.AccentSage
import com.example.postureplus.theme.AccentSageLight
import com.example.postureplus.theme.AccentSlateDark
import com.example.postureplus.theme.AccentTerracotta
import com.example.postureplus.theme.AccentTerracottaGlow
import com.example.postureplus.theme.AccentTerracottaLight
import com.example.postureplus.theme.BgGradientBottom
import com.example.postureplus.theme.BgGradientMid
import com.example.postureplus.theme.BgGradientSoft
import com.example.postureplus.theme.BgGradientTop
import com.example.postureplus.theme.CardBorderSubtle
import com.example.postureplus.theme.DiskFrostBottom
import com.example.postureplus.theme.DiskFrostMid
import com.example.postureplus.theme.DiskFrostTop
import com.example.postureplus.theme.PlusJakartaSans
import com.example.postureplus.theme.StateGoodHaloCore
import com.example.postureplus.theme.StateGoodHaloMid
import com.example.postureplus.theme.StateMildHaloCore
import com.example.postureplus.theme.StateMildHaloMid
import com.example.postureplus.theme.StatePoorHaloCore
import com.example.postureplus.theme.StatePoorHaloMid
import com.example.postureplus.theme.SurfaceCard
import com.example.postureplus.theme.SurfaceTinted
import com.example.postureplus.theme.TextOnDark
import com.example.postureplus.theme.TextPrimary
import com.example.postureplus.theme.TextSecondary
import com.example.postureplus.theme.TextTertiary

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            // 100% Scandinavian luxury apricot-peach sunset wash
            .background(
                Brush.verticalGradient(
                    0.0f to BgGradientTop,
                    0.20f to BgGradientMid,
                    0.50f to BgGradientSoft,
                    1.0f to BgGradientBottom
                )
            )
    ) {
        when (state.activeTab) {
            PostureTab.HOME -> {
                // Generous bottom clearance (140dp) completely eliminates bottom dock overlap
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 140.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        AppHeader(
                            state = state,
                            onTogglePod = { viewModel.togglePodConnection() }
                        )
                    }

                    item {
                        DateStripSection(
                            state = state,
                            onSelectDay = { viewModel.selectDay(it) },
                            onSync = { viewModel.syncHardware() }
                        )
                    }

                    item {
                        HeroPostureCard(state = state)
                    }

                    item {
                        SimulatorControlCard(
                            angle = state.angle,
                            onAngleChange = { viewModel.updateAngle(it) }
                        )
                    }

                    item {
                        MetricsGrid(state = state)
                    }

                    item {
                        WeeklyTrendCard(
                            selectedDay = state.selectedDayIndex,
                            onSelectDay = { viewModel.selectDay(it) }
                        )
                    }

                    item {
                        AiPrognosisCard()
                    }
                }
            }

            PostureTab.HISTORY -> PlaceholderTabContent(title = "Full Telemetry History", subtitle = "View daily slouch timestamps and cloud synchronizations.")
            PostureTab.POD -> PlaceholderTabContent(title = "Posture+ Pod Hardware", subtitle = "BLE Device ID: POD-71-BLE-A8F\nFirmware: v2.4.1\nVibration Motor: Level 2 (Gentle)")
            PostureTab.PROFILE -> PlaceholderTabContent(title = "User Profile & Targets", subtitle = "Logged in as Mohit\nStreak: 4 Days Active\nCervical Alignment Goal: 80°")
        }

        // Floating Bottom Dock (Exact white active pill, clean clearance)
        BottomDock(
            activeTab = state.activeTab,
            onSelectTab = { viewModel.selectTab(it) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )
    }
}

/* ==========================================================================
   1. TOP APP HEADER (REAL STUDIO PORTRAIT + VECTOR BATTERY & BELL)
   ========================================================================== */
@Composable
fun AppHeader(
    state: PostureUiState,
    onTogglePod: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // User Profile with Real Studio Portrait & Multi-gradient Ring
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .shadow(elevation = 8.dp, shape = CircleShape, spotColor = AccentTerracottaGlow)
                    .clip(CircleShape)
                    .background(
                        Brush.sweepGradient(
                            listOf(
                                Color(0xFFDE7049),
                                Color(0xFFF7D5C4),
                                Color(0xFFE8952B),
                                Color(0xFFDE7049)
                            )
                        )
                    )
                    .padding(2.5.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avatar_user),
                    contentDescription = "User Profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            Column {
                Text(
                    text = "Hi, Mohit",
                    fontFamily = PlusJakartaSans,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
                Text(
                    text = if (state.isPodConnected) "Posture+ Active" else "Pod Disconnected",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    color = if (state.isPodConnected) TextSecondary else TextTertiary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Header Actions: Precision White Capsule BLE Pill + White Round Bell
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            // BLE Pod Pill with Animated Breathing Dot
            Row(
                modifier = Modifier
                    .shadow(elevation = 3.dp, shape = RoundedCornerShape(999.dp), spotColor = Color(0x18000000))
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0x14000000), RoundedCornerShape(999.dp))
                    .clickable { onTogglePod() }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Animated Breathing Green Pulse Dot
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val pulseScale by infiniteTransition.animateFloat(
                    initialValue = 0.85f,
                    targetValue = 1.15f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1100, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "pulseScale"
                )
                val pulseAlpha by infiniteTransition.animateFloat(
                    initialValue = 0.45f,
                    targetValue = 1.0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1100, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "pulseAlpha"
                )

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .scale(if (state.isPodConnected) pulseScale else 1f)
                        .clip(CircleShape)
                        .background(
                            if (state.isPodConnected) AccentSage.copy(alpha = pulseAlpha)
                            else TextTertiary
                        )
                )

                // Precision Vector Battery Pill with Terminal Nub
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(18.dp)
                            .height(10.dp)
                            .border(1.5.dp, TextPrimary, RoundedCornerShape(2.5.dp))
                            .padding(1.5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(if (state.isPodConnected) (state.batteryPercent / 100f).coerceIn(0.1f, 1f) else 0.05f)
                                .height(5.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .background(if (state.isPodConnected) TextPrimary else TextTertiary)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(0.5.dp))
                            .background(TextPrimary)
                    )
                }

                Text(
                    text = if (state.isPodConnected) "${state.batteryPercent}%" else "--",
                    fontFamily = PlusJakartaSans,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            // Notification Bell (Pristine White Circle + Feather Vector Icon)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .shadow(elevation = 3.dp, shape = CircleShape, spotColor = Color(0x18000000))
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, Color(0x14000000), CircleShape)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bell),
                    contentDescription = "Notifications",
                    tint = TextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/* ==========================================================================
   2. WEEKLY DATE STRIP (ELONGATED LUXURY CAPSULES & WARM THU 22 GLOW)
   ========================================================================== */
@Composable
fun DateStripSection(
    state: PostureUiState,
    onSelectDay: (Int) -> Unit,
    onSync: () -> Unit,
    modifier: Modifier = Modifier
) {
    val days = listOf(
        DayItem("Mon", "19", false, 65),
        DayItem("Tue", "20", false, 78),
        DayItem("Wed", "21", false, 84),
        DayItem("Thu", "22", true, 92),
        DayItem("Fri", "23", false, 55),
        DayItem("Sat", "24", false, 70),
        DayItem("Sun", "25", false, 60),
    )

    val currentDay = days.getOrNull(state.selectedDayIndex) ?: days[3]

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Label Row with Peach Sync Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${currentDay.dayName}, ${currentDay.dayNum} Oct • ${if (currentDay.isToday) "Today" else "History"}",
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6E5F57)
            )

            // Peach-tinted sync button with vector sync arrow
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(AccentTerracottaLight)
                    .clickable { onSync() }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val infiniteTransition = rememberInfiniteTransition(label = "sync")
                val rotation by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(800, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "syncRot"
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_sync_arrow),
                    contentDescription = "Sync",
                    tint = AccentTerracotta,
                    modifier = Modifier
                        .size(14.dp)
                        .then(if (state.isSyncing) Modifier.rotate(rotation) else Modifier)
                )
                Text(
                    text = if (state.isSyncing) "Syncing" else "Sync",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentTerracotta
                )
            }
        }

        // Tall Elongated Date Capsules
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            days.forEachIndexed { index, day ->
                val isSelected = index == state.selectedDayIndex

                if (isSelected) {
                    // Active Pill (Solid Terracotta with Warm Shadow)
                    Column(
                        modifier = Modifier
                            .width(54.dp)
                            .height(88.dp)
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(27.dp),
                                spotColor = AccentTerracotta
                            )
                            .clip(RoundedCornerShape(27.dp))
                            .background(AccentTerracotta)
                            .clickable { onSelectDay(index) }
                            .padding(vertical = 14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = day.dayName,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = day.dayNum,
                            fontFamily = PlusJakartaSans,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                } else {
                    // Inactive Pill (Pristine White Capsule with Soft Drop Shadow)
                    Column(
                        modifier = Modifier
                            .width(50.dp)
                            .height(82.dp)
                            .shadow(
                                elevation = 3.dp,
                                shape = RoundedCornerShape(25.dp),
                                spotColor = Color(0x18000000)
                            )
                            .clip(RoundedCornerShape(25.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0x0C000000), RoundedCornerShape(25.dp))
                            .clickable { onSelectDay(index) }
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = day.dayName,
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8A7D76)
                        )
                        Text(
                            text = day.dayNum,
                            fontFamily = PlusJakartaSans,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    }
                }
            }
        }
    }
}

/* ==========================================================================
   3. HERO LIVE POSTURE CARD (34DP RADIUS & WHITE FLOATING ANGLE PILL)
   ========================================================================== */
@Composable
fun HeroPostureCard(
    state: PostureUiState,
    modifier: Modifier = Modifier
) {
    val haloCore by animateColorAsState(
        when (state.postureCategory) {
            PostureCategory.UPRIGHT -> StateGoodHaloCore
            PostureCategory.MILD_SLOUCH -> StateMildHaloCore
            PostureCategory.SLOUCH_ALERT -> StatePoorHaloCore
        }, label = "haloCore"
    )

    val haloMid by animateColorAsState(
        when (state.postureCategory) {
            PostureCategory.UPRIGHT -> StateGoodHaloMid
            PostureCategory.MILD_SLOUCH -> StateMildHaloMid
            PostureCategory.SLOUCH_ALERT -> StatePoorHaloMid
        }, label = "haloMid"
    )

    val accentBadgeColor by animateColorAsState(
        when (state.postureCategory) {
            PostureCategory.UPRIGHT -> AccentTerracotta
            PostureCategory.MILD_SLOUCH -> AccentAmber
            PostureCategory.SLOUCH_ALERT -> AccentTerracotta
        }, label = "accentColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(34.dp),
                spotColor = Color(0x226E4020)
            ),
        shape = RoundedCornerShape(34.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(Color(0x22D86843), CardBorderSubtle)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Status Indicator + Vector Waveform Squircle Box
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status Tag (Reference matching)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(accentBadgeColor.copy(alpha = 0.25f))
                            .padding(3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(accentBadgeColor)
                        )
                    }

                    Column {
                        Text(
                            text = state.statusHeadline,
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = accentBadgeColor,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = state.statusSubheadline,
                            fontFamily = PlusJakartaSans,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB87864),
                            letterSpacing = 0.4.sp
                        )
                    }
                }

                // Vector Waveform Squircle Box
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFFDF7F3))
                        .border(1.dp, Color(0x18D86843), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_waveform),
                        contentDescription = "Real-time biosensor pulse",
                        tint = AccentTerracotta,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Central Stage: Circular Disk with Cool-Frosted Tint & Avatar
            Box(
                modifier = Modifier
                    .size(230.dp)
                    .drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(haloCore, haloMid, Color.Transparent),
                                center = center,
                                radius = size.minDimension * 0.52f
                            )
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                // Frosted Circular Disk (Contrast against character)
                Box(
                    modifier = Modifier
                        .size(204.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(DiskFrostTop, DiskFrostMid, DiskFrostBottom)
                            )
                        )
                        .border(1.5.dp, Color(0xFFF0E5DF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    val tiltDegrees = (90 - state.angle).toFloat()
                    val smoothRotation by animateFloatAsState(
                        targetValue = tiltDegrees * -0.045f,
                        label = "spineTilt"
                    )
                    val smoothYShift by animateFloatAsState(
                        targetValue = tiltDegrees * 0.12f,
                        label = "spineY"
                    )

                    // Active Frame Picker (Zero Ghosting)
                    val frameRes = when {
                        state.angle >= 85 -> R.drawable.posture_stage_90
                        state.angle >= 76 -> R.drawable.posture_stage_80
                        state.angle >= 66 -> R.drawable.posture_stage_71
                        state.angle >= 53 -> R.drawable.posture_stage_60
                        else -> R.drawable.posture_stage_45
                    }

                    Image(
                        painter = painterResource(id = frameRes),
                        contentDescription = "Anatomical Posture Avatar",
                        modifier = Modifier
                            .size(146.dp)
                            .graphicsLayer {
                                rotationZ = smoothRotation
                                translationY = smoothYShift
                                transformOrigin = TransformOrigin(0.54f, 0.96f)
                            }
                    )
                }

                // EXACT WHITE FLOATING ANGLE BADGE FROM SCREENSHOT
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-8).dp, y = (-4).dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(20.dp),
                            spotColor = Color(0x30000000)
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0x15D86843), RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        Text(
                            text = "ANGLE",
                            fontFamily = PlusJakartaSans,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFBA6549),
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "${state.angle}°",
                            fontFamily = PlusJakartaSans,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AccentTerracotta
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Footer: Score Block & Diagnostic Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${state.integrityScore}%",
                        fontFamily = PlusJakartaSans,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AccentTerracotta
                    )
                    Text(
                        text = "INTEGRITY SCORE",
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFA58D84),
                        letterSpacing = 0.5.sp
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(AccentSlateDark)
                        .padding(horizontal = 16.dp, vertical = 11.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "DIAGNOSTIC TAB",
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextOnDark,
                        letterSpacing = 0.4.sp
                    )
                    Text(
                        text = "›",
                        fontFamily = PlusJakartaSans,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextOnDark
                    )
                }
            }
        }
    }
}

/* ==========================================================================
   4. LIVE SENSOR SIMULATOR & PRESETS (WITH VECTOR SLIDERS ICON)
   ========================================================================== */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulatorControlCard(
    angle: Int,
    onAngleChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CardBorderSubtle, CardBorderSubtle)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFBF4ED)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sliders),
                            contentDescription = "Sensor Simulator",
                            tint = AccentTerracotta,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Live Sensor Simulator",
                            fontFamily = PlusJakartaSans,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Test biomechanical states",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(AccentSageLight)
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "BLE Active",
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentSage
                    )
                }
            }

            // Custom Slider with White-ringed Terracotta Thumb
            Slider(
                value = angle.toFloat(),
                onValueChange = { onAngleChange(it.toInt()) },
                valueRange = 45f..90f,
                colors = SliderDefaults.colors(
                    thumbColor = AccentTerracotta,
                    activeTrackColor = AccentTerracotta,
                    inactiveTrackColor = AccentTerracottaLight
                ),
                thumb = {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .shadow(elevation = 6.dp, shape = CircleShape, spotColor = AccentTerracotta)
                            .clip(CircleShape)
                            .background(AccentTerracotta)
                            .border(3.dp, Color.White, CircleShape)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "45° Slouch", fontFamily = PlusJakartaSans, fontSize = 11.sp, color = TextTertiary)
                Text(text = "$angle°", fontFamily = PlusJakartaSans, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = AccentTerracotta)
                Text(text = "90° Upright", fontFamily = PlusJakartaSans, fontSize = 11.sp, color = TextTertiary)
            }

            // Presets with Micro-dots
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PresetPill(title = "90° Ideal", targetAngle = 90, currentAngle = angle, dotColor = AccentSage, onClick = onAngleChange)
                PresetPill(title = "80° Good", targetAngle = 80, currentAngle = angle, dotColor = AccentSage, onClick = onAngleChange)
                PresetPill(title = "71° Mild", targetAngle = 71, currentAngle = angle, dotColor = AccentAmber, onClick = onAngleChange)
                PresetPill(title = "55° Slouch", targetAngle = 55, currentAngle = angle, dotColor = AccentTerracotta, onClick = onAngleChange)
            }
        }
    }
}

@Composable
fun PresetPill(
    title: String,
    targetAngle: Int,
    currentAngle: Int,
    dotColor: Color,
    onClick: (Int) -> Unit
) {
    val isActive = targetAngle == currentAngle
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(if (isActive) AccentSlateDark else SurfaceTinted)
            .border(1.dp, if (isActive) AccentSlateDark else CardBorderSubtle, RoundedCornerShape(999.dp))
            .clickable { onClick(targetAngle) }
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Text(
            text = title,
            fontFamily = PlusJakartaSans,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isActive) TextOnDark else TextPrimary
        )
    }
}

/* ==========================================================================
   5. 2x2 TELEMETRIC METRICS GRID (ZERO EMOJIS - 100% VECTOR PRECISION)
   ========================================================================== */
@Composable
fun MetricsGrid(
    state: PostureUiState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Card 1: Spinal Stress Load (Peach Squircle + Spine Vector)
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CardBorderSubtle, CardBorderSubtle)))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFDF0EB)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_spine),
                                contentDescription = "Spinal Column",
                                tint = AccentTerracotta,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        val chipText = when (state.postureCategory) {
                            PostureCategory.UPRIGHT -> "Optimal"
                            PostureCategory.MILD_SLOUCH -> "Moderate"
                            PostureCategory.SLOUCH_ALERT -> "High"
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(AccentAmberLight)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(text = chipText, fontFamily = PlusJakartaSans, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AccentAmber)
                        }
                    }

                    Text(text = "Spinal Stress Load", fontFamily = PlusJakartaSans, fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(text = "${state.spinalLoadLbs}", fontFamily = PlusJakartaSans, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                        Text(text = " lbs", fontFamily = PlusJakartaSans, fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                    }

                    // 3-tier cervical strain gauge with micro-axis
                    Row(
                        modifier = Modifier.fillMaxWidth().height(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f).fillMaxSize().clip(RoundedCornerShape(3.dp)).background(AccentSage))
                        Box(modifier = Modifier.weight(1f).fillMaxSize().clip(RoundedCornerShape(3.dp)).background(if (state.angle <= 79) AccentAmber else Color(0xFFEADBCE)))
                        Box(modifier = Modifier.weight(1f).fillMaxSize().clip(RoundedCornerShape(3.dp)).background(if (state.angle <= 67) AccentTerracotta else Color(0xFFEADBCE)))
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "0", fontFamily = PlusJakartaSans, fontSize = 8.sp, color = TextTertiary)
                        Text(text = "18 Safe", fontFamily = PlusJakartaSans, fontSize = 8.sp, color = TextTertiary)
                        Text(text = "35+ lbs", fontFamily = PlusJakartaSans, fontSize = 8.sp, color = TextTertiary)
                    }
                }
            }

            // Card 2: Torso Inclination (Blue Squircle + Inclinometer Vector)
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CardBorderSubtle, CardBorderSubtle)))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFEFF5FD)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_inclinometer),
                                contentDescription = "Torso Tilt",
                                tint = Color(0xFF3A7BD5),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        val overDeg = state.torsoTiltDeg - 10
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(if (overDeg > 0) AccentTerracottaLight else AccentSageLight)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = if (overDeg > 0) "+$overDeg° Over" else "Safe",
                                fontFamily = PlusJakartaSans,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (overDeg > 0) AccentTerracotta else AccentSage
                            )
                        }
                    }

                    Text(text = "Torso Inclination", fontFamily = PlusJakartaSans, fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(text = "${state.torsoTiltDeg}°", fontFamily = PlusJakartaSans, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                        Text(text = " tilt", fontFamily = PlusJakartaSans, fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                    }

                    // Inclinometer track with target pin at 10 deg
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFFEADBCE))
                    ) {
                        val progress = (state.torsoTiltDeg.toFloat() / 45f).coerceIn(0f, 1f)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress)
                                .fillMaxSize()
                                .background(if (state.torsoTiltDeg > 15) AccentTerracotta else AccentAmber)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "0°", fontFamily = PlusJakartaSans, fontSize = 8.sp, color = TextTertiary)
                        Text(text = "10° Goal", fontFamily = PlusJakartaSans, fontSize = 8.sp, color = AccentSage, fontWeight = FontWeight.Bold)
                        Text(text = "45°", fontFamily = PlusJakartaSans, fontSize = 8.sp, color = TextTertiary)
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Card 3: Upright Session Time (Sage Squircle + Vector Clock)
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CardBorderSubtle, CardBorderSubtle)))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFEDF6F1)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_clock),
                                contentDescription = "Upright Clock",
                                tint = AccentSage,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(AccentSageLight)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(text = "Target: 45m", fontFamily = PlusJakartaSans, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AccentSage)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Upright Time", fontFamily = PlusJakartaSans, fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(text = "${state.uprightMinutes}", fontFamily = PlusJakartaSans, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                                Text(text = "m", fontFamily = PlusJakartaSans, fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                            }
                            Text(text = "93% goal met", fontFamily = PlusJakartaSans, fontSize = 9.sp, color = AccentSage, fontWeight = FontWeight.Bold)
                        }

                        // Circular Canvas Progress Ring with Centered 93%
                        Box(
                            modifier = Modifier.size(46.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                                drawArc(
                                    color = Color(0xFFEADBCE),
                                    startAngle = -90f,
                                    sweepAngle = 360f,
                                    useCenter = false,
                                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                                )
                                drawArc(
                                    color = AccentSage,
                                    startAngle = -90f,
                                    sweepAngle = 360f * 0.93f,
                                    useCenter = false,
                                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }
                            Text(text = "93%", fontFamily = PlusJakartaSans, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                    }
                }
            }

            // Card 4: Slouch Incidents (Amber Squircle + Shield Vector)
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CardBorderSubtle, CardBorderSubtle)))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFEF5EA)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_shield),
                                contentDescription = "Slouch Streak",
                                tint = AccentAmber,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(AccentAmberLight)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(text = "4d Streak", fontFamily = PlusJakartaSans, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AccentAmber)
                        }
                    }

                    Text(text = "Slouch Alerts", fontFamily = PlusJakartaSans, fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(text = "${state.slouchAlerts}", fontFamily = PlusJakartaSans, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                        Text(text = " alerts", fontFamily = PlusJakartaSans, fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                    }

                    // 4-Slot timeline beads (9A, 12P, 3P, 6P)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TimelineBead(label = "9A", isAlert = true)
                        TimelineBead(label = "12P", isAlert = true)
                        TimelineBead(label = "3P", isAlert = false)
                        TimelineBead(label = "6P", isAlert = false)
                    }

                    Text(text = "Safe posture kept", fontFamily = PlusJakartaSans, fontSize = 9.sp, color = AccentSage, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun TimelineBead(label: String, isAlert: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (isAlert) AccentTerracotta else AccentSage)
        )
        Text(text = label, fontFamily = PlusJakartaSans, fontSize = 8.sp, color = TextTertiary, fontWeight = FontWeight.Bold)
    }
}

/* ==========================================================================
   6. WEEKLY TREND BAR CHART (WITH 80% GUIDELINE)
   ========================================================================== */
@Composable
fun WeeklyTrendCard(
    selectedDay: Int,
    onSelectDay: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val weekData = listOf(
        Pair("Mon", 65),
        Pair("Tue", 78),
        Pair("Wed", 84),
        Pair("Thu", 92),
        Pair("Fri", 55),
        Pair("Sat", 70),
        Pair("Sun", 60)
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CardBorderSubtle, CardBorderSubtle)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "WEEKLY TELEMETRY", fontFamily = PlusJakartaSans, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextTertiary, letterSpacing = 0.5.sp)
                    Text(text = "Posture History & Trends", fontFamily = PlusJakartaSans, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(AccentSageLight)
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Text(text = "Avg. 81% Upright", fontFamily = PlusJakartaSans, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentSage)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 26.dp)
                        .height(1.dp)
                        .background(Color(0xFFEADBCE))
                )
                Text(
                    text = "80% Target",
                    fontFamily = PlusJakartaSans,
                    fontSize = 9.sp,
                    color = TextTertiary,
                    modifier = Modifier.align(Alignment.TopEnd).offset(y = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    weekData.forEachIndexed { index, (day, pct) ->
                        val isSelected = index == selectedDay
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSelectDay(index) },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "$pct%",
                                fontFamily = PlusJakartaSans,
                                fontSize = 9.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) AccentTerracotta else TextTertiary
                            )

                            Box(
                                modifier = Modifier
                                    .width(18.dp)
                                    .height(80.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFF7EFE9)),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height((80f * (pct / 100f)).dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            if (isSelected) AccentTerracotta
                                            else if (pct >= 80) AccentSage
                                            else AccentAmber
                                        )
                                )
                            }

                            Text(
                                text = day,
                                fontFamily = PlusJakartaSans,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) TextPrimary else TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

/* ==========================================================================
   7. AI ERGONOMIST PROGNOSIS CARD (WITH SPARKLE & CHAT VECTOR ICONS)
   ========================================================================== */
@Composable
fun AiPrognosisCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceTinted),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(AccentTerracotta.copy(alpha = 0.3f), CardBorderSubtle)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sparkle),
                        contentDescription = "AI Sparkle",
                        tint = AccentTerracotta,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "AI ERGONOMIST",
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AccentTerracotta
                    )
                }
                Text(
                    text = "Prognosis: +15% Alignment",
                    fontFamily = PlusJakartaSans,
                    fontSize = 10.sp,
                    color = AccentSage,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Gemini Biomechanical Insight",
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = "\"Peak slouch occurs around 9:00 AM due to desk height. Raising monitor by 2.5cm will alleviate 8.2 lbs cervical tension.\"",
                fontFamily = PlusJakartaSans,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 18.sp
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(AccentTerracotta)
                    .clickable { }
                    .padding(horizontal = 14.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chat),
                    contentDescription = "Ask advice",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Ask Ergonomist Advice",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextOnDark
                )
            }
        }
    }
}

/* ==========================================================================
   8. EXACT BOTTOM DOCK (MATCHING SCREENSHOT WITH PURE WHITE ACTIVE PILL)
   ========================================================================== */
@Composable
fun BottomDock(
    activeTab: PostureTab,
    onSelectTab: (PostureTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(999.dp),
                spotColor = Color(0x45000000)
            ),
        shape = RoundedCornerShape(999.dp),
        color = AccentSlateDark
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tab 1: Home (White Pill when active with pure vector home icon)
            DockTabItemExact(
                iconRes = R.drawable.ic_nav_home,
                label = "Home",
                isActive = activeTab == PostureTab.HOME,
                onClick = { onSelectTab(PostureTab.HOME) }
            )

            // Tab 2: History
            DockTabItemExact(
                iconRes = R.drawable.ic_nav_history,
                label = "History",
                isActive = activeTab == PostureTab.HISTORY,
                onClick = { onSelectTab(PostureTab.HISTORY) }
            )

            // Tab 3: Pod
            DockTabItemExact(
                iconRes = R.drawable.ic_nav_pod,
                label = "Pod",
                isActive = activeTab == PostureTab.POD,
                onClick = { onSelectTab(PostureTab.POD) }
            )

            // Tab 4: Profile
            DockTabItemExact(
                iconRes = R.drawable.ic_nav_profile,
                label = "Profile",
                isActive = activeTab == PostureTab.PROFILE,
                onClick = { onSelectTab(PostureTab.PROFILE) }
            )
        }
    }
}

@Composable
fun DockTabItemExact(
    iconRes: Int,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(if (isActive) Color.White else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = if (isActive) 18.dp else 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = if (isActive) TextPrimary else Color(0xFF8A827C),
            modifier = Modifier.size(18.dp)
        )
        AnimatedVisibility(visible = isActive) {
            Text(
                text = label,
                fontFamily = PlusJakartaSans,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
        }
    }
}

@Composable
fun PlaceholderTabContent(title: String, subtitle: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = title, fontFamily = PlusJakartaSans, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = subtitle, fontFamily = PlusJakartaSans, fontSize = 13.sp, color = TextSecondary, lineHeight = 20.sp)
            }
        }
    }
}
