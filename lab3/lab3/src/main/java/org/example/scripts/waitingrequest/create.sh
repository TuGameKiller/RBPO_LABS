#!/bin/bash
curl -X POST http://localhost:8080/api/waiting-requests \
  -H "Content-Type: application/json" \
  -d '{"bookEditionId": 13, "userId": 2}'