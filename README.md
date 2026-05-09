# Course Assistant Agent

Course assistant agent project aligned with the SpringAI tutorial style:
- `ChatClient + Advisor` orchestration
- `RAG (VectorStore + QuestionAnswerAdvisor)`
- file-based conversation memory
- full-stack: Spring Boot + MySQL + Redis + Vue

## Structure
- `backend`: Spring Boot API service
- `frontend`: Vue web app
- `docs`: architecture and API notes

## Quick Start

### 1) Start infra
```bash
docker compose up -d mysql redis
```

### 2) Run backend
```bash
cd backend
mvn spring-boot:run
```

### 3) Run frontend
```bash
cd frontend
npm install
npm run dev
```

## What Was Migrated From yu-ai-agent Style
- `CourseAssistantApp`: app-level AI capability encapsulation
- `MyLoggerAdvisor`: prompt/response logging
- `FileBasedChatMemory`: local persistent memory per session
- `CourseDocumentLoader + CourseVectorStoreConfig`: markdown + DB document loading and vectorization
- `QuestionAnswerAdvisor`: RAG answer generation path

## Next Upgrade Suggestions
- Replace `SimpleVectorStore` with `pgvector` for persistence and recall quality
- Add JWT + RBAC
- Add PDF/DOCX parser pipeline
