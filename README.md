# ATM App

Backend-сервис ATM на `Spring Boot` + `PostgreSQL` с CRUD API для аккаунтов.
Проект содержит:
- приложение (`backend`)
- docker-окружение для локального запуска
- `infra` (Terraform + Ansible) для деплоя

## Технологии
- Java 21
- Spring Boot 4
- Spring Data JPA
- PostgreSQL
- Docker / Docker Compose
- JUnit 5 + Mockito + MockMvc + JaCoCo

## Структура проекта
```text
atm-app/
  backend/           # Spring Boot backend
  infra/terraform/   # IaC для VM в Yandex Cloud
  infra/ansible/     # Установка Docker и деплой backend
  docker-compose.yml # Локальный запуск postgres + backend
```

## Быстрый старт (Docker)
1. Скопируй переменные окружения:
```bash
cp .env.example .env
```

2. Запусти сервисы:
```bash
docker compose up --build -d
```

3. Проверка:
```bash
curl http://localhost:8084/api/v1/atm
```

## Локальный запуск backend (без контейнера backend)
Можно поднять только БД в Docker и запустить backend локально:

```bash
# 1) поднять postgres
docker compose up -d postgres

# 2) запустить backend
cd backend
DB_URL=jdbc:postgresql://localhost:5433/atm_service \
DB_USERNAME=atm \
DB_PASSWORD=change_me \
./gradlew bootRun
```

`application.yml` читает переменные `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.

## API
Base URL: `http://localhost:8084/api/v1/atm`

### 1. Создать аккаунт
```bash
curl -i -X POST "http://localhost:8084/api/v1/atm" \
  -H "Content-Type: application/json" \
  -d '{
    "ownerName": "Ivan Petrov",
    "balance": 1500.75,
    "currency": "rub"
  }'
```

### 2. Получить все аккаунты
```bash
curl -i "http://localhost:8084/api/v1/atm"
```

### 3. Получить аккаунт по ID
```bash
curl -i "http://localhost:8084/api/v1/atm/1"
```

### 4. Частично обновить аккаунт
```bash
curl -i -X PATCH "http://localhost:8084/api/v1/atm/1" \
  -H "Content-Type: application/json" \
  -d '{
    "ownerName": "Ivan Updated",
    "balance": 2000.00,
    "currency": "usd",
    "accountStatus": "frozen"
  }'
```

### 5. Удалить аккаунт
```bash
curl -i -X DELETE "http://localhost:8084/api/v1/atm/1"
```

## Тесты и покрытие
Из папки `backend`:

```bash
./gradlew clean test jacocoTestReport jacocoTestCoverageVerification
```

Быстрый вывод итогового покрытия:
```bash
./gradlew printCoverage
```

HTML-отчёт JaCoCo:
`backend/build/reports/jacoco/test/html/index.html`

## CI
Backend CI запускается через GitHub Actions:
- `.github/workflows/ci-backend.yml`
- проверяет сборку, тесты и порог покрытия (`>= 80%`)

## Infra
- Terraform: `infra/terraform/README.md`
- Ansible: `infra/ansible/README.md`

## License
MIT (см. `LICENSE`).
