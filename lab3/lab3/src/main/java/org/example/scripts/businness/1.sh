#!/bin/bash
curl -i -X POST http://localhost:8080/api/loans/issue \
  -H "Content-Type: application/json" \
  -d '{"copyId":1,"userId":1,"loanDate":"2026-11-21"}'