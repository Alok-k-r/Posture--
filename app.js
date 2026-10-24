/* ==========================================================================
   POSTURE+ INTERACTIVE SIMULATOR & TELEMETRY ENGINE
   Exact Live Posture Component Integration
   ========================================================================== */

document.addEventListener('DOMContentLoaded', () => {
  // DOM Elements - Live Telemetry
  const angleSlider = document.getElementById('angleSlider');
  const sliderValDisplay = document.getElementById('sliderValDisplay');
  const currentAngleText = document.getElementById('currentAngleText');
  const alignmentScoreText = document.getElementById('alignmentScoreText');
  const statusBadgeTag = document.getElementById('statusBadgeTag');
  const statusLabel = document.getElementById('statusLabel');
  const statusDot = statusBadgeTag ? statusBadgeTag.querySelector('.status-dot') : null;

  // Avatar SVG Elements
  const femaleHeadGroup = document.getElementById('femaleHeadGroup') || document.getElementById('avatarTiltGroup');
  const avatarTiltGroup = femaleHeadGroup;
  const femaleEarWire = document.getElementById('femaleEarWire') || document.getElementById('earHookWire');
  const femaleEarPod = document.getElementById('femaleEarPod') || document.getElementById('earPodBody');
  const pulseIconBox = document.querySelector('.pulse-icon-box');

  // Telemetry Cards
  const spinalLoadValue = document.getElementById('spinalLoadValue');
  const spinalLoadChip = document.getElementById('spinalLoadChip');
  const spinalLoadHint = document.getElementById('spinalLoadHint');
  const strainSegLow = document.getElementById('strainSegLow');
  const strainSegMed = document.getElementById('strainSegMed');
  const strainSegHigh = document.getElementById('strainSegHigh');

  const torsoTiltValue = document.getElementById('torsoTiltValue');
  const torsoTiltHint = document.getElementById('torsoTiltHint');
  const torsoDeltaChip = document.getElementById('torsoDeltaChip');
  const torsoMeterFill = document.getElementById('torsoMeterFill');
  const slouchCountValue = document.getElementById('slouchCountValue');
  const progressRingFill = document.getElementById('progressRingFill');

  // Controls & Buttons
  const presetPills = document.querySelectorAll('.preset-pill');
  const datePills = document.querySelectorAll('.date-pill');
  const selectedDateTitle = document.getElementById('selectedDateTitle');
  const diagnosticTabBtn = document.getElementById('diagnosticTabBtn');
  const syncNowBtn = document.getElementById('syncNowBtn');
  const bleStatusBtn = document.getElementById('bleStatusBtn');
  const batteryPercent = document.getElementById('batteryPercent');
  const dockTabs = document.querySelectorAll('.dock-tab');
  const tabViews = document.querySelectorAll('.tab-view');

  const heroPostureCard = document.getElementById('heroPostureCard');
  const statusSubLabel = document.getElementById('statusSubLabel');

  // -------------------------------------------------------------------------
  // 1. DYNAMIC POSTURE & AVATAR TILT ENGINE
  // -------------------------------------------------------------------------
  function updatePostureMetrics(angle) {
    angle = parseInt(angle, 10);

    // 1. Update text displays
    if (sliderValDisplay) sliderValDisplay.textContent = `${angle}°`;
    if (currentAngleText) currentAngleText.innerHTML = `${angle}&deg;`;

    // 2. Alignment / Integrity Score calculation (calibrated so 71° = 75%)
    let score;
    if (angle === 71) {
      score = 75;
    } else if (angle >= 71) {
      score = Math.round(75 + ((angle - 71) / 19) * 24); // 71° -> 75%, 90° -> 99%
    } else {
      score = Math.round(38 + ((angle - 45) / 26) * 36); // 45° -> 38%, 70° -> 74%
    }
    if (alignmentScoreText) alignmentScoreText.textContent = `${score}%`;

    // 3. Cervical Spinal Load (Kapandji neck flexion strain formula)
    const tiltDegrees = 90 - angle;
    if (torsoTiltValue) torsoTiltValue.innerHTML = `${tiltDegrees}&deg; <small>tilt</small>`;

    let loadLbs = 10;
    if (tiltDegrees > 0) {
      loadLbs = (10 + (tiltDegrees * 0.95)).toFixed(1);
    }
    if (spinalLoadValue) spinalLoadValue.innerHTML = `${loadLbs} <small>lbs</small>`;

    // 4. Dynamic Ambience State Management (Green vs Amber vs Red)
    if (angle >= 80) {
      // STATE 1: UPRIGHT OPTIMAL (Soft Sage Green Ambience)
      if (heroPostureCard) {
        heroPostureCard.classList.add('state-good');
        heroPostureCard.classList.remove('state-mild', 'state-poor');
      }
      if (statusLabel) statusLabel.textContent = 'UPRIGHT OPTIMAL';
      if (statusSubLabel) statusSubLabel.textContent = 'ALIGNMENT OPTIMAL';

      const spinalTrend = document.getElementById('spinalTrend');
      if (spinalLoadChip) {
        spinalLoadChip.textContent = 'Optimal';
        spinalLoadChip.className = 'mini-status-chip good';
      }
      if (spinalTrend) spinalTrend.textContent = 'Safe <18';
      if (spinalLoadHint) {
        spinalLoadHint.textContent = 'Optimal: under 18 lbs cervical strain';
      }
      if (strainSegLow) strainSegLow.className = 'strain-seg seg-low active';
      if (strainSegMed) strainSegMed.className = 'strain-seg seg-med';
      if (strainSegHigh) strainSegHigh.className = 'strain-seg seg-high';

      if (torsoDeltaChip) {
        torsoDeltaChip.innerHTML = `Within 5&deg;`;
        torsoDeltaChip.className = 'mini-delta-chip good';
      }
      if (torsoMeterFill) {
        torsoMeterFill.style.width = '24%';
        torsoMeterFill.style.background = '#DE6740';
      }
      if (torsoTiltHint) {
        torsoTiltHint.textContent = 'Within safe 5° baseline';
        torsoTiltHint.className = 'metric-hint success';
      }

    } else if (angle >= 68) {
      // STATE 2: MILD SLOUCH (Soft Warm Amber Ambience - Prototype Exact)
      if (heroPostureCard) {
        heroPostureCard.classList.add('state-mild');
        heroPostureCard.classList.remove('state-good', 'state-poor');
      }
      if (statusLabel) statusLabel.textContent = 'MILD SLOUCH';
      if (statusSubLabel) statusSubLabel.textContent = 'CLINICAL PRECISION';

      const spinalTrend = document.getElementById('spinalTrend');
      if (spinalLoadChip) {
        spinalLoadChip.textContent = 'Moderate';
        spinalLoadChip.className = 'mini-status-chip moderate';
      }
      if (spinalTrend) spinalTrend.textContent = 'Mild +10';
      if (spinalLoadHint) {
        spinalLoadHint.textContent = 'Cervical strain within moderate limit';
      }
      if (strainSegLow) strainSegLow.className = 'strain-seg seg-low active';
      if (strainSegMed) strainSegMed.className = 'strain-seg seg-med active';
      if (strainSegHigh) strainSegHigh.className = 'strain-seg seg-high';

      const overBaseline = tiltDegrees > 10 ? tiltDegrees - 10 : 1;
      if (torsoDeltaChip) {
        torsoDeltaChip.innerHTML = `+${overBaseline}&deg; Over`;
        torsoDeltaChip.className = 'mini-delta-chip warning';
      }
      if (torsoMeterFill) {
        torsoMeterFill.style.width = '63%';
        torsoMeterFill.style.background = '#E08528';
      }
      if (torsoTiltHint) {
        torsoTiltHint.textContent = 'Exceeds 10° target threshold';
        torsoTiltHint.className = 'metric-hint warning';
      }

    } else {
      // STATE 3: SLOUCH ALERT (Soft Rose-Red Ambience)
      if (heroPostureCard) {
        heroPostureCard.classList.add('state-poor');
        heroPostureCard.classList.remove('state-good', 'state-mild');
      }
      if (statusLabel) statusLabel.textContent = 'SLOUCH ALERT';
      if (statusSubLabel) statusSubLabel.textContent = 'HIGH CERVICAL STRAIN';

      const spinalTrend = document.getElementById('spinalTrend');
      if (spinalLoadChip) {
        spinalLoadChip.textContent = 'High Strain';
        spinalLoadChip.className = 'mini-status-chip warning';
      }
      if (spinalTrend) spinalTrend.textContent = 'High Load';
      if (spinalLoadHint) {
        spinalLoadHint.textContent = 'Severe: exceeds safe 35 lbs threshold';
      }
      if (strainSegLow) strainSegLow.className = 'strain-seg seg-low active';
      if (strainSegMed) strainSegMed.className = 'strain-seg seg-med active';
      if (strainSegHigh) strainSegHigh.className = 'strain-seg seg-high active';

      const overBaseline = tiltDegrees - 10;
      if (torsoDeltaChip) {
        torsoDeltaChip.innerHTML = `+${overBaseline}&deg; Critical`;
        torsoDeltaChip.className = 'mini-delta-chip danger';
      }
      if (torsoMeterFill) {
        torsoMeterFill.style.width = '92%';
        torsoMeterFill.style.background = '#D44242';
      }
      if (torsoTiltHint) {
        torsoTiltHint.textContent = 'Immediate neck correction advised';
        torsoTiltHint.className = 'metric-hint warning';
      }
    }

    // 5. Dynamic Single-Frame Spine Kinematics (Zero Ghosting / Double-Face Eliminated)
    // 90° (Upright Optimal): Frame 90
    // 80° (Good Alignment): Frame 80
    // 71° (Mild Slouch - Prototype Exact): Frame 71
    // 60° (Slouch Alert): Frame 60
    // 45° (Severe Slouch): Frame 45
    const stage = document.getElementById('characterFramesStage');
    const frame90 = document.getElementById('postureFrame90');
    const frame80 = document.getElementById('postureFrame80');
    const frame71 = document.getElementById('postureFrame71');
    const frame60 = document.getElementById('postureFrame60');
    const frame45 = document.getElementById('postureFrame45');
    const allFrames = [frame90, frame80, frame71, frame60, frame45];

    // Continuous physical spine flexion: smooth forward tilt & compression
    // Anchored at the neck base (54% 96%) so every 1° of slider movement feels fluid, organic & alive
    if (stage) {
      const tilt = (90 - angle) * -0.045;
      const yShift = (90 - angle) * 0.055;
      stage.style.transform = `rotate(${tilt.toFixed(2)}deg) translateY(${yShift.toFixed(2)}px)`;
    }

    // Determine the single active frame:
    // Solid 100% opacity locks ensure ZERO ghosting or double exposure when stopping at 76°, 79°, etc.
    let activeFrame = frame71;
    if (angle >= 85) {
      activeFrame = frame90; // 85° - 90°
    } else if (angle >= 76) {
      activeFrame = frame80; // 76° - 84° (100% single Frame 80 at 76° & 79°)
    } else if (angle >= 66) {
      activeFrame = frame71; // 66° - 75°
    } else if (angle >= 53) {
      activeFrame = frame60; // 53° - 65°
    } else {
      activeFrame = frame45; // Below 53°
    }

    allFrames.forEach(f => {
      if (f) {
        if (f === activeFrame) {
          f.style.display = 'block';
          f.style.opacity = '1';
          f.classList.add('active');
        } else {
          f.style.display = 'none';
          f.style.opacity = '0';
          f.classList.remove('active');
        }
      }
    });

    // Update preset pills active state
    presetPills.forEach(pill => {
      if (parseInt(pill.dataset.angle, 10) === angle) {
        pill.classList.add('active');
      } else {
        pill.classList.remove('active');
      }
    });
  }

  // -------------------------------------------------------------------------
  // 2. SLIDER & PRESET EVENT LISTENERS
  // -------------------------------------------------------------------------
  if (angleSlider) {
    angleSlider.addEventListener('input', (e) => {
      updatePostureMetrics(e.target.value);
    });
  }

  presetPills.forEach(pill => {
    pill.addEventListener('click', () => {
      const targetAngle = pill.dataset.angle;
      if (angleSlider) angleSlider.value = targetAngle;
      updatePostureMetrics(targetAngle);
    });
  });

  // Diagnostic Tab Button Click (Smooth Scroll / Action)
  if (diagnosticTabBtn) {
    diagnosticTabBtn.addEventListener('click', () => {
      // Micro-animation click effect
      diagnosticTabBtn.style.transform = 'scale(0.96)';
      setTimeout(() => {
        diagnosticTabBtn.style.transform = 'translateY(-1px)';
      }, 150);

      // Scroll smoothly to metrics grid
      const metricsGrid = document.querySelector('.metrics-grid');
      if (metricsGrid) {
        metricsGrid.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }
    });
  }

  // Pulse Waveform Icon Click (Simulate Instant Sensor Reading)
  if (pulseIconBox) {
    pulseIconBox.addEventListener('click', () => {
      pulseIconBox.style.transform = 'scale(0.9)';
      pulseIconBox.style.color = 'var(--accent-terracotta)';

      setTimeout(() => {
        pulseIconBox.style.transform = 'scale(1)';
        pulseIconBox.style.color = 'var(--accent-amber)';
      }, 300);
    });
  }

  // -------------------------------------------------------------------------
  // 3. DATE STRIP INTERACTION (HISTORY PREVIEWS)
  // -------------------------------------------------------------------------
  datePills.forEach(pill => {
    pill.addEventListener('click', () => {
      datePills.forEach(p => p.classList.remove('active'));
      pill.classList.add('active');

      const dateStr = pill.dataset.date;
      const isToday = pill.querySelector('.day-name')?.textContent === 'Thu';
      if (selectedDateTitle) {
        selectedDateTitle.innerHTML = `${dateStr} &bull; ${isToday ? 'Today' : 'History Log'}`;
      }

      // Simulate historical slouch data variation
      if (slouchCountValue) {
        if (!isToday) {
          slouchCountValue.textContent = Math.floor(Math.random() * 5);
        } else {
          slouchCountValue.textContent = '2';
        }
      }
    });
  });

  // -------------------------------------------------------------------------
  // 4. HARDWARE SYNC & BLE SIMULATION
  // -------------------------------------------------------------------------
  if (syncNowBtn) {
    syncNowBtn.addEventListener('click', () => {
      const icon = syncNowBtn.querySelector('svg');
      if (icon) {
        icon.style.transition = 'transform 0.6s ease';
        icon.style.transform = 'rotate(360deg)';
      }

      const textSpan = syncNowBtn.querySelector('span');
      if (textSpan) textSpan.textContent = 'Syncing...';

      setTimeout(() => {
        if (icon) icon.style.transform = 'rotate(0deg)';
        if (textSpan) textSpan.textContent = 'Synced';
        setTimeout(() => {
          if (textSpan) textSpan.textContent = 'Sync';
        }, 1500);
      }, 700);
    });
  }

  // BLE Pod Battery toggle simulation
  if (bleStatusBtn) {
    bleStatusBtn.addEventListener('click', () => {
      const isConn = bleStatusBtn.classList.contains('connected');
      const pulseDot = bleStatusBtn.querySelector('.pulse-dot');
      const subtitle = document.getElementById('livePodSubtitle');

      if (isConn) {
        bleStatusBtn.classList.remove('connected');
        if (pulseDot) pulseDot.style.background = '#A69B94';
        if (subtitle) subtitle.textContent = 'Pod Disconnected';
        if (batteryPercent) batteryPercent.textContent = '--';
      } else {
        bleStatusBtn.classList.add('connected');
        if (pulseDot) pulseDot.style.background = 'var(--accent-sage)';
        if (subtitle) subtitle.textContent = 'Posture+ Active';
        if (batteryPercent) batteryPercent.textContent = '84%';
      }
    });
  }

  // -------------------------------------------------------------------------
  // 5. BOTTOM DOCK TAB SWITCHING
  // -------------------------------------------------------------------------
  dockTabs.forEach(tab => {
    tab.addEventListener('click', () => {
      dockTabs.forEach(t => t.classList.remove('active'));
      tab.classList.add('active');

      const targetId = tab.dataset.tab;
      tabViews.forEach(view => {
        if (view.id === targetId) {
          view.classList.add('active');
        } else {
          view.classList.remove('active');
        }
      });
    });
  });

  // -------------------------------------------------------------------------
  // 6. WEEKLY TREND BAR CHART INTERACTION
  // -------------------------------------------------------------------------
  const barCols = document.querySelectorAll('.bar-col');
  const trendAvgBadge = document.querySelector('.trend-avg-badge');
  barCols.forEach(col => {
    col.addEventListener('click', () => {
      barCols.forEach(c => c.classList.remove('active'));
      col.classList.add('active');
      const day = col.querySelector('.bar-day')?.textContent;
      const pct = col.querySelector('.bar-pct')?.textContent;
      if (trendAvgBadge && day && pct) {
        trendAvgBadge.textContent = `${day}: ${pct} Upright`;
      }
    });
  });

  // Initialize with Default (71 deg, exact 75% score from prototype)
  updatePostureMetrics(71);
});
