#!/bin/bash
curl -X PATCH http://localhost:8080/api/waiting-requests/4 \
  -H "Content-Type: application/json" \
  -d '{"status": "FULFILLED"}'