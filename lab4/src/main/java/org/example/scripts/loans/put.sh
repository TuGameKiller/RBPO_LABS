#!/bin/bash
curl -X PUT http://localhost:8080/api/loans/1 \
  -u admin:admin12345 \
  -H "Content-Type: application/json" \
  -d '{"returnDate": "2026-10-26"}'