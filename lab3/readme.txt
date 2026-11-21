# Library REST API

Бэкенд-сервис библиотеки на Spring Boot, JPA и PostgreSQL.

## Сущности
- `User` (`users`): `username`, `password`, `role`, `status` (`ENABLED` / `DISABLED`)
- `BookEdition` (`book_editions`): `title`, `author`, `isbn`
- `Copy` (`copies`): `bookEdition`, `inventoryNumber`, `status` (`AVAILABLE`, `BORROWED`, `WRITTEN_OFF`)
- `Loan` (`loans`): `copy`, `user`, `loanDate`, `returnDate`
- `WaitingRequest` (`waiting_requests`): `bookEdition`, `user`, `requestDate`, `status` (`PENDING`, `FULFILLED`)

## Эндпоинты
- **CRUD (`GET`, `POST`, `PATCH`, `DELETE`):** `/api/book-editions`, `/api/users`, `/api/copies`, `/api/loans`, `/api/waiting-requests`
- **Бизнес-операции:**
  1. `POST /api/loans/issue` — выдача экземпляра (`copy -> BORROWED`)
  2. `POST /api/loans/{id}/return` — возврат с автопередачей по очереди (`PENDING -> FULFILLED` и новая выдача либо `copy -> AVAILABLE`)
  3. `POST /api/loans/{id}/extend` — продление выдачи при отсутствии очереди `PENDING`
  4. `POST /api/copies/{id}/write-off` — списание экземпляра (`WRITTEN_OFF`), если он не `BORROWED`
  5. `POST /api/waiting-requests/enqueue` — постановка в очередь `PENDING`, если нет свободных `AVAILABLE`-копий и дублей заявки

## Запуск и проверка
1. Настройте `.env`, конфиг приложения и `src/main/resources/schema.sql` (с ролями и `CHECK`-ограничениями в PostgreSQL).
2. Запустите приложение.
