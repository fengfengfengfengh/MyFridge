// 随机昵称列表
const randomNicknames = [
    '路过的电子企鹅',
    '冰箱里的半颗柠檬',
    '没有感情的饭团',
    '过期但乐观的酸奶',
    '宇宙便利店店员',
    '夜班便利店幽灵',
    '被冻住的小番茄',
    '匿名冰箱贴客',
    '来自未来的冰淇淋',
    '某个午后的梦游者',
    '冰晶堆里的秘密',
    '记忆中的温度'
]

// 生成随机昵称
export const generateRandomNickname = () => {
    return randomNicknames[Math.floor(Math.random() * randomNicknames.length)]
}

// 便签颜色配置
export const noteColors = [
    { name: 'cream', label: '奶油黄', bg: '#fffacd', text: '#8b8b00' },
    { name: 'pink', label: '草莓粉', bg: '#ffe6f0', text: '#c41e8c' },
    { name: 'mint', label: '薄荷绿', bg: '#e0f8f7', text: '#008080' },
    { name: 'blue', label: '天空蓝', bg: '#e0f4ff', text: '#0066cc' },
    { name: 'purple', label: '葡萄紫', bg: '#f3e5ff', text: '#6b47b5' },
    { name: 'white', label: '纯白', bg: '#f5f5f5', text: '#333' }
]

// 贴纸图标列表
export const stickerIcons = ['🍅', '🧊', '🐱', '⭐', '🍙', '🥛', '🍓', '🐧', '🌙', '💌']

// 格式化时间 - 相对时间
export const formatRelativeTime = (dateString) => {
    const date = new Date(dateString)
    const now = new Date()
    const diff = now - date

    const minutes = Math.floor(diff / 60000)
    const hours = Math.floor(diff / 3600000)
    const days = Math.floor(diff / 86400000)

    if (minutes < 1) return '刚刚'
    if (minutes < 60) return `${minutes} 分钟前`
    if (hours < 24) return `${hours} 小时前`
    if (days < 7) return `${days} 天前`
    
    return date.toLocaleDateString('zh-CN')
}

// 冰箱说话文案列表
export const fridgeSayings = {
    empty: [
        '今天有点安静，连嗡嗡声都变小了。',
        '冰箱门还空着。要不要贴下第一张便签？'
    ],
    newNote: [
        '刚刚有人贴了一张新便签，我假装没偷看。',
        '嘘，新来的便签正在融入我们这个小社区。'
    ],
    many: [
        '门上快贴不下了，但我还能撑。',
        '这么多便签，我都记不清谁来过了。',
        '满满的回忆，我的内存即将爆满。'
    ],
    lowEnergy: [
        '已记录主人的精神温度，建议今日低功耗运行。',
        '主人看起来有点累，我也降低一下嗡嗡声。'
    ],
    random: [
        '冰箱说：今天也要好好吃饭。',
        '冰箱说：我负责保鲜，你负责开心一点。',
        '冰箱说：不要把坏心情放太久，会串味。',
        '冰箱说：刚才有一张便签偷偷笑了一下。',
        '冰箱说：这里可以存放废话，也可以存放一点点勇气。',
        '冰箱说：冷冷的世界里，我用 -18 度的温度给予拥抱。',
        '冰箱说：每一张便签都是一个小故事。',
        '冰箱说：没什么，就是开门的时候想到了你。',
        '冰箱说：食材会过期，但记忆不会。',
        '冰箱说：嗡嗡嗡，这是我最温柔的语言。'
    ]
}

// 获取冰箱说话内容
export const getFridgeSaying = (notesCount, energyLevel = 50) => {
    let sayings = [...fridgeSayings.random]
    
    if (notesCount === 0) {
        return fridgeSayings.empty[Math.floor(Math.random() * fridgeSayings.empty.length)]
    }
    
    if (notesCount === 1) {
        sayings = [...sayings, ...fridgeSayings.newNote]
    }
    
    if (notesCount > 10) {
        sayings = [...sayings, ...fridgeSayings.many]
    }
    
    if (energyLevel < 30) {
        sayings = [...sayings, ...fridgeSayings.lowEnergy]
    }
    
    return sayings[Math.floor(Math.random() * sayings.length)]
}

// 保存到 localStorage
export const saveToStorage = (key, value) => {
    localStorage.setItem(key, JSON.stringify(value))
}

// 从 localStorage 读取
export const loadFromStorage = (key, defaultValue = null) => {
    const item = localStorage.getItem(key)
    return item ? JSON.parse(item) : defaultValue
}

// 生成随机位置
export const generateRandomPosition = () => {
    return {
        x: Math.random() * 80,
        y: Math.random() * 60
    }
}

// 根据颜色名称获取颜色配置
export const getColorConfig = (colorName) => {
    return noteColors.find(c => c.name === colorName) || noteColors[0]
}
