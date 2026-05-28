<template>
  <div class="modal-overlay" @click="handleOverlayClick">
    <div class="modal">
      <div class="modal-header">
        <h2>编辑今日状态</h2>
        <button class="modal-close" @click="$emit('close')">✕</button>
      </div>

      <div class="modal-body">
        <!-- 精神电量 -->
        <div class="form-group">
          <label for="energy">精神电量</label>
          <div class="slider-group">
            <input 
              id="energy"
              v-model.number="form.energyLevel"
              type="range"
              min="0"
              max="100"
              step="5"
              class="slider"
            />
            <span class="slider-value">{{ form.energyLevel }}%</span>
          </div>
        </div>

        <!-- 社交电量 -->
        <div class="form-group">
          <label for="social">社交电量</label>
          <div class="slider-group">
            <input 
              id="social"
              v-model.number="form.socialLevel"
              type="range"
              min="0"
              max="100"
              step="5"
              class="slider"
            />
            <span class="slider-value">{{ form.socialLevel }}%</span>
          </div>
        </div>

        <!-- 创作欲 -->
        <div class="form-group">
          <label for="creativity">创作欲</label>
          <div class="slider-group">
            <input 
              id="creativity"
              v-model.number="form.creativityLevel"
              type="range"
              min="0"
              max="100"
              step="5"
              class="slider"
            />
            <span class="slider-value">{{ form.creativityLevel }}%</span>
          </div>
        </div>

        <!-- 今日饮品 -->
        <div class="form-group">
          <label for="drink">今日饮品</label>
          <input 
            id="drink"
            v-model="form.todayDrink"
            type="text"
            placeholder="冰美式、温水、奶茶……"
            class="text-input"
            maxlength="20"
          />
        </div>

        <!-- 今日关键词 -->
        <div class="form-group">
          <label for="keyword">今日关键词</label>
          <input 
            id="keyword"
            v-model="form.todayKeyword"
            type="text"
            placeholder="缓慢重启、摸鱼、冲浪……"
            class="text-input"
            maxlength="20"
          />
        </div>

        <!-- 今日备注 -->
        <div class="form-group">
          <label for="note">今日备注</label>
          <textarea 
            id="note"
            v-model="form.todayNote"
            placeholder="不要问，问就是正在加载。"
            rows="3"
            class="textarea-input"
            maxlength="100"
          ></textarea>
          <div class="char-count">{{ form.todayNote.length }}/100</div>
        </div>
      </div>

      <div class="modal-footer">
        <button class="btn-cancel" @click="$emit('close')">取消</button>
        <button class="btn-submit" @click="handleSubmit">
          保存
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive } from 'vue'

export default {
  name: 'StatusModal',
  props: {
    status: {
      type: Object,
      required: true
    }
  },
  emits: ['update', 'close'],
  setup(props, { emit }) {
    const form = reactive({
      energyLevel: props.status?.energyLevel || 50,
      socialLevel: props.status?.socialLevel || 50,
      creativityLevel: props.status?.creativityLevel || 50,
      todayDrink: props.status?.todayDrink || '',
      todayKeyword: props.status?.todayKeyword || '',
      todayNote: props.status?.todayNote || ''
    })

    const handleOverlayClick = (e) => {
      if (e.target === e.currentTarget) {
        emit('close')
      }
    }

    const handleSubmit = () => {
      const statusData = {
        id: props.status?.id,
        energyLevel: form.energyLevel,
        socialLevel: form.socialLevel,
        creativityLevel: form.creativityLevel,
        todayDrink: form.todayDrink.trim() || '未设置',
        todayKeyword: form.todayKeyword.trim() || '未设置',
        todayNote: form.todayNote.trim() || '未设置'
      }

      emit('update', statusData)
    }

    return {
      form,
      handleOverlayClick,
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

.slider-group {
  display: flex;
  gap: 12px;
  align-items: center;
}

.slider {
  flex: 1;
  height: 8px;
  border-radius: 4px;
  background: linear-gradient(to right, #ff9999, #ffcc99);
  outline: none;
  -webkit-appearance: none;
  appearance: none;
  cursor: pointer;
}

.slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: white;
  border: 3px solid #ffcc99;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

.slider::-webkit-slider-thumb:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.slider::-moz-range-thumb {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: white;
  border: 3px solid #ffcc99;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

.slider::-moz-range-thumb:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.slider-value {
  min-width: 45px;
  text-align: right;
  font-weight: 600;
  color: #ffcc99;
  font-size: 14px;
}

.text-input {
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

.textarea-input {
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
  background: linear-gradient(135deg, #7cb9e8, #a8d8ea);
  color: white;
}

.btn-submit:hover {
  background: linear-gradient(135deg, #6aacdb, #96d0e2);
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(124, 185, 232, 0.3);
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

  .modal-footer {
    flex-direction: column;
    padding: 16px;
  }
}
</style>
