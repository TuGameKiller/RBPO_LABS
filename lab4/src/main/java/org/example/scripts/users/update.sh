#!/bin/bash
curl -X PATCH http://localhost:8080/api/users/2 \
  -u test:a1234567 \
  -H "Content-Type: application/json" \
  -d '{"name": "Иван Петров"}'