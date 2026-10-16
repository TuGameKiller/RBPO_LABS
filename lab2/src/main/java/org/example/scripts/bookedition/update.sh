#!/bin/bash
curl -X PATCH http://localhost:8080/api/book-editions/3 \
  -H "Content-Type: application/json" \
  -d '{"title": "ЕВгений Онегин"}'
