<template>
  <main class="page">
    <section class="panel docs">
      <h2>课程资料管理</h2>
      <form class="doc-form" @submit.prevent="createDocument">
        <input v-model="doc.title" placeholder="资料标题" required />
        <input v-model="doc.source" placeholder="资料来源（课程名/章节）" required />
        <textarea v-model="doc.content" placeholder="粘贴课程内容" rows="6" required />
        <button :disabled="loadingDoc">{{ loadingDoc ? '提交中...' : '新增资料' }}</button>
      </form>

      <ul class="doc-list">
        <li v-for="d in documents" :key="d.id">
          <strong>{{ d.title }}</strong>
          <span>{{ d.source }}</span>
        </li>
      </ul>
    </section>

    <section class="panel chat">
      <h2>课程助教 Agent</h2>
      <div class="messages">
        <div v-for="(m, idx) in messages" :key="idx" :class="['message', m.role]">
          <p>{{ m.text }}</p>
          <small v-if="m.citations?.length">参考: {{ m.citations.join(' | ') }}</small>
        </div>
      </div>

      <form class="chat-form" @submit.prevent="ask">
        <input v-model="question" placeholder="输入课程问题..." required />
        <button :disabled="loadingChat">{{ loadingChat ? '思考中...' : '提问' }}</button>
      </form>
    </section>
  </main>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { api } from './api/client'

const sessionId = `session-${Date.now()}`
const question = ref('')
const loadingChat = ref(false)
const loadingDoc = ref(false)
const documents = ref([])
const messages = ref([
  { role: 'assistant', text: '你好，我是课程助教 Agent。请先录入课程资料后提问。' }
])

const doc = reactive({ title: '', source: '', content: '' })

async function loadDocuments() {
  const res = await api.get('/documents')
  documents.value = res.data
}

async function createDocument() {
  loadingDoc.value = true
  try {
    await api.post('/documents', { ...doc })
    doc.title = ''
    doc.source = ''
    doc.content = ''
    await loadDocuments()
  } finally {
    loadingDoc.value = false
  }
}

async function ask() {
  const q = question.value.trim()
  if (!q) return
  messages.value.push({ role: 'user', text: q })
  question.value = ''
  loadingChat.value = true

  try {
    const res = await api.post('/chat/ask', { sessionId, question: q })
    messages.value.push({
      role: 'assistant',
      text: res.data.answer,
      citations: res.data.citations
    })
  } catch (e) {
    messages.value.push({ role: 'assistant', text: '请求失败，请检查后端服务。' })
  } finally {
    loadingChat.value = false
  }
}

onMounted(loadDocuments)
</script>
