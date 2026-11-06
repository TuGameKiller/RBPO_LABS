#!/bin/bash
curl -X PATCH http://localhost:8080/api/copies/4 \
  -H "Content-Type: application/json" \
  -d '{"status": "AVAILABLE"}'