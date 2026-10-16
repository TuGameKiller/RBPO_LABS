#!/bin/bash
curl -X PATCH http://localhost:8080/api/loans/2 \
  -H "Content-Type: application/json" \
  -d '{"returnDate": "2026-11-01"}'