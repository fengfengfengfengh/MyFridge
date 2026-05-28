<template>
  <div class="fridge-wrapper">
    <!-- 冰箱说话区域 -->
    <div class="fridge-say">
      <span class="say-icon">❄️</span>
      <span class="say-text">{{ currentSaying }}</span>
    </div>

    <!-- 冰箱+状态卡容器 -->
    <div class="fridge-with-card">
      <!-- 左上角悬浮状态卡 -->
      <div class="floating-status-card">
        <div class="status-header">
          <h3>📊 今日状态</h3>
          <button class="edit-btn" @click="$emit('editStatus')">✏️</button>
        </div>
        <div class="status-mini-grid">
          <div class="status-mini-item">
            <span class="label">精神</span>
            <div class="mini-bar">
              <div class="mini-fill" :style="{ width: (fridgeStatus?.energyLevel || 0) + '%' }"></div>
            </div>
            <span class="mini-value">{{ fridgeStatus?.energyLevel || 0 }}%</span>
          </div>
          <div class="status-mini-item">
            <span class="label">社交</span>
            <div class="mini-bar">
              <div class="mini-fill" :style="{ width: (fridgeStatus?.socialLevel || 0) + '%' }"></div>
            </div>
            <span class="mini-value">{{ fridgeStatus?.socialLevel || 0 }}%</span>
          </div>
          <div class="status-mini-item">
            <span class="label">创作</span>
            <div class="mini-bar">
              <div class="mini-fill" :style="{ width: (fridgeStatus?.creativityLevel || 0) + '%' }"></div>
            </div>
            <span class="mini-value">{{ fridgeStatus?.creativityLevel || 0 }}%</span>
          </div>
        </div>
        <div class="status-mini-info">
          <span v-if="fridgeStatus?.todayDrink">☕ {{ fridgeStatus.todayDrink }}</span>
          <span v-if="fridgeStatus?.todayKeyword">🏷️ {{ fridgeStatus.todayKeyword }}</span>
        </div>
      </div>

      <!-- 冰箱本体 -->
      <div class="fridge">
        <!-- 冰箱上部分（冷冻室） -->
        <div class="fridge-top">
          <div class="fridge-decorations">
            🧊 | ❄️ | 🧊
          </div>
        </div>

        <!-- 冰箱门 -->
        <div class="fridge-door">
          <div class="fridge-door-handle"></div>
          
          <div class="fridge-door-content">
            <!-- 便签区域 -->
            <div class="notes-area">
              <div v-if="notes.length === 0" class="empty-state">
                <p>🚪 冰箱门还空着</p>
                <p class="empty-hint">要不要贴下第一张便签？</p>
              </div>

              <!-- 便签列表 -->
              <div v-else class="notes-container">
                <Note 
                  v-for="note in notes" 
                  :key="note.id"
                  :note="note"
                  @addMagnet="$emit('addMagnet', note.id)"
                  @updatePosition="handleNotePosition"
                  @delete="$emit('deleteNote', note.id)"
                />
              </div>
            </div>
          </div>
        </div>

        <!-- 冰箱下部分（冷藏室） -->
        <div class="fridge-bottom">
          <div class="fridge-bottom-decor">
            🧊 | 💙 | 🧊
          </div>
        </div>

        <!-- 冰箱脚 -->
        <div class="fridge-legs">
          <div class="fridge-leg"></div>
          <div class="fridge-leg"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, watch } from 'vue'
import Note from './Note.vue'
import { getFridgeSaying } from '../utils/helpers'

