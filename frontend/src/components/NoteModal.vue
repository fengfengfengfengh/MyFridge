<template>
  <div class="modal-overlay" @click="handleOverlayClick">
    <div class="modal">
      <div class="modal-header">
        <h2>你想贴点什么？</h2>
        <button class="modal-close" @click="$emit('close')">✕</button>
      </div>

      <div class="modal-body">
        <!-- 便签内容输入 -->
        <div class="form-group">
          <label for="content">💭 你的想法</label>
          <textarea 
            id="content"
            v-model="form.content"
            placeholder="碎碎念、废话、祝福、到此一游……来吧！"
            rows="5"
            class="textarea-input"
            maxlength="200"
          ></textarea>
          <div class="char-count">{{ form.content.length }}/200</div>
        </div>

        <!-- 昵称 -->
        <div class="form-group">
          <label for="nickname">🐧 你叫什么</label>
          <div class="nickname-row">
            <input 
              id="nickname"
              v-model="form.nickname"
              type="text"
              placeholder="不填会随机生成昵称"
              class="text-input"
              maxlength="20"
            />
            <button class="random-nickname-btn" @click="generateNickname">
              🎲 随机
            </button>
          </div>
        </div>

        <!-- 颜色选择 -->
        <div class="form-group">
          <label>🎨 颜色</label>
          <div class="color-picker">
            <button 
              v-for="color in noteColors"
              :key="color.name"
              class="color-option"
              :class="{ selected: form.color === color.name }"
              :style="{ backgroundColor: color.bg, color: color.text, borderColor: color.text }"
              @click="form.color = color.name"
              :title="color.label"
            >
              {{ color.label }}
            </button>
          </div>
        </div>

        <!-- 贴纸选择 -->
        <div class="form-group">
          <label>✨ 贴纸</label>
          <div class="sticker-picker">
            <button 
              v-for="icon in stickerIcons"
              :key="icon"
              class="sticker-option"
              :class="{ selected: form.icon === icon }"
              @click="form.icon = icon"
            >
              {{ icon }}
            </button>
          </div>
        </div>

        <!-- 保鲜期选择 -->
        <div class="form-group">
          <label for="retention">⏰ 保鲜期</label>
          <select id="retention" v-model="form.retention" class="select-input">
            <option value="permanent">永久保存</option>
            <option value="30days">30天</option>
            <option value="7days">7天</option>
            <option value="24hours">24小时</option>
          </select>
        </div>
      </div>

      <div class="modal-footer">
        <button class="btn-cancel" @click="$emit('close')">再想想</button>
        <button class="btn-submit" @click="handleSubmit" :disabled="!form.content.trim()">
          贴上去
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive } from 'vue'
import { generateRandomNickname, noteColors, stickerIcons } from '../utils/helpers'

export default {
  name: 'NoteModal',
  emits: ['create', 'close'],
  setup(props, { emit }) {
    const form = reactive({
      content: '',
      nickname: '',
      color: 'cream',
      icon: '💌',
      retention: 'permanent'
    })

    const handleOverlayClick = (e) => {
      if (e.target === e.currentTarget) {
        emit('close')
      }
    }

    const generateNickname = () => {
      form.nickname = generateRandomNickname()
    }

    const handleSubmit = () => {
      if (!form.content.trim()) {
        alert('请输入点东西呀！')
        return
      }

      const nickname = form.nickname.trim() || generateRandomNickname()
      
      const noteData = {
        content: form.content.trim(),
        nickname,
        color: form.color,
        icon: form.icon,
        magnetCount: 0
      }

      emit('create', noteData)

      // 重置表单
      form.content = ''
      form.nickname = ''
      form.color = 'cream'
      form.icon = '💌'
      form.retention = 'permanent'
    }

    return {
      form,
      noteColors,
      stickerIcons,
      handleOverlayClick,
      generateNickname,
      handleSubmit
    }
  }
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

.modal {
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.25);
  max-width: 500px;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
  animation: slideUp 0.3s ease;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  border-bottom: 2px solid #f0f0f0;
}

