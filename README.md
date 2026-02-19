# Code Review Agent - Getting Started

## 1. Infrastructure Setup
먼저 Docker를 사용하여 PostgreSQL과 Redis를 실행합니다.
```bash
docker-compose up -d
```
* PostgreSQL: `localhost:5432` (User: `user`, Pass: `password`, DB: `codereview`)
* Redis: `localhost:6379`

## 2. Backend Setup (Spring Boot)
1. `backend/src/main/resources/application.properties`에서 설정을 확인합니다.
2. Groq API Key를 설정하려면 다음 환경변수를 설정하거나 properties에 추가하세요:
   - `groq.api.key=YOUR_API_KEY`
3. 백엔드 실행:
```bash
cd backend
./gradlew bootRun
```

## 3. Frontend Setup (Next.js)
1. `frontend/.env.local` 파일에 GitHub OAuth 정보를 입력합니다.
   - GitHub Developer Settings에서 OAuth App을 생성하고 `http://localhost:3000/api/auth/callback/github`를 Callback URL로 등록하세요.
2. 의존성 설치 및 실행:
```bash
cd frontend
npm install
npm run dev
```

## 4. How to Test (Webhook)
로컬에서 Webhook 이벤트를 테스트하려면 다음 `curl` 명령어를 사용할 수 있습니다 (PR 오픈 시뮬레이션):
```bash
curl -X POST http://localhost:8080/api/webhook/github 
  -H "Content-Type: application/json" 
  -H "X-GitHub-Event: pull_request" 
  -d '{
    "action": "opened",
    "pull_request": {
      "id": 12345,
      "number": 1,
      "title": "Test PR",
      "user": { "login": "testuser" }
    },
    "repository": {
      "id": 99999,
      "full_name": "owner/repo"
    }
  }'
```
*(주의: DB에 해당 레포지토리 ID가 `is_active=true` 상태로 존재해야 워커가 동작합니다.)*