export default {
  name: 'Fridge',
  components: {
    Note
  },
  props: {
    notes: {
      type: Array,
      required: true
    },
    fridgeStatus: {
      type: Object,
      required: true
    }
  },
  emits: ['addMagnet', 'updatePosition', 'deleteNote', 'editStatus'],
  setup(props, { emit }) {
    const currentSaying = ref('')

    const updateSaying = () => {
      currentSaying.value = getFridgeSaying(
        props.notes.length,
        props.fridgeStatus?.energyLevel || 50
      )
    }

    const getEnergyColor = (level) => {
      if (level >= 70) return '#7cb9e8'
      if (level >= 40) return '#ffcc99'
      return '#ff9999'
    }

    const handleNotePosition = (noteId, x, y) => {
      emit('updatePosition', noteId, x, y)
    }

    onMounted(() => {
      updateSaying()
    })

    watch(() => props.notes.length, () => {
      updateSaying()
    })

    watch(() => props.fridgeStatus?.energyLevel, () => {
      updateSaying()
    })

    return {
      currentSaying,
      getEnergyColor,
      handleNotePosition
    }
  }
}
</script>

<style scoped>
.fridge-wrapper {
  position: relative;
  width: 100%;
  max-width: 480px;
  margin: 0 auto;
}

/* 冰箱+状态卡容器 */
.fridge-with-card {
  position: relative;
}