.modal-header h2 {
  margin: 0;
  font-size: 24px;
  color: #5a5a5a;
}

.modal-close {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
  transition: all 0.3s ease;
}

.modal-close:hover {
  color: #ff9999;
  transform: rotate(90deg);
}

.modal-body {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-weight: 600;
  color: #5a5a5a;
  font-size: 14px;
}

.textarea-input {
  width: 100%;
  padding: 12px;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  font-size: 14px;
  font-family: inherit;
  resize: vertical;
  transition: all 0.3s ease;
}

.textarea-input:focus {
  outline: none;
  border-color: #ffcc99;
  box-shadow: 0 0 0 4px rgba(255, 204, 153, 0.1);
}

.char-count {
  font-size: 12px;
  color: #999;
  text-align: right;
}

.nickname-row {
  display: flex;
  gap: 8px;
}

.text-input {
  flex: 1;
  padding: 12px;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  font-size: 14px;
  font-family: inherit;
  transition: all 0.3s ease;
}

.text-input:focus {
  outline: none;
  border-color: #ffcc99;
  box-shadow: 0 0 0 4px rgba(255, 204, 153, 0.1);
}

.random-nickname-btn {
  padding: 12px 16px;
  background: linear-gradient(135deg, #fff0e6, #ffe6d5);
  border: 2px solid #ffcc99;
  border-radius: 12px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.random-nickname-btn:hover {
  background: linear-gradient(135deg, #ffe6d5, #ffccbb);
  transform: translateY(-2px);
}

.color-picker {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(80px, 1fr));
  gap: 8px;
}

.color-option {
  padding: 12px;
  border: 3px solid transparent;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.color-option.selected {
  border-color: #333;
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.color-option:hover {
  transform: translateY(-2px);
}

.sticker-picker {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 8px;
}

.sticker-option {
  padding: 12px;
  background: #f5f5f5;
  border: 3px solid transparent;
  border-radius: 12px;
  font-size: 24px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.sticker-option.selected {
  background: rgba(255, 204, 153, 0.2);
  border-color: #ffcc99;
  transform: scale(1.1);
}

.sticker-option:hover {
  background: rgba(255, 204, 153, 0.3);
  transform: translateY(-2px);
}

.select-input {
  padding: 12px;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  font-size: 14px;
  font-family: inherit;
  cursor: pointer;
  transition: all 0.3s ease;
}

.select-input:focus {
  outline: none;
  border-color: #ffcc99;
  box-shadow: 0 0 0 4px rgba(255, 204, 153, 0.1);
}

.modal-footer {
  display: flex;
  gap: 12px;
  padding: 24px;
  border-top: 2px solid #f0f0f0;
  background: #f9f9f9;
}

.btn-cancel,
.btn-submit {
  flex: 1;
  padding: 14px;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-cancel {
  background: #e8e8e8;
  color: #5a5a5a;
}

.btn-cancel:hover {
  background: #d0d0d0;
  transform: translateY(-2px);
}

.btn-submit {
  background: linear-gradient(135deg, #ff9999, #ffcc99);
  color: white;
}

.btn-submit:hover:not(:disabled) {
  background: linear-gradient(135deg, #ff7777, #ffbb77);
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(255, 153, 153, 0.3);
}

.btn-submit:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideUp {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

@media (max-width: 600px) {
  .modal {
    border-radius: 16px;
    width: 95%;
  }

  .modal-header {
    padding: 20px;
  }

  .modal-header h2 {
    font-size: 20px;
  }

  .modal-body {
    padding: 20px;
    gap: 16px;
  }

  .color-picker {
    grid-template-columns: repeat(3, 1fr);
  }

  .sticker-picker {
    grid-template-columns: repeat(5, 1fr);
  }

  .modal-footer {
    flex-direction: column;
    padding: 16px;
  }
}
</style>
