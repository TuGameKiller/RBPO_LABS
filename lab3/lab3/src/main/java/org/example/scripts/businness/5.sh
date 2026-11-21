#!/bin/bash
curl -i -X POST http://localhost:8080/api/waiting-requests/enqueue \
  -H "Content-Type: application/json" \
  -d '{"bookEditionId":1,"userId":2,"requestDate":"2026-11-21"}'