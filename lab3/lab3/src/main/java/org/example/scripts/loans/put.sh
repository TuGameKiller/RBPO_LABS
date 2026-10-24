#!/bin/bash
curl -X PUT http://localhost:8080/api/loans/8 \
  -H "Content-Type: application/json" \
  -d '{"returnDate": "2026-10-26"}'