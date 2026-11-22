#!/bin/bash
curl -i -X POST http://localhost:8080/api/loans/5/return \
  -H "Content-Type: application/json" \
  -d '{"date":"2026-11-21"}'