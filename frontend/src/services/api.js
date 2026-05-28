import axios from 'axios'

const API_URL = 'http://localhost:8080/api'

const api = axios.create({
    baseURL: API_URL,
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// 便签相关 API
export const noteAPI = {
    // 获取所有便签
    getAllNotes: () => api.get('/notes'),

    // 创建新便签
    createNote: (noteData) => api.post('/notes', noteData),

    // 获取单个便签
    getNoteById: (id) => api.get(`/notes/${id}`),

    // 增加磁铁
    addMagnet: (id) => api.post(`/notes/${id}/magnet`),

    // 更新位置
    updatePosition: (id, x, y) => api.put(`/notes/${id}/position`, {}, {
        params: { x, y }
    }),

    // 删除便签
    deleteNote: (id) => api.delete(`/notes/${id}`)
}

// 冰箱状态相关 API
export const fridgeStatusAPI = {
    // 获取冰箱状态
    getStatus: () => api.get('/fridge-status'),

    // 更新冰箱状态
    updateStatus: (statusData) => api.put('/fridge-status', statusData),

    // 初始化状态
    initialize: () => api.post('/fridge-status/initialize')
}

export default api
