# Course Assistant Agent

A production-oriented starter for a course assistant agent system based on:
- Spring Boot + Spring AI + MySQL + Redis + Vector Retrieval abstraction
- Vue 3 + Vite frontend

## Structure
- `backend`: Spring Boot API service
- `frontend`: Vue web app
- `docs`: architecture and design docs

## Quick Start

### 1) Start infra (MySQL + Redis)
```bash
docker compose up -d mysql redis
```

### 2) Run backend
```bash
cd backend
./mvnw spring-boot:run
```

### 3) Run frontend
```bash
cd frontend
npm install
npm run dev
```

## Notes
- Current vector retrieval uses an in-memory implementation for local run.
- You can switch to pgvector/Milvus by implementing `VectorStoreService`.
