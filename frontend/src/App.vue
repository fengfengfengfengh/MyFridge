<template>
  <div class="app-container">
    <!-- 顶部区域 -->
    <header class="app-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="main-title">❄️ 我的赛博冰箱</h1>
          <p class="subtitle">来都来了，贴张便签再走吧</p>
        </div>
      </div>
    </header>

    <!-- 主内容区域 -->
    <main class="app-main">
      <div class="fridge-container">
        <!-- 冰箱主体 -->
        <Fridge 
          :notes="notes" 
          :fridgeStatus="fridgeStatus"
          @addMagnet="handleAddMagnet"
          @updatePosition="handleUpdatePosition"
          @deleteNote="handleDeleteNote"
          @editStatus="showStatusModal = true"
        />

        <!-- 贴便签按钮 -->
        <button class="add-note-btn" @click="showNoteModal = true">
          ✏️ 贴一张便签
        </button>
      </div>
    </main>

    <!-- 便签模态框 -->
    <NoteModal 
      v-if="showNoteModal"
      @create="handleCreateNote"
      @close="showNoteModal = false"
    />

    <!-- 编辑状态模态框 -->
    <StatusModal 
      v-if="showStatusModal"
      :status="fridgeStatus"
      @update="handleUpdateStatus"
      @close="showStatusModal = false"
    />
  </div>
</template>

<script>
import { ref, onMounted, computed, watch } from 'vue'
import Fridge from './components/Fridge.vue'
import NoteModal from './components/NoteModal.vue'
import StatusModal from './components/StatusModal.vue'
import { noteAPI, fridgeStatusAPI } from './services/api'
import { saveToStorage, loadFromStorage } from './utils/helpers'

export default {
  name: 'App',
  components: {
    Fridge,
    NoteModal,
    StatusModal
  },
  setup() {
    const notes = ref([])
    const fridgeStatus = ref(null)
    const showNoteModal = ref(false)
    const showStatusModal = ref(false)
    const loading = ref(false)

    // 加载所有便签
    const loadNotes = async () => {
      try {
        const response = await noteAPI.getAllNotes()
        notes.value = response.data || []
        // 应用本地保存的位置信息
        notes.value.forEach(note => {
          const saved = loadFromStorage(`note-pos-${note.id}`)
          if (saved) {
            note.positionX = saved.x
            note.positionY = saved.y
          }
        })
      } catch (error) {
        console.error('加载便签失败:', error)
        // 如果后端不可用，从 localStorage 加载
        const saved = loadFromStorage('notes-cache', [])
        notes.value = saved
      }
    }

    // 加载冰箱状态
    const loadFridgeStatus = async () => {
      try {
        const response = await fridgeStatusAPI.getStatus()
        fridgeStatus.value = response.data
        // 保存到本地
        saveToStorage('fridge-status', response.data)
      } catch (error) {
        console.error('加载冰箱状态失败:', error)
        // 从 localStorage 加载或使用默认值
        fridgeStatus.value = loadFromStorage('fridge-status', {
          energyLevel: 62,
          socialLevel: 24,
          creativityLevel: 88,
          todayDrink: '冰美式',
          todayKeyword: '缓慢重启',
          todayNote: '不要问，问就是正在加载。'
        })
      }
    }

    // 创建新便签
    const handleCreateNote = async (noteData) => {
      try {
        const response = await noteAPI.createNote(noteData)
        notes.value.unshift(response.data)
        saveToStorage('notes-cache', notes.value)
        showNoteModal.value = false
      } catch (error) {
        console.error('创建便签失败:', error)
        alert('糟糕，贴不上去！')
      }
    }

    // 增加磁铁
    const handleAddMagnet = async (noteId) => {
      try {
        const response = await noteAPI.addMagnet(noteId)
        const note = notes.value.find(n => n.id === noteId)
        if (note && response.data.magnetCount !== undefined) {
          note.magnetCount = response.data.magnetCount
        }
      } catch (error) {
        console.error('添加磁铁失败:', error)
      }
    }

    // 更新位置
    const handleUpdatePosition = async (noteId, x, y) => {
      try {
        await noteAPI.updatePosition(noteId, x, y)
        const note = notes.value.find(n => n.id === noteId)
        if (note) {
          note.positionX = x
          note.positionY = y
        }
        // 保存到本地
        saveToStorage(`note-pos-${noteId}`, { x, y })
      } catch (error) {
        console.error('更新位置失败:', error)
      }
    }

    // 删除便签
    const handleDeleteNote = async (noteId) => {
      try {
        await noteAPI.deleteNote(noteId)
        notes.value = notes.value.filter(n => n.id !== noteId)
        saveToStorage('notes-cache', notes.value)
      } catch (error) {
        console.error('删除便签失败:', error)
      }
    }

    // 更新冰箱状态
    const handleUpdateStatus = async (statusData) => {
      try {
        const response = await fridgeStatusAPI.updateStatus(statusData)
        fridgeStatus.value = response.data
        saveToStorage('fridge-status', response.data)
        showStatusModal.value = false
      } catch (error) {
        console.error('更新冰箱状态失败:', error)
        alert('更新失败，请重试')
      }
    }

    // 初始化
    onMounted(() => {
      loadNotes()
      loadFridgeStatus()
    })

    // 自动刷新便签列表
    const refreshInterval = setInterval(() => {
      loadNotes()
    }, 10000)

    return {
      notes,
      fridgeStatus,
      showNoteModal,
      showStatusModal,
      loading,
      handleCreateNote,
      handleAddMagnet,
      handleUpdatePosition,
      handleDeleteNote,
      handleUpdateStatus
    }
  }
}
</script>

<style scoped>
.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-header {
  padding: 40px 20px;
  text-align: center;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(200, 200, 200, 0.2);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
}

.title-section {
  animation: fadeInDown 0.6s ease;
}

.main-title {
  font-size: 48px;
  font-weight: 700;
  color: #5a5a5a;
  margin-bottom: 10px;
  letter-spacing: 2px;
}

.subtitle {
  font-size: 18px;
  color: #9a9a9a;
  font-weight: 400;
  letter-spacing: 1px;
}

.app-main {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
}

.fridge-container {
  position: relative;
  width: 100%;
  max-width: 900px;
}

.add-note-btn {
  position: fixed;
  bottom: 40px;
  right: 40px;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #ff9999, #ffcc99);
  color: white;
  font-size: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 20px rgba(255, 153, 153, 0.3);
  z-index: 100;
  transition: all 0.3s ease;
}

.add-note-btn:hover {
  transform: scale(1.1) translateY(-5px);
  box-shadow: 0 12px 30px rgba(255, 153, 153, 0.4);
}

.add-note-btn:active {
  transform: scale(0.95);
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 768px) {
  .main-title {
    font-size: 36px;
  }

  .subtitle {
    font-size: 16px;
  }

  .add-note-btn {
    width: 50px;
    height: 50px;
    bottom: 30px;
    right: 30px;
    font-size: 20px;
  }
}
</style>
