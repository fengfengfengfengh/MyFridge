<template>
  <div 
    class="note-container"
    :data-note-id="note.id"
    :style="{
      left: note.positionX + '%',
      top: note.positionY + '%',
      transform: `rotate(${rotation}deg)`
    }"
    @mouseenter="isHovering = true"
    @mouseleave="isHovering = false"
  >
    <div 
      class="note"
      :class="{ hovering: isHovering, dragging }"
      :style="{
        backgroundColor: colorConfig.bg,
        color: colorConfig.text
      }"
      @mousedown="startDrag"
    >
      <!-- 便签内容 -->
      <div class="note-header">
        <span class="note-sticker">{{ note.icon }}</span>
        <span class="note-nickname">{{ note.nickname }}</span>
      </div>

      <div class="note-content">
        {{ note.content }}
      </div>

      <div class="note-footer">
        <span class="note-time">{{ formatTime(note.createdAt) }}</span>
      </div>

      <!-- 磁铁区域 -->
      <div class="note-actions">
        <button class="magnet-btn" @click="handleAddMagnet" :class="{ active: isHovering }">
          <span class="magnet-count">🧲 {{ note.magnetCount || 0 }}</span>
        </button>
        <button class="delete-btn" @click="$emit('delete')" :class="{ active: isHovering }">
          ✕
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { formatRelativeTime, getColorConfig } from '../utils/helpers'

export default {
  name: 'Note',
  props: {
    note: {
      type: Object,
      required: true
    }
  },
  emits: ['addMagnet', 'updatePosition', 'delete'],
  setup(props, { emit }) {
    const isHovering = ref(false)
    const dragging = ref(false)
    const dragOffset = ref({ x: 0, y: 0 })
    const rotation = ref(0)

    const colorConfig = computed(() => {
      return getColorConfig(props.note.color || 'cream')
    })

    onMounted(() => {
      // 随机旋转角度，使便签看起来更自然
      rotation.value = (Math.random() - 0.5) * 6
    })

    const formatTime = (timeStr) => {
      return formatRelativeTime(timeStr)
    }

    const handleAddMagnet = (e) => {
      e.stopPropagation()
      emit('addMagnet')
    }

    const startDrag = (e) => {
      if (e.target.closest('.note-actions')) return
      
      dragging.value = true
      const rect = e.target.closest('.note').getBoundingClientRect()
      dragOffset.value = {
        x: e.clientX - rect.left,
        y: e.clientY - rect.top
      }

      document.addEventListener('mousemove', onDrag)
      document.addEventListener('mouseup', stopDrag)
    }

    const onDrag = (e) => {
      if (!dragging.value) return

      const container = document.querySelector('.fridge-door')
      if (!container) return

      const containerRect = container.getBoundingClientRect()
      const x = e.clientX - containerRect.left - dragOffset.value.x
      const y = e.clientY - containerRect.top - dragOffset.value.y

      // 转换为百分比位置
      let percentX = (x / containerRect.width) * 100
      let percentY = (y / containerRect.height) * 100

      // 限制在容器内
      percentX = Math.max(0, Math.min(percentX, 85))
      percentY = Math.max(0, Math.min(percentY, 90))

      // 实时更新位置
      const noteElement = document.querySelector(`[data-note-id="${props.note.id}"]`)
      if (noteElement) {
        noteElement.style.left = percentX + '%'
        noteElement.style.top = percentY + '%'
      }
    }

    const stopDrag = (e) => {
      if (!dragging.value) return

      dragging.value = false
      document.removeEventListener('mousemove', onDrag)
      document.removeEventListener('mouseup', stopDrag)

      // 计算最终位置
      const container = document.querySelector('.fridge-door')
      if (!container) return

      const noteElement = document.querySelector(`[data-note-id="${props.note.id}"]`)
      if (noteElement) {
        const containerRect = container.getBoundingClientRect()
        const noteRect = noteElement.getBoundingClientRect()

        let x = e.clientX - containerRect.left - dragOffset.value.x
        let y = e.clientY - containerRect.top - dragOffset.value.y

        let percentX = (x / containerRect.width) * 100
        let percentY = (y / containerRect.height) * 100

        percentX = Math.max(0, Math.min(percentX, 85))
        percentY = Math.max(0, Math.min(percentY, 90))

        emit('updatePosition', props.note.id, percentX, percentY)
      }
    }

    onUnmounted(() => {
      document.removeEventListener('mousemove', onDrag)
      document.removeEventListener('mouseup', stopDrag)
    })

    return {
      colorConfig,
      isHovering,
      dragging,
      rotation,
      formatTime,
      handleAddMagnet,
      startDrag
    }
  }
}
</script>

