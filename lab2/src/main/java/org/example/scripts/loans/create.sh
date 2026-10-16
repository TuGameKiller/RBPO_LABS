#!/bin/bash
curl -X POST http://localhost:8080/api/loans \
  -H "Content-Type: application/json" \
  -d '{"copyId": 1, "userId": 1}'