/* 左上角悬浮状态卡 */
.floating-status-card {
  position: absolute;
  top: -10px;
  left: -10px;
  z-index: 100;
  background: linear-gradient(135deg, #fffacd 0%, #fff5d6 100%);
  border-radius: 16px;
  padding: 14px;
  box-shadow: 
    0 8px 24px rgba(200, 160, 80, 0.25),
    0 2px 6px rgba(0, 0, 0, 0.08);
  min-width: 180px;
  transform: rotate(-2deg);
}

.floating-status-card::before {
  content: '📌';
  position: absolute;
  top: -6px;
  right: 12px;
  font-size: 16px;
}

.floating-status-card .status-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.floating-status-card .status-header h3 {
  margin: 0;
  font-size: 12px;
  color: #c9a086;
  font-weight: 600;
}

.floating-status-card .edit-btn {
  background: linear-gradient(135deg, #ffd4b8, #ffb899);
  border: none;
  border-radius: 8px;
  padding: 4px 8px;
  font-size: 10px;
  color: #8b5a3c;
  cursor: pointer;
  box-shadow: 0 2px 6px rgba(200, 130, 80, 0.2);
}

.floating-status-card .edit-btn:hover {
  transform: scale(1.05);
}

.status-mini-grid {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
}

.status-mini-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.status-mini-item .label {
  font-size: 9px;
  color: #b8a080;
}

.mini-bar {
  width: 100%;
  height: 6px;
  background: rgba(200, 160, 100, 0.2);
  border-radius: 3px;
  overflow: hidden;
}

.mini-fill {
  height: 100%;
  background: linear-gradient(90deg, #ffb899, #ffd4b8);
  border-radius: 3px;
  transition: width 0.5s ease;
}

.mini-value {
  font-size: 11px;
  font-weight: 600;
  color: #e8a060;
}

.status-mini-info {
  display: flex;
  gap: 10px;
  font-size: 10px;
  color: #a08060;
}

/* 冰箱说话区域 - 可爱气泡风格 */
.fridge-say {
  text-align: center;
  margin-bottom: 24px;
  padding: 16px 24px;
  background: linear-gradient(135deg, #fff9f0 0%, #fff5eb 100%);
  border-radius: 24px;
  box-shadow: 
    0 8px 32px rgba(200, 150, 100, 0.15),
    0 2px 8px rgba(0, 0, 0, 0.05),
    inset 0 -2px 0 rgba(255, 200, 150, 0.3);
  animation: slideUp 0.6s ease;
  border: 2px dashed rgba(255, 180, 120, 0.3);
  position: relative;
}

.fridge-say::before {
  content: '';
  position: absolute;
  top: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 0;
  border-left: 10px solid transparent;
  border-right: 10px solid transparent;
  border-bottom: 10px solid #fff5eb;
}

.say-icon {
  display: inline-block;
  font-size: 20px;
  margin-right: 8px;
  animation: float 3s ease-in-out infinite;
}

.say-text {
  color: #8b7355;
  font-size: 14px;
  font-weight: 500;
  line-height: 1.6;
}

/* 冰箱主体 - 奶油色圆润冰箱 */
.fridge {
  background: linear-gradient(180deg, #fff8f3 0%, #ffe8d6 50%, #ffdcc0 100%);
  border-radius: 40px 40px 30px 30px;
  overflow: visible;
  box-shadow: 
    0 20px 60px rgba(200, 150, 100, 0.25),
    0 8px 24px rgba(0, 0, 0, 0.08),
    inset 0 2px 0 rgba(255, 255, 255, 0.8),
    inset 0 -4px 0 rgba(200, 150, 100, 0.1);
  border: 3px solid rgba(255, 200, 160, 0.5);
  position: relative;
}

/* 冰箱表面纹理 */
.fridge::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    radial-gradient(circle at 20% 20%, rgba(255,255,255,0.3) 0%, transparent 50%),
    radial-gradient(circle at 80% 80%, rgba(255,200,150,0.2) 0%, transparent 50%);
  border-radius: inherit;
  pointer-events: none;
}

/* 冰箱顶部 - 冷冻室 */
.fridge-top {
  background: linear-gradient(180deg, #fff0e5, #ffe4d0);
  padding: 14px 20px;
  text-align: center;
  color: #c9a086;
  font-size: 12px;
  border-bottom: 3px dashed rgba(255, 180, 130, 0.4);
  border-radius: 40px 40px 0 0;
  position: relative;
}

.fridge-decorations {
  font-weight: 500;
  letter-spacing: 6px;
  opacity: 0.7;
}

/* 可爱装饰 */
.fridge-decorations::before,
.fridge-decorations::after {
  content: '✨';
  letter-spacing: 2px;
}

/* 冰箱门 */
.fridge-door {
  position: relative;
  min-height: 520px;
  background: linear-gradient(160deg, #fff5ee 0%, #ffe8d8 100%);
  padding: 0;
  margin: 0 16px;
  border-radius: 24px 24px 20px 20px;
  box-shadow: 
    inset 0 4px 12px rgba(255, 200, 150, 0.3),
    inset 0 -2px 0 rgba(255, 180, 130, 0.2);
}

/* 冰箱门把手 - 可爱的圆润把手 */
.fridge-door-handle {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  width: 14px;
  height: 100px;
  background: linear-gradient(90deg, #ffb899, #ffd4b8, #ffb899);
  border-radius: 7px;
  box-shadow: 
    0 4px 12px rgba(200, 130, 80, 0.3),
    inset 0 2px 0 rgba(255, 255, 255, 0.5),
    inset 0 -2px 0 rgba(200, 130, 80, 0.2);
  z-index: 10;
}

/* 把手装饰 */
.fridge-door-handle::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 6px;
  height: 6px;
  background: rgba(200, 130, 80, 0.3);
  border-radius: 50%;
}

.fridge-door-content {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  position: relative;
  z-index: 2;
}

/* 今日状态卡 - 奶油便签风格 */
.status-card {
  background: linear-gradient(135deg, #fffacd 0%, #fff5d6 100%);
  border: none;
  border-radius: 20px;
  padding: 18px;
  box-shadow: 
    0 6px 20px rgba(200, 160, 80, 0.2),
    0 2px 6px rgba(0, 0, 0, 0.05),
    inset 0 -3px 0 rgba(200, 160, 80, 0.15);
  color: #8b7355;
  position: relative;
  transform: rotate(-1deg);
}

/* 便签装饰角 */
.status-card::before {
  content: '📌';
  position: absolute;
  top: -8px;
  right: 16px;
  font-size: 20px;
}

.status-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.status-header h3 {
  margin: 0;
  font-size: 14px;
  color: #c9a086;
  font-weight: 600;
}

.edit-btn {
  background: linear-gradient(135deg, #ffd4b8, #ffb899);
  border: none;
  border-radius: 12px;
  padding: 6px 12px;
  font-size: 12px;
  color: #8b5a3c;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 3px 8px rgba(200, 130, 80, 0.25);
}

.edit-btn:hover {
  transform: scale(1.08) rotate(-2deg);
  box-shadow: 0 5px 15px rgba(200, 130, 80, 0.35);
}

.status-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  margin-bottom: 12px;
}

.status-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
  background: rgba(255, 255, 255, 0.6);
  padding: 10px;
  border-radius: 14px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.status-label {
  font-size: 11px;
  color: #b8a080;
  font-weight: 500;
}

.status-value {
  font-size: 18px;
  font-weight: 700;
  color: #e8a060;
  text-shadow: 0 1px 2px rgba(200, 130, 80, 0.2);
}

.status-bar {
  width: 100%;
  height: 8px;
  background: rgba(200, 160, 100, 0.2);
  border-radius: 4px;
  overflow: hidden;
}

.status-fill {
  height: 100%;
  border-radius: 4px;
  transition: all 0.5s ease;
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.5);
}

.status-info {
  font-size: 12px;
  color: #a08060;
  line-height: 1.8;
  background: rgba(255, 255, 255, 0.5);
  padding: 10px 12px;
  border-radius: 12px;
}

.status-info p {
  margin: 2px 0;
}

.status-info strong {
  color: #c9a086;
  font-weight: 600;
}

/* 便签区域 - 冰箱门上的便签 */
.notes-area {
  min-height: 300px;
  position: relative;
  background: 
    repeating-linear-gradient(
      0deg,
      transparent,
      transparent 30px,
      rgba(255, 200, 150, 0.1) 30px,
      rgba(255, 200, 150, 0.1) 31px
    );
  border-radius: 16px;
  padding: 12px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 280px;
  color: #c9b8a8;
  text-align: center;
}

.empty-state p:first-child {
  font-size: 48px;
  margin-bottom: 12px;
}

.empty-hint {
  font-size: 14px;
  color: #d4c4b4;
}

.notes-container {
  position: relative;
  min-height: 280px;
  height: 100%;
}

/* 便签数量提示 */
.notes-count {
  position: absolute;
  bottom: 10px;
  right: 10px;
  z-index: 10;
}

.count-badge {
  background: linear-gradient(135deg, #ffd4b8, #ffb899);
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 12px;
  color: #8b5a3c;
  box-shadow: 0 3px 10px rgba(200, 130, 80, 0.3);
}

/* 冰箱底部装饰 */
.fridge-bottom {
  background: linear-gradient(180deg, #ffdcc0, #ffc8a0);
  padding: 12px 20px;
  text-align: center;
  color: #c9a086;
  font-size: 12px;
  border-radius: 0 0 30px 30px;
  margin-top: -1px;
}

.fridge-bottom-decor {
  font-weight: 500;
  letter-spacing: 6px;
  opacity: 0.7;
}

/* 冰箱脚 - 可爱的圆润脚 */
.fridge-legs {
  display: flex;
  justify-content: space-between;
  padding: 0 30px;
  margin-top: -5px;
}

.fridge-leg {
  width: 45px;
  height: 18px;
  background: linear-gradient(180deg, #ffb899, #e8a080);
  border-radius: 0 0 12px 12px;
  box-shadow: 0 4px 8px rgba(200, 130, 80, 0.25);
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes float {
  0%, 100% { transform: translateY(0) rotate(-3deg); }
  50% { transform: translateY(-4px) rotate(3deg); }
}

@media (max-width: 768px) {
  .fridge {
    border-radius: 30px 30px 20px 20px;
  }

  .fridge-door {
    margin: 0 12px;
    min-height: 450px;
    border-radius: 18px 18px 16px 16px;
  }

  .fridge-door-handle {
    height: 80px;
    width: 12px;
  }

  .status-grid {
    grid-template-columns: repeat(3, 1fr);
    gap: 8px;
  }

  .status-item {
    padding: 8px;
  }

  .status-value {
    font-size: 16px;
  }
}
</style>