<style scoped>
.note-container {
  position: absolute;
  width: 160px;
  cursor: move;
  transition: z-index 0.2s;
}

.note-container:hover {
  z-index: 50;
}

.note {
  position: relative;
  width: 100%;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15), 
              0 2px 4px rgba(0, 0, 0, 0.1);
  font-size: 13px;
  line-height: 1.5;
  word-wrap: break-word;
  white-space: pre-wrap;
  transition: all 0.3s ease;
  user-select: none;
  cursor: grab;
  min-height: 120px;
  display: flex;
  flex-direction: column;
}

.note:active {
  cursor: grabbing;
}

.note.hovering {
  transform: translateY(-8px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2),
              0 4px 8px rgba(0, 0, 0, 0.12);
}

.note.dragging {
  opacity: 0.95;
  box-shadow: 0 12px 25px rgba(0, 0, 0, 0.25);
}

.note-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  min-height: 24px;
}

.note-sticker {
  font-size: 18px;
  flex-shrink: 0;
}

.note-nickname {
  font-weight: 600;
  font-size: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
  opacity: 0.8;
}

.note-content {
  flex: 1;
  font-size: 13px;
  line-height: 1.5;
  margin-bottom: 8px;
  word-wrap: break-word;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
}

.note-footer {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 8px;
}

.note-time {
  font-size: 11px;
  opacity: 0.6;
  font-weight: 500;
}

.note-actions {
  display: flex;
  gap: 8px;
  opacity: 0;
  transition: opacity 0.2s ease;
}

.note:hover .note-actions {
  opacity: 1;
}

.magnet-btn,
.delete-btn {
  flex: 1;
  padding: 4px 8px;
  border: none;
  border-radius: 4px;
  font-size: 11px;
  background: rgba(255, 255, 255, 0.6);
  cursor: pointer;
  transition: all 0.2s ease;
  opacity: 0;
  transform: translateY(4px);
}

.note.hovering .magnet-btn,
.note.hovering .delete-btn {
  opacity: 1;
  transform: translateY(0);
}

.magnet-btn:hover {
  background: rgba(255, 200, 100, 0.8);
}

.delete-btn:hover {
  background: rgba(255, 150, 150, 0.8);
}

.magnet-count {
  display: block;
  white-space: nowrap;
  font-weight: 600;
}

/* 便签纸张感 */
.note::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: 
    repeating-linear-gradient(
      90deg,
      transparent,
      transparent 2px,
      rgba(255, 255, 255, 0.1) 2px,
      rgba(255, 255, 255, 0.1) 4px
    ),
    repeating-linear-gradient(
      0deg,
      transparent,
      transparent 2px,
      rgba(255, 255, 255, 0.05) 2px,
      rgba(255, 255, 255, 0.05) 4px
    );
  border-radius: 8px;
  pointer-events: none;
}

@media (max-width: 768px) {
  .note-container {
    width: 140px;
  }

  .note {
    font-size: 12px;
    padding: 12px;
    min-height: 100px;
  }

  .note-content {
    -webkit-line-clamp: 3;
  }
}
</style>
