#!/bin/bash
curl -X POST http://localhost:8080/api/waiting-requests \
  -H "Content-Type: application/json" \
  -d '{"bookEditionId": 1, "userId": 1}'