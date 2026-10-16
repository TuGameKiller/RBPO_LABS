#!/bin/bash
curl -X PATCH http://localhost:8080/api/copies/2 \
  -H "Content-Type: application/json" \
  -d '{"status": "SOSAIT"}'