#!/bin/bash
curl -X POST http://localhost:8080/api/loans \
  -u test:a1234567 \
  -H "Content-Type: application/json" \
  -d '{"copyId": 1, "userId": 2